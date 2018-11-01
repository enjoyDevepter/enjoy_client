package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;

import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.di.component.DaggerHospitalInfoComponent;
import me.jessyan.mvparms.demo.di.module.HospitalInfoModule;
import me.jessyan.mvparms.demo.mvp.contract.HospitalInfoContract;
import me.jessyan.mvparms.demo.mvp.model.entity.HGoods;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorBean;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.HospitalBean;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.ActivityInfo;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.HospitalInfoBean;
import me.jessyan.mvparms.demo.mvp.presenter.HospitalInfoPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DoctorListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HGoodsListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HospitalEnvImageAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.CarouselView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class HospitalInfoActivity extends BaseActivity<HospitalInfoPresenter> implements HospitalInfoContract.View, View.OnClickListener, DefaultAdapter.OnRecyclerViewItemClickListener {

    public static final String KEY_FOR_HOSPITAL_ID = "key_for_hospital_id";
    public static final String KEY_FOR_HOSPITAL_NAME = "key_for_hospital_name";

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.hot_img)
    ImageView hot_img;
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.follow_layout)
    View followLayout;
    @BindView(R.id.follow)
    View followV;
    @BindView(R.id.ad)
    View adV;
    @BindView(R.id.infos)
    CarouselView carouselView;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    HGoodsListAdapter hospitalGoodsListAdapter;
    @Inject
    DoctorListAdapter doctorListAdapter;
    @Inject
    HospitalEnvImageAdapter hospitalEnvImageAdapter;
    private View[] views = new View[4];
    private String[] titles = new String[]{
            "医院介绍",
            "医院项目",
            "医生列表",
            "医院环境"
    };
    // 第一个页面
    private WebView hospitalInfo;
    // 第二个页面
    private RecyclerView goodsList;
    private SwipeRefreshLayout goodsSwipeRefreshLayout;
    private Paginate goodsPaginate;
    private boolean isGoodsLoadingMore;
    private boolean isGoodsEnd;
    // 第三个页面
    private RecyclerView doctorList;
    private SwipeRefreshLayout doctorSwipeRefreshLayout;
    private Paginate doctorPaginate;
    private boolean isDoctorLoadingMore;
    private boolean isDoctorEnd;
    // 第四个页面
    private RecyclerView envList;

    private void initViewPager() {
        // 初始化第一个页面
        hospitalInfo = new WebView(this);
        hospitalInfo.getSettings().setUseWideViewPort(true);
        hospitalInfo.getSettings().setLoadWithOverviewMode(true);
        views[0] = hospitalInfo;

        // 初始化第二个页面
        goodsSwipeRefreshLayout = (SwipeRefreshLayout) LayoutInflater.from(this).inflate(R.layout.swipe_recyclerview, null);
        goodsList = goodsSwipeRefreshLayout.findViewById(R.id.list);
        goodsList.setAdapter(hospitalGoodsListAdapter);
        hospitalGoodsListAdapter.setOnItemClickListener(this);
        LinearLayoutManager goodsListLayoutManager = new LinearLayoutManager(this);
        ArmsUtils.configRecyclerView(goodsList, goodsListLayoutManager);
        views[1] = goodsSwipeRefreshLayout;
        Paginate.Callbacks goodsPaginateCallback = new Paginate.Callbacks() {
            @Override
            public void onLoadMore() {
                mPresenter.getHGoodsList(false);
            }

            @Override
            public boolean isLoading() {
                return isGoodsLoadingMore;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return isGoodsEnd;
            }
        };

        goodsPaginate = Paginate.with(goodsList, goodsPaginateCallback)
                .setLoadingTriggerThreshold(0)
                .build();
        goodsPaginate.setHasMoreDataToLoad(false);
        goodsSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getHGoodsList(true);
            }
        });

        // 初始化第三个页面
        doctorSwipeRefreshLayout = (SwipeRefreshLayout) LayoutInflater.from(this).inflate(R.layout.swipe_recyclerview, null);
        doctorList = doctorSwipeRefreshLayout.findViewById(R.id.list);
        LinearLayoutManager doctorLayoutManager = new LinearLayoutManager(this);
        ArmsUtils.configRecyclerView(doctorList, doctorLayoutManager);
        doctorListAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                Intent intent = new Intent(HospitalInfoActivity.this, DoctorMainActivity.class);
                intent.putExtra(DoctorMainActivity.KEY_FOR_DOCTOR_ID, ((DoctorBean) data).getDoctorId());
                ArmsUtils.startActivity(intent);
            }
        });
        doctorList.setAdapter(doctorListAdapter);
        views[2] = doctorSwipeRefreshLayout;
        Paginate.Callbacks doctorPaginateCallback = new Paginate.Callbacks() {
            @Override
            public void onLoadMore() {
                mPresenter.requestDoctor(false);
            }

            @Override
            public boolean isLoading() {
                return isDoctorLoadingMore;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return isDoctorEnd;
            }
        };

        doctorPaginate = Paginate.with(doctorList, doctorPaginateCallback)
                .setLoadingTriggerThreshold(0)
                .build();
        doctorPaginate.setHasMoreDataToLoad(false);
        doctorSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.requestDoctor(true);
            }
        });

        // 初始化第四个页面
        envList = new RecyclerView(this);
        GridLayoutManager envLayoutManager = new GridLayoutManager(this, 2);
        ArmsUtils.configRecyclerView(envList, envLayoutManager);
        envList.setAdapter(hospitalEnvImageAdapter);
        envList.addItemDecoration(new RecyclerView.ItemDecoration() {

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int margin = ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.space_10);
                if (parent.getChildLayoutPosition(view) % 2 == 0) {
                    outRect.set(0, 0, margin, margin);
                } else {
                    outRect.set(0, 0, 0, margin);
                }
            }
        });
        views[3] = envList;


        // 初始化viewPager
        viewpager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return views.length;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View view = views[position];
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        });
    }

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerHospitalInfoComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .hospitalInfoModule(new HospitalInfoModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_hospital_info; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        title.setText(getIntent().getStringExtra(KEY_FOR_HOSPITAL_NAME));
        back.setOnClickListener(this);
        followLayout.setOnClickListener(this);
        initViewPager();
        tab.setupWithViewPager(viewpager);
        provideCache().put(KEY_FOR_HOSPITAL_ID, getIntent().getStringExtra(KEY_FOR_HOSPITAL_ID));
        provideCache().put(KEY_FOR_HOSPITAL_NAME, getIntent().getStringExtra(KEY_FOR_HOSPITAL_NAME));
    }

    @Subscriber(tag = EventBusTags.CHANGE_HOSPITAL)
    public void init(HospitalBean hospitalBean) {
        provideCache().put(KEY_FOR_HOSPITAL_ID, hospitalBean.getHospitalId());
        provideCache().put(KEY_FOR_HOSPITAL_NAME, hospitalBean.getName());
        title.setText(hospitalBean.getName());
        mPresenter.init();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    public void hideDoctorLoading() {
        ((SwipeRefreshLayout) views[2]).setRefreshing(false);
    }

    public void hideGoodsLoading() {
        ((SwipeRefreshLayout) views[1]).setRefreshing(false);
    }

    @Override
    public Cache getCache() {
        return provideCache();
    }

    @Override
    public void updatefollowStatus(boolean follow) {
        followV.setSelected(follow);
    }

    public void startLoadGoodsMore() {
        isGoodsLoadingMore = true;
    }

    public void endLoadGoodsMore() {
        isGoodsLoadingMore = false;
    }

    public void startLoadDoctorMore() {
        isGoodsLoadingMore = true;
    }

    public void endLoadDoctorMore() {
        isGoodsLoadingMore = false;
    }

    public void endGoods(boolean isGoodsEnd) {
        this.isGoodsEnd = isGoodsEnd;
    }

    public void endDoctor(boolean isDoctorEnd) {
        this.isDoctorEnd = isDoctorEnd;
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
            case R.id.follow_layout:
                mPresenter.follow(!followV.isSelected());
                break;
        }
    }

    public Activity getActivity() {
        return this;
    }

    @Override
    public void updateActivityInfo(List<ActivityInfo> activityInfoList) {
        if (null == activityInfoList || (activityInfoList != null && activityInfoList.size() <= 0)) {
            adV.setVisibility(View.GONE);
        } else {
            carouselView.removeAllViews();
            carouselView.addView(R.layout.carouse_view);
            carouselView.upDataListAndView(activityInfoList, 2000);
            carouselView.startLooping();
            carouselView.setOnClickListener(new CarouselView.OnClickItemListener() {
                @Override
                public void onClick(int position) {
                    Intent intent = new Intent(getApplication(), ActivityInfoActivity.class);
                    intent.putExtra("activityId", activityInfoList.get(position).getActivityId());
                    intent.putExtra("title", activityInfoList.get(position).getTitle());
                    intent.putExtra(KEY_FOR_HOSPITAL_ID, getIntent().getStringExtra(KEY_FOR_HOSPITAL_ID));
                    ArmsUtils.startActivity(intent);
                }
            });
        }
    }

    public void updateHosptialInfo(HospitalInfoBean hospital) {
        WebView webView = (WebView) views[0];
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.loadUrl(hospital.getIntro());
        mImageLoader.loadImage(this,
                ImageConfigImpl
                        .builder()
                        .placeholder(R.drawable.place_holder_img)
                        .url(hospital.getImage())
                        .isCenterCrop(true)
                        .imageView(hot_img)
                        .build());
        followV.setSelected("1".equals(hospital.getIsFollow()) ? true : false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        carouselView.stopLooping();
    }

    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(doctorList);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        DefaultAdapter.releaseAllHolder(envList);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        DefaultAdapter.releaseAllHolder(goodsList);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
        doctorPaginate = null;
        goodsPaginate = null;
    }

    @Override
    public void onItemClick(View view, int viewType, Object data, int position) {
        HGoods hGoods = hospitalGoodsListAdapter.getInfos().get(position);
        Intent hGoodsintent = new Intent(getActivity().getApplication(), HGoodsDetailsActivity.class);
        hGoodsintent.putExtra("goodsId", hGoods.getGoodsId());
        hGoodsintent.putExtra("merchId", hGoods.getMerchId());
        hGoodsintent.putExtra("advanceDepositId", hGoods.getAdvanceDepositId());
        ArmsUtils.startActivity(hGoodsintent);
    }
}
