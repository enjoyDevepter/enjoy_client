package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.PermissionUtil;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.LoginContract;
import me.jessyan.mvparms.demo.mvp.model.entity.request.LoginByPhoneRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.LoginByUserRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.VeritfyRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.RegisterResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class LoginPresenter extends BasePresenter<LoginContract.Model, LoginContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;

    @Inject
    public LoginPresenter(LoginContract.Model model, LoginContract.View rootView) {
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

    public void requestPermissions() {
        //请求外部存储权限用于适配android6.0的权限管理机制
        PermissionUtil.readPhoneState(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                //request permission success, do something.
            }

            @Override
            public void onRequestPermissionFailure(List<String> permissions) {
                mRootView.showMessage("Request permissions failure");
            }

            @Override
            public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                mRootView.showMessage("Need to go to the settings");
            }
        }, mRootView.getRxPermissions(), mErrorHandler);
    }

    public void loginByPhone(String mobile, String verifyCode) {
        LoginByPhoneRequest request = new LoginByPhoneRequest();
        request.setMobile(mobile);
        request.setVerifyCode(verifyCode);

        mModel.loginByPhone(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RegisterResponse>() {
                    @Override
                    public void accept(RegisterResponse response) throws Exception {
                        mRootView.hideLoading();
                        if (response.isSuccess()) {
                            cacheUserInfo(response.getToken(), response.getSignkey());
                            mRootView.killMyself();
                        } else {
                            mRootView.showVerity();
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });

    }

    public void loginByUser(String username, String password) {
        mRootView.showLoading();
        LoginByUserRequest request = new LoginByUserRequest();
        request.setUsername(username);
        request.setPassword(password);

        mModel.loginByUserName(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new Consumer<RegisterResponse>() {
                    @Override
                    public void accept(RegisterResponse response) throws Exception {
                        mRootView.hideLoading();
                        if (response.isSuccess()) {
                            cacheUserInfo(response.getToken(), response.getSignkey());
                            mRootView.killMyself();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });

    }

    public void getVerifyForUser(String mobile) {
        VeritfyRequest request = new VeritfyRequest();
        request.setCmd(106);
        request.setMobile(mobile);

        mModel.getVerifyForUser(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse baseResponse) throws Exception {
                        if (!baseResponse.isSuccess()) {
                            mRootView.showVerity();
                            mRootView.showMessage(baseResponse.getRetDesc());
                        }
                    }
                });
    }

    private void cacheUserInfo(String token, String signkey) {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        cache.put(KEY_KEEP + "token", token);
        cache.put(KEY_KEEP + "signkey", signkey);
    }
}
