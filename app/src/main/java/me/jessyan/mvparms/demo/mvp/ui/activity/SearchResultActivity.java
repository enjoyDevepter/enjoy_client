package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindDrawable;
import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerSerachResultComponent;
import me.jessyan.mvparms.demo.di.module.SearchResultModule;
import me.jessyan.mvparms.demo.mvp.contract.SearchResultContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Category;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.HGoods;
import me.jessyan.mvparms.demo.mvp.presenter.SearchResultPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsFilterSecondAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsFilterThirdAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HGoodsListAdapter;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class SearchResultActivity extends BaseActivity<SearchResultPresenter> implements SearchResultContract.View, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, DefaultAdapter.OnRecyclerViewItemClickListener {

    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.type_layout)
    View typeV;
    @BindView(R.id.type)
    TextView typeTV;
    @BindView(R.id.type_status)
    View typeStatusV;
    @BindView(R.id.sale_layout)
    View saleV;
    @BindView(R.id.sale)
    TextView saleTV;
    @BindView(R.id.sale_status)
    View saleStatusV;
    @BindView(R.id.price_layout)
    View priceV;
    @BindView(R.id.price)
    TextView priceTV;
    @BindView(R.id.price_status)
    View priceStautsV;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.goods)
    RecyclerView mRecyclerView;
    @BindView((R.id.no_date))
    View noDataV;
    @BindView(R.id.secondCategory)
    RecyclerView secondFilterRV;
    @BindView(R.id.thirdCategory)
    RecyclerView thirdFilterRV;
    @BindView(R.id.filter_layout)
    View filterV;
    @BindView(R.id.mask)
    View maskV;
    @BindDrawable(R.mipmap.arrow_up)
    Drawable asceD;
    @BindDrawable(R.mipmap.arrow_down)
    Drawable descD;
    @BindColor(R.color.choice)
    int choiceColor;
    @BindColor(R.color.unchoice)
    int unChoiceColor;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    GoodsListAdapter mAdapter;
    @Inject
    HGoodsListAdapter mHAdapter;
    @Inject
    GoodsFilterSecondAdapter secondAdapter;
    GoodsFilterThirdAdapter thirdAdapter;

    private List<Category> thirdCategoryList;

    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerSerachResultComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .searchResultModule(new SearchResultModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_serach_result; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titleTV.setText(getIntent().getStringExtra("title"));
        backV.setOnClickListener(this);
        typeV.setOnClickListener(this);
        saleV.setOnClickListener(this);
        priceV.setOnClickListener(this);
        maskV.setOnClickListener(this);
        mAdapter.setOnItemClickListener(this);
        mHAdapter.setOnItemClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        ArmsUtils.configRecyclerView(secondFilterRV, new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        secondFilterRV.setAdapter(secondAdapter);
        secondAdapter.setOnItemClickListener(this);

        String busType = getIntent().getStringExtra("busType");
        if ("1".equals(busType)) {
            mRecyclerView.setAdapter(mAdapter);
        } else if ("2".equals(busType)) {
            mRecyclerView.setAdapter(mAdapter);
        } else if ("3".equals(busType)) {
            mRecyclerView.setAdapter(mHAdapter);
        }
        initPaginate();
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

    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.getSearchResult(false);
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
    public void showError(boolean hasDate) {
        noDataV.setVisibility(hasDate ? INVISIBLE : VISIBLE);
        swipeRefreshLayout.setVisibility(hasDate ? VISIBLE : INVISIBLE);
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
            case R.id.mask:
                showFilter(false);
                break;
            case R.id.type_layout:
                typeV.setSelected(!typeV.isSelected());
                showFilter(typeV.isSelected());
                break;
            case R.id.sale_layout:
                provideCache().put("orderByField", "sales");
                provideCache().put("orderByAsc", saleTV.isSelected());
                saleV.setSelected(!saleV.isSelected());
                saleTV.setTextColor(choiceColor);
                saleStatusV.setBackground(saleV.isSelected() ? asceD : descD);

                priceTV.setTextColor(unChoiceColor);
                priceV.setSelected(false);
                priceStautsV.setBackground(descD);
                mPresenter.getSearchResult(true);
                break;
            case R.id.price_layout:
                provideCache().put("orderByField", "salesPrice");
                provideCache().put("orderByAsc", priceV.isSelected());
                priceV.setSelected(!priceV.isSelected());
                priceTV.setTextColor(choiceColor);
                priceStautsV.setBackground(priceV.isSelected() ? asceD : descD);

                saleTV.setTextColor(unChoiceColor);
                saleV.setSelected(false);
                saleStatusV.setBackground(descD);
                showFilter(false);
                mPresenter.getSearchResult(true);
                break;
        }
    }

    private void showFilter(boolean show) {
        typeV.setSelected(show);
        if (show && secondAdapter.getInfos().size() > 0) {
            secondAdapter.notifyDataSetChanged();
            for (Category category : secondAdapter.getInfos()) {
                if (category.isChoice()) {
                    thirdCategoryList = new ArrayList<>();
                    thirdCategoryList.addAll(category.getCatagories());
                }
            }
            thirdAdapter = new GoodsFilterThirdAdapter(thirdCategoryList);
            ArmsUtils.configRecyclerView(thirdFilterRV, new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            thirdFilterRV.setAdapter(thirdAdapter);
            thirdAdapter.setOnItemClickListener(this);
            if (filterV.getVisibility() == GONE) {
                filterV.setVisibility(VISIBLE);
                filterV.setAnimation(AnimationUtils.loadAnimation(this, R.anim.menu_in));
            }
            if (maskV.getVisibility() == GONE) {
                maskV.setVisibility(VISIBLE);
                maskV.setAnimation(AnimationUtils.loadAnimation(this, R.anim.mask_in));
            }
        } else {
            if (filterV.getVisibility() == VISIBLE) {
                filterV.setVisibility(GONE);
                filterV.setAnimation(AnimationUtils.loadAnimation(this, R.anim.menu_out));
            }
            if (maskV.getVisibility() == VISIBLE) {
                maskV.setVisibility(GONE);
                maskV.setAnimation(AnimationUtils.loadAnimation(this, R.anim.mask_out));
            }
        }
    }


    @Override
    public Cache getCache() {
        return provideCache();
    }

    @Override
    public void onItemClick(View view, int viewType, Object data, int position) {
        // 如何区分
        switch (viewType) {
            case R.layout.goods_list_item:
                String busType = getIntent().getStringExtra("busType");
                Intent intent;
                if ("1".equals(busType)) {
                    intent = new Intent(getActivity().getApplication(), GoodsDetailsActivity.class);
                } else {
                    intent = new Intent(getActivity().getApplication(), KGoodsDetailsActivityActivity.class);
                }
                Goods goods = mAdapter.getInfos().get(position);
                intent.putExtra("type", goods.getType());
                intent.putExtra("goodsId", goods.getGoodsId());
                intent.putExtra("merchId", goods.getMerchId());
                ArmsUtils.startActivity(intent);
                break;
            case R.layout.h_goods_list_item:
                HGoods hGoods = mHAdapter.getInfos().get(position);
                Intent hGoodsintent = new Intent(getActivity().getApplication(), HGoodsDetailsActivity.class);
                hGoodsintent.putExtra("goodsId", hGoods.getGoodsId());
                hGoodsintent.putExtra("merchId", hGoods.getMerchId());
                hGoodsintent.putExtra("advanceDepositId", hGoods.getAdvanceDepositId());
                ArmsUtils.startActivity(hGoodsintent);
                break;
            case R.layout.goods_filter_second_item:
                List<Category> childs = secondAdapter.getInfos();
                for (int i = 0; i < childs.size(); i++) {
                    childs.get(i).setChoice(i == position ? true : false);
                    if (null != childs.get(i).getCatagories()) {
                        for (Category childCategory : childs.get(i).getCatagories()) {
                            childCategory.setChoice(false);
                        }
                    }
                }
                if (childs.get(position).getCatagories() == null || childs.get(position).getCatagories() != null && childs.get(position).getCatagories().size() == 0) {
                    showFilter(false);
                    provideCache().put("secondCategoryId", "");
                    provideCache().put("categoryId", "");
                    if (position != 0) {
                        typeTV.setTextColor(choiceColor);
                        typeStatusV.setBackground(asceD);
                    } else {
                        typeTV.setTextColor(unChoiceColor);
                        typeStatusV.setBackground(descD);
                    }
                    typeTV.setText(childs.get(position).getName());
                    mPresenter.getSearchResult(true);
                    return;
                }
                secondAdapter.notifyDataSetChanged();
                thirdCategoryList.clear();
                thirdCategoryList.addAll(childs.get(position).getCatagories());
                provideCache().put("secondCategoryId", childs.get(position).getId());
                thirdAdapter.notifyDataSetChanged();
                break;
            case R.layout.goods_filter_third_item:
                List<Category> grands = thirdAdapter.getInfos();
                for (int i = 0; i < grands.size(); i++) {
                    grands.get(i).setChoice(i == position ? true : false);
                }
                thirdAdapter.notifyDataSetChanged();
                typeTV.setTextColor(choiceColor);
                typeStatusV.setBackground(asceD);
                typeTV.setText(grands.get(position).getName());
                provideCache().put("categoryId", grands.get(position).getId());
                showFilter(false);
                mPresenter.getSearchResult(true);
                break;
        }
    }

    @Override
    public void onDestroy() {
        DefaultAdapter.releaseAllHolder(mRecyclerView);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        DefaultAdapter.releaseAllHolder(secondFilterRV);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        DefaultAdapter.releaseAllHolder(thirdFilterRV);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
        this.mPaginate = null;
    }

    @Override
    public void onRefresh() {
        mPresenter.getSearchResult(true);
    }
}
