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
import me.jessyan.mvparms.demo.mvp.contract.DiaryForGoodsContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Diary;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryVoteRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.FollowMemberRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.user.request.MyDiaryRequest;
import me.jessyan.mvparms.demo.mvp.ui.activity.LoginActivity;
import me.jessyan.mvparms.demo.mvp.ui.adapter.MyDiaryListAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class DiaryForGoodsPresenter extends BasePresenter<DiaryForGoodsContract.Model, DiaryForGoodsContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    List<Diary> diaryList;
    @Inject
    MyDiaryListAdapter mAdapter;

    private int preEndIndex;
    private int lastPageIndex = 1;

    @Inject
    public DiaryForGoodsPresenter(DiaryForGoodsContract.Model model, DiaryForGoodsContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {
        getDiary();
        getMyDiaryList();
    }

    public void getMyDiaryList() {
        MyDiaryRequest request = new MyDiaryRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        String token = (String) (cache.get(KEY_KEEP + "token"));
        if (ArmsUtils.isEmpty(token)) {
            request.setCmd(816);
        } else {
            request.setCmd(806);
        }
        request.setToken(token);
        request.setPageIndex(lastPageIndex);
        request.setMemberId(mRootView.getActivity().getIntent().getStringExtra("memberId"));
        request.setGoodsId(mRootView.getActivity().getIntent().getStringExtra("goodsId"));
        request.setMerchId(mRootView.getActivity().getIntent().getStringExtra("merchId"));

        mModel.getMyDiaryList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    mRootView.startLoadMore();//显示上拉加载更多的进度条
                })
                .doFinally(() -> {
                    mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                })
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<DiaryListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(DiaryListResponse response) {
                        if (response.isSuccess()) {
                            mRootView.setLoadedAllItems(response.getNextPageIndex() == -1);
                            diaryList.addAll(response.getDiaryList());
                            preEndIndex = diaryList.size();//更新之前列表总长度,用于确定加载更多的起始位置
                            lastPageIndex = diaryList.size() / 10;
                            if (lastPageIndex == 1) {
                                mAdapter.notifyDataSetChanged();
                            } else {
                                mAdapter.notifyItemRangeInserted(preEndIndex, diaryList.size());
                            }
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }


    private void getDiary() {
        DiaryRequest request = new DiaryRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        String token = (String) (cache.get(KEY_KEEP + "token"));
        if (ArmsUtils.isEmpty(token)) {
            request.setCmd(808);
        } else {
            request.setCmd(818);
        }
        request.setToken(token);
        request.setDiaryId(mRootView.getActivity().getIntent().getStringExtra("diaryId"));
        request.setGoodsId(mRootView.getActivity().getIntent().getStringExtra("goodsId"));
        request.setMerchId(mRootView.getActivity().getIntent().getStringExtra("merchId"));

        mModel.getDiary(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<DiaryResponse>(mErrorHandler) {
                    @Override
                    public void onNext(DiaryResponse response) {
                        if (response.isSuccess()) {
                            mRootView.updateUI(response);
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    public void getDiaryForGoods() {


    }


    public void vote(boolean vote, int position) {
        if (checkLoginStatus()) {
            ArmsUtils.startActivity(LoginActivity.class);
            return;
        }
        DiaryVoteRequest request = new DiaryVoteRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        request.setDiaryId((String) mRootView.getCache().get("diaryId"));
        request.setCmd(vote ? 811 : 812);
        mModel.diaryVote(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.isSuccess()) {
                            diaryList.get(position).setIsPraise(vote ? "1" : "0");
                            int num = diaryList.get(position).getPraise();
                            diaryList.get(position).setPraise(vote ? num + 1 : num <= 0 ? 0 : num - 1);
                            mAdapter.notifyItemChanged(position);
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }


    public void follow(boolean follow) {
        if (checkLoginStatus()) {
            ArmsUtils.startActivity(LoginActivity.class);
            return;
        }
        FollowMemberRequest request = new FollowMemberRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        request.setMemberId((String) mRootView.getCache().get("memberId"));
        request.setCmd(follow ? 201 : 211);
        mModel.follow(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.isSuccess()) {
                            mRootView.updateFollow(follow);
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
    }

    private boolean checkLoginStatus() {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        String token = (String) (cache.get(KEY_KEEP + "token"));
        return ArmsUtils.isEmpty(token);
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
