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
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.CommentDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.CommentDoctorResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorCommentBean;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.DoctorHotCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.DoctorHotCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.DoctorInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.DoctorInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.LikeDoctorCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.LikeDoctorCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.LikeDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.LikeDoctorResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.LoginUserDoctorHotCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.LoginUserDoctorHotCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.LoginUserDoctorInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.LoginUserDoctorInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.UnLikeDoctorCommentRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.UnLikeDoctorCommentResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.UnLikeDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.UnLikeDoctorResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.DoctorMainActivity;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import me.jessyan.mvparms.demo.mvp.contract.DoctorMainContract;

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
    public void initDoctorInfo(){
        requestDoctorHotComment();
        String doctorId = mRootView.getActivity().getIntent().getStringExtra(DoctorMainActivity.KEY_FOR_DOCTOR_ID);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");

        if(TextUtils.isEmpty(token)){
            // 未登录用户
            DoctorInfoRequest doctorInfoRequest = new DoctorInfoRequest();
            doctorInfoRequest.setDoctorId(doctorId);
            mModel.requestDoctorInfo(doctorInfoRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<DoctorInfoResponse>() {
                        @Override
                        public void accept(DoctorInfoResponse baseResponse) throws Exception {
                            if (baseResponse.isSuccess()) {
                                mRootView.updateDoctorInfo(baseResponse.getDoctor());
                            }else{
                                mRootView.showMessage(baseResponse.getRetDesc());
                            }
                        }
                    });
        }else{
            // 已登录用户
            LoginUserDoctorInfoRequest loginUserDoctorInfoRequest = new LoginUserDoctorInfoRequest();
            loginUserDoctorInfoRequest.setDoctorId(doctorId);
            loginUserDoctorInfoRequest.setToken(token);
            mModel.requestLoginUserDoctorInfo(loginUserDoctorInfoRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<LoginUserDoctorInfoResponse>() {
                        @Override
                        public void accept(LoginUserDoctorInfoResponse baseResponse) throws Exception {
                            if (baseResponse.isSuccess()) {
                                mRootView.updateDoctorInfo(baseResponse.getDoctor());
                            }else{
                                mRootView.showMessage(baseResponse.getRetDesc());
                            }
                        }
                    });
        }
    }

    public void likeDoctor(String doctorId){
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");

        LikeDoctorRequest request = new LikeDoctorRequest();
        request.setToken(token);
        request.setDoctorId(doctorId);

        mModel.likeDoctor(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LikeDoctorResponse>() {
                    @Override
                    public void accept(LikeDoctorResponse baseResponse) throws Exception {
                        if (baseResponse.isSuccess()) {
                            mRootView.updateLikeImage(true);
                        }else{
                            mRootView.showMessage(baseResponse.getRetDesc());
                        }
                    }
                });
    }

    public void unlikeDoctor(String doctorId){
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");

        UnLikeDoctorRequest request = new UnLikeDoctorRequest();
        request.setToken(token);
        request.setDoctorId(doctorId);

        mModel.unlikeDoctor(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UnLikeDoctorResponse>() {
                    @Override
                    public void accept(UnLikeDoctorResponse baseResponse) throws Exception {
                        if (baseResponse.isSuccess()) {
                            mRootView.updateLikeImage(false);
                        }else{
                            mRootView.showMessage(baseResponse.getRetDesc());
                        }
                    }
                });
    }

    public void commentDoctor(String doctorId,String content,int star,String projectId){
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
                .subscribe(new Consumer<CommentDoctorResponse>() {
                    @Override
                    public void accept(CommentDoctorResponse baseResponse) throws Exception {
                        if (baseResponse.isSuccess()) {
                            mRootView.commentOk();
                        }else{
                            mRootView.showMessage(baseResponse.getRetDesc());
                        }
                    }
                });
    }

    @Inject
    RecyclerView.Adapter mAdapter;
    @Inject
    List<DoctorCommentBean> orderBeanList;

    private int nextDoctorHotCommentPageIndex = 1;
    public void requestDoctorHotComment(){
        requestDoctorHotCommentInner(1,true);
    }

    public void nextDoctorHotComment(){
        requestDoctorHotCommentInner(nextDoctorHotCommentPageIndex,false);
    }

    private void requestDoctorHotCommentInner(int pageIndex,boolean clear){
        String doctorId = mRootView.getActivity().getIntent().getStringExtra(DoctorMainActivity.KEY_FOR_DOCTOR_ID);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");

        if(TextUtils.isEmpty(token)){
            DoctorHotCommentRequest request = new DoctorHotCommentRequest();
            request.setPageIndex(pageIndex);
            request.setDoctorId(doctorId);

            mModel.requestDoctorHotComment(request)
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(disposable -> {
                        if (clear) {
//                        mRootView.showLoading();//显示下拉刷新的进度条
                        }else
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
                    .subscribe(new Consumer<DoctorHotCommentResponse>() {
                        @Override
                        public void accept(DoctorHotCommentResponse response) throws Exception {
                            if (response.isSuccess()) {
                                if(clear){
                                    orderBeanList.clear();
                                }
                                nextDoctorHotCommentPageIndex = response.getNextPageIndex();
                                mRootView.setEnd(nextDoctorHotCommentPageIndex == -1);
                                List<DoctorCommentBean> orderList = response.getDoctorCommentList();
                                orderBeanList.addAll(orderList);
                                mAdapter.notifyDataSetChanged();
                                mRootView.updateRecyclerViewHeight();
                                mRootView.hideLoading();
                            } else {
                                mRootView.showMessage(response.getRetDesc());
                            }
                        }
                    });
        }else{
            LoginUserDoctorHotCommentRequest request = new LoginUserDoctorHotCommentRequest();
            request.setPageIndex(pageIndex);
            request.setDoctorId(doctorId);
            request.setToken(token);

            mModel.loginUserRequestDoctorHotComment(request)
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe(disposable -> {
                        if (clear) {
//                        mRootView.showLoading();//显示下拉刷新的进度条
                        }else
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
                    .subscribe(new Consumer<LoginUserDoctorHotCommentResponse>() {
                        @Override
                        public void accept(LoginUserDoctorHotCommentResponse response) throws Exception {
                            if (response.isSuccess()) {
                                if(clear){
                                    orderBeanList.clear();
                                }
                                nextDoctorHotCommentPageIndex = response.getNextPageIndex();
                                mRootView.setEnd(nextDoctorHotCommentPageIndex == -1);
                                List<DoctorCommentBean> orderList = response.getDoctorCommentList();
                                orderBeanList.addAll(orderList);
                                mAdapter.notifyDataSetChanged();
                                mRootView.updateRecyclerViewHeight();
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
