package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerAddAddressComponent;
import me.jessyan.mvparms.demo.di.module.AddAddressModule;
import me.jessyan.mvparms.demo.mvp.contract.AddAddressContract;
import me.jessyan.mvparms.demo.mvp.presenter.AddAddressPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class AddAddressActivity extends BaseActivity<AddAddressPresenter> implements AddAddressContract.View, OnClickListener {

    @BindView(R.id.back)
    View backV;
    @BindView(R.id.county_chocie)
    View choiceV;
    @BindView(R.id.contact)
    EditText nameET;
    @BindView(R.id.tel)
    EditText phoneET;
    @BindView(R.id.address)
    EditText addressET;
    @BindView(R.id.county)
    TextView countyTV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.confirm)
    View confirmV;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerAddAddressComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .addAddressModule(new AddAddressModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_add_address; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titleTV.setText("收货信息");
        confirmV.setOnClickListener(this);
        backV.setOnClickListener(this);
        choiceV.setOnClickListener(this);
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
            case R.id.county_chocie:
                break;
            case R.id.confirm:
                mPresenter.addAddress();
                break;
        }
    }
}
