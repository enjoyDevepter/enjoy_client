package me.jessyan.mvparms.demo.mvp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.paginate.Paginate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerDiscoverComponent;
import me.jessyan.mvparms.demo.di.module.DiscoverModule;
import me.jessyan.mvparms.demo.mvp.contract.DiscoverContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Diary;
import me.jessyan.mvparms.demo.mvp.model.entity.DiaryNavi;
import me.jessyan.mvparms.demo.mvp.presenter.DiscoverPresenter;
import me.jessyan.mvparms.demo.mvp.ui.activity.DiaryForGoodsActivity;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DiaryListAdapter;
import me.jessyan.mvparms.demo.mvp.ui.widget.SpacesItemDecoration;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class DiscoverFragment extends BaseFragment<DiscoverPresenter> implements DiscoverContract.View, DiaryListAdapter.OnChildItemClickLinstener, SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.tab)
    TabLayout tabLayout;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.diaryRV)
    RecyclerView diaryRV;
    @BindView(R.id.noData)
    View noDataV;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    DiaryListAdapter mAdapter;

    private Paginate mPaginate;
    private boolean isLoadingMore;
    private boolean hasLoadedAllItems;
    private List<DiaryNavi> diaryNavis;

    public static DiscoverFragment newInstance() {
        DiscoverFragment fragment = new DiscoverFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(AppComponent appComponent) {
        DaggerDiscoverComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .discoverModule(new DiscoverModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discover, container, false);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titleTV.setText("日记");
        backV.setVisibility(View.GONE);
        ArmsUtils.configRecyclerView(diaryRV, mLayoutManager);
        diaryRV.addItemDecoration(new SpacesItemDecoration(0, ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.address_list_item_space)));
        diaryRV.setAdapter(mAdapter);
        mAdapter.setOnChildItemClickLinstener(this);
        initPaginate();
        swipeRefreshLayout.setOnRefreshListener(this);
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
                    mPresenter.getDiaryList(false);
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
    public void updateTab(List<DiaryNavi> diaryNavis) {
        this.diaryNavis = diaryNavis;

        // 一级导航
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText("推荐"));
        tabLayout.addTab(tabLayout.newTab().setText("我的关注"));
        for (DiaryNavi diaryNavi : diaryNavis) {
            tabLayout.addTab(tabLayout.newTab().setText(diaryNavi.getTitle()));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        provideCache().put("type", "recom");
                        break;
                    case 1:
                        provideCache().put("type", "folow");
                        break;
                    default:
                        try {
                            JSONObject object = new JSONObject(diaryNavis.get(tab.getPosition() - 2).getExtendParam());
                            provideCache().put("type", object.optString("typeId"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                }
                mPresenter.getDiaryList(true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void showError(boolean nodata) {
        noDataV.setVisibility(nodata ? View.VISIBLE : View.GONE);
        swipeRefreshLayout.setVisibility(nodata ? View.GONE : View.VISIBLE);
    }

    @Override
    public Cache getCache() {
        return provideCache();
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
            case RIGHT_IMAGE:
            case ITEM:
                Intent intent = new Intent(getActivity().getApplication(), DiaryForGoodsActivity.class);
                intent.putExtra("diaryId", diary.getDiaryId());
                intent.putExtra("goodsId", diary.getGoods().getGoodsId());
                intent.putExtra("merchId", diary.getGoods().getMerchId());
                intent.putExtra("memberId", diary.getMember().getMemberId());
                ArmsUtils.startActivity(intent);
                break;
        }
    }

    @Override
    public void onDestroy() {
        DefaultAdapter.releaseAllHolder(diaryRV);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        this.mPaginate = null;
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        mPresenter.getDiaryList(true);
    }
}
