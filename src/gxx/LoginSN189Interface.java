package gxx;

/**
 * 登陆http://sn.189.cn/网站接口类
 * Create by Gxx
 * Time: 2013-10-07 21:50
 */
public interface LoginSN189Interface
{
    /**
     * Cookie名字
     */
    public static final String COOKIE_SSO_AUTO_TOKEN = "COM.TYDIC.SSO_AUTH_TOKEN";

    /**
     * 登陆URL
     */
    public static final String LOGIN_URL = "http://sn.189.cn/sso/GetLoginServlet";

    /**
     * 余额查询URL: 普通余额 + 其它余额
     */
    public static final String BAL_QUERY_URL = "http://sn.189.cn/service/bill/resto.action";

    /**
     * 流量查询URL(上个月)
     */
    public static final String FLOW_QUERY_URL = "http://sn.189.cn/biz/service/bill/queryDetail.action";

    /**
     * 套餐查询URL
     */
    public static final String PACKAGE_QUERY_URL = "http://sn.189.cn/biz/service/bill/initQueryMealCharges.action";

    /**
     * 呼叫转移加载URL
     */
    public static final String CALL_DIVERT_LOAD_URL = "http://sn.189.cn/service/transaction/toCallDivert.action";

    /**
     * 呼叫转移设置URL
     */
    public static final String CALL_DIVERT_SET_URL = "http://sn.189.cn/service/transaction/doCallDivert.action";
}
