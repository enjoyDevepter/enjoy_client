package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.SearchContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Category;
import me.jessyan.mvparms.demo.mvp.model.entity.request.HotRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.SimpleRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.CategoryResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HotResponse;
import me.jessyan.mvparms.demo.mvp.ui.adapter.SearchTypeAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@ActivityScope
public class SearchPresenter extends BasePresenter<SearchContract.Model, SearchContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<String> hotList;
    @Inject
    List<Category> categoryList;
    @Inject
    TagAdapter mAdapter;
    @Inject
    SearchTypeAdapter typeAdapter;

    @Inject
    public SearchPresenter(SearchContract.Model model, SearchContract.View rootView) {
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


    public void getHot(String province, String city, String county) {
        HotRequest request = new HotRequest();
        request.setCity(city);
        request.setCounty(county);
        request.setProvince(province);
        mModel.getHot(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<HotResponse>() {
                    @Override
                    public void accept(HotResponse response) throws Exception {
                        if (response.isSuccess()) {
                            hotList.clear();
                            for (HotResponse.Key key : response.getGoodsSearchKeywordList()) {
                                hotList.add(key.getName());
                            }
                            mAdapter.notifyDataChanged();
                        }
                    }
                });
    }

    public void goSearch(String type, String keywords) {

    }


    public void getCategory() {

        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        if (cache.get("category") != null) {
            categoryList.clear();
            categoryList.addAll((List<Category>) cache.get("category"));
            typeAdapter.notifyDataSetChanged();
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
                            categoryList.addAll(sortCategory(response.getGoodsCategoryList()));
                            typeAdapter.notifyDataSetChanged();
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
        return categories;
    }
}
