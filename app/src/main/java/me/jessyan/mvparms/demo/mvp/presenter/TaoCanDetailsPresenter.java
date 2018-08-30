package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.TaoCanDetailsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.MealGoods;
import me.jessyan.mvparms.demo.mvp.model.entity.request.MealDetailsRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.MealDetailsResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.FollowRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.ShareResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.LoginActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.MealOrderConfirmActivity;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class TaoCanDetailsPresenter extends BasePresenter<TaoCanDetailsContract.Model, TaoCanDetailsContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<MealGoods.Goods> goodsList;
    @Inject
    RecyclerView.Adapter mAdapter;
    MealDetailsResponse mealDetailsResponse;

    @Inject
    public TaoCanDetailsPresenter(TaoCanDetailsContract.Model model, TaoCanDetailsContract.View rootView) {
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

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        getMealDetail(true);
    }

    public void getMealDetail(boolean pullToRefresh) {
        MealDetailsRequest request = new MealDetailsRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        String token = (String) cache.get(KEY_KEEP + "token");
        if (cache.get(KEY_KEEP + "token") != null) {
            request.setCmd(432);
        } else {
            request.setCmd(431);
        }
        request.setToken(token);
        request.setSetMealId(mRootView.getActivity().getIntent().getStringExtra("setMealId"));
        mModel.getMealDetail(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<MealDetailsResponse>(mErrorHandler) {
                    @Override
                    public void onNext(MealDetailsResponse response) {
                        if (response.isSuccess()) {
                            goodsList.clear();
                            goodsList.addAll(response.getSetMealGoods().getGoodsList());
                            mRootView.updateUI(response);
                            mealDetailsResponse = response;
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
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

        MealDetailsRequest request = new MealDetailsRequest();
        request.setCmd(collect ? 433 : 434);
        request.setToken((String) cache.get(KEY_KEEP + "token"));

        request.setSetMealId(mRootView.getActivity().getIntent().getStringExtra("setMealId"));
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

    public void buy() {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        if (cache.get(KEY_KEEP + "token") == null) {
            ArmsUtils.startActivity(LoginActivity.class);
            return;
        }
        Intent intent = new Intent(mRootView.getActivity(), MealOrderConfirmActivity.class);
        intent.putExtra("totalPrice", mealDetailsResponse.getSetMealGoods().getTotalPrice());
        intent.putExtra("setMealId", mealDetailsResponse.getSetMealGoods().getSetMealId());
        intent.putExtra("salePrice", mealDetailsResponse.getSetMealGoods().getSalePrice());
        ArmsUtils.startActivity(intent);

    }

    public void share() {
        FollowRequest request = new FollowRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(ArmsUtils.getContext()).extras();
        String token = (String) cache.get(KEY_KEEP + "token");

        if (ArmsUtils.isEmpty(token)) {
            ArmsUtils.startActivity(LoginActivity.class);
            return;
        }
        request.setToken(token);
        request.setCmd(909);
        mModel.share(request)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<ShareResponse>(mErrorHandler) {
                    @Override
                    public void onNext(ShareResponse response) {
                        if (response.isSuccess()) {
                            mRootView.showWX(response.getShare());
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

}
