package me.jessyan.mvparms.demo.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import java.util.List;

import javax.inject.Inject;

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
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsListAdapter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MallFragment extends BaseFragment<MallPresenter> implements MallContract.View, View.OnClickListener, DefaultAdapter.OnRecyclerViewItemClickListener {

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
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    GoodsListAdapter mAdapter;


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

        ArmsUtils.configRecyclerView(mRecyclerView, mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter.getCategory();
        mPresenter.getGoodsList("", "");
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
        }
    }

    @Override
    public void refreshNaviTitle(List<Category> categories) {
        for (Category category : categories) {
            if ("0".equals(category.getParentId())) {
                tabLayout.addTab(tabLayout.newTab().setText(category.getName()));
            }
        }
    }

    @Override
    public void onItemClick(View view, int viewType, Object data, int position) {
        Goods goods = mAdapter.getInfos().get(position);
        Intent intent = new Intent(getActivity().getApplication(), GoodsDetailsActivity.class);
        intent.putExtra("goodsId", goods.getGoodsId());
        intent.putExtra("merchId", goods.getMerchId());
        ArmsUtils.startActivity(intent);
    }
}
