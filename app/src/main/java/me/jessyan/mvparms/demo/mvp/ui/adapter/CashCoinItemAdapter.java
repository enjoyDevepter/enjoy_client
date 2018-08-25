package me.jessyan.mvparms.demo.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.CashBean;
import me.jessyan.mvparms.demo.mvp.ui.holder.CashCoinItemHolder;

public class CashCoinItemAdapter extends DefaultAdapter<CashBean> {
    public CashCoinItemAdapter(List<CashBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<CashBean> getHolder(View v, int viewType) {
        return new CashCoinItemHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.cash_coin_info_item;
    }
}

