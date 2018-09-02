package me.jessyan.mvparms.demo.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.BalanceBean;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.ChargeBean;
import me.jessyan.mvparms.demo.mvp.ui.holder.ConsumeCoinHolder;
import me.jessyan.mvparms.demo.mvp.ui.holder.ConsumeInputHolder;

public class ConsumeInputAdapter extends DefaultAdapter<ChargeBean> {
    public ConsumeInputAdapter(List<ChargeBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<ChargeBean> getHolder(View v, int viewType) {
        return new ConsumeInputHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.consume_input_item;
    }
}

