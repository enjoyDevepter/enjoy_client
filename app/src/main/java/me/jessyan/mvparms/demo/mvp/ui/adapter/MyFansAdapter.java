package me.jessyan.mvparms.demo.mvp.ui.adapter;


import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.FansMember;
import me.jessyan.mvparms.demo.mvp.ui.holder.MyFansHolder;

public class MyFansAdapter extends DefaultAdapter<FansMember> {
    public MyFansAdapter(List<FansMember> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<FansMember> getHolder(View v, int viewType) {
        return new MyFansHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.fans_item;
    }
}
