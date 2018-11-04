package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import org.simple.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.di.component.DaggerDoctorForHospitalComponent;
import me.jessyan.mvparms.demo.di.module.DoctorForHospitalModule;
import me.jessyan.mvparms.demo.mvp.contract.DoctorForHospitalContract;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.HospitalBean;
import me.jessyan.mvparms.demo.mvp.presenter.DoctorForHospitalPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HospitalRelatedListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.SpacesItemDecoration;

import static com.jess.arms.utils.ArmsUtils.getContext;
import static com.jess.arms.utils.Preconditions.checkNotNull;
import static me.jessyan.mvparms.demo.mvp.ui.activity.HospitalInfoActivity.KEY_FOR_HOSPITAL_NAME;


public class DoctorForHospitalActivity extends BaseActivity<DoctorForHospitalPresenter> implements DoctorForHospitalContract.View, View.OnClickListener, DefaultAdapter.OnRecyclerViewItemClickListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.content)
    RecyclerView mRecyclerView;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    HospitalRelatedListAdapter mAdapter;
    @Inject
    List<HospitalBean> hospitalList;


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerDoctorForHospitalComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .doctorForHospitalModule(new DoctorForHospitalModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_doctor_for_hospital; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titleTV.setText("所属医院");
        backV.setOnClickListener(this);
        hospitalList.addAll(getIntent().getParcelableArrayListExtra("hospitals"));
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(0, ArmsUtils.getDimens(getContext(), R.dimen.address_list_item_space)));
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
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
        }
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void onItemClick(View view, int viewType, Object data, int position) {
        HospitalBean hospital = mAdapter.getInfos().get(position);
        Intent hospitalIntent = new Intent(this, HospitalInfoActivity.class);
        hospitalIntent.putExtra(KEY_FOR_HOSPITAL_NAME, hospital.getName());
        hospitalIntent.putExtra(HospitalInfoActivity.KEY_FOR_HOSPITAL_ID, hospital.getHospitalId());
        ArmsUtils.startActivity(hospitalIntent);
        EventBus.getDefault().post(hospital, EventBusTags.CHANGE_HOSPITAL);
    }
}
