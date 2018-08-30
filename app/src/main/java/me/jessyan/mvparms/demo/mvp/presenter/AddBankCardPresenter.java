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

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.AddBankCardContract;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.BankCardBean;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.AddBankCardRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.BankListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.AddBankCardResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.BankListResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class AddBankCardPresenter extends BasePresenter<AddBankCardContract.Model, AddBankCardContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;

    @Inject
    public AddBankCardPresenter(AddBankCardContract.Model model, AddBankCardContract.View rootView) {
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
    public void getBankList(){
        Cache<String,Object> cache= ArmsUtils.obtainAppComponentFromContext(ArmsUtils.getContext()).extras();

        BankListRequest request = new BankListRequest();
        mModel.getBankList(request)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();//显示下拉刷新的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();
                })
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new Consumer<BankListResponse>() {
                    @Override
                    public void accept(BankListResponse response) throws Exception {
                        if (response.isSuccess()) {
                            mRootView.updateBankList(response.getBankList());
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    public void addBankCard(String bankName,String cardNo,String name,String id){
        AddBankCardRequest request = new AddBankCardRequest();
        BankCardBean bankCardBean = new BankCardBean();
        bankCardBean.setBankName(bankName);
        bankCardBean.setBankId(id);
        bankCardBean.setIsDefaultIn("0");
        bankCardBean.setCardNo(cardNo);
        bankCardBean.setName(name);
        request.setBankCard(bankCardBean);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");
        request.setToken(token);

        mModel.addBankCard(request)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    mRootView.showLoading();//显示下拉刷新的进度条
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    mRootView.hideLoading();//隐藏下拉刷新的进度条
                })
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<AddBankCardResponse>(mErrorHandler) {
                    @Override
                    public void onNext(AddBankCardResponse response) {
                        if (response.isSuccess()) {
                            ArmsUtils.makeText(ArmsUtils.getContext(), "添加成功");
                            mRootView.killMyself();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

}
