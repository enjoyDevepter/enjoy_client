package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerDoctorMainComponent;
import me.jessyan.mvparms.demo.di.module.DoctorMainModule;
import me.jessyan.mvparms.demo.mvp.contract.DoctorMainContract;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorBean;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.bean.DoctorSkill;
import me.jessyan.mvparms.demo.mvp.presenter.DoctorMainPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DoctorCommentHolderAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DoctorSkillAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.RatingBar;
import me.jessyan.mvparms.demo.mvp.ui.widget.ShapeImageView;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class DoctorMainActivity extends BaseActivity<DoctorMainPresenter> implements DoctorMainContract.View, TextView.OnEditorActionListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, DoctorCommentHolderAdapter.OnChildItemClickLinstener {

    public static final String KEY_FOR_DOCTOR_ID = "key_for_doctor_id";

    public static final String LIKE = "1";
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    View back;
    @BindView(R.id.project)
    TextView project;
    @BindView(R.id.head_image)
    ShapeImageView head_image;
    @BindView(R.id.doctor_name)
    TextView doctor_name;
    @BindView(R.id.comment_count)
    TextView comment_count;
    @BindView(R.id.rating)
    RatingBar rating;
    @BindView(R.id.hit_good)
    View hit_good;
    boolean isLike;
    @BindView(R.id.hosp_info)
    TextView hosp_info;
    @BindView(R.id.more)
    View moreV;
    @BindView(R.id.skill_list)
    TagFlowLayout skill_list;
    @BindView(R.id.doctor_intro)
    View doctor_intro;
    @BindView(R.id.doctor_paper)
    View doctor_paper;
    @BindView(R.id.doctor_honor)
    View doctor_honor;
    @BindView(R.id.comment_commit_btn)
    View comment_commit_btn;
    @BindView(R.id.comment_edit)
    EditText comment_edit;
    @BindView(R.id.comment_star)
    RatingBar comment_star;
    @BindView(R.id.all_comment)
    TextView all_comment;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    DoctorCommentHolderAdapter mAdapter;
    @BindView(R.id.contentList)
    RecyclerView contentList;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private String doctorId;
    private DoctorSkillAdapter doctorSkillAdapter;
    private DoctorSkill currDoctorSkill;
    private PopupWindow popupWindow;
    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean isEnd;

    private DoctorBean doctorBean;

    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerDoctorMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .doctorMainModule(new DoctorMainModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_doctor_main; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        doctorId = getIntent().getStringExtra(KEY_FOR_DOCTOR_ID);
        title.setText("医生主页");
        moreV.setOnClickListener(this);
        all_comment.setOnClickListener(this);
        back.setOnClickListener(this);
        hit_good.setOnClickListener(this);
        doctor_paper.setOnClickListener(this);
        doctor_honor.setOnClickListener(this);
        comment_commit_btn.setOnClickListener(this);
        comment_edit.setOnEditorActionListener(this);
        ArmsUtils.configRecyclerView(contentList, mLayoutManager);
        contentList.setAdapter(mAdapter);
        mAdapter.setOnChildItemClickLinstener(this);
        contentList.setNestedScrollingEnabled(false);
        swipeRefreshLayout.setOnRefreshListener(this);
        initPaginate();
    }

    @Override
    public void showLoading() {

    }

    public void updateRecyclerViewHeight() {
        RecyclerView.Adapter adapter = contentList.getAdapter();
        if (adapter.getItemCount() == 0) {
            all_comment.setVisibility(View.GONE);
            return;
        }
        ViewGroup.LayoutParams layoutParams = swipeRefreshLayout.getLayoutParams();
        layoutParams.height = ArmsUtils.getDimens(this, R.dimen.doctor_tab_height) * (adapter.getItemCount() < 10 ? adapter.getItemCount() : 10);
        swipeRefreshLayout.setLayoutParams(layoutParams);
    }

    public void updateDoctorInfo(DoctorBean doctorBean) {
        this.doctorBean = doctorBean;
        mImageLoader.loadImage(this,
                ImageConfigImpl
                        .builder()
                        .placeholder(R.mipmap.place_holder_user)
                        .url(doctorBean.getHeadImage())
                        .imageView(head_image)
                        .isCenterCrop(true)
                        .build());
        doctor_name.setText(doctorBean.getName());
        comment_count.setText("" + doctorBean.getComment());
        rating.setStar(doctorBean.getStar());
        updateLikeImage(LIKE.equals(doctorBean.getIsFollow()));
        if (null != doctorBean.getHospitalList() && doctorBean.getHospitalList().size() > 0) {
            hosp_info.setText(doctorBean.getHospitalList().get(0).getName());
        } else {
            hosp_info.setText("");
        }
        List<DoctorSkill> doctorSkillList = doctorBean.getDoctorSkillList();
        doctorSkillAdapter = new DoctorSkillAdapter(doctorSkillList);
        TagAdapter<DoctorSkill> adapter = new TagAdapter<DoctorSkill>(new ArrayList<>(doctorSkillList)) {
            @Override
            public View getView(FlowLayout parent, int position, DoctorSkill s) {
                TextView tv = (TextView) LayoutInflater.from(ArmsUtils.getContext()).inflate(R.layout.doctor_main_skill_item, null, false);
                tv.setText(s.getProjectName());
                tv.setTextColor(Color.BLACK);
                return tv;
            }
        };
        skill_list.setAdapter(adapter);
        skill_list.setMaxSelectCount(0);

        doctor_intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DoctorMainActivity.this, DoctorIntorActivity.class);
                intent.putExtra("doctorId", doctorBean.getDoctorId());
                ArmsUtils.startActivity(intent);
            }
        });
        if (doctorSkillList != null && doctorSkillList.size() != 0) {
            setCurrDoctorSkill(doctorSkillList.get(0));
            project.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showType();
                }
            });
        }
    }

    private void setCurrDoctorSkill(DoctorSkill doctorSkill) {
        currDoctorSkill = doctorSkill;
        project.setText(currDoctorSkill.getProjectName());
    }

    @Override
    public void updateLikeImage(boolean isLike) {
        this.isLike = isLike;
        hit_good.setBackground(getResources().getDrawable(isLike ? R.mipmap.doctor_main_followed_doctor : R.mipmap.doctor_main_follow_doctor));
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

    private void showType() {
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }

        if (doctorSkillAdapter == null) {
            return;
        }

        RecyclerView recyclerView = new RecyclerView(this);
        ArmsUtils.configRecyclerView(recyclerView, new LinearLayoutManager(this));
        recyclerView.setAdapter(doctorSkillAdapter);
        doctorSkillAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                setCurrDoctorSkill((DoctorSkill) data);
                if (popupWindow != null) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
            }
        });
        popupWindow = new PopupWindow(recyclerView, ArmsUtils.getDimens(this, R.dimen.code_width), LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//设置背景
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(project, 0, -ArmsUtils.getDimens(this, R.dimen.code_height));
    }

    public void commentOk() {
        comment_edit.setText("");
        ArmsUtils.makeText(this, "提交评论成功");
    }

    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.nextDoctorHotComment();
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
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    public Activity getActivity() {
        return this;
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
        super.onDestroy();
        this.mPaginate = null;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_SEND:
                String comment = comment_edit.getText().toString();
                if (ArmsUtils.isEmpty(comment)) {
                    showMessage("请输入评论内容");
                    return true;
                }
                hintKeyBoard();
                mPresenter.commentDoctor(doctorId, comment, comment_star.getStar(), currDoctorSkill.getProjectId());
                return true;
        }
        return false;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
            case R.id.more:
                Intent hosiptals = new Intent(getApplicationContext(), DoctorForHospitalActivity.class);
                hosiptals.putParcelableArrayListExtra("hospitals", (ArrayList<? extends Parcelable>) doctorBean.getHospitalList());
                ArmsUtils.startActivity(hosiptals);
                break;
            case R.id.all_comment:
                Intent intent = new Intent(DoctorMainActivity.this, DoctorAllCommentActivity.class);
                intent.putExtra(DoctorAllCommentActivity.KEY_FOR_DOCTOR_ID, doctorId);
                ArmsUtils.startActivity(intent);
                break;
            case R.id.hit_good:
                if (isLike) {
                    mPresenter.unlikeDoctor(doctorId);
                } else {
                    mPresenter.likeDoctor(doctorId);
                }
                break;
            case R.id.doctor_paper:
                Intent doctorIntent = new Intent(DoctorMainActivity.this, DoctorPaperActivity.class);
                doctorIntent.putExtra(DoctorPaperActivity.KEY_FOR_DOCTOR_ID, doctorId);
                ArmsUtils.startActivity(doctorIntent);
                break;
            case R.id.doctor_honor:
                Intent honorIntent = new Intent(DoctorMainActivity.this, DoctorHonorActivity.class);
                honorIntent.putExtra(DoctorHonorActivity.KEY_FOR_DOCTOR_ID, doctorId);
                ArmsUtils.startActivity(honorIntent);
                break;
            case R.id.comment_commit_btn:
                String str = comment_edit.getText().toString();
                if (TextUtils.isEmpty(str)) {
                    ArmsUtils.makeText(ArmsUtils.getContext(), "请输入评论内容");
                    return;
                }
                mPresenter.commentDoctor(doctorId, str, comment_star.getStar(), currDoctorSkill.getProjectId());
                break;
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.requestDoctorHotComment();
    }

    @Override
    public void onChildItemClick(View v, DoctorCommentHolderAdapter.ViewName viewname, int position) {
        switch (viewname) {
            case ITEM:
                Intent intent = new Intent(DoctorMainActivity.this, DoctorCommentInfoActivity.class);
                intent.putExtra(DoctorCommentInfoActivity.KEY_FOR_DOCTOR_COMMENT_BEAN, mAdapter.getItem(position));
                ArmsUtils.startActivity(intent);
                break;
            case LIKE:
                if ("1".equals(mAdapter.getItem(position).getIsPraise())) {
                    mPresenter.unlikeDoctorComment(doctorId, mAdapter.getItem(position).getCommentId());
                } else {
                    mPresenter.likeDoctorComment(doctorId, mAdapter.getItem(position).getCommentId());
                }
                break;
        }
    }
}
