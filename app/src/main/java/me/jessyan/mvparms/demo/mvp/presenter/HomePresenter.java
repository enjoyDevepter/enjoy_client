package me.jessyan.mvparms.demo.mvp.presenter;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.AppManager;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.PermissionUtil;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.HomeContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Diary;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryListRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.DiaryVoteRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.FollowMemberRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.HomeRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DiaryListResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.HomeResponse;
import me.jessyan.mvparms.demo.mvp.ui.activity.LoginActivity;
import me.jessyan.mvparms.demo.mvp.ui.adapter.DiaryListAdapter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class HomePresenter extends BasePresenter<HomeContract.Model, HomeContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    DiaryListAdapter mAdapter;
    @Inject
    List<Diary> diaryList;

    private int preEndIndex;
    private int lastPageIndex = 1;

    @Inject
    public HomePresenter(HomeContract.Model model, HomeContract.View rootView) {
        super(model, rootView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
    }


    public void updateHomeInfo() {

        //请求外部存储权限用于适配android6.0的权限管理机制

        PermissionUtil.readPhoneState(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                //request permission success, do something.
                HomeRequest request = new HomeRequest();
                Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
                String token = String.valueOf(cache.get(KEY_KEEP + "token"));
                if (ArmsUtils.isEmpty(token)) {
                    request.setCmd(301);
                } else {
                    request.setCmd(302);
                }
                request.setCity(String.valueOf(cache.get("city")));
                request.setCounty(String.valueOf(cache.get("county")));
                request.setProvince(String.valueOf(cache.get("province")));
                request.setToken(token);

                mModel.getHomeInfo(request)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> {
                            mRootView.showLoading();//显示下拉刷新的进度条
                        }).doFinally(() -> {
                    mRootView.hideLoading();//隐藏下拉刷新的进度条
                }).subscribe(new Consumer<HomeResponse>() {
                    @Override
                    public void accept(HomeResponse response) throws Exception {
                        if (response.isSuccess()) {
                            mRootView.refreshUI(response.getFirstNavList(), response.getCarouselList(), response.getModuleList(), response.getSecondNavList());
                            getRecommenDiaryList();
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });
            }

            @Override
            public void onRequestPermissionFailure(List<String> permissions) {
                mRootView.showMessage("Request permissions failure");
            }

            @Override
            public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                mRootView.showMessage("Need to go to the settings");
            }
        }, mRootView.getRxPermissions(), mErrorHandler);
    }


    public void getRecommenDiaryList() {

        DiaryListRequest request = new DiaryListRequest();

        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        String token = (String) (cache.get(KEY_KEEP + "token"));
        if (ArmsUtils.isEmpty(token)) {
            request.setCmd(803);
        } else {
            request.setCmd(813);
        }
        request.setToken(token);

        request.setPageIndex(lastPageIndex);//下拉刷新默认只请求第一页

        mModel.getDiaryList(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> {
                    mRootView.startLoadMore();//显示上拉加载更多的进度条
                })
                .doFinally(() -> {
                    mRootView.endLoadMore();//隐藏上拉加载更多的进度条
                }).subscribe(new Consumer<DiaryListResponse>() {
            @Override
            public void accept(DiaryListResponse response) throws Exception {
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

    public void vote(boolean vote, int position) {
        if (checkLoginStatus()) {
            ArmsUtils.startActivity(LoginActivity.class);
            return;
        }
        DiaryVoteRequest request = new DiaryVoteRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        request.setCmd(vote ? 811 : 812);
        request.setDiaryId((String) mRootView.getCache().get("diaryId"));
        mModel.diaryVote(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse response) throws Exception {
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

    public void follow(boolean follow, int position) {
        if (checkLoginStatus()) {
            ArmsUtils.startActivity(LoginActivity.class);
            return;
        }
        FollowMemberRequest request = new FollowMemberRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        request.setCmd(follow ? 201 : 211);
        request.setMemberId((String) mRootView.getCache().get("memberId"));
        mModel.follow(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse response) throws Exception {
                        if (response.isSuccess()) {
                            diaryList.get(position).getMember().setIsFollow(follow ? "1" : "0");
                            mAdapter.notifyItemChanged(position);
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
}
