package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.di.component.DaggerDoctorAllCommentComponent;
import me.jessyan.mvparms.demo.di.module.DoctorAllCommentModule;
import me.jessyan.mvparms.demo.mvp.contract.DoctorAllCommentContract;
import me.jessyan.mvparms.demo.mvp.model.entity.doctor.DoctorCommentBean;
import me.jessyan.mvparms.demo.mvp.presenter.DoctorAllCommentPresenter;

import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DoctorCommentHolderAdapter;


import static com.jess.arms.utils.Preconditions.checkNotNull;


public class DoctorAllCommentActivity extends BaseActivity<DoctorAllCommentPresenter> implements DoctorAllCommentContract.View {

    public static final String KEY_FOR_DOCTOR_ID = "KEY_FOR_DOCTOR_ID";

    @BindView(R.id.back)
    View back;
    @BindView(R.id.title)
    TextView title;

    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.Adapter mAdapter;
    @BindView(R.id.contentList)
    RecyclerView contentList;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean isEnd;

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
        DaggerDoctorAllCommentComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .doctorAllCommentModule(new DoctorAllCommentModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_doctor_all_comment; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMyself();
            }
        });
        title.setText("全部评论");

        ArmsUtils.configRecyclerView(contentList, mLayoutManager);
        contentList.setAdapter(mAdapter);
        ((DoctorCommentHolderAdapter)mAdapter).setOnChildItemClickLinstener(new DoctorCommentHolderAdapter.OnChildItemClickLinstener() {
            @Override
            public void onChildItemClick(View v, DoctorCommentHolderAdapter.ViewName viewname, int position) {
                DoctorCommentBean item = ((DoctorCommentHolderAdapter) mAdapter).getItem(position);
                switch (viewname){
                    case ITEM:
                        Intent intent = new Intent(DoctorAllCommentActivity.this,DoctorCommentInfoActivity.class);
                        intent.putExtra(DoctorCommentInfoActivity.KEY_FOR_DOCTOR_COMMENT_BEAN, item);
                        ArmsUtils.startActivity(intent);
                        break;
                    case LIKE:
                        if("1".equals(item.getIsPraise())){
                            mPresenter.unlikeDoctorComment(item.getDoctorId(), item.getCommentId());
                        }else{
                            mPresenter.likeDoctorComment(item.getDoctorId(), item.getCommentId());
                        }
                        break;
                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.requestOrderList();
            }
        });
        initPaginate();
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public Activity getActivity(){
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

    @Override
    public void killMyself() {
        finish();
    }
}
