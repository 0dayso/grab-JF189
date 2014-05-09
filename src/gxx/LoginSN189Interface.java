package gxx;

/**
 * ��½http://sn.189.cn/��վ�ӿ���
 * Create by Gxx
 * Time: 2013-10-07 21:50
 */
public interface LoginSN189Interface
{
    /**
     * Cookie����
     */
    public static final String COOKIE_SSO_AUTO_TOKEN = "COM.TYDIC.SSO_AUTH_TOKEN";

    /**
     * ��½URL
     */
    public static final String LOGIN_URL = "http://sn.189.cn/sso/GetLoginServlet";

    /**
     * ����ѯURL: ��ͨ��� + �������
     */
    public static final String BAL_QUERY_URL = "http://sn.189.cn/service/bill/resto.action";

    /**
     * ������ѯURL(�ϸ���)
     */
    public static final String FLOW_QUERY_URL = "http://sn.189.cn/biz/service/bill/queryDetail.action";

    /**
     * �ײͲ�ѯURL
     */
    public static final String PACKAGE_QUERY_URL = "http://sn.189.cn/biz/service/bill/initQueryMealCharges.action";

    /**
     * ����ת�Ƽ���URL
     */
    public static final String CALL_DIVERT_LOAD_URL = "http://sn.189.cn/service/transaction/toCallDivert.action";

    /**
     * ����ת������URL
     */
    public static final String CALL_DIVERT_SET_URL = "http://sn.189.cn/service/transaction/doCallDivert.action";
}
