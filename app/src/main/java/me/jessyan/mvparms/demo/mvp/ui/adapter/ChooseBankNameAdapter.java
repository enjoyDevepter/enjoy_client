package me.jessyan.mvparms.demo.mvp.ui.adapter;

import com.bigkoo.pickerview.adapter.WheelAdapter;

import java.util.List;

import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.BankBean;

public class ChooseBankNameAdapter implements WheelAdapter {

    private List<BankBean> bankBeanList;

    public ChooseBankNameAdapter(List<BankBean> bankBeanList){
        this.bankBeanList = bankBeanList;
    }

    @Override
    public int getItemsCount() {
        return bankBeanList.size();
    }

    @Override
    public String getItem(int i) {
        return bankBeanList.get(i).getName();
    }

    @Override
    public int indexOf(Object o) {
        return bankBeanList.indexOf(o);
    }
}
