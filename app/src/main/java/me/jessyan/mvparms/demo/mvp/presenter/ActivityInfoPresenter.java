package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.ActivityInfoContract;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.ActivityInfo;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.request.ActivityInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.ActivityInfoListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.ActivityInfoResponse;
import me.jessyan.mvparms.demo.mvp.ui.adapter.ActivityListAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static me.jessyan.mvparms.demo.mvp.ui.activity.HospitalInfoActivity.KEY_FOR_HOSPITAL_ID;


@ActivityScope
public class ActivityInfoPresenter extends BasePresenter<ActivityInfoContract.Model, ActivityInfoContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<ActivityInfo> activityInfoList;
    @Inject
    ActivityListAdapter mAdapter;

    @Inject
    public ActivityInfoPresenter(ActivityInfoContract.Model model, ActivityInfoContract.View rootView) {
        super(model, rootView);
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        getActivityInfo();
    }

    public void getActivityInfo() {
        ActivityInfoRequest request = new ActivityInfoRequest();
        request.setCmd(924);
        request.setActivityId((String) mRootView.getCache().get("activityId"));
        request.setHospitalId(mRootView.getActivity().getIntent().getStringExtra(KEY_FOR_HOSPITAL_ID));
        mModel.getActivityInfo(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁

                .subscribe(new ErrorHandleSubscriber<ActivityInfoResponse>(mErrorHandler) {
                    @Override
                    public void onNext(ActivityInfoResponse response) {
                        if (response.isSuccess()) {
                            mRootView.updateUI(response.getActivityInfo());
                            getActivityList();
                        }
                    }
                });
    }

    private void getActivityList() {
        ActivityInfoRequest request = new ActivityInfoRequest();
        request.setCmd(923);
        request.setHospitalId(mRootView.getActivity().getIntent().getStringExtra(KEY_FOR_HOSPITAL_ID));
        mModel.getActivityList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<ActivityInfoListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(ActivityInfoListResponse response) {
                        if (response.isSuccess()) {
                            activityInfoList.clear();
                            activityInfoList.addAll(response.getActivityInfoList());
                            if (activityInfoList.size() > 0) {
                                String activityId = (String) mRootView.getCache().get("activityId");
                                int deleteIndex = -1;
                                for (ActivityInfo activityInfo : activityInfoList) {
                                    if (activityInfo.getActivityId().equals(activityId)) {
                                        deleteIndex = activityInfoList.indexOf(activityInfo);
                                        break;
                                    }
                                }
                                if (-1 != deleteIndex) {
                                    activityInfoList.remove(deleteIndex);
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                            mRootView.updateInfos(activityInfoList.size() > 0);
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
