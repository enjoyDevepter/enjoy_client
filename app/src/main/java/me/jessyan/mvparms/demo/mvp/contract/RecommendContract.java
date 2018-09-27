package me.jessyan.mvparms.demo.mvp.contract;

import android.app.Activity;

import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;

import java.util.List;

import io.reactivex.Observable;
import me.jessyan.mvparms.demo.mvp.model.entity.Category;
import me.jessyan.mvparms.demo.mvp.model.entity.request.GoodsListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.SimpleRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.CategoryResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HGoodsListResponse;


public interface RecommendContract {
    //对于经常使用的关于UI的方法可以定义到IView中,如显示隐藏进度条,和显示文字消息
    interface View extends IView {

        void startLoadMore();

        void endLoadMore();

        void setLoadedAllItems(boolean has);

        Activity getActivity();

        Cache getCache();

        void showError(boolean hasDate);

        void refreshNaviTitle(List<Category> categories);

    }

    //Model层定义接口,外部只需关心Model返回的数据,无需关心内部细节,即是否使用缓存
    interface Model extends IModel {

        Observable<CategoryResponse> getCategory(SimpleRequest request);

        Observable<GoodsListResponse> getRecommendGoodsList(GoodsListRequest request);

        Observable<HGoodsListResponse> getHGoodsList(GoodsListRequest request);

    }
}
