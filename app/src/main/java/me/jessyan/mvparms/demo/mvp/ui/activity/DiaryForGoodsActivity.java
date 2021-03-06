package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;

import org.simple.eventbus.Subscriber;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.di.component.DaggerDiaryForGoodsComponent;
import me.jessyan.mvparms.demo.di.module.DiaryForGoodsModule;
import me.jessyan.mvparms.demo.mvp.contract.DiaryForGoodsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Diary;
import me.jessyan.mvparms.demo.mvp.model.entity.DiaryAlbum;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryResponse;
import me.jessyan.mvparms.demo.mvp.presenter.DiaryForGoodsPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyDiaryListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.HiNestedScrollView;
import me.jessyan.mvparms.demo.mvp.ui.widget.MoneyView;
import me.jessyan.mvparms.demo.mvp.ui.widget.ShapeImageView;
import me.jessyan.mvparms.demo.mvp.ui.widget.SpacesItemDecoration;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;
import static com.jess.arms.utils.ArmsUtils.getContext;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class DiaryForGoodsActivity extends BaseActivity<DiaryForGoodsPresenter> implements DiaryForGoodsContract.View, View.OnClickListener, MyDiaryListAdapter.OnChildItemClickLinstener, NestedScrollView.OnScrollChangeListener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.title_layout)
    View titleLayoutV;
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.headImage)
    ShapeImageView headImageIV;
    @BindView(R.id.nickName)
    TextView nickNameTV;
    @BindView(R.id.follow)
    View followV;
    @BindView(R.id.left)
    ImageView leftIV;
    @BindView(R.id.left_count)
    TextView leftCountTV;
    @BindView(R.id.right)
    ImageView rightIV;
    @BindView(R.id.right_count)
    TextView rightCountTV;
    @BindView(R.id.goods_info)
    View goodsInfoV;
    @BindView(R.id.goods_image)
    ImageView goodsImageIV;
    @BindView(R.id.goods_name)
    TextView goodsNameTV;
    @BindView(R.id.goods_price)
    MoneyView goodsPriceTV;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.nestedScrollView)
    HiNestedScrollView nestedScrollView;
    @BindView(R.id.tab)
    TabLayout tabLayout;
    @BindView(R.id.tabFloat)
    TabLayout talFloatLayout;
    @BindView(R.id.diaryRV)
    RecyclerView diaryRV;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    MyDiaryListAdapter mAdapter;
    @Inject
    ImageLoader mImageLoader;

    private DiaryResponse response;

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
        DaggerDiaryForGoodsComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .diaryForGoodsModule(new DiaryForGoodsModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_diary_for_goods; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titleTV.setText("日记本");
        backV.setOnClickListener(this);
        leftIV.setOnClickListener(this);
        rightIV.setOnClickListener(this);
        followV.setOnClickListener(this);
        goodsInfoV.setOnClickListener(this);
        ArmsUtils.configRecyclerView(diaryRV, mLayoutManager);
        diaryRV.setAdapter(mAdapter);
        diaryRV.addItemDecoration(new SpacesItemDecoration(0, ArmsUtils.getDimens(getContext(), R.dimen.address_list_item_space)));
        mAdapter.setOnChildItemClickLinstener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        tabLayout.addTab(tabLayout.newTab().setText("我的日记"));
        talFloatLayout.addTab(talFloatLayout.newTab().setText("我的日记"));
        diaryRV.setNestedScrollingEnabled(false);
        diaryRV.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstCompletelyVisibleItemPosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                if (firstCompletelyVisibleItemPosition == 0) {
                    nestedScrollView.setNeedScroll(true);
                }
            }
        });
        nestedScrollView.setOnScrollChangeListener(this);
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


    @Subscriber(tag = EventBusTags.DIARY_COMMENT_SUCCESS)
    private void updateDirayComment(boolean success) {
        mPresenter.getMyDiaryList(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
            case R.id.follow:
                provideCache().put("memberId", response.getMember().getMemberId());
                mPresenter.follow(!followV.isSelected());
                break;
            case R.id.goods_info:
                Intent intent = new Intent(getActivity().getApplication(), GoodsDetailsActivity.class);
                intent.putExtra("goodsId", response.getGoods().getGoodsId());
                intent.putExtra("merchId", response.getGoods().getMerchId());
                ArmsUtils.startActivity(intent);
                break;
            case R.id.left:
                List<DiaryAlbum> diaryAlbumList = response.getDiaryAlbumList();
                if (null == diaryAlbumList || diaryAlbumList.size() <= 0) {
                    return;
                }
                Intent leftIntent = new Intent(getActivity().getApplication(), DiaryImageActivity.class);
                leftIntent.putExtra("diaryAlbumId", diaryAlbumList.get(0).getDiaryAlbumId());
                leftIntent.putExtra("goodsId", response.getGoods().getGoodsId());
                leftIntent.putExtra("merchId", response.getGoods().getMerchId());
                leftIntent.putExtra("memberId", response.getMember().getMemberId());
                ArmsUtils.startActivity(leftIntent);
                break;
            case R.id.right:
                List<DiaryAlbum> diaryList = response.getDiaryAlbumList();
                if (null == diaryList || diaryList.size() <= 1) {
                    return;
                }
                Intent rightIntent = new Intent(getActivity().getApplication(), DiaryImageActivity.class);
                rightIntent.putExtra("diaryAlbumId", diaryList.get(0).getDiaryAlbumId());
                rightIntent.putExtra("goodsId", response.getGoods().getGoodsId());
                rightIntent.putExtra("merchId", response.getGoods().getMerchId());
                rightIntent.putExtra("memberId", response.getMember().getMemberId());
                ArmsUtils.startActivity(rightIntent);
                break;
        }
    }

    @Override
    public void setLoadedAllItems(boolean has) {
        this.hasLoadedAllItems = has;
    }

    @Override
    public void updateDiaryUI(int count) {
        int[] location = new int[2];
        titleLayoutV.getLocationInWindow(location);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) diaryRV.getLayoutParams();
        layoutParams.height = ArmsUtils.getDimens(getContext(), R.dimen.home_diary_item_height) * count + ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.address_list_item_space) * (count - 1);
        diaryRV.setLayoutParams(layoutParams);
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
    public void updateFollow(boolean follow) {
        followV.setSelected(follow);
    }

    @Override
    public void updateUI(DiaryResponse response) {
        if (null == response) {
            return;
        }
        this.response = response;

        mImageLoader.loadImage(this,
                ImageConfigImpl
                        .builder()
                        .placeholder(R.mipmap.place_holder_user)
                        .url(response.getMember().getHeadImage())
                        .imageView(headImageIV)
                        .build());
        nickNameTV.setText(response.getMember().getNickName());
        followV.setSelected("1".equals(response.getMember().getIsFollow()) ? true : false);
        if (response.getDiaryAlbumList().size() > 0) {
            mImageLoader.loadImage(this,
                    ImageConfigImpl
                            .builder()
                            .placeholder(R.drawable.place_holder_img)
                            .url(response.getDiaryAlbumList().get(0).getImage())
                            .imageView(leftIV)
                            .isCenterCrop(true)
                            .build());
            if (response.getDiaryAlbumList().get(0).getNum() > 0) {
                leftCountTV.setText(String.valueOf(response.getDiaryAlbumList().get(0).getNum()));
            } else {
                leftCountTV.setVisibility(View.GONE);
            }
        } else {
            mImageLoader.loadImage(this,
                    ImageConfigImpl
                            .builder()
                            .placeholder(R.drawable.place_holder_img)
                            .url("")
                            .imageView(leftIV)
                            .isCenterCrop(true)
                            .build());
            leftCountTV.setVisibility(View.GONE);
        }
        if (response.getDiaryAlbumList().size() > 1) {
            rightCountTV.setVisibility(View.VISIBLE);
            mImageLoader.loadImage(this,
                    ImageConfigImpl
                            .builder()
                            .placeholder(R.drawable.place_holder_img)
                            .url(response.getDiaryAlbumList().get(1).getImage())
                            .imageView(rightIV)
                            .isCenterCrop(true)
                            .build());
            if (response.getDiaryAlbumList().get(0).getNum() > 0) {
                rightCountTV.setText(String.valueOf(response.getDiaryAlbumList().get(1).getNum()));
            } else {
                rightCountTV.setVisibility(View.GONE);
            }
        } else {
            mImageLoader.loadImage(this,
                    ImageConfigImpl
                            .builder()
                            .placeholder(R.drawable.place_holder_img)
                            .url("")
                            .imageView(rightIV)
                            .isCenterCrop(true)
                            .build());
            rightCountTV.setVisibility(View.INVISIBLE);
        }

        mImageLoader.loadImage(this,
                ImageConfigImpl
                        .builder()
                        .placeholder(R.drawable.place_holder_img)
                        .url(response.getGoods().getImage())
                        .imageView(goodsImageIV)
                        .isCenterCrop(true)
                        .build());
        goodsNameTV.setText(response.getGoods().getName());
        goodsPriceTV.setMoneyText(String.valueOf(response.getGoods().getSalePrice()));
    }


    @Override
    public void onChildItemClick(View v, MyDiaryListAdapter.ViewName viewname, int position) {
        Diary diary = mAdapter.getInfos().get(position);
        switch (viewname) {
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
                Intent intent = new Intent(getActivity().getApplication(), DiaryDetailsActivity.class);
                intent.putExtra("diaryId", diary.getDiaryId());
                ArmsUtils.startActivity(intent);
                break;
            case COMMENT:
                break;
            case SHARE:
                showWX(diary);
                break;
        }
    }

    private void showWX(Diary diary) {
        if (null != diary) {
            Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(getContext()).extras();
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

    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(diaryRV);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        int[] location = new int[2];
        tabLayout.getLocationOnScreen(location);
        int[] titleLocation = new int[2];
        titleLayoutV.getLocationInWindow(titleLocation);
        int yPosition = location[1];
        if (yPosition < (titleLayoutV.getHeight() + titleLocation[1])) {
            talFloatLayout.setVisibility(View.VISIBLE);
        } else {
            talFloatLayout.setVisibility(View.GONE);
        }
        if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
            if (!hasLoadedAllItems) {
                mPresenter.getMyDiaryList(false);
            }
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.getDiaryForGoodsInfo(true);
    }
}
