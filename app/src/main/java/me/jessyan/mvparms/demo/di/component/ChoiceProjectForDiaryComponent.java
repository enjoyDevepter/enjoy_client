package me.jessyan.mvparms.demo.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;

import dagger.Component;
import me.jessyan.mvparms.demo.di.module.ChoiceProjectForDiaryModule;
import me.jessyan.mvparms.demo.mvp.ui.activity.ChoiceProjectForDiaryActivity;

@ActivityScope
@Component(modules = ChoiceProjectForDiaryModule.class, dependencies = AppComponent.class)
public interface ChoiceProjectForDiaryComponent {
    void inject(ChoiceProjectForDiaryActivity activity);
}