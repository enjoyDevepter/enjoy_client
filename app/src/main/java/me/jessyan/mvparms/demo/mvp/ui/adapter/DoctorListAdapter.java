package me.jessyan.mvparms.demo.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorBean;
import me.jessyan.mvparms.demo.mvp.ui.holder.DoctorListHolder;

public class DoctorListAdapter extends DefaultAdapter<DoctorBean> {
    public DoctorListAdapter(List<DoctorBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<DoctorBean> getHolder(View v, int viewType) {
        return new DoctorListHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.hospital_info_doctor_list_item;
    }
}
