package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.ModifyContract;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ModifyRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class ModifyPresenter extends BasePresenter<ModifyContract.Model, ModifyContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;

    @Inject
    public ModifyPresenter(ModifyContract.Model model, ModifyContract.View rootView) {
        super(model, rootView);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void modify(String password, String confirmPassword) {
        ModifyRequest request = new ModifyRequest();
        request.setPassword(password);
        request.setConfirmPassword(confirmPassword);
        request.setToken((String) ArmsUtils.obtainAppComponentFromContext(mApplication).extras().get(KEY_KEEP + "token"));

        mModel.modify(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse baseResponse) throws Exception {
                        mRootView.showMessage(baseResponse.getRetDesc());
                        if (baseResponse.isSuccess()) {
                            mRootView.killMyself();
                        }
                    }
                });
    }

}
