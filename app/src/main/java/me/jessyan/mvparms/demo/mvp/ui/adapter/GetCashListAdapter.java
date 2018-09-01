package me.jessyan.mvparms.demo.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.DrawCashBean;
import me.jessyan.mvparms.demo.mvp.ui.holder.GetCaseListHolder;

public class GetCashListAdapter extends DefaultAdapter<DrawCashBean> {
    public GetCashListAdapter(List<DrawCashBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<DrawCashBean> getHolder(View v, int viewType) {
        return new GetCaseListHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.get_case_list_item;
    }
}

