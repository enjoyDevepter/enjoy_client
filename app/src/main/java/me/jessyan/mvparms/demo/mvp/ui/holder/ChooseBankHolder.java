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
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.BankCardBean;

public class ChooseBankHolder extends BaseHolder<BankCardBean> {

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.card_num)
    TextView card_num;
    @BindView(R.id.delete)
    TextView delete;


    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;


    public ChooseBankHolder(View itemView) {
        super(itemView);
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(BankCardBean data, int position) {

//        mImageLoader.loadImage(itemView.getContext(),
//                ImageConfigImpl
//                        .builder()
//                        .url()
//                        .imageView(image)
//                        .build());

        name.setText(data.getBankName());
        String cardNo = data.getCardNo();
        card_num.setText(cardNo.substring(cardNo.length() - 4));
    }

    @Override
    protected void onRelease() {
        this.image = null;
        this.name = null;
        this.card_num = null;
        this.delete = null;
    }
}

