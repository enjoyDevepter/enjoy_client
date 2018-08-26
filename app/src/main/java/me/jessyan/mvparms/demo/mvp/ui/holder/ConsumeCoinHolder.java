package me.jessyan.mvparms.demo.mvp.ui.holder;

import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.BalanceBean;

public class ConsumeCoinHolder extends BaseHolder<BalanceBean> {

    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.fuhao)
    TextView fuhao;
    @BindView(R.id.score_num)
    TextView score_num;

    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;


    public ConsumeCoinHolder(View itemView) {
        super(itemView);
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(BalanceBean data, int position) {
        time.setText(data.getCreateDate());
        type.setText(data.getDesc());
        if(data.getInMoney() == 0){
            fuhao.setText("-");
            score_num.setText(String.format("%.2f",data.getOutMoney() * 1.0 / 100));
        }else{
            fuhao.setText("+");
            score_num.setText(String.format("%.2f",data.getInMoney() * 1.0 / 100));
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
