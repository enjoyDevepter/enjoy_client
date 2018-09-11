package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;

import org.simple.eventbus.EventBus;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.di.component.DaggerSettingComponent;
import me.jessyan.mvparms.demo.di.module.SettingModule;
import me.jessyan.mvparms.demo.mvp.contract.SettingContract;
import me.jessyan.mvparms.demo.mvp.presenter.SettingPresenter;
import me.jessyan.mvparms.demo.mvp.ui.widget.CustomDialog;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class SettingActivity extends BaseActivity<SettingPresenter> implements SettingContract.View, View.OnClickListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.user_info)
    View userInfoV;
    @BindView(R.id.password)
    View passwordV;
    @BindView(R.id.address)
    View addressV;
    @BindView(R.id.authentication)
    View authenticationV;
    @BindView(R.id.fallback)
    View fallbackV;
    @BindView(R.id.contact)
    View contactV;
    @BindView(R.id.clean)
    View cleanV;
    @BindView(R.id.version)
    View versionV;
    @BindView(R.id.submit)
    View submitV;
    CustomDialog dialog = null;
    @BindView(R.id.get_cash_password)
    View get_cash_password;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerSettingComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .settingModule(new SettingModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_setting; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        titleTV.setText("设置");
        userInfoV.setOnClickListener(this);
        passwordV.setOnClickListener(this);
        addressV.setOnClickListener(this);
        fallbackV.setOnClickListener(this);
        authenticationV.setOnClickListener(this);
        contactV.setOnClickListener(this);
        cleanV.setOnClickListener(this);
        versionV.setOnClickListener(this);
        submitV.setOnClickListener(this);
        get_cash_password.setOnClickListener(this);
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
            case R.id.user_info:
                ArmsUtils.startActivity(UserInfoActivity.class);
                break;
            case R.id.password:
                ArmsUtils.startActivity(ModifyPasswordActivity.class);
                break;
            case R.id.address:
                ArmsUtils.startActivity(AddressListActivity.class);
                break;
            case R.id.authentication:
                ArmsUtils.startActivity(AuthenticationActivity.class);
                break;
            case R.id.fallback:
                ArmsUtils.startActivity(FeedBackActivity.class);
                break;
            case R.id.contact:
                mPresenter.getTel();
                break;
            case R.id.clean:
                showDailog("确认清除应用缓存吗?");
                break;
            case R.id.version:
                break;
            case R.id.submit:
                Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(this).extras();
                cache.put(KEY_KEEP + "token", null);
                cache.put(KEY_KEEP + "signkey", null);
                EventBus.getDefault().post(EventBusTags.USER_LOGOUT);
                killMyself();
                break;
            case R.id.get_cash_password:
                ArmsUtils.startActivity(CashPasswordActivity.class);
                break;
        }
    }

    private void showDailog(String text) {
        dialog = CustomDialog.create(getSupportFragmentManager())
                .setViewListener(new CustomDialog.ViewListener() {
                    @Override
                    public void bindView(View view) {
                        ((TextView) view.findViewById(R.id.content)).setText(text);
                        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setLayoutRes(R.layout.dialog_remove_good_for_cart)
                .setDimAmount(0.5f)
                .isCenter(true)
                .setWidth(ArmsUtils.getDimens(this, R.dimen.dialog_width))
                .setHeight(ArmsUtils.getDimens(this, R.dimen.dialog_height))
                .show();
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
