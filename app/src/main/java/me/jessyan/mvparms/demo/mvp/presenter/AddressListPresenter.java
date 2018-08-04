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

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.AddressListContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Address;
import me.jessyan.mvparms.demo.mvp.model.entity.request.AddressListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DelAddressRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.AddressListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.ui.adapter.AddressEditListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.AddressListAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


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

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
//        getAddressList();
        for (int i = 0; i < 3; i++) {
            Address address = new Address();
            address.setAddress("ajfdksjfsljfsljfslfjs");
            address.setPhone("123112313");
            address.setReceiverName("测试");
            if (i == 0) {
                address.setIsDefaultIn("1");
            }
            addressList.add(address);
        }
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
        request.setToken(String.valueOf(cache.get("token")));
        request.setId(addressId);

        mModel.delAddress(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse response) throws Exception {
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
        request.setToken(String.valueOf(cache.get("token")));
        request.setPageIndex(1);
        request.setPageSize(10);

        mModel.getAddressList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AddressListResponse>() {
                    @Override
                    public void accept(AddressListResponse response) throws Exception {
                        if (response.isSuccess()) {
                            addressList = response.getMemberAddressList();
                            addressListAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    public void refreshList(int position) {
        for (int i = 0; i < addressList.size(); i++) {
            if (i == position) {
                addressList.get(i).setIsDefaultIn("1");
            } else {
                addressList.get(i).setIsDefaultIn("0");
            }
        }
        addressEditListAdapter.notifyDataSetChanged();
    }
}
