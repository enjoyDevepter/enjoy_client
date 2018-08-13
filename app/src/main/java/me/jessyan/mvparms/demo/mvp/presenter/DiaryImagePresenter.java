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
import me.jessyan.mvparms.demo.mvp.contract.DiaryImageContract;
import me.jessyan.mvparms.demo.mvp.model.entity.DiaryAlbum;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryImagesRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryImagesResponse;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DiaryImageAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class DiaryImagePresenter extends BasePresenter<DiaryImageContract.Model, DiaryImageContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<DiaryAlbum> diaryAlbumList;
    @Inject
    DiaryImageAdapter mAdapter;

    @Inject
    public DiaryImagePresenter(DiaryImageContract.Model model, DiaryImageContract.View rootView) {
        super(model, rootView);
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        getDiaryImages();
    }

    private void getDiaryImages() {
        DiaryImagesRequest request = new DiaryImagesRequest();
        request.setGoodsId(mRootView.getActivity().getIntent().getStringExtra("goodsId"));
        request.setMemberId(mRootView.getActivity().getIntent().getStringExtra("memberId"));
        request.setMerchId(mRootView.getActivity().getIntent().getStringExtra("merchId"));
        mModel.getDiaryImages(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DiaryImagesResponse>() {
                    @Override
                    public void accept(DiaryImagesResponse response) throws Exception {
                        if (response.isSuccess()) {
                            diaryAlbumList.clear();
                            diaryAlbumList.addAll(response.getDiaryAlbumList());
                            mAdapter.notifyDataSetChanged();
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
