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
import me.jessyan.mvparms.demo.di.component.DaggerGetCashComponent;
import me.jessyan.mvparms.demo.di.module.GetCashModule;
import me.jessyan.mvparms.demo.mvp.contract.GetCashContract;
import me.jessyan.mvparms.demo.mvp.model.MyModel;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.MemberAccount;
import me.jessyan.mvparms.demo.mvp.presenter.GetCashPresenter;

import me.jessyan.mvparms.demo.R;


import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class GetCashActivity extends BaseActivity<GetCashPresenter> implements GetCashContract.View {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    View back;

    @BindView(R.id.yue)
    TextView yue;  // 余额，1888.99
    @BindView(R.id.convert_btn)
    View convert_btn;
    @BindView(R.id.add_card)
    View add_card;
    @BindView(R.id.all_convert)
    View all_convert;
    @BindView(R.id.et_money)
    EditText et_money;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerGetCashComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .getCashModule(new GetCashModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_get_cash; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        title.setText("提现");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMyself();
            }
        });
        Cache<String,Object> cache= ArmsUtils.obtainAppComponentFromContext(ArmsUtils.getContext()).extras();
        updateUserAccount ((MemberAccount)cache.get(KEY_KEEP+ MyModel.KEY_FOR_USER_ACCOUNT));

        add_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArmsUtils.startActivity(ChooseBankActivity.class);
            }
        });
        all_convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String format = String.format("%d", account.getBonus() / 100);
                et_money.setText(format);
                et_money.setSelection(format.length());
            }
        });

        convert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = et_money.getText().toString();
                if(TextUtils.isEmpty(s)){
                    ArmsUtils.makeText(ArmsUtils.getContext(),"请输入金额");
                    return;
                }
                int money = 0;
                try{
                    money = Integer.parseInt(s);
                }catch (Exception e){
                    e.printStackTrace();
                    ArmsUtils.makeText(ArmsUtils.getContext(),"金额输入有误");
                    return;
                }
                if(money * 100 > account.getBonus()){
                    ArmsUtils.makeText(ArmsUtils.getContext(),"现金币不足");
                    return;
                }

//                mPresenter.convertCash(money * 100);
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
        yue.setText(String.format("%.2f",account.getBonus() * 1.0 / 100));
    }
}
