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

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.app.utils.ImageUploadManager;
import me.jessyan.mvparms.demo.mvp.contract.UserInfoContract;
import me.jessyan.mvparms.demo.mvp.model.entity.AreaAddress;
import me.jessyan.mvparms.demo.mvp.model.entity.QiniuRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.QiniuResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.request.SimpleRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.AllAddressResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.ModifyUserInfoRequest;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class UserInfoPresenter extends BasePresenter<UserInfoContract.Model, UserInfoContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<AreaAddress> addressList;

    @Inject
    public UserInfoPresenter(UserInfoContract.Model model, UserInfoContract.View rootView) {
        super(model, rootView);
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        getAllAddressList();
    }


    private void getAllAddressList() {
        SimpleRequest request = new SimpleRequest();
        request.setCmd(902);

        mModel.getAllAddressList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<AllAddressResponse>(mErrorHandler) {
                    @Override
                    public void onNext(AllAddressResponse response) {
                        if (response.isSuccess()) {
                            addressList.clear();
                            addressList.addAll(response.getAreaList());
                        }
                    }
                });

    }

    public void modifyUserInfo() {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();

        ModifyUserInfoRequest request = new ModifyUserInfoRequest();
        int type = (int) mRootView.getCache().get("type");
        switch (type) {
            case 1: // 图像
                request.setCmd(1101);
                request.setHeadImage((String) mRootView.getCache().get("headImage"));
                break;
            case 2: // 地区
                request.setCmd(1106);
                request.setProvince((String) mRootView.getCache().get("province"));
                request.setCity((String) mRootView.getCache().get("city"));
                request.setCounty((String) mRootView.getCache().get("county"));
                break;
        }
        request.setToken(String.valueOf(cache.get(KEY_KEEP + "token")));
        mModel.modifyUserInfo(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (!response.isSuccess()) {
                            mRootView.showMessage(response.getRetDesc());
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
                                    if (null != mRootView) {
                                        mRootView.getCache().put("headImage", url);
                                        modifyUserInfo();
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
