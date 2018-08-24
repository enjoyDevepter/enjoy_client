package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorAllCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorAllCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorCommentBean;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorHotCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorHotCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LikeDoctorCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LikeDoctorCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LoginUserDoctorAllCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LoginUserDoctorAllCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LoginUserDoctorHotCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LoginUserDoctorHotCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.UnLikeDoctorCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.UnLikeDoctorCommentResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.DoctorAllCommentActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.DoctorMainActivity;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import me.jessyan.mvparms.demo.mvp.contract.DoctorAllCommentContract;

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

    private int nextPageIndex = 1;

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
                    }).subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(() -> {
                        if (clear)
                            mRootView.hideLoading();//隐藏下拉刷新的进度条
                        else
                            mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                    })
                    .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                    .subscribe(new Consumer<DoctorAllCommentResponse>() {
                        @Override
                        public void accept(DoctorAllCommentResponse response) throws Exception {
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
                            } else {
                                mRootView.showMessage(response.getRetDesc());
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
                    }).subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally(() -> {
                        if (clear)
                            mRootView.hideLoading();//隐藏下拉刷新的进度条
                        else
                            mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                    })
                    .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                    .subscribe(new Consumer<LoginUserDoctorAllCommentResponse>() {
                        @Override
                        public void accept(LoginUserDoctorAllCommentResponse response) throws Exception {
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
                            } else {
                                mRootView.showMessage(response.getRetDesc());
                            }
                        }
                    });
        }
    }


    public void unlikeDoctorComment(String doctorId,String commentId){
        UnLikeDoctorCommentRequest unLikeDoctorCommentRequest = new UnLikeDoctorCommentRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");

        unLikeDoctorCommentRequest.setToken(token);
        unLikeDoctorCommentRequest.setDoctorId(doctorId);
        unLikeDoctorCommentRequest.setCommentId(commentId);

        mModel.unLikeDoctorComment(unLikeDoctorCommentRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UnLikeDoctorCommentResponse>() {
                    @Override
                    public void accept(UnLikeDoctorCommentResponse baseResponse) throws Exception {
                        if (baseResponse.isSuccess()) {
                        }else{
                        }
                    }
                });
    }

    public void likeDoctorComment(String doctorId,String commentId){
        LikeDoctorCommentRequest likeDoctorCommentRequest = new LikeDoctorCommentRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");

        likeDoctorCommentRequest.setToken(token);
        likeDoctorCommentRequest.setDoctorId(doctorId);
        likeDoctorCommentRequest.setCommentId(commentId);

        mModel.likeDoctorComment(likeDoctorCommentRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LikeDoctorCommentResponse>() {
                    @Override
                    public void accept(LikeDoctorCommentResponse baseResponse) throws Exception {
                        if (baseResponse.isSuccess()) {
                        }else{
                        }
                    }
                });
    }
}
