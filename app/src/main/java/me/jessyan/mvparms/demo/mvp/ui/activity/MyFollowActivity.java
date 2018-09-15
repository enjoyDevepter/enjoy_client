package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerMyFollowComponent;
import me.jessyan.mvparms.demo.di.module.MyFollowModule;
import me.jessyan.mvparms.demo.mvp.contract.MyFollowContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Hospital;
import me.jessyan.mvparms.demo.mvp.model.entity.Member;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorBean;
import me.jessyan.mvparms.demo.mvp.presenter.MyFollowPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyFollowDoctorAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyFollowHospitalAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyFollowMemberAdapter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MyFollowActivity extends BaseActivity<MyFollowPresenter> implements MyFollowContract.View, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, TabLayout.OnTabSelectedListener, MyFollowMemberAdapter.OnChildItemClickLinstener, MyFollowHospitalAdapter.OnChildItemClickLinstener, MyFollowDoctorAdapter.OnChildItemClickLinstener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.tab)
    TabLayout tabLayout;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_date)
    View noDataV;
    @BindView(R.id.follows)
    RecyclerView mRecyclerView;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    MyFollowMemberAdapter memberAdapter;
    @Inject
    MyFollowHospitalAdapter hospitalAdapter;
    @Inject
    MyFollowDoctorAdapter doctorAdapter;

    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerMyFollowComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .myFollowModule(new MyFollowModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_my_follow; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titleTV.setText("我的关注");
        backV.setOnClickListener(this);
        backV.setOnClickListener(this);
        provideCache().put("status", 0);
        swipeRefreshLayout.setOnRefreshListener(this);
        tabLayout.addTab(tabLayout.newTab().setText("会员"));
        tabLayout.addTab(tabLayout.newTab().setText("医生"));
        tabLayout.addTab(tabLayout.newTab().setText("医院"));
        tabLayout.addOnTabSelectedListener(this);
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(memberAdapter);
        memberAdapter.setOnChildItemClickLinstener(this);
        hospitalAdapter.setOnChildItemClickLinstener(this);
        doctorAdapter.setOnChildItemClickLinstener(this);
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
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
    public void showConent(boolean hasData) {
        swipeRefreshLayout.setVisibility(hasData ? View.VISIBLE : View.GONE);
        noDataV.setVisibility(hasData ? View.GONE : View.VISIBLE);
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


    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.getMyFollow(false);
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

            mPaginate = Paginate.with(mRecyclerView, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .build();
            mPaginate.setHasMoreDataToLoad(false);
        }
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
    public void onRefresh() {
        mPresenter.getMyFollow(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        provideCache().put("status", tab.getPosition());
        switch (tab.getPosition()) {
            case 0:
                mRecyclerView.setAdapter(memberAdapter);
                break;
            case 1:
                mRecyclerView.setAdapter(doctorAdapter);
                break;
            case 2:
                mRecyclerView.setAdapter(hospitalAdapter);
                break;
        }
        initPaginate();
        mPresenter.getMyFollow(true);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onChildItemClick(View v, MyFollowMemberAdapter.ViewName viewname, int position) {
        Member member = memberAdapter.getInfos().get(position);
        switch (viewname) {
            case FLLOW:
                provideCache().put("memberId", member.getMemberId());
                mPresenter.unfollow();
                break;
            case ITEM:
                break;
        }
    }

    @Override
    public void onChildItemClick(View v, MyFollowHospitalAdapter.ViewName viewname, int position) {
        Hospital hospital = hospitalAdapter.getInfos().get(position);
        switch (viewname) {
            case FLLOW:
                provideCache().put("hospitalId", hospital.getHospitalId());
                mPresenter.unfollow();
                break;
            case ITEM:
                break;
        }
    }

    @Override
    public void onChildItemClick(View v, MyFollowDoctorAdapter.ViewName viewname, int position) {
        DoctorBean doctor = doctorAdapter.getInfos().get(position);
        switch (viewname) {
            case FLLOW:
                provideCache().put("doctorId", doctor.getDoctorId());
                mPresenter.unfollow();
                break;
            case ITEM:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(mRecyclerView);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
    }
}
