package gxx;

/**
 * 基础类接口
 * Create by Gxx
 * Time: 2013-10-09 03:00
 */
public interface BaseInterface
{
    /**
     * 工具Title
     */
    public static final String TITLE = "智能网页机器人";

    /**
     * EXCEL的第一行数据
     */
    public static final String[] EXCEL_TITLE = {"手机号码","手机密码","是否查询余额(是/否)","普通余额",
            "其他余额","流量查询时间段","总流量","总时长","套餐名称","套餐使用情况","是否呼叫转移设置(是/否)",
            "无条件呼叫转移号码","无应答呼叫转移号码","遇忙时呼叫转移号码","呼叫转移设置结果","日志信息"};

    /**
     * 新增空一行
     */
    public static final String[] EMPTY_STRING_ARRAY = {"","","","","","","","","","","","","","","",""};

    /**
     * 加密密码JS文件路径
     */
    public static final String ENC_PWD_JS_FILE = BaseUtils.getJarPath() + "whole.js";//"E:\\03.my_projects\\grab_SN189\\src\\js\\whole.js";

    /**
     * 加密密码JS文件 中 加密的js方法名称
     */
    public static final String ENC_PWD_FUNCTION_NAME = "encPwd";

    /**
     * 判网络是不是通的 访问得URL
     */
    public static final String[] CHECK_NET_WORK_LINKED_URLS = new String[]{"http://www.baidu.com", "http://www.google.cn"};
}
