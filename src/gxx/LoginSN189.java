package gxx;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.util.HttpURLConnection;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ��½http://sn.189.cn/��վ��
 * Create by Gxx
 * Time: 2013-10-07 14:50
 */
public class LoginSN189 implements LoginSN189Interface
{
    /**
     * ��־������
     */
    Logger logger = Logger.getLogger(LoginSN189.class);

    /**
     * ��http://www.sn.189.cn/ץȡ���ݹ�����
     */
    GrabSN189 grabSN189;

    /**
     * �û���
     */
    String loginName;

    /**
     * ���� ����
     */
    String passWord;

    /**
     * �Ƿ��ѯ���
     */
    boolean isQueryBal;

    /**
     * �Ƿ����ת������
     */
    boolean isCallDivert;

    /**
     * ����������ת�ƺ���
     */
    String unConditionNum;

    /**
     * ��Ӧ�����ת�ƺ���
     */
    String unAnswerNum;

    /**
     * ��æʱ����ת�ƺ���
     */
    String callBusyNum;

    /**
     * COOKIE_SSO_AUTO_TOKENֵ
     */
    String cookieValue;

    /**
     * ��ͨ���
     */
    String puTongYuE = StringUtils.EMPTY;

    /**
     * �������
     */
    String qiTaYuE = StringUtils.EMPTY;

    /**
     * ������ѯʱ���
     */
    String flowQueryPeriod;

    /**
     * ������
     */
    String totalFlow = StringUtils.EMPTY;

    /**
     * ��ʱ��
     */
    String totalTime = StringUtils.EMPTY;

    /**
     * �ײ�����
     */
    String packageName = StringUtils.EMPTY;

    /**
     * �ײ�ʹ�����
     */
    String packageInfo = StringUtils.EMPTY;

    /**
     * ����ת�����ý��
     */
    String callDivertSetResult = StringUtils.EMPTY;

    /**
     * ���캯��
     * @param loginName
     * @param passWord
     */
    public LoginSN189(String loginName, String passWord, String unConditionNum, String unAnswerNum, String callBusyNum) throws Exception
    {
        this.loginName = loginName;
        this.passWord = passWord;
        this.unConditionNum = unConditionNum;
        this.unAnswerNum = unAnswerNum;
        this.callBusyNum = callBusyNum;

        // ����ʼ������
        init();
    }

    /**
     * ���캯��
     * @param dto
     */
    public LoginSN189(GrabSN189 grabSN189, InputDto dto) throws Exception
    {
        this.grabSN189 = grabSN189;
        this.loginName = dto.getShouJiHaoMa();
        this.passWord = dto.getShouJiMiMa();
        this.unConditionNum = dto.getUnConditionNum();
        this.unAnswerNum = dto.getUnAnswerNum();
        this.callBusyNum = dto.getCallBusyNum();
        this.isQueryBal = dto.isQueryBal();
        this.isCallDivert = dto.isCallDivert();

        // �ж���ͣץȡ
        BaseUtils.checkPause(grabSN189);

        // ����ʼ������
        init();
    }

    /**
     * ����ʼ������
     * ����½����
     * @throws Exception
     */
    public void init() throws Exception
    {
        // ����½����
        login1();
    }

    /**
     * ���̲߳���
     * @throws Exception
     */
    public void process() throws Exception
    {
        // 1 ����ѯ (�� �Ƿ��ѯ���)
        if(isQueryBal)
        {
            // �ж���ͣץȡ
            BaseUtils.checkPause(grabSN189);

            queryBal();
        }

        // �ж���ͣץȡ
        BaseUtils.checkPause(grabSN189);

        // 2 ������ѯ(�ϸ���)
        queryFlowByLastMonth();

        // �ж���ͣץȡ
        BaseUtils.checkPause(grabSN189);

        // 3 �ײͲ�ѯ
        queryPackage();

        // 4 ����ת������ (�� �Ƿ����ת������ ���� ���к�������ֵ�Ƿ�Ϊ��)
        if(isCallDivert)
        {
            // �ж���ͣץȡ
            BaseUtils.checkPause(grabSN189);

            callDivert();
        }
    }

