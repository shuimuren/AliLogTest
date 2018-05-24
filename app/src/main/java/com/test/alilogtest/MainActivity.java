package com.test.alilogtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.xmd.log.IUploadResultInterface;
import com.xmd.log.PostLogManager;


public class MainActivity extends AppCompatActivity implements IUploadResultInterface{

    String endpoint = "cn-hangzhou.log.aliyuncs.com"; //公网服务入口

    String accessKeyId = "LTAINowKYWyYAbDV"; // 使用您的阿里云访问密钥 AccessKeyId

    String accessKeySecret = "Tw9Gpd46sEn985HwHrFSFnh3ugn68d"; // 使用您的阿里云访问密钥Secret

    String project = "spa-pay"; // 项目名称

    String logstore = "pos"; // 日志库名称
    /**
     * @param stsAk                  使用您的阿里云访问密钥 AccessKeyId
     * @param stsSk                  使用您的阿里云访问密钥Secret
     * @param endpoint               公网服务入口
     * @param logEnable              是否打印在控制台
     * @param iUploadResultInterface 上传失败成功回调
     */
    private Button btn;
    PostLogManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = PostLogManager.getInstance();
        manager.initPostLogManager(accessKeyId,accessKeySecret,endpoint,project,logstore,true,this);
        btn = findViewById(R.id.btn_up);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.postLogToSercive("此处为测试代码");
            }
        });

       // PostLogManager.getInstance().initPostLogManager();
    }


    @Override
    public void postSuccess() {
        Log.i(">>>","上传成功");
    }

    @Override
    public void postFailure(String errorCode, String errorMessage) {
        Log.i(">>>","上传失败");
    }

    @Override
    public void clientFailure(String error) {
        Log.i(">>>","error"+error);
    }
}
