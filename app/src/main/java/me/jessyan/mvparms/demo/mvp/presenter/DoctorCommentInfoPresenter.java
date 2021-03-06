package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v7.widget.RecyclerView;

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
import me.jessyan.mvparms.demo.mvp.contract.DoctorCommentInfoContract;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorCommentBean;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorCommentReplyBean;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.GetDoctorCommentReplyPageRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.LikeDoctorCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.ReplyDoctorCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.UnLikeDoctorCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.GetDoctorCommentReplyPageResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.LikeDoctorCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.ReplyDoctorCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.UnLikeDoctorCommentResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;
import static me.jessyan.mvparms.demo.mvp.ui.activity.DoctorCommentInfoActivity.KEY_FOR_DOCTOR_COMMENT_BEAN;


@ActivityScope
public class DoctorCommentInfoPresenter extends BasePresenter<DoctorCommentInfoContract.Model, DoctorCommentInfoContract.View> {
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
    List<DoctorCommentReplyBean> orderBeanList;
    private int nextPageIndex = 1;

    @Inject
    public DoctorCommentInfoPresenter(DoctorCommentInfoContract.Model model, DoctorCommentInfoContract.View rootView) {
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

    public void initDoctorInfo() {
        nextPage();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void requestOrderList() {
        requestOrderList(1, true);
    }

    public void nextPage() {
        requestOrderList(nextPageIndex, false);
    }

    private void requestOrderList(int pageIndex, final boolean clear) {
        GetDoctorCommentReplyPageRequest getDoctorCommentReplyPageRequest = new GetDoctorCommentReplyPageRequest();
        DoctorCommentBean doctorCommentBean = (DoctorCommentBean) mRootView.getActivity().getIntent().getSerializableExtra(KEY_FOR_DOCTOR_COMMENT_BEAN);
        getDoctorCommentReplyPageRequest.setPageSize(10);
        getDoctorCommentReplyPageRequest.setPageIndex(pageIndex);
        getDoctorCommentReplyPageRequest.setCommentId(doctorCommentBean.getCommentId());

        mModel.getDoctorCommentReplyPage(getDoctorCommentReplyPageRequest)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (clear) {
                        //                        mRootView.showLoading();//显示下拉刷新的进度条
                    } else
                        mRootView.startLoadMore();//显示上拉加载更多的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (clear)
                        mRootView.hideLoading();//隐藏下拉刷新的进度条
                    else
                        mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                })
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<GetDoctorCommentReplyPageResponse>(mErrorHandler) {
                    @Override
                    public void onNext(GetDoctorCommentReplyPageResponse response) {
                        if (response.isSuccess()) {
                            if (clear) {
                                orderBeanList.clear();
                            }
                            nextPageIndex = response.getNextPageIndex();
                            mRootView.setEnd(nextPageIndex == -1);
                            orderBeanList.addAll(response.getDoctorCommentReplyList());
                            mAdapter.notifyDataSetChanged();
                            mRootView.hideLoading();
                        }
                    }
                });
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
                        if (response.isSuccess()) {
                            mRootView.updateGoodView(false);
                        }
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
                            mRootView.updateGoodView(true);
                        }
                    }
                });
    }

    public void replyDoctorComment(String content) {

        DoctorCommentBean doctorCommentBean = (DoctorCommentBean) mRootView.getActivity().getIntent().getSerializableExtra(KEY_FOR_DOCTOR_COMMENT_BEAN);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");

        ReplyDoctorCommentRequest request = new ReplyDoctorCommentRequest();
        request.setCommentId(doctorCommentBean.getCommentId());
        request.setToken(token);
        request.setContent(content);

        mModel.replyDoctorComment(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<ReplyDoctorCommentResponse>(mErrorHandler) {
                    @Override
                    public void onNext(ReplyDoctorCommentResponse response) {
                        if (response.isSuccess()) {
                            requestOrderList();
                            ArmsUtils.makeText(ArmsUtils.getContext(), "评论成功");
                            mRootView.clear();
                        }
                    }
                });
    }
}
