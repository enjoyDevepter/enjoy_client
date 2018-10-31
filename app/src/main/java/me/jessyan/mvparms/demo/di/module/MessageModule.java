package me.jessyan.mvparms.demo.di.module;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jess.arms.di.scope.ActivityScope;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;
import me.jessyan.mvparms.demo.mvp.contract.MessageContract;
import me.jessyan.mvparms.demo.mvp.model.MessageModel;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.CommentMessage;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.DynamicMessage;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.NoticeMessage;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.PraiseMessage;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.PrivateMessage;
import me.jessyan.mvparms.demo.mvp.ui.adapter.CommentMessageAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DynamicMessageAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.NoticeMessageAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.PraiseMessageAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.PrivateMessageAdapter;


@Module
public class MessageModule {
    private MessageContract.View view;

    /**
     * 构建MessageModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public MessageModule(MessageContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    MessageContract.View provideMessageView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    MessageContract.Model provideMessageModel(MessageModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    RecyclerView.LayoutManager provideLayoutManager() {
        return new LinearLayoutManager(view.getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    @ActivityScope
    @Provides
    List<PrivateMessage> providePrivateMessageList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    List<NoticeMessage> provideNoticeMessageList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    List<DynamicMessage> provideDynamicMessageList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    List<CommentMessage> provideCommentMessageList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    List<PraiseMessage> providePraiseMessageList() {
        return new ArrayList<>();
    }

    @ActivityScope
    @Provides
    PrivateMessageAdapter provideCommentListAdapter(List<PrivateMessage> members) {
        return new PrivateMessageAdapter(members);
    }

    @ActivityScope
    @Provides
    NoticeMessageAdapter provideHospitalListAdapter(List<NoticeMessage> members) {
        return new NoticeMessageAdapter(members);
    }

    @ActivityScope
    @Provides
    DynamicMessageAdapter provideDynamicMessageAdapter(List<DynamicMessage> members) {
        return new DynamicMessageAdapter(members);
    }

    @ActivityScope
    @Provides
    CommentMessageAdapter provideCommentMessageAdapter(List<CommentMessage> members) {
        return new CommentMessageAdapter(members);
    }

    @ActivityScope
    @Provides
    PraiseMessageAdapter providePraiseMessageAdapter(List<PraiseMessage> members) {
        return new PraiseMessageAdapter(members);
    }

}