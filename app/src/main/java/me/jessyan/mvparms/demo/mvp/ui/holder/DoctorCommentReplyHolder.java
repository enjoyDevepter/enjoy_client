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
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.CommentMemberBean;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorCommentReplyBean;
import me.jessyan.mvparms.demo.mvp.ui.widget.ShapeImageView;

public class DoctorCommentReplyHolder extends BaseHolder<DoctorCommentReplyBean> {


    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;

    @BindView(R.id.image)
    ShapeImageView image;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.time)
    TextView time;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public DoctorCommentReplyHolder(View itemView) {
        super(itemView);
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(DoctorCommentReplyBean data, int position) {

        CommentMemberBean member = data.getMember();
        if(member != null){
            mImageLoader.loadImage(itemView.getContext(),
                    ImageConfigImpl
                            .builder()
                            .url(member.getHeadImage())
                            .imageView(image)
                            .build());
            name.setText(member.getNickName());
        }
        content.setText(data.getContent());
        time.setText(simpleDateFormat.format(new Date(data.getCreateDate())));
    }

    @Override
    protected void onRelease() {
        this.image = null;
        this.name = null;
        this.content = null;
        this.time = null;
    }
}
