package me.jessyan.mvparms.demo.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.MyMemberBean;
import me.jessyan.mvparms.demo.mvp.ui.holder.MyMemberHolder;

public class MyMemberAdapter extends DefaultAdapter<MyMemberBean> {
    public MyMemberAdapter(List<MyMemberBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<MyMemberBean> getHolder(View v, int viewType) {
        return new MyMemberHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.my_member_item;
    }
}

