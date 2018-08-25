package me.jessyan.mvparms.demo.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.BalanceBean;
import me.jessyan.mvparms.demo.mvp.ui.holder.ConsumeCoinHolder;

public class ConsumeCoinAdapter extends DefaultAdapter<BalanceBean> {
    public ConsumeCoinAdapter(List<BalanceBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<BalanceBean> getHolder(View v, int viewType) {
        return new ConsumeCoinHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.consume_info_item;
    }
}

