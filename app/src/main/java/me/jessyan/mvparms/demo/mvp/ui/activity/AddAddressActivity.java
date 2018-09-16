package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerAddAddressComponent;
import me.jessyan.mvparms.demo.di.module.AddAddressModule;
import me.jessyan.mvparms.demo.mvp.contract.AddAddressContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Address;
import me.jessyan.mvparms.demo.mvp.model.entity.AreaAddress;
import me.jessyan.mvparms.demo.mvp.presenter.AddAddressPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class AddAddressActivity extends BaseActivity<AddAddressPresenter> implements AddAddressContract.View, OnClickListener {

    @BindView(R.id.back)
    View backV;
    @BindView(R.id.county_chocie)
    View choiceV;
    @BindView(R.id.contact)
    EditText nameET;
    @BindView(R.id.tel)
    EditText phoneET;
    @BindView(R.id.address)
    EditText addressET;
    @BindView(R.id.county)
    TextView countyTV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.confirm)
    View confirmV;
    @Inject
    List<AreaAddress> addressList;

    private List<AreaAddress> options1Items = new ArrayList<>();
    private List<List<AreaAddress>> options2Items = new ArrayList<>();
    private List<List<List<AreaAddress>>> options3Items = new ArrayList<>();

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerAddAddressComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .addAddressModule(new AddAddressModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_add_address; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titleTV.setText("收货信息");
        confirmV.setOnClickListener(this);
        backV.setOnClickListener(this);
        choiceV.setOnClickListener(this);

        Address address = getIntent().getParcelableExtra("address");
        if (null != address) {
            nameET.setText(address.getReceiverName());
            phoneET.setText(address.getPhone());
            addressET.setText(address.getAddress());
            provideCache().put("province", address.getProvince());
            provideCache().put("city", address.getCity());
            provideCache().put("county", address.getCounty());
            provideCache().put("addressId", address.getAddressId());
            countyTV.setText(address.getProvinceName() + " " + address.getCityName() + " " + address.getCountyName());
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
            case R.id.county_chocie:
                showPickerView();
                break;
            case R.id.confirm:
                String phone = phoneET.getText().toString();
                if (!ArmsUtils.isPhoneNum(phone)) {
                    showMessage("手机格式有误");
                    break;
                }
                provideCache().put("receiverName", nameET.getText().toString());
                provideCache().put("phone", phone);
                provideCache().put("address", addressET.getText().toString());
                Address address = getIntent().getParcelableExtra("address");
                if (address != null) { //从地址列表点击编辑跳转过来
                    provideCache().put("isDefaultIn", address.getIsDefaultIn());
                } else {
                    provideCache().put("isDefaultIn", "0");
                }
                mPresenter.modifyAddress(address == null);
                break;
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


    private void showPickerView() {// 弹出选择器

        if (addressList.size() <= 0) {
            return;
        }

        options1Items.clear();
        options2Items.clear();
        options3Items.clear();

        options1Items.addAll(addressList);

        for (AreaAddress areaAddress : addressList) { // 遍历省份
            List<AreaAddress> cities = new ArrayList<>();
            List<List<AreaAddress>> countyList = new ArrayList<>();
            for (AreaAddress city : areaAddress.getAreaList()) {
                cities.add(city);
                List<AreaAddress> counties = new ArrayList<>();//该城市的所有地区列表

                for (AreaAddress county : city.getAreaList()) {
                    counties.add(county);
                }
                countyList.add(counties);
            }
            options2Items.add(cities);

            options3Items.add(countyList);
        }


        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() + " " +
                        options2Items.get(options1).get(options2).getPickerViewText() + " " +
                        options3Items.get(options1).get(options2).get(options3).getPickerViewText();
                countyTV.setText(tx);
                provideCache().put("province", options1Items.get(options1).getAreaId());
                provideCache().put("provinceName", options1Items.get(options1).getName());
                provideCache().put("city", options2Items.get(options1).get(options2).getAreaId());
                provideCache().put("cityName", options2Items.get(options1).get(options2).getName());
                provideCache().put("county", options3Items.get(options1).get(options2).get(options3).getAreaId());
                provideCache().put("countyName", options3Items.get(options1).get(options2).get(options3).getName());
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setCancelColor(Color.parseColor("#FF5fbfe3"))
                .setSubmitColor(Color.parseColor("#FF5fbfe3"))
                .setContentTextSize(20)
                .build();

        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }
}
