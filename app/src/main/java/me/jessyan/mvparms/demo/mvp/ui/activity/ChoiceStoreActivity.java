package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerChoiceStoreComponent;
import me.jessyan.mvparms.demo.di.module.ChoiceStoreModule;
import me.jessyan.mvparms.demo.mvp.contract.ChoiceStoreContract;
import me.jessyan.mvparms.demo.mvp.model.entity.request.Store;
import me.jessyan.mvparms.demo.mvp.presenter.ChoiceStorePresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ChoiceStoreActivity extends BaseActivity<ChoiceStorePresenter> implements ChoiceStoreContract.View, View.OnClickListener, DefaultAdapter.OnRecyclerViewItemClickListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleV;
    @BindView(R.id.confirm)
    View confirmV;
    @BindView(R.id.stores)
    RecyclerView storesRV;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.Adapter mAdapter;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerChoiceStoreComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .choiceStoreModule(new ChoiceStoreModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_choice_store; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        titleV.setText("选择店铺");
        ArmsUtils.configRecyclerView(storesRV, mLayoutManager);
        storesRV.setAdapter(mAdapter);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
            case R.id.confirm:
                break;
        }
    }

    @Override
    public void onItemClick(View view, int viewType, Object data, int position) {
        List<Store> storeList = ((DefaultAdapter) mAdapter).getInfos();
        storeList.get(position).setCheck(true);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
