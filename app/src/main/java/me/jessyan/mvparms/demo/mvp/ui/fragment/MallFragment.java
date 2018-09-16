package me.jessyan.mvparms.demo.mvp.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
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
import me.jessyan.mvparms.demo.di.component.DaggerMallComponent;
import me.jessyan.mvparms.demo.di.module.MallModule;
import me.jessyan.mvparms.demo.mvp.contract.MallContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Category;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.HGoods;
import me.jessyan.mvparms.demo.mvp.presenter.MallPresenter;
import me.jessyan.mvparms.demo.mvp.ui.activity.GoodsDetailsActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.HGoodsDetailsActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.MessageActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.SearchActivity;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsFilterSecondAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsFilterThirdAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HGoodsListAdapter;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MallFragment extends BaseFragment<MallPresenter> implements MallContract.View, View.OnClickListener, DefaultAdapter.OnRecyclerViewItemClickListener, TabLayout.OnTabSelectedListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.tab)
    TabLayout tabLayout;
    @BindView(R.id.message)
    View messageV;
    @BindView(R.id.cart)
    View cartV;
    @BindView(R.id.search)
    View searchV;
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

    public static MallFragment newInstance() {
        MallFragment fragment = new MallFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerMallComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .mallModule(new MallModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mall, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        messageV.setOnClickListener(this);
        cartV.setOnClickListener(this);
        searchV.setOnClickListener(this);
        typeV.setOnClickListener(this);
        saleV.setOnClickListener(this);
        priceV.setOnClickListener(this);
        mAdapter.setOnItemClickListener(this);
        mHAdapter.setOnItemClickListener(this);
        maskV.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        ArmsUtils.configRecyclerView(secondFilterRV, new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        secondFilterRV.setAdapter(secondAdapter);
        secondAdapter.setOnItemClickListener(this);
        mPresenter.getCategory();
        mRecyclerView.setAdapter(mAdapter);
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
                    String type = (String) provideCache().get("type");
                    if ("1".equals(type)) {
                        mPresenter.getGoodsList(true);
                    } else if ("2".equals(type)) {
                        mPresenter.getKGoodsList(true);
                    } else if ("3".equals(type)) {
                        mPresenter.getHGoodsList(true);
                    }
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

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传Message,通过what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onCreate还没执行
     * setData里却调用了presenter的方法时,是会报空的,因为dagger注入是在onCreated方法中执行的,然后才创建的presenter
     * 如果要做一些初始化操作,可以不必让外部调setData,在initData中初始化就可以了
     *
     * @param data
     */

    @Override
    public void setData(Object data) {
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

    }

    @Override
    public void onClick(View v) {
        String type = (String) provideCache().get("type");
        switch (v.getId()) {
            case R.id.cart:
                mPresenter.goCart();
                break;
            case R.id.message:
                ArmsUtils.startActivity(MessageActivity.class);
                break;
            case R.id.search:
                ArmsUtils.startActivity(SearchActivity.class);
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
                if ("1".equals(type)) {
                    mPresenter.getGoodsList(true);
                } else if ("2".equals(type)) {
                    mPresenter.getKGoodsList(true);
                } else if ("3".equals(type)) {
                    mPresenter.getHGoodsList(true);
                }
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
                if ("1".equals(type)) {
                    mPresenter.getGoodsList(true);
                } else if ("2".equals(type)) {
                    mPresenter.getKGoodsList(true);
                } else if ("3".equals(type)) {
                    mPresenter.getHGoodsList(true);
                }
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
                filterV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.menu_in));
            }
            if (maskV.getVisibility() == GONE) {
                maskV.setVisibility(VISIBLE);
                maskV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.mask_in));
            }
        } else {
            if (filterV.getVisibility() == VISIBLE) {
                filterV.setVisibility(GONE);
                filterV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.menu_out));
            }
            if (maskV.getVisibility() == VISIBLE) {
                maskV.setVisibility(GONE);
                maskV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.mask_out));
            }
        }
    }

    @Override
    public void refreshNaviTitle(List<Category> categories) {
        List<Category> navi = new ArrayList<>();
        for (Category category : categories) {
            if ("0".equals(category.getParentId())) {
                navi.add(category);
            }
        }
        for (Category category : navi) {
            TabLayout.Tab tab1 = tabLayout.newTab().setTag(category.getBusType()).setText(category.getName());
            tabLayout.addTab(tab1);

        }
        String type = (String) tabLayout.getTabAt(0).getTag();
        provideCache().put("type", type);
        tabLayout.getTabAt(0).select();
        if ("1".equals(type)) {
            mRecyclerView.setAdapter(mAdapter);
            mPresenter.getGoodsList(true);
        } else if ("2".equals(type)) {
            mRecyclerView.setAdapter(mAdapter);
            mPresenter.getKGoodsList(true);
        } else if ("3".equals(type)) {
            mRecyclerView.setAdapter(mHAdapter);
            mPresenter.getHGoodsList(true);
        }
        initPaginate();
        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(getActivity(),
                R.drawable.tablayout_divider_vertical));
        tabLayout.addOnTabSelectedListener(this);
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
                Goods goods = mAdapter.getInfos().get(position);
                Intent intent = new Intent(getActivity().getApplication(), GoodsDetailsActivity.class);
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
                    if (null != childs.get(1).getCatagories()) {
                        for (Category childCategory : childs.get(i).getCatagories()) {
                            childCategory.setChoice(false);
                        }
                    }
                }
                if (childs.get(position).getCatagories() == null || childs.get(position).getCatagories() != null && childs.get(position).getCatagories().size() == 0) {
                    showFilter(false);
                    provideCache().put("secondCategoryId", "");
                    provideCache().put("categoryId", "");
                    typeTV.setTextColor(choiceColor);
                    typeTV.setText(childs.get(position).getName());
                    String type = (String) provideCache().get("type");
                    if ("1".equals(type)) {
                        mPresenter.getGoodsList(true);
                    } else if ("2".equals(type)) {
                        mPresenter.getKGoodsList(true);
                    } else if ("3".equals(type)) {
                        mPresenter.getHGoodsList(true);
                    }
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
                typeTV.setText(grands.get(position).getName());
                provideCache().put("categoryId", grands.get(position).getId());
                showFilter(false);
                String type = (String) provideCache().get("type");
                if ("1".equals(type)) {
                    mPresenter.getGoodsList(true);
                } else if ("2".equals(type)) {
                    mPresenter.getKGoodsList(true);
                } else if ("3".equals(type)) {
                    mPresenter.getHGoodsList(true);
                }
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        String type = (String) tab.getTag();
        provideCache().put("type", type);
        priceTV.setTextColor(unChoiceColor);
        priceV.setSelected(false);
        priceStautsV.setBackground(descD);

        saleTV.setTextColor(unChoiceColor);
        saleV.setSelected(false);
        saleStatusV.setBackground(descD);

        typeTV.setTextColor(unChoiceColor);
        typeTV.setText("全部商品");
        typeStatusV.setBackground(descD);

        provideCache().put("secondCategoryId", "");
        provideCache().put("categoryId", "");

        if ("1".equals(type)) {
            mRecyclerView.setAdapter(mAdapter);
            mPresenter.getGoodsList(true);
        } else if ("2".equals(type)) {
            mRecyclerView.setAdapter(mAdapter);
            mPresenter.getKGoodsList(true);
        } else if ("3".equals(type)) {
            mRecyclerView.setAdapter(mHAdapter);
            mPresenter.getHGoodsList(true);
        }
        initPaginate();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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
        if (null != provideCache().get("type")) {
            String type = (String) provideCache().get("type");
            if ("1".equals(type)) {
                mPresenter.getGoodsList(true);
            } else if ("2".equals(type)) {
                mPresenter.getKGoodsList(true);
            } else if ("3".equals(type)) {
                mPresenter.getHGoodsList(true);
            }
        } else {
            mPresenter.getGoodsList(true);
        }
    }
}
