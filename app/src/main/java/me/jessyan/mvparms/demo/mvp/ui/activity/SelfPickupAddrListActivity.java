package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerSelfPickupAddrListComponent;
import me.jessyan.mvparms.demo.di.module.SelfPickupAddrListModule;
import me.jessyan.mvparms.demo.mvp.contract.SelfPickupAddrListContract;
import me.jessyan.mvparms.demo.mvp.model.entity.AreaAddress;
import me.jessyan.mvparms.demo.mvp.presenter.SelfPickupAddrListPresenter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class SelfPickupAddrListActivity extends BaseActivity<SelfPickupAddrListPresenter> implements SelfPickupAddrListContract.View, View.OnClickListener {

    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.district)
    TextView districtV;
    @BindView(R.id.district_layout)
    View districtLayoutV;
    @BindView(R.id.store)
    TextView storeV;
    @BindView(R.id.store_layout)
    View storeLayoutV;
    @BindView(R.id.confirm)
    View confirmV;

    @Inject
    List<AreaAddress> addressList;
    private List<AreaAddress> options1Items = new ArrayList<>();
    private List<List<AreaAddress>> options2Items = new ArrayList<>();
    private List<List<List<AreaAddress>>> options3Items = new ArrayList<>();

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerSelfPickupAddrListComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .selfPickupAddrListModule(new SelfPickupAddrListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_self_pickup_addr_list; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titleTV.setText("自提地址");
        backV.setOnClickListener(this);
        districtV.setOnClickListener(this);
        storeV.setOnClickListener(this);
        confirmV.setOnClickListener(this);
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
            case R.id.district_layout:
                showPickerView();
                break;
            case R.id.store_layout:
                Intent intent = new Intent(this, ChoiceStoreActivity.class);
                intent.putExtra("province", (String) provideCache().get("province"));
                intent.putExtra("city", (String) provideCache().get("city"));
                intent.putExtra("county", (String) provideCache().get("county"));
                ArmsUtils.startActivity(intent);
                break;
            case R.id.confirm:
                break;
        }
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
                districtV.setText(tx);
                provideCache().put("province", options1Items.get(options1).getAreaId());
                provideCache().put("city", options2Items.get(options1).get(options2).getAreaId());
                provideCache().put("county", options3Items.get(options1).get(options2).get(options3).getAreaId());
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
