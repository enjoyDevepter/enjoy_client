package me.jessyan.mvparms.demo.mvp.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.ChargeBean;

public class ConsumeInputHolder extends BaseHolder<ChargeBean> {

    @BindView(R.id.type)
    TextView typeTV;
    @BindView(R.id.time)
    TextView timeTV;
    @BindView(R.id.order_id)
    TextView orderTV;
    @BindView(R.id.status)
    TextView statusTV;


    public ConsumeInputHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void setData(ChargeBean data, int position) {
        typeTV.setText(data.getTypeDesc() + " " + ArmsUtils.formatLong(data.getMoney()));
        timeTV.setText(data.getCreateDate());
        orderTV.setText("订单号：" + data.getOrderId());
        statusTV.setText(data.getStatusDesc());
    }

    @Override
    protected void onRelease() {
        this.typeTV = null;
        this.timeTV = null;
        this.orderTV = null;
        this.statusTV = null;
    }
}
