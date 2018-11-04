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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.app.utils.ImageUploadManager;
import me.jessyan.mvparms.demo.mvp.contract.ReleaseDiaryContract;
import me.jessyan.mvparms.demo.mvp.model.entity.DiaryBean;
import me.jessyan.mvparms.demo.mvp.model.entity.QiniuRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.QiniuResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.diary.ProjectRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ReleaseDiaryRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.mvparms.demo.mvp.model.entity.response.DirayProjectListResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import me.jessyan.rxerrorhandler.handler.ErrorHandleSubscriber;
import me.jessyan.rxerrorhandler.handler.RetryWithDelay;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;


@ActivityScope
public class ReleaseDiaryPresenter extends BasePresenter<ReleaseDiaryContract.Model, ReleaseDiaryContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;
    @Inject
    ImageLoader mImageLoader;
    List<String> images = new ArrayList<>();

    @Inject
    public ReleaseDiaryPresenter(ReleaseDiaryContract.Model model, ReleaseDiaryContract.View rootView) {
        super(model, rootView);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void getFirstProject() {
        getProject();
    }

    public void getProject() {
        ProjectRequest request = new ProjectRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mRootView.getActivity()).extras();
        String token = (String) cache.get(KEY_KEEP + "token");
        request.setToken(token);
        request.setOrderId(mRootView.getActivity().getIntent().getStringExtra("orderId"));

        mModel.getProjects(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<DirayProjectListResponse>(mErrorHandler) {
                    @Override
                    public void onNext(DirayProjectListResponse response) {
                        if (response.isSuccess()) {
                            mRootView.updateProject(response);
                        }
                    }
                });
    }


    public void relsaseDiary() {
        String title = (String) mRootView.getCache().get("title");
        String content = (String) mRootView.getCache().get("content");

        if (ArmsUtils.isEmpty(title)) {
            mRootView.showMessage("请输入日志标题");
            return;
        }
        if (ArmsUtils.isEmpty(content)) {
            mRootView.showMessage("请输入日志内容");
            return;
        }

        if (images.size() <= 0) {
            mRootView.showMessage("请上传图片");
            return;
        }

        ReleaseDiaryRequest request = new ReleaseDiaryRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        request.setToken((String) (cache.get(KEY_KEEP + "token")));
        DiaryBean diary = new DiaryBean();
        diary.setTitle(title);
        diary.setContent(content);
        diary.setImageList(images);
        diary.setGoodsId((String) mRootView.getCache().get("goodsId"));
        diary.setMerchId((String) mRootView.getCache().get("merchId"));
        diary.setOrderId((String) mRootView.getCache().get("orderId"));
        request.setDiary(diary);
        mModel.releaseDiary(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<BaseResponse>(mErrorHandler) {
                    @Override
                    public void onNext(BaseResponse response) {
                        if (response.isSuccess()) {
                            mRootView.clean();
                            images.clear();
                            mRootView.showMessage("发表成功");
                            mRootView.killMyself();
                        }
                    }
                });
    }


    public void uploadImage() {
        final File file = new File((String) mRootView.getCache().get("imagePath"));
        QiniuRequest request = new QiniuRequest();
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(mApplication).extras();
        String token = (String) cache.get(KEY_KEEP + "token");
        request.setToken(token);
        mModel.getQiniuInfo(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .retryWhen(new RetryWithDelay(3, 2))//遇到错误时重试,第一个参数为重试几次,第二个参数为重试的间隔
                .compose(RxLifecycleUtils.bindToLifecycle(mRootView))//使用 Rxlifecycle,使 Disposable 和 Activity 一起销毁
                .subscribe(new ErrorHandleSubscriber<QiniuResponse>(mErrorHandler) {
                    @Override
                    public void onNext(QiniuResponse response) {
                        if (response.isSuccess()) {
                            ImageUploadManager.getInstance().updateImage(file, response.getUploadToken(), response.getUrlPrefix(), new ImageUploadManager.ImageUploadResponse() {
                                @Override
                                public void onImageUploadOk(String url) {
                                    images.add(url);
                                }

                                @Override
                                public void onImageUploadError(String errorInfo) {
                                }
                            });
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
