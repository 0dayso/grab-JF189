package gxx;

/**
 * ������ӿ�
 * Create by Gxx
 * Time: 2013-10-09 03:00
 */
public interface BaseInterface
{
    /**
     * ����Title
     */
    public static final String TITLE = "������ҳ������";

    /**
     * EXCEL�ĵ�һ������
     */
    public static final String[] EXCEL_TITLE = {"�ֻ�����","�ֻ�����","�Ƿ��ѯ���(��/��)","��ͨ���",
            "�������","������ѯʱ���","������","��ʱ��","�ײ�����","�ײ�ʹ�����","�Ƿ����ת������(��/��)",
            "����������ת�ƺ���","��Ӧ�����ת�ƺ���","��æʱ����ת�ƺ���","����ת�����ý��","��־��Ϣ"};

    /**
     * ������һ��
     */
    public static final String[] EMPTY_STRING_ARRAY = {"","","","","","","","","","","","","","","",""};

    /**
     * ��������JS�ļ�·��
     */
    public static final String ENC_PWD_JS_FILE = BaseUtils.getJarPath() + "whole.js";//"E:\\03.my_projects\\grab_SN189\\src\\js\\whole.js";

    /**
     * ��������JS�ļ� �� ���ܵ�js��������
     */
    public static final String ENC_PWD_FUNCTION_NAME = "encPwd";

    /**
     * �������ǲ���ͨ�� ���ʵ�URL
     */
    public static final String[] CHECK_NET_WORK_LINKED_URLS = new String[]{"http://www.baidu.com", "http://www.google.cn"};
}
