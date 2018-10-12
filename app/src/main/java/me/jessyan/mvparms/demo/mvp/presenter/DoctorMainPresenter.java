package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
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
import me.jessyan.mvparms.demo.mvp.contract.DoctorMainContract;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorCommentBean;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.CommentDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.DoctorHotCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.DoctorInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.LikeDoctorCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.LikeDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.LoginUserDoctorHotCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.LoginUserDoctorInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.UnLikeDoctorCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.UnLikeDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.CommentDoctorResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.DoctorHotCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.DoctorInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.LikeDoctorCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.LikeDoctorResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.LoginUserDoctorHotCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.LoginUserDoctorInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.UnLikeDoctorCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.UnLikeDoctorResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.DoctorMainActivity;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DoctorCommentHolderAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class DoctorMainPresenter extends BasePresenter<DoctorMainContract.Model, DoctorMainContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    DoctorCommentHolderAdapter mAdapter;
    @Inject
    List<DoctorCommentBean> orderBeanList;
    private int nextDoctorHotCommentPageIndex = 1;

    @Inject
    public DoctorMainPresenter(DoctorMainContract.Model model, DoctorMainContract.View rootView) {
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
    public void initDoctorInfo() {
        requestDoctorHotComment();
        String doctorId = mRootView.getActivity().getIntent().getStringExtra(DoctorMainActivity.KEY_FOR_DOCTOR_ID);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");

        if (TextUtils.isEmpty(token)) {
            // 未登录用户
            DoctorInfoRequest doctorInfoRequest = new DoctorInfoRequest();
            doctorInfoRequest.setDoctorId(doctorId);
            mModel.requestDoctorInfo(doctorInfoRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                    .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                    .subscribe(new ErrorHandleSubscriber<DoctorInfoResponse>(mErrorHandler) {
                        @Override
                        public void onNext(DoctorInfoResponse response) {
                            if (response.isSuccess()) {
                                mRootView.updateDoctorInfo(response.getDoctor());
                            }
                        }
                    });
        } else {
            // 已登录用户
            LoginUserDoctorInfoRequest loginUserDoctorInfoRequest = new LoginUserDoctorInfoRequest();
            loginUserDoctorInfoRequest.setDoctorId(doctorId);
            loginUserDoctorInfoRequest.setToken(token);
            mModel.requestLoginUserDoctorInfo(loginUserDoctorInfoRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                    .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                    .subscribe(new ErrorHandleSubscriber<LoginUserDoctorInfoResponse>(mErrorHandler) {
                        @Override
                        public void onNext(LoginUserDoctorInfoResponse response) {
                            if (response.isSuccess()) {
                                mRootView.updateDoctorInfo(response.getDoctor());
                            }
                        }
                    });
        }
    }

    public void likeDoctor(String doctorId) {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");

        LikeDoctorRequest request = new LikeDoctorRequest();
        request.setToken(token);
        request.setDoctorId(doctorId);

        mModel.likeDoctor(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<LikeDoctorResponse>(mErrorHandler) {
                    @Override
                    public void onNext(LikeDoctorResponse response) {
                        if (response.isSuccess()) {
                            mRootView.updateLikeImage(true);
                        }
                    }
                });
    }

    public void unlikeDoctor(String doctorId) {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");

        UnLikeDoctorRequest request = new UnLikeDoctorRequest();
        request.setToken(token);
        request.setDoctorId(doctorId);

        mModel.unlikeDoctor(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<UnLikeDoctorResponse>(mErrorHandler) {
                    @Override
                    public void onNext(UnLikeDoctorResponse response) {
                        if (response.isSuccess()) {
                            mRootView.updateLikeImage(false);
                        }
                    }
                });
    }

    public void commentDoctor(String doctorId, String content, int star, String projectId) {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");

        CommentDoctorRequest commentDoctorRequest = new CommentDoctorRequest();
        commentDoctorRequest.setContent(content);
        commentDoctorRequest.setDoctorId(doctorId);
        commentDoctorRequest.setProjectId(projectId);
        commentDoctorRequest.setStar(star);
        commentDoctorRequest.setToken(token);

        mModel.commentDoctor(commentDoctorRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<CommentDoctorResponse>(mErrorHandler) {
                    @Override
                    public void onNext(CommentDoctorResponse response) {
                        if (response.isSuccess()) {
                            requestDoctorHotComment();
                            mRootView.commentOk();
                        }
                    }
                });
    }

    public void requestDoctorHotComment() {
        requestDoctorHotCommentInner(1, true);
    }

    public void nextDoctorHotComment() {
        requestDoctorHotCommentInner(nextDoctorHotCommentPageIndex, false);
    }

    private void requestDoctorHotCommentInner(int pageIndex, boolean clear) {
        String doctorId = mRootView.getActivity().getIntent().getStringExtra(DoctorMainActivity.KEY_FOR_DOCTOR_ID);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");

        if (TextUtils.isEmpty(token)) {
            DoctorHotCommentRequest request = new DoctorHotCommentRequest();
            request.setPageIndex(pageIndex);
            request.setDoctorId(doctorId);

            mModel.requestDoctorHotComment(request)
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
                    .subscribe(new ErrorHandleSubscriber<DoctorHotCommentResponse>(mErrorHandler) {
                        @Override
                        public void onNext(DoctorHotCommentResponse response) {
                            if (response.isSuccess()) {
                                if (clear) {
                                    orderBeanList.clear();
                                }
                                nextDoctorHotCommentPageIndex = response.getNextPageIndex();
                                mRootView.setEnd(nextDoctorHotCommentPageIndex == -1);
                                List<DoctorCommentBean> orderList = response.getDoctorCommentList();
                                orderBeanList.addAll(orderList);
                                mAdapter.notifyDataSetChanged();
                                mRootView.updateRecyclerViewHeight();
                                mRootView.hideLoading();
                            }
                        }
                    });
        } else {
            LoginUserDoctorHotCommentRequest request = new LoginUserDoctorHotCommentRequest();
            request.setPageIndex(pageIndex);
            request.setDoctorId(doctorId);
            request.setToken(token);

            mModel.loginUserRequestDoctorHotComment(request)
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
                    .subscribe(new ErrorHandleSubscriber<LoginUserDoctorHotCommentResponse>(mErrorHandler) {
                        @Override
                        public void onNext(LoginUserDoctorHotCommentResponse response) {
                            if (response.isSuccess()) {
                                if (clear) {
                                    orderBeanList.clear();
                                }
                                nextDoctorHotCommentPageIndex = response.getNextPageIndex();
                                mRootView.setEnd(nextDoctorHotCommentPageIndex == -1);
                                List<DoctorCommentBean> orderList = response.getDoctorCommentList();
                                orderBeanList.addAll(orderList);
                                mAdapter.notifyDataSetChanged();
                                mRootView.updateRecyclerViewHeight();
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
                        if (response.isSuccess()) {
                        } else {
                        }
                    }
                });
    }
}
