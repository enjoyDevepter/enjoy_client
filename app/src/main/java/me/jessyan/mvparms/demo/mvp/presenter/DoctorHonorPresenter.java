package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.DoctorHonorContract;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorHonorBean;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.DoctorHonorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.DoctorHonorResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.DoctorHonorActivity;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;


@ActivityScope
public class DoctorHonorPresenter extends BasePresenter<DoctorHonorContract.Model, DoctorHonorContract.View> {
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
    List<DoctorHonorBean> GoodsList;

    @Inject
    public DoctorHonorPresenter(DoctorHonorContract.Model model, DoctorHonorContract.View rootView) {
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
    public void initDoctorPaper() {
        String doctorId = mRootView.getActivity().getIntent().getStringExtra(DoctorHonorActivity.KEY_FOR_DOCTOR_ID);
        if (TextUtils.isEmpty(doctorId)) {
            throw new RuntimeException("doctorId is can't null");
        }
        DoctorHonorRequest doctorPaperRequest = new DoctorHonorRequest();
        doctorPaperRequest.setDoctorId(doctorId);
        mModel.getDoctorHonor(doctorPaperRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<DoctorHonorResponse>(mErrorHandler) {
                    @Override
                    public void onNext(DoctorHonorResponse response) {
                        if (response.isSuccess()) {
                            GoodsList.clear();
                            GoodsList.addAll(response.getHonorList());
                            mRootView.setEmpty(response.getHonorList().size() == 0);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

}
