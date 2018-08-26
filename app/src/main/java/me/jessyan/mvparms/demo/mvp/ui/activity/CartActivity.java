package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.cchao.MoneyView;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerCartComponent;
import me.jessyan.mvparms.demo.di.module.CartModule;
import me.jessyan.mvparms.demo.mvp.contract.CartContract;
import me.jessyan.mvparms.demo.mvp.model.entity.CartBean;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.presenter.CartPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.CartListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.CustomDialog;
import me.jessyan.mvparms.demo.mvp.ui.widget.SpacesItemDecoration;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class CartActivity extends BaseActivity<CartPresenter> implements CartContract.View, View.OnClickListener, CartListAdapter.OnChildItemClickLinstener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.edit)
    TextView editTV;
    @BindView(R.id.all)
    View allV;
    @BindView(R.id.delete)
    View deleteV;
    @BindView(R.id.confirm)
    View confirmV;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.cartList)
    RecyclerView cartRV;
    @BindView(R.id.deductionMoney)
    MoneyView deductionMoneyTV;
    @BindView(R.id.payPrice)
    MoneyView payPriceTV;
    @BindView(R.id.totalPrice)
    MoneyView totalPriceTV;
    @BindView(R.id.check)
    View checkV;
    @BindView(R.id.pay_info)
    LinearLayout payInfoLL;
    @Inject
    RecyclerView.Adapter mAdapter;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    CustomDialog dialog = null;
    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerCartComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .cartModule(new CartModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_cart;
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        titleTV.setText("购物车");
        editTV.setOnClickListener(this);
        allV.setOnClickListener(this);
        deleteV.setOnClickListener(this);
        confirmV.setOnClickListener(this);
        checkV.setOnClickListener(this);
        checkV.setSelected(true);
        ArmsUtils.configRecyclerView(cartRV, mLayoutManager);
        ((CartListAdapter) mAdapter).setOnChildItemClickLinstener(this);
        cartRV.setAdapter(mAdapter);
        cartRV.addItemDecoration(new SpacesItemDecoration(0, ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.address_list_item_space)));
        swipeRefreshLayout.setOnRefreshListener(this);
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

    /**
     * 初始化Paginate,用于加载更多
     */
    private void initPaginate() {
        if (mPaginate == null) {
            Paginate.Callbacks callbacks = new Paginate.Callbacks() {
                @Override
                public void onLoadMore() {
                    mPresenter.getCartList(false);
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

            mPaginate = Paginate.with(cartRV, callbacks)
                    .setLoadingTriggerThreshold(0)
                    .build();
            mPaginate.setHasMoreDataToLoad(false);
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
            case R.id.edit:
                swtichToEditMode(!editTV.isSelected());
                break;
            case R.id.all:
                checkV.setSelected(!checkV.isSelected());
                mPresenter.seletedAll(checkV.isSelected());
                break;
            case R.id.delete:
                mPresenter.deleteCartList();
                break;
            case R.id.confirm:
                Intent intent = new Intent(getActivity().getApplication(), ConfirmOrderActivity.class);
                List<CartBean.CartItem> cartItems = ((CartListAdapter) mAdapter).getInfos();
                List<Goods> goodsList = new ArrayList<>();
                for (CartBean.CartItem cartItem : cartItems) {
                    for (Goods goodsBean : cartItem.getGoodsList()) {
                        if ("1".equals(goodsBean.getStatus())) {
                            goodsList.add(goodsBean);
                        }
                    }
                }
                intent.putParcelableArrayListExtra("goodsList", (ArrayList<? extends Parcelable>) goodsList);
                ArmsUtils.startActivity(intent);
                break;
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
    public void updateUI(CartBean cartBean) {
        deductionMoneyTV.setMoneyText(ArmsUtils.formatLong(cartBean.getDeductionMoney()));
        payPriceTV.setMoneyText(ArmsUtils.formatLong(cartBean.getPayPrice()));
        totalPriceTV.setMoneyText(ArmsUtils.formatLong(cartBean.getTotalPrice()));
        for (CartBean.CartItem cartItem : cartBean.getCartItems()) {
            for (Goods goodsBean : cartItem.getGoodsList()) {
                if (!"1".equals(goodsBean.getStatus())) {
                    checkV.setSelected(false);
                }
            }
        }
    }

    private void swtichToEditMode(boolean edit) {
        editTV.setText(edit ? "确定" : "编辑");
        editTV.setSelected(edit);
        confirmV.setVisibility(edit ? View.GONE : View.VISIBLE);
        deleteV.setVisibility(edit ? View.VISIBLE : View.GONE);

        LinearLayout.LayoutParams payInfolayoutParams = (LinearLayout.LayoutParams) payInfoLL.getLayoutParams();
        LinearLayout.LayoutParams deleteLayoutParams = (LinearLayout.LayoutParams) deleteV.getLayoutParams();
        if (edit) {
            deleteLayoutParams.width = 0;
            deleteLayoutParams.weight = 1;
            payInfolayoutParams.weight = 0;
            payInfolayoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        } else {
            payInfolayoutParams.width = 0;
            payInfolayoutParams.weight = 1;
            deleteLayoutParams.weight = 0;
            deleteLayoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        }
        deleteV.setLayoutParams(deleteLayoutParams);
        payInfoLL.setLayoutParams(payInfolayoutParams);
    }

    @Override
    public void onChildItemClick(View v, CartListAdapter.ViewName viewname, int parentPosition, int childPosition) {
        CartListAdapter adapter = (CartListAdapter) mAdapter;
        Goods goodsBean = adapter.getInfos().get(parentPosition).getGoodsList().get(childPosition);
        provideCache().put("goodsId", goodsBean.getGoodsId());
        provideCache().put("merchId", goodsBean.getMerchId());
        provideCache().put("nums", goodsBean.getNums());
        CartBean.Promotion promotion = adapter.getInfos().get(parentPosition).getPromotion();
        if (null != promotion) {
            provideCache().put("promotionId", adapter.getInfos().get(parentPosition).getPromotion().getPromotionId());
        }
        switch (viewname) {
            case ADD:
                provideCache().put("nums", goodsBean.getNums() + 1);
                break;
            case MINUS:
                // 当商品数量为1时，提示对话框，确认是否删除
                if (goodsBean.getNums() == 1) {
                    showCancelDailog("确认将该商品移除购物车?");
                } else {
                    provideCache().put("nums", goodsBean.getNums() - 1);
                }
                break;
            case CHECK:
                if ("1".equals(goodsBean.getStatus())) {
                    provideCache().put("status", "0");
                } else {
                    provideCache().put("status", "1");
                }
                break;
        }
        mPresenter.editCartList(false);
    }

    private void showCancelDailog(String text) {
        dialog = CustomDialog.create(getSupportFragmentManager())
                .setViewListener(new CustomDialog.ViewListener() {
                    @Override
                    public void bindView(View view) {
                        ((TextView) view.findViewById(R.id.content)).setText(text);
                        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                provideCache().put("nums", 0);
                                mPresenter.editCartList(true);
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setLayoutRes(R.layout.dialog_remove_good_for_cart)
                .setDimAmount(0.5f)
                .isCenter(true)
                .setWidth(ArmsUtils.getDimens(this, R.dimen.dialog_width))
                .setHeight(ArmsUtils.getDimens(this, R.dimen.dialog_height))
                .show();
    }

    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(cartRV);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();

    }

    @Override
    public void onRefresh() {
        mPresenter.getCartList(true);
    }
}
