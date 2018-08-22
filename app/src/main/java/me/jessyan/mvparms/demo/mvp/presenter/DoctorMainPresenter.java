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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.CommentDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LikeDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LikeDoctorResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LoginUserDoctorInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.LoginUserDoctorInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.UnLikeDoctorRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.UnLikeDoctorResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.HospitalEnvBean;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.request.HospitalInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.request.LoginUserHospitalInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.HospitalInfoResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.LoginUserHospitalInfoResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.DoctorMainActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.HospitalInfoActivity;
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


}
