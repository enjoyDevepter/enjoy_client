package me.jessyan.mvparms.demo.mvp.ui.activity;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;
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

import static com.jess.arms.integration.cache.IntelligentCache.KEY_KEEP;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View, ViewPager.OnPageChangeListener, View.OnClickListener, AMapLocationListener {

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
    CustomDialog dialog = null;
    DownloadManager downloadManager;
    DownLoadBroadCastReceiver downLoadBroadCastReceiver;
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
    protected void onResume() {
        super.onResume();
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
        viewPager.setOffscreenPageLimit(2);
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
        Cache<String, Object> appCache = ArmsUtils.obtainAppComponentFromContext(getApplication()).extras();
        if (position == 2 && positionOffset > 0 && ArmsUtils.isEmpty((String) appCache.get(KEY_KEEP + "token"))) {
            viewPager.setCurrentItem(2, false);
        }
    }

    @Override
    public void onPageSelected(int position) {
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
        dialog = CustomDialog.create(getSupportFragmentManager())
                .setViewListener(new CustomDialog.ViewListener() {
                    @Override
                    public void bindView(View view) {
                        Cache<String, Object> cache = ArmsUtils.obtainAppComponentFromContext(getApplication()).extras();
                        ((TextView) view.findViewById(R.id.content)).setText("是否切换到定位地址: " + cache.get("current_location_info"));
                        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
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
                                dialog.dismiss();
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
        dialog = CustomDialog.create(getSupportFragmentManager())
                .setViewListener(new CustomDialog.ViewListener() {
                    @Override
                    public void bindView(View view) {
                        ((TextView) view.findViewById(R.id.content)).setText(updateResponse.getDescription());
                        View close = view.findViewById(R.id.close);
                        close.setVisibility(updateResponse.isForceUpgrade() ? View.GONE : View.VISIBLE);
                        view.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        view.findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                downLoadBroadCastReceiver = new DownLoadBroadCastReceiver();
                                IntentFilter intentFilter = new IntentFilter();
                                intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                                getActivity().registerReceiver(downLoadBroadCastReceiver, intentFilter);
                                downloadAPk(updateResponse.getUrl());
                                dialog.dismiss();
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

    private void showAdDailog(HomeAd ad) {
        dialog = CustomDialog.create(getSupportFragmentManager())
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
                                dialog.dismiss();
                            }
                        });
                        image.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
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

    private void downloadAPk(String url) {
        SharedPreferences sp = getActivity().getSharedPreferences("setting", Context.MODE_PRIVATE);
        downloadId = sp.getLong("downloadId", -1);
        if (downloadId != -1l) {

            int status = getDownloadStatus(downloadId);

            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                Uri uri = getDownloadUri(downloadId);
                if (uri != null) {
                    //对比下载的apk版本和本地应用版本
                    if (compare(getApkInfo(uri.getPath()))) {
                        installApk();
                    } else {
                        downloadManager.remove(downloadId);
                        startDownload(url);
                    }
                }
            } else if (status == DownloadManager.STATUS_FAILED) {
                startDownload(url);
            }
        } else {
            startDownload(url);
        }
    }

    private void startDownload(final String url) {
        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //移动网络情况下是否允许漫游
        request.setAllowedOverRoaming(false);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setTitle("");
        request.setVisibleInDownloadsUi(true);
        //设置下载的路径
        //创建目录
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir();

        //设置文件存放路径
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "app.apk");

        //将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
        downloadId = downloadManager.enqueue(request);

        SharedPreferences sp = getActivity().getSharedPreferences("setting", Context.MODE_PRIVATE);
        //获取到edit对象
        SharedPreferences.Editor edit = sp.edit();
        //通过editor对象写入数据
        edit.putLong("downloadId", downloadId);
        //提交数据存入到xml文件中
        edit.commit();
    }


    /**
     * 获取保存的apk文件的地址
     *
     * @param downloadApkId
     * @return
     */
    private Uri getDownloadUri(long downloadApkId) {
        return downloadManager.getUriForDownloadedFile(downloadApkId);
    }

    private int getDownloadStatus(long downloadApkId) {

        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadApkId);
        Cursor c = downloadManager.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                c.close();
            }
        }
        return -1;
    }

    private void installApk() {
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath(), "app.apk");
        Intent install = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= 24) {//判读版本是否在7.0以上
            Uri apkUri = FileProvider.getUriForFile(getActivity(), "cn.ehanmy.fileprovider", file);//在AndroidManifest中的android:authorities值
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            install.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        getActivity().startActivity(install);
    }

    /**
     * 获取下载的apk版本信息
     *
     * @param path
     * @return
     */
    private PackageInfo getApkInfo(String path) {
        PackageManager pm = getActivity().getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (info != null) {

            return info;
        }
        return null;
    }

    /**
     * 如果当前版本号小于apk的版本号则返回true
     *
     * @param apkInfo
     * @return
     */
    private boolean compare(PackageInfo apkInfo) {
        if (apkInfo == null) {
            return false;
        }
        int versionCode = getCurrentVersionCode(this);

        if (apkInfo.versionCode > versionCode) {
            return true;
        }
        return false;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        provideCache().put("lon", String.valueOf(aMapLocation.getLongitude()));
        provideCache().put("lat", String.valueOf(aMapLocation.getLatitude()));
        mPresenter.getAreaForLoaction();
    }

    private class DownLoadBroadCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadId == id) {
                //下载完成
                getActivity().unregisterReceiver(downLoadBroadCastReceiver);
                //跳到安装界面
                installApk();
            }
        }
    }

}
