package com.larry.shugo.utils;

/**
 * 基本配置
 */
public class ConfigUtil {

    /**
     * Application ID ，初始化用到密钥
     */
    public static final String BMOB_APP_ID = "03875f045493c5bfe104bed63325e6e7";

    /**
     * REST API Key , REST API请求中HTTP头部信息必须附带密钥之一
     */
    public static final String BMOB_API_KEY = "67dba42bc44bcf0af225dd2d977984a1";

    /**
     * Secret key ，是SDK安全密钥，不可泄漏，在云端逻辑测试云端代码时需要用到
     */
    public static final String BMOB_SECRET_KEY = "372c21fe6e12f827";

    /**
     * Master Key , 超级权限Key。应用开发或调试的时候可以使用该密钥进行各种权限的操作，此密钥不可泄漏
     */
    public static final String BMOB_MASTER_KEY = "104f5edd53ea90e27c738a9546d38def";

    /**
     * apikey ，APIStore通用key
     */
    public static final String APISTORE_API_KEY = "618aa2b9fcfa0575e8acefb8c843f76a";


    /**
     * 鹰眼服务id
     */
    public static final String YINGYAN_SERVICE_ID = "115788";

    /**
     * 天气接口地址
     * 目前不可用
     */
    public static final String WEATHER_API = "http://apis.baidu.com/heweather/weather/free";

    /**
     * 城市列表接口
     * 目前不可用
     */
    public static final String CITY_LIST_API = " https://api.heweather.com/x3/citylist?search=allchina" +
            "&key=a7ec86e719d9458da2e1f67ebc73d2e4";

}
