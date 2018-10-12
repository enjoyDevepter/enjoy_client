package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.GoodsDetailsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Diary;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.Promotion;
import me.jessyan.mvparms.demo.mvp.model.entity.request.AddGoodsToCartRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.CollectGoodsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryForGoodsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GoodsDetailsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsDetailsResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.ConfirmOrderActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.LoginActivity;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DiaryListAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class GoodsDetailsPresenter extends BasePresenter<GoodsDetailsContract.Model, GoodsDetailsContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    GoodsDetailsResponse goodsDetailsResponse;
    @Inject
    List<Promotion> promotionList;
    @Inject
    DiaryListAdapter mAdapter;
    @Inject
    List<Diary> diaryList;

    private int preEndIndex;
    private int lastPageIndex = 1;

    @Inject
    public GoodsDetailsPresenter(GoodsDetailsContract.Model model, GoodsDetailsContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        getGoodsDetails();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

    /**
     * 获取商品详情
     */
    public void getGoodsDetails() {
        GoodsDetailsRequest request = new GoodsDetailsRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        String promotionId = mRootView.getActivity().getIntent().getStringExtra("promotionId");
        String token = (String) cache.get(KEY_KEEP + "token");
        String where = mRootView.getActivity().getIntent().getStringExtra("where");
        if ("timelimitdetail".equals(where)) {
            request.setPromotionId(promotionId);
            if (ArmsUtils.isEmpty(token)) {
                request.setCmd(461);
            } else {
                request.setCmd(462);
            }
        } else if ("newpeople".equals(where)) {
            request.setPromotionId(promotionId);
            if (ArmsUtils.isEmpty(token)) {
                request.setCmd(451);
            } else {
                request.setCmd(452);
            }
        } else if ("recom".equals(where)) {
            if (ArmsUtils.isEmpty(token)) {
                request.setCmd(405);
            } else {
                request.setCmd(415);
            }
        } else {
            if (ArmsUtils.isEmpty(token)) {
                request.setCmd(403);
            } else {
                request.setCmd(413);
            }
        }
        request.setToken(token);
        request.setCity((String) (cache.get("city")));
        request.setCounty((String) (cache.get("county")));
        request.setProvince((String) (cache.get("province")));
        request.setGoodsId(mRootView.getActivity().getIntent().getStringExtra("goodsId"));
        request.setMerchId(mRootView.getActivity().getIntent().getStringExtra("merchId"));
        mModel.getGoodsDetails(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<GoodsDetailsResponse>(mErrorHandler) {
                    @Override
                    public void onNext(GoodsDetailsResponse response) {
                        if (response.isSuccess()) {
                            goodsDetailsResponse = response;
                            promotionList.clear();
                            if (null != response.getPromotionList() && response.getPromotionList().size() > 0) {
                                promotionList.addAll(response.getPromotionList());
                                promotionList.get(0).setCheck(true);
                            }
                            Cache<String, Object> cache = mRootView.getCache();
                            cache.put("goods", response.getGoods());
                            mRootView.updateUI(response);
//                            getGoodsForDiary(); // 临时去掉
                        }
                    }
                });
    }

    public void collectGoods(boolean collect) {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        if (cache.get(KEY_KEEP + "token") == null) {
            ArmsUtils.startActivity(LoginActivity.class);
            return;
        }

        CollectGoodsRequest request = new CollectGoodsRequest();
        request.setCmd(collect ? 407 : 408);
        request.setToken((String) cache.get(KEY_KEEP + "token"));
        request.setGoodsId(mRootView.getActivity().getIntent().getStringExtra("goodsId"));
        request.setMerchId(mRootView.getActivity().getIntent().getStringExtra("merchId"));
        mModel.collectGoods(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.isSuccess()) {
                            if (response.getCmd() == 407) {
                                mRootView.showMessage("收藏成功");
                            } else {
                                mRootView.showMessage("取消收藏");
                            }
                            mRootView.updateCollect(collect);
                        }
                    }
                });
    }


    /**
     * 更加规格获取商品详情
     */
    public void getCoodsDetailsForSpecValueId() {
        GoodsDetailsRequest request = new GoodsDetailsRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        String token = (String) cache.get(KEY_KEEP + "token");
        if (ArmsUtils.isEmpty(token)) {
            request.setCmd(409);
        } else {
            request.setCmd(419);
        }
        request.setToken(token);
        request.setCity((String) (cache.get("city")));
        request.setCounty((String) (cache.get("county")));
        request.setProvince((String) (cache.get("province")));
        request.setGoodsId(mRootView.getActivity().getIntent().getStringExtra("goodsId"));
        request.setMerchId((String) mRootView.getCache().get("merchId"));
        request.setSpecValueId((String) (mRootView.getCache().get("specValueId")));
        mModel.getGoodsDetails(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<GoodsDetailsResponse>(mErrorHandler) {
                    @Override
                    public void onNext(GoodsDetailsResponse response) {
                        if (response.isSuccess()) {
                            goodsDetailsResponse = response;
                            promotionList.clear();
                            promotionList.addAll(response.getPromotionList());
                            Cache<String, Object> cache = mRootView.getCache();
                            cache.put("goods", response.getGoods());
                            mRootView.updateUI(response);
                        }
                    }
                });
    }

    /**
     * 添加购物车
     */
    public void addGoodsToCart() {
        Cache<String, Object> appCache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        if (ArmsUtils.isEmpty((String) appCache.get(KEY_KEEP + "token"))) {
            ArmsUtils.startActivity(LoginActivity.class);
            return;
        }
        AddGoodsToCartRequest request = new AddGoodsToCartRequest();
        request.setGoodsId(mRootView.getActivity().getIntent().getStringExtra("goodsId"));
        request.setMerchId(mRootView.getActivity().getIntent().getStringExtra("merchId"));
        request.setNums(1);
        request.setPromotionId((String) mRootView.getCache().get("promotionId"));
        request.setToken((String) appCache.get(KEY_KEEP + "token"));
        mModel.addGoodsToCart(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.isSuccess()) {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    public void getGoodsForDiary() {
        DiaryForGoodsRequest request = new DiaryForGoodsRequest();

        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        String token = (String) (cache.get(KEY_KEEP + "token"));
        if (ArmsUtils.isEmpty(token)) {
            request.setCmd(819);
        } else {
            request.setCmd(820);
        }
        request.setGoodsId(mRootView.getActivity().getIntent().getStringExtra("goodsId"));
        request.setMerchId(mRootView.getActivity().getIntent().getStringExtra("merchId"));
        request.setToken(token);

        request.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页

        mModel.getDiaryForGoodsIdList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    mRootView.startLoadMore();//显示上拉加载更多的进度条
                })
                .doFinally(() -> {
                    mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                })
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<DiaryListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(DiaryListResponse response) {
                        if (response.isSuccess()) {
                            if (lastPageIndex == 1) {
                                diaryList.clear();
                            }
                            mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                            diaryList.addAll(response.getDiaryList());
//                            mRootView.updateDiaryUI(response.getDiaryList().size() > 0);
                            mRootView.updateDiaryUI(false);
                            preEndIndex = diaryList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                            lastPageIndex = diaryList.size() / 10 == 0 ? 1 : diaryList.size() / 10;
                            if (lastPageIndex == 1) {
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.notifyItemRangeInserted(preEndIndex, diaryList.size());
                            }
                        }
                    }
                });
    }

    public void goOrderConfirm() {
        Cache<String, Object> appCache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        if (ArmsUtils.isEmpty((String) appCache.get(KEY_KEEP + "token"))) {
            ArmsUtils.startActivity(LoginActivity.class);
            return;
        }
        Intent intent = new Intent(mRootView.getActivity(), ConfirmOrderActivity.class);
        ArrayList<Goods> goodsList = new ArrayList<>();
        Goods goods = goodsDetailsResponse.getGoods();
        goods.setNums(1);
        goodsList.add(goods);
        intent.putExtra("where", mRootView.getActivity().getIntent().getStringExtra("where"));
        intent.putParcelableArrayListExtra("goodsList", goodsList);
        ArmsUtils.startActivity(intent);
    }

}