    /**
     * ��½��1�� �����û�������������¼
     * ��ַ��http://sn.189.cn/sso/GetLoginServlet
     * @return
     * @throws Exception
     */
    public void login1() throws Exception
    {
        //BaseUtils.loggerOut(grabSN189, "��½��1���������û�������������¼[�û���=" + loginName + "]");
        PostMethod method = new PostMethod(LOGIN_URL);
        //��¼����
        method.addParameter("loginName", loginName);//�û���
        method.addParameter("passWord", BaseUtils.encPwd(passWord));//����
        method.addParameter("returnUrl", "/biz/service/account/init.action");
        method.addParameter("sysId", "1003");
        method.addParameter("activityUrl", "null");
        method.addParameter("recominfo", "null");
        method.addParameter("loginType", "4");
        method.addParameter("latnId", "290");
        method.addParameter("passType", "0");//�������� �ֻ�����(0) �������(1)
        method.addParameter("validCode", "");

        String respStr = connect(method);
        //BaseUtils.loggerOut(grabSN189, "�����ַ:" + LOGIN_URL);
        //BaseUtils.loggerOut(grabSN189, "�������ݣ�" + respStr);

        int index = respStr.indexOf("<a href=\"");
        String temp = respStr.substring(index + "<a href=\"".length());
        index = temp.indexOf("\"");
        temp = temp.substring(0, index);
        temp = temp.replaceAll("&amp;", "&");
        String jumpUrl = "http://sn.189.cn" + temp;//
        //BaseUtils.loggerOut(grabSN189, "��ȡ��½��2����תURL:" + jumpUrl);

        // �ж���ͣץȡ
        BaseUtils.checkPause(grabSN189);

        cookieValue = login2(jumpUrl);
        if(StringUtils.isBlank(cookieValue))
        {
            throw new RuntimeException("COOKIE_SSO_AUTO_TOKENֵΪ�գ�");
        }
    }

    /**
     * ��½��2�� ��½����ת
     * ��ַ����:http://sn.189.cn/sso/jsp/loginRedirect.jsp?sendInfos=5bf036d2b6e8c5350d028b53b2305cff11122021b5dca214e74620403c25d31da4e8c2d0aa6f5628716011f862b070cbde1ad516b01353d290f4a4c3aaa3df6c1f18b341be1939f48d08c92b070741ebb18f4295498914a99abbdc0e4978e6b0eb42b5fec75785210b15a61afb6140eb4ebe5b3320da7d9a0fb257fd9db21e62fb8e014b078c4cc44ea8256e13407bfd72ea022e4ba389a24ef1cb67bae676cf2315bc99baab072f32b1c274d64e103bb0a87e49498081fca8450627072eed55b0a87e49498081fccd924dca1a37ba05d7323ff7359efcaf29308ddfe10a27760d03b2f023839fd083e1beed5f4414de1f9ada0c2fde73265bf3bd2c06d0190aad7bf47c8d6956b44b1b053b333002369d6686619585a5f043a6b84a45ca27de53d286d8a2a629585836cb624e99a6d030d6fc40fb660585258aa7e128d6a7560854e0426deb2ec2a6b7986a1bd178a6a941299fd53892f3780df0c02d92ead005e46a975ea745e3f39e16556d84c513a4d7b0e9a7caf1a06f7445714e1f88d76780150ebbe7734277145b4a49a9b794b090d73a40797d151cb9ba2020e84f0eb9da8215ce303d141222a94ca6bd09a12dc5cfa7c2a7f3648f16977a215fcc039ec0e18825da4941340f444a3ea79e28900e3cc9adcaa957c89fc21c5868d0114b55453b7607cc17873127c191bdc6b563fca07d678d68b12c2ce15401670769078a962b5322e1be1a6c019d5e20ab299fe25c6e96d4b8f4fcd2dbf784b556fed7f609a218acceb345ba15358ae54e06429a0ea308721f0ffc4fe74933806fc1aaaf564f13fdcf6e918cc6ba4d69661f6c33cc08d01df7ee0428a6d677bde84313e8fbccc3f0e63eb6325b07734c95cf9fe5fad79e12761aa00dd9a21133d93d56462b5977edd38e90f957216e8d3ea5&reqUrl=http://117.32.232.182/uam2/servlet/Login4CT10000Servlet
     * @param jumpUrl
     * @return
     * @throws Exception
     */
    public String login2(String jumpUrl) throws Exception
    {
        //BaseUtils.loggerOut(grabSN189, "��½��2�� ��½����ת");
        PostMethod method = new PostMethod(jumpUrl);
        String respStr = connect(method);
        //BaseUtils.loggerOut(grabSN189, "�����ַ:" + jumpUrl);
        //BaseUtils.loggerOut(grabSN189, "�������ݣ�" + respStr);

        int index = respStr.indexOf("action=\"");
        String temp = respStr.substring(index + "action=\"".length());
        index = temp.indexOf("\"");
        String formAction = temp.substring(0, index);

        index = respStr.indexOf("name=\"SSORequestXML\" value=\"");
        temp = respStr.substring(index + "name=\"SSORequestXML\" value=\"".length());
        index = temp.indexOf("\"");
        String SSORequestXML = temp.substring(0, index);

        jumpUrl = formAction + "?SSORequestXML=" + SSORequestXML;
        //BaseUtils.loggerOut(grabSN189, "��ȡ��½��3����תURL:" + jumpUrl);

        // �ж���ͣץȡ
        BaseUtils.checkPause(grabSN189);

        /**
         * 20131020�޸�����������
         * ֱ�ӵ����Ĳ� ��ȡcookie
         */
        return login3(jumpUrl);
        //return login4(jumpUrl);
    }

