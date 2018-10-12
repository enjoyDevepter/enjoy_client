package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;

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
import me.jessyan.mvparms.demo.mvp.contract.FeedBackContract;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.FeedbackRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.FeedbackResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class FeedBackPresenter extends BasePresenter<FeedBackContract.Model, FeedBackContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public FeedBackPresenter(FeedBackContract.Model model, FeedBackContract.View rootView) {
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

    public void feedback(String content){
        FeedbackRequest request = new FeedbackRequest();
        request.setContent(content);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(ArmsUtils.getContext()).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));

        mModel.feedback(request)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();//显示下拉刷新的进度条
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();//隐藏下拉刷新的进度条
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
        .subscribe(new ErrorHandleSubscriber<FeedbackResponse>(mErrorHandler) {
            @Override
            public void onNext(FeedbackResponse response) {
                if (response.isSuccess()) {
                    mRootView.showOk();
                }
            }
        });
    }
}
