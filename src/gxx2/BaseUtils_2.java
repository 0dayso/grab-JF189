package gxx2;

import gxx.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import sun.misc.BASE64Decoder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;

/**
 * ����������
 * Create by Gxx
 * Time: 2013-10-09 00:46
 */
public class BaseUtils_2 implements BaseInterface_2
{
    /**
     * ��־�ļ����
     */
    public static Logger logger = Logger.getLogger(BaseUtils_2.class);

    /**
     * ��ʼ��������(��ǰϵͳ�ķ��)
     */
    public static void initLookAndFeel()
    {
        String lookAndFeel = UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ��½ǰ�ļ���cookie����֤��
     */
    public static Map loadCheckImg(InputDto_2 dto){
        //���ݵ�¼���ͻ�ȡ��¼��ַ
        String loginUrl = BaseUtils_2.getLoginUrlByLoginType(dto.getLoginType());
        // 1.���ʵ�½�����ַ����ȡcookie��spring web flow��(lt)
        Map result = HttpClientUtils.getUrl(loginUrl, "UTF-8", "GBK", new Cookie[]{});//���ؽ��
        boolean isSuccess = (Boolean)result.get(KEY_IS_SUCCESS);//���Ƿ�ɹ�
        if(!isSuccess)
        {
            return result;
        }
        Cookie[] cookies = (Cookie[])result.get(KEY_RESPONSE_COOKIES);//����ͷ
        String webContent = (String)result.get(KEY_RESP_STR);//��ҳ����
        String springWebFlow = getSpringWebFlow(webContent);//������ҳ��ַ�õ�springWebFlow

        // 2.����ͼƬ��ַ������ͼƬ
        result = HttpClientUtils.downloadImage(IMAGE_URL + Math.random(), cookies);//���ؽ��
        isSuccess = (Boolean)result.get(KEY_IS_SUCCESS);//���Ƿ�ɹ�
        if(!isSuccess)
        {
            return result;
        }
        String checkImgRoute = (String)result.get(KEY_CHECK_IMG_ROUTE);
        result = new HashMap();
        result.put(KEY_IS_SUCCESS, isSuccess);
        result.put(KEY_SPRING_WEB_FLOW, springWebFlow);
        result.put(KEY_RESPONSE_COOKIES, cookies);
        result.put(KEY_CHECK_IMG_ROUTE, checkImgRoute);
        return result;
    }

    /**
     * ���ݵ�¼���ͻ�ȡ��¼��ַ
     * @param loginType
     * @return
     */
    public static String getLoginUrlByLoginType(String loginType) {
        if(LOGIN_TYPE_MB.equals(loginType)){
            return LOGIN_URL;
        } else if(LOGIN_TYPE_PH.equals(loginType)){
            return LOGIN_URL_FOR_GUHUA;
        } else if(LOGIN_TYPE_CARD.equals(loginType)){
            return LOGIN_URL_FOR_CARD;
        } else if(LOGIN_TYPE_NET.equals(loginType)){
            return LOGIN_URL_FOR_NET;
        } else if(LOGIN_TYPE_SLYH.equals(loginType)){
            return LOGIN_URL_FOR_SLUSER;
        } else if(LOGIN_TYPE_TWYH.equals(loginType)){
            return LOGIN_URL_FOR_ONUSER;
        } else {
            throw new RuntimeException("��Ч�ĵ�¼��ʽ��");
        }
    }

    /**
     * ������ҳ��ַ�õ�springWebFlow
     * @param webContent
     * @return
     */
    public static String getSpringWebFlow(String webContent) {
        String indexStr = "name=\"lt\" value=\"";
        webContent = webContent.substring(webContent.indexOf(indexStr) + indexStr.length());
        indexStr = "\"";
        String springWebFlow = webContent.substring(0, webContent.indexOf(indexStr));
        logger.info(springWebFlow);
        return springWebFlow;
    }

    /**
     * ������֤��ͼƬ�ļ���
     * @return
     */
    public static String getImgCacheDir(){
        /**
         * �������� ͼƬ��������
         * return "C:\\Users\\sky\\Desktop\\";
         */
//        return "C:\\Users\\sky\\Desktop\\";//todo
        return getSameDirWithLib() + "image_cache\\";
    }

    /**
     * �õ������ļ�Ŀ¼
     * @return
     */
    public static String getSaveFileDir(){
        /**
         * �������� ͼƬ��������
         * return "C:\\Users\\sky\\Desktop\\";
         */
//        return "C:\\Users\\sky\\Desktop\\";//todo
        return getSameDirWithLib() + "save\\";
    }

    /**
     * �ύ
     * @param userName
     * @param password
     * @param check
     * @param springWebFlow
     * @param cookies
     * @return
     */
    public static Map loginMB(GrabJF189_2 grabJF189, String userName, String password, String check, String springWebFlow, Cookie[] cookies)
        throws Exception{
        String province = BaseUtils_2.getProvinceByMobile(grabJF189, userName);
        String provinceNum = province.substring(0, province.indexOf("|"));
        String provinceStr = province.substring(province.indexOf("|") + "|".length());

        PostMethod method = new PostMethod(LOGIN_URL);
        //����
        method.addParameter("forbidpass", "null");
        method.addParameter("forbidaccounts", "null");
        method.addParameter("authtype", "c2000004");
        method.addParameter("open_no", "1");
        method.addParameter("customFileld02", provinceNum);
        method.addParameter("areaname", provinceStr);
        method.addParameter("submitBtn1", "�����ύ.....");
        method.addParameter("_eventId", "submit");
        method.addParameter("c2000004RmbMe", "on");
        method.addParameter("customFileld01", "3");
        method.addParameter("password_r", "");
        method.addParameter("password_c", "");
        //�û���
        method.addParameter("username", userName);
        //����
        method.addParameter("password", password);
        //ͼƬ��֤��
        method.addParameter("randomId", check);
        //spring web flow��
        method.addParameter("lt", springWebFlow);

        for(NameValuePair nameValuePair :  method.getParameters()){
            logger.info(nameValuePair.getName() + "=" + nameValuePair.getValue());
        }

        //�������
        Map result = HttpClientUtils.connect(method, "UTF-8", cookies);//���ؽ��
        //���ؽ��
        return result;
    }

    /**
     * �ύ
     * @param userName
     * @param password
     * @param check
     * @param springWebFlow
     * @param cookies
     * @return
     */
    public static Map loginPH(GrabJF189_2 grabJF189, String userName, String password, String check, String springWebFlow, Cookie[] cookies)
            throws Exception{
        String provinceText = grabJF189.centerPanel.provinceComboBox.getSelectedItem().toString();
        String cityText = grabJF189.centerPanel.cityComboBox.getSelectedItem().toString();
        String provinceNum = StringUtils.EMPTY;
        String provinceStr = StringUtils.EMPTY;
        if(provinceText.indexOf(",") > -1){
            provinceNum = provinceText.substring(0, provinceText.indexOf(","));
            provinceStr = provinceText.substring(provinceText.indexOf(",") + 1);
        }
        String zoneNum = StringUtils.EMPTY;
        String zoneName = StringUtils.EMPTY;
        String showZoneName = StringUtils.EMPTY;
        if(cityText.indexOf(",") > -1){
            zoneName = cityText.substring(0, cityText.indexOf(","));
            zoneNum = cityText.substring(cityText.indexOf(",") + 1);
            showZoneName = zoneName;
        }

        PostMethod method = new PostMethod(LOGIN_URL_FOR_GUHUA);
        //����
        method.addParameter("forbidpass", "null");
        method.addParameter("forbidaccounts", "null");
        method.addParameter("authtype", "c2000001");
        method.addParameter("open_no", "c2000001");
        method.addParameter("customFileld01", "3");
        method.addParameter("_eventId", "submit");
        method.addParameter("submitBtn1", "�����ύ.....");
        method.addParameter("c2000001RmbMe", "on");
        method.addParameter("password_r", "");
        method.addParameter("password_c", "");

        //��ݵ���
        method.addParameter("customFileld02", provinceNum);
        method.addParameter("areaname", provinceStr);
        method.addParameter("customFileld03", zoneNum);
        method.addParameter("zoneName", zoneName);
        method.addParameter("showZoneName", showZoneName);
        //�û���
        method.addParameter("username", userName);
        //����
        method.addParameter("password", password);
        //ͼƬ��֤��
        method.addParameter("randomId", check);
        //spring web flow��
        method.addParameter("lt", springWebFlow);

        //�������
        Map result = HttpClientUtils.connect(method, "GBK", cookies);//���ؽ��
        return result;
    }

    /**
     * �ύ
     * @param userName
     * @param password
     * @param check
     * @param springWebFlow
     * @param cookies
     * @return
     */
    public static Map loginNet(GrabJF189_2 grabJF189, String userName, String password, String check, String springWebFlow, Cookie[] cookies)
            throws Exception{
        String provinceText = grabJF189.centerPanel.provinceComboBox.getSelectedItem().toString();
        String cityText = grabJF189.centerPanel.cityComboBox.getSelectedItem().toString();
        String provinceNum = StringUtils.EMPTY;
        String provinceStr = StringUtils.EMPTY;
        if(provinceText.indexOf(",") > -1){
            provinceNum = provinceText.substring(0, provinceText.indexOf(","));
            provinceStr = provinceText.substring(provinceText.indexOf(",") + 1);
        }
        String zoneNum = StringUtils.EMPTY;
        String zoneName = StringUtils.EMPTY;
        String showZoneName = StringUtils.EMPTY;
        if(cityText.indexOf(",") > -1){
            zoneName = cityText.substring(0, cityText.indexOf(","));
            zoneNum = cityText.substring(cityText.indexOf(",") + 1);
            showZoneName = zoneName;
        }

        PostMethod method = new PostMethod(LOGIN_URL_FOR_NET);
        //����
        method.addParameter("forbidpass", "null");
        method.addParameter("forbidaccounts", "null");
        method.addParameter("authtype", "c2000002");
        method.addParameter("open_no", "c2000002");
        method.addParameter("customFileld01", "3");
        method.addParameter("_eventId", "submit");
        method.addParameter("submitBtn1", "�����ύ.....");
        //method.addParameter("c2000001RmbMe", "on");
        method.addParameter("password_r", "");
        method.addParameter("password_c", "");

        //��ݵ���
        method.addParameter("customFileld02", provinceNum);
        method.addParameter("areaname", provinceStr);
        method.addParameter("customFileld03", zoneNum);
        method.addParameter("zoneName", zoneName);
        method.addParameter("showZoneName", showZoneName);
        //�û���
        method.addParameter("username", userName);
        //����
        method.addParameter("password", password);
        //ͼƬ��֤��
        method.addParameter("randomId", check);
        //spring web flow��
        method.addParameter("lt", springWebFlow);

        //�������
        Map result = HttpClientUtils.connect(method, "GBK", cookies);//���ؽ��
        return result;
    }

    /**
     * �ύ
     * @param userName
     * @param password
     * @param check
     * @param springWebFlow
     * @param cookies
     * @return
     */
    public static Map loginCard(GrabJF189_2 grabJF189, String userName, String password, String check, String springWebFlow, Cookie[] cookies)
            throws Exception{
        String provinceText = grabJF189.centerPanel.provinceComboBox.getSelectedItem().toString();
        String cityText = grabJF189.centerPanel.cityComboBox.getSelectedItem().toString();
        String provinceNum = StringUtils.EMPTY;
        String provinceStr = StringUtils.EMPTY;
        if(provinceText.indexOf(",") > -1){
            provinceNum = provinceText.substring(0, provinceText.indexOf(","));
            provinceStr = provinceText.substring(provinceText.indexOf(",") + 1);
        }
        String zoneNum = StringUtils.EMPTY;
        String zoneName = StringUtils.EMPTY;
        String showZoneName = StringUtils.EMPTY;
        if(cityText.indexOf(",") > -1){
            zoneName = cityText.substring(0, cityText.indexOf(","));
            zoneNum = cityText.substring(cityText.indexOf(",") + 1);
            showZoneName = zoneName;
        }

        PostMethod method = new PostMethod(LOGIN_URL_FOR_CARD);
        //����
        method.addParameter("forbidpass", "null");
        method.addParameter("forbidaccounts", "null");
        method.addParameter("authtype", "c0000001");
        method.addParameter("open_no", "0000001");
        method.addParameter("customFileld01", "1");
        method.addParameter("_eventId", "submit");
        method.addParameter("submitBtn1", "�����ύ.....");
        method.addParameter("0000001RmbMe", "on");
        //method.addParameter("password_r", "");
        //method.addParameter("password_c", "");

        //��ݵ���
        method.addParameter("customFileld02", provinceNum);
        method.addParameter("areaname", provinceStr);
        //method.addParameter("customFileld03", zoneNum);
        //method.addParameter("zoneName", zoneName);
        //method.addParameter("showZoneName", showZoneName);
        //�û���
        method.addParameter("username", userName);
        //����
        method.addParameter("password", password);
        //ͼƬ��֤��
        method.addParameter("randomId", check);
        //spring web flow��
        method.addParameter("lt", springWebFlow);

        //�������
        Map result = HttpClientUtils.connect(method, "GBK", cookies);//���ؽ��
        return result;
    }

    /**
     * ����ת
     * @param jumpUrl
     */
    public static Map jump(GrabJF189_2 grabJF189, String jumpUrl) {
        // 4.��½��֤�ɹ�����һ����ת����ַλ��ResponseHeaders�еĵ�Location
        logger.info("��½��֤�ɹ�����һ����ת����ַ:" + jumpUrl);
        Map result = new HashMap();
        try{
            result = HttpClientUtils.getUrl(jumpUrl, "UTF-8", "GBK", new Cookie[]{});//���ؽ�� ���Բ���cookie
        } catch (Exception e){
            result.put(KEY_IS_SUCCESS, false);
            String errorMessage = "��½��֤��һ����תʧ�ܣ������µ�½��";
            BaseUtils_2.loggerOut(grabJF189, errorMessage);
            BaseUtils_2.alertErrorMessage(errorMessage);
            return result;
        }
        boolean isSuccess = (Boolean)result.get(KEY_IS_SUCCESS);//���Ƿ�ɹ�
        if(!isSuccess)
        {
            String errorMessage = "��½��֤��һ����תʧ�ܣ������µ�½��";
            BaseUtils_2.loggerOut(grabJF189, errorMessage);
            BaseUtils_2.alertErrorMessage(errorMessage);
            return result;
        }
        String webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//��ҳ����
        logger.info("��һ����ת����������" + webContent);
        Map headerMap = (Map)result.get(HttpClientUtils.KEY_RESPONSE_HEADER_MAP);//����ͷ
        //cookies = (Cookie[])result.get(HttpClientUtils.KEY_RESPONSE_COOKIES);//����ͷ

        // 5.�ڶ�����ת������ ��һ����ת �������ݵõ��ڶ�����ת��ַ
        jumpUrl = getJumpUrl2(webContent);
        logger.info("�ڶ�����ת����ַ:" + jumpUrl);
        result = HttpClientUtils.getUrl(jumpUrl, "UTF-8", "GBK", new Cookie[]{});//���ؽ�� ���Բ���cookie
        isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//���Ƿ�ɹ�
        if(!isSuccess)
        {
            String errorMessage = "��½��֤�ڶ�����תʧ�ܣ������µ�½��";
            BaseUtils_2.loggerOut(grabJF189, errorMessage);
            BaseUtils_2.alertErrorMessage(errorMessage);
            return result;
        }
        webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//��ҳ����
        logger.info("�ڶ�����ת����������" + webContent);
        headerMap = (Map)result.get(HttpClientUtils.KEY_RESPONSE_HEADER_MAP);//����ͷ
        //cookies = (Cookie[])result.get(HttpClientUtils.KEY_RESPONSE_COOKIES);//����ͷ

        // 6.��������ת������ �ڶ�����ת �������ݵõ���������ת��ַ
        jumpUrl = getJumpUrl3(webContent);
        logger.info("��������ת����ַ:" + jumpUrl);
        result = HttpClientUtils.getUrl(jumpUrl, "UTF-8", "GBK", new Cookie[]{});//���ؽ�� ���Բ���cookie
        isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//���Ƿ�ɹ�
        if(!isSuccess)
        {
            String errorMessage = "��½��֤��������תʧ�ܣ������µ�½��";
            BaseUtils_2.loggerOut(grabJF189, errorMessage);
            BaseUtils_2.alertErrorMessage(errorMessage);
            return result;
        }
        result.put(KEY_BROWSE_URL1, jumpUrl);//��������ת��ַ
        webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//��ҳ����
        logger.info("��������ת����������" + webContent);
        return result;

        //==============================================================================================
        //��ȡ��Ʒ�б� ���۸����� http://mall.jf.189.cn/Goods/GoodsList
        //http://mall.jf.189.cn/Goods/CanBuyGoodsList
        //http://mall.jf.189.cn:8000/Goods/CanBuyGoodsList

//        String canBuyUrl = "http://mall.jf.189.cn/Order/SellOrder?GoodsSn=bbb2a86c-bd84-4665-8947-ddd9f9" +
//                "9bd6fa&GoodsAttSn=0a1ba597-3cf8-4799-b7ee-55c118f78a89&GoodNum=1";
//        logger.info("���ĸ�url:" + canBuyUrl);
//        result = HttpClientUtils.getUrl(canBuyUrl, "UTF-8", "GBK", cookies);
//        //���ؽ�� ���Բ���cookie ���Ƿ��ػ��cookie
//        isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//���Ƿ�ɹ�
//        if(!isSuccess)
//        {
//            logger.info("ʧ�ܣ�");//�Ƿ���ֹ
//            return;
//        }
//        webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//��ҳ����
//        logger.info("+++++++++++��������" + webContent);
//        cookies = (Cookie[])result.get(HttpClientUtils.KEY_RESPONSE_COOKIES);//����ͷ

//        logger.info("�����url:" + canBuyUrl);
//        result = HttpClientUtils.getUrl(canBuyUrl, "UTF-8", "GBK", cookies);
//        //���ؽ�� ���Բ���cookie ���Ƿ��ػ��cookie
//        isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//���Ƿ�ɹ�
//        if(!isSuccess)
//        {
//            logger.info("ʧ�ܣ�");//�Ƿ���ֹ
//            return;
//        }
//        webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//��ҳ����
//        logger.info("+++++++++++��������" + webContent);
//        //cookies = (Cookie[])result.get(HttpClientUtils.KEY_RESPONSE_COOKIES);//����ͷ
    }

    /**
     * ���� ��һ����ת �������ݵõ��ڶ�����ת��ַ
     * @param webContent
     * @return
     */
    public static String getJumpUrl2(String webContent) {
        String indexStr = "location.replace('";
        String jumpUrl = webContent.substring(webContent.indexOf(indexStr) + indexStr.length());
        indexStr = "'";
        jumpUrl = jumpUrl.substring(0, jumpUrl.indexOf(indexStr));
        return jumpUrl;
    }

    /**
     * ���� �ڶ�����ת �������ݵõ���������ת��ַ
     * @param webContent
     * @return
     */
    public static String getJumpUrl3(String webContent) {
        String indexStr = "window.top.location.href=\"";
        String jumpUrl = webContent.substring(webContent.indexOf(indexStr) + indexStr.length());
        indexStr = "\"";
        jumpUrl = jumpUrl.substring(0, jumpUrl.indexOf(indexStr));
        return jumpUrl;
    }

    /**
     * ����ҳ����ȡ�ֽ��Ʒ�б�
     * @param page
     * @return
     */
    public static Map getMoneyGoodsListByPage(GrabJF189_2 grabJF189, int page) {
        String message = "����ҳ��[" + page + "]��ȡ�ֽ��Ʒ�б�";
        BaseUtils_2.loggerOut(grabJF189, message);

        List<MoneyGoods> moneyGoodsList = new ArrayList<MoneyGoods>();

        String moneyGoodsListUrl = "http://jf.189.cn/duihuan/ncommoditylist.aspx";
        logger.info("����ҳ����ȡ�ֽ��Ʒ�б�url:" + moneyGoodsListUrl);
        PostMethod method = new PostMethod(moneyGoodsListUrl);
        //����
        method.addParameter("__VIEWSTATE", "/wEPDwUKMTgzNzgzNjYxMA8WAh4ERmxhZwUBMhYCAgUPZBYUZg9kFgJmDxYCHglpbm5lcmh0bWwF3ycNCiAgICAgICAgICAgIDxESVYgaWQ9YWxsc29ydCBjbGFzcz1tPg0KICAgICAgICAgICAgICA8RElWIGNsYXNzPW1jPjxESVYgY2xhc3M9J2l0ZW0nPjxTUEFOPjxIND48QSBocmVmPSdOQ29tbW9kaXR5TGlzdC5hc3B4P1R5cGU9MDEnPuaJi+acuuaVsOeggTwvQT48L0g0PjxJTlM+Jm5ic3A7LSZuYnNwO+e9kee7nOiuvuWkh3zmkYTlvbHmkYTlg488L0lOUz48L1NQQU4+PERJVj48REw+PERUPjwvRFQ+PEREPjxFTT48QSBocmVmPSdOQ29tbW9kaXR5TGlzdC5hc3B4P1R5cGU9MDEwMSc+572R57uc6K6+5aSHPC9BPjwvRU0+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wMTAzJz7mkYTlvbHmkYTlg488L0E+PC9FTT48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTAxMDQnPuaJi+acuumFjeS7tjwvQT48L0VNPjxFTT48QSBocmVmPSdOQ29tbW9kaXR5TGlzdC5hc3B4P1R5cGU9MDEwNSc+5pWw56CB5ZGo6L65PC9BPjwvRU0+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wMTA2Jz7lvbHpn7Porr7lpIc8L0E+PC9FTT48L0REPjwvREw+PC9ESVY+PC9ESVY+PERJViBjbGFzcz0naXRlbSc+PFNQQU4+PEg0PjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wMic+5a625bGF55m+6LSnPC9BPjwvSDQ+PElOUz4mbmJzcDstJm5ic3A755Sf5rS75pel55SofOW3peWFt+Wll+ijhTwvSU5TPjwvU1BBTj48RElWPjxETD48RFQ+PC9EVD48REQ+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wMjAzJz7nlJ/mtLvml6XnlKg8L0E+PC9FTT48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTAyMDQnPuW3peWFt+Wll+ijhTwvQT48L0VNPjxFTT48QSBocmVmPSdOQ29tbW9kaXR5TGlzdC5hc3B4P1R5cGU9MDIwNSc+5Yqe5YWs55So5ZOBPC9BPjwvRU0+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wMjExJz7lrrbnurrml6XnlKg8L0E+PC9FTT48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTAyMTQnPuWOqOWFt+eUqOWTgTwvQT48L0VNPjwvREQ+PC9ETD48L0RJVj48L0RJVj48RElWIGNsYXNzPSdpdGVtJz48U1BBTj48SDQ+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTAzJz7pkp/ooajjgIHphY3ppbA8L0E+PC9IND48SU5TPiZuYnNwOy0mbmJzcDvpkp/ooajjgIHmiYvooah86YWN6aWw44CB54+g5a6dPC9JTlM+PC9TUEFOPjxESVY+PERMPjxEVD48L0RUPjxERD48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTAzMDEnPumSn+ihqOOAgeaJi+ihqDwvQT48L0VNPjxFTT48QSBocmVmPSdOQ29tbW9kaXR5TGlzdC5hc3B4P1R5cGU9MDMwNCc+6YWN6aWw44CB54+g5a6dPC9BPjwvRU0+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wMzA2Jz7nnLzplZzjgIHnrrHljIU8L0E+PC9FTT48L0REPjwvREw+PC9ESVY+PC9ESVY+PERJViBjbGFzcz0naXRlbSc+PFNQQU4+PEg0PjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wNCc+5Liq5oqk576O5aaGPC9BPjwvSDQ+PElOUz4mbmJzcDstJm5ic3A76a2F5Yqb5b2p5aaGfOmdoumDqOaKpOeQhjwvSU5TPjwvU1BBTj48RElWPjxETD48RFQ+PC9EVD48REQ+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wNDAxJz7prYXlipvlvanlpoY8L0E+PC9FTT48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTA0MDQnPumdoumDqOaKpOeQhjwvQT48L0VNPjxFTT48QSBocmVmPSdOQ29tbW9kaXR5TGlzdC5hc3B4P1R5cGU9MDQwNSc+6Lqr5L2T5oqk55CGPC9BPjwvRU0+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wNDA2Jz7nvo7lrrnlt6Xlhbc8L0E+PC9FTT48L0REPjwvREw+PC9ESVY+PC9ESVY+PERJViBjbGFzcz0naXRlbSc+PFNQQU4+PEg0PjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wNSc+5q+N5am044CB546p5YW3PC9BPjwvSDQ+PElOUz4mbmJzcDstJm5ic3A75YS/56ul546p5YW3fOWWguWFu+eUqOWTgTwvSU5TPjwvU1BBTj48RElWPjxETD48RFQ+PC9EVD48REQ+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wNTAyJz7lhL/nq6Xnjqnlhbc8L0E+PC9FTT48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTA1MDMnPuWWguWFu+eUqOWTgTwvQT48L0VNPjxFTT48QSBocmVmPSdOQ29tbW9kaXR5TGlzdC5hc3B4P1R5cGU9MDUwNCc+5a+d5YW35pyN6aWwPC9BPjwvRU0+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wNTA1Jz7mtJfmiqTnlKjlk4E8L0E+PC9FTT48L0REPjwvREw+PC9ESVY+PC9ESVY+PERJViBjbGFzcz0naXRlbSc+PFNQQU4+PEg0PjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wNic+5p2C5b+X5pyf5YiKPC9BPjwvSDQ+PElOUz4mbmJzcDstJm5ic3A75pe25bCa5p2C5b+XfOeDremUgOS5puWIijwvSU5TPjwvU1BBTj48RElWPjxETD48RFQ+PC9EVD48REQ+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wNjAzJz7ml7blsJrmnYLlv5c8L0E+PC9FTT48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTA2MDQnPueDremUgOS5puWIijwvQT48L0VNPjwvREQ+PC9ETD48L0RJVj48L0RJVj48RElWIGNsYXNzPSdpdGVtJz48U1BBTj48SDQ+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTA3Jz7po5/lk4HjgIHojLblj7bjgIHnibnkvps8L0E+PC9IND48SU5TPiZuYnNwOy0mbmJzcDvnsq7msrnosIPlkbN85rig6YGT54m55L6bPC9JTlM+PC9TUEFOPjxESVY+PERMPjxEVD48L0RUPjxERD48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTA3MDEnPueyruayueiwg+WRszwvQT48L0VNPjxFTT48QSBocmVmPSdOQ29tbW9kaXR5TGlzdC5hc3B4P1R5cGU9MDcwMic+5rig6YGT54m55L6bPC9BPjwvRU0+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wNzAzJz7nsr7lk4HojLblj7Y8L0E+PC9FTT48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTA3MDknPuWcsOaWueeJueS6pzwvQT48L0VNPjwvREQ+PC9ETD48L0RJVj48L0RJVj48RElWIGNsYXNzPSdpdGVtJz48U1BBTj48SDQ+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTA4Jz7omZrmi5/npLzlk4E8L0E+PC9IND48SU5TPiZuYnNwOy0mbmJzcDvnlLXlrZDljaHliLh855S15L+h5Lqn5ZOBPC9JTlM+PC9TUEFOPjxESVY+PERMPjxEVD48L0RUPjxERD48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTA4MDYnPueUteWtkOWNoeWIuDwvQT48L0VNPjxFTT48QSBocmVmPSdOQ29tbW9kaXR5TGlzdC5hc3B4P1R5cGU9MDgxNic+55S15L+h5Lqn5ZOBPC9BPjwvRU0+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wODE3Jz7lupfpnaLlhZHmjaI8L0E+PC9FTT48L0REPjwvREw+PC9ESVY+PC9ESVY+PERJViBjbGFzcz0naXRlbSc+PFNQQU4+PEg0PjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wOSc+5aSp57+85a6i5oi35L+x5LmQ6YOoPC9BPjwvSDQ+PElOUz4mbmJzcDstJm5ic3A76L+Q5Yqo5L+x5LmQ6YOofOaRhOW9seS/seS5kOmDqDwvSU5TPjwvU1BBTj48RElWPjxETD48RFQ+PC9EVD48REQ+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wOTAxJz7ov5Dliqjkv7HkuZDpg6g8L0E+PC9FTT48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTA5MDInPuaRhOW9seS/seS5kOmDqDwvQT48L0VNPjxFTT48QSBocmVmPSdOQ29tbW9kaXR5TGlzdC5hc3B4P1R5cGU9MDkwMyc+5b2x6L+35L+x5LmQ6YOoPC9BPjwvRU0+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wOTA0Jz7ml4Xlj4vkv7HkuZDpg6g8L0E+PC9FTT48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTA5MDUnPjEw5YWD55yL5aSn54mHPC9BPjwvRU0+PC9ERD48L0RMPjwvRElWPjwvRElWPjxESVYgY2xhc3M9J2l0ZW0nPjxTUEFOPjxIND48QSBocmVmPSdOQ29tbW9kaXR5TGlzdC5hc3B4P1R5cGU9MTAnPuWutueUqOeUteWZqDwvQT48L0g0PjxJTlM+Jm5ic3A7LSZuYnNwO+eUn+a0u+WutueUtXzljqjmiL/nlLXlmag8L0lOUz48L1NQQU4+PERJVj48REw+PERUPjwvRFQ+PEREPjxFTT48QSBocmVmPSdOQ29tbW9kaXR5TGlzdC5hc3B4P1R5cGU9MTAwMSc+55Sf5rS75a6255S1PC9BPjwvRU0+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0xMDAyJz7ljqjmiL/nlLXlmag8L0E+PC9FTT48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTEwMDMnPuS4quS6uuaKpOeQhjwv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");//Ĭ������
        method.addParameter("__EVENTTARGET", "");//Ĭ������
        method.addParameter("__EVENTARGUMENT", "");//Ĭ������
        method.addParameter("SelOrder", "0");//Ĭ������
        method.addParameter("PAYTYPE", "0");//Ĭ������
        method.addParameter("AspNetPager1_input", StringUtils.EMPTY + page);
        method.addParameter("AspNetPager1", "go");
        method.addParameter("AspNetPager2_input", "1");
        Map result;//���ؽ��
        try {
            result = HttpClientUtils.connect(method, "GBK", new Cookie[]{});
        } catch (Exception e) {
            e.printStackTrace();
            result = new HashMap();
            result.put(HttpClientUtils.KEY_IS_SUCCESS, false);
            return result;
        }
        //���ؽ�� ���Բ���cookie ���Ƿ��ػ��cookie
        boolean isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//���Ƿ�ɹ�
        if(!isSuccess)
        {
            String errorMessage = "����ҳ��[" + page + "]��ȡ�ֽ��Ʒ�б�ʧ��";
            BaseUtils_2.loggerOut(grabJF189, errorMessage);
            BaseUtils_2.alertErrorMessage(errorMessage);
            return result;
        }
        String webContent = (String)result.get(KEY_RESP_STR);//��ҳ����
        logger.info("+++++++++++��������" + webContent);

        String indexStr = "<span class='productImg2'><a href='productsInfo.aspx?ID=";
        int index = webContent.indexOf(indexStr);
        while (index > -1)
        {
            webContent = webContent.substring(index + indexStr.length());
            indexStr = "'";
            index = webContent.indexOf(indexStr);
            String goodsId = webContent.substring(0, index);

            webContent = webContent.substring(index + indexStr.length());
            indexStr = "<IMG title='";
            index = webContent.indexOf(indexStr);
            webContent = webContent.substring(index + indexStr.length());
            indexStr = "'";
            index = webContent.indexOf(indexStr);
            String goodsName = webContent.substring(0, index);

            webContent = webContent.substring(index + indexStr.length());
            indexStr = "src='";
            index = webContent.indexOf(indexStr);
            webContent = webContent.substring(index + indexStr.length());
            indexStr = "'";
            index = webContent.indexOf(indexStr);
            String image = webContent.substring(0, index);

            webContent = webContent.substring(index + indexStr.length());
            indexStr = "������֣�<strong class='orange'>";
            index = webContent.indexOf(indexStr);
            webContent = webContent.substring(index + indexStr.length());
            indexStr = "����</strong>";
            index = webContent.indexOf(indexStr);
            int price = Integer.parseInt(webContent.substring(0, index));
            webContent = webContent.substring(index + indexStr.length());

            logger.info("goodsId=" + goodsId + ",goodsName=" + goodsName + ",image=" + image +
                    ",price=" + price);
            MoneyGoods goods = new MoneyGoods(goodsId, goodsName, price, image);
            moneyGoodsList.add(goods);

            indexStr = "<span class='productImg2'><a href='productsInfo.aspx?ID=";
            index = webContent.indexOf(indexStr);
        }

        indexStr = "<span id=\"LBMsg2\">��";
        index = webContent.indexOf(indexStr);
        webContent = webContent.substring(index + indexStr.length());
        indexStr = "/";
        index = webContent.indexOf(indexStr);
        page = Integer.parseInt(webContent.substring(0, index));

        int pageSize = moneyGoodsList.size();

        webContent = webContent.substring(index + indexStr.length());
        indexStr = "ҳ";
        index = webContent.indexOf(indexStr);
        int pageCount = Integer.parseInt(webContent.substring(0, index));
        logger.info("page=" + page + ",pageSize=" + pageSize + ",pageCount=" + pageCount);

        MoneyGoodsList moneyGoodsList1 = new MoneyGoodsList(page, pageSize, pageCount, moneyGoodsList);
        result.put(KEY_MONEY_GOODS_LIST, moneyGoodsList1);
        return result;
    }

    /**
     * ����ҳ����ȡ��Ʒ�б� todo
     * @param page
     * @param cookies
     * @return
     */
    public static Map getGoodsListByPage(GrabJF189_2 grabJF189, int page, Cookie[] cookies) {
        String goodsListUrl = "http://mall.jf.189.cn/Goods/GetGoodsListInfo?categoryType=3&" +
                "ExchangeCountOrderType=0&PriceOrderType=3&AddDateOrderType=0&p=" + page;
        logger.info("���ĸ�url:" + goodsListUrl);
        Map result = HttpClientUtils.getUrl(goodsListUrl, "UTF-8", "GBK", cookies);
        //���ؽ�� ���Բ���cookie ���Ƿ��ػ��cookie
        boolean isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//���Ƿ�ɹ�
        if(!isSuccess)
        {
            String errorMessage = "����ҳ��[" + page + "]��ȡ��Ʒ�б�ʧ��";
            BaseUtils_2.loggerOut(grabJF189, errorMessage);
            BaseUtils_2.alertErrorMessage(errorMessage);
            return result;
        }
        String webContent = (String)result.get(KEY_RESP_STR);//��ҳ����
        logger.info("+++++++++++��������" + webContent);
        cookies = (Cookie[])result.get(HttpClientUtils.KEY_RESPONSE_COOKIES);//����ͷ

        //webContent = EscapeUtils.unescape(webContent);//data.replaceAll("\\\\", "%")
        //webContent = webContent.substring(1, webContent.length()-1);
        JSONObject object = (JSONObject) new JSONTokener( webContent ).nextValue();
        Map ret = new HashMap();
        for ( Iterator iterator = object.entrySet().iterator(); iterator.hasNext(); )
        {
            Map.Entry entry = (Map.Entry) iterator.next();
            ret.put( entry.getKey(), entry.getValue() );
        }
        result = new HashMap();
        result.put(KEY_IS_SUCCESS, "TRUE".equalsIgnoreCase((String) ret.get("Result")));
        result.put(KEY_RESPONSE_COOKIES, cookies);
        int pageSize = (Integer)ret.get("ListCount");
        int pageCount = getPageCountFromPageBar((String)ret.get("PageBarHtmlSimple"));
        JSONArray json = JSONArray.fromObject(ret.get("GoodsList"));
        List<Goods> goodsList = new ArrayList<Goods>();
        for(int i=0;i<json.size();i++)
        {
            JSONObject temp = json.getJSONObject(i);
            String GoodsSn = (StringUtils.trimToEmpty((String) temp.get("GoodsSn")));
            String GoodsAttSn = (StringUtils.trimToEmpty((String) temp.get("GoodsAttSn")));
            String GoodsName = (StringUtils.trimToEmpty((String) temp.get("GoodsName")));
            int IntegralPrice = (Integer)temp.get("IntegralPrice") + 2288;
            String image = (StringUtils.trimToEmpty((String) temp.get("Originallmage")));
            logger.info("GoodsSn=" + GoodsSn + ",GoodsAttSn=" + GoodsAttSn + ",GoodsName=" + GoodsName +
                    ",IntegralPrice=" + IntegralPrice + ",image=" + image);
            Goods goods = new Goods(GoodsSn, GoodsAttSn, GoodsName, IntegralPrice, image);
            goodsList.add(goods);
        }
        GoodsList goodsList1 = new GoodsList(page, pageSize, pageCount, goodsList);
        result.put(KEY_MONEY_GOODS_LIST, goodsList1);//todo
        return result;
    }

    /**
     * �õ���ҳ��
     * @param pageBarSimple
     * @return
     */
    public static int getPageCountFromPageBar(String pageBarSimple){
        pageBarSimple = pageBarSimple.substring(pageBarSimple.indexOf("/") + 1);
        return Integer.parseInt(pageBarSimple.substring(0, pageBarSimple.indexOf("<")));
    }

    public static String getViewStateFromMakeOrder(String webContent) {
        String indexStr = "id=\"__VIEWSTATE\" value=\"";
        webContent = webContent.substring(webContent.indexOf(indexStr) + indexStr.length());
        indexStr = "\"";
        String viewState = webContent.substring(0, webContent.indexOf(indexStr));
        return viewState;
    }

    public static String getOrderUrl(String webContent) {
        String indexStr = "Object moved to <a href=\"";
        webContent = webContent.substring(webContent.indexOf(indexStr) + indexStr.length());
        indexStr = "\"";
        String url = "http://jf.189.cn" + webContent.substring(0, webContent.indexOf(indexStr)).replaceAll("%2f", "/").replaceAll("%3f", "?").replaceAll("%3d", "=");
        return url;
    }

    /**
     * ˢ������
     */
    public static void refreshNetWork(GrabJF189_2 grabJF189){
        try {
            //ͨ��process��ʽ����windows����������ִ��
            String cmd = "ipconfig /release";
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), "GBK"));
            String msg;
            while ((msg = br.readLine()) != null) {
                logger.info(msg);
                BaseUtils_2.loggerOut(grabJF189, msg);//todo
            }

