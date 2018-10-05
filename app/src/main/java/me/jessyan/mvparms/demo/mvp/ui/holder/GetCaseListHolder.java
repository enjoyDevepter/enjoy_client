package me.jessyan.mvparms.demo.mvp.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.DrawCashBean;

public class GetCaseListHolder extends BaseHolder<DrawCashBean> {


    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.card_num)
    TextView card_num;  // 尾号
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.time)
    TextView time;  // 2018-02-12
    @BindView(R.id.state)
    TextView state;
    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;



    public GetCaseListHolder(View itemView) {
        super(itemView);
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(DrawCashBean data, int position) {

        mImageLoader.loadImage(itemView.getContext(),
                ImageConfigImpl
                        .builder()
                        .url(data.getImage())
                        .imageView(image)
                        .isCenterCrop(true)
                        .build());
        name.setText(data.getBankName());
        String cardNo = data.getCardNo();
        card_num.setText(cardNo.substring(cardNo.length() - 4));
        money.setText(String.format("%.2f",data.getMoney() * 1.0 / 100));
        time.setText(data.getCreateDate());
        state.setText(data.getStatusDesc());
    }

    @Override
    protected void onRelease() {
        this.image = null;
        this.name = null;
        this.card_num = null;
        this.money = null;
        this.time = null;
        this.state = null;
    }
}
