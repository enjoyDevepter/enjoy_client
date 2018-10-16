package me.jessyan.mvparms.demo.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;

import org.simple.eventbus.Subscriber;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.di.component.DaggerAppointmentComponent;
import me.jessyan.mvparms.demo.di.module.AppointmentModule;
import me.jessyan.mvparms.demo.mvp.contract.AppointmentContract;
import me.jessyan.mvparms.demo.mvp.model.entity.appointment.Appointment;
import me.jessyan.mvparms.demo.mvp.presenter.AppointmentPresenter;
import me.jessyan.mvparms.demo.mvp.ui.activity.ChoiceTimeActivity;
import me.jessyan.mvparms.demo.mvp.ui.adapter.AppointmentListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.CustomDialog;
import me.jessyan.mvparms.demo.mvp.ui.widget.SpacesItemDecoration;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class AppointmentFragment extends BaseFragment<AppointmentPresenter> implements AppointmentContract.View, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, AppointmentListAdapter.OnChildItemClickLinstener, TabLayout.OnTabSelectedListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.shengmei)
    View shengmeiV;
    @BindView(R.id.yimei1)
    View yimei1V;
    @BindView(R.id.yimei2)
    View yimei2V;
    @BindView(R.id.tab)
    TabLayout tabLayout;
    @BindView(R.id.no_data)
    View noDataV;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.appointments)
    RecyclerView mRecyclerView;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    AppointmentListAdapter mAdapter;
    CustomDialog dialog = null;
    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;

    public static AppointmentFragment newInstance() {
        AppointmentFragment fragment = new AppointmentFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerAppointmentComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .appointmentModule(new AppointmentModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_appointment, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setVisibility(View.INVISIBLE);
        titleTV.setText("预约");
        shengmeiV.setOnClickListener(this);
        yimei1V.setOnClickListener(this);
        yimei2V.setOnClickListener(this);
        yimei1V.setSelected(true);
        swipeRefreshLayout.setOnRefreshListener(this);
        tabLayout.addTab(tabLayout.newTab().setTag("status").setText("全部"));
        tabLayout.addTab(tabLayout.newTab().setTag("status").setText("已预约"));
        tabLayout.addTab(tabLayout.newTab().setTag("status").setText("可预约"));
        tabLayout.addOnTabSelectedListener(this);
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(0, ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.address_list_item_space)));
        mAdapter.setOnChildItemClickLinstener(this);
        provideCache().put("type", 1);
        provideCache().put("status", 0);
        initPaginate();
        mPresenter.getAppointment(true, false);
    }


    @Subscriber(tag = EventBusTags.CHANGE_APPOINTMENT_TIME)
    private void updateTime(int position) {
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传Message,通过what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onCreate还没执行
     * setData里却调用了presenter的方法时,是会报空的,因为dagger注入是在onCreated方法中执行的,然后才创建的presenter
     * 如果要做一些初始化操作,可以不必让外部调setData,在initData中初始化就可以了
     *
     * @param data
     */

    @Override
    public void setData(Object data) {
    }

    /**
     * 开始加载更多
     */
    @Override
    public void startLoadMore() {
        isLoadingMore = true;
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
    public Cache getCache() {
        return provideCache();
    }

    @Override
    public void showError(boolean hasData) {
        swipeRefreshLayout.setVisibility(hasData ? View.VISIBLE : View.GONE);
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
                    mPresenter.getAppointment(true, false);
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

    @Subscriber(tag = EventBusTags.ONREFRESH_CONTENT)
    public void refreshContent(int index) {
        if (index == 3) {
            mPresenter.getAppointment(true, true);
        }
    }


    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }


    @Override
    public void killMyself() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shengmei:
                provideCache().put("type", 0);
                shengmeiV.setSelected(true);
                yimei1V.setSelected(false);
                yimei2V.setSelected(false);
                break;
            case R.id.yimei1:
                provideCache().put("type", 1);
                shengmeiV.setSelected(false);
                yimei1V.setSelected(true);
                yimei2V.setSelected(false);
                break;
            case R.id.yimei2:
                provideCache().put("type", 2);
                shengmeiV.setSelected(false);
                yimei1V.setSelected(false);
                yimei2V.setSelected(true);
                break;
        }
        mPresenter.getAppointment(true, false);
    }

    @Override
    public void onRefresh() {
        mPresenter.getAppointment(true, false);
    }

    @Override
    public void onChildItemClick(View v, AppointmentListAdapter.ViewName viewname, int position) {
        Appointment appointment = mAdapter.getInfos().get(position);
        switch (viewname) {
            case LEFT:
                if (appointment.getStatus().equals("2")) {
                    // 取消预约
                    provideCache().put("reservationId", appointment.getReservationId());
                    showDailog("是否取消预约?");
                }
                break;
            case RIGHT:
                if (appointment.getStatus().equals("1")) {
                    // 预约
                    Intent addappointmentsIntent = new Intent(getActivity(), ChoiceTimeActivity.class);
                    addappointmentsIntent.putExtra("isMeal", false);
                    addappointmentsIntent.putExtra("projectId", appointment.getProjectId());
                    addappointmentsIntent.putExtra("type", "add_appointment_time");
                    addappointmentsIntent.putExtra("index", position);
                    ArmsUtils.startActivity(addappointmentsIntent);
                } else if (appointment.getStatus().equals("2")) {
                    // 改约
                    Intent addappointmentsIntent = new Intent(getActivity(), ChoiceTimeActivity.class);
                    addappointmentsIntent.putExtra("isMeal", false);
                    addappointmentsIntent.putExtra("reservationId", appointment.getReservationId());
                    addappointmentsIntent.putExtra("type", "modify_appointment_time");
                    addappointmentsIntent.putExtra("index", position);
                    ArmsUtils.startActivity(addappointmentsIntent);
                }
                break;
            case ITEM:
                break;
        }
    }

    private void showDailog(String text) {
        dialog = CustomDialog.create(getFragmentManager())
                .setViewListener(new CustomDialog.ViewListener() {
                    @Override
                    public void bindView(View view) {
                        ((TextView) view.findViewById(R.id.content)).setText(text);
                        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPresenter.cancelAppointment();
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setLayoutRes(R.layout.dialog_remove_good_for_cart)
                .setDimAmount(0.5f)
                .isCenter(true)
                .setWidth(ArmsUtils.getDimens(getContext(), R.dimen.dialog_width))
                .setHeight(ArmsUtils.getDimens(getContext(), R.dimen.dialog_height))
                .show();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        provideCache().put("status", tab.getPosition());
        mPresenter.getAppointment(true, false);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onDestroy() {
        DefaultAdapter.releaseAllHolder(mRecyclerView);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
    }
}
