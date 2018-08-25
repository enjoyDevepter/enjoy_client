package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.di.component.DaggerDoctorCommentInfoComponent;
import me.jessyan.mvparms.demo.di.module.DoctorCommentInfoModule;
import me.jessyan.mvparms.demo.mvp.contract.DoctorCommentInfoContract;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.CommentMemberBean;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorCommentBean;
import me.jessyan.mvparms.demo.mvp.presenter.DoctorCommentInfoPresenter;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.ui.widget.RatingBar;


import static com.jess.arms.utils.Preconditions.checkNotNull;


public class DoctorCommentInfoActivity extends BaseActivity<DoctorCommentInfoPresenter> implements DoctorCommentInfoContract.View {

    public static final String KEY_FOR_DOCTOR_COMMENT_BEAN = "KEY_FOR_DOCTOR_COMMENT_BEAN";

    @BindView(R.id.back)
    View back;
    @BindView(R.id.title)
    TextView title;


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

    @Inject
    ImageLoader mImageLoader;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerDoctorCommentInfoComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .doctorCommentInfoModule(new DoctorCommentInfoModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_doctor_comment_info; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMyself();
            }
        });
        title.setText("评论详情");

        DoctorCommentBean doctorCommentBean = (DoctorCommentBean) getIntent().getSerializableExtra(KEY_FOR_DOCTOR_COMMENT_BEAN);
        setData(doctorCommentBean);
    }


    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private void setData(DoctorCommentBean data) {
        CommentMemberBean member = data.getMember();
        mImageLoader.loadImage(this,
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
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }
}
