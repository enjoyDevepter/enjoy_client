package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.DiaryImageContract;
import me.jessyan.mvparms.demo.mvp.model.entity.DiaryAlbum;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryImagesRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryImagesResponse;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DiaryImageAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;


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

    private int preEndIndex;
    private int lastPageIndex = 1;

    @Inject
    public DiaryImagePresenter(DiaryImageContract.Model model, DiaryImageContract.View rootView) {
        super(model, rootView);
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        getDiaryImages(true);
    }

    public void getDiaryImages(boolean pullToRefresh) {
        DiaryImagesRequest request = new DiaryImagesRequest();
        request.setGoodsId(mRootView.getActivity().getIntent().getStringExtra("goodsId"));
        request.setMemberId(mRootView.getActivity().getIntent().getStringExtra("memberId"));
        request.setMerchId(mRootView.getActivity().getIntent().getStringExtra("merchId"));

        if (pullToRefresh) lastPageIndex = 1;
        request.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页

        mModel.getDiaryImages(request)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    if (pullToRefresh)
                        mRootView.showLoading();//显示下拉刷新的进度条
                    else
                        mRootView.startLoadMore();//显示上拉加载更多的进度条
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(() -> {
                    if (pullToRefresh)
                        mRootView.hideLoading();//隐藏下拉刷新的进度条
                    else
                        mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                })
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<DiaryImagesResponse>(mErrorHandler) {
                    @Override
                    public void onNext(DiaryImagesResponse response) {
                        if (response.isSuccess()) {
                            if (pullToRefresh) {
                                diaryAlbumList.clear();
                            }
                            mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                            diaryAlbumList.addAll(response.getDiaryAlbumList());
                            preEndIndex = diaryAlbumList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                            lastPageIndex = diaryAlbumList.size() / 10 + 1;
                            if (pullToRefresh) {
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.notifyItemRangeInserted(preEndIndex, diaryAlbumList.size());
                            }
                            mAdapter.notifyDataSetChanged();
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
