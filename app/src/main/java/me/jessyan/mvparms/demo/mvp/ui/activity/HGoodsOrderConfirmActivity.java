package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.cchao.MoneyView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.di.component.DaggerHGoodsOrderConfirmComponent;
import me.jessyan.mvparms.demo.di.module.HGoodsOrderConfirmModule;
import me.jessyan.mvparms.demo.mvp.contract.HGoodsOrderConfirmContract;
import me.jessyan.mvparms.demo.mvp.model.HAppointments;
import me.jessyan.mvparms.demo.mvp.model.entity.Address;
import me.jessyan.mvparms.demo.mvp.model.entity.Coupon;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.HAppointmentsTime;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.HospitalBaseInfoBean;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HGoodsOrderConfirmInfoResponse;
import me.jessyan.mvparms.demo.mvp.presenter.HGoodsOrderConfirmPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;
import static me.jessyan.mvparms.demo.mvp.ui.activity.HospitalInfoActivity.KEY_FOR_HOSPITAL_ID;
import static me.jessyan.mvparms.demo.mvp.ui.activity.HospitalInfoActivity.KEY_FOR_HOSPITAL_NAME;


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


    @Subscriber(tag = EventBusTags.HOSPITAL_CHANGE_EVENT)
    private void updateHospitalInfo(HospitalBaseInfoBean baseInfoBean) {
        this.hospitalBaseInfoBean = baseInfoBean;
        provideCache().put(KEY_FOR_HOSPITAL_NAME, hospitalBaseInfoBean.getName());
        provideCache().put(KEY_FOR_HOSPITAL_ID, hospitalBaseInfoBean.getHospitalId());
        hospitalTV.setText(hospitalBaseInfoBean.getName());
        provideCache().put("baseInfoBean", baseInfoBean);
    }

    @Subscriber(tag = EventBusTags.APPOINTMENTS_CHANGE_EVENT)
    private void updateAppointments(List<HAppointments> hAppointmentsList) {
        for (HAppointments appointments : hAppointmentsList) {
            if (appointments.isChoice()) {
                provideCache().put("appointmentsDate", appointments.getDate());
                for (HAppointmentsTime hAppointmentsTime : appointments.getReservationTimeList()) {
                    if (hAppointmentsTime.isChoice()) {
                        provideCache().put("appointmentsTime", hAppointmentsTime.getTime());
                        appointmnetTV.setText(appointments.getDate() + " " + hAppointmentsTime.getTime());
                        break;
                    }
                }
                break;
            }
        }
        provideCache().put("hAppointments", hAppointmentsList);
    }


    @Subscriber(tag = EventBusTags.CHANGE_COUPON)
    private void updateCoupon(Coupon coupon) {
        provideCache().put("couponId", coupon.getCouponId());
        couponTV.setText(coupon.getName());
        mPresenter.getOrderConfirmInfo();
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
        moneyET.setHint(ArmsUtils.formatLong(response.getMoney()));
        couponButtomTV.setText(ArmsUtils.formatLong(response.getCoupon()));
        payMoneyTV.setText(ArmsUtils.formatLong(response.getPayMoney()));
        List<Coupon> couponList = response.getCouponList();
        if (null == couponList || null != couponList && couponList.size() <= 0) {
            couponTV.setText("暂无优惠卷");
        } else {
            for (Coupon coupon : couponList) {
                if (coupon.getCouponId().equals(response.getCouponId())) {
                    couponTV.setText(coupon.getName());
                    break;
                }
            }
        }

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
                if (provideCache().get(KEY_FOR_HOSPITAL_ID) == null) {
                    ArmsUtils.makeText(this, "请选择医院");
                    break;
                }
                Intent hospitalIntent = new Intent(HGoodsOrderConfirmActivity.this, HospitalInfoActivity.class);
                hospitalIntent.putExtra(KEY_FOR_HOSPITAL_NAME, (String) provideCache().get(KEY_FOR_HOSPITAL_NAME));
                hospitalIntent.putExtra(HospitalInfoActivity.KEY_FOR_HOSPITAL_ID, (String) provideCache().get(KEY_FOR_HOSPITAL_ID));
                ArmsUtils.startActivity(hospitalIntent);
                break;
            case R.id.hospital:
                Intent intent2 = new Intent(this, SelfPickupAddrListActivity.class);
                intent2.putExtra(SelfPickupAddrListActivity.KEY_FOR_ACTIVITY_LIST_TYPE, listType);
                if (null != hospitalBaseInfoBean) {
                    listType.setStoreName(hospitalBaseInfoBean.getName());
                    listType.setCity(hospitalBaseInfoBean.getCity());
                    listType.setCounty(hospitalBaseInfoBean.getCounty());
                    listType.setProvince(hospitalBaseInfoBean.getProvince());
                    listType.setDistrict(hospitalBaseInfoBean.getProvinceName() + " " + hospitalBaseInfoBean.getCityName() + " " + hospitalBaseInfoBean.getCountyName());
                }
                intent2.putExtra("specValueId", response.getGoodsList().get(0).getGoodsSpecValue().getSpecValueId());
                ArmsUtils.startActivity(intent2);
                break;
            case R.id.appointments_layout:
                Intent appointmentsIntent = new Intent(this, ChoiceTimeActivity.class);
                appointmentsIntent.putExtra("type", "choice_time");
                if (null != provideCache().get("hAppointments")) {
                    appointmentsIntent.putParcelableArrayListExtra("appointmnetInfo", (ArrayList<? extends Parcelable>) provideCache().get("hAppointments"));
                } else {
                    appointmentsIntent.putParcelableArrayListExtra("appointmnetInfo", (ArrayList<? extends Parcelable>) response.getReservationDateList());
                }
                ArmsUtils.startActivity(appointmentsIntent);

                break;
            case R.id.no_address:
            case R.id.addres_info_layout:
                ArmsUtils.startActivity(AddressListActivity.class);
                break;
            case R.id.coupon_layout:
                List<Coupon> couponList = response.getCouponList();
                if (null == couponList || null != couponList && couponList.size() <= 0) {
                    return;
                }
                Intent intent = new Intent(this, CouponActivity.class);
                intent.putExtra("type", "优惠券");
                intent.putParcelableArrayListExtra("coupons", (ArrayList<? extends Parcelable>) couponList);
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                //使EditText触发一次失去焦点事件
                v.setFocusable(false);
//                v.setFocusable(true); //这里不需要是因为下面一句代码会同时实现这个功能
                v.setFocusableInTouchMode(true);
                return true;
            }
        }
        return false;
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
