package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;

import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.di.component.DaggerCashConvertComponent;
import me.jessyan.mvparms.demo.di.module.CashConvertModule;
import me.jessyan.mvparms.demo.mvp.contract.CashConvertContract;
import me.jessyan.mvparms.demo.mvp.model.MyModel;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.MemberAccount;
import me.jessyan.mvparms.demo.mvp.presenter.CashConvertPresenter;

import me.jessyan.mvparms.demo.R;


import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;
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

    @BindView(R.id.convert_ok)
    View convert_ok;
    @BindView(R.id.close)
    View close;

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

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        Cache<String,Object> cache= ArmsUtils.obtainAppComponentFromContext(ArmsUtils.getContext()).extras();
        updateUserAccount ((MemberAccount)cache.get(KEY_KEEP+ MyModel.KEY_FOR_USER_ACCOUNT));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMyself();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convert_ok.setVisibility(View.GONE);
            }
        });
        title.setText("转换");
        convert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = et_money.getText().toString();
                if(TextUtils.isEmpty(s)){
                    ArmsUtils.makeText(ArmsUtils.getContext(),"请输入金额");
                    return;
                }
                int money = Integer.parseInt(s);
                if(money * 100 > account.getBonus()){
                    ArmsUtils.makeText(ArmsUtils.getContext(),"现金币不足");
                    return;
                }

                mPresenter.convertCash(money);
            }
        });
        all_convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_money.setText(String.format("%d",account.getBonus() / 100));
            }
        });
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

    private MemberAccount account;

    @Subscriber(tag = EventBusTags.USER_ACCOUNT_CHANGE)
    public void updateUserAccount(MemberAccount account){
        this.account = account;
        all_cash.setText(String.format("%d",account.getBonus() / 100));
    }

    public void showConvertOk(){
        convert_ok.setVisibility(View.VISIBLE);
    }

}
