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
import com.jess.arms.integration.cache.Cache;
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

    // 要启动这个页面，必须使用这个Key，将ListType作为参数通过intent传递进来
    public static final String KEY_FOR_ACTIVITY_LIST_TYPE = "key_for_activity_list_type";

    private ListType listType;

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
        listType = (ListType) getIntent().getSerializableExtra(KEY_FOR_ACTIVITY_LIST_TYPE);
        if(listType == null){
            throw new RuntimeException("listType is not null.you need send listType use key  \"KEY_FOR_ACTIVITY_LIST_TYPE\"");
        }
        titleTV.setText(listType.getTitle());
        backV.setOnClickListener(this);
        confirmV.setOnClickListener(this);
        storeLayoutV.setOnClickListener(this);
        districtLayoutV.setOnClickListener(this);
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
                intent.putExtra(KEY_FOR_ACTIVITY_LIST_TYPE,listType);
                ArmsUtils.startActivity(intent);
                break;
            case R.id.confirm:
                Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(this).extras();
                if (cache.get(listType.getDataKey()) == null) {
                    showMessage("请选择店铺！");
                    return;
                }
                killMyself();
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

    /**
     * 这个页面可以进行复用。使用这个
     * */
    public enum ListType{
        HOP("选择医院","选择医院","请选择为您服务的医院","choose_hosptial_info"),  // 选择医院
        STORE("选择店铺","选择店铺","请选择为您服务的店铺","choose_store_info"),  //选择店铺
        ADDR("自提地址","选择店铺","请选择为您服务的店铺","choose_addr_info"); // 自提地址

        private String title;  // 整个页面的标题
        private String secendListTitle;  // 第二个选项的标题，也就是第二个Activity的大标题
        private String infoText;  // 第二个页面的说明文字
        private String dataKey;  // 携带数据的时候，使用的key。每个页面使用的Key应该不一样，避免相互覆盖

        ListType(String title,String secendListTitle,String infoText,String dataKey){
            this.title = title;
            this.secendListTitle = secendListTitle;
            this.infoText = infoText;
            this.dataKey = dataKey;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSecendListTitle() {
            return secendListTitle;
        }

        public void setSecendListTitle(String secendListTitle) {
            this.secendListTitle = secendListTitle;
        }

        public String getInfoText() {
            return infoText;
        }

        public void setInfoText(String infoText) {
            this.infoText = infoText;
        }

        public String getDataKey() {
            return dataKey;
        }

        public void setDataKey(String dataKey) {
            this.dataKey = dataKey;
        }
    }
}
