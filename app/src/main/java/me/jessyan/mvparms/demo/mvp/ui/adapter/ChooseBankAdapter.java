package me.jessyan.mvparms.demo.mvp.ui.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;

import java.util.List;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.BankCardBean;
import me.jessyan.mvparms.demo.mvp.ui.holder.ChooseBankHolder;

public class ChooseBankAdapter extends DefaultAdapter<BankCardBean> {
    public ChooseBankAdapter(List<BankCardBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<BankCardBean> getHolder(View v, int viewType) {
        return new ChooseBankHolder(v,onChildItemClickLinstener);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.choose_bank_item;
    }

    public void setOnChildItemClickLinstener(OnChildItemClickLinstener onChildItemClickLinstener) {
        this.onChildItemClickLinstener = onChildItemClickLinstener;
    }

    private OnChildItemClickLinstener onChildItemClickLinstener;

    public OnChildItemClickLinstener getOnChildItemClickLinstener() {
        return onChildItemClickLinstener;
    }

    public interface OnChildItemClickLinstener {
        void onChildItemClick(View v, ViewName viewname, int position);
    }


    public enum ViewName {
        DELETE,ITEM
    }
}

