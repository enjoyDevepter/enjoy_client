package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import org.simple.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.mvp.model.MyModel;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.CashConvertRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.UserInfoRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.CashConvertResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.UserInfoResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import me.jessyan.mvparms.demo.mvp.contract.CashConvertContract;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class CashConvertPresenter extends BasePresenter<CashConvertContract.Model, CashConvertContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public CashConvertPresenter(CashConvertContract.Model model, CashConvertContract.View rootView) {
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

    public void convertCash(int money){  // 单位分
        CashConvertRequest request = new CashConvertRequest();
        Cache<String,Object> cache= ArmsUtils.obtainAppComponentFromContext(ArmsUtils.getContext()).extras();
        String token=(String)cache.get(KEY_KEEP+"token");
        request.setToken(token);
        request.setMoney(money);

        mModel.convertCash(request)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();//显示下拉刷新的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();//隐藏下拉刷新的进度条
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new Consumer<CashConvertResponse>() {
                    @Override
                    public void accept(CashConvertResponse response) throws Exception {
                        if (response.isSuccess()) {
                            mRootView.showConvertOk();
                            initUser();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });

    }

    public void initUser(){
        UserInfoRequest request = new UserInfoRequest();
        Cache<String,Object> cache= ArmsUtils.obtainAppComponentFromContext(ArmsUtils.getContext()).extras();
        String token=(String)cache.get(KEY_KEEP+"token");
        request.setToken(token);

        mModel.getUserInfo(request)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();//显示下拉刷新的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();//隐藏下拉刷新的进度条
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new Consumer<UserInfoResponse>() {
                    @Override
                    public void accept(UserInfoResponse response) throws Exception {
                        if (response.isSuccess()) {
                            cache.put(KEY_KEEP+ MyModel.KEY_FOR_USER_INFO,response.getMember());
                            cache.put(KEY_KEEP+ MyModel.KEY_FOR_USER_ACCOUNT,response.getMemberAccount());
                            EventBus.getDefault().post(response.getMember(), EventBusTags.USER_INFO_CHANGE);
                            EventBus.getDefault().post(response.getMemberAccount(), EventBusTags.USER_ACCOUNT_CHANGE);
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

}
