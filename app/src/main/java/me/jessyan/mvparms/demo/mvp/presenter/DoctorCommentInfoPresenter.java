package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v7.widget.RecyclerView;

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

import javax.inject.Inject;

import me.jessyan.mvparms.demo.mvp.contract.DoctorCommentInfoContract;

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

    public void initDoctorInfo(){
        nextPage();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void requestOrderList(){
        requestOrderList(1,true);
    }

    public void nextPage(){
        requestOrderList(nextPageIndex,false);
    }

    private int nextPageIndex = 1;

    private void requestOrderList(int pageIndex,final boolean clear) {
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
                .subscribe(new Consumer<GetDoctorCommentReplyPageResponse>() {
                    @Override
                    public void accept(GetDoctorCommentReplyPageResponse response) throws Exception {
                        if (response.isSuccess()) {
                            if(clear){
                                orderBeanList.clear();
                            }
                            nextPageIndex = response.getNextPageIndex();
                            mRootView.setEnd(nextPageIndex == -1);
                            orderBeanList.addAll(response.getDoctorCommentReplyList());
                            mAdapter.notifyDataSetChanged();
                            mRootView.hideLoading();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
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
                            mRootView.updateGoodView(false);
                        }else{
                            mRootView.showMessage(baseResponse.getRetDesc());
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
                            mRootView.updateGoodView(true);
                        }else{
                            mRootView.showMessage(baseResponse.getRetDesc());
                        }
                    }
                });
    }

    public void replyDoctorComment(String content){

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
                .subscribe(new Consumer<ReplyDoctorCommentResponse>() {
                    @Override
                    public void accept(ReplyDoctorCommentResponse baseResponse) throws Exception {
                        if (baseResponse.isSuccess()) {
                            ArmsUtils.makeText(ArmsUtils.getContext(),"评论成功");
                            mRootView.clear();
                        }else{
                            mRootView.showMessage(baseResponse.getRetDesc());
                        }
                    }
                });
    }
}
