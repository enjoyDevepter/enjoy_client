package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.utils.ArmsUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.mvp.contract.GoodsDetailsContract;
import me.jessyan.mvparms.demo.mvp.model.GoodsDetailsModel;
import me.jessyan.mvparms.demo.mvp.model.entity.GoodsDetails;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsDetailsResponse;
import me.jessyan.mvparms.demo.mvp.ui.adapter.GoodsPromotionAdapter;


@Module
public class GoodsDetailsModule {
    private GoodsDetailsContract.View view;

    /**
     * 构建GoodsDetailsModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public GoodsDetailsModule(GoodsDetailsContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    GoodsDetailsContract.View provideGoodsDetailsView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    GoodsDetailsContract.Model provideGoodsDetailsModel(GoodsDetailsModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    GoodsDetailsResponse provideGoodsDetailsResponse() {
        return new GoodsDetailsResponse();
    }


    @ActivityScope
    @Provides
    TagAdapter provideSerachAdapter(List<GoodsDetails.GoodsSpecValue> list) {
        return new TagAdapter<GoodsDetails.GoodsSpecValue>(list) {
            @Override
            public View getView(FlowLayout parent, int position, GoodsDetails.GoodsSpecValue s) {
                TextView tv = (TextView) LayoutInflater.from(ArmsUtils.getContext()).inflate(R.layout.goods_spec_item, null, false);
                tv.setText(s.getSpecValueName());
                return tv;
            }
        };
    }

    @ActivityScope
    @Provides
    List<GoodsDetails.GoodsSpecValue> provideGoodSpecList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<GoodsDetails.Promotion> providePromotionList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    GoodsPromotionAdapter providePromotionAdapter(List<GoodsDetails.Promotion> list) {
        return new GoodsPromotionAdapter(list);
    }


}