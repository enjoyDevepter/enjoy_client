package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;

import org.simple.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.di.component.DaggerMyDiaryComponent;
import me.jessyan.mvparms.demo.di.module.MyDiaryModule;
import me.jessyan.mvparms.demo.mvp.contract.MyDiaryContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Diary;
import me.jessyan.mvparms.demo.mvp.presenter.MyDiaryPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DiaryListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.CustomDialog;
import me.jessyan.mvparms.demo.mvp.ui.widget.SpacesItemDecoration;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MyDiaryActivity extends BaseActivity<MyDiaryPresenter> implements MyDiaryContract.View, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, DiaryListAdapter.OnChildItemClickLinstener {

    @BindView(R.id.back)
    View backV;
    @BindView(R.id.apply)
    View applyV;
    @BindView(R.id.release)
    View releaseV;
    @BindView(R.id.tab)
    TabLayout tabLayout;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.diaryRV)
    RecyclerView mRecyclerView;
    @BindView(R.id.noData)
    View noDataV;
    @BindView(R.id.diary_info)
    View diaryInfoV;
    @BindView(R.id.no_diary)
    View noDiaryV;
    @BindView(R.id.rule_info)
    TextView ruleTV;
    @BindView(R.id.confirm)
    View confirmV;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    DiaryListAdapter mAdapter;
    CustomDialog dialog = null;

    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
        }
    };

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerMyDiaryComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .myDiaryModule(new MyDiaryModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_my_diary; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        applyV.setOnClickListener(this);
        releaseV.setOnClickListener(this);
        confirmV.setOnClickListener(this);
        boolean hasData = getIntent().getIntExtra("projectNum", 0) == 0 ? false : true;
        diaryInfoV.setVisibility(hasData ? View.VISIBLE : View.GONE);
        noDiaryV.setVisibility(hasData ? View.GONE : View.VISIBLE);
        if (!hasData) {
            List<String> rules = getIntent().getStringArrayListExtra("rules");
            StringBuilder sb = new StringBuilder();
            if (null != rules) {
                for (String rule : rules) {
                    sb.append(rule).append("\n");
                }
            }
            ruleTV.setText(sb.toString());
        } else {
            swipeRefreshLayout.setOnRefreshListener(this);
            tabLayout.addTab(tabLayout.newTab().setText("我的日记"));
            ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
            mRecyclerView.addItemDecoration(new SpacesItemDecoration(0, ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.address_list_item_space)));
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnChildItemClickLinstener(this);
            initPaginate();
        }
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideLoading() {
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 开始加载更多
     */
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
    public void setLoadedAllItems(boolean has) {
        this.hasLoadedAllItems = has;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Cache getCache() {
        return provideCache();
    }


    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.getMyDiaryList(false);
                }

                @Override
                public boolean isLoading() {
                    return isLoadingMore;
                }

                @Override
                public boolean hasLoadedAllItems() {
                    return hasLoadedAllItems;
                }
            };

            mPaginate = Paginate.with(mRecyclerView, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .build();
            mPaginate.setHasMoreDataToLoad(false);
        }
    }


    @Override
    public void onRefresh() {
        mPresenter.getMyDiaryList(true);
    }

    @Override
    public void showError(boolean nodata) {
        noDataV.setVisibility(nodata ? View.VISIBLE : View.GONE);
        swipeRefreshLayout.setVisibility(nodata ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showApply(String content) {
        showDailog(content);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
            case R.id.apply:
                Intent articleIntent = new Intent(getApplication(), PlatformActivity.class);
                articleIntent.putExtra("url", getIntent().getStringExtra("url"));
                ArmsUtils.startActivity(articleIntent);
                break;
            case R.id.release:
                ArmsUtils.startActivity(ReleaseDiaryActivity.class);
                break;
            case R.id.confirm:
                EventBus.getDefault().post(3, EventBusTags.CHANGE_MAIN_ITEM);
                killMyself();
                break;
        }
    }

    private void showDailog(String text) {
        dialog = CustomDialog.create(getSupportFragmentManager())
                .setViewListener(new CustomDialog.ViewListener() {
                    @Override
                    public void bindView(View view) {
                        ((TextView) view.findViewById(R.id.content)).setText(text);
                        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPresenter.apply(false);
                                dialog.dismissAllowingStateLoss();
                            }
                        });
                    }
                })
                .setLayoutRes(R.layout.diary_for_apply_benefit)
                .setDimAmount(0.5f)
                .isCenter(true)
                .setWidth(ArmsUtils.getDimens(this, R.dimen.diray_apply_width))
                .setHeight(ArmsUtils.getDimens(this, R.dimen.diray_apply_height))
                .show();
    }


    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(mRecyclerView);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
    }

    @Override
    public void onChildItemClick(View v, DiaryListAdapter.ViewName viewname, int position) {

        Diary diary = mAdapter.getInfos().get(position);
        switch (viewname) {
            case FLLOW:
                provideCache().put("memberId", diary.getMember().getMemberId());
                mPresenter.follow("1".equals(diary.getMember().getIsFollow()) ? false : true, position);
                break;
            case VOTE:
                provideCache().put("diaryId", diary.getDiaryId());
                mPresenter.vote("1".equals(diary.getIsPraise()) ? false : true, position);
                break;
            case LEFT_IMAGE:
                if (diary.getImageList() != null && diary.getImageList().size() > 0) {
                    Intent intent = new Intent(getActivity(), ImageShowActivity.class);
                    String[] images = new String[diary.getImageList().size()];
                    for (int i = 0; i < diary.getImageList().size(); i++) {
                        images[i] = diary.getImageList().get(i);
                    }
                    intent.putExtra("images", images);
                    ArmsUtils.startActivity(intent);
                }
                break;
            case RIGHT_IMAGE:
                if (diary.getImageList() != null && diary.getImageList().size() > 1) {
                    Intent intent = new Intent(getActivity(), ImageShowActivity.class);
                    String[] images = new String[diary.getImageList().size()];
                    for (int i = 0; i < diary.getImageList().size(); i++) {
                        images[i] = diary.getImageList().get(i);
                    }
                    intent.putExtra("images", images);
                    ArmsUtils.startActivity(intent);
                }
                break;
            case ITEM:
                Intent intent = new Intent(getActivity().getApplication(), DiaryForGoodsActivity.class);
                intent.putExtra("diaryId", diary.getDiaryId());
                intent.putExtra("goodsId", diary.getGoods().getGoodsId());
                intent.putExtra("merchId", diary.getGoods().getMerchId());
                intent.putExtra("memberId", diary.getMember().getMemberId());
                ArmsUtils.startActivity(intent);
                break;
            case COMMENT:
                Intent diaryIntent = new Intent(getActivity().getApplication(), DiaryDetailsActivity.class);
                diaryIntent.putExtra("diaryId", diary.getDiaryId());
                ArmsUtils.startActivity(diaryIntent);
                break;
            case SHARE:
                showWX(diary);
                break;
        }
    }

    private void showWX(Diary diary) {
        if (null != diary) {
            Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(getActivity()).extras();
            if (cache.get(KEY_KEEP + "token") == null) {
                ArmsUtils.startActivity(LoginActivity.class);
                return;
            }
            UMWeb web = new UMWeb(diary.getShareUrl());
            web.setTitle(diary.getShareTitle());//标题
            web.setDescription(diary.getShareDesc());
            new ShareAction(getActivity())
                    .withMedia(web)
                    .setCallback(shareListener)
                    .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                    .open();

        }
    }

}
