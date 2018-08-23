package me.jessyan.mvparms.demo.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorBean;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorCommentBean;
import me.jessyan.mvparms.demo.mvp.ui.holder.DoctorCommentHolder;
import me.jessyan.mvparms.demo.mvp.ui.holder.DoctorListHolder;

public class DoctorCommentHolderAdapter extends DefaultAdapter<DoctorCommentBean> {
    public DoctorCommentHolderAdapter(List<DoctorCommentBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<DoctorCommentBean> getHolder(View v, int viewType) {
        return new DoctorCommentHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.doctor_comment_item;
    }
}
