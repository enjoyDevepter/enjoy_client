package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;

import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.di.component.DaggerMealOrderConfirmComponent;
import me.jessyan.mvparms.demo.di.module.MealOrderConfirmModule;
import me.jessyan.mvparms.demo.mvp.contract.MealOrderConfirmContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Address;
import me.jessyan.mvparms.demo.mvp.model.entity.response.MealOrderConfirmResponse;
import me.jessyan.mvparms.demo.mvp.presenter.MealOrderConfirmPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MealOrderConfirmActivity extends BaseActivity<MealOrderConfirmPresenter> implements MealOrderConfirmContract.View, View.OnClickListener, View.OnFocusChangeListener {

    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.confirm)
    View confirmV;
    @BindView(R.id.balance)
    TextView balanceTV;
    @BindView(R.id.price)
    TextView priceTV;
    @BindView(R.id.payMoney)
    TextView payMoneyTV;
    @BindView(R.id.remark)
    EditText remarkET;
    @BindView(R.id.money_et)
    EditText moneyET;
    @BindView(R.id.money)
    TextView moneyTV;
    @BindView(R.id.no_address)
    View noAddressV;
    @BindView(R.id.addres_info_layout)
    View addressInfoV;
    @BindView(R.id.name)
    TextView nameTV;
    @BindView(R.id.phone)
    TextView phoneTV;
    @BindView(R.id.address)
    TextView addressTV;
    @BindView(R.id.meal_name)
    TextView mealNameTV;
    @BindView(R.id.salesPrice)
    TextView salesPriceTV;
    @BindView(R.id.totalPrice)
    TextView totalPriceTV;
    @BindView(R.id.image)
    ImageView imageIV;

    private volatile boolean shouldSubmit;

    private MealOrderConfirmResponse response;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerMealOrderConfirmComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .mealOrderConfirmModule(new MealOrderConfirmModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_meal_order_confirm; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titleTV.setText("确认订单");
        backV.setOnClickListener(this);
        confirmV.setOnClickListener(this);
        noAddressV.setOnClickListener(this);
        addressInfoV.setOnClickListener(this);
        moneyET.setOnFocusChangeListener(this);
    }

    @Subscriber(tag = EventBusTags.ADDRESS_CHANGE_EVENT)
    private void updateAddress(Address address) {
        provideCache().put("addressId", address.getAddressId());
        addressTV.setText(address.getProvinceName() + " " + address.getCityName() + " " + address.getCountyName() + " " + address.getAddress());
        nameTV.setText(address.getReceiverName());
        phoneTV.setText(address.getPhone());
        noAddressV.setVisibility(View.GONE);
        addressInfoV.setVisibility(View.VISIBLE);
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    public Activity getActivity() {
        return this;
    }

    @Override
    public Cache getCache() {
        return provideCache();
    }

    @Override
    public void updateUI(MealOrderConfirmResponse response) {
        this.response = response;

        balanceTV.setText(ArmsUtils.formatLong(response.getBalance()));
        priceTV.setText(ArmsUtils.formatLong(response.getPrice()));
        payMoneyTV.setText(ArmsUtils.formatLong(response.getPayMoney()));
        moneyTV.setText(ArmsUtils.formatLong(response.getMoney()));
        moneyET.setHint(ArmsUtils.formatLong(response.getMoney()));

        mealNameTV.setText(response.getSetMealGoods().getName());
        salesPriceTV.setText(String.valueOf(response.getSetMealGoods().getSalePrice()));
        totalPriceTV.setText(String.valueOf(response.getSetMealGoods().getTotalPrice()));
        totalPriceTV.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        AppComponent mAppComponent = ArmsUtils.obtainAppComponentFromContext(this);
        mAppComponent.imageLoader()
                .loadImage(this,
                        ImageConfigImpl
                                .builder()
                                .url(response.getSetMealGoods().getImage())
                                .imageView(imageIV)
                                .build());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
            case R.id.no_address:
            case R.id.addres_info_layout:
                ArmsUtils.startActivity(AddressListActivity.class);
                break;
            case R.id.confirm:
                provideCache().put("remark", remarkET.getText().toString());
                mPresenter.placeMealOrder();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            shouldSubmit = hasFocus;
        } else {
            if (shouldSubmit) {
                String m = moneyET.getText().toString();
                if (!ArmsUtils.isEmpty(m)) {
                    moneyET.setText("");
                    provideCache().put("money", Long.valueOf(m) * 100);
                }
                shouldSubmit = hasFocus;
                mPresenter.getMealOrderConfirmInfo();
            }
        }
    }
}