    /**
     * ��½��3�� ��½����ת
     * ��ַ���ƣ�http://117.32.232.182/uam2/servlet/Login4CT10000Servlet?SSORequestXML=5bf036d2b6e8c5350d028b53b2305cff11122021b5dca214e74620403c25d31da4e8c2d0aa6f5628716011f862b070cbde1ad516b01353d290f4a4c3aaa3df6c1f18b341be1939f48d08c92b070741ebb18f4295498914a99abbdc0e4978e6b0eb42b5fec75785210b15a61afb6140eb4ebe5b3320da7d9a0fb257fd9db21e62fb8e014b078c4cc44ea8256e13407bfd72ea022e4ba389a24ef1cb67bae676cf2315bc99baab072f32b1c274d64e103bb0a87e49498081fca8450627072eed55b0a87e49498081fccd924dca1a37ba05d7323ff7359efcaf29308ddfe10a27760d03b2f023839fd083e1beed5f4414de1f9ada0c2fde73265bf3bd2c06d0190aad7bf47c8d6956b44b1b053b333002369d6686619585a5f043a6b84a45ca27de53d286d8a2a629585836cb624e99a6d030d6fc40fb660585258aa7e128d6a7560854e0426deb2ec2a6b7986a1bd178a6a941299fd53892f3780df0c02d92ead005e46a975ea745e3f39e16556d84c513a4d7b0e9a7caf1a06f7445714e1f88d76780150ebbe7734277145b4a49a9b794b090d73a40797d151cb9ba2020e84f0eb9da8215ce303d141222a94ca6bd09a12dc5cfa7c2a7f3648f16977a215fcc039ec0e18825da4941340f444a3ea79e28900e3cc9adcaa957c89fc21c5868d0114b55453b7607cc17873127c191bdc6b563fca07d678d68b12c2ce15401670769078a962b5322e1be1a6c019d5e20ab299fe25c6e96d4b8f4fcd2dbf784b556fed7f609a218acceb345ba15358ae54e06429a0ea308721f0ffc4fe74933806fc1aaaf564f13fdcf6e918cc6ba4d69661f6c33cc08d01df7ee0428a6d677bde84313e8fbccc3f0e63eb6325b07734c95cf9fe5fad79e12761aa00dd9a21133d93d56462b5977edd38e90f957216e8d3ea5
     * @param jumpUrl
     * @return
     * @throws Exception
     */
    public String login3(String jumpUrl) throws Exception
    {
        //BaseUtils.loggerOut(grabSN189, "��½��3�� ��½����ת");
        PostMethod method = new PostMethod(jumpUrl);
        String respStr = connect(method);
        //BaseUtils.loggerOut(grabSN189, "�����ַ:" + jumpUrl);
        //BaseUtils.loggerOut(grabSN189, "�������ݣ�" + respStr);

        int index = respStr.indexOf("action='");
        String temp = respStr.substring(index + "action='".length());
        index = temp.indexOf("'");
        String formAction = temp.substring(0, index);

        index = respStr.indexOf("name='UATicket' value='");
        temp = respStr.substring(index + "name='UATicket' value='".length());
        index = temp.indexOf("'");
        String UATicket = temp.substring(0, index);

        index = respStr.indexOf("name='SSORequestXML' value='");
        temp = respStr.substring(index + "name='SSORequestXML' value='".length());
        index = temp.indexOf("'");
        String SSORequestXML = temp.substring(0, index);

        jumpUrl = formAction + "UATicket=" + UATicket + "&SSORequestXML=" + SSORequestXML;
        //BaseUtils.loggerOut(grabSN189, "��ȡ��½��4����תURL:" + jumpUrl);

        return login4(jumpUrl);
    }

