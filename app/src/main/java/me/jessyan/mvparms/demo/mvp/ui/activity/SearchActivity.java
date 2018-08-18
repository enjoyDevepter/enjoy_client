package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.di.component.DaggerSearchComponent;
import me.jessyan.mvparms.demo.di.module.SearchModule;
import me.jessyan.mvparms.demo.mvp.contract.SearchContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Category;
import me.jessyan.mvparms.demo.mvp.presenter.SearchPresenter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HistoryAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.SearchTypeAdapter;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchContract.View, TagFlowLayout.OnTagClickListener, View.OnClickListener, DefaultAdapter.OnRecyclerViewItemClickListener, TextView.OnEditorActionListener {
    @BindView(R.id.hot_search)
    TagFlowLayout tagFlowLayout;
    @BindView(R.id.content)
    EditText contentET;
    @BindView(R.id.clean)
    View cleanV;
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.type)
    TextView searchTypeV;
    @BindView(R.id.history)
    RecyclerView historyRV;
    @BindView(R.id.hot_layout)
    View hotLayout;
    @Inject
    TagAdapter adapter;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.Adapter historyAdapter;
    @Inject
    SearchTypeAdapter typeAdapter;

    private PopupWindow popupWindow;


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerSearchComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .searchModule(new SearchModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_search; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        ArmsUtils.configRecyclerView(historyRV, mLayoutManager);
        tagFlowLayout.setAdapter(adapter);
        historyRV.setAdapter(historyAdapter);
        searchTypeV.setOnClickListener(this);
        backV.setOnClickListener(this);
        ((HistoryAdapter) historyAdapter).setOnItemClickListener(this);
        cleanV.setOnClickListener(this);
        contentET.setOnEditorActionListener(this);
        mPresenter.getHot("110000", "110000", "110000");
        mPresenter.getCategory();
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
    public boolean onTagClick(View view, int position, FlowLayout parent) {
        return false;
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void dimissHotLayout() {
        hotLayout.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clean:
                break;
            case R.id.type:
                showType();
                break;
            case R.id.back:
                killMyself();
                break;
        }
    }

    @Override
    public void onItemClick(View view, int viewType, Object data, int position) {
        mPresenter.goSearch("", (String) data);
    }

    private void showType() {

        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }

        RecyclerView recyclerView = new RecyclerView(this);
        ArmsUtils.configRecyclerView(recyclerView, new LinearLayoutManager(this));
        recyclerView.setAdapter(typeAdapter);
        typeAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {
                searchTypeV.setText(((Category) data).getName());
                if (popupWindow != null) {
                    popupWindow.dismiss();
                    popupWindow = null;
                }
            }
        });
        popupWindow = new PopupWindow(recyclerView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(searchTypeV, 0, 0);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_ACTION_SEARCH:
                return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DefaultAdapter.releaseAllHolder(historyRV);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
    }
}
