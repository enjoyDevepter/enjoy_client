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
import com.jess.arms.utils.ArmsUtils;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerAddressListComponent;
import me.jessyan.mvparms.demo.di.module.AddressListModule;
import me.jessyan.mvparms.demo.mvp.contract.AddressListContract;
import me.jessyan.mvparms.demo.mvp.presenter.AddressListPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.AddressEditListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.AddressListAdapter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class AddressListActivity extends BaseActivity<AddressListPresenter> implements AddressListContract.View, View.OnClickListener {

    @BindView(R.id.back)
    View backV;
    @BindView(R.id.edit)
    View editV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.add_address)
    View addV;
    @BindView(R.id.content)
    RecyclerView contentRV;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    AddressListAdapter addressListAdapter;
    @Inject
    AddressEditListAdapter addressEditListAdapter;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerAddressListComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .addressListModule(new AddressListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_address_list; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        editV.setOnClickListener(this);
        addV.setOnClickListener(this);
        titleTV.setText("收货地址");
        ArmsUtils.configRecyclerView(contentRV, mLayoutManager);
        contentRV.setAdapter(addressListAdapter);
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
            case R.id.edit:
                contentRV.setAdapter(addressEditListAdapter);
                break;
            case R.id.add:
                ArmsUtils.startActivity(AddAddressActivity.class);
                break;
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
