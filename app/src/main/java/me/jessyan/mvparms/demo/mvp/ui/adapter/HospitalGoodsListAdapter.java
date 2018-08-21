package me.jessyan.mvparms.demo.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorBean;
import me.jessyan.mvparms.demo.mvp.ui.holder.DoctorListHolder;
import me.jessyan.mvparms.demo.mvp.ui.holder.HospitalGoodsListHolder;

public class HospitalGoodsListAdapter extends DefaultAdapter<Goods> {
    public HospitalGoodsListAdapter(List<Goods> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<Goods> getHolder(View v, int viewType) {
        return new HospitalGoodsListHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.hospital_info_doctor_list_item;
    }
}
