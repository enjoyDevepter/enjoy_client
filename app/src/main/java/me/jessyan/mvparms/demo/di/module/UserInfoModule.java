package me.jessyan.mvparms.demo.di.module;

import com.jess.arms.di.scope.ActivityScope;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.UserInfoContract;
import me.jessyan.mvparms.demo.mvp.model.UserInfoModel;
import me.jessyan.mvparms.demo.mvp.model.entity.AreaAddress;


@Module
public class UserInfoModule {
    private UserInfoContract.View view;

    /**
     * 构建UserInfoModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public UserInfoModule(UserInfoContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    UserInfoContract.View provideUserInfoView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    UserInfoContract.Model provideUserInfoModel(UserInfoModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RxPermissions provideRxPermissions() {
        return new RxPermissions(view.getActivity());
    }

    @ActivityScope
    @Provides
    List<AreaAddress> provideAddressList() {
        return new ArrayList<>();
    }
}

