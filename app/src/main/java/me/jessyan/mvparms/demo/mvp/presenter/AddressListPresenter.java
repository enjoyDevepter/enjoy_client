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
import me.jessyan.mvparms.demo.mvp.contract.AddressListContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Address;
import me.jessyan.mvparms.demo.mvp.model.entity.request.AddressListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DelAddressRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ModifyAddressRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.AddressListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.ui.adapter.AddressEditListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.AddressListAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class AddressListPresenter extends BasePresenter<AddressListContract.Model, AddressListContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AddressListAdapter addressListAdapter;
    @Inject
    AddressEditListAdapter addressEditListAdapter;
    @Inject
    List<Address> addressList;

    @Inject
    public AddressListPresenter(AddressListContract.Model model, AddressListContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onCreate() {
        getAddressList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    public void delAddress(String addressId, int delIndex) {
        DelAddressRequest request = new DelAddressRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        request.setToken(String.valueOf(cache.get(KEY_KEEP + "token")));
        request.setId(addressId);

        mModel.delAddress(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.isSuccess()) {
                            addressList.remove(delIndex);
                            addressEditListAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    public void getAddressList() {
        AddressListRequest request = new AddressListRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        request.setToken(String.valueOf(cache.get(KEY_KEEP + "token")));
        request.setPageIndex(1);
        request.setPageSize(10);

        mModel.getAddressList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<AddressListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(AddressListResponse response) {
                        if (response.isSuccess()) {
                            addressList.clear();
                            addressList.addAll(response.getMemberAddressList());
                            if (addressList.size() <= 0) {
                                return;
                            }
                            addressListAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    private void modifyAddress(Address address) {
        ModifyAddressRequest request = new ModifyAddressRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        request.setCmd(205);
        request.setToken(String.valueOf(cache.get(KEY_KEEP + "token")));
        request.setMemberAddress(address);
        mModel.modifyAddress(request)
                .subscribeOn(Schedulers.io())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.isSuccess()) {
                            addressEditListAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }


    public void refreshList(int position) {
        if ("1".equals(addressList.get(position).getIsDefaultIn())) {
            return;
        }
        for (int i = 0; i < addressList.size(); i++) {
            Address address = addressList.get(i);
            if (i == position) {
                address.setIsDefaultIn("1");
                modifyAddress(address);
            } else if ("1".equals(address.getIsDefaultIn())) {
                address.setIsDefaultIn("0");
                modifyAddress(address);
            }
        }
    }
}
