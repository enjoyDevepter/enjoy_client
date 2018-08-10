package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.SelfPickupAddrListContract;
import me.jessyan.mvparms.demo.mvp.model.entity.AreaAddress;
import me.jessyan.mvparms.demo.mvp.model.entity.request.SimpleRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.AllAddressResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class SelfPickupAddrListPresenter extends BasePresenter<SelfPickupAddrListContract.Model, SelfPickupAddrListContract.View> {
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
    public SelfPickupAddrListPresenter(SelfPickupAddrListContract.Model model, SelfPickupAddrListContract.View rootView) {
        super(model, rootView);
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
                .subscribe(new Consumer<AllAddressResponse>() {
                    @Override
                    public void accept(AllAddressResponse response) throws Exception {
                        if (response.isSuccess()) {
                            addressList.clear();
                            addressList.addAll(response.getAreaList());
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
