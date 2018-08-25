package me.jessyan.mvparms.demo.mvp.presenter;

import android.app.Application;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.integration.AppManager;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.ArmsUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.jessyan.mvparms.demo.mvp.contract.ReleaseDiaryContract;
import me.jessyan.mvparms.demo.mvp.model.entity.DiaryBean;
import me.jessyan.mvparms.demo.mvp.model.entity.request.ReleaseDiaryRequest;
import me.jessyan.mvparms.demo.mvp.model.entity.response.BaseResponse;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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
        diary.setGoodsId(mRootView.getActivity().getIntent().getStringExtra("goodsId"));
        diary.setMerchId(mRootView.getActivity().getIntent().getStringExtra("merchId"));
        diary.setOrderId(mRootView.getActivity().getIntent().getStringExtra("orderId"));
        request.setDiary(diary);
        mModel.releaseDiary(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse response) throws Exception {
                        if (response.isSuccess()) {
                            mRootView.showMessage("发表成功");
                        } else {
                            mRootView.showMessage(response.getRetDesc());
                        }
                    }
                });


    }

    public void uploadImage() {

        File file = new File((String) mRootView.getCache().get("imagePath"));
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        mModel.uploadImage(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse response) throws Exception {
                        if (response.isSuccess()) {
                            images.add(response.getResult().getUrl());
                        } else {
                            mRootView.showMessage(response.getRetDesc());
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
