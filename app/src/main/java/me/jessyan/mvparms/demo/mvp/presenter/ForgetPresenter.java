package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.ForgetContract;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ForgetRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.VeritfyRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.RegisterResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class ForgetPresenter extends BasePresenter<ForgetContract.Model, ForgetContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;

    @Inject
    public ForgetPresenter(ForgetContract.Model model, ForgetContract.View rootView) {
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

    public void getVerifyForFind(String mobile) {
        VeritfyRequest request = new VeritfyRequest();
        request.setCmd(107);
        request.setMobile(mobile);

        mModel.getVerifyForFind(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse baseResponse) throws Exception {
                        if (!baseResponse.isSuccess()) {
                            mRootView.showMessage(baseResponse.getRetDesc());
                        }
                    }
                });

    }

    public void find(String mobile, String verifyCode) {
        ForgetRequest request = new ForgetRequest();
        request.setMobile(mobile);
        request.setVerifyCode(verifyCode);
        mModel.find(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RegisterResponse>() {
                    @Override
                    public void accept(RegisterResponse response) throws Exception {
                        if (response.isSuccess()) {
                            // 跳转到主页面
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

}
