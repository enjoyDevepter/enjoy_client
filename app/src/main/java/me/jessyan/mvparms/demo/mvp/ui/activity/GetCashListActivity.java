package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.di.component.DaggerGetCashListComponent;
import me.jessyan.mvparms.demo.di.module.GetCashListModule;
import me.jessyan.mvparms.demo.mvp.contract.GetCashListContract;
import me.jessyan.mvparms.demo.mvp.presenter.GetCashListPresenter;

import me.jessyan.mvparms.demo.R;


import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class GetCashListActivity extends BaseActivity<GetCashListPresenter> implements GetCashListContract.View {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    View back;

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
    @BindView(R.id.no_date)
    View onDateV;

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
        DaggerGetCashListComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .getCashListModule(new GetCashListModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_get_cash_list; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    private class DividerDrawable extends Drawable{

        @Override
        public int getIntrinsicHeight() {
            return ArmsUtils.dip2px(ArmsUtils.getContext(),3);
        }

        @Override
        public void draw(@NonNull Canvas canvas) {
            canvas.drawColor(Color.parseColor("#EEEEEE"));
        }

        @Override
        public void setAlpha(int alpha) {

        }

        @Override
        public void setColorFilter(@Nullable ColorFilter colorFilter) {

        }

        @Override
        public int getOpacity() {
            return 0;
        }
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        ArmsUtils.configRecyclerView(contentList, mLayoutManager);
        DividerItemDecoration decor = new DividerItemDecoration(this, LinearLayout.VISIBLE);
        decor.setDrawable(new DividerDrawable());
        contentList.addItemDecoration(decor);
        contentList.setAdapter(mAdapter);
        title.setText("提现记录");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                killMyself();
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
    public void showLoading() {

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
    public void showError(boolean hasDate) {
        onDateV.setVisibility(hasDate ? INVISIBLE : VISIBLE);
        swipeRefreshLayout.setVisibility(hasDate ? VISIBLE : INVISIBLE);
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
