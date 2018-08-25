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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.RecommendContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Category;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GoodsListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.SimpleRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.CategoryResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsListResponse;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsListAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


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
    GoodsListAdapter mAdapter;
    @Inject
    List<Category> categories;
    private int preEndIndex;
    private int lastPageIndex = 1;

    @Inject
    public RecommendPresenter(RecommendContract.Model model, RecommendContract.View rootView) {
        super(model, rootView);
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onCreate() {
        getCategory();
        getRecommendGoodsList(true);
    }

    public void getCategory() {
        SimpleRequest request = new SimpleRequest();
        request.setCmd(401);
        mModel.getCategory(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CategoryResponse>() {
                    @Override
                    public void accept(CategoryResponse response) throws Exception {
                        if (response.isSuccess()) {
                            sortCategory(response.getGoodsCategoryList());
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    public void getRecommendGoodsList(boolean pullToRefresh) {
        GoodsListRequest request = new GoodsListRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setCmd(404);
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
                }).subscribe(new Consumer<GoodsListResponse>() {
            @Override
            public void accept(GoodsListResponse response) throws Exception {
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
            allCategory.setCatagories(new ArrayList<>());
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