            //ͨ��process��ʽ����windows����������ִ��
            cmd = "ipconfig /renew";
            p = Runtime.getRuntime().exec(cmd);
            br = new BufferedReader(new InputStreamReader(p.getInputStream(), "GBK"));
            while ((msg = br.readLine()) != null) {
                logger.info(msg);
                BaseUtils_2.loggerOut(grabJF189, msg);//todo
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ��ȡip��ַ
     * @return
     * @throws UnknownHostException
     */
    public static String getIpAddress(GrabJF189_2 grabJF189) {
        String url = "http://www.ip.cn/";
        Map result = HttpClientUtils.getUrl(url, "UTF-8", "GBK", new Cookie[]{});
        boolean isSuccess = (Boolean)result.get(KEY_IS_SUCCESS);//���Ƿ�ɹ�
        if(!isSuccess)
        {
            String errorMessage = "��ȡip��ַʧ�ܣ�";
            BaseUtils_2.loggerOut(grabJF189, errorMessage);
            BaseUtils_2.alertErrorMessage(errorMessage);
            return StringUtils.EMPTY;
        }
        String webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//��ҳ����
        logger.info("��ȡ��½��Ϣ����������" + webContent);
        String indexStr = "<p>��ǰ IP��<code>";
        int index = webContent.indexOf(indexStr);
        webContent = webContent.substring(index + indexStr.length());
        indexStr = "</code>";
        index = webContent.indexOf(indexStr);
        String outIp = webContent.substring(0, index);
        return outIp;
    }

    /**
     * У��Excel�Ƿ���ȷ
     * @param path
     */
    public static void checkExcel(String path, GrabJF189_2 grabJF189) throws Exception
    {
        loggerOut(grabJF189, "����Excel·��:[" + path + "]");
        HSSFWorkbook workbook;
        HSSFSheet sheet;

        // 1 װ���ļ����Ƿ����쳣
        try {
            workbook = new HSSFWorkbook(new FileInputStream(path));
            sheet = workbook.getSheetAt(0);
        } catch (Exception e) {
            loggerOut(grabJF189, "װ���ļ��쳣");
            throw new RuntimeException("װ���ļ��쳣��" + path);
        }

        // 2 ���Ƿ�������
        int totalRowNum = sheet.getLastRowNum();
        loggerOut(grabJF189, "�ļ�����=" + (totalRowNum+1) + "(����̧ͷ)");
        if(totalRowNum <= 0)
        {
            throw new RuntimeException("���ļ�������");
        }

        // 3 У���һ�������Ƿ�һ��
        HSSFRow row = sheet.getRow(0);
        int totalCellNum = row.getLastCellNum();
        loggerOut(grabJF189, "�ļ�����=" + totalCellNum);
        if(EXCEL_TITLE.length != totalCellNum)//�Ƚϸ���
        {
            alertWarnMessage("���ļ���ģ�岻һ�£������������ļ���");
            return;
        }

        int index = 0;
        while (index < EXCEL_TITLE.length)//�Ƚ��Ƿ�һ��
        {
            try{
                if(!EXCEL_TITLE[index].equals(getHSSFCellValue(row.getCell(index++))))
                {
                    alertWarnMessage("���ļ���ģ�岻һ�£������������ļ���");
                    return;
                }
            } catch (Exception e)
            {
                alertWarnMessage("���ļ���ģ�岻һ�£������������ļ���");
                return;
            }
        }

        /**
         * 4 ��ȡ���ݵ�table��
         */
        readExcel2Table(sheet,grabJF189);

        /**
         * 5 ��������У��
         */
        for(int i=0;i<grabJF189.inputDtoList.size();i++)
        {
            InputDto_2 dto = (InputDto_2)grabJF189.inputDtoList.get(i);
            if(dto.isValidate)
            {
                if((null == dto.mobile) || "".equals(dto.password))
                {
                    dto.isValidate = false;
                    dto.errorMsg = "�ֻ��ź����벻��Ϊ�գ�";
                } else if(!isValidLoginType(dto.getLoginType()))
                {
                    dto.isValidate = false;
                    dto.errorMsg = "��¼��������";
                } else
                {
                    dto.isValidate = true;
                }
            }
        }
    }

    /**
     * �е�¼�����Ƿ�����
     * @param loginType
     * @return
     */
    public static boolean isValidLoginType(String loginType){
        if(StringUtils.equals(LOGIN_TYPE_MB, loginType) || StringUtils.equals(LOGIN_TYPE_PH, loginType) ||
                StringUtils.equals(LOGIN_TYPE_NET, loginType) || StringUtils.equals(LOGIN_TYPE_CARD, loginType) ||
                StringUtils.equals(LOGIN_TYPE_SLYH, loginType) || StringUtils.equals(LOGIN_TYPE_TWYH, loginType)){
            return true;
        }
        return false;
    }

    /**
     * ��ʾ��Ϣ
     * @param message
     */
    public static void alertMessage(String message)
    {
        JOptionPane.showMessageDialog(null, message, "��Ϣ", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * ������Ϣ
     * @param message
     */
    public static void alertWarnMessage(String message)
    {
        JOptionPane.showMessageDialog(null, message, "��Ϣ", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * ������Ϣ
     * @param message
     */
    public static void alertErrorMessage(String message)
    {
        JOptionPane.showMessageDialog(null, message, "��Ϣ", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * ����־д��loggerTextPane
     * @param grabJF189
     * @param string
     */
    public static void loggerOutWithNoLine(GrabJF189_2 grabJF189, String string)
    {
        grabJF189.loggerTextArea.append(string);
        logger.info(string);
    }

    /**
     * ����־д��loggerTextPane
     * @param grabJF189
     * @param string
     */
    public static void loggerOut(GrabJF189_2 grabJF189, String string)
    {
        grabJF189.loggerTextArea.append(string + "\r\n");
        logger.info(string);
    }

    /**
     * �õ�jar��·��
     * @return
     */
    public static String getJarPath()
    {
        JarUtil ju = new JarUtil(JarUtil.class);
        return ju.getJarPath() + "/";
    }

    /**
     * �������ǲ���ͨ��
     * @return
     */
    public static boolean isNetWorkLinked()
    {
        boolean isNetWorkLinked = false;
        for (int urlsCount = 0; urlsCount < CHECK_NET_WORK_LINKED_URLS.length; urlsCount++) {
            if (canGetHtmlCode(CHECK_NET_WORK_LINKED_URLS[urlsCount])) {
                isNetWorkLinked = true;
                break;
            }
        }
        return isNetWorkLinked;
    }

    /**
     * ���Ƿ��ܵõ�HTML����
     * @param httpUrl
     * @return
     */
    public static boolean canGetHtmlCode(String httpUrl) {
        String htmlCode = "";
        try {
            InputStream in;
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/4.0");
            connection.connect();
            in = connection.getInputStream();
            byte[] buffer = new byte[50];
            in.read(buffer);
            htmlCode = new String(buffer);
        } catch (Exception e) {
        }
        if (htmlCode == null || htmlCode.equals("")) {
            return false;
        }
        return true;
    }

    /**
     * �õ���Ԫ���ֵ
     * @param cell
     * @return
     */
    public static String getHSSFCellValue(HSSFCell cell)
    {
        if(null == cell)
        {
            return StringUtils.EMPTY;
        }
        Object cellValue = null;
//        String[] valueArr = mapvalue.split(",");
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING://�ַ�������
                cellValue = cell.getRichStringCellValue().getString();
                if (((String)cellValue).trim().equals("")
                        || ((String)cellValue).trim().length() <= 0) {
                    cellValue = "";
                }
                //cellValue = inputEncode((String)cellValue);
                break;
            case HSSFCell.CELL_TYPE_NUMERIC://��������
//                if (valueArr.length == 2 && valueArr[1].equals("date")) {
//                    cellValue = cell.getDateCellValue();;
//                }
//                if (valueArr.length == 2 && valueArr[1].equals("timestamp")) {
//                    Date date = cell.getDateCellValue();
//                    SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String time=format1.format(date);
//                    cellValue= Timestamp.valueOf(time);
//                }else { // �������Ϊ2��˵������ΪĬ���ַ�������
                BigDecimal big = new BigDecimal(cell.getNumericCellValue());
                // cellValue =big.toEngineeringString();
                cellValue = big.toString();
//                }
                break;
            case HSSFCell.CELL_TYPE_FORMULA:
                BigDecimal bigula = new BigDecimal(cell
                        .getCachedFormulaResultType());
                // cellValue = bigula.toEngineeringString();
                cellValue = bigula.toString();
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                cellValue = "";
                break;
            default:
                break;
        }
        return null == cellValue?"":cellValue.toString();
    }

    /**
     * ��ȡ���ݵ�table��
     * @param sheet
     * @param grabJF189
     */
    private static void readExcel2Table(HSSFSheet sheet, GrabJF189_2 grabJF189)
    {
        // 0 ��ʼ��inputDtoList
        grabJF189.inputDtoList = new ArrayList();

        // 1 ��������� �� ��ʼ��ͷһ��
        clearAllAndInitTitle(grabJF189.table);

        // 2 �õ�tableModel
        DefaultTableModel tableModel = (DefaultTableModel) grabJF189.table.getModel();

        // 2 ��������+�޸�table+����inputDtoList
        int totalRowNum = sheet.getLastRowNum();
        for(int i=0;i<totalRowNum;i++)
        {
            boolean isRowError = false;
            tableModel.addRow(EMPTY_STRING_ARRAY);
            HSSFRow tempRow = sheet.getRow(i+1);
            for(int j=0;j<tempRow.getLastCellNum();j++)
            {
                try
                {
                    String value = getHSSFCellValue(tempRow.getCell(j));
                    grabJF189.table.setValueAt(value,i+1, j);
                } catch (Exception e)
                {
                    isRowError = true;
                    grabJF189.table.setValueAt("",i+1, j);
                }
            }

            InputDto_2 dto;
            try
            {
//                try{
//                    Integer.parseInt(getHSSFCellValue(tempRow.getCell(4)));
//                } catch (Exception e){
//                    tempRow.getCell(4).setCellValue(0);
//                }

                dto = new InputDto_2(getHSSFCellValue(tempRow.getCell(0)), getHSSFCellValue(tempRow.getCell(1)), getHSSFCellValue(tempRow.getCell(2)),
                        getHSSFCellValue(tempRow.getCell(3)), getHSSFCellValue(tempRow.getCell(4)), getHSSFCellValue(tempRow.getCell(5)));
            } catch (Exception e)
            {
                e.printStackTrace();
                isRowError = true;
                dto = new InputDto_2("", "", "", "", "", "");
            }

            if(isRowError)
            {
                dto.isValidate = false;
                dto.errorMsg = "������������ʧ�ܣ�";
            }
            /**
             * �����汾��
             * 1.ֻ֧���ֻ���¼��ʽ
             * 2.֧�ָ��ֵ�¼��ʽ
             */
            /**
             * //������δ��룬��ֻ֧���ֻ���¼��ʽ
             * if(!dto.getLoginType().equals("�ֻ�")){
             *      dto.isValidate = false;
             *      dto.errorMsg = "�˰汾ֻ֧���ֻ���¼��ʽ��";
             * }
             */
            grabJF189.inputDtoList.add(dto);
        }
    }

    /**
     * ��������� �� ��ʼ��ͷһ��
     * @param table
     */
    public static void clearAllAndInitTitle(JTable table)
    {
        // 1 �õ�tableModel
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        // 2 ��������� �� ����ͷһ��
        for(int i=tableModel.getRowCount()-1;i>=1;i--)
        {
            tableModel.removeRow(i);
        }

        // 3 ����ͷһ�е�Title
        for(int i=0;i<EXCEL_TITLE.length;i++)
        {
            tableModel.setValueAt(EXCEL_TITLE[i], 0, i);
        }
    }

    /**
     * ��������� �� ��ʼ��ͷһ��
     * @param table
     */
    public static void clearSuccessAllAndInitTitle(JTable table)
    {
        // 1 �õ�tableModel
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        // 3 ����ͷһ�е�Title
        for(int i=0;i<DETAIL_EXCEL_TITLE.length;i++)
        {
            tableModel.setValueAt(DETAIL_EXCEL_TITLE[i], 0, i);
        }
    }

    /**
     * ��ȡ��½��Ϣ
     * @param cookies
     * @return
     */
    public static Map getLoginInfo(GrabJF189_2 grabJF189, Cookie[] cookies) {
        String url = "http://jf.189.cn/home/ajax.ashx?g=nocachelogin";
        logger.info("��½�ɹ�,��ȡ��½��Ϣurl=" + url);
        Map result = HttpClientUtils.getUrl(url, "UTF-8", "GBK", cookies);//���ؽ�� ���Բ���cookie
        boolean isSuccess = (Boolean)result.get(KEY_IS_SUCCESS);//���Ƿ�ɹ�
        if(!isSuccess)
        {
            String errorMessage = "��ȡ��½��Ϣʧ�ܣ�";
            BaseUtils_2.loggerOut(grabJF189, errorMessage);
            BaseUtils_2.alertErrorMessage(errorMessage);
            return result;
        }
        String webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//��ҳ����
        logger.info("��ȡ��½��Ϣ����������" + webContent);
        JSONObject object = (JSONObject) new JSONTokener( webContent ).nextValue();
        Map ret = new HashMap();
        for ( Iterator iterator = object.entrySet().iterator(); iterator.hasNext(); )
        {
            Map.Entry entry = (Map.Entry) iterator.next();
            ret.put( entry.getKey(), entry.getValue() );
        }
        result.put(KEY_USER_NAME, Native2AsciiUtil.ascii2Native((String) ret.get("UserRealName")));
        result.put(KEY_JI_FEN, StringUtils.EMPTY + ret.get("UserIntegral"));
        return result;
    }

    /**
     * ��ѯ�������
     * @param orderId
     * @param cookies
     * @return
     * @throws Exception
     */
    public static Map orderDetail(String orderId, Cookie[] cookies) throws Exception {
        String url = "http://jf.189.cn/SelfHelp/OrderDetail.aspx?S=0&ORDERID=" + orderId;
        Map result = HttpClientUtils.getUrl(url, "UTF-8", "GBK", cookies);//���ؽ�� ���Բ���cookie
        return result;
    }

    /**
     * ���ݵ绰�����ȡʡ����Ϣ
     *
     * @param mobile
     * @return
     */
    public static String getProvinceByMobile(GrabJF189_2 grabJF189, String mobile){
        String url = "https://uam.ct10000.com/ct10000uam/FindPhoneAreaServlet";
        PostMethod method = new PostMethod(url);
        //����
        method.addParameter("username", mobile);

        //�������
        try {
            Map result = HttpClientUtils.connect(method, "UTF-8", new Cookie[]{});//���ؽ��
            //���ؽ�� ���Բ���cookie ���Ƿ��ػ��cookie
            boolean isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//���Ƿ�ɹ�
            if(!isSuccess)
            {
                String errorMessage = "��ȡʡ����Ϣʧ�ܣ�";
                BaseUtils_2.loggerOut(grabJF189, errorMessage);
                BaseUtils_2.alertErrorMessage(errorMessage);
                return "|";
            }
            String webContent = (String)result.get(KEY_RESP_STR);//��ҳ����
            logger.info("+++++++++++��������" + webContent);
            return webContent;
        } catch (Exception e) {
            e.printStackTrace();
            return "|";
        }
    }

    /**
     * ��ӳɹ���¼�������
     *
     * @param successTable
     * @param order
     * @param successCount
     */
    public static void putSuccessTable(JTable successTable, Order order, int successCount){
        // 2 �õ�tableModel
        DefaultTableModel tableModel = (DefaultTableModel) successTable.getModel();
        tableModel.addRow(EMPTY_STRING_ARRAY);
        successTable.setValueAt(order.getMobile(), successCount, 0);
        successTable.setValueAt(order.getUserName(), successCount, 1);

        successTable.setValueAt(order.getGoodsId(), successCount, 2);
        successTable.setValueAt(order.getGoodsName(), successCount, 3);
        successTable.setValueAt(StringUtils.EMPTY + order.getSingleJiFen(), successCount, 4);
        successTable.setValueAt(StringUtils.EMPTY + order.getBuyNum(), successCount, 5);

        successTable.setValueAt(order.getOrderId(), successCount, 6);
        successTable.setValueAt(order.isSuccess()?"�ɹ�":"ʧ��", successCount, 7);//�ɹ��ű���
        successTable.setValueAt(order.getCardNo(), successCount, 8);
        successTable.setValueAt(order.getPassword(), successCount, 9);
        successTable.setValueAt(order.getDeadline(), successCount, 10);
    }

    /**
     * �����������ҳ��
     * @param url
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InterruptedException
     * @throws InvocationTargetException
     * @throws IOException
     * @throws NoSuchMethodException
     */
    public static void browse(String url) {
        try{
            String osName = System.getProperty("os.name", "");
            if (osName.startsWith("Mac OS")) {
                Class fileMgr = Class.forName("com.apple.eio.FileManager");
                Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
                openURL.invoke(null, new Object[] { url });
            } else if (osName.startsWith("Windows")) {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else { // assume Unix or Linux
                String[] browsers = { "firefox", "opera", "konqueror", "epiphany", "mozilla", "netscape" };
                String browser = null;
                for (int count = 0; count < browsers.length && browser == null; count++)
                    if (Runtime.getRuntime().exec(new String[] { "which", browsers[count] }).waitFor() == 0)
                        browser = browsers[count];
                if (browser == null)
                    throw new NoSuchMethodException("Could not find web browser");
                else
                    Runtime.getRuntime().exec(new String[] { browser, url });
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * ����mobile����InputDto
     * @param mobile
     * @param grabJF189
     * @return
     */
    public static InputDto_2 getInputDtoByMobile(String mobile, GrabJF189_2 grabJF189){
        for(Object inputDto_2 : grabJF189.inputDtoList){
            if(((InputDto_2)inputDto_2).getMobile().equals(mobile)){
                return (InputDto_2)inputDto_2;
            }
        }
        return null;
    }

    /**
     * ����Excel
     * @param grabJF189
     */
    public static void saveExcel(GrabJF189_2 grabJF189) throws Exception
    {
        /**
         * 1 ���� �û������룬������ʣ����֣��ɹ�����
         */
        // ���Ƿ���ع�Excel
        if(StringUtils.isBlank(grabJF189.rightPanel.filePath))
        {
            alertWarnMessage("��ǰδ����Excel�������Excel��");
            return;
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet;

        // 1.1 װ���ļ����Ƿ����쳣
        sheet = workbook.createSheet();

        // 1.2 �õ�tableModel
        DefaultTableModel tableModel = (DefaultTableModel) grabJF189.table.getModel();

        // 1.3 ��table�����ݿ�����excel��
        for(int i=0;i<tableModel.getRowCount();i++)
        {
            HSSFRow row = sheet.createRow(i);
            row.createCell(0);
            row.getCell(0).setCellValue((String) tableModel.getValueAt(i, 0));
            row.createCell(1);
            row.getCell(1).setCellValue((String) tableModel.getValueAt(i, 1));
            row.createCell(2);
            row.getCell(2).setCellValue((String) tableModel.getValueAt(i, 2));
            row.createCell(3);
            row.getCell(3).setCellValue((String) tableModel.getValueAt(i, 3));
            row.createCell(4);
            row.getCell(4).setCellValue((String) tableModel.getValueAt(i, 4));
            row.createCell(4);
            row.getCell(4).setCellValue((String) tableModel.getValueAt(i, 5));
        }

        // 1.4 �����ļ�
        String fileName = new File(grabJF189.rightPanel.filePath).getName();
        // ������ jar������libĿ¼��ͬ��Ŀ¼out�� �����ļ����� �������.xls��β
        String path = getSaveFileDir() + fileName.substring(0, fileName.lastIndexOf(".xls"))
                + "�������" + grabJF189.fetch + ".xls";
        FileOutputStream fOut = new FileOutputStream(path);
        workbook.write(fOut);
        fOut.flush();
        fOut.close();
        loggerOut(grabJF189, "���[�û��������]excel�ļ��ɹ���·��[" + path + "]");

        /**
         * 2 ���� �ɹ���ˮ��������Ϣ �û���Ϣ�ȵ�
         */
        workbook = new HSSFWorkbook();

        // 2.1 װ���ļ����Ƿ����쳣
        sheet = workbook.createSheet();

        // 2.2 �õ�tableModel
        tableModel = (DefaultTableModel) grabJF189.centerPanel.successTable.getModel();

        // 1.3 ��table�����ݿ�����excel��
        for(int i=0;i<tableModel.getRowCount();i++)
        {
            HSSFRow row = sheet.createRow(i);
            row.createCell(0);
            row.getCell(0).setCellValue((String) tableModel.getValueAt(i, 0));
            row.createCell(1);
            row.getCell(1).setCellValue((String) tableModel.getValueAt(i, 1));
            row.createCell(2);
            row.getCell(2).setCellValue((String) tableModel.getValueAt(i, 2));
            row.createCell(3);
            row.getCell(3).setCellValue((String) tableModel.getValueAt(i, 3));
            row.createCell(4);
            row.getCell(4).setCellValue((String) tableModel.getValueAt(i, 4));
            row.createCell(5);
            row.getCell(5).setCellValue((String) tableModel.getValueAt(i, 5));
            row.createCell(6);
            row.getCell(6).setCellValue((String) tableModel.getValueAt(i, 6));
            row.createCell(7);
            row.getCell(7).setCellValue((String) tableModel.getValueAt(i, 7));
            row.createCell(8);
            row.getCell(8).setCellValue((String) tableModel.getValueAt(i, 8));
            row.createCell(9);
            row.getCell(9).setCellValue((String) tableModel.getValueAt(i, 9));
            row.createCell(10);
            row.getCell(10).setCellValue((String) tableModel.getValueAt(i, 10));
        }

        // 1.4 �����ļ�
        // ������ jar������libĿ¼��ͬ��Ŀ¼out�� �����ļ����� �������.xls��β
        path = getSaveFileDir() + fileName.substring(0, fileName.lastIndexOf(".xls")) + "�ɹ�����" + grabJF189.fetch + ".xls";
        fOut = new FileOutputStream(path);
        workbook.write(fOut);
        fOut.flush();
        fOut.close();
        loggerOut(grabJF189, "���[�ɹ�����]excel�ļ��ɹ���·��[" + path + "]");

        // 3 ��ʾ����Excel�ɹ�
        alertMessage("�����������ɹ���");
    }

    /**
     * �õ�jar������libĿ¼
     * @return
     */
    public static String getSameDirWithLib(){
//        return "D:\\04.my_projects\\grab-JF189\\";//todo
        return getJarPath().substring(0, getJarPath().lastIndexOf("lib"));
    }
}
