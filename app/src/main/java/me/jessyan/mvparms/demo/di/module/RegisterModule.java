package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.RegisterContract;
import me.jessyan.mvparms.demo.mvp.model.RegisterModel;
import me.jessyan.mvparms.demo.mvp.ui.adapter.CodeAdapter;


@Module
public class RegisterModule {
    private RegisterContract.View view;

    /**
     * 构建RegisterModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public RegisterModule(RegisterContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    RegisterContract.View provideRegisterView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    RxPermissions provideRxPermissions() {
        return new RxPermissions(view.getActivity());
    }

    @ActivityScope
    @Provides
    RegisterContract.Model provideRegisterModel(RegisterModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    List<String> provideCodeList() {
        List<String> codes = new ArrayList<>();
        codes.add("请选择");
        codes.add("邀请码");
        codes.add("邀请人");
        return codes;
    }

    @ActivityScope
    @Provides
    RecyclerView.Adapter provideCodeAdapter(List<String> list) {
        return new CodeAdapter(list);
    }
}