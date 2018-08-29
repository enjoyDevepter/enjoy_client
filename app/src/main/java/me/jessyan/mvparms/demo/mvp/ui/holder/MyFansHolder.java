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
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.FansMember;

public class MyFansHolder extends BaseHolder<FansMember> {

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.good)
    View good;
    @BindView(R.id.time)
    TextView time;

    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;


    public MyFansHolder(View itemView) {
        super(itemView);
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(FansMember data, int position) {

        mImageLoader.loadImage(itemView.getContext(),
                ImageConfigImpl
                        .builder()
                        .url(data.getHeadImage())
                        .imageView(image)
                        .build());
        name.setText(data.getNickName());
        time.setText(data.getFollowDate());
        follow("1".equals(data.getIsFollow()));
    }

    private boolean isFollowed = false;
    private void follow(boolean isFollowed){
        this.isFollowed = isFollowed;
        if(isFollowed){
            good.setBackground(ArmsUtils.getContext().getResources().getDrawable(R.mipmap.doctor_main_followed_doctor));
        }else{
            good.setBackground(ArmsUtils.getContext().getResources().getDrawable(R.mipmap.doctor_main_follow_doctor));

        }
    }

    @Override
    protected void onRelease() {
        this.image = null;
        this.name = null;
        this.good = null;
        this.time = null;
    }
}

