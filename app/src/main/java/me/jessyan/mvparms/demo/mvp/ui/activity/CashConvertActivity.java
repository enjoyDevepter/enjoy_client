package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import me.jessyan.mvparms.demo.di.component.DaggerCashConvertComponent;
import me.jessyan.mvparms.demo.di.module.CashConvertModule;
import me.jessyan.mvparms.demo.mvp.contract.CashConvertContract;
import me.jessyan.mvparms.demo.mvp.presenter.CashConvertPresenter;

import me.jessyan.mvparms.demo.R;


import static com.jess.arms.utils.Preconditions.checkNotNull;


public class CashConvertActivity extends BaseActivity<CashConvertPresenter> implements CashConvertContract.View {

    public static final String KEY_FOR_CASH_NUM = "KEY_FOR_CASH_NUM";

    @BindView(R.id.back)
    View back;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.all_cash)
    TextView all_cash;
    @BindView(R.id.et_money)
    EditText et_money;
    @BindView(R.id.all_convert)
    View all_convert;
    @BindView(R.id.convert_btn)
    View convert_btn;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerCashConvertComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .cashConvertModule(new CashConvertModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_cash_convert; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    private int currentNum;

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        currentNum = getIntent().getIntExtra(KEY_FOR_CASH_NUM, 0);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMyself();
            }
        });
        title.setText("转换");
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
}
