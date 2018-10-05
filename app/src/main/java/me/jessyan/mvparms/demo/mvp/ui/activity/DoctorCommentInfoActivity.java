package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Context;
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
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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


public class DoctorCommentInfoActivity extends BaseActivity<DoctorCommentInfoPresenter> implements DoctorCommentInfoContract.View, OnClickListener {

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
    @BindView(R.id.vote)
    View voteV;
    @BindView(R.id.vote_layout)
    View voteLayoutV;
    DoctorCommentBean doctorCommentBean;
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
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                killMyself();
            }
        });
        title.setText("评论详情");
        doctorCommentBean = (DoctorCommentBean) getIntent().getSerializableExtra(KEY_FOR_DOCTOR_COMMENT_BEAN);
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
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEND:
                        String s = comment.getText().toString();
                        if (TextUtils.isEmpty(s)) {
                            ArmsUtils.makeText(ArmsUtils.getContext(), "请输入评论内容");
                            return true;
                        }
                        hintKeyBoard();
                        mPresenter.replyDoctorComment(s);
                        break;
                }
                return false;
            }
        });
        voteLayoutV.setOnClickListener(this);
    }

    public void hintKeyBoard() {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm.isActive() && getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private void setData(DoctorCommentBean data) {
        doctorCommentBean = data;
        CommentMemberBean member = data.getMember();
        mImageLoader.loadImage(this,
                ImageConfigImpl
                        .builder()
                        .placeholder(R.mipmap.place_holder_user)
                        .url(member.getHeadImage())
                        .isCenterCrop(true)
                        .imageView(head)
                        .build());
        name.setText(member.getNickName());
        time.setText(simpleDateFormat.format(new Date(data.getCreateDate())));
        rating.setStar(data.getStar());
        content.setText(data.getContent());
        view_count.setText("" + data.getViews());
        comment_count.setText("" + data.getComment());
        good_count.setText("" + data.getPraise());
        good_count.setText("" + data.getPraise());
        Drawable drawable = ArmsUtils.getContext().getResources().getDrawable("1".equals(data.getIsPraise()) ? R.mipmap.small_good_icon : R.mipmap.small_no_good_icon);
        drawable.setBounds(0, 0, ArmsUtils.dip2px(this, 8), ArmsUtils.dip2px(this, 8));
        good_count.setCompoundDrawables(drawable, null, null, null);
        good_count.setText("" + data.getPraise());
        good_count.setOnClickListener(this);
        voteV.setSelected("1".equals(data.getIsPraise()) ? true : false);
    }

    public void updateGoodView(boolean isGood) {
        DoctorCommentBean data = (DoctorCommentBean) getIntent().getSerializableExtra(KEY_FOR_DOCTOR_COMMENT_BEAN);
        if (isGood) {
            data.setPraise(data.getPraise() + 1);
            data.setIsPraise("1");
        } else {
            data.setPraise(data.getPraise() - 1);
            data.setIsPraise("0");
        }
        voteV.setSelected(isGood);
        good_count.setText("" + data.getPraise());
        Drawable drawable = ArmsUtils.getContext().getResources().getDrawable("1".equals(data.getIsPraise()) ? R.mipmap.small_good_icon : R.mipmap.small_no_good_icon);
        drawable.setBounds(0, 0, ArmsUtils.dip2px(this, 8), ArmsUtils.dip2px(this, 8));
        good_count.setCompoundDrawables(drawable, null, null, null);
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

    public Activity getActivity() {
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

    public void clear() {
        comment.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
            case R.id.vote_layout:
            case R.id.good_count:
                if (voteV.isSelected()) {
                    // 取消评论
                    mPresenter.unlikeDoctorComment(doctorCommentBean.getDoctorId(), doctorCommentBean.getCommentId());
                } else {
                    // 增加评论
                    mPresenter.likeDoctorComment(doctorCommentBean.getDoctorId(), doctorCommentBean.getCommentId());
                }
                break;
        }
    }
}
