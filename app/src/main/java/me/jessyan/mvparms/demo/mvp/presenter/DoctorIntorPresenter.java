package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.request.DoctorIntorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.response.DoctorIntorResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import me.jessyan.mvparms.demo.mvp.contract.DoctorIntorContract;


@ActivityScope
public class DoctorIntorPresenter extends BasePresenter<DoctorIntorContract.Model, DoctorIntorContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public DoctorIntorPresenter(DoctorIntorContract.Model model, DoctorIntorContract.View rootView) {
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

    public void getDoctorInfo(String doctorId){
        DoctorIntorRequest request = new DoctorIntorRequest();
        request.setDoctorId(doctorId);

        mModel.getDoctorIntor(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DoctorIntorResponse>() {
                    @Override
                    public void accept(DoctorIntorResponse baseResponse) throws Exception {
                        if (baseResponse.isSuccess()) {
                            mRootView.update(baseResponse.getDoctor());
                        }else{
                            mRootView.showMessage(baseResponse.getRetDesc());
                        }
                    }
                });
    }
}
