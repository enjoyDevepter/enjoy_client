package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
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
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorBean;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.HospitalEnvBean;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.request.HospitalInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.request.LoginUserHospitalInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.HospitalInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.LoginUserHospitalInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorListResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.HospitalInfoActivity;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DoctorListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HospitalEnvImageAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HospitalGoodsListAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import me.jessyan.mvparms.demo.mvp.contract.HospitalInfoContract;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


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
    HospitalGoodsListAdapter hospitalGoodsListAdapter;
    @Inject
    List<Goods> hospitalList;
    private int goodsNextPageIndex = -1;

    // 第三个页面
    @Inject
    DoctorListAdapter doctorListAdapter;
    @Inject
    List<DoctorBean> doctorBeans;
    private int doctorNextPageIndex = -1;

    // 第四个页面
    @Inject
    HospitalEnvImageAdapter hospitalEnvImageAdapter;
    @Inject
    List<String> imageList;

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

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void init(){
        initHospital();
    }

    private void initHospital(){
        String hospitalId = mRootView.getActivity().getIntent().getStringExtra(HospitalInfoActivity.KEY_FOR_HOSPITAL_ID);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");
        if(TextUtils.isEmpty(token)){
            // 未登录用户
            HospitalInfoRequest hospitalInfoRequest = new HospitalInfoRequest();
            hospitalInfoRequest.setHospitalId(hospitalId);
            mModel.requestHospital(hospitalInfoRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<HospitalInfoResponse>() {
                        @Override
                        public void accept(HospitalInfoResponse baseResponse) throws Exception {
                            if (baseResponse.isSuccess()) {
                                mRootView.updateHosptialInfo(baseResponse.getHospital());
                                imageList.clear();
                                for(HospitalEnvBean env : baseResponse.getHospitalEnvList()){
                                    imageList.add(env.getImage());
                                }
                                hospitalEnvImageAdapter.notifyDataSetChanged();
                            }else{
                                mRootView.showMessage(baseResponse.getRetDesc());
                            }
                        }
                    });
        }else{
            // 已登录用户
            LoginUserHospitalInfoRequest loginUserHospitalInfoRequest = new LoginUserHospitalInfoRequest();
            loginUserHospitalInfoRequest.setHospitalId(hospitalId);
            loginUserHospitalInfoRequest.setToken(token);

            mModel.requestHospitalByUser(loginUserHospitalInfoRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<LoginUserHospitalInfoResponse>() {
                        @Override
                        public void accept(LoginUserHospitalInfoResponse baseResponse) throws Exception {
                            if (baseResponse.isSuccess()) {
                                mRootView.updateHosptialInfo(baseResponse.getHospital());
                            }else{
                                mRootView.showMessage(baseResponse.getRetDesc());
                            }
                        }
                    });
        }
    }

    public void requestDoctor(){
        requestDoctor(1,true);
    }

    public void nextDoctorPage(){
        requestDoctor(doctorNextPageIndex,false);
    }

    private void requestDoctor(int pageIndex,final boolean clear) {
        DoctorListRequest request = new DoctorListRequest();
        request.setPageIndex(pageIndex);
        request.setPageSize(10);
        String hospitalId = mRootView.getActivity().getIntent().getStringExtra(HospitalInfoActivity.KEY_FOR_HOSPITAL_ID);
        request.setHospitalId(hospitalId);

        mModel.requestDoctorPage(request)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (clear) {
                    }else
                        mRootView.startLoadDoctorMore();//显示上拉加载更多的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (clear)
                        mRootView.hideLoading();//隐藏下拉刷新的进度条
                    else
                        mRootView.endLoadDoctorMore();//隐藏上拉加载更多的进度条
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new Consumer<DoctorListResponse>() {
                    @Override
                    public void accept(DoctorListResponse response) throws Exception {
                        if (response.isSuccess()) {
                            if(clear){
                                doctorBeans.clear();
                            }
                            doctorNextPageIndex = response.getNextPageIndex();
                            mRootView.endDoctor(doctorNextPageIndex == -1);
                            doctorBeans.addAll(response.getDoctorList());
                            doctorListAdapter.notifyDataSetChanged();
                            mRootView.hideLoading();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }
}
