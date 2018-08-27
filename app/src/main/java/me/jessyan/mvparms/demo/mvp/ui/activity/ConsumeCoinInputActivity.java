package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;

import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.di.component.DaggerConsumeCoinInputComponent;
import me.jessyan.mvparms.demo.di.module.ConsumeCoinInputModule;
import me.jessyan.mvparms.demo.mvp.contract.ConsumeCoinInputContract;
import me.jessyan.mvparms.demo.mvp.model.MyModel;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.MemberAccount;
import me.jessyan.mvparms.demo.mvp.presenter.ConsumeCoinInputPresenter;

import me.jessyan.mvparms.demo.R;


import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ConsumeCoinInputActivity extends BaseActivity<ConsumeCoinInputPresenter> implements ConsumeCoinInputContract.View {

    @BindView(R.id.back)
    View back;
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.consume_count)
    TextView consume_count;  // 消费币

    private MemberAccount account;

    @Subscriber(tag = EventBusTags.USER_ACCOUNT_CHANGE)
    public void updateUserAccount(MemberAccount account){
        if(account == null){
            return;
        }
        this.account = account;
        consume_count.setText(String.format("%.2f", account.getAmount() * 1.0 / 100));
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerConsumeCoinInputComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .consumeCoinInputModule(new ConsumeCoinInputModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_consume_coin_input; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(ArmsUtils.getContext()).extras();
        updateUserAccount((MemberAccount)cache.get(KEY_KEEP + MyModel.KEY_FOR_USER_ACCOUNT));
        title.setText("消费币");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMyself();
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
}
