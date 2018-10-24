package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.di.component.DaggerSelfPickupAddrListComponent;
import me.jessyan.mvparms.demo.di.module.SelfPickupAddrListModule;
import me.jessyan.mvparms.demo.mvp.contract.SelfPickupAddrListContract;
import me.jessyan.mvparms.demo.mvp.model.entity.AreaAddress;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.HospitalBaseInfoBean;
import me.jessyan.mvparms.demo.mvp.presenter.SelfPickupAddrListPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.StoresListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.SpacesItemDecoration;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class SelfPickupAddrListActivity extends BaseActivity<SelfPickupAddrListPresenter> implements SelfPickupAddrListContract.View, View.OnClickListener, DefaultAdapter.OnRecyclerViewItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    // 要启动这个页面，必须使用这个Key，将ListType作为参数通过intent传递进来
    public static final String KEY_FOR_ACTIVITY_LIST_TYPE = "key_for_activity_list_type";
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.district)
    TextView districtV;
    @BindView(R.id.district_layout)
    View districtLayoutV;
    @BindView(R.id.confirm)
    View confirmV;
    @BindView(R.id.stores)
    RecyclerView storesRV;
    @BindView(R.id.no_date)
    View noDataV;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    StoresListAdapter mAdapter;
    @Inject
    List<AreaAddress> addressList;

    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;

    private ListType listType;
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
        if (listType == null) {
            throw new RuntimeException("listType is not null.you need send listType use key  \"KEY_FOR_ACTIVITY_LIST_TYPE\"");
        }
        titleTV.setText(listType.getSecendListTitle());
        backV.setOnClickListener(this);
        confirmV.setOnClickListener(this);
        districtLayoutV.setOnClickListener(this);
        districtV.setText(listType.getDistrict());

        provideCache().put("province", listType.getProvince());
        provideCache().put("city", listType.getCity());
        provideCache().put("county", listType.getCity());
        provideCache().put("goodsId", listType.getGoodsId());
        provideCache().put("merchId", listType.getMerchId());

        ArmsUtils.configRecyclerView(storesRV, mLayoutManager);
        storesRV.setAdapter(mAdapter);
        storesRV.addItemDecoration(new SpacesItemDecoration(0, ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.address_list_item_space)));
        mAdapter.setOnItemClickListener(this);
        initPaginate();
        swipeRefreshLayout.setOnRefreshListener(this);
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
            case R.id.confirm:
                SelfPickupAddrListActivity.ListType listType = (SelfPickupAddrListActivity.ListType) this.getIntent().getSerializableExtra(KEY_FOR_ACTIVITY_LIST_TYPE);
                if (null != provideCache().get("choiceItem")) {
                    int index = (int) provideCache().get("choiceItem");
                    switch (listType) {
                        case HOP:
                            if (getIntent().getBooleanExtra("isMeal", false)) {
                                // 从套餐详情页面进入，进入选择时间界面
                                // 预约
                                Intent addappointmentsIntent = new Intent(this, ChoiceTimeActivity.class);
                                addappointmentsIntent.putExtra("isMeal", true);
                                addappointmentsIntent.putExtra("projectId", getIntent().getStringExtra("projectId"));
                                addappointmentsIntent.putExtra("type", getIntent().getStringExtra("type"));
                                addappointmentsIntent.putExtra("merchId", getIntent().getStringExtra("merchId"));
                                addappointmentsIntent.putExtra("goodsId", getIntent().getStringExtra("goodsId"));
                                addappointmentsIntent.putExtra("reservationId", getIntent().getStringExtra("reservationId"));
                                addappointmentsIntent.putExtra("hospitalId", ((HospitalBaseInfoBean) mAdapter.getInfos().get(index)).getHospitalId());
                                ArmsUtils.startActivity(addappointmentsIntent);
                            } else {
                                EventBus.getDefault().post(mAdapter.getInfos().get(index), EventBusTags.HOSPITAL_CHANGE_EVENT);
                            }
                            break;
                        case STORE:
                            EventBus.getDefault().post(mAdapter.getInfos().get(index), EventBusTags.STORE_CHANGE_EVENT);
                            break;
                        case ADDR:
                            break;
                    }
                } else {
                    switch (listType) {
                        case HOP:
                            showMessage("请选择医院！");
                            return;
                        case STORE:
                            showMessage("请选择店铺信息！");
                            return;
                        case ADDR:
                            return;
                    }
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
                AreaAddress countyCity = new AreaAddress();
                countyCity.setName("全部");
                counties.add(countyCity);
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
                mPresenter.getData(true);
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

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 开始加载更多
     */
    @Override
    public void startLoadMore() {
        isLoadingMore = true;
    }

    /**
     * 结束加载更多
     */
    @Override
    public void endLoadMore() {
        isLoadingMore = false;
    }

    @Override
    public void setLoadedAllItems(boolean has) {
        this.hasLoadedAllItems = has;
    }

    @Override
    public void showConent(boolean hasData) {
        storesRV.setVisibility(hasData ? View.VISIBLE : View.GONE);
        noDataV.setVisibility(hasData ? View.GONE : View.VISIBLE);
    }

    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.getData(false);
                }

                @Override
                public boolean isLoading() {
                    return isLoadingMore;
                }

                @Override
                public boolean hasLoadedAllItems() {
                    return hasLoadedAllItems;
                }
            };

            mPaginate = Paginate.with(storesRV, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .build();
            mPaginate.setHasMoreDataToLoad(false);
        }
    }


    @Override
    public void onRefresh() {
        mPresenter.getData(true);
    }


    @Override
    public void onItemClick(View view, int viewType, Object data, int position) {
        for (int i = 0; i < mAdapter.getInfos().size(); i++) {
            mAdapter.getInfos().get(i).setCheck(i == position ? true : false);
        }
        mAdapter.notifyDataSetChanged();
        provideCache().put("choiceItem", position);
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
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(storesRV);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
    }

    /**
     * 这个页面可以进行复用。使用这个
     */
    public enum ListType {
        HOP("选择机构", "选择机构: ", "请选择为您服务的机构", "choose_hosptial_info"),  // 选择医院
        STORE("选择店铺", "选择店铺: ", "请选择为您服务的店铺", "choose_store_info"),  //选择店铺
        ADDR("自提地址", "选择店铺: ", "请选择为您服务的店铺", "choose_addr_info"); // 自提地址

        private String title;  // 整个页面的标题
        private String secendListTitle;  // 第二个选项的标题，也就是第二个Activity的大标题
        private String infoText;  // 第二个页面的说明文字
        private String dataKey;  // 携带数据的时候，使用的key。每个页面使用的Key应该不一样，避免相互覆盖
        private String district;
        private String province;
        private String city;
        private String county;
        private String storeName;
        private String merchId;
        private String goodsId;

        ListType(String title, String secendListTitle, String infoText, String dataKey) {
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

        public String getStoreName() {
            return storeName;
        }

        public void setStoreName(String storeName) {
            this.storeName = storeName;
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

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCounty() {
            return county;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public String getMerchId() {
            return merchId;
        }

        public void setMerchId(String merchId) {
            this.merchId = merchId;
        }

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }
    }
}
