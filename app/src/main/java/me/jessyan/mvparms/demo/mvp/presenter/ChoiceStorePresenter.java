package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.ChoiceStoreContract;
import me.jessyan.mvparms.demo.mvp.model.entity.CommonStoreDateType;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.OrderBy;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.request.HospitalListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.response.HospitalListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.request.StoresListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.StoresListResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.SelfPickupAddrListActivity;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import static me.jessyan.mvparms.demo.mvp.ui.activity.SelfPickupAddrListActivity.KEY_FOR_ACTIVITY_LIST_TYPE;


@ActivityScope
public class ChoiceStorePresenter extends BasePresenter<ChoiceStoreContract.Model, ChoiceStoreContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    RecyclerView.Adapter mAdapter;
    @Inject
    List<CommonStoreDateType> commonStoreDateTypeList;

    @Inject
    public ChoiceStorePresenter(ChoiceStoreContract.Model model, ChoiceStoreContract.View rootView) {
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
        SelfPickupAddrListActivity.ListType listType = (SelfPickupAddrListActivity.ListType) mRootView.getActivity().getIntent().getSerializableExtra(KEY_FOR_ACTIVITY_LIST_TYPE);
        switch (listType) {
            case HOP:
                getHospital();
                break;
            case STORE:
                getStores();
                break;
            case ADDR:
                break;
        }
    }

    private void getStores() {
        StoresListRequest request = new StoresListRequest();
        request.setCountyId(mRootView.getActivity().getIntent().getStringExtra("county"));
        request.setCityId(mRootView.getActivity().getIntent().getStringExtra("city"));
        request.setProvinceId(mRootView.getActivity().getIntent().getStringExtra("province"));

        List<StoresListRequest.OrderBy> orderByList = new ArrayList<>();
        StoresListRequest.OrderBy orderBy = new StoresListRequest.OrderBy();
        orderBy.setField("distance");
        orderBy.setAsc(false);
        orderByList.add(orderBy);
        request.setOrderBys(orderByList);
        mModel.getStores(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<StoresListResponse>() {
                    @Override
                    public void accept(StoresListResponse response) throws Exception {
                        if (response.isSuccess() && response.getStoreList() != null) {
                            commonStoreDateTypeList.clear();
                            commonStoreDateTypeList.addAll(response.getStoreList());
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    private void getHospital() {
        HospitalListRequest request = new HospitalListRequest();
        request.setCountyId(mRootView.getActivity().getIntent().getStringExtra("county"));
        request.setCityId(mRootView.getActivity().getIntent().getStringExtra("city"));
        request.setProvinceId(mRootView.getActivity().getIntent().getStringExtra("province"));
        request.setSpecValueId(mRootView.getActivity().getIntent().getStringExtra("specValueId"));
        List<OrderBy> orderByList = new ArrayList<>();
        OrderBy orderBy = new OrderBy();
        orderBy.setField("distance");
        orderBy.setAsc(false);
        orderByList.add(orderBy);
        request.setOrderBys(orderByList);
        mModel.getHospitals(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HospitalListResponse>() {
                    @Override
                    public void accept(HospitalListResponse response) throws Exception {
                        if (response.isSuccess() && response.getHospitalList() != null) {
                            commonStoreDateTypeList.clear();
                            commonStoreDateTypeList.addAll(response.getHospitalList());
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    public void choiceItem(int position) {
        for (int i = 0; i < commonStoreDateTypeList.size(); i++) {
            commonStoreDateTypeList.get(i).setCheck(i == position ? true : false);
        }
        mAdapter.notifyDataSetChanged();
    }

}
