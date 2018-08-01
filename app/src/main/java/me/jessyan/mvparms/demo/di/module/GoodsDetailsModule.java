package me.jessyan.mvparms.demo.di.module;

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
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsDetailsResponse;


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
    TagAdapter provideSerachAdapter(List<String> list) {
        return new TagAdapter<String>(list) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(ArmsUtils.getContext()).inflate(R.layout.search_hot_item, null, false);
                tv.setText(s);
                return tv;
            }
        };
    }

    @ActivityScope
    @Provides
    List<String> provideHotList() {
        return new ArrayList<>();
    }


}