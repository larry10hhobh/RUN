package com.shu.runbang.model.bean;

/**
 * 网络请求返回接口
 */
public interface IHttpCallback {

    /**
     * 成功
     * @param response
     */
    void onSuccess(String response);

    /**
     * 失败
     */
    void onFailure(Exception e);
}
