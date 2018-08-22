package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v7.widget.RecyclerView;

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
import me.jessyan.mvparms.demo.mvp.contract.CartContract;
import me.jessyan.mvparms.demo.mvp.model.entity.CartBean;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.request.CartListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DeleteCartListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.EidtCartRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.CartListResponse;
import me.jessyan.mvparms.demo.mvp.ui.adapter.CartListAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class CartPresenter extends BasePresenter<CartContract.Model, CartContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<CartBean.CartItem> cartItems;
    @Inject
    RecyclerView.Adapter mAdapter;

    private int preEndIndex;
    private int lastPageIndex = 1;

    @Inject
    public CartPresenter(CartContract.Model model, CartContract.View rootView) {
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

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onCreate() {
        getCartList(true);
    }

    public void getCartList(boolean pullToRefresh) {
        CartListRequest request = new CartListRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        request.setToken(String.valueOf(cache.get(KEY_KEEP + "token")));


        if (pullToRefresh) lastPageIndex = 1;
//        request.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页

        mModel.getGoodsOfCart(request)
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
                }).subscribe(new Consumer<CartListResponse>() {
            @Override
            public void accept(CartListResponse response) throws Exception {
                if (response.isSuccess()) {
                    if (pullToRefresh) {
                        cartItems.clear();
                    }
                    mRootView.setLoadedAllItems(true);
                    cartItems.addAll(response.getCart().getCartItems());
                    preEndIndex = cartItems.size();//更新之前列表总长度,用于确定加载更多的起始位置
                    lastPageIndex = cartItems.size() / 10;
                    if (pullToRefresh) {
                        mAdapter.notifyDataSetChanged();
                    } else {
                        mAdapter.notifyItemRangeInserted(preEndIndex, cartItems.size());
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    mRootView.showMessage(response.getRetDesc());
                }
            }
        });
    }


    /**
     * 编辑购物车
     */
    public void editCartList(boolean delete) {
        EidtCartRequest request = new EidtCartRequest();
        request.setCmd(delete ? 1003 : 1005);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        request.setToken(String.valueOf(cache.get(KEY_KEEP + "token")));
        request.setGoodsId((String) mRootView.getCache().get("goodsId"));
        request.setMerchId((String) mRootView.getCache().get("merchId"));
        request.setPromotionId((String) mRootView.getCache().get("promotionId"));
        if (null != mRootView.getCache().get("nums")) {
            request.setNums((Integer) mRootView.getCache().get("nums"));
        }
        request.setStatus((String) mRootView.getCache().get("status"));
        mModel.editCartList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CartListResponse>() {
                    @Override
                    public void accept(CartListResponse response) throws Exception {
                        if (response.isSuccess()) {
                            cartItems.clear();
                            cartItems.addAll(response.getCart().getCartItems());
                            mRootView.updateUI(response.getCart());
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }


    /**
     * 全选
     */
    public void seletedAll(boolean allCheck) {
        mRootView.showLoading();
        CartListRequest request = new CartListRequest();
        request.setCmd(allCheck ? 1008 : 1009);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        request.setToken(String.valueOf(cache.get(KEY_KEEP + "token")));
        mModel.getGoodsOfCart(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CartListResponse>() {
                    @Override
                    public void accept(CartListResponse response) throws Exception {
                        mRootView.hideLoading();
                        if (response.isSuccess()) {
                            cartItems.clear();
                            cartItems.addAll(response.getCart().getCartItems());
                            mRootView.updateUI(response.getCart());
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    public void deleteCartList() {

        DeleteCartListRequest request = new DeleteCartListRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        request.setToken(String.valueOf(cache.get(KEY_KEEP + "token")));

        List<DeleteCartListRequest.GoodsBean> goodsBeans = new ArrayList<>();

        List<CartBean.CartItem> cartItems = ((CartListAdapter) mAdapter).getInfos();
        for (CartBean.CartItem cartItem : cartItems) {
            for (Goods goodsBean : cartItem.getGoodsList()) {
                if ("1".equals(goodsBean.getStatus())) {
                    DeleteCartListRequest.GoodsBean bean = new DeleteCartListRequest.GoodsBean();
                    bean.setGoodsId(goodsBean.getGoodsId());
                    bean.setMerchId(goodsBean.getMerchId());
                    goodsBeans.add(bean);
                }
            }
        }
        request.setGoodsList(goodsBeans);
        if (goodsBeans.size() <= 0) {
            mRootView.showMessage("请选择需要删除的商品！");
            return;
        }
        mRootView.showLoading();
        mModel.deleteCartList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CartListResponse>() {
                    @Override
                    public void accept(CartListResponse response) throws Exception {
                        mRootView.hideLoading();
                        if (response.isSuccess()) {
                            cartItems.clear();
                            cartItems.addAll(response.getCart().getCartItems());
                            mRootView.updateUI(response.getCart());
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

}
