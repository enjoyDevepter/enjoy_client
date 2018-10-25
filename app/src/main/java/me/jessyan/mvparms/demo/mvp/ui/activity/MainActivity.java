package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.chaychan.library.BottomBarLayout;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.integration.AppManager;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.app.utils.SPUtils;
import me.jessyan.mvparms.demo.di.component.DaggerMainComponent;
import me.jessyan.mvparms.demo.di.module.MainModule;
import me.jessyan.mvparms.demo.mvp.contract.MainContract;
import me.jessyan.mvparms.demo.mvp.model.entity.Area;
import me.jessyan.mvparms.demo.mvp.model.entity.HomeAd;
import me.jessyan.mvparms.demo.mvp.model.entity.user.response.UpdateResponse;
import me.jessyan.mvparms.demo.mvp.presenter.MainPresenter;
import me.jessyan.mvparms.demo.mvp.ui.fragment.AppointmentFragment;
import me.jessyan.mvparms.demo.mvp.ui.fragment.DiscoverFragment;
import me.jessyan.mvparms.demo.mvp.ui.fragment.HomeFragment;
import me.jessyan.mvparms.demo.mvp.ui.fragment.MallFragment;
import me.jessyan.mvparms.demo.mvp.ui.fragment.MyFragment;
import me.jessyan.mvparms.demo.mvp.ui.widget.CustomDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View, ViewPager.OnPageChangeListener, View.OnClickListener, AMapLocationListener {

    private static final int REQUEST_CODE_APP_INSTALL = 1000;
    private static boolean isExit = false;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    @BindView(R.id.ll_home)
    View homeV;
    @BindView(R.id.ll_mall)
    View mallV;
    @BindView(R.id.ll_found)
    View foundV;
    @BindView(R.id.ll_appointment)
    View appointmentV;
    @BindView(R.id.ll_my)
    View myV;
    @BindView(R.id.bbl)
    BottomBarLayout bottomBarLayout;
    @BindView(R.id.sign)
    View signV;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @Inject
    RxPermissions mRxPermissions;
    @Inject
    ImageLoader mImageLoader;
    @Inject
    AppManager appManager;
    DownloadManager downloadManager;
    CustomDialog locationDialog;
    CustomDialog updateDialog;
    CustomDialog adDialog;
    long startPosition = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    private long downloadId;
    private List<Fragment> mFragmentList = new ArrayList<>();

    /**
     * 获取当前应用版本号
     *
     * @param context
     * @return
     */
    public static int getCurrentVersionCode(Context context) {

        PackageManager packageManager = context.getPackageManager();

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);

            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;

    }

    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_main; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(this);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.SignIn);
        if (null != mLocationClient) {
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationClient.setLocationOption(mLocationOption);
            mLocationOption.setOnceLocation(true);
            mLocationOption.setOnceLocationLatest(true);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
        initFragment();
        viewPager.setAdapter(new FragmentStatePagerAdapter((getSupportFragmentManager())) {
            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return "";
            }

            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

        });
        viewPager.setOffscreenPageLimit(1);
        viewPager.addOnPageChangeListener(this);
        bottomBarLayout.setViewPager(viewPager);
        homeV.setOnClickListener(this);
        mallV.setOnClickListener(this);
        foundV.setOnClickListener(this);
        appointmentV.setOnClickListener(this);
        myV.setOnClickListener(this);
        viewPager.setCurrentItem(0, false);
        signV.setOnClickListener(this);
        downloadManager = (DownloadManager) this.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    private void initFragment() {
        mFragmentList.add(HomeFragment.newInstance());
        mFragmentList.add(MallFragment.newInstance());
        mFragmentList.add(DiscoverFragment.newInstance());
        mFragmentList.add(AppointmentFragment.newInstance());
        mFragmentList.add(MyFragment.newInstance());
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Subscriber(tag = EventBusTags.USER_LOGOUT)
    public void logout(int index) {
        viewPager.setCurrentItem(0, false);
    }

    @Subscriber(tag = EventBusTags.CHANGE_MAIN_ITEM)
    public void changeItem(int item) {
        viewPager.setCurrentItem(item, false);
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
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 3:
                EventBus.getDefault().post(position, EventBusTags.ONREFRESH_CONTENT);
                break;
            case 4:
                EventBus.getDefault().post(position, EventBusTags.ONREFRESH_CONTENT);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public Cache getCache() {
        return provideCache();
    }

    @Override
    public void showSign(boolean show) {
        signV.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showAD(HomeAd ad) {
        showAdDailog(ad);
    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void showUpdateInfo(UpdateResponse updateResponse) {
        if (updateResponse.isNeedUpgrade()) {
            showUpdateDailog(updateResponse);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            showMessage("再按一次退出程序");
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            ArmsUtils.exitApp();
        }
    }

    @Override
    public void showLocationChange(Area county) {
        locationDialog = CustomDialog.create(getSupportFragmentManager())
                .setViewListener(new CustomDialog.ViewListener() {
                    @Override
                    public void bindView(View view) {
                        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(getApplication()).extras();
                        ((TextView) view.findViewById(R.id.content)).setText("是否切换到定位地址: " + cache.get("current_location_info"));
                        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                locationDialog.dismiss();
                            }
                        });
                        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cache.put("province", provideCache().get("province"));
                                SPUtils.put("province", provideCache().get("province"));
                                cache.put("city", provideCache().get("city"));
                                SPUtils.put("city", provideCache().get("city"));
                                cache.put("county", provideCache().get("county"));
                                SPUtils.put("county", provideCache().get("county"));
                                SPUtils.put("countyName", county.getName());
                                EventBus.getDefault().post(county, EventBusTags.CITY_CHANGE_EVENT);
                                locationDialog.dismiss();
                            }
                        });
                    }
                })
                .setLayoutRes(R.layout.dialog_remove_good_for_cart)
                .setDimAmount(0.5f)
                .isCenter(true)
                .setWidth(ArmsUtils.getDimens(this, R.dimen.dialog_width))
                .setHeight(ArmsUtils.getDimens(this, R.dimen.dialog_height))
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_home:
                viewPager.setCurrentItem(0, false);
                break;
            case R.id.ll_mall:
                viewPager.setCurrentItem(1, false);
                break;
            case R.id.ll_found:
                viewPager.setCurrentItem(2, false);
                break;
            case R.id.ll_appointment:
                Cache<String, Object> appCache = ArmsUtils.obtainAppComponentFromContext(getApplication()).extras();
                if (ArmsUtils.isEmpty((String) appCache.get(KEY_KEEP + "token"))) {
                    ArmsUtils.startActivity(LoginActivity.class);
                    return;
                }
                viewPager.setCurrentItem(3, false);
                break;
            case R.id.ll_my:
                Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(getApplication()).extras();
                if (ArmsUtils.isEmpty((String) cache.get(KEY_KEEP + "token"))) {
                    ArmsUtils.startActivity(LoginActivity.class);
                    return;
                }
                viewPager.setCurrentItem(4, false);
                break;
            case R.id.sign:
                ArmsUtils.startActivity(UserIntegralActivity.class);
                break;
        }
    }

    private void showUpdateDailog(UpdateResponse updateResponse) {
        updateDialog = CustomDialog.create(getSupportFragmentManager())
                .setViewListener(new CustomDialog.ViewListener() {
                    @Override
                    public void bindView(View view) {
                        ((TextView) view.findViewById(R.id.content)).setText(updateResponse.getDescription());
                        View close = view.findViewById(R.id.close);
                        close.setVisibility(updateResponse.isForceUpgrade() ? View.GONE : View.VISIBLE);
                        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                updateDialog.dismiss();
                            }
                        });
                        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startDownload(updateResponse.getUrl(), updateResponse.getUpgradeVersion());
                                updateDialog.dismiss();
                            }
                        });
                    }
                })
                .setLayoutRes(R.layout.dialog_update)
                .setDimAmount(0.5f)
                .isCenter(true)
                .setCancelOutside(updateResponse.isForceUpgrade() ? false : true)
                .setWidth(ArmsUtils.getDimens(this, R.dimen.update_dialog_width))
                .setHeight(ArmsUtils.getDimens(this, R.dimen.update_dialog_height))
                .show();
    }

    public void startDownload(String url, String name) {
        if (ArmsUtils.isEmpty(url) || ArmsUtils.isEmpty(name)) {
            return;
        }
        File downloadFile = new File(Environment.getExternalStorageDirectory(), name + ".apk");// 设置路径

        if (downloadFile.exists()) {
            startPosition = downloadFile.length();
        }
        final Request request = new Request.Builder()
                .addHeader("RANGE", "bytes=" + startPosition + "-")
                .url(url)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                InputStream is = body.byteStream();
                byte[] bytes = new byte[2048];
                int len = 0;
                long totalNum = startPosition;
                RandomAccessFile raf = new RandomAccessFile(downloadFile, "rw");
                while ((len = is.read(bytes, 0, bytes.length)) != -1) {
                    raf.seek(totalNum);
                    raf.write(bytes, 0, len);
                    totalNum += len;
                }
                body.close();
                installApk(downloadFile);
            }
        });
    }


    private void showAdDailog(HomeAd ad) {
        adDialog = CustomDialog.create(getSupportFragmentManager())
                .setViewListener(new CustomDialog.ViewListener() {
                    @Override
                    public void bindView(View view) {
                        ImageView image = view.findViewById(R.id.ad);
                        mImageLoader.loadImage(image.getContext(),
                                ImageConfigImpl
                                        .builder()
                                        .placeholder(R.drawable.place_holder_img)
                                        .url(ad.getImageUrl())
                                        .imageView(image)
                                        .build());
                        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                adDialog.dismiss();
                            }
                        });
                        image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                adDialog.dismiss();
                                if ("coupon".equals(ad.getRedirectType())) {
                                    ArmsUtils.startActivity(PickCouponActivity.class);
                                } else if ("login".equals(ad.getRedirectType())) {
                                    ArmsUtils.startActivity(LoginActivity.class);
                                } else if ("web".equals(ad.getRedirectType())) {
                                    ArmsUtils.startActivity(PlatformActivity.class);
                                }
                            }
                        });
                    }
                })
                .setLayoutRes(R.layout.dialog_ad)
                .setDimAmount(0.5f)
                .isCenter(true)
                .setWidth(ArmsUtils.getDimens(this, R.dimen.ad_dialog_width))
                .setHeight(ArmsUtils.getDimens(this, R.dimen.ad_dialog_height))
                .show();
    }


    private void installApk(File file) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上
            Uri apkUri = FileProvider.getUriForFile(getApplication(), "cn.ehanmy.fileprovider", file);//在AndroidManifest中的android:authorities值
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");

            //兼容8.0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                boolean hasInstallPermission = getActivity().getPackageManager().canRequestPackageInstalls();
                if (!hasInstallPermission) {
                    showMessage("安装应用需要打开未知来源权限，请去设置中开启权限");
                    startInstallPermissionSettingActivity(getActivity());
                    return;
                }
            }
        } else {
            install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        ArmsUtils.startActivity(install);
    }

    /**
     * 开启设置安装未知来源应用权限界面
     *
     * @param context
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE_APP_INSTALL);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_APP_INSTALL) {
            File file = new File(Environment.getExternalStorageDirectory(), "hi.apk");// 设置路径
            installApk(file);
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        provideCache().put("lon", String.valueOf(aMapLocation.getLongitude()));
        provideCache().put("lat", String.valueOf(aMapLocation.getLatitude()));
        mPresenter.getAreaForLoaction();
    }
}
