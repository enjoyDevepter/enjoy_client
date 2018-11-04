package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerMyMealDetailsComponent;
import me.jessyan.mvparms.demo.di.module.MyMealDetailsModule;
import me.jessyan.mvparms.demo.mvp.contract.MyMealDetailsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.appointment.Appointment;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.Share;
import me.jessyan.mvparms.demo.mvp.presenter.MyMealDetailsPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyMealDetailListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.CustomDialog;
import me.jessyan.mvparms.demo.mvp.ui.widget.SpacesItemDecoration;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MyMealDetailsActivity extends BaseActivity<MyMealDetailsPresenter> implements MyMealDetailsContract.View, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, MyMealDetailListAdapter.OnChildItemClickLinstener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.content)
    RecyclerView mRecyclerView;
    @BindView(R.id.no_date)
    View onDate;
    @BindView(R.id.name)
    TextView nameTV;
    @BindView(R.id.orderId)
    TextView orderIdTV;
    @BindView(R.id.desc)
    TextView descTV;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    MyMealDetailListAdapter mAdapter;
    CustomDialog dialog = null;
    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;

    private SelfPickupAddrListActivity.ListType listType = SelfPickupAddrListActivity.ListType.HOP;

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
        DaggerMyMealDetailsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .myMealDetailsModule(new MyMealDetailsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_my_meal_details; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        titleTV.setText("我的套餐");
        swipeRefreshLayout.setOnRefreshListener(this);
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnChildItemClickLinstener(this);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(0, ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.address_list_item_space)));
        initPaginate();

        nameTV.setText(getIntent().getStringExtra("mealName"));
        orderIdTV.setText(getIntent().getStringExtra("orderId"));
        String desc = getIntent().getStringExtra("desc");
        if (!ArmsUtils.isEmpty(desc)) {
            descTV.setText(desc);
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

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Cache getCache() {
        return provideCache();
    }

    @Override
    public void showError(boolean hasDate) {
        onDate.setVisibility(hasDate ? INVISIBLE : VISIBLE);
        swipeRefreshLayout.setVisibility(hasDate ? VISIBLE : INVISIBLE);
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

    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.getAppointment(false);
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
        mPresenter.getAppointment(true);
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
        }
    }

    public void share(Share share) {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(this).extras();
        if (cache.get(KEY_KEEP + "token") == null) {
            ArmsUtils.startActivity(LoginActivity.class);
            return;
        }
        if (null == share) {
            return;
        }
        UMWeb web = new UMWeb(share.getUrl());
        web.setTitle(share.getTitle());//标题
        web.setDescription(share.getIntro().replace("si.ehanmy.cn", ""));
        web.setThumb(new UMImage(this, share.getImage()));
        new ShareAction(this)
                .withMedia(web)
                .setCallback(shareListener)
                .setDisplayList(SHARE_MEDIA.WEIXIN)
                .open();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onChildItemClick(View v, MyMealDetailListAdapter.ViewName viewname, int position) {
        Appointment appointment = mAdapter.getInfos().get(position);
        switch (viewname) {
            case LEFT:
                if (appointment.getStatus().equals("1")) {
                    // 复制
                    //获取剪贴板管理器：
//                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                    // 创建普通字符型ClipData
//                    ClipData mClipData = ClipData.newPlainText("Label", appointment.getProjectId());
//                    // 将ClipData内容放到系统剪贴板里。
//                    cm.setPrimaryClip(mClipData);
//                    showMessage("已复制到剪贴板");
                    provideCache().put("projectId", appointment.getProjectId());
                    // 分享
                    mPresenter.share();
                } else if (appointment.getStatus().equals("2")) {
                    // 取消预约
                    provideCache().put("reservationId", appointment.getReservationId());
                    showDailog("是否取消预约?");
                } else if (appointment.getStatus().equals("4")) {
                    // 申请奖励
                    provideCache().put("merchId", appointment.getGoods().getMerchId());
                    provideCache().put("goodsId", appointment.getGoods().getGoodsId());
                    mPresenter.apply();
                }
                break;
            case RIGHT:
                if (appointment.getStatus().equals("1")) {
                    // 选择医院
                    Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(getActivity()).extras();
                    Intent intent2 = new Intent(this, SelfPickupAddrListActivity.class);
                    intent2.putExtra(SelfPickupAddrListActivity.KEY_FOR_ACTIVITY_LIST_TYPE, listType);
                    intent2.putExtra("isMeal", true);
                    intent2.putExtra("projectId", appointment.getProjectId());
                    intent2.putExtra("type", "add_appointment_time");
                    listType.setMerchId(appointment.getGoods().getMerchId());
                    listType.setGoodsId(appointment.getGoods().getGoodsId());
                    ArmsUtils.startActivity(intent2);
                } else if (appointment.getStatus().equals("2")) {
                    // 选择医院
                    Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(getActivity()).extras();
                    Intent intent2 = new Intent(this, SelfPickupAddrListActivity.class);
                    intent2.putExtra(SelfPickupAddrListActivity.KEY_FOR_ACTIVITY_LIST_TYPE, listType);
                    intent2.putExtra("isMeal", true);
                    intent2.putExtra("projectId", appointment.getProjectId());
                    intent2.putExtra("type", "modify_appointment_time");
                    intent2.putExtra("reservationId", appointment.getReservationId());
                    listType.setMerchId(appointment.getGoods().getMerchId());
                    listType.setGoodsId(appointment.getGoods().getGoodsId());
                    ArmsUtils.startActivity(intent2);
                } else if (appointment.getStatus().equals("3")) {
                    provideCache().put("reservationId", appointment.getReservationId());
                    // 取消预约
                    showDailog("是否取消预约?");
                } else if (appointment.getStatus().equals("4")) {
                    // 写日记
                    Intent intent = new Intent(getActivity(), ReleaseDiaryActivity.class);
                    intent.putExtra("change", false);
                    intent.putExtra("orderId", appointment.getProjectId());
                    ArmsUtils.startActivity(intent);
                    break;
                }
            case ITEM:
                break;
        }
    }

    private void showDailog(String text) {
        dialog = CustomDialog.create(getSupportFragmentManager())
                .setViewListener(new CustomDialog.ViewListener() {
                    @Override
                    public void bindView(View view) {
                        ((TextView) view.findViewById(R.id.content)).setText(text);
                        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismissAllowingStateLoss();
                            }
                        });
                        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPresenter.cancelAppointment();
                                dialog.dismissAllowingStateLoss();
                            }
                        });
                    }
                })
                .setLayoutRes(R.layout.dialog_remove_good_for_cart)
                .setDimAmount(0.5f)
                .isCenter(true)
                .setWidth(ArmsUtils.getDimens(getActivity(), R.dimen.dialog_width))
                .setHeight(ArmsUtils.getDimens(getActivity(), R.dimen.dialog_height))
                .show();
    }
}
