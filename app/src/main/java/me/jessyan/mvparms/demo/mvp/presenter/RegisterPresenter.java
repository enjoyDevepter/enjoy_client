package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.app.utils.SPUtils;
import me.jessyan.mvparms.demo.mvp.contract.RegisterContract;
import me.jessyan.mvparms.demo.mvp.model.entity.request.RegisterRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.SimpleRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.VeritfyRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.RegisterResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.LoginActivity;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class RegisterPresenter extends BasePresenter<RegisterContract.Model, RegisterContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;

    @Inject
    public RegisterPresenter(RegisterContract.Model model, RegisterContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        getProtocolURL();
    }

    private void getProtocolURL() {
        SimpleRequest request = new SimpleRequest();
        request.setCmd(913);

        mModel.getProtocolURL(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<RegisterResponse>(mErrorHandler) {
                    @Override
                    public void onNext(RegisterResponse response) {
                        if (response.isSuccess()) {
                            mRootView.getCache().put("protocolURL", response.getUrl());
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void register(String mobile, String password, String verify, String code) {

        RegisterRequest request = new RegisterRequest();
        request.setMobile(mobile);
        request.setPassword(password);
        request.setVerifyCode(verify);
        request.setType((String) mRootView.getCache().get("type"));
        request.setCode(code);

        mModel.register(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<RegisterResponse>(mErrorHandler) {
                    @Override
                    public void onNext(RegisterResponse response) {
                        if (response.isSuccess()) {
                            Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
                            cache.put(KEY_KEEP + "token", response.getToken());
                            cache.put(KEY_KEEP + "signkey", response.getSignkey());
                            SPUtils.put("token", response.getToken());
                            SPUtils.put("signkey", response.getSignkey());
                            mAppManager.killActivity(LoginActivity.class);
                            mRootView.killMyself();
                        } else {
                            mRootView.showVerity();
                        }
                    }
                });
    }


    public void getVerify(String mobile) {
        VeritfyRequest request = new VeritfyRequest();
        request.setMobile(mobile);
        mModel.getVerify(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (!response.isSuccess()) {
                            mRootView.showVerity();
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }
}
