package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.ModifyPasswordContract;
import me.jessyan.mvparms.demo.mvp.model.entity.response.RegisterResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.ModifyUserInfoRequest;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class ModifyPasswordPresenter extends BasePresenter<ModifyPasswordContract.Model, ModifyPasswordContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;

    @Inject
    public ModifyPasswordPresenter(ModifyPasswordContract.Model model, ModifyPasswordContract.View rootView) {
        super(model, rootView);
    }

    public void modifyPassword() {
        String old = (String) mRootView.getCache().get("old");
        String newly = (String) mRootView.getCache().get("new");
        String confirm = (String) mRootView.getCache().get("confirm");

        if (ArmsUtils.isEmpty(old)) {
            mRootView.showMessage("请输入原密码");
            return;
        }
        if (ArmsUtils.isEmpty(newly)) {
            mRootView.showMessage("请输入新密码");
            return;
        }
        if (ArmsUtils.isEmpty(confirm)) {
            mRootView.showMessage("请输入确认密码");
            return;
        }

        if (!newly.equals(confirm)) {
            mRootView.showMessage("确认不一致");
            return;
        }

        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        ModifyUserInfoRequest request = new ModifyUserInfoRequest();
        request.setCmd(1103);
        request.setOldUserPwd(old);
        request.setNewUserPwd(newly);
        request.setConfirmUserPwd(confirm);
        request.setToken(String.valueOf(cache.get(KEY_KEEP + "token")));
        mModel.modifyPassword(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RegisterResponse>() {
                    @Override
                    public void accept(RegisterResponse response) throws Exception {
                        if (response.isSuccess()) {
                            cacheUserInfo(response.getToken(), response.getSignkey());
                            mRootView.showMessage(response.getRetDesc());
                            mRootView.killMyself();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });

    }

    private void cacheUserInfo(String token, String signkey) {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        cache.put(KEY_KEEP + "token", token);
        cache.put(KEY_KEEP + "signkey", signkey);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

}
