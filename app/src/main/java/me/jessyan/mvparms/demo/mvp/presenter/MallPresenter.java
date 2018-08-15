package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;

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
import me.jessyan.mvparms.demo.mvp.contract.MallContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Category;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GoodsListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.SimpleRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.CategoryResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HGoodsListResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.CartActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.LoginActivity;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HGoodsListAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class MallPresenter extends BasePresenter<MallContract.Model, MallContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<Goods> mGoods;
    @Inject
    List<HGoodsListResponse.HGoods> mHGoods;
    @Inject
    List<Category> categories;
    @Inject
    GoodsListAdapter mAdapter;
    @Inject
    HGoodsListAdapter mHAdapter;

    @Inject
    public MallPresenter(MallContract.Model model, MallContract.View rootView) {
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

    public void getCategory() {

        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        if (cache.get("category") != null) {
            mRootView.refreshNaviTitle((List<Category>) cache.get("category"));
            return;
        }
        SimpleRequest request = new SimpleRequest();
        request.setCmd(401);
        mModel.getCategory(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CategoryResponse>() {
                    @Override
                    public void accept(CategoryResponse response) throws Exception {
                        if (response.isSuccess()) {
                            mRootView.refreshNaviTitle(sortCategory(response.getGoodsCategoryList()));
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }


    public void goCart() {
        Cache<String, Object> appCache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        if (ArmsUtils.isEmpty((String) appCache.get("token"))) {
            ArmsUtils.startActivity(LoginActivity.class);
            return;
        }
        ArmsUtils.startActivity(CartActivity.class);
    }

    public void getGoodsList() {
        GoodsListRequest request = new GoodsListRequest();
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

        mModel.getGoodsList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GoodsListResponse>() {
                    @Override
                    public void accept(GoodsListResponse response) throws Exception {
                        if (response.isSuccess()) {
                            mGoods.clear();
                            mGoods.addAll(response.getGoodsList());
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    public void getHGoodsList() {
        GoodsListRequest request = new GoodsListRequest();
        request.setCmd(440);
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

        mModel.getHGoodsList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HGoodsListResponse>() {
                    @Override
                    public void accept(HGoodsListResponse response) throws Exception {
                        if (response.isSuccess()) {
                            mHGoods.clear();
                            mHGoods.addAll(response.getGoodsList());
                            mHAdapter.notifyDataSetChanged();
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
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        cache.put("category", categories);
        this.categories.clear();
        this.categories.addAll(categories.get(0).getCatagories());
        return categories;
    }

}
