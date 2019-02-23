package com.bing.mybsdiff.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;


import java.io.File;

/**
 * Created by Administrator on 2019/2/19.
 */

public class UriParseUtils {

    /**
     *创建一个文件的输出路径（fileprovider）
     * @param context
     * @param file
     * @return
     */
    private static Uri getUriForFile(Context context, File file){
        return FileProvider.getUriForFile(context,getFileProvider(context),file);
    }

    /**
     * 获取FileProvider的路径   适配6.0
     * @param context
     * @return
     */
    private static String getFileProvider(Context context) {
        return context.getPackageName()+".fileprovider";
    }

    /**
     * 安装apk
     * @param activity
     * @param apkFile
     */
   public static void installApk(Activity activity,File apkFile){
        if(!apkFile.exists()){
            return;
        }
       Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
           Uri fileUri = getUriForFile(activity,apkFile);
            intent.setDataAndType(fileUri,"application/vnd.android.package-archive");
       }else {
           intent.setDataAndType(Uri.fromFile(apkFile),"application/vnd.android.package-archive");
       }
       activity.startActivity(intent);
   }
}
