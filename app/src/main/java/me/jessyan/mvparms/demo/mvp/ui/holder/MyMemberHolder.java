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
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.MyMemberBean;

public class MyMemberHolder extends BaseHolder<MyMemberBean> {


    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.time)
    TextView time;


    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;


    public MyMemberHolder(View itemView) {
        super(itemView);
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(MyMemberBean data, int position) {

        mImageLoader.loadImage(itemView.getContext(),
                ImageConfigImpl
                        .builder()
                        .placeholder(R.mipmap.place_holder_user)
                        .url(data.getHeadImage())
                        .imageView(image)
                        .build());
        name.setText(data.getUserName());
        time.setText(data.getRegTime());
    }

    @Override
    protected void onRelease() {
        this.image = null;
        this.name = null;
        this.time = null;
    }
}


