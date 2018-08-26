package me.jessyan.mvparms.demo.mvp.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;

import java.util.List;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.model.ChooseBankModel;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.BankBean;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.BankCardBean;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;

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
    private List<BankBean> bankBeanList;


    public ChooseBankHolder(View itemView) {
        super(itemView);
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
        Cache<String,Object> cache= ArmsUtils.obtainAppComponentFromContext(ArmsUtils.getContext()).extras();
        bankBeanList = (List<BankBean>) cache.get(KEY_KEEP+ ChooseBankModel.KEY_FOR_BANK_LIST);
    }

    @Override
    public void setData(BankCardBean data, int position) {
        String bankName = data.getBankName();
        BankBean bankBean = null;
        for(BankBean bb : bankBeanList){
            if (bb.getName().equals(bankName)) {
                bankBean = bb;
                break;
            }
        }

        mImageLoader.loadImage(itemView.getContext(),
                ImageConfigImpl
                        .builder()
                        .url(bankBean.getImage())
                        .imageView(image)
                        .build());

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

