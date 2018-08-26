package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerDiaryForGoodsComponent;
import me.jessyan.mvparms.demo.di.module.DiaryForGoodsModule;
import me.jessyan.mvparms.demo.mvp.contract.DiaryForGoodsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Diary;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryResponse;
import me.jessyan.mvparms.demo.mvp.presenter.DiaryForGoodsPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyDiaryListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.ShapeImageView;
import me.jessyan.mvparms.demo.mvp.ui.widget.SpacesItemDecoration;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class DiaryForGoodsActivity extends BaseActivity<DiaryForGoodsPresenter> implements DiaryForGoodsContract.View, View.OnClickListener, MyDiaryListAdapter.OnChildItemClickLinstener {
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
    TextView goodsPriceTV;
    @BindView(R.id.tab)
    TabLayout tabLayout;
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
        diaryRV.addItemDecoration(new SpacesItemDecoration(0, ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.address_list_item_space)));
        diaryRV.setAdapter(mAdapter);
        mAdapter.setOnChildItemClickLinstener(this);
        tabLayout.addTab(tabLayout.newTab().setText("我的日记"));
        initPaginate();
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
                Intent leftIntent = new Intent(getActivity().getApplication(), DiaryImageActivity.class);
                leftIntent.putExtra("diaryAlbumId", response.getDiaryAlbumList().get(0).getDiaryAlbumId());
                leftIntent.putExtra("goodsId", response.getGoods().getGoodsId());
                leftIntent.putExtra("merchId", response.getGoods().getMerchId());
                leftIntent.putExtra("memberId", response.getMember().getMemberId());
                ArmsUtils.startActivity(leftIntent);
                break;
            case R.id.right:
                if (response.getDiaryAlbumList().size() <= 1) {
                    return;
                }
                Intent rightIntent = new Intent(getActivity().getApplication(), DiaryImageActivity.class);
                rightIntent.putExtra("diaryAlbumId", response.getDiaryAlbumList().get(0).getDiaryAlbumId());
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

    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.getMyDiaryList();
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
                        .url(response.getMember().getHeadImage())
                        .imageView(headImageIV)
                        .build());
        nickNameTV.setText(response.getMember().getNickName());
        followV.setSelected("1".equals(response.getMember().getIsFollow()) ? true : false);
        if (response.getDiaryAlbumList().size() > 0) {
            mImageLoader.loadImage(this,
                    ImageConfigImpl
                            .builder()
                            .url(response.getDiaryAlbumList().get(0).getImage())
                            .imageView(leftIV)
                            .build());
            leftCountTV.setText(String.valueOf(response.getDiaryAlbumList().get(0).getNum()));
        }
        if (response.getDiaryAlbumList().size() > 1) {
            rightCountTV.setVisibility(View.VISIBLE);
            mImageLoader.loadImage(this,
                    ImageConfigImpl
                            .builder()
                            .url(response.getDiaryAlbumList().get(1).getImage())
                            .imageView(rightIV)
                            .build());
            rightCountTV.setText(String.valueOf(response.getDiaryAlbumList().get(1).getNum()));
        } else {
            rightCountTV.setVisibility(View.INVISIBLE);
        }

        mImageLoader.loadImage(this,
                ImageConfigImpl
                        .builder()
                        .url(response.getGoods().getImage())
                        .imageView(goodsImageIV)
                        .build());
        goodsNameTV.setText(response.getGoods().getName());
        goodsPriceTV.setText(String.valueOf(response.getGoods().getSalePrice()));


    }


    @Override
    public void onChildItemClick(View v, MyDiaryListAdapter.ViewName viewname, int position) {
        Diary diary = mAdapter.getInfos().get(position);
        switch (viewname) {
            case LEFT_IMAGE:
                break;
            case RIGHT_IMAGE:
                break;
            case VOTE:
                provideCache().put("diaryId", diary.getDiaryId());
                mPresenter.vote("1".equals(diary.getIsPraise()) ? false : true, position);
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
}
