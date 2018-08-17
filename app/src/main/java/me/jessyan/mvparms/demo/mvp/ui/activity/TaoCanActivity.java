package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerTaoCanComponent;
import me.jessyan.mvparms.demo.di.module.TaoCanModule;
import me.jessyan.mvparms.demo.mvp.contract.TaoCanContract;
import me.jessyan.mvparms.demo.mvp.model.entity.MealGoods;
import me.jessyan.mvparms.demo.mvp.presenter.TaoCanPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.TaoCanListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.TaoCanListAdapter.ViewName;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class TaoCanActivity extends BaseActivity<TaoCanPresenter> implements TaoCanContract.View, View.OnClickListener, TaoCanListAdapter.OnChildItemClickLinstener {

    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleV;
    @BindView(R.id.content)
    RecyclerView mRecyclerView;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    TaoCanListAdapter mAdapter;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerTaoCanComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .taoCanModule(new TaoCanModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_tao_can; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titleV.setText("套餐专区");
        backV.setOnClickListener(this);
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnChildItemClickLinstener(this);
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }


    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
        }
    }

    @Override
    public void onChildItemClick(View v, ViewName viewname, int position) {
        MealGoods goods = mAdapter.getInfos().get(position);
        switch (viewname) {
            case BUY:
                Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(this).extras();
                if (null == cache.get(KEY_KEEP + "token")) {
                    ArmsUtils.startActivity(LoginActivity.class);
                    return;
                }
                Intent confrimIntent = new Intent(this, MealOrderConfirmActivity.class);
                confrimIntent.putExtra("nums", 1);
                confrimIntent.putExtra("totalPrice", goods.getTotalPrice());
                confrimIntent.putExtra("setMealId", goods.getSetMealId());
                confrimIntent.putExtra("salePrice", goods.getSalesPrice());
                ArmsUtils.startActivity(confrimIntent);
                break;
            case ITEM:
                Intent intent = new Intent(this, TaoCanDetailsActivity.class);
                intent.putExtra("setMealId", goods.getSetMealId());
                ArmsUtils.startActivity(intent);
                break;
        }
    }
}
