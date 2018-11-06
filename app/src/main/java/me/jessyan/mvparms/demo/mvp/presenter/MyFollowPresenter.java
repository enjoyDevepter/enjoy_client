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

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.MyFollowContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Hospital;
import me.jessyan.mvparms.demo.mvp.model.entity.Member;
import me.jessyan.mvparms.demo.mvp.model.entity.Store;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorBean;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.FollowRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.MyFollowRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.MyFollowListResponse;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyFollowDoctorAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyFollowHospitalAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyFollowMemberAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyFollowStoreAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class MyFollowPresenter extends BasePresenter<MyFollowContract.Model, MyFollowContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    MyFollowMemberAdapter memberAdapter;
    @Inject
    List<Member> memberList;
    @Inject
    MyFollowDoctorAdapter doctorAdapter;
    @Inject
    List<DoctorBean> doctorList;
    @Inject
    MyFollowHospitalAdapter hospitalAdapter;
    @Inject
    List<Hospital> hospitalList;
    @Inject
    MyFollowStoreAdapter storeAdapter;
    @Inject
    List<Store> storeList;

    private int preEndIndex;
    private int lastPageIndex = 1;

    @Inject
    public MyFollowPresenter(MyFollowContract.Model model, MyFollowContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onCreate() {
        getMyFollow(true);
    }

    public void getMyFollow(boolean pullToRefresh) {

        MyFollowRequest request = new MyFollowRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        final int status = (int) mRootView.getCache().get("status");
        switch (status) {
            case 0:
                request.setCmd(1175);
                break;
            case 1:
                request.setCmd(1176);
                break;
            case 2:
                request.setCmd(1177);
                break;
            case 3:
                request.setCmd(1178);
                break;
        }
        if (pullToRefresh) lastPageIndex = 1;
        request.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页

        mModel.getMyFollows(request)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (pullToRefresh)
                        mRootView.showLoading();//显示下拉刷新的进度条
                    else
                        mRootView.startLoadMore();//显示上拉加载更多的进度条
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (pullToRefresh)
                        mRootView.hideLoading();//隐藏下拉刷新的进度条
                    else
                        mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                })
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<MyFollowListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(MyFollowListResponse response) {
                        if (response.isSuccess()) {
                            switch (status) {
                                case 0:
                                    mRootView.showConent(response.getMemberList().size() > 0);
                                    if (pullToRefresh) {
                                        memberList.clear();
                                    }
                                    mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                                    memberList.addAll(response.getMemberList());
                                    preEndIndex = memberList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                                    lastPageIndex = memberList.size() / 10 + 1;
                                    if (pullToRefresh) {
                                        memberAdapter.notifyDataSetChanged();
                                    } else {
                                        memberAdapter.notifyItemRangeInserted(preEndIndex, memberList.size());
                                    }
                                    break;
                                case 1:
                                    mRootView.showConent(response.getDoctorList().size() > 0);
                                    if (pullToRefresh) {
                                        doctorList.clear();
                                    }
                                    mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                                    doctorList.addAll(response.getDoctorList());
                                    preEndIndex = doctorList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                                    lastPageIndex = doctorList.size() / 10 + 1;
                                    if (pullToRefresh) {
                                        doctorAdapter.notifyDataSetChanged();
                                    } else {
                                        doctorAdapter.notifyItemRangeInserted(preEndIndex, doctorList.size());
                                    }
                                    break;
                                case 2:
                                    mRootView.showConent(response.getHospitalList().size() > 0);
                                    if (pullToRefresh) {
                                        hospitalList.clear();
                                    }
                                    mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                                    hospitalList.addAll(response.getHospitalList());
                                    preEndIndex = hospitalList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                                    lastPageIndex = hospitalList.size() / 10 + 1;
                                    if (pullToRefresh) {
                                        hospitalAdapter.notifyDataSetChanged();
                                    } else {
                                        hospitalAdapter.notifyItemRangeInserted(preEndIndex, hospitalList.size());
                                    }
                                    break;
                                case 3:
                                    mRootView.showConent(response.getStoreList() != null && response.getStoreList().size() > 0);
                                    if (pullToRefresh) {
                                        storeList.clear();
                                    }
                                    mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                                    if (response.getStoreList() != null) {
                                        storeList.addAll(response.getStoreList());
                                    }
                                    preEndIndex = storeList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                                    lastPageIndex = storeList.size() / 10 + 1;
                                    if (pullToRefresh) {
                                        storeAdapter.notifyDataSetChanged();
                                    } else {
                                        storeAdapter.notifyItemRangeInserted(preEndIndex, storeList.size());
                                    }
                                    break;
                            }
                        }
                    }
                });
    }

    public void unfollow() {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        FollowRequest request = new FollowRequest();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        final int status = (int) mRootView.getCache().get("status");
        switch (status) {
            case 0:
                request.setCmd(211);
                request.setMemberId((String) mRootView.getCache().get("memberId"));
                break;
            case 1:
                request.setCmd(666);
                request.setDoctorId((String) mRootView.getCache().get("doctorId"));
                break;
            case 2:
                request.setCmd(605);
                request.setHospitalId((String) mRootView.getCache().get("hospitalId"));
                break;
            case 3:
                request.setCmd(708);
                request.setStoreId((String) mRootView.getCache().get("storeId"));
                break;
        }

        mModel.follow(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();//显示下拉刷新的进度条
                })
                .doFinally(() -> {
                    mRootView.hideLoading();//隐藏下拉刷新的进度条
                })
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.isSuccess()) {
                            getMyFollow(true);
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
