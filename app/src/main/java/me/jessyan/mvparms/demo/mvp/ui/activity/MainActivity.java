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
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaychan.library.BottomBarLayout;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.integration.cache.Cache;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.simple.eventbus.Subscriber;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import me.jessyan.mvparms.demo.R;
import me.jessyan.mvparms.demo.app.EventBusTags;
import me.jessyan.mvparms.demo.di.component.DaggerMainComponent;
import me.jessyan.mvparms.demo.di.module.MainModule;
import me.jessyan.mvparms.demo.mvp.contract.MainContract;
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


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View, ViewPager.OnPageChangeListener, LocationListener, View.OnClickListener {

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
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @Inject
    RxPermissions mRxPermissions;
    @Inject
    ImageLoader mImageLoader;
    CustomDialog dialog = null;

    DownloadManager downloadManager;
    DownLoadBroadCastReceiver downLoadBroadCastReceiver;
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
        viewPager.addOnPageChangeListener(this);
        bottomBarLayout.setViewPager(viewPager);
        homeV.setOnClickListener(this);
        mallV.setOnClickListener(this);
        foundV.setOnClickListener(this);
        appointmentV.setOnClickListener(this);
        myV.setOnClickListener(this);
        viewPager.setCurrentItem(0, false);

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
    public void onLocationChanged(Location location) {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(this);
        provideCache().put("lon", String.valueOf(location.getLongitude()));
        provideCache().put("lat", String.valueOf(location.getLatitude()));
        mPresenter.getAreaForLoaction();
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

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
        }
    }

    private void showUpdateDailog(UpdateResponse updateResponse) {
        dialog = CustomDialog.create(getSupportFragmentManager())
                .setViewListener(new CustomDialog.ViewListener() {
                    @Override
                    public void bindView(View view) {
                        ((TextView) view.findViewById(R.id.content)).setText(updateResponse.getDescription());
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
                                        .placeholder(R.mipmap.place_holder_img)
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


    @Override
    protected void onDestroy() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationManager.removeUpdates(this);
        super.onDestroy();
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
