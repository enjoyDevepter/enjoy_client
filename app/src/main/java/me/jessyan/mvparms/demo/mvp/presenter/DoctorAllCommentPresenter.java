package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.DoctorAllCommentContract;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorCommentBean;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.DoctorAllCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.LikeDoctorCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.LoginUserDoctorAllCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.UnLikeDoctorCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.DoctorAllCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.LikeDoctorCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.LoginUserDoctorAllCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.UnLikeDoctorCommentResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.DoctorAllCommentActivity;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class DoctorAllCommentPresenter extends BasePresenter<DoctorAllCommentContract.Model, DoctorAllCommentContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    RecyclerView.Adapter mAdapter;
    @Inject
    List<DoctorCommentBean> orderBeanList;
    private int nextPageIndex = 1;

    @Inject
    public DoctorAllCommentPresenter(DoctorAllCommentContract.Model model, DoctorAllCommentContract.View rootView) {
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

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void requestOrderList() {
        requestOrderList(1, true);
    }

    public void nextPage() {
        requestOrderList(nextPageIndex, false);
    }

    private void requestOrderList(int pageIndex, final boolean clear) {
        String doctorId = mRootView.getActivity().getIntent().getStringExtra(DoctorAllCommentActivity.KEY_FOR_DOCTOR_ID);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");

        if (TextUtils.isEmpty(token)) {
            DoctorAllCommentRequest request = new DoctorAllCommentRequest();
            request.setPageIndex(pageIndex);
            request.setDoctorId(doctorId);

            mModel.requestAllComment(request)
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(disposable -> {
                        if (clear) {
//                        mRootView.showLoading();//显示下拉刷新的进度条
                        } else
                            mRootView.startLoadMore();//显示上拉加载更多的进度条
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(() -> {
                        if (clear)
                            mRootView.hideLoading();//隐藏下拉刷新的进度条
                        else
                            mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                    })
                    .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                    .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                    .subscribe(new ErrorHandleSubscriber<DoctorAllCommentResponse>(mErrorHandler) {
                        @Override
                        public void onNext(DoctorAllCommentResponse response) {
                            if (response.isSuccess()) {
                                if (clear) {
                                    orderBeanList.clear();
                                }
                                nextPageIndex = response.getNextPageIndex();
                                mRootView.setEnd(nextPageIndex == -1);
                                List<DoctorCommentBean> orderList = response.getDoctorCommentList();
                                orderBeanList.addAll(orderList);
                                mAdapter.notifyDataSetChanged();
                                mRootView.hideLoading();
                            }
                        }
                    });
        } else {
            LoginUserDoctorAllCommentRequest request = new LoginUserDoctorAllCommentRequest();
            request.setPageIndex(pageIndex);
            request.setDoctorId(doctorId);
            request.setToken(token);

            mModel.loginUserRequestAllComment(request)
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(disposable -> {
                        if (clear) {
//                        mRootView.showLoading();//显示下拉刷新的进度条
                        } else
                            mRootView.startLoadMore();//显示上拉加载更多的进度条
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(() -> {
                        if (clear)
                            mRootView.hideLoading();//隐藏下拉刷新的进度条
                        else
                            mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                    })
                    .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                    .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                    .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                    .subscribe(new ErrorHandleSubscriber<LoginUserDoctorAllCommentResponse>(mErrorHandler) {
                        @Override
                        public void onNext(LoginUserDoctorAllCommentResponse response) {
                            if (response.isSuccess()) {
                                if (clear) {
                                    orderBeanList.clear();
                                }
                                nextPageIndex = response.getNextPageIndex();
                                mRootView.setEnd(nextPageIndex == -1);
                                List<DoctorCommentBean> orderList = response.getDoctorCommentList();
                                orderBeanList.addAll(orderList);
                                mAdapter.notifyDataSetChanged();
                                mRootView.hideLoading();
                            }
                        }
                    });
        }
    }


    public void unlikeDoctorComment(String doctorId, String commentId) {
        UnLikeDoctorCommentRequest unLikeDoctorCommentRequest = new UnLikeDoctorCommentRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");

        unLikeDoctorCommentRequest.setToken(token);
        unLikeDoctorCommentRequest.setDoctorId(doctorId);
        unLikeDoctorCommentRequest.setCommentId(commentId);

        mModel.unLikeDoctorComment(unLikeDoctorCommentRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<UnLikeDoctorCommentResponse>(mErrorHandler) {
                    @Override
                    public void onNext(UnLikeDoctorCommentResponse response) {

                    }
                });
    }

    public void likeDoctorComment(String doctorId, String commentId) {
        LikeDoctorCommentRequest likeDoctorCommentRequest = new LikeDoctorCommentRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");

        likeDoctorCommentRequest.setToken(token);
        likeDoctorCommentRequest.setDoctorId(doctorId);
        likeDoctorCommentRequest.setCommentId(commentId);

        mModel.likeDoctorComment(likeDoctorCommentRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<LikeDoctorCommentResponse>(mErrorHandler) {
                    @Override
                    public void onNext(LikeDoctorCommentResponse response) {
                    }
                });
    }
}
