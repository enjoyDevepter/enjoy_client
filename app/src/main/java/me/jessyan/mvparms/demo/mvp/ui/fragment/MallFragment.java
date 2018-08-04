package me.jessyan.mvparms.demo.mvp.ui.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;

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
import me.jessyan.mvparms.demo.mvp.presenter.MallPresenter;
import me.jessyan.mvparms.demo.mvp.ui.activity.GoodsDetailsActivity;
import me.jessyan.mvparms.demo.mvp.ui.activity.SearchActivity;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsFilterSecondAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsFilterThirdAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsListAdapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MallFragment extends BaseFragment<MallPresenter> implements MallContract.View, View.OnClickListener, DefaultAdapter.OnRecyclerViewItemClickListener, TabLayout.OnTabSelectedListener {

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
    @BindView(R.id.goods)
    RecyclerView mRecyclerView;
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
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    GoodsListAdapter mAdapter;
    @Inject
    GoodsFilterSecondAdapter secondAdapter;
    GoodsFilterThirdAdapter thirdAdapter;

    private List<Category> thirdCategoryList;

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
        maskV.setOnClickListener(this);

        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        ArmsUtils.configRecyclerView(secondFilterRV, new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        secondFilterRV.setAdapter(secondAdapter);
        secondAdapter.setOnItemClickListener(this);
        tabLayout.addOnTabSelectedListener(this);
        mPresenter.getCategory();
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cart:
                break;
            case R.id.message:
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
//                typeTV.setTextColor(choiceColor);
//                typeStatusV.setBackground(typeV.isSelected() ? asceD : descD);
                break;
            case R.id.sale_layout:
                saleV.setSelected(!saleV.isSelected());
                provideCache().put("orderByField", "sales");
                provideCache().put("orderByAsc", saleTV.isSelected());
                saleTV.setTextColor(choiceColor);
                saleStatusV.setBackground(saleV.isSelected() ? asceD : descD);
                mPresenter.getGoodsList();
                break;
            case R.id.price_layout:
                priceV.setSelected(!priceV.isSelected());
                provideCache().put("orderByField", "salesPrice");
                provideCache().put("orderByAsc", priceV.isSelected());
                priceTV.setTextColor(choiceColor);
                priceStautsV.setBackground(priceV.isSelected() ? asceD : descD);
                showFilter(false);
                mPresenter.getGoodsList();
                break;
        }
    }

    private void showFilter(boolean show) {
        typeV.setSelected(show);
        if (show && secondAdapter.getInfos().size() > 0) {
            secondAdapter.notifyDataSetChanged();
            thirdCategoryList = new ArrayList<>();
            thirdCategoryList.addAll(secondAdapter.getInfos().get(0).getCatagories());
            thirdAdapter = new GoodsFilterThirdAdapter(thirdCategoryList);
            ArmsUtils.configRecyclerView(thirdFilterRV, new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            thirdFilterRV.setAdapter(thirdAdapter);
            thirdAdapter.setOnItemClickListener(this);
            filterV.setVisibility(VISIBLE);
            filterV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.menu_in));
            maskV.setVisibility(VISIBLE);
            maskV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.mask_in));
        } else {
            filterV.setVisibility(GONE);
            filterV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.menu_out));
            maskV.setVisibility(GONE);
            maskV.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.mask_out));
        }
    }

    @Override
    public void refreshNaviTitle(List<Category> categories) {
        for (Category category : categories) {
            if ("0".equals(category.getParentId())) {
                tabLayout.addTab(tabLayout.newTab().setTag(category.getBusType()).setText(category.getName()));
            }
        }
    }

    @Override
    public Cache getCache() {
        return provideCache();
    }

    @Override
    public void onItemClick(View view, int viewType, Object data, int position) {
        System.out.println("");
        // 如何区分
        switch (viewType) {
            case R.layout.goods_list_item:
                Goods goods = mAdapter.getInfos().get(position);
                Intent intent = new Intent(getActivity().getApplication(), GoodsDetailsActivity.class);
                intent.putExtra("goodsId", goods.getGoodsId());
                intent.putExtra("merchId", goods.getMerchId());
                ArmsUtils.startActivity(intent);
                break;
            case R.layout.goods_filter_second_item:
                List<Category> childs = secondAdapter.getInfos();
                for (int i = 0; i < childs.size(); i++) {
                    childs.get(i).setChoice(i == position ? true : false);
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
                mPresenter.getGoodsList();
                break;
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        provideCache().put("busType", tab.getTag());
        mPresenter.getGoodsList();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
