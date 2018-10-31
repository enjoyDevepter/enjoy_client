package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.RxLifecycleUtils;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.MessageContract;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.CommentMessage;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.DynamicMessage;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.NoticeMessage;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.PraiseMessage;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.PrivateMessage;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.MessageDetailRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.MessageRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.MessageDetailResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.MessageResponse;
import me.jessyan.mvparms.demo.mvp.ui.adapter.CommentMessageAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DynamicMessageAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.NoticeMessageAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.PraiseMessageAdapter;
import me.jessyan.mvparms.demo.mvp.ui.adapter.PrivateMessageAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class MessagePresenter extends BasePresenter<MessageContract.Model, MessageContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<PrivateMessage> privateMessageList;
    @Inject
    List<NoticeMessage> noticeMessageList;
    @Inject
    List<DynamicMessage> dynamicMessageList;
    @Inject
    List<CommentMessage> commentMessageList;
    @Inject
    List<PraiseMessage> praiseMessageList;
    @Inject
    PrivateMessageAdapter pMAdapter;
    @Inject
    NoticeMessageAdapter nAdapter;
    @Inject
    PraiseMessageAdapter pAdapter;
    @Inject
    DynamicMessageAdapter dAdapter;
    @Inject
    CommentMessageAdapter cAdapter;

    private int preEndIndex;
    private int lastPageIndex = 1;

    @Inject
    public MessagePresenter(MessageContract.Model model, MessageContract.View rootView) {
        super(model, rootView);
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    void onCreate() {
        getMessage(true);
    }

    public void getMessage(boolean pullToRefresh) {
        MessageRequest request = new MessageRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        int type = 0;
        if (null != mRootView.getCache().get("type")) {
            type = (int) mRootView.getCache().get("type");
        }
        switch (type) {
            case 0:
                request.setCmd(925);
                break;
            case 1:
                request.setCmd(928);
                break;
            case 2:
                request.setCmd(930);
                break;
            case 3:
                request.setCmd(934);
                break;
            case 4:
                request.setCmd(936);
                break;
        }

        if (pullToRefresh) lastPageIndex = 1;
        request.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页

        mModel.getMessage(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    if (pullToRefresh)
                        mRootView.showLoading();//显示下拉刷新的进度条
                    else
                        mRootView.startLoadMore();//显示上拉加载更多的进度条
                })
                .doFinally(() -> {
                    if (pullToRefresh)
                        mRootView.hideLoading();//隐藏下拉刷新的进度条
                    else
                        mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                })
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<MessageResponse>(mErrorHandler) {
                    @Override
                    public void onNext(MessageResponse response) {
                        if (response.isSuccess()) {
                            if (pullToRefresh) {
                                privateMessageList.clear();
                                noticeMessageList.clear();
                                dynamicMessageList.clear();
                                commentMessageList.clear();
                                praiseMessageList.clear();
                            }
                            int typeC = (int) mRootView.getCache().get("type");
                            switch (typeC) {
                                case 0:
                                    privateMessageList.addAll(response.getPrivateMessageList());
                                    mRootView.showConent(privateMessageList.size() > 0);
                                    mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                                    preEndIndex = privateMessageList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                                    lastPageIndex = privateMessageList.size() / 10 + 1;
                                    break;
                                case 1:
                                    noticeMessageList.addAll(response.getNoticeList());
                                    mRootView.showConent(noticeMessageList.size() > 0);
                                    mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                                    preEndIndex = noticeMessageList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                                    lastPageIndex = noticeMessageList.size() / 10 + 1;
                                    break;
                                case 2:
                                    dynamicMessageList.addAll(response.getDynamicList());
                                    mRootView.showConent(dynamicMessageList.size() > 0);
                                    mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                                    preEndIndex = dynamicMessageList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                                    lastPageIndex = dynamicMessageList.size() / 10 + 1;
                                    break;
                                case 3:
                                    commentMessageList.addAll(response.getDiaryCommentList());
                                    mRootView.showConent(commentMessageList.size() > 0);
                                    mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                                    preEndIndex = commentMessageList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                                    lastPageIndex = commentMessageList.size() / 10 + 1;
                                    break;
                                case 4:
                                    praiseMessageList.addAll(response.getDiaryPraiseList());
                                    mRootView.showConent(privateMessageList.size() > 0);
                                    mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                                    preEndIndex = praiseMessageList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                                    lastPageIndex = praiseMessageList.size() / 10 + 1;
                                    break;
                            }
                            if (pullToRefresh) {
                                pMAdapter.notifyDataSetChanged();
                                nAdapter.notifyDataSetChanged();
                                pAdapter.notifyDataSetChanged();
                                dAdapter.notifyDataSetChanged();
                                cAdapter.notifyDataSetChanged();
                            } else {
                                pMAdapter.notifyItemRangeInserted(preEndIndex, privateMessageList.size());
                                nAdapter.notifyItemRangeInserted(preEndIndex, noticeMessageList.size());
                                pAdapter.notifyItemRangeInserted(preEndIndex, dynamicMessageList.size());
                                dAdapter.notifyItemRangeInserted(preEndIndex, commentMessageList.size());
                                cAdapter.notifyItemRangeInserted(preEndIndex, praiseMessageList.size());
                            }
                        }
                    }
                });

    }

    public void getNoticeDetail() {
        MessageDetailRequest request = new MessageDetailRequest();
        int type = 0;
        if (null != mRootView.getCache().get("type")) {
            type = (int) mRootView.getCache().get("type");
        }
        switch (type) {
            case 0:
                request.setCmd(926);
                request.setPrivateMessageId((String) mRootView.getCache().get("privateMessageId"));
                break;
            case 1:
                request.setCmd(929);
                request.setNoticeId((String) mRootView.getCache().get("noticeId"));
                break;
            case 2:
                request.setCmd(931);
                request.setDynamicId((String) mRootView.getCache().get("dynamicId"));
                break;
        }
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        mModel.getNoticeDetail(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<MessageDetailResponse>(mErrorHandler) {
                    @Override
                    public void onNext(MessageDetailResponse response) {
                        if (response.isSuccess()) {
                            int typeC = (int) mRootView.getCache().get("type");
                            switch (typeC) {
                                case 0:
                                    mRootView.showPrivateDetail(response.getPrivateMessage());
                                    break;
                                case 1:
                                    mRootView.showNotice(response.getNotice());
                                    break;
                                case 2:
                                    mRootView.showDynamic(response.getDynamic());
                                    break;
                            }
                        }
                    }
                });
    }


    public void reply() {
        MessageDetailRequest request = new MessageDetailRequest();
        request.setCmd(927);
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        request.setPrivateMessageId((String) mRootView.getCache().get("privateMessageId"));
        request.setContent((String) mRootView.getCache().get("reply"));
        mModel.reply(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.isSuccess()) {
                            getNoticeDetail();
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mImageLoader = null;
        this.mApplication = null;
    }

}
