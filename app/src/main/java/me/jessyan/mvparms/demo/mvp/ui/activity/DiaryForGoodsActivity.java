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

import com.github.cchao.MoneyView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerDiaryForGoodsComponent;
import me.jessyan.mvparms.demo.di.module.DiaryForGoodsModule;
import me.jessyan.mvparms.demo.mvp.contract.DiaryForGoodsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Diary;
import me.jessyan.mvparms.demo.mvp.model.entity.DiaryAlbum;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryResponse;
import me.jessyan.mvparms.demo.mvp.presenter.DiaryForGoodsPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyDiaryListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.HiNestedScrollView;
import me.jessyan.mvparms.demo.mvp.ui.widget.ShapeImageView;
import me.jessyan.mvparms.demo.mvp.ui.widget.SpacesItemDecoration;

import static com.jess.arms.utils.ArmsUtils.getContext;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class DiaryForGoodsActivity extends BaseActivity<DiaryForGoodsPresenter> implements DiaryForGoodsContract.View, View.OnClickListener, MyDiaryListAdapter.OnChildItemClickLinstener, NestedScrollView.OnScrollChangeListener, SwipeRefreshLayout.OnRefreshListener {
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

    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;

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
        initPaginate();
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
    public void updateDiaryUI(int count) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) diaryRV.getLayoutParams();
        layoutParams.height = ArmsUtils.getDimens(getContext(), R.dimen.home_diary_item_height) * count + ArmsUtils.getDimens(getContext(), R.dimen.address_list_item_space) * (count - 1);
        diaryRV.setLayoutParams(layoutParams);
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

            mPaginate = Paginate.with(diaryRV, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .build();
            mPaginate.setHasMoreDataToLoad(false);
        }
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
                            .build());
            leftCountTV.setText(String.valueOf(response.getDiaryAlbumList().get(0).getNum()));
        } else {
            mImageLoader.loadImage(this,
                    ImageConfigImpl
                            .builder()
                            .placeholder(R.drawable.place_holder_img)
                            .url("")
                            .imageView(leftIV)
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
                            .build());
            rightCountTV.setText(String.valueOf(response.getDiaryAlbumList().get(1).getNum()));
        } else {
            mImageLoader.loadImage(this,
                    ImageConfigImpl
                            .builder()
                            .placeholder(R.drawable.place_holder_img)
                            .url("")
                            .imageView(rightIV)
                            .build());
            rightCountTV.setVisibility(View.INVISIBLE);
        }

        mImageLoader.loadImage(this,
                ImageConfigImpl
                        .builder()
                        .placeholder(R.drawable.place_holder_img)
                        .url(response.getGoods().getImage())
                        .imageView(goodsImageIV)
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
                    intent.putExtra("images", new String[]{diary.getImageList().get(0)});
                    ArmsUtils.startActivity(intent);
                }
                break;
            case RIGHT_IMAGE:
                if (diary.getImageList() != null && diary.getImageList().size() > 1) {
                    Intent intent = new Intent(getActivity(), ImageShowActivity.class);
                    intent.putExtra("images", new String[]{diary.getImageList().get(1)});
                    ArmsUtils.startActivity(intent);
                }
                break;
            case ITEM:
                Intent intent = new Intent(getActivity().getApplication(), DiaryDetailsActivity.class);
                intent.putExtra("diaryId", diary.getDiaryId());
                intent.putExtra("position", position);
                ArmsUtils.startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(diaryRV);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        this.mPaginate = null;
        super.onDestroy();
    }

    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        int[] location = new int[2];
        tabLayout.getLocationOnScreen(location);
        int yPosition = location[1];
        if (yPosition <= ArmsUtils.getDimens(this.getActivity(), R.dimen.title_height)) {
            talFloatLayout.setVisibility(View.VISIBLE);
            nestedScrollView.setNeedScroll(false);
        } else {
            talFloatLayout.setVisibility(View.GONE);
            nestedScrollView.setNeedScroll(true);
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.getDiaryForGoodsInfo(true);
    }
}
