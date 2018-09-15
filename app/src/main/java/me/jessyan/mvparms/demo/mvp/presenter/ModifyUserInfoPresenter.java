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

import org.simple.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.mvp.contract.ModifyUserInfoContract;
import me.jessyan.mvparms.demo.mvp.model.MyModel;
import me.jessyan.mvparms.demo.mvp.model.entity.request.SimpleRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.CommonUserInfo;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.Member;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.ModifyUserInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.CommonUserInfoResponse;
import me.jessyan.mvparms.demo.mvp.ui.adapter.CommonUserInfoAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class ModifyUserInfoPresenter extends BasePresenter<ModifyUserInfoContract.Model, ModifyUserInfoContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<CommonUserInfo> commonUserInfoList;
    @Inject
    CommonUserInfoAdapter mAdapter;

    @Inject
    public ModifyUserInfoPresenter(ModifyUserInfoContract.Model model, ModifyUserInfoContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void initDate() {
        getCommonUserInfo();
    }

    private void getCommonUserInfo() {
        SimpleRequest request = new SimpleRequest();
        String type = mRootView.getActivity().getIntent().getStringExtra("type");
        if ("name".equals(type) || ("age".equals(type))) {

            return;
        }
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(ArmsUtils.getContext()).extras();
        Member member = (Member) cache.get(KEY_KEEP + MyModel.KEY_FOR_USER_INFO);
        if ("male".equals(type)) {
            commonUserInfoList.clear();
            CommonUserInfo userInfo = new CommonUserInfo();
            userInfo.setValue("0");
            userInfo.setLabel("保密");
            CommonUserInfo userInfo1 = new CommonUserInfo();
            userInfo1.setLabel("男");
            userInfo1.setValue("1");
            CommonUserInfo userInfo2 = new CommonUserInfo();
            userInfo2.setLabel("女");
            userInfo2.setValue("2");
            if ("0".equals(member.getSex())) {
                userInfo.setChoice(true);
            } else if ("1".equals(member.getSex())) {
                userInfo1.setChoice(true);
            } else {
                userInfo2.setChoice(true);
            }
            commonUserInfoList.add(userInfo);
            commonUserInfoList.add(userInfo1);
            commonUserInfoList.add(userInfo2);
            mAdapter.notifyDataSetChanged();
            return;
        }

        if ("constellation".equals(type)) {
            request.setCmd(903);
        } else if ("hobby".equals(type)) {
            request.setCmd(904);
        } else if ("occupation".equals(type)) {
            request.setCmd(905);
        }
        mModel.getCommonUserInfo(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<CommonUserInfoResponse>(mErrorHandler) {
                    @Override
                    public void onNext(CommonUserInfoResponse response) {
                        if (response.isSuccess()) {
                            commonUserInfoList.clear();
                            List<CommonUserInfo> commonUserInfos = response.getDictList();
                            switch (response.getCmd()) {
                                case 903:
                                    for (CommonUserInfo commonUserInfo : commonUserInfos) {
                                        if (commonUserInfo.getValue().equals(member.getConstellation())) {
                                            commonUserInfo.setChoice(true);
                                            break;
                                        }
                                    }
                                    break;
                                case 904:
                                    if (member.getHobbyList() != null && member.getHobbyList().size() > 0) {
                                        for (String str : member.getHobbyList()) {
                                            for (CommonUserInfo commonUserInfo : commonUserInfos) {
                                                if (commonUserInfo.getValue().equals(str)) {
                                                    commonUserInfo.setChoice(true);
                                                    continue;
                                                }
                                            }
                                        }
                                    }
                                    break;
                                case 905:
                                    for (CommonUserInfo commonUserInfo : commonUserInfos) {
                                        if (commonUserInfo.getValue().equals(member.getOccupation())) {
                                            commonUserInfo.setChoice(true);
                                            break;
                                        }
                                    }
                                    break;
                            }
                            commonUserInfoList.addAll(commonUserInfos);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    public void modifyUserInfo() {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        ModifyUserInfoRequest request = new ModifyUserInfoRequest();
        String type = mRootView.getActivity().getIntent().getStringExtra("type");
        cache.put("label", mRootView.getCache().get("label"));
        if ("constellation".equals(type)) {
            request.setCmd(1107);
            request.setConstellation((String) mRootView.getCache().get("value"));
        } else if ("nick".equals(type)) {
            request.setCmd(1102);
            request.setNickName((String) mRootView.getCache().get("value"));
        } else if ("male".equals(type)) {
            request.setCmd(1104);
            request.setSex((String) mRootView.getCache().get("value"));
        } else if ("age".equals(type)) {
            request.setCmd(1105);
            request.setAge((int) mRootView.getCache().get("value"));
        } else if ("hobby".equals(type)) {
            request.setCmd(1108);
            request.setHobby((String) mRootView.getCache().get("value"));
        } else if ("occupation".equals(type)) {
            request.setCmd(1109);
            request.setOccupation((String) mRootView.getCache().get("value"));
        } else if ("name".equals(type)) {
            request.setCmd(1111);
            request.setRealName((String) mRootView.getCache().get("value"));
        }
        request.setToken(String.valueOf(cache.get(KEY_KEEP + "token")));
        mModel.modifyUserInfo(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.isSuccess()) {
                            EventBus.getDefault().post(response.getCmd(), EventBusTags.USER_BASE_INFO_CHANGE);
                            mRootView.killMyself();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
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
