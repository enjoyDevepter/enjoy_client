package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import java.io.File;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.app.utils.ImageUploadManager;
import me.jessyan.mvparms.demo.mvp.contract.AuthenticationContract;
import me.jessyan.mvparms.demo.mvp.model.entity.QiniuRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.QiniuResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.AuthenticationRequest;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class AuthenticationPresenter extends BasePresenter<AuthenticationContract.Model, AuthenticationContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;

    private String frontImage;
    private String backImage;

    @Inject
    public AuthenticationPresenter(AuthenticationContract.Model model, AuthenticationContract.View rootView) {
        super(model, rootView);
    }


    public void auth() {

        String realName = (String) mRootView.getCache().get("realName");
        String idCard = (String) mRootView.getCache().get("idCard");

        if (ArmsUtils.isEmpty(realName)) {
            mRootView.showMessage("请输入真实姓名");
            return;
        }
        if (ArmsUtils.isEmpty(idCard)) {
            mRootView.showMessage("请输入身份证号码");
            return;
        }
        if (ArmsUtils.isEmpty(frontImage) || ArmsUtils.isEmpty(backImage)) {
            mRootView.showMessage("请上传身份证照片");
            return;
        }

        AuthenticationRequest request = new AuthenticationRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        request.setToken(String.valueOf(cache.get(KEY_KEEP + "token")));
        mModel.auth(request)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.isSuccess()) {
                            mRootView.showMessage("认证成功");
                            mRootView.killMyself();
                        }
                    }
                });
    }

    public void uploadImage() {
        final File file = new File((String) mRootView.getCache().get("imagePath"));
        QiniuRequest request = new QiniuRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");
        request.setToken(token);
        mModel.getQiniuInfo(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<QiniuResponse>(mErrorHandler) {
                    @Override
                    public void onNext(QiniuResponse response) {
                        if (response.isSuccess()) {
                            ImageUploadManager.getInstance().updateImage(file, response.getUploadToken(), response.getUrlPrefix(), new ImageUploadManager.ImageUploadResponse() {
                                @Override
                                public void onImageUploadOk(String url) {
                                    if (((boolean) mRootView.getCache().get("isFront"))) {
                                        frontImage = response.getResult().getUrl();
                                    } else {
                                        backImage = response.getResult().getUrl();
                                    }
                                }

                                @Override
                                public void onImageUploadError(String errorInfo) {
                                }
                            });
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

}
