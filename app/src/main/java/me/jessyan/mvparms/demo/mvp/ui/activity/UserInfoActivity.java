package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.guoqi.actionsheet.ActionSheet;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.jess.arms.utils.PermissionUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.simple.eventbus.Subscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.app.utils.ImageUploadUtils;
import me.jessyan.mvparms.demo.di.component.DaggerUserInfoComponent;
import me.jessyan.mvparms.demo.di.module.UserInfoModule;
import me.jessyan.mvparms.demo.mvp.contract.UserInfoContract;
import me.jessyan.mvparms.demo.mvp.model.MyModel;
import me.jessyan.mvparms.demo.mvp.model.entity.Area;
import me.jessyan.mvparms.demo.mvp.model.entity.AreaAddress;
import me.jessyan.mvparms.demo.mvp.model.entity.user.bean.Member;
import me.jessyan.mvparms.demo.mvp.presenter.UserInfoPresenter;
import me.jessyan.mvparms.demo.mvp.ui.widget.ShapeImageView;
import me.jessyan.rxerrorhandler.core.RxErrorHandler;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class UserInfoActivity extends BaseActivity<UserInfoPresenter> implements UserInfoContract.View, View.OnClickListener, ActionSheet.OnActionSheetSelected {
    private static final int GALLERY_OPEN_REQUEST_CODE = 1;
    private static final int CROP_IMAGE_REQUEST_CODE = 2;
    private static final int CAMERA_OPEN_REQUEST_CODE = 3;
    @BindView(R.id.back)
    View backV;
    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.image_layout)
    View imageV;
    @BindView(R.id.image)
    ShapeImageView imageTV;
    @BindView(R.id.nick_layout)
    View nickV;
    @BindView(R.id.nickName)
    TextView nickNameTV;
    @BindView(R.id.name_layout)
    View nameV;
    @BindView(R.id.name)
    TextView nameTV;
    @BindView(R.id.male_layout)
    View maleV;
    @BindView(R.id.male)
    TextView maleTV;
    @BindView(R.id.age_layout)
    View ageV;
    @BindView(R.id.age)
    TextView ageTV;
    @BindView(R.id.area_layout)
    View areaV;
    @BindView(R.id.area)
    TextView areaTV;
    @BindView(R.id.constellation_layout)
    View constellationV;
    @BindView(R.id.constellation)
    TextView constellationTV;
    @BindView(R.id.hobby_layout)
    View hobbyV;
    @BindView(R.id.hobby)
    TextView hobbyTV;
    @BindView(R.id.occupation_layout)
    View occupationV;
    @BindView(R.id.occupation)
    TextView occupationTV;
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    RxPermissions mRxPermissions;
    @Inject
    List<AreaAddress> addressList;
    @Inject
    ImageLoader mImageLoader;

    private String mCameraFilePath = "";
    private String mCropImgFilePath = "";
    private List<AreaAddress> options1Items = new ArrayList<>();
    private List<List<AreaAddress>> options2Items = new ArrayList<>();
    private List<List<List<AreaAddress>>> options3Items = new ArrayList<>();


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerUserInfoComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .userInfoModule(new UserInfoModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_user_info; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        titleTV.setText("个人信息");
        backV.setOnClickListener(this);
        imageV.setOnClickListener(this);
        maleV.setOnClickListener(this);
        ageV.setOnClickListener(this);
        areaV.setOnClickListener(this);
        constellationV.setOnClickListener(this);
        hobbyV.setOnClickListener(this);
        nameTV.setOnClickListener(this);
        occupationV.setOnClickListener(this);
        nickNameTV.setOnClickListener(this);

        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(ArmsUtils.getContext()).extras();
        Member member = (Member) cache.get(KEY_KEEP + MyModel.KEY_FOR_USER_INFO);
        mImageLoader.loadImage(this,
                ImageConfigImpl
                        .builder()
                        .placeholder(R.mipmap.place_holder_user)
                        .url(member.getHeadImage())
                        .imageView(imageTV)
                        .build());
        nameTV.setText(member.getRealName());
        nickNameTV.setText(member.getNickName() + "");
        maleTV.setText("0".equals(member.getSex()) ? "保密" : "1".equals(member.getSex()) ? "男" : "女");
        ageTV.setText(member.getAge() + "");
        Area city = member.getCity();
        if (city != null) {
            areaTV.setText(city.getName());
        }
        constellationTV.setText(member.getConstellationDesc());

        StringBuilder sb = new StringBuilder();
        if (member.getHobbyDescList() != null) {
            for (String string : member.getHobbyDescList()) {
                sb.append(string).append(" ");
            }
        }
        hobbyTV.setText(sb.toString());
        occupationTV.setText(member.getOccupationDesc());
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


    @Subscriber(tag = EventBusTags.USER_BASE_INFO_CHANGE)
    public void updateUserInfo(int type) {
        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(getApplication()).extras();
        switch (type) {
            case 1102:
                nameTV.setText((String) cache.get("label"));
                break;
            case 1104:
                maleTV.setText((String) cache.get("label"));
                break;
            case 1105:
                ageTV.setText(String.valueOf(cache.get("label")));
                break;
            case 1107:
                constellationTV.setText((String) cache.get("label"));
                break;
            case 1108:
                hobbyTV.setText((String) cache.get("label"));
                break;
            case 1109:
                occupationTV.setText((String) cache.get("label"));
                break;
            case 1111:
                nameTV.setText((String) cache.get("label"));
                break;
        }
        cache.put("lable", null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                killMyself();
                break;
            case R.id.image_layout:
                provideCache().put("type", 1);
                ActionSheet.showSheet(this, this, null);
                break;
            case R.id.nick_layout:
                Intent nickIntent = new Intent(getActivity(), ModifyUserInfoActivity.class);
                nickIntent.putExtra("type", "nick");
                nickIntent.putExtra("title", "更改昵称");
                ArmsUtils.startActivity(nickIntent);
                break;
            case R.id.name_layout:
                Intent nameIntent = new Intent(getActivity(), ModifyUserInfoActivity.class);
                nameIntent.putExtra("type", "name");
                nameIntent.putExtra("title", "更改姓名");
                ArmsUtils.startActivity(nameIntent);
                break;
            case R.id.male_layout:
                Intent maleIntent = new Intent(getActivity(), ModifyUserInfoActivity.class);
                maleIntent.putExtra("type", "male");
                maleIntent.putExtra("title", "更改性别");
                ArmsUtils.startActivity(maleIntent);
                break;
            case R.id.age_layout:
                Intent ageIntent = new Intent(getActivity(), ModifyUserInfoActivity.class);
                ageIntent.putExtra("type", "age");
                ageIntent.putExtra("title", "更改年龄");
                ArmsUtils.startActivity(ageIntent);
                break;
            case R.id.area_layout:
                provideCache().put("type", 2);
                showPickerView();
                break;
            case R.id.constellation_layout:
                Intent constellationIntent = new Intent(getActivity(), ModifyUserInfoActivity.class);
                constellationIntent.putExtra("type", "constellation");
                constellationIntent.putExtra("title", "更改星座");
                ArmsUtils.startActivity(constellationIntent);
                break;
            case R.id.hobby_layout:
                Intent hobbyIntent = new Intent(getActivity(), ModifyUserInfoActivity.class);
                hobbyIntent.putExtra("type", "hobby");
                hobbyIntent.putExtra("title", "更改爱好");
                ArmsUtils.startActivity(hobbyIntent);
                break;
            case R.id.occupation_layout:
                Intent occupationIntent = new Intent(getActivity(), ModifyUserInfoActivity.class);
                occupationIntent.putExtra("type", "occupation");
                occupationIntent.putExtra("title", "更改职业");
                ArmsUtils.startActivity(occupationIntent);
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
                                imageTV.getWidth(),
                                imageTV.getHeight(),
                                CROP_IMAGE_REQUEST_CODE);
                    }
                    break;
                case CROP_IMAGE_REQUEST_CODE:
                    provideCache().put("imagePath", mCropImgFilePath);
                    mPresenter.uploadImage();
                    imageTV.setImageBitmap(BitmapFactory.decodeFile(mCropImgFilePath));
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
                                imageTV.getWidth(),
                                imageTV.getHeight(),
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


    private void showPickerView() {

        if (addressList.size() <= 0) {
            return;
        }

        options1Items.clear();
        options2Items.clear();
        options3Items.clear();

        options1Items.addAll(addressList);

        for (AreaAddress areaAddress : addressList) { // 遍历省份
            List<AreaAddress> cities = new ArrayList<>();
            List<List<AreaAddress>> countyList = new ArrayList<>();
            for (AreaAddress city : areaAddress.getAreaList()) {
                cities.add(city);
                List<AreaAddress> counties = new ArrayList<>();//该城市的所有地区列表

                for (AreaAddress county : city.getAreaList()) {
                    counties.add(county);
                }
                countyList.add(counties);
            }
            options2Items.add(cities);

            options3Items.add(countyList);
        }


        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() + " " +
                        options2Items.get(options1).get(options2).getPickerViewText() + " " +
                        options3Items.get(options1).get(options2).get(options3).getPickerViewText();
                areaTV.setText(tx);
                provideCache().put("province", options1Items.get(options1).getAreaId());
                provideCache().put("city", options2Items.get(options1).get(options2).getAreaId());
                provideCache().put("county", options3Items.get(options1).get(options2).get(options3).getAreaId());
                mPresenter.modifyUserInfo();
            }
        })

                .setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setCancelColor(Color.parseColor("#FF5fbfe3"))
                .setSubmitColor(Color.parseColor("#FF5fbfe3"))
                .setContentTextSize(20)
                .build();

        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

}
