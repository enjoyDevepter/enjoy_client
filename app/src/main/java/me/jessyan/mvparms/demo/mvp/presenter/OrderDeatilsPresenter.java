package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.OrderDeatilsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.MealGoods;
import me.jessyan.mvparms.demo.mvp.model.entity.order.OrderGoods;
import me.jessyan.mvparms.demo.mvp.model.entity.request.OrderDetailsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.OrderDetailsResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class OrderDeatilsPresenter extends BasePresenter<OrderDeatilsContract.Model, OrderDeatilsContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<OrderGoods> orderGoods;
    @Inject
    RecyclerView.Adapter mAdapter;

    @Inject
    public OrderDeatilsPresenter(OrderDeatilsContract.Model model, OrderDeatilsContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        getOrderDetails();
    }

    private void getOrderDetails() {
        int orderType = mRootView.getActivity().getIntent().getIntExtra("type", 0);
        OrderDetailsRequest request = new OrderDetailsRequest();
        boolean isMeal = mRootView.getActivity().getIntent().getBooleanExtra("isMeal", false);
        switch (orderType) {
            case 0:
                request.setCmd(551);
                break;
            case 1:
                return;
            case 2:
                if (isMeal) {
                    request.setCmd(572);
                } else {
                    request.setCmd(571);
                }
                break;
        }
        request.setToken((String) ArmsUtils.obtainAppComponentFromContext(mApplication).extras().get(KEY_KEEP + "token"));
        request.setOrderId(mRootView.getActivity().getIntent().getStringExtra("orderId"));
        mModel.getOrderDetails(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<OrderDetailsResponse>() {
                    @Override
                    public void accept(OrderDetailsResponse response) throws Exception {
                        if (response.isSuccess()) {
                            orderGoods.clear();
                            if (isMeal) {
                                for (MealGoods mealGoods : response.getOrder().getSetMealGoodsList()) {
                                    OrderGoods goods = new OrderGoods();
                                    goods.setType("6");
                                    goods.setImage(mealGoods.getImage());
                                    goods.setName(mealGoods.getName());
                                    goods.setSalePrice(mealGoods.getSalePrice());
                                    orderGoods.add(goods);
                                }
                            } else {
                                if (orderType == 2) {
                                    for (OrderGoods goods : response.getOrder().getGoodsList()) {
                                        goods.setType("7");
                                    }
                                }
                                orderGoods.addAll(response.getOrder().getGoodsList());
                            }
                            mRootView.updateUI(response);
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
