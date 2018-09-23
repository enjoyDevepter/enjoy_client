package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.guoqi.actionsheet.ActionSheet;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.PermissionUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.utils.ImageUploadUtils;
import me.jessyan.mvparms.demo.di.component.DaggerAuthenticationComponent;
import me.jessyan.mvparms.demo.di.module.AuthenticationModule;
import me.jessyan.mvparms.demo.mvp.contract.AuthenticationContract;
import me.jessyan.mvparms.demo.mvp.presenter.AuthenticationPresenter;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class AuthenticationActivity extends BaseActivity<AuthenticationPresenter> implements AuthenticationContract.View, View.OnClickListener, ActionSheet.OnActionSheetSelected {
    private static final int GALLERY_OPEN_REQUEST_CODE = 1;
    private static final int CROP_IMAGE_REQUEST_CODE = 2;
    private static final int CAMERA_OPEN_REQUEST_CODE = 3;
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.name)
    EditText nameET;
    @BindView(R.id.card)
    EditText cardET;
    @BindView(R.id.submit)
    View submitV;
    @BindView(R.id.opposite)
    ImageView oppositeV;
    @BindView(R.id.front)
    ImageView frontV;
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    RxPermissions mRxPermissions;
    private String mCameraFilePath = "";
    private String mCropImgFilePath = "";

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerAuthenticationComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .authenticationModule(new AuthenticationModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_authentication; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titleTV.setText("实名认证");
        backV.setOnClickListener(this);
        submitV.setOnClickListener(this);
        oppositeV.setOnClickListener(this);
        frontV.setOnClickListener(this);
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
            case R.id.submit:
                provideCache().put("realName", nameET.getText().toString());
                provideCache().put("idCard", cardET.getText().toString());
                mPresenter.auth();
                break;
            case R.id.front:
                provideCache().put("isFront", true);
                ActionSheet.showSheet(this, this, null);
                break;
            case R.id.opposite:
                provideCache().put("isFront", false);
                ActionSheet.showSheet(this, this, null);
                break;
        }
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
                                frontV.getWidth(),
                                frontV.getHeight(),
                                CROP_IMAGE_REQUEST_CODE);
                    }
                    break;
                case CROP_IMAGE_REQUEST_CODE:
                    provideCache().put("imagePath", mCropImgFilePath);
                    mPresenter.uploadImage();
                    if (((boolean) provideCache().get("isFront"))) {
                        frontV.setImageBitmap(BitmapFactory.decodeFile(mCropImgFilePath));
                    } else {
                        oppositeV.setImageBitmap(BitmapFactory.decodeFile(mCropImgFilePath));
                    }
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
                                frontV.getWidth(),
                                frontV.getHeight(),
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
}
