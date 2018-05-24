package com.xmd.log;




public interface IUploadResultInterface {
    void postSuccess();
    void postFailure(String errorCode, String errorMessage);
    void clientFailure(String error);
}
