package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.cchao.MoneyView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;

import java.util.ArrayList;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerHGoodsOrderConfirmComponent;
import me.jessyan.mvparms.demo.di.module.HGoodsOrderConfirmModule;
import me.jessyan.mvparms.demo.mvp.contract.HGoodsOrderConfirmContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Address;
import me.jessyan.mvparms.demo.mvp.model.entity.Coupon;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.HospitalBaseInfoBean;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HGoodsOrderConfirmInfoResponse;
import me.jessyan.mvparms.demo.mvp.presenter.HGoodsOrderConfirmPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class HGoodsOrderConfirmActivity extends BaseActivity<HGoodsOrderConfirmPresenter> implements HGoodsOrderConfirmContract.View, View.OnClickListener, View.OnFocusChangeListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.image)
    ImageView imageIV;
    @BindView(R.id.goods_name)
    TextView goodsNameTV;
    @BindView(R.id.deposit)
    MoneyView depositTV;
    @BindView(R.id.nums)
    TextView numsTV;
    @BindView(R.id.tailMoney)
    MoneyView tailMoneyTV;
    @BindView(R.id.goods_spec)
    TextView goodsSpecTV;
    @BindView(R.id.hospital_info)
    View hospitalV;
    @BindView(R.id.hospital)
    TextView hospitalTV;
    @BindView(R.id.appointments_layout)
    View appointmnetV;
    @BindView(R.id.appointments_time)
    TextView appointmnetTV;

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

    @BindView(R.id.coupon_layout)
    View couponV;
    @BindView(R.id.coupon)
    TextView couponTV;
    @BindView(R.id.balance)
    TextView balanceTV;
    @BindView(R.id.money)
    EditText moneyET;
    @BindView(R.id.remark)
    EditText remarkET;

    @BindView(R.id.deposit_buttom)
    TextView depositButtomTV;
    @BindView(R.id.tailMoney_buttom)
    TextView tailMoneyButtomTV;
    @BindView(R.id.money_buttom)
    TextView moneyTV;
    @BindView(R.id.coupon_buttom)
    TextView couponButtomTV;
    @BindView(R.id.payMoney)
    TextView payMoneyTV;
    @BindView(R.id.confirm)
    View confirmV;

    @BindView(R.id.depositTV)
    TextView depositTVTV;
    @BindView(R.id.tailMoney_layout_xx)
    View tailMoney_layout_xxV;
    @BindView(R.id.deductionMoney_layout)
    View deductionMoney_layoutV;
    @BindView(R.id.deductionMoney)
    TextView deductionMoneyTV;
    @BindView(R.id.deposit_tag)
    TextView depositTagTV;

    @BindView(R.id.tailMoney_layout)
    View tailMoneyLayoutV;

    private volatile boolean shouldSubmit;

    private HGoodsOrderConfirmInfoResponse response;

    private SelfPickupAddrListActivity.ListType listType = SelfPickupAddrListActivity.ListType.HOP;
    private HospitalBaseInfoBean hospitalBaseInfoBean;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerHGoodsOrderConfirmComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .hGoodsOrderConfirmModule(new HGoodsOrderConfirmModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_hgoods_order_confirm; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titleTV.setText("确认订单");
        backV.setOnClickListener(this);
        appointmnetV.setOnClickListener(this);
        noAddressV.setOnClickListener(this);
        addressInfoV.setOnClickListener(this);
        couponV.setOnClickListener(this);
        confirmV.setOnClickListener(this);
        hospitalV.setOnClickListener(this);
        hospitalTV.setOnClickListener(this);
        moneyET.setOnFocusChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(this).extras();
        if (cache.get("memberAddressInfo") != null) {
            Address address = (Address) cache.get("memberAddressInfo");
            addressTV.setText(address.getProvinceName() + " " + address.getCityName() + " " + address.getCountyName() + " " + address.getAddress());
            nameTV.setText(address.getReceiverName());
            phoneTV.setText(address.getPhone());
            noAddressV.setVisibility(View.GONE);
            addressInfoV.setVisibility(View.VISIBLE);
        } else {
            noAddressV.setVisibility(View.VISIBLE);
            addressInfoV.setVisibility(View.GONE);
        }

        if (cache.get("appointmentsDate") != null && cache.get("appointmentsTime") != null) {
            appointmnetTV.setText((String) cache.get("appointmentsDate") + " " + cache.get("appointmentsTime"));
        }

        if (cache.get("coupon") != null) {
            couponTV.setText(((Coupon) cache.get("coupon")).getName());
        }

        if (cache.get(listType.getDataKey()) != null) {
            hospitalBaseInfoBean = (HospitalBaseInfoBean) cache.get(listType.getDataKey());
            hospitalTV.setText(hospitalBaseInfoBean.getName());
        }


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
    public void updateUI(HGoodsOrderConfirmInfoResponse response) {
        this.response = response;
        Goods goods = response.getGoodsList().get(0);
        goodsNameTV.setText(goods.getName());
        numsTV.setText("x" + String.valueOf(goods.getNums()));
        tailMoneyTV.setMoneyText(String.valueOf(goods.getTailMoney()));
        goodsSpecTV.setText(goods.getGoodsSpecValue().getSpecValueName());

        balanceTV.setText(ArmsUtils.formatLong(response.getBalance()));
        tailMoneyButtomTV.setText(String.valueOf(goods.getTailMoney()));
        moneyTV.setText(ArmsUtils.formatLong(response.getMoney()));
        couponButtomTV.setText(ArmsUtils.formatLong(response.getCoupon()));
        payMoneyTV.setText(ArmsUtils.formatLong(response.getPayMoney()));

        AppComponent mAppComponent = ArmsUtils.obtainAppComponentFromContext(this);
        mAppComponent.imageLoader()
                .loadImage(this,
                        ImageConfigImpl
                                .builder()
                                .url(goods.getImage())
                                .imageView(imageIV)
                                .build());

        boolean payAll = getIntent().getBooleanExtra("payAll", false);
        if (payAll) {
            tailMoneyLayoutV.setVisibility(View.INVISIBLE);
            depositTV.setMoneyText(String.valueOf(goods.getSalePrice()));
            depositTVTV.setText("总金额");
            depositTagTV.setText("总金额");
            tailMoney_layout_xxV.setVisibility(View.GONE);
            deductionMoney_layoutV.setVisibility(View.VISIBLE);
            depositButtomTV.setText(String.valueOf(goods.getSalePrice()));
            deductionMoneyTV.setText(ArmsUtils.formatLong(response.getDeductionMoney()));

        } else {
            depositTVTV.setText("预付款");
            depositTagTV.setText("总金额");
            deductionMoney_layoutV.setVisibility(View.GONE);
            tailMoney_layout_xxV.setVisibility(View.VISIBLE);
            tailMoneyLayoutV.setVisibility(View.VISIBLE);
            depositButtomTV.setText(String.valueOf(goods.getDeposit()));
            depositTV.setMoneyText(String.valueOf(goods.getDeposit()));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
            case R.id.hospital_info:
                if (hospitalBaseInfoBean == null) {
                    ArmsUtils.makeText(this, "请先选择医院");
                    break;
                }
                Intent hospitalIntent = new Intent(HGoodsOrderConfirmActivity.this, HospitalInfoActivity.class);
                hospitalIntent.putExtra(HospitalInfoActivity.KEY_FOR_HOSPITAL_NAME, hospitalBaseInfoBean.name);
                hospitalIntent.putExtra(HospitalInfoActivity.KEY_FOR_HOSPITAL_ID, hospitalBaseInfoBean.getHospitalId());
                ArmsUtils.startActivity(hospitalIntent);
                break;
            case R.id.hospital:
                Intent intent2 = new Intent(this, SelfPickupAddrListActivity.class);
                intent2.putExtra(SelfPickupAddrListActivity.KEY_FOR_ACTIVITY_LIST_TYPE, listType);
                intent2.putExtra("specValueId", response.getGoodsList().get(0).getGoodsSpecValue().getSpecValueId());
                ArmsUtils.startActivity(intent2);
                break;
            case R.id.appointments_layout:
                Intent appointmentsIntent = new Intent(this, ChoiceTimeActivity.class);
                appointmentsIntent.putExtra("type", "choice_time");
                appointmentsIntent.putParcelableArrayListExtra("appointmnetInfo", (ArrayList<? extends Parcelable>) response.getReservationDateList());
                ArmsUtils.startActivity(appointmentsIntent);
                break;
            case R.id.no_address:
            case R.id.addres_info_layout:
                ArmsUtils.startActivity(AddressListActivity.class);
                break;
            case R.id.coupon_layout:
                Intent intent = new Intent(this, CouponActivity.class);
                intent.putExtra("type", "优惠券");
                intent.putParcelableArrayListExtra("coupons", (ArrayList<? extends Parcelable>) response.getCouponList());
                ArmsUtils.startActivity(intent);
                break;
            case R.id.confirm:
                provideCache().put("remark", remarkET.getText().toString());
                mPresenter.placeHGoodsOrder();
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
                moneyET.setText("");
                provideCache().put("money", Long.valueOf(m) * 100);
                shouldSubmit = hasFocus;
                mPresenter.getOrderConfirmInfo();
            }
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Cache getCache() {
        return provideCache();
    }

}
