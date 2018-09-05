package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.yu.bundles.extended.recyclerview.ExtendedHolder;
import com.yu.bundles.extended.recyclerview.ExtendedHolderFactory;
import com.yu.bundles.extended.recyclerview.ExtendedNode;
import com.yu.bundles.extended.recyclerview.ExtendedRecyclerViewBuilder;
import com.yu.bundles.extended.recyclerview.ExtendedRecyclerViewHelper;

import org.simple.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.di.component.DaggerCityComponent;
import me.jessyan.mvparms.demo.di.module.CityModule;
import me.jessyan.mvparms.demo.mvp.contract.CityContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Area;
import me.jessyan.mvparms.demo.mvp.presenter.CityPresenter;
import me.jessyan.mvparms.demo.mvp.ui.holder.CityItemHolder;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class CityActivity extends BaseActivity<CityPresenter> implements CityContract.View, View.OnClickListener {
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.location_info)
    TextView locationInfoTV;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerCityComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .cityModule(new CityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_city; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        backV.setOnClickListener(this);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(this).extras();
        locationInfoTV.setText((String) cache.get("current_location_info"));
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
        }
    }

    @Override
    public void onRefresh(List<ExtendedNode> nodeList) {
        CityItemHolder.OnChoiceListener onChoiceListener = new CityItemHolder.OnChoiceListener() {
            @Override
            public void onChoice(Area area) {
                Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(getApplication()).extras();
                switch (Integer.valueOf(area.getType())) {
                    case 2: // province
                        cache.put("province", area.getId());
                        break;
                    case 3: // city
                        cache.put("city", area.getCode());
                        for (ExtendedNode<Area> node : nodeList) {
                            if (area.getParentId().equals(node.data.getId())) {
                                cache.put("province", node.data.getId());
                                break;
                            }
                        }
                        break;
                    case 4: // county
                        cache.put("county", area.getId());
                        for (ExtendedNode<Area> node : nodeList) {
                            for (ExtendedNode<Area> child : node.getSons()) {
                                if (area.getParentId().equals(child.data.getId())) {
                                    cache.put("city", child.data.getId());
                                    cache.put("province", node.data.getId());
                                    break;
                                }
                            }
                        }
                        break;
                }
                killMyself();
                EventBus.getDefault().post(area, EventBusTags.CITY_CHANGE_EVENT);
            }
        };
        ExtendedRecyclerViewHelper extendedRecyclerViewHelper = ExtendedRecyclerViewBuilder.build(recyclerView)
                .init(nodeList, new ExtendedHolderFactory() {
                    @Override
                    public ExtendedHolder getHolder(ExtendedRecyclerViewHelper helper, ViewGroup parent, int viewType) {
                        switch (viewType) {
                            case 0:
                                return new CityItemHolder(helper, LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item_1, parent, false), onChoiceListener);
                            case 1:
                                return new CityItemHolder(helper, LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item_2, parent, false), onChoiceListener);
                            case 2:
                                return new CityItemHolder(helper, LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item_3, parent, false), onChoiceListener);
                        }
                        return null;
                    }
                })
                .setEnableExtended(true)
                .complete();
    }

    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(recyclerView);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();
    }
}
