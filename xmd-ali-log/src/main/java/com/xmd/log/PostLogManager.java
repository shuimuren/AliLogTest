package com.xmd.log;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aliyun.sls.android.sdk.ClientConfiguration;
import com.aliyun.sls.android.sdk.LOGClient;
import com.aliyun.sls.android.sdk.LogException;
import com.aliyun.sls.android.sdk.SLSLog;
import com.aliyun.sls.android.sdk.core.auth.PlainTextAKSKCredentialProvider;
import com.aliyun.sls.android.sdk.core.callback.CompletedCallback;
import com.aliyun.sls.android.sdk.model.Log;
import com.aliyun.sls.android.sdk.model.LogGroup;
import com.aliyun.sls.android.sdk.request.PostLogRequest;
import com.aliyun.sls.android.sdk.result.PostLogResult;

public class PostLogManager {

    private String STS_AK = "";
    private String STS_SK = "";
    private String END_POINT = "";
    private String projectName;
    private String logStoreName;
    private LOGClient logClient;
    private IUploadResultInterface iUploadResultInterface;
    private LogGroup logGroup;

    private PostLogManager() {

    }

    private static class PostLogManagerHolder {
        private static PostLogManager postLogManagerInstance = new PostLogManager();
    }

    public static PostLogManager getInstance() {
        return PostLogManagerHolder.postLogManagerInstance;
    }

    /**
     * @param stsAk                  使用您的阿里云访问密钥 AccessKeyId
     * @param stsSk                  使用您的阿里云访问密钥Secret
     * @param endpoint               公网服务入口
     * @param logEnable              是否打印在控制台
     * @param iUploadResultInterface 上传失败成功回调
     */
    public void initPostLogManager(@NonNull String stsAk, @NonNull String stsSk, @NonNull String endpoint, @NonNull String projectName, @NonNull String logStoreName,
                                   boolean logEnable, @Nullable IUploadResultInterface iUploadResultInterface) {
        this.STS_AK = stsAk;
        this.STS_SK = stsSk;
        this.END_POINT = endpoint;
        this.projectName = projectName;
        this.logStoreName = logStoreName;
        this.iUploadResultInterface = iUploadResultInterface;
        PlainTextAKSKCredentialProvider credentialProvider =
                new PlainTextAKSKCredentialProvider(STS_AK, STS_SK);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2
        if (logEnable) {
            SLSLog.enableLog(); // log打印在控制台
        } else {
            SLSLog.disableLog();
        }
        logClient = new LOGClient(END_POINT, credentialProvider, conf);
        /* 创建logGroup */
        logGroup = new LogGroup();
    }

    /**
     * @param logContent 上报具体内容
     */
    public void postLogToSercive(String logContent) {
        /* 存入一条log */
        Log log = new Log();
        log.PutContent("current time ", "" + DateUtil.longToDate(System.currentTimeMillis()));
        log.PutContent("content", logContent);
        logGroup.PutLog(log);
        try {
            PostLogRequest request = new PostLogRequest(projectName, logStoreName, logGroup);
            logClient.asyncPostLog(request, new CompletedCallback<PostLogRequest, PostLogResult>() {
                @Override
                public void onSuccess(PostLogRequest request, PostLogResult result) {
                    if (iUploadResultInterface != null) {
                        iUploadResultInterface.postSuccess();
                    }
                }
                @Override
                public void onFailure(PostLogRequest request, LogException exception) {
                    if (iUploadResultInterface != null) {
                        iUploadResultInterface.postFailure(exception.getErrorCode(),exception.getErrorMessage());
                    }
                }
            });
        } catch (LogException e) {
            e.printStackTrace();
            if(iUploadResultInterface != null){
                iUploadResultInterface.clientFailure(e.getLocalizedMessage().toString());
            }
        }
    }


}
