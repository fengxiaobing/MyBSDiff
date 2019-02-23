package com.bing.mybsdiff;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bing.mybsdiff.utils.UriParseUtils;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    // 用于在应用程序启动时加载“本地的lib”库
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView version = findViewById(R.id.version);
        version.setText(BuildConfig.VERSION_NAME);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String[] pers = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            if(checkSelfPermission(pers[0]) == PackageManager.PERMISSION_DENIED){
                requestPermissions(pers,2000);
            }
        }

    }


    /**
     * 合成安装包
     * @param oldApk 旧版本安装包 如1.1.1
     * @param patch 差分包
     * @param output 合成后的新版本ap 输出
     */
    public native void bsPatch(String oldApk,String patch,String output);


    @SuppressLint("StaticFieldLeak")
    public void update(View view) {

       //从服务器下载到用户手机，SD卡里面
        new AsyncTask<Void, Void, File>() {
           //在后台执行
            @Override
            protected File doInBackground(Void... voids) {
                //获取旧版本的路径（正在运行的apk）
                String oldApk = getApplicationInfo().sourceDir;
                String patch = new File(Environment.getExternalStorageDirectory(),"patch").getAbsolutePath();
                String output = createNewApk().getAbsolutePath();
                bsPatch(oldApk,patch,output);
                return new File(output);
            }

            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
                // 已经合成了，调用该方法，安装新版本
                UriParseUtils.installApk(MainActivity.this,file);
            }
        }.execute();

    }

    //创建合成后的apk
    private File createNewApk() {
        File apkFile = new File(Environment.getExternalStorageDirectory(),"bsdiff.apk");
        if(!apkFile.exists()){
            try {
                apkFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return apkFile;
    }


    public void startSecond(View view) {
        startActivity(new Intent(this,SecondActivity.class));
    }
}
