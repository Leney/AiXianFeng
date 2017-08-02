package com.bjzcht.lovebeequick;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;

import com.bjzcht.lovebeequick.manager.NetManager;
import com.bjzcht.lovebeequick.manager.UserInfoManager;
import com.bjzcht.lovebeequick.util.Constants;
import com.bjzcht.lovebeequick.util.download.DownloadManagerUtil;
import com.bjzcht.lovebeequick.util.log.DLog;
import com.bjzcht.lovebeequick.util.log.DefaultExceptionHandler;
import com.bjzcht.lovebeequick.util.rsa.RSACrypt;
import com.facebook.drawee.backends.pipeline.Fresco;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Application 类
 * Created by yb on 2016/8/11.
 */
public class AppApplication extends Application {
    private static Properties properties;
    private boolean isLog = true;

    private static AppApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (properties == null) {
            initProperties(getApplicationContext());
        }
        getConfigUrl();
        // 初始化日志
        DLog.DEBUG = isLog;
        // 全局异常监听
        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(
                getApplicationContext()));
        // 初始化AppManager
        NetManager.getInstance().init(this);
        // 初始化图片加载框架Fresco
        Fresco.initialize(this);
        // 初始化UserInfo
        UserInfoManager.getInstance().init(getApplicationContext());
        // 初始化下载模块
        DownloadManagerUtil.getInstance().init(getApplicationContext());
    }

    // 通过此方法传入名称，来获取此名称所对应的配置信息
    private String getProperty(String key) {
        return properties.getProperty(key, "");
    }

    public static AppApplication getInstance() {
        return instance;
    }


    /**
     * 获取配置信息
     */
    private void getConfigUrl() {

        try {
//            // 解析RSA公钥
//            Constants.RSA_PUBLIC_KEY = new String(RSACrypt.decode(getProperty("RSA_PUBLIC_KEY")),
//                    "utf-8");
            // 解析主Url地址
            Constants.MAIN_URL = new String(RSACrypt.decode(getProperty("MAIN_URL")), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String value = getProperty("IS_LOG");
        if ("true".equals(value)) {
            isLog = true;
        } else {
            isLog = false;
        }
    }

    /**
     * 获取配置url
     *
     * @param context
     * @return
     */
    private void initProperties(Context context) {
        properties = new Properties();
        InputStream is = null;
        try {
            String packageName = context.getPackageName();
            // PackageManager pm = context.getPackageManager();
            // PackageInfo pi = pm.getPackageInfo(packageName, 0);

            // String versionCode = pi.versionCode + "";
            // if (versionCode.endsWith("0"))
            // {
            AssetManager am = context.getAssets();
            is = am.open(packageName + ".ini");
            // } else
            // {
            // File file = new File(Environment.getExternalStorageDirectory()
            // + "/" + packageName + ".ini");
            // is = new BufferedInputStream(new FileInputStream(file));
            // }
            properties.load(is);
        } catch (Exception e) {
            DLog.e("AppApplication#initProperties. System Properties init faild. Exception=",
                    e);
            throw new RuntimeException(
                    "AppApplication#initProperties. System Properties init faild.");
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    DLog.e("AppApplication#initProperties. System Properties init faild. " +
                                    "IOException=",
                            e);
                    throw new RuntimeException(
                            "AppApplication#initProperties. System Properties init faild.");
                }
            }
        }
    }
}
