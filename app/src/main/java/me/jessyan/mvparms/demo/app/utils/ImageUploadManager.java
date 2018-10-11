package me.jessyan.mvparms.demo.app.utils;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

import me.jessyan.mvparms.demo.mvp.model.api.service.LoginAndRegisterService;
import me.jessyan.mvparms.demo.mvp.model.api.service.UserService;

public class ImageUploadManager {

    private UploadManager uploadManager;
    private static final String LOG_TAG = "ImageUploadManager";

    private ImageUploadManager(){
        Configuration config = new Configuration.Builder()
                .build();

        uploadManager = new UploadManager(config);
    }

    public void updateImage(File file,String qiniuToken,String urlPrefix,@NonNull  ImageUploadResponse imageUploadResponse){
        UUID uuid = UUID.randomUUID();
        String s = uuid.toString();
        UploadOptions options = new UploadOptions(null,"image/jpeg",false,null,null);
        uploadManager.put(file, s, qiniuToken, new UpCompletionHandler() {

            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if(info.isOK()){
                    String url = urlPrefix + "/" + key;
                    imageUploadResponse.onImageUploadOk(url);
                }else{
                    String error = info.error;
                    imageUploadResponse.onImageUploadError(error);
                }
            }
        },options);
    }

    /**获取单例*/
    public static ImageUploadManager getInstance(){
        return Holder.instance;
    }
    private static final class Holder{
        private static final ImageUploadManager instance = new ImageUploadManager();
    }

    public interface ImageUploadResponse{
        void onImageUploadOk(String url);
        void onImageUploadError(String errorInfo);
    }

}