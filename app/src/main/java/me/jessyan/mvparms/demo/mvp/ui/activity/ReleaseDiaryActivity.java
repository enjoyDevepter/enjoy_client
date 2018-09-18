package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.cchao.MoneyView;
import com.guoqi.actionsheet.ActionSheet;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.base.DefaultAdapter;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.PermissionUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.simple.eventbus.Subscriber;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindDimen;
import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.app.utils.ImageUploadUtils;
import me.jessyan.mvparms.demo.di.component.DaggerReleaseDiaryComponent;
import me.jessyan.mvparms.demo.di.module.ReleaseDiaryModule;
import me.jessyan.mvparms.demo.mvp.contract.ReleaseDiaryContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Goods;
import me.jessyan.mvparms.demo.mvp.model.entity.response.GoodsListResponse;
import me.jessyan.mvparms.demo.mvp.presenter.ReleaseDiaryPresenter;
import me.jessyan.mvparms.demo.mvp.ui.widget.SpacesItemDecoration;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class ReleaseDiaryActivity extends BaseActivity<ReleaseDiaryPresenter> implements ReleaseDiaryContract.View, View.OnClickListener, ActionSheet.OnActionSheetSelected {
    private static final int GALLERY_OPEN_REQUEST_CODE = 1;
    private static final int CROP_IMAGE_REQUEST_CODE = 2;
    private static final int CAMERA_OPEN_REQUEST_CODE = 3;
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.add)
    View addV;
    @BindView(R.id.choice)
    View choiceV;
    @BindView(R.id.submit)
    View submitV;
    @BindView(R.id.name)
    TextView nameTV;
    @BindView(R.id.image)
    ImageView imageIV;
    @BindView(R.id.price)
    MoneyView priceMV;
    @BindView(R.id.content)
    EditText contentET;
    @BindView(R.id.images)
    RecyclerView imagesRV;
    @BindView(R.id.diary_title)
    EditText titleET;
    @BindDimen(R.dimen.image_upload_width)
    int imageWdith;
    @BindDimen(R.dimen.image_upload_height)
    int imageHeight;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    RecyclerView.LayoutManager mLayoutManager;
    @Inject
    RecyclerView.Adapter mAdapter;
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    RxPermissions mRxPermissions;
    @Inject
    List<String> images;
    private String mCameraFilePath = "";
    private String mCropImgFilePath = "";

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerReleaseDiaryComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .releaseDiaryModule(new ReleaseDiaryModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_release_diary; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titleTV.setText("发表日志");
        addV.setOnClickListener(this);
        backV.setOnClickListener(this);
        choiceV.setOnClickListener(this);
        submitV.setOnClickListener(this);
        ArmsUtils.configRecyclerView(imagesRV, mLayoutManager);
        imagesRV.addItemDecoration(new SpacesItemDecoration(ArmsUtils.getDimens(ArmsUtils.getContext(), R.dimen.plus_width), 0));
        imagesRV.setAdapter(mAdapter);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
            case R.id.choice:
                ArmsUtils.startActivity(ChoiceProjectForDiaryActivity.class);
                break;
            case R.id.add:
                ActionSheet.showSheet(this, this, null);
                break;
            case R.id.submit:
                provideCache().put("title", titleET.getText().toString());
                provideCache().put("content", contentET.getText().toString());
                mPresenter.relsaseDiary();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        DefaultAdapter.releaseAllHolder(imagesRV);//super.onDestroy()之后会unbind,所有view被置为null,所以必须在之前调用
        super.onDestroy();

    }


    @Override
    public void onClick(int whichButton) {
        switch (whichButton) {
            case ActionSheet.CHOOSE_PICTURE:
                //相册
                openAlbum();
                break;
            case ActionSheet.TAKE_PICTURE:
                //拍照
                openCamera();
                break;
            case ActionSheet.CANCEL:
                //取消
                break;
        }
    }


    private void openAlbum() {
        //请求外部存储权限用于适配android6.0的权限管理机制
        PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                //request permission success, do something.
            }

            @Override
            public void onRequestPermissionFailure(List<String> permissions) {
            }

            @Override
            public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
            }
        }, mRxPermissions, mErrorHandler);

        ImageUploadUtils.startGallery(this, GALLERY_OPEN_REQUEST_CODE);

    }

    private void openCamera() {
        //请求外部存储权限用于适配android6.0的权限管理机制
        PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                //request permission success, do something.
            }

            @Override
            public void onRequestPermissionFailure(List<String> permissions) {
            }

            @Override
            public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
            }
        }, mRxPermissions, mErrorHandler);
        ImageUploadUtils.startCamera(this, CAMERA_OPEN_REQUEST_CODE, generateCameraFilePath());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case GALLERY_OPEN_REQUEST_CODE:
                    if (data != null) {
                        String mGalleryPath = ImageUploadUtils.parseGalleryPath(this, data.getData());

                        BitmapFactory.Options mOptions = getBitampOptions(mGalleryPath);
                        generateCropImgFilePath();
                        ImageUploadUtils.startCropImage(
                                this,
                                mGalleryPath,
                                mCropImgFilePath,
                                mOptions.outWidth,
                                mOptions.outHeight,
                                imageWdith,
                                imageHeight,
                                CROP_IMAGE_REQUEST_CODE);
                    }
                    break;
                case CROP_IMAGE_REQUEST_CODE:
                    provideCache().put("imagePath", mCropImgFilePath);
                    mPresenter.uploadImage();
                    images.add(mCropImgFilePath);
                    mAdapter.notifyDataSetChanged();
                    break;
                case CAMERA_OPEN_REQUEST_CODE:
                    if (data == null || data.getExtras() == null) {
                        BitmapFactory.Options mOptions = getBitampOptions(mCameraFilePath);
                        generateCropImgFilePath();
                        ImageUploadUtils.startCropImage(
                                this,
                                mCameraFilePath,
                                mCropImgFilePath,
                                mOptions.outWidth,
                                mOptions.outHeight,
                                imageWdith,
                                imageHeight,
                                CROP_IMAGE_REQUEST_CODE);
                    }
                    break;
            }
        }
    }

    private BitmapFactory.Options getBitampOptions(String path) {
        BitmapFactory.Options mOptions = new BitmapFactory.Options();
        mOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, mOptions);
        return mOptions;
    }

    private String generateCameraFilePath() {
        String mCameraFileDirPath = Environment.getExternalStorageDirectory() + File.separator + "camera";
        File mCameraFileDir = new File(mCameraFileDirPath);
        if (!mCameraFileDir.exists()) {
            mCameraFileDir.mkdirs();
        }
        mCameraFilePath = mCameraFileDirPath + File.separator + System.currentTimeMillis() + ".jgp";
        return mCameraFilePath;
    }

    private String generateCropImgFilePath() {
        String mCameraFileDirPath = Environment.getExternalStorageDirectory() + File.separator + "camera";
        File mCameraFileDir = new File(mCameraFileDirPath);
        if (!mCameraFileDir.exists()) {
            mCameraFileDir.mkdirs();
        }
        mCropImgFilePath = mCameraFileDirPath + File.separator + System.currentTimeMillis() + ".jgp";
        return mCropImgFilePath;
    }

    @Override
    public Cache getCache() {
        return provideCache();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    @Subscriber(tag = EventBusTags.CHANGE_DIRAY_PROJECT)
    public void updateProject(GoodsListResponse response) {
        if (null != response.getGoodsList() && response.getGoodsList().size() > 0) {
            Goods goods = response.getGoodsList().get(0);
            provideCache().put("goodsId", goods.getGoodsId());
            provideCache().put("merchId", goods.getMerchId());
            mImageLoader.loadImage(this,
                    ImageConfigImpl
                            .builder()
                            .placeholder(R.drawable.place_holder_img)
                            .url(goods.getImage())
                            .imageView(imageIV)
                            .build());
            nameTV.setText(goods.getName());
            priceMV.setMoneyText(String.valueOf(goods.getSalePrice()));
        }
    }
}
