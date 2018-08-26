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
import me.jessyan.mvparms.demo.mvp.contract.RegisterContract;
import me.jessyan.mvparms.demo.mvp.model.entity.request.RegisterRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.VeritfyRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.RegisterResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void register(String mobile, String password, String verify, String type, String code) {

        RegisterRequest request = new RegisterRequest();
        request.setMobile(mobile);
        request.setPassword(password);
        request.setVerifyCode(verify);
        request.setType(type);
        request.setCode(code);

        mModel.register(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RegisterResponse>() {
                    @Override
                    public void accept(RegisterResponse registerResponse) throws Exception {
                        if (registerResponse.isSuccess()) {
                            Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
                            cache.put(KEY_KEEP + "token", registerResponse.getToken());
                            cache.put(KEY_KEEP + "signkey", registerResponse.getSignkey());
                            mRootView.killMyself();
                        } else {
                            mRootView.showVerity();
                            mRootView.showMessage(registerResponse.getRetDesc());
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
}
