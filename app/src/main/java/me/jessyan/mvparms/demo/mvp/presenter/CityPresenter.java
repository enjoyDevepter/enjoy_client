package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.yu.bundles.extended.recyclerview.ExtendedNode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.CityContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Area;
import me.jessyan.mvparms.demo.mvp.model.entity.request.SimpleRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.CityResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class CityPresenter extends BasePresenter<CityContract.Model, CityContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;

    @Inject
    public CityPresenter(CityContract.Model model, CityContract.View rootView) {
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


    public void getCities() {

        SimpleRequest request = new SimpleRequest();
        request.setCmd(901);
        mModel.getCity(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CityResponse>() {
                    @Override
                    public void accept(CityResponse response) throws Exception {
                        if (response.isSuccess()) {
                            dealData(response.getAreaList());
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    private void dealData(List<Area> areaList) {
        if (areaList != null) {
            ArrayList<ExtendedNode> roots = new ArrayList<>();
            for (Area area : areaList) {
                if ("0".equals(area.getParentId())) {
                    roots.add(new ExtendedNode<>(area, false));
                }
            }
            for (ExtendedNode<Area> node : roots) {
                for (Area area : areaList) {
                    ArrayList<ExtendedNode> childs = new ArrayList<>();
                    if (area.getParentId().equals(node.data.getId())) {
                        childs.add(new ExtendedNode<>(area, false));
                    }
                    node.addSons(childs);
                    for (ExtendedNode<Area> grandson : childs) {
                        ArrayList<ExtendedNode> grandsons = new ArrayList<>();
                        for (Area area1 : areaList) {
                            if (area1.getParentId().equals(grandson.data.getId())) {
                                grandsons.add(new ExtendedNode<>(area1, false));
                            }
                        }
                        grandson.addSons(grandsons);
                    }
                }
            }
            mRootView.onRefresh(roots);
        }
    }

}
