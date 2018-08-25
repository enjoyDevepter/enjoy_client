package me.jessyan.mvparms.demo.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorCommentReplyBean;
import me.jessyan.mvparms.demo.mvp.ui.holder.DoctorCommentReplyHolder;

public class DoctorCommentReplyAdapter extends DefaultAdapter<DoctorCommentReplyBean> {
    public DoctorCommentReplyAdapter(List<DoctorCommentReplyBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<DoctorCommentReplyBean> getHolder(View v, int viewType) {
        return new DoctorCommentReplyHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.doctor_comment_reply_item;
    }
}
