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
import me.jessyan.mvparms.demo.mvp.contract.HospitalInfoContract;
import me.jessyan.mvparms.demo.mvp.model.entity.HGoods;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorBean;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.DoctorListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.DoctorListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.HospitalEnvBean;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.request.HospitalInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.request.LoginUserHospitalInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.HospitalInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.LoginUserHospitalInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GoodsListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HGoodsListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.FollowRequest;
import me.jessyan.mvparms.demo.mvp.ui.activity.LoginActivity;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DoctorListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HGoodsListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HospitalEnvImageAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;
import static me.jessyan.mvparms.demo.mvp.ui.activity.HospitalInfoActivity.KEY_FOR_HOSPITAL_ID;


@ActivityScope
public class HospitalInfoPresenter extends BasePresenter<HospitalInfoContract.Model, HospitalInfoContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    // 第二个页面
    @Inject
    HGoodsListAdapter hospitalGoodsListAdapter;
    @Inject
    List<HGoods> hospitalList;
    // 第三个页面
    @Inject
    DoctorListAdapter doctorListAdapter;
    @Inject
    List<DoctorBean> doctorBeans;
    // 第四个页面
    @Inject
    HospitalEnvImageAdapter hospitalEnvImageAdapter;
    @Inject
    List<String> imageList;
    private int goodsNextPageIndex = 1;
    private int doctorNextPageIndex = 1;
    private int preEndIndex;
    private int lastPageIndex;

    @Inject
    public HospitalInfoPresenter(HospitalInfoContract.Model model, HospitalInfoContract.View rootView) {
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

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void init() {
        initHospital();
        nextDoctorPage();
        getHGoodsList(true);
    }

    public void follow(boolean follow) {
        if (checkLoginStatus()) {
            ArmsUtils.startActivity(LoginActivity.class);
            return;
        }
        FollowRequest request = new FollowRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        request.setCmd(follow ? 604 : 605);
        request.setHospitalId(mRootView.getActivity().getIntent().getStringExtra(KEY_FOR_HOSPITAL_ID));
        mModel.follow(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.isSuccess()) {
                            mRootView.updatefollowStatus(follow);
                        }
                    }
                });
    }

    private void initHospital() {
        String hospitalId = mRootView.getActivity().getIntent().getStringExtra(KEY_FOR_HOSPITAL_ID);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");
        if (TextUtils.isEmpty(token)) {
            // 未登录用户
            HospitalInfoRequest hospitalInfoRequest = new HospitalInfoRequest();
            hospitalInfoRequest.setHospitalId(hospitalId);
            mModel.requestHospital(hospitalInfoRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                    .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                    .subscribe(new ErrorHandleSubscriber<HospitalInfoResponse>(mErrorHandler) {
                        @Override
                        public void onNext(HospitalInfoResponse response) {
                            if (response.isSuccess()) {
                                mRootView.updateHosptialInfo(response.getHospital());
                                imageList.clear();
                                for (HospitalEnvBean env : response.getHospitalEnvList()) {
                                    imageList.add(env.getImage());
                                }
                                hospitalEnvImageAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        } else {
            // 已登录用户
            LoginUserHospitalInfoRequest loginUserHospitalInfoRequest = new LoginUserHospitalInfoRequest();
            loginUserHospitalInfoRequest.setHospitalId(hospitalId);
            loginUserHospitalInfoRequest.setToken(token);

            mModel.requestHospitalByUser(loginUserHospitalInfoRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                    .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                    .subscribe(new ErrorHandleSubscriber<LoginUserHospitalInfoResponse>(mErrorHandler) {
                        @Override
                        public void onNext(LoginUserHospitalInfoResponse response) {
                            if (response.isSuccess()) {
                                mRootView.updateHosptialInfo(response.getHospital());
                                imageList.clear();
                                for (HospitalEnvBean env : response.getHospitalEnvList()) {
                                    imageList.add(env.getImage());
                                }
                                hospitalEnvImageAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }

    public void requestDoctor() {
        requestDoctor(1, true);
    }

    public void nextDoctorPage() {
        requestDoctor(doctorNextPageIndex, false);
    }

    private void requestDoctor(int pageIndex, final boolean clear) {
        DoctorListRequest request = new DoctorListRequest();
        request.setPageIndex(pageIndex);
        request.setPageSize(10);
        String hospitalId = mRootView.getActivity().getIntent().getStringExtra(KEY_FOR_HOSPITAL_ID);
        request.setHospitalId(hospitalId);

        mModel.requestDoctorPage(request)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (clear) {
                    } else
                        mRootView.startLoadDoctorMore();//显示上拉加载更多的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (clear)
                        mRootView.hideDoctorLoading();//隐藏下拉刷新的进度条
                    else
                        mRootView.endLoadDoctorMore();//隐藏上拉加载更多的进度条
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<DoctorListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(DoctorListResponse response) {
                        if (response.isSuccess()) {
                            if (clear) {
                                doctorBeans.clear();
                            }
                            doctorNextPageIndex = response.getNextPageIndex();
                            mRootView.endDoctor(doctorNextPageIndex == -1);
                            doctorBeans.addAll(response.getDoctorList());
                            doctorListAdapter.notifyDataSetChanged();
                            mRootView.hideLoading();
                        }
                    }
                });
    }

    public void getHGoodsList(final boolean pullToRefresh) {
        GoodsListRequest request = new GoodsListRequest();
        request.setCmd(449);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setHospitalId(mRootView.getActivity().getIntent().getStringExtra(KEY_FOR_HOSPITAL_ID));
        request.setCity(String.valueOf(cache.get("city")));
        request.setCounty(String.valueOf(cache.get("county")));
        request.setProvince(String.valueOf(cache.get("province")));
        GoodsListRequest.OrderBy orderBy = new GoodsListRequest.OrderBy();
        orderBy.setAsc(false);
        orderBy.setField("sales");
        request.setOrderBy(orderBy);

        request.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页

        mModel.getHGoodsList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    if (pullToRefresh) {
                    }
//                        mRootView.showLoading();//显示下拉刷新的进度条
                    else
                        mRootView.startLoadGoodsMore();//显示上拉加载更多的进度条
                })
                .doFinally(() -> {
                    if (pullToRefresh)
                        mRootView.hideGoodsLoading();//隐藏下拉刷新的进度条
                    else
                        mRootView.endLoadGoodsMore();//隐藏上拉加载更多的进度条
                })
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<HGoodsListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(HGoodsListResponse response) {
                        if (response.isSuccess()) {
                            if (pullToRefresh) {
                                hospitalList.clear();
                            }
//                    mRootView.showError(response.getGoodsList().size() > 0);
                            if (response.getGoodsList().size() <= 0) {
                                return;
                            }
                            mRootView.endGoods(response.getNextPageIndex() == -1);
                            hospitalList.addAll(response.getGoodsList());
                            preEndIndex = hospitalList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                            lastPageIndex = hospitalList.size() / 10 + 1;
                            if (pullToRefresh) {
                                hospitalGoodsListAdapter.notifyDataSetChanged();
                            } else {
                                hospitalGoodsListAdapter.notifyItemRangeInserted(preEndIndex, hospitalList.size());
                            }
                        }
                    }
                });
    }

    private boolean checkLoginStatus() {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        String token = (String) (cache.get(KEY_KEEP + "token"));
        return ArmsUtils.isEmpty(token);
    }
}
