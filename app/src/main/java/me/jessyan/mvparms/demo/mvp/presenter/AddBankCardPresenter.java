package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;

import com.jess.arms.integration.AppManager;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.BankCardBean;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.AddBankCardRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.AddBankCardResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import javax.inject.Inject;

import me.jessyan.mvparms.demo.mvp.contract.AddBankCardContract;

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

    public void addBankCard(String bankName,String cardNo,String name){
        AddBankCardRequest request = new AddBankCardRequest();
        BankCardBean bankCardBean = new BankCardBean();
        bankCardBean.setBankName(bankName);
        bankCardBean.setCardNo(cardNo);
        bankCardBean.setName(name);
        request.setBankCard(bankCardBean);
        Cache<String,Object> cache= ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token=(String)cache.get(KEY_KEEP+"token");
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
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new Consumer<AddBankCardResponse>() {
                    @Override
                    public void accept(AddBankCardResponse response) throws Exception {
                        if (response.isSuccess()) {
                            ArmsUtils.makeText(ArmsUtils.getContext(),"添加成功");
                            mRootView.killMyself();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });

    }

}
