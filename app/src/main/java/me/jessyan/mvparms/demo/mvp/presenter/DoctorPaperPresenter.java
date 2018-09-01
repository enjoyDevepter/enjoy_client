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
import me.jessyan.mvparms.demo.mvp.contract.DoctorPaperContract;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorIdentificationBean;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.DoctorPaperRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.DoctorPaperResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.DoctorPaperActivity;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;


@ActivityScope
public class DoctorPaperPresenter extends BasePresenter<DoctorPaperContract.Model, DoctorPaperContract.View> {
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
    List<DoctorIdentificationBean> GoodsList;

    @Inject
    public DoctorPaperPresenter(DoctorPaperContract.Model model, DoctorPaperContract.View rootView) {
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
        String doctorId = mRootView.getActivity().getIntent().getStringExtra(DoctorPaperActivity.KEY_FOR_DOCTOR_ID);
        if (TextUtils.isEmpty(doctorId)) {
            throw new RuntimeException("doctorId is can't null");
        }
        DoctorPaperRequest doctorPaperRequest = new DoctorPaperRequest();
        doctorPaperRequest.setDoctorId(doctorId);
        mModel.getDoctorPaper(doctorPaperRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<DoctorPaperResponse>(mErrorHandler) {
                    @Override
                    public void onNext(DoctorPaperResponse response) {
                        if (response.isSuccess()) {
                            GoodsList.clear();
                            GoodsList.addAll(response.getIdentificationList());
                            mRootView.setEmpty(response.getIdentificationList().size() == 0);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }
}
