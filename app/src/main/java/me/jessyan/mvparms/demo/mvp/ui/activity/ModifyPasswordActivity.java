package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerModifyPasswordComponent;
import me.jessyan.mvparms.demo.di.module.ModifyPasswordModule;
import me.jessyan.mvparms.demo.mvp.contract.ModifyPasswordContract;
import me.jessyan.mvparms.demo.mvp.presenter.ModifyPasswordPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ModifyPasswordActivity extends BaseActivity<ModifyPasswordPresenter> implements ModifyPasswordContract.View, View.OnClickListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.submit)
    View submitV;
    @BindView(R.id.old_layout)
    View oldV;
    @BindView(R.id.old)
    EditText oldET;
    @BindView(R.id.newly)
    EditText newET;
    @BindView(R.id.confirm)
    EditText confirmET;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerModifyPasswordComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .modifyPasswordModule(new ModifyPasswordModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_modify_password; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        submitV.setOnClickListener(this);
        oldV.setVisibility(getIntent().getBooleanExtra("isForget", false) ? View.GONE : View.VISIBLE);
        provideCache().put("isForget", getIntent().getBooleanExtra("isForget", false));
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
            case R.id.submit:
                provideCache().put("old", oldET.getText().toString());
                provideCache().put("new", newET.getText().toString());
                provideCache().put("confirm", confirmET.getText().toString());
                mPresenter.modifyPassword();
                break;
        }
    }

    @Override
    public Cache getCache() {
        return provideCache();
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