    /**
     * ��½��4�� ��½����ת
     * ��ַ���ƣ�http://sn.189.cn/sso/LoginServlet?UATicket=271766740&SSORequestXML=5bf036d2b6e8c5350d028b53b2305cff11122021b5dca214e74620403c25d31da4e8c2d0aa6f5628716011f862b070cbde1ad516b01353d290f4a4c3aaa3df6c1f18b341be1939f48d08c92b070741ebb18f4295498914a99abbdc0e4978e6b0eb42b5fec75785210b15a61afb6140eb4ebe5b3320da7d9a0fb257fd9db21e62fb8e014b078c4cc44ea8256e13407bfd72ea022e4ba389a24ef1cb67bae676cf2315bc99baab072f32b1c274d64e103bb0a87e49498081fca8450627072eed55b0a87e49498081fccd924dca1a37ba05d7323ff7359efcaf29308ddfe10a27760d03b2f023839fd083e1beed5f4414de76e1704ebf6cadce7a2d84befa175e54ad7bf47c8d6956b44b1b053b333002369d6686619585a5f043a6b84a45ca27de53d286d8a2a629585836cb624e99a6d030d6fc40fb660585258aa7e128d6a7560854e0426deb2ec2a6b7986a1bd178a6a941299fd53892f3780df0c02d92ead005e46a975ea745e3f39e16556d84c513a4d7b0e9a7caf1a06f7445714e1f88d76780150ebbe7734277145b4a49a9b794b090d73a40797d151cb9ba2020e84f0eb9da8215ce303d141222a94ca6bd09a12dc5cfa7c2a7f3648f16977a215fcc039ec0e18825da4941340f444a3ea79e28900e3cc9adcaa957c89fc21c5868d0114b55453b7607cc17873127c191bdc6b563fca07d678d68b12c2ce15401670769078a962b5322e1be1a6c019d5e20ab299fe25c6e96d4b8f4fcd2dbf784b556fed7f609a218acceb345ba15358ae54e06429a0ea308721f0ffc4fe74933806fc1aaaf564f13fdcf6e918cc6ba4d69661f6c33cc08d01df7ee0428a6d677bde84313e8fbccc3f0e63eb6325b07734c95cf9fe5fad79e12761aa00dd9a21133d93d56462b5977edd38e90f957216e8d3ea5
     * @param jumpUrl
     * @return
     * @throws Exception
     */
    public String login4(String jumpUrl) throws Exception
    {
        //BaseUtils.loggerOut(grabSN189, "��½��4�� ��½����ת");
        PostMethod method = new PostMethod(jumpUrl);
        String respStr = connect(method);
        //BaseUtils.loggerOut(grabSN189, "�����ַ:" + jumpUrl);
        //BaseUtils.loggerOut(grabSN189, "�������ݣ�" + respStr);

        // �ж���ͣץȡ
        BaseUtils.checkPause(grabSN189);

        for(int i=0;i<method.getResponseHeaders().length;i++)
        {
            if("Set-Cookie".equalsIgnoreCase(method.getResponseHeaders()[i].getName()))
            {
                String[] strs = method.getResponseHeaders()[i].getValue().split(";");
                for (int j=0;j<strs.length;j++) {
                    String str = strs[j];
                    String[] cookies = str.split("=");
                    if(cookies.length > 1){
                        if("COM.TYDIC.SSO_AUTH_TOKEN".equals(cookies[0].trim()))
                        {
                            //BaseUtils.loggerOut(grabSN189, "==============>ȡ��Cookie:[" + COOKIE_SSO_AUTO_TOKEN + "=" + cookies[1].trim() + "]");
                            return cookies[1].trim();
                        }
                    }
                }
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * ����ѯ: ��ͨ��� + �������
     * @return
     * @throws Exception
     */
    public void queryBal() throws Exception
    {
        //BaseUtils.loggerOut(grabSN189, "����ѯ");
        PostMethod method = new PostMethod(BAL_QUERY_URL);
        // ����cookie
        method.setRequestHeader("Cookie", COOKIE_SSO_AUTO_TOKEN + "=" + cookieValue);
        String respStr = connect(method);
        //BaseUtils.loggerOut(grabSN189, "�����ַ:" + BAL_QUERY_URL);
        //BaseUtils.loggerOut(grabSN189, "�������ݣ�" + respStr);

        int index = respStr.indexOf("<a onclick=\"showDetailBalance('" + loginName + "','4','290','1','0')\" class=\"green\">");
        String temp = respStr.substring(index + ("<a onclick=\"showDetailBalance('" + loginName + "','4','290','1','0')\" class=\"green\">").length());
        index = temp.indexOf("<");
        puTongYuE = temp.substring(0, index);//��ͨ���

        index = temp.indexOf("<a onclick=\"showDetailBalance('" + loginName + "','4','290','1','0')\" class=\"green\">");
        temp = temp.substring(index + ("<a onclick=\"showDetailBalance('" + loginName + "','4','290','1','0')\" class=\"green\">").length());
        index = temp.indexOf("<");
        qiTaYuE = temp.substring(0, index);//�������

        //BaseUtils.loggerOut(grabSN189, "puTongYuE=" + puTongYuE + ",qiTaYuE=" + qiTaYuE);
    }

    /**
     * ������ѯ(�ϸ���)
     * @throws Exception
     */
    public void queryFlowByLastMonth() throws Exception
    {
        //BaseUtils.loggerOut(grabSN189, "������ѯ(�ϸ���)");
        PostMethod method = new PostMethod(FLOW_QUERY_URL);
        //��¼����
        method.addParameter("currentPage", "1");
        method.addParameter("pageSize", "15");
        method.addParameter("effDate", getMinMonthDateOfLastMonth());//�ϸ�����ʼ����
        method.addParameter("expDate", getMaxMonthDateOfLastMonth());//�ϸ�����ֹ����
        method.addParameter("serviceNbr", loginName);
        method.addParameter("operListId", "12");
        method.addParameter("sendSmsFlag", "true");

        // ������ѯʱ���
        flowQueryPeriod = getMinMonthDateOfLastMonth() + "��" + getMaxMonthDateOfLastMonth();

        // ����cookie
        method.setRequestHeader("Cookie", COOKIE_SSO_AUTO_TOKEN + "=" + cookieValue);
        String respStr = connect(method);
        //BaseUtils.loggerOut(grabSN189, "�����ַ:" + FLOW_QUERY_URL);
        //BaseUtils.loggerOut(grabSN189, "�������ݣ�" + respStr);

        int index = respStr.indexOf("��&nbsp;&nbsp;��&nbsp;����");
        String temp = respStr.substring(index + ("��&nbsp;&nbsp;��&nbsp;����").length());
        index = temp.indexOf("<");
        totalFlow = temp.substring(0, index);//������

        index = respStr.indexOf("��&nbsp;&nbsp;ʱ&nbsp;����");
        temp = respStr.substring(index + ("��&nbsp;&nbsp;ʱ&nbsp;����").length());
        index = temp.indexOf("<");
        totalTime = temp.substring(0, index);//��ʱ��
        //BaseUtils.loggerOut(grabSN189, "totalFlow=" + totalFlow + ",totalTime=" + totalTime);
    }

    /**
     * �ײͲ�ѯ
     * @throws Exception
     */
    public void queryPackage() throws Exception
    {
        //BaseUtils.loggerOut(grabSN189, "�ײͲ�ѯ");
        PostMethod method = new PostMethod(PACKAGE_QUERY_URL);
        // ����cookie
        method.setRequestHeader("Cookie", COOKIE_SSO_AUTO_TOKEN + "=" + cookieValue);
        String respStr = connect(method);
        //BaseUtils.loggerOut(grabSN189, "�����ַ:" + PACKAGE_QUERY_URL);
        //BaseUtils.loggerOut(grabSN189, "�������ݣ�" + respStr);

        int index = respStr.indexOf("<td style=\"text-align:left;\">");
        String temp = respStr.substring(index + ("<td style=\"text-align:left;\">").length());
        index = temp.indexOf("<");
        packageName = temp.substring(0, index);//�ײ�����

        index = temp.indexOf("<td style=\"text-align:left;\">");
        temp = temp.substring(index + ("<td style=\"text-align:left;\">").length());
        index = temp.indexOf("<");
        packageInfo = temp.substring(0, index);//�ײ�ʹ�����
        //BaseUtils.loggerOut(grabSN189, "packageName=" + packageName + ",packageInfo=" + packageInfo);
    }

    /**
     * ����ת������
     * @throws Exception
     */
    public void callDivert() throws Exception
    {
        //BaseUtils.loggerOut(grabSN189, "����ת�Ƽ���");
        PostMethod method = new PostMethod(CALL_DIVERT_LOAD_URL);
        // ����cookie
        method.setRequestHeader("Cookie", COOKIE_SSO_AUTO_TOKEN + "=" + cookieValue);
        String respStr = connect(method);
        //BaseUtils.loggerOut(grabSN189, "�����ַ:" + CALL_DIVERT_LOAD_URL);
        //BaseUtils.loggerOut(grabSN189, "�������ݣ�" + respStr);

        int index = respStr.indexOf("name='divertInfoMap.uncondOldNum' value=\"");
        String temp = respStr.substring(index + ("name='divertInfoMap.uncondOldNum' value=\"").length());
        index = temp.indexOf("\"");
        String divertInfoMap_uncondOldNum = temp.substring(0, index);
        //BaseUtils.loggerOut(grabSN189, "divertInfoMap_uncondOldNum=" + divertInfoMap_uncondOldNum);

        index = respStr.indexOf("name='divertInfoMap.unanswerOldNum' value=\"");
        temp = respStr.substring(index + ("name='divertInfoMap.unanswerOldNum' value=\"").length());
        index = temp.indexOf("\"");
        String divertInfoMap_unanswerOldNum = temp.substring(0, index);
        //BaseUtils.loggerOut(grabSN189, "divertInfoMap_unanswerOldNum=" + divertInfoMap_unanswerOldNum);

        index = respStr.indexOf("name='divertInfoMap.callBusyOldNum' value=\"");
        temp = respStr.substring(index + ("name='divertInfoMap.callBusyOldNum' value=\"").length());
        index = temp.indexOf("\"");
        String divertInfoMap_callBusyOldNum = temp.substring(0, index);
        //BaseUtils.loggerOut(grabSN189, "divertInfoMap_callBusyOldNum=" + divertInfoMap_callBusyOldNum);

        index = respStr.indexOf("name=\"serviceNbr\" value=\"");
        temp = respStr.substring(index + ("name=\"serviceNbr\" value=\"").length());
        index = temp.indexOf("\"");
        String serviceNbr = temp.substring(0, index);
        //BaseUtils.loggerOut(grabSN189, "serviceNbr=" + serviceNbr);

        index = respStr.indexOf("name=\"divertInfoMap.uncondProdInsId\" value=\"");
        temp = respStr.substring(index + ("name=\"divertInfoMap.uncondProdInsId\" value=\"").length());
        index = temp.indexOf("\"");
        String divertInfoMap_uncondProdInsId = temp.substring(0, index);
        //BaseUtils.loggerOut(grabSN189, "divertInfoMap_uncondProdInsId=" + divertInfoMap_uncondProdInsId);

        index = respStr.indexOf("name=\"divertInfoMap.unanswerProdInsId\" value=\"");
        temp = respStr.substring(index + ("name=\"divertInfoMap.unanswerProdInsId\" value=\"").length());
        index = temp.indexOf("\"");
        String divertInfoMap_unanswerProdInsId = temp.substring(0, index);
        //BaseUtils.loggerOut(grabSN189, "divertInfoMap_unanswerProdInsId=" + divertInfoMap_unanswerProdInsId);

        index = respStr.indexOf("name=\"divertInfoMap.callBusyProdInsId\" value=\"");
        temp = respStr.substring(index + ("name=\"divertInfoMap.callBusyProdInsId\" value=\"").length());
        index = temp.indexOf("\"");
        String divertInfoMap_callBusyProdInsId = temp.substring(0, index);
        //BaseUtils.loggerOut(grabSN189, "divertInfoMap_callBusyProdInsId=" + divertInfoMap_callBusyProdInsId);

        //BaseUtils.loggerOut(grabSN189, "����ת������");
        method = new PostMethod(CALL_DIVERT_SET_URL);
        //��¼����
        method.addParameter("divertInfoMap.uncondNum", unConditionNum);
        method.addParameter("divertInfoMap.unanswerNum", unAnswerNum);
        method.addParameter("divertInfoMap.callBusyNum", callBusyNum);
        method.addParameter("divertInfoMap.uncondOldNum", divertInfoMap_uncondOldNum);
        method.addParameter("divertInfoMap.unanswerOldNum", divertInfoMap_unanswerOldNum);
        method.addParameter("divertInfoMap.callBusyOldNum", divertInfoMap_callBusyOldNum);
        method.addParameter("csrftoken", "");
        method.addParameter("serviceNbr", serviceNbr);
        method.addParameter("divertInfoMap.uncondProdInsId", divertInfoMap_uncondProdInsId);
        method.addParameter("divertInfoMap.unanswerProdInsId", divertInfoMap_unanswerProdInsId);
        method.addParameter("divertInfoMap.callBusyProdInsId", divertInfoMap_callBusyProdInsId);
        method.addParameter("divertInfoMap.uncondFlag", distBisinessType(divertInfoMap_uncondOldNum, unConditionNum));
        method.addParameter("divertInfoMap.unanswerFlag", distBisinessType(divertInfoMap_unanswerOldNum, unAnswerNum));
        method.addParameter("divertInfoMap.callBusyFlag", distBisinessType(divertInfoMap_callBusyOldNum, callBusyNum));

        // ����cookie
        method.setRequestHeader("Cookie", COOKIE_SSO_AUTO_TOKEN + "=" + cookieValue);
        respStr = connect(method);
        //BaseUtils.loggerOut(grabSN189, "�����ַ:" + CALL_DIVERT_SET_URL);
        //BaseUtils.loggerOut(grabSN189, "�������ݣ�" + respStr);

        if(respStr.indexOf("ҵ������ɹ���") > -1)
        {
            callDivertSetResult = "���óɹ���";
            if(StringUtils.isBlank(unConditionNum) && StringUtils.isBlank(unAnswerNum) &&
                    StringUtils.isBlank(callBusyNum))
            {
                callDivertSetResult = "������ȡ����";
            }
        }
        //BaseUtils.loggerOut(grabSN189, "callDivertSetResult=" + callDivertSetResult);
    }

    /**
     * �������ͨѶ
     */
    public String connect(PostMethod method) throws Exception
    {
        InputStream is = null;
        BufferedReader br = null;
        try
        {
            HttpClient client = new HttpClient();
            int returnCode = client.executeMethod(method);

            if (HttpURLConnection.HTTP_OK != returnCode)
            {
                //logger.error("����������������쳣,returnCode=" + returnCode);
            }
            is = method.getResponseBodyAsStream();
            br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), "UTF-8"));
            StringBuffer response = new StringBuffer();
            String readLine;
            while (((readLine = br.readLine()) != null))
            {
                response.append(readLine + "\r\n");
            }
            return response.toString();
        } catch (Exception e)
        {
            //logger.error("����������������쳣~", e);
            throw new RuntimeException("����������������쳣~", e);
        } finally
        {
            try
            {
                if (null != method)
                {
                    method.releaseConnection();
                }
                if (null != is)
                {
                    is.close();
                }
                if (null != br)
                {
                    br.close();
                }
            } catch (Exception e)
            {
                //logger.error("����������������쳣~", e);
            }
        }
    }

