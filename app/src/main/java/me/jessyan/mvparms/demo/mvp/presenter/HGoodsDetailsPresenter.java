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
import me.jessyan.mvparms.demo.mvp.contract.HGoodsDetailsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Diary;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.Promotion;
import me.jessyan.mvparms.demo.mvp.model.entity.request.CollectGoodsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryForGoodsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GoodsDetailsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HGoodsDetailsResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.HGoodsOrderConfirmActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.LoginActivity;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DiaryListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsPromotionAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class HGoodsDetailsPresenter extends BasePresenter<HGoodsDetailsContract.Model, HGoodsDetailsContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<Promotion> promotionList;
    @Inject
    GoodsPromotionAdapter goodsPromotionAdapter;
    @Inject
    DiaryListAdapter mAdapter;
    @Inject
    List<Diary> diaryList;
    HGoodsDetailsResponse hGoodsDetailsResponse;
    private int preEndIndex;
    private int lastPageIndex = 1;

    @Inject
    public HGoodsDetailsPresenter(HGoodsDetailsContract.Model model, HGoodsDetailsContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        getHGoodsDetails();
    }

    /**
     * 获取商品详情
     */
    public void getHGoodsDetails() {

        GoodsDetailsRequest request = new GoodsDetailsRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        String token = (String) cache.get(KEY_KEEP + "token");
        String promotionId = mRootView.getActivity().getIntent().getStringExtra("promotionId");
        String advanceDepositId = mRootView.getActivity().getIntent().getStringExtra("advanceDepositId");
        String where = mRootView.getActivity().getIntent().getStringExtra("where");
        if ("timelimitdetail".equals(where)) {
            request.setPromotionId(promotionId);
            if (ArmsUtils.isEmpty(token)) {
                request.setCmd(465);
            } else {
                request.setCmd(466);
            }
        } else if ("newpeople".equals(where)) {
            request.setPromotionId(promotionId);
            if (ArmsUtils.isEmpty(token)) {
                request.setCmd(455);
            } else {
                request.setCmd(456);
            }
        } else {

            if (ArmsUtils.isEmpty(advanceDepositId)) {
                if (ArmsUtils.isEmpty(token)) {
                    request.setCmd(445);
                } else {
                    request.setCmd(446);
                }
            } else {
                if (ArmsUtils.isEmpty(token)) {
                    request.setCmd(441);
                } else {
                    request.setCmd(442);
                }
            }
        }
        request.setToken(token);
        request.setCity((String) (cache.get("city")));
        request.setCounty((String) (cache.get("county")));
        request.setProvince((String) (cache.get("province")));
        request.setGoodsId(mRootView.getActivity().getIntent().getStringExtra("goodsId"));
        request.setMerchId(mRootView.getActivity().getIntent().getStringExtra("merchId"));
        request.setAdvanceDepositId(advanceDepositId);
        mModel.getHGoodsDetails(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<HGoodsDetailsResponse>(mErrorHandler) {
                    @Override
                    public void onNext(HGoodsDetailsResponse response) {
                        if (response.isSuccess()) {
                            hGoodsDetailsResponse = response;
                            promotionList.clear();
                            if (null != response.getPromotionList() && response.getPromotionList().size() > 0) {
                                promotionList.addAll(response.getPromotionList());
                                promotionList.get(0).setCheck(true);
                            }
                            mRootView.updateUI(response);
                            goodsPromotionAdapter.notifyDataSetChanged();
                            getGoodsForDiary();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    public void getHGoodsDetailsForSpecValueId() {
        GoodsDetailsRequest request = new GoodsDetailsRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        String advanceDepositId = mRootView.getActivity().getIntent().getStringExtra("advanceDepositId");
        String token = (String) cache.get(KEY_KEEP + "token");
        if (ArmsUtils.isEmpty(advanceDepositId)) {
            if (ArmsUtils.isEmpty(token)) {
                request.setCmd(447);
            } else {
                request.setCmd(448);
            }
        } else {
            if (ArmsUtils.isEmpty(token)) {
                request.setCmd(443);
            } else {
                request.setCmd(444);
            }
        }

        request.setToken(token);
        request.setCity((String) (cache.get("city")));
        request.setCounty((String) (cache.get("county")));
        request.setProvince((String) (cache.get("province")));
        request.setGoodsId(mRootView.getActivity().getIntent().getStringExtra("goodsId"));
        request.setMerchId((String) (mRootView.getCache().get("merchId")));
        request.setSpecValueId((String) (mRootView.getCache().get("specValueId")));
        request.setAdvanceDepositId(advanceDepositId);
        mModel.getHGoodsDetails(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<HGoodsDetailsResponse>(mErrorHandler) {
                    @Override
                    public void onNext(HGoodsDetailsResponse response) {
                        if (response.isSuccess()) {
                            hGoodsDetailsResponse = response;
                            promotionList.clear();
                            promotionList.addAll(response.getPromotionList());
                            mRootView.updateUI(response);
                            goodsPromotionAdapter.notifyDataSetChanged();
                        } else {
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
                            mRootView.updateDiaryUI(response.getDiaryList().size() > 0);
                            preEndIndex = diaryList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                            lastPageIndex = diaryList.size() / 10 == 0 ? 1 : diaryList.size() / 10;
                            if (lastPageIndex == 1) {
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.notifyItemRangeInserted(preEndIndex, diaryList.size());
                            }
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }


    public void goOrderConfirm() {
        String specValueId = (String) mRootView.getCache().get("specValueId");
        if (ArmsUtils.isEmpty(specValueId)) {
            mRootView.showMessage("请选择规格！");
            return;
        }

        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        String token = (String) cache.get(KEY_KEEP + "token");
        if (ArmsUtils.isEmpty(token)) {
            ArmsUtils.startActivity(LoginActivity.class);
            return;
        }

        String advanceDepositId = mRootView.getActivity().getIntent().getStringExtra("advanceDepositId");

        Intent intent = new Intent(mRootView.getActivity(), HGoodsOrderConfirmActivity.class);
        ArrayList<Goods> goodsList = new ArrayList<>();
        Goods goods = hGoodsDetailsResponse.getGoods();
        goods.setNums(1);
        goodsList.add(goods);
        intent.putExtra("where", mRootView.getActivity().getIntent().getStringExtra("where"));
        intent.putParcelableArrayListExtra("goodsList", goodsList);
        intent.putExtra("payAll", ArmsUtils.isEmpty(advanceDepositId));
        ArmsUtils.startActivity(intent);
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
                            mRootView.updateCollect(collect);
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
