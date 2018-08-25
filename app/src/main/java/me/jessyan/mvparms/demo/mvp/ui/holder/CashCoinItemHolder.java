package me.jessyan.mvparms.demo.mvp.ui.holder;


import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.CashBean;

public class CashCoinItemHolder extends BaseHolder<CashBean> {

    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.fuhao)
    TextView fuhao;
    @BindView(R.id.score_num)
    TextView score_num;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;


    public CashCoinItemHolder(View itemView) {
        super(itemView);
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(CashBean data, int position) {
        type.setText(data.getDesc());
        time.setText(simpleDateFormat.format(new Date(data.getCreateDate())));
        if(data.getInMoney() == 0){
            fuhao.setText("-");
            score_num.setText(String.format("%.0f",data.getOutMoney() / 100));
        }else{
            fuhao.setText("+");
            score_num.setText(String.format("%.0f",data.getInMoney() / 100));
        }

    }

    @Override
    protected void onRelease() {
        this.type = null;
        this.time = null;
        this.fuhao = null;
        this.score_num = null;
    }
}

