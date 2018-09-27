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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.RecommendContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Category;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.HGoods;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GoodsListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.SimpleRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.CategoryResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HGoodsListResponse;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HGoodsListAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;


@ActivityScope
public class RecommendPresenter extends BasePresenter<RecommendContract.Model, RecommendContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager mAppManager;
    @Inject
    List<Goods> mGoods;
    @Inject
    List<HGoods> mHGoods;
    @Inject
    GoodsListAdapter mAdapter;
    @Inject
    HGoodsListAdapter mHAdapter;
    @Inject
    List<Category> categories;
    private int preEndIndex;
    private int lastPageIndex = 1;

    @Inject
    public RecommendPresenter(RecommendContract.Model model, RecommendContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        getCategory();
        boolean isGoods = mRootView.getActivity().getIntent().getBooleanExtra("isGoods", true);
        mRootView.getCache().put("isGoods", isGoods);
        if (isGoods) {
            getRecommendGoodsList(true);
        } else {
            getHGoodsList(true);
        }
    }

    public void getCategory() {
        SimpleRequest request = new SimpleRequest();
        request.setCmd(401);
        mModel.getCategory(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<CategoryResponse>(mErrorHandler) {
                    @Override
                    public void onNext(CategoryResponse response) {
                        if (response.isSuccess()) {
                            mRootView.refreshNaviTitle(sortCategory(response.getGoodsCategoryList()));
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    public void getRecommendGoodsList(boolean pullToRefresh) {
        GoodsListRequest request = new GoodsListRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        if ((boolean) mRootView.getCache().get("isGoods")) {
            request.setCmd(404);
        } else {
            request.setCmd(480);
        }
        request.setProvince(String.valueOf(cache.get("province")));
        request.setCity(String.valueOf(cache.get("city")));
        request.setCounty(String.valueOf(cache.get("county")));
        request.setCategoryId((String) mRootView.getCache().get("categoryId"));
        request.setSecondCategoryId((String) (mRootView.getCache().get("secondCategoryId")));
        request.setFirstCategoryId((String) (mRootView.getCache().get("firstCategoryId")));
        if (!ArmsUtils.isEmpty(String.valueOf(mRootView.getCache().get("orderByField")))) {
            GoodsListRequest.OrderBy orderBy = new GoodsListRequest.OrderBy();
            orderBy.setField((String) mRootView.getCache().get("orderByField"));
            orderBy.setAsc((Boolean) mRootView.getCache().get("orderByAsc"));
            request.setOrderBy(orderBy);
        }

        if (pullToRefresh) lastPageIndex = 1;
        request.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页

        mModel.getRecommendGoodsList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    if (pullToRefresh)
                        mRootView.showLoading();//显示下拉刷新的进度条
                    else
                        mRootView.startLoadMore();//显示上拉加载更多的进度条
                })
                .doFinally(() -> {
                    if (pullToRefresh)
                        mRootView.hideLoading();//隐藏下拉刷新的进度条
                    else
                        mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                })
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<GoodsListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(GoodsListResponse response) {
                        if (response.isSuccess()) {
                            if (pullToRefresh) {
                                mGoods.clear();
                            }
                            mRootView.showError(response.getGoodsList().size() > 0);
                            mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                            mGoods.addAll(response.getGoodsList());
                            preEndIndex = mGoods.size();//更新之前列表总长度,用于确定加载更多的起始位置
                            lastPageIndex = mGoods.size() / 10;
                            if (pullToRefresh) {
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.notifyItemRangeInserted(preEndIndex, mGoods.size());
                            }
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    public void getHGoodsList(final boolean pullToRefresh) {
        GoodsListRequest request = new GoodsListRequest();
        request.setCmd(480);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setProvince(String.valueOf(cache.get("province")));
        request.setCity(String.valueOf(cache.get("city")));
        request.setCounty(String.valueOf(cache.get("county")));
        request.setCategoryId((String) mRootView.getCache().get("categoryId"));
        request.setSecondCategoryId((String) (mRootView.getCache().get("secondCategoryId")));

        if (!ArmsUtils.isEmpty(String.valueOf(mRootView.getCache().get("orderByField")))) {
            GoodsListRequest.OrderBy orderBy = new GoodsListRequest.OrderBy();
            orderBy.setField((String) mRootView.getCache().get("orderByField"));
            orderBy.setAsc((Boolean) mRootView.getCache().get("orderByAsc"));
            request.setOrderBy(orderBy);
        }
        if (pullToRefresh) lastPageIndex = 1;
        request.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页

        mModel.getHGoodsList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .doOnSubscribe(disposable -> {
                    if (pullToRefresh)
                        mRootView.showLoading();//显示下拉刷新的进度条
                    else
                        mRootView.startLoadMore();//显示上拉加载更多的进度条
                })
                .doFinally(() -> {
                    if (pullToRefresh)
                        mRootView.hideLoading();//隐藏下拉刷新的进度条
                    else
                        mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                })
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<HGoodsListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(HGoodsListResponse response) {
                        if (response.isSuccess()) {
                            if (pullToRefresh) {
                                mHGoods.clear();
                            }
                            mRootView.showError(response.getGoodsList().size() > 0);
                            if (response.getGoodsList().size() <= 0) {
                                return;
                            }
                            mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                            mHGoods.addAll(response.getGoodsList());
                            preEndIndex = mHGoods.size();//更新之前列表总长度,用于确定加载更多的起始位置
                            lastPageIndex = mHGoods.size() / 10;
                            if (pullToRefresh) {
                                mHAdapter.notifyDataSetChanged();
                            } else {
                                mHAdapter.notifyItemRangeInserted(preEndIndex, mHGoods.size());
                            }
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    private List<Category> sortCategory(List<Category> categoryList) {
        List<Category> categories = new ArrayList<>();

        for (Category category : categoryList) {
            if ("0".equals(category.getParentId())) {
                category.setCatagories(new ArrayList<>());
                categories.add(category);
            }
        }

        for (Category category : categories) {
            Category allCategory = new Category();
            allCategory.setChoice(true);
            allCategory.setName("全部项目");
            allCategory.setId("");
            allCategory.setBusType("");
            allCategory.setParentId("");
            ArrayList childList = new ArrayList();
            Category allChildCategory = new Category();
            allChildCategory.setChoice(true);
            allChildCategory.setName("全部");
            allChildCategory.setId("");
            allChildCategory.setBusType("");
            allChildCategory.setParentId("");
            childList.add(allChildCategory);
            allCategory.setCatagories(childList);
            category.getCatagories().add(allCategory);
            // 子
            for (Category child : categoryList) {
                if (child.getParentId().equals(category.getId())) {
                    child.setCatagories(new ArrayList<>());
                    category.getCatagories().add(child);
                }
            }
            // 孙
            for (Category child : category.getCatagories()) {
                for (Category grandson : categoryList) {
                    if (child.getId().equals(grandson.getParentId())) {
                        child.getCatagories().add(grandson);
                    }
                }
            }
        }
        this.categories.clear();
        this.categories.addAll(categories.get(0).getCatagories());
        return categories;
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
