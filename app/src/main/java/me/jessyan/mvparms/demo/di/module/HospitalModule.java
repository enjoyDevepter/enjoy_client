package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.HospitalContract;
import me.jessyan.mvparms.demo.mvp.model.HospitalModel;
import me.jessyan.mvparms.demo.mvp.model.entity.Hospital;
import me.jessyan.mvparms.demo.mvp.ui.adapter.HospitalListAdapter;


@Module
public class HospitalModule {
    private HospitalContract.View view;

    /**
     * 构建HospitalModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public HospitalModule(HospitalContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    HospitalContract.View provideHospitalView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    HospitalContract.Model provideHospitalModel(HospitalModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<Hospital> provideDiariesList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    HospitalListAdapter provideGoodsListAdapter(List<Hospital> diaries) {
        return new HospitalListAdapter(diaries);
    }

}