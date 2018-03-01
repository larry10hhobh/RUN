package com.larry.shugo.model.bean;

/**
 * bmob返回结果处理接口
 */
public interface IBmobCallback {

    /**
     * 成功
     * @param identifier 标识
     * @param object
     */
    void onFinish(int identifier,Object object);

    /**
     * 失败
     * @param identifier
     */
    void onFailure(int identifier);
}