    /**
     * ��js�Ĵ��� ����� java����
     //�����û����������޸ġ��ر� ҵ�����
     function vierdictType(oVal,nVal){
     var type = '';//Ĭ�����޸Ĳ���
     if(oVal=='' && nVal!=''){ //�ύʱ��ѡ�� ����֮ǰû�к��룬��������
     type = '1'; //����
     }else if(oVal!='' && nVal==''){//�ύʱûѡ��֮ǰ�к��룬���ǹر�
     type = '3'; //�ر�
     }else if(nVal!='' && (oVal!=nVal)){
     type = '2' //�޸�
     }else{
     type = '0'; //�����޸ģ���ǰû�а����ҵ������Ҳδ����
     }
     return type;
     }
     */
    public String distBisinessType(String oldVal, String newVal)
    {
        String type = "";//Ĭ�����޸Ĳ���
        if(StringUtils.isBlank(oldVal) && StringUtils.isNotBlank(newVal)){ //�ύʱ��ѡ�� ����֮ǰû�к��룬��������
            type = "1"; //����
        }else if(StringUtils.isNotBlank(oldVal) && StringUtils.isBlank(newVal)){//�ύʱûѡ��֮ǰ�к��룬���ǹر�
            type = "3"; //�ر�
        }else if(StringUtils.isNotBlank(newVal) && (!StringUtils.equals(oldVal, newVal))){
            type = "2"; //�޸�
        }else{
            type = "0"; //�����޸ģ���ǰû�а����ҵ������Ҳδ����
        }
        return type;
    }

    /**
     * ��ȡ�·���ʼ����
     * @return
     * @throws java.text.ParseException
     */
    public static Date getMinMonthDate() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * ��ȡ�ϸ��·���ʼ����
     * @return
     * @throws java.text.ParseException
     */
    public static String getMinMonthDateOfLastMonth() throws ParseException {
        Date thisMonthMinDate = getMinMonthDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(thisMonthMinDate);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return dateFormat.format(calendar.getTime());
    }

    /**
     * ��ȡ�ϸ��·��������
     * @return
     * @throws java.text.ParseException
     */
    public static String getMaxMonthDateOfLastMonth() throws ParseException{
        Date thisMonthMinDate = getMinMonthDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(thisMonthMinDate);
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dateFormat.format(calendar.getTime());
    }
}