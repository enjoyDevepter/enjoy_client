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

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.AddAddressContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Address;
import me.jessyan.mvparms.demo.mvp.model.entity.AreaAddress;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ModifyAddressRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.SimpleRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.AllAddressResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class AddAddressPresenter extends BasePresenter<AddAddressContract.Model, AddAddressContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<AreaAddress> addressList;

    @Inject
    public AddAddressPresenter(AddAddressContract.Model model, AddAddressContract.View rootView) {
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
    void onCreate() {
        getAllAddressList();
    }


    private void getAllAddressList() {
        SimpleRequest request = new SimpleRequest();
        request.setCmd(902);

        mModel.getAllAddressList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<AllAddressResponse>(mErrorHandler) {
                    @Override
                    public void onNext(AllAddressResponse response) {
                        if (response.isSuccess()) {
                            addressList.clear();
                            addressList.addAll(response.getAreaList());
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    public void modifyAddress(boolean add) {
        ModifyAddressRequest request = new ModifyAddressRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        if (add) {
            request.setCmd(204);
        } else {
            request.setCmd(205);
        }
        request.setToken(String.valueOf(cache.get(KEY_KEEP + "token")));
        Address address = new Address();
        address.setAddress((String) mRootView.getCache().get("address"));
        address.setReceiverName((String) mRootView.getCache().get("receiverName"));
        address.setPhone((String) mRootView.getCache().get("phone"));
        address.setProvince((String) mRootView.getCache().get("province"));
        address.setCity((String) mRootView.getCache().get("city"));
        address.setCounty((String) mRootView.getCache().get("county"));
        address.setAddressId((String) mRootView.getCache().get("addressId"));
        address.setIsDefaultIn((String) mRootView.getCache().get("isDefaultIn"));

        if (ArmsUtils.isEmpty(address.getAddress())
                || ArmsUtils.isEmpty(address.getReceiverName())
                || ArmsUtils.isEmpty(address.getPhone())
                || ArmsUtils.isEmpty(address.getProvince())
                || ArmsUtils.isEmpty(address.getCity())
                || ArmsUtils.isEmpty(address.getCounty())) {
            mRootView.showMessage("请完善相关地址信息");
            return;
        }

        mRootView.showLoading();
        request.setMemberAddress(address);
        mModel.modifyAddress(request)
                .subscribeOn(Schedulers.io())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        mRootView.hideLoading();
                        if (response.isSuccess()) {
                            mRootView.killMyself();
                            mRootView.showMessage(response.getRetDesc());
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

}
