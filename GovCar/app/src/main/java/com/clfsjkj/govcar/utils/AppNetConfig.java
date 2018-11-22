package com.clfsjkj.govcar.utils;

/**
 * @author zheng
 *         配置网络请求相关的地址
 */
public class AppNetConfig {

    public static final String IPADDRESS = "http://47.92.110.210:8080/paike";//Aliyun
//    public static final String IPADDRESS = "http://192.168.1.245:8080/paike";
    public static final String LOGIN_URL = IPADDRESS + "/appAdmin/login";
    public static final String GET_COURSETABLEDATA_URL = IPADDRESS + "/appAdmin/getCourseData";
    public static final String GET_IMG_URL = IPADDRESS + "/appAdmin/getImgData";
    public static final String GET_SCOREDATETREE_URL = IPADDRESS + "/appAdmin/getScoreTypeTree";
    public static final String GET_getScoreDataByTime_URL = IPADDRESS + "/appAdmin/getScoreDataByTime";


}
