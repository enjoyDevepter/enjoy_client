package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerChoiceTimeComponent;
import me.jessyan.mvparms.demo.di.module.ChoiceTimeModule;
import me.jessyan.mvparms.demo.mvp.contract.ChoiceTimeContract;
import me.jessyan.mvparms.demo.mvp.model.HAppointments;
import me.jessyan.mvparms.demo.mvp.model.entity.HAppointmentsTime;
import me.jessyan.mvparms.demo.mvp.presenter.ChoiceTimePresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DateAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.TimeAdapter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ChoiceTimeActivity extends BaseActivity<ChoiceTimePresenter> implements ChoiceTimeContract.View, View.OnClickListener, DefaultAdapter.OnRecyclerViewItemClickListener {

    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.confirm)
    TextView confrimTV;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.date)
    RecyclerView dateRV;
    @BindView(R.id.time)
    RecyclerView timeRV;
    @Inject
    DateAdapter dateAdapter;
    @Inject
    RecyclerView.LayoutManager layoutManager;
    @Inject
    TimeAdapter timeAdapter;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerChoiceTimeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .choiceTimeModule(new ChoiceTimeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_choice_time; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titleTV.setText("选择时间");
        backV.setOnClickListener(this);
        confrimTV.setOnClickListener(this);
        dateAdapter.setOnItemClickListener(this);
        timeAdapter.setOnItemClickListener(this);
        ArmsUtils.configRecyclerView(dateRV, layoutManager);
        ArmsUtils.configRecyclerView(timeRV, new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        dateRV.setAdapter(dateAdapter);
        timeRV.setAdapter(timeAdapter);
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
            case R.id.confirm:
                if ("choice_time".equals(getActivity().getIntent().getStringExtra("type"))) {
                    Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(this).extras();
                    cache.put("appointmentsDate", provideCache().get("appointmentsDate"));
                    cache.put("appointmentsTime", provideCache().get("appointmentsTime"));
                    killMyself();
                } else {
                    mPresenter.modifyAppointmentTime();
                }
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

    @Override
    public void onItemClick(View view, int viewType, Object data, int position) {
        List<HAppointments> appointments = dateAdapter.getInfos();
        List<HAppointmentsTime> timeList = timeAdapter.getInfos();
        switch (viewType) {
            case R.layout.goods_filter_second_item:
                for (int i = 0; i < appointments.size(); i++) {
                    appointments.get(i).setChoice(i == position ? true : false);
                }
                timeList.clear();
                timeList.addAll(appointments.get(position).getReservationTimeList());
                for (HAppointmentsTime time : timeList) {
                    time.setChoice(false);
                }
                provideCache().put("appointmentsTime", "");
                provideCache().put("appointmentsDate", appointments.get(position).getDate());
                dateAdapter.notifyDataSetChanged();
                timeAdapter.notifyDataSetChanged();
                break;
            case R.layout.goods_filter_third_item:
                for (int i = 0; i < timeList.size(); i++) {
                    timeList.get(i).setChoice(i == position ? true : false);
                }
                timeAdapter.notifyDataSetChanged();
                provideCache().put("appointmentsTime", timeList.get(position).getTime());
                break;
        }
    }

    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(dateRV);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        DefaultAdapter.releaseAllHolder(timeRV);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
    }
}
