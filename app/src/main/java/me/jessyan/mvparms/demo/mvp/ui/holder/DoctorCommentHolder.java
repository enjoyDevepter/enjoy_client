package me.jessyan.mvparms.demo.mvp.ui.holder;

import android.view.View;
import android.widget.ImageView;
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
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.CommentMemberBean;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorCommentBean;
import me.jessyan.mvparms.demo.mvp.ui.widget.RatingBar;

public class DoctorCommentHolder extends BaseHolder<DoctorCommentBean> {

    @BindView(R.id.head)
    ImageView head;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.rating)
    RatingBar rating;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.view_count)
    TextView view_count;
    @BindView(R.id.comment_count)
    TextView comment_count;
    @BindView(R.id.good_count)
    TextView good_count;

    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;


    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public DoctorCommentHolder(View itemView) {
        super(itemView);
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(DoctorCommentBean data, int position) {
        CommentMemberBean member = data.getMember();
        mImageLoader.loadImage(itemView.getContext(),
                ImageConfigImpl
                        .builder()
                        .url(member.getHeadImage())
                        .imageView(head)
                        .build());
        name.setText(member.getNickName());
        time.setText(simpleDateFormat.format(new Date(data.getCreateDate())));
        rating.setStar(data.getStar());
        content.setText(data.getContent());
        view_count.setText(""+data.getViews());
        comment_count.setText(""+data.getComment());
        good_count.setText(""+data.getPraise());
        good_count.setCompoundDrawables(ArmsUtils.getContext().getResources().getDrawable("1".equals(data.getIsPraise()) ? R.mipmap.small_good_icon : R.mipmap.small_no_good_icon),null,null,null);
    }

    @Override
    protected void onRelease() {
        this.name = null;
        this.head = null;
        this.time = null;
        this.rating = null;
        this.content = null;
        this.view_count = null;
        this.comment_count = null;
        this.good_count = null;
    }
}