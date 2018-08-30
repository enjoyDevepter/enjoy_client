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
import me.jessyan.mvparms.demo.mvp.ui.adapter.ChooseBankAdapter;

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

    @BindView(R.id.parent)
    View parent;


    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;
    private List<BankBean> bankBeanList;

    private ChooseBankAdapter.OnChildItemClickLinstener onChildItemClickLinstener;


    public ChooseBankHolder(View itemView, ChooseBankAdapter.OnChildItemClickLinstener onChildItemClickLinstener) {
        super(itemView);
        this.onChildItemClickLinstener = onChildItemClickLinstener;
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(BankCardBean data, int position) {
        mImageLoader.loadImage(itemView.getContext(),
                ImageConfigImpl
                        .builder()
                        .url(data.getImage())
                        .imageView(image)
                        .build());

        name.setText(data.getBankName());
        String cardNo = data.getCardNo();
        card_num.setText(cardNo.substring(cardNo.length() - 4));
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChildItemClickLinstener.onChildItemClick(parent, ChooseBankAdapter.ViewName.ITEM,position);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChildItemClickLinstener.onChildItemClick(delete, ChooseBankAdapter.ViewName.DELETE,position);
            }
        });
    }

    @Override
    protected void onRelease() {
        this.image = null;
        this.name = null;
        this.card_num = null;
        this.delete = null;
        this.parent = null;
    }
}

