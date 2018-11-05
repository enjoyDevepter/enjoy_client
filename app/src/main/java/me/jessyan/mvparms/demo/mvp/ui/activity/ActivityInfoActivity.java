package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerActivityInfoComponent;
import me.jessyan.mvparms.demo.di.module.ActivityInfoModule;
import me.jessyan.mvparms.demo.mvp.contract.ActivityInfoContract;
import me.jessyan.mvparms.demo.mvp.model.entity.hospital.bean.ActivityInfo;
import me.jessyan.mvparms.demo.mvp.presenter.ActivityInfoPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.ActivityListAdapter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ActivityInfoActivity extends BaseActivity<ActivityInfoPresenter> implements ActivityInfoContract.View, View.OnClickListener, DefaultAdapter.OnRecyclerViewItemClickListener {

    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.image)
    ImageView imageIV;
    @BindView(R.id.content)
    TextView contentTV;
    @BindView(R.id.tab)
    TabLayout tabLayout;
    @BindView(R.id.infos)
    RecyclerView infosRV;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    ActivityListAdapter mAdapter;
    @Inject
    ImageLoader mImageLoader;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityInfoComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .activityInfoModule(new ActivityInfoModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_info; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        titleTV.setText(getIntent().getStringExtra("title"));
        tabLayout.addTab(tabLayout.newTab().setText("最新活动"));
        ArmsUtils.configRecyclerView(infosRV, mLayoutManager);
        infosRV.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
        provideCache().put("activityId", getIntent().getStringExtra("activityId"));
        provideCache().put("isFromStore", getIntent().getBooleanExtra("isFromStore", false));
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
    public Cache getCache() {
        return provideCache();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void updateUI(ActivityInfo activityInfo) {
        titleTV.setText(activityInfo.getTitle());
        contentTV.setText(Html.fromHtml(activityInfo.getContent()));
        if (ArmsUtils.isEmpty(activityInfo.getImage())) {
            imageIV.setVisibility(View.GONE);
        } else {
            imageIV.setVisibility(View.VISIBLE);
            mImageLoader.loadImage(imageIV.getContext(),
                    ImageConfigImpl
                            .builder()
                            .placeholder(R.drawable.place_holder_img)
                            .url(activityInfo.getImage())
                            .isCenterCrop(true)
                            .imageView(imageIV)
                            .build());
        }
    }

    @Override
    public void updateInfos(boolean show) {
        tabLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        infosRV.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(infosRV);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
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
    public void onItemClick(View view, int viewType, Object data, int position) {
        getCache().put("activityId", mAdapter.getInfos().get(position).getActivityId());
        mPresenter.getActivityInfo();
    }
}
