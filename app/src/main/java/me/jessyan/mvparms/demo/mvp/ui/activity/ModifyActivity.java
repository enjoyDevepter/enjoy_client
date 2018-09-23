package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.utils.SoftHideKeyBoardUtil;
import me.jessyan.mvparms.demo.di.component.DaggerModifyComponent;
import me.jessyan.mvparms.demo.di.module.ModifyModule;
import me.jessyan.mvparms.demo.mvp.contract.ModifyContract;
import me.jessyan.mvparms.demo.mvp.presenter.ModifyPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ModifyActivity extends BaseActivity<ModifyPresenter> implements ModifyContract.View, View.OnClickListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.modify)
    View modifyV;
    @BindView(R.id.password)
    EditText passWordET;
    @BindView(R.id.repeat)
    EditText repeatET;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerModifyComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .modifyModule(new ModifyModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_modify; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        modifyV.setOnClickListener(this);
        SoftHideKeyBoardUtil.assistActivity(this);
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
            case R.id.modify:
                mPresenter.modify(passWordET.getText().toString(), repeatET.getText().toString());
                break;
        }
    }
}
