package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerDoctorCommentInfoComponent;
import me.jessyan.mvparms.demo.di.module.DoctorCommentInfoModule;
import me.jessyan.mvparms.demo.mvp.contract.DoctorCommentInfoContract;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.CommentMemberBean;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorCommentBean;
import me.jessyan.mvparms.demo.mvp.presenter.DoctorCommentInfoPresenter;
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

    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.Adapter mAdapter;
    @BindView(R.id.contentList)
    RecyclerView contentList;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.comment)
    EditText comment;
    @BindView(R.id.good)
    View good;
    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean isEnd;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.nextPage();
                }

                @Override
                public boolean isLoading() {
                    return isLoadingMore;
                }

                @Override
                public boolean hasLoadedAllItems() {
                    return isEnd;
                }
            };

            mPaginate = Paginate.with(contentList, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .build();
            mPaginate.setHasMoreDataToLoad(false);
        }
    }

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

        ArmsUtils.configRecyclerView(contentList, mLayoutManager);
        contentList.setAdapter(mAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.requestOrderList();
            }
        });
        initPaginate();
        comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_UP){
                    String s = comment.getText().toString();
                    if(TextUtils.isEmpty(s)){
                        ArmsUtils.makeText(ArmsUtils.getContext(),"请输入评论内容");
                        return true;
                    }

                    mPresenter.replyDoctorComment(s);
                }
                return true;
            }
        });
    }

    private void setData(DoctorCommentBean data) {
        CommentMemberBean member = data.getMember();
        mImageLoader.loadImage(this,
                ImageConfigImpl
                        .builder()
                        .placeholder(R.mipmap.place_holder_user)
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
        good_count.setText(""+data.getPraise());
        Drawable drawable = ArmsUtils.getContext().getResources().getDrawable("1".equals(data.getIsPraise()) ? R.mipmap.small_good_icon : R.mipmap.small_no_good_icon);
        drawable.setBounds(0,0,ArmsUtils.dip2px(this,8),ArmsUtils.dip2px(this,8));
        good_count.setCompoundDrawables(drawable,null,null,null);
        good_count.setText(""+data.getPraise());
        View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("1".equals(data.getIsPraise())) {
                    // 取消评论
                    mPresenter.unlikeDoctorComment(data.getDoctorId(), data.getCommentId());
                } else {
                    // 增加评论
                    mPresenter.likeDoctorComment(data.getDoctorId(), data.getCommentId());
                }
            }
        };
        good_count.setOnClickListener(l);
        good.setOnClickListener(l);

        Drawable drawable2 = ArmsUtils.getContext().getResources().getDrawable("1".equals(data.getIsPraise()) ? R.mipmap.small_good_icon : R.mipmap.small_no_good_icon);
        good.setBackground(drawable2);
    }

    public void updateGoodView(boolean isGood){
        DoctorCommentBean data = (DoctorCommentBean) getIntent().getSerializableExtra(KEY_FOR_DOCTOR_COMMENT_BEAN);
        if(isGood){
            data.setPraise(data.getPraise() + 1);
            data.setIsPraise("1");
        }else{
            data.setPraise(data.getPraise() - 1);
            data.setIsPraise("0");
        }
        good_count.setText(""+data.getPraise());
        Drawable drawable = ArmsUtils.getContext().getResources().getDrawable("1".equals(data.getIsPraise()) ? R.mipmap.small_good_icon : R.mipmap.small_no_good_icon);
        drawable.setBounds(0,0,ArmsUtils.dip2px(this,8),ArmsUtils.dip2px(this,8));
        good_count.setCompoundDrawables(drawable,null,null,null);


        Drawable drawable2 = ArmsUtils.getContext().getResources().getDrawable("1".equals(data.getIsPraise()) ? R.mipmap.small_good_icon : R.mipmap.small_no_good_icon);
        good.setBackground(drawable2);
    }

    @Override
    public void showLoading() {

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

    public Activity getActivity(){
        return this;
    }
    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void startLoadMore() {
        isLoadingMore = true;
    }

    /**
     * 结束加载更多
     */
    @Override
    public void endLoadMore() {
        isLoadingMore = false;
    }
    @Override
    public void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
    }
    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(contentList);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        this.mPaginate = null;
        super.onDestroy();
    }

    public void clear(){
        comment.setText("");
    }

}
