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
 * 基础工具类
 * Create by Gxx
 * Time: 2013-10-09 00:46
 */
public class BaseUtils_2 implements BaseInterface_2
{
    /**
     * 日志文件输出
     */
    public static Logger logger = Logger.getLogger(BaseUtils_2.class);

    /**
     * 初始化界面风格(当前系统的风格)
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
     * 登陆前的加载cookie和验证码
     */
    public static Map loadCheckImg(InputDto_2 dto){
        //根据登录类型获取登录地址
        String loginUrl = BaseUtils_2.getLoginUrlByLoginType(dto.getLoginType());
        // 1.访问登陆界面地址，获取cookie和spring web flow流(lt)
        Map result = HttpClientUtils.getUrl(loginUrl, "UTF-8", "GBK", new Cookie[]{});//返回结果
        boolean isSuccess = (Boolean)result.get(KEY_IS_SUCCESS);//判是否成功
        if(!isSuccess)
        {
            return result;
        }
        Cookie[] cookies = (Cookie[])result.get(KEY_RESPONSE_COOKIES);//返回头
        String webContent = (String)result.get(KEY_RESP_STR);//网页内容
        String springWebFlow = getSpringWebFlow(webContent);//解析网页地址得到springWebFlow

        // 2.访问图片地址，下载图片
        result = HttpClientUtils.downloadImage(IMAGE_URL + Math.random(), cookies);//返回结果
        isSuccess = (Boolean)result.get(KEY_IS_SUCCESS);//判是否成功
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
     * 根据登录类型获取登录地址
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
            throw new RuntimeException("无效的登录方式！");
        }
    }

    /**
     * 解析网页地址得到springWebFlow
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
     * 返回验证码图片文件夹
     * @return
     */
    public static String getImgCacheDir(){
        /**
         * 本地运行 图片放在桌名
         * return "C:\\Users\\sky\\Desktop\\";
         */
//        return "C:\\Users\\sky\\Desktop\\";//todo
        return getSameDirWithLib() + "image_cache\\";
    }

    /**
     * 得到保存文件目录
     * @return
     */
    public static String getSaveFileDir(){
        /**
         * 本地运行 图片放在桌名
         * return "C:\\Users\\sky\\Desktop\\";
         */
//        return "C:\\Users\\sky\\Desktop\\";//todo
        return getSameDirWithLib() + "save\\";
    }

    /**
     * 提交
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
        //参数
        method.addParameter("forbidpass", "null");
        method.addParameter("forbidaccounts", "null");
        method.addParameter("authtype", "c2000004");
        method.addParameter("open_no", "1");
        method.addParameter("customFileld02", provinceNum);
        method.addParameter("areaname", provinceStr);
        method.addParameter("submitBtn1", "正在提交.....");
        method.addParameter("_eventId", "submit");
        method.addParameter("c2000004RmbMe", "on");
        method.addParameter("customFileld01", "3");
        method.addParameter("password_r", "");
        method.addParameter("password_c", "");
        //用户名
        method.addParameter("username", userName);
        //密码
        method.addParameter("password", password);
        //图片验证码
        method.addParameter("randomId", check);
        //spring web flow流
        method.addParameter("lt", springWebFlow);

        for(NameValuePair nameValuePair :  method.getParameters()){
            logger.info(nameValuePair.getName() + "=" + nameValuePair.getValue());
        }

        //结果集合
        Map result = HttpClientUtils.connect(method, "UTF-8", cookies);//返回结果
        //返回结果
        return result;
    }

    /**
     * 提交
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
        //参数
        method.addParameter("forbidpass", "null");
        method.addParameter("forbidaccounts", "null");
        method.addParameter("authtype", "c2000001");
        method.addParameter("open_no", "c2000001");
        method.addParameter("customFileld01", "3");
        method.addParameter("_eventId", "submit");
        method.addParameter("submitBtn1", "正在提交.....");
        method.addParameter("c2000001RmbMe", "on");
        method.addParameter("password_r", "");
        method.addParameter("password_c", "");

        //身份地区
        method.addParameter("customFileld02", provinceNum);
        method.addParameter("areaname", provinceStr);
        method.addParameter("customFileld03", zoneNum);
        method.addParameter("zoneName", zoneName);
        method.addParameter("showZoneName", showZoneName);
        //用户名
        method.addParameter("username", userName);
        //密码
        method.addParameter("password", password);
        //图片验证码
        method.addParameter("randomId", check);
        //spring web flow流
        method.addParameter("lt", springWebFlow);

        //结果集合
        Map result = HttpClientUtils.connect(method, "GBK", cookies);//返回结果
        return result;
    }

    /**
     * 提交
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
        //参数
        method.addParameter("forbidpass", "null");
        method.addParameter("forbidaccounts", "null");
        method.addParameter("authtype", "c2000002");
        method.addParameter("open_no", "c2000002");
        method.addParameter("customFileld01", "3");
        method.addParameter("_eventId", "submit");
        method.addParameter("submitBtn1", "正在提交.....");
        //method.addParameter("c2000001RmbMe", "on");
        method.addParameter("password_r", "");
        method.addParameter("password_c", "");

        //身份地区
        method.addParameter("customFileld02", provinceNum);
        method.addParameter("areaname", provinceStr);
        method.addParameter("customFileld03", zoneNum);
        method.addParameter("zoneName", zoneName);
        method.addParameter("showZoneName", showZoneName);
        //用户名
        method.addParameter("username", userName);
        //密码
        method.addParameter("password", password);
        //图片验证码
        method.addParameter("randomId", check);
        //spring web flow流
        method.addParameter("lt", springWebFlow);

        //结果集合
        Map result = HttpClientUtils.connect(method, "GBK", cookies);//返回结果
        return result;
    }

    /**
     * 提交
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
        //参数
        method.addParameter("forbidpass", "null");
        method.addParameter("forbidaccounts", "null");
        method.addParameter("authtype", "c0000001");
        method.addParameter("open_no", "0000001");
        method.addParameter("customFileld01", "1");
        method.addParameter("_eventId", "submit");
        method.addParameter("submitBtn1", "正在提交.....");
        method.addParameter("0000001RmbMe", "on");
        //method.addParameter("password_r", "");
        //method.addParameter("password_c", "");

        //身份地区
        method.addParameter("customFileld02", provinceNum);
        method.addParameter("areaname", provinceStr);
        //method.addParameter("customFileld03", zoneNum);
        //method.addParameter("zoneName", zoneName);
        //method.addParameter("showZoneName", showZoneName);
        //用户名
        method.addParameter("username", userName);
        //密码
        method.addParameter("password", password);
        //图片验证码
        method.addParameter("randomId", check);
        //spring web flow流
        method.addParameter("lt", springWebFlow);

        //结果集合
        Map result = HttpClientUtils.connect(method, "GBK", cookies);//返回结果
        return result;
    }

    /**
     * 做跳转
     * @param jumpUrl
     */
    public static Map jump(GrabJF189_2 grabJF189, String jumpUrl) {
        // 4.登陆验证成功，第一次跳转，地址位于ResponseHeaders中的的Location
        logger.info("登陆验证成功，第一次跳转，地址:" + jumpUrl);
        Map result = new HashMap();
        try{
            result = HttpClientUtils.getUrl(jumpUrl, "UTF-8", "GBK", new Cookie[]{});//返回结果 可以不带cookie
        } catch (Exception e){
            result.put(KEY_IS_SUCCESS, false);
            String errorMessage = "登陆验证第一次跳转失败，请重新登陆！";
            BaseUtils_2.loggerOut(grabJF189, errorMessage);
            BaseUtils_2.alertErrorMessage(errorMessage);
            return result;
        }
        boolean isSuccess = (Boolean)result.get(KEY_IS_SUCCESS);//判是否成功
        if(!isSuccess)
        {
            String errorMessage = "登陆验证第一次跳转失败，请重新登陆！";
            BaseUtils_2.loggerOut(grabJF189, errorMessage);
            BaseUtils_2.alertErrorMessage(errorMessage);
            return result;
        }
        String webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//网页内容
        logger.info("第一次跳转，返回内容" + webContent);
        Map headerMap = (Map)result.get(HttpClientUtils.KEY_RESPONSE_HEADER_MAP);//返回头
        //cookies = (Cookie[])result.get(HttpClientUtils.KEY_RESPONSE_COOKIES);//返回头

        // 5.第二次跳转，解析 第一次跳转 返回内容得到第二次跳转地址
        jumpUrl = getJumpUrl2(webContent);
        logger.info("第二次跳转，地址:" + jumpUrl);
        result = HttpClientUtils.getUrl(jumpUrl, "UTF-8", "GBK", new Cookie[]{});//返回结果 可以不带cookie
        isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//判是否成功
        if(!isSuccess)
        {
            String errorMessage = "登陆验证第二次跳转失败，请重新登陆！";
            BaseUtils_2.loggerOut(grabJF189, errorMessage);
            BaseUtils_2.alertErrorMessage(errorMessage);
            return result;
        }
        webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//网页内容
        logger.info("第二次跳转，返回内容" + webContent);
        headerMap = (Map)result.get(HttpClientUtils.KEY_RESPONSE_HEADER_MAP);//返回头
        //cookies = (Cookie[])result.get(HttpClientUtils.KEY_RESPONSE_COOKIES);//返回头

        // 6.第三次跳转，解析 第二次跳转 返回内容得到第三次跳转地址
        jumpUrl = getJumpUrl3(webContent);
        logger.info("第三次跳转，地址:" + jumpUrl);
        result = HttpClientUtils.getUrl(jumpUrl, "UTF-8", "GBK", new Cookie[]{});//返回结果 可以不带cookie
        isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//判是否成功
        if(!isSuccess)
        {
            String errorMessage = "登陆验证第三次跳转失败，请重新登陆！";
            BaseUtils_2.loggerOut(grabJF189, errorMessage);
            BaseUtils_2.alertErrorMessage(errorMessage);
            return result;
        }
        result.put(KEY_BROWSE_URL1, jumpUrl);//第三次跳转地址
        webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//网页内容
        logger.info("第三次跳转，返回内容" + webContent);
        return result;

        //==============================================================================================
        //读取产品列表 按价格上涨 http://mall.jf.189.cn/Goods/GoodsList
        //http://mall.jf.189.cn/Goods/CanBuyGoodsList
        //http://mall.jf.189.cn:8000/Goods/CanBuyGoodsList

//        String canBuyUrl = "http://mall.jf.189.cn/Order/SellOrder?GoodsSn=bbb2a86c-bd84-4665-8947-ddd9f9" +
//                "9bd6fa&GoodsAttSn=0a1ba597-3cf8-4799-b7ee-55c118f78a89&GoodNum=1";
//        logger.info("第四个url:" + canBuyUrl);
//        result = HttpClientUtils.getUrl(canBuyUrl, "UTF-8", "GBK", cookies);
//        //返回结果 可以不带cookie 但是返回会带cookie
//        isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//判是否成功
//        if(!isSuccess)
//        {
//            logger.info("失败！");//是否终止
//            return;
//        }
//        webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//网页内容
//        logger.info("+++++++++++返回内容" + webContent);
//        cookies = (Cookie[])result.get(HttpClientUtils.KEY_RESPONSE_COOKIES);//返回头

//        logger.info("第五个url:" + canBuyUrl);
//        result = HttpClientUtils.getUrl(canBuyUrl, "UTF-8", "GBK", cookies);
//        //返回结果 可以不带cookie 但是返回会带cookie
//        isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//判是否成功
//        if(!isSuccess)
//        {
//            logger.info("失败！");//是否终止
//            return;
//        }
//        webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//网页内容
//        logger.info("+++++++++++返回内容" + webContent);
//        //cookies = (Cookie[])result.get(HttpClientUtils.KEY_RESPONSE_COOKIES);//返回头
    }

    /**
     * 解析 第一次跳转 返回内容得到第二次跳转地址
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
     * 解析 第二次跳转 返回内容得到第三次跳转地址
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
     * 根据页数获取现金产品列表
     * @param page
     * @return
     */
    public static Map getMoneyGoodsListByPage(GrabJF189_2 grabJF189, int page) {
        String message = "根据页数[" + page + "]获取现金产品列表";
        BaseUtils_2.loggerOut(grabJF189, message);

        List<MoneyGoods> moneyGoodsList = new ArrayList<MoneyGoods>();

        String moneyGoodsListUrl = "http://jf.189.cn/duihuan/ncommoditylist.aspx";
        logger.info("根据页数获取现金产品列表url:" + moneyGoodsListUrl);
        PostMethod method = new PostMethod(moneyGoodsListUrl);
        //参数
        method.addParameter("__VIEWSTATE", "/wEPDwUKMTgzNzgzNjYxMA8WAh4ERmxhZwUBMhYCAgUPZBYUZg9kFgJmDxYCHglpbm5lcmh0bWwF3ycNCiAgICAgICAgICAgIDxESVYgaWQ9YWxsc29ydCBjbGFzcz1tPg0KICAgICAgICAgICAgICA8RElWIGNsYXNzPW1jPjxESVYgY2xhc3M9J2l0ZW0nPjxTUEFOPjxIND48QSBocmVmPSdOQ29tbW9kaXR5TGlzdC5hc3B4P1R5cGU9MDEnPuaJi+acuuaVsOeggTwvQT48L0g0PjxJTlM+Jm5ic3A7LSZuYnNwO+e9kee7nOiuvuWkh3zmkYTlvbHmkYTlg488L0lOUz48L1NQQU4+PERJVj48REw+PERUPjwvRFQ+PEREPjxFTT48QSBocmVmPSdOQ29tbW9kaXR5TGlzdC5hc3B4P1R5cGU9MDEwMSc+572R57uc6K6+5aSHPC9BPjwvRU0+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wMTAzJz7mkYTlvbHmkYTlg488L0E+PC9FTT48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTAxMDQnPuaJi+acuumFjeS7tjwvQT48L0VNPjxFTT48QSBocmVmPSdOQ29tbW9kaXR5TGlzdC5hc3B4P1R5cGU9MDEwNSc+5pWw56CB5ZGo6L65PC9BPjwvRU0+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wMTA2Jz7lvbHpn7Porr7lpIc8L0E+PC9FTT48L0REPjwvREw+PC9ESVY+PC9ESVY+PERJViBjbGFzcz0naXRlbSc+PFNQQU4+PEg0PjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wMic+5a625bGF55m+6LSnPC9BPjwvSDQ+PElOUz4mbmJzcDstJm5ic3A755Sf5rS75pel55SofOW3peWFt+Wll+ijhTwvSU5TPjwvU1BBTj48RElWPjxETD48RFQ+PC9EVD48REQ+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wMjAzJz7nlJ/mtLvml6XnlKg8L0E+PC9FTT48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTAyMDQnPuW3peWFt+Wll+ijhTwvQT48L0VNPjxFTT48QSBocmVmPSdOQ29tbW9kaXR5TGlzdC5hc3B4P1R5cGU9MDIwNSc+5Yqe5YWs55So5ZOBPC9BPjwvRU0+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wMjExJz7lrrbnurrml6XnlKg8L0E+PC9FTT48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTAyMTQnPuWOqOWFt+eUqOWTgTwvQT48L0VNPjwvREQ+PC9ETD48L0RJVj48L0RJVj48RElWIGNsYXNzPSdpdGVtJz48U1BBTj48SDQ+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTAzJz7pkp/ooajjgIHphY3ppbA8L0E+PC9IND48SU5TPiZuYnNwOy0mbmJzcDvpkp/ooajjgIHmiYvooah86YWN6aWw44CB54+g5a6dPC9JTlM+PC9TUEFOPjxESVY+PERMPjxEVD48L0RUPjxERD48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTAzMDEnPumSn+ihqOOAgeaJi+ihqDwvQT48L0VNPjxFTT48QSBocmVmPSdOQ29tbW9kaXR5TGlzdC5hc3B4P1R5cGU9MDMwNCc+6YWN6aWw44CB54+g5a6dPC9BPjwvRU0+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wMzA2Jz7nnLzplZzjgIHnrrHljIU8L0E+PC9FTT48L0REPjwvREw+PC9ESVY+PC9ESVY+PERJViBjbGFzcz0naXRlbSc+PFNQQU4+PEg0PjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wNCc+5Liq5oqk576O5aaGPC9BPjwvSDQ+PElOUz4mbmJzcDstJm5ic3A76a2F5Yqb5b2p5aaGfOmdoumDqOaKpOeQhjwvSU5TPjwvU1BBTj48RElWPjxETD48RFQ+PC9EVD48REQ+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wNDAxJz7prYXlipvlvanlpoY8L0E+PC9FTT48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTA0MDQnPumdoumDqOaKpOeQhjwvQT48L0VNPjxFTT48QSBocmVmPSdOQ29tbW9kaXR5TGlzdC5hc3B4P1R5cGU9MDQwNSc+6Lqr5L2T5oqk55CGPC9BPjwvRU0+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wNDA2Jz7nvo7lrrnlt6Xlhbc8L0E+PC9FTT48L0REPjwvREw+PC9ESVY+PC9ESVY+PERJViBjbGFzcz0naXRlbSc+PFNQQU4+PEg0PjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wNSc+5q+N5am044CB546p5YW3PC9BPjwvSDQ+PElOUz4mbmJzcDstJm5ic3A75YS/56ul546p5YW3fOWWguWFu+eUqOWTgTwvSU5TPjwvU1BBTj48RElWPjxETD48RFQ+PC9EVD48REQ+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wNTAyJz7lhL/nq6Xnjqnlhbc8L0E+PC9FTT48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTA1MDMnPuWWguWFu+eUqOWTgTwvQT48L0VNPjxFTT48QSBocmVmPSdOQ29tbW9kaXR5TGlzdC5hc3B4P1R5cGU9MDUwNCc+5a+d5YW35pyN6aWwPC9BPjwvRU0+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wNTA1Jz7mtJfmiqTnlKjlk4E8L0E+PC9FTT48L0REPjwvREw+PC9ESVY+PC9ESVY+PERJViBjbGFzcz0naXRlbSc+PFNQQU4+PEg0PjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wNic+5p2C5b+X5pyf5YiKPC9BPjwvSDQ+PElOUz4mbmJzcDstJm5ic3A75pe25bCa5p2C5b+XfOeDremUgOS5puWIijwvSU5TPjwvU1BBTj48RElWPjxETD48RFQ+PC9EVD48REQ+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wNjAzJz7ml7blsJrmnYLlv5c8L0E+PC9FTT48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTA2MDQnPueDremUgOS5puWIijwvQT48L0VNPjwvREQ+PC9ETD48L0RJVj48L0RJVj48RElWIGNsYXNzPSdpdGVtJz48U1BBTj48SDQ+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTA3Jz7po5/lk4HjgIHojLblj7bjgIHnibnkvps8L0E+PC9IND48SU5TPiZuYnNwOy0mbmJzcDvnsq7msrnosIPlkbN85rig6YGT54m55L6bPC9JTlM+PC9TUEFOPjxESVY+PERMPjxEVD48L0RUPjxERD48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTA3MDEnPueyruayueiwg+WRszwvQT48L0VNPjxFTT48QSBocmVmPSdOQ29tbW9kaXR5TGlzdC5hc3B4P1R5cGU9MDcwMic+5rig6YGT54m55L6bPC9BPjwvRU0+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wNzAzJz7nsr7lk4HojLblj7Y8L0E+PC9FTT48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTA3MDknPuWcsOaWueeJueS6pzwvQT48L0VNPjwvREQ+PC9ETD48L0RJVj48L0RJVj48RElWIGNsYXNzPSdpdGVtJz48U1BBTj48SDQ+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTA4Jz7omZrmi5/npLzlk4E8L0E+PC9IND48SU5TPiZuYnNwOy0mbmJzcDvnlLXlrZDljaHliLh855S15L+h5Lqn5ZOBPC9JTlM+PC9TUEFOPjxESVY+PERMPjxEVD48L0RUPjxERD48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTA4MDYnPueUteWtkOWNoeWIuDwvQT48L0VNPjxFTT48QSBocmVmPSdOQ29tbW9kaXR5TGlzdC5hc3B4P1R5cGU9MDgxNic+55S15L+h5Lqn5ZOBPC9BPjwvRU0+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wODE3Jz7lupfpnaLlhZHmjaI8L0E+PC9FTT48L0REPjwvREw+PC9ESVY+PC9ESVY+PERJViBjbGFzcz0naXRlbSc+PFNQQU4+PEg0PjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wOSc+5aSp57+85a6i5oi35L+x5LmQ6YOoPC9BPjwvSDQ+PElOUz4mbmJzcDstJm5ic3A76L+Q5Yqo5L+x5LmQ6YOofOaRhOW9seS/seS5kOmDqDwvSU5TPjwvU1BBTj48RElWPjxETD48RFQ+PC9EVD48REQ+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wOTAxJz7ov5Dliqjkv7HkuZDpg6g8L0E+PC9FTT48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTA5MDInPuaRhOW9seS/seS5kOmDqDwvQT48L0VNPjxFTT48QSBocmVmPSdOQ29tbW9kaXR5TGlzdC5hc3B4P1R5cGU9MDkwMyc+5b2x6L+35L+x5LmQ6YOoPC9BPjwvRU0+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0wOTA0Jz7ml4Xlj4vkv7HkuZDpg6g8L0E+PC9FTT48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTA5MDUnPjEw5YWD55yL5aSn54mHPC9BPjwvRU0+PC9ERD48L0RMPjwvRElWPjwvRElWPjxESVYgY2xhc3M9J2l0ZW0nPjxTUEFOPjxIND48QSBocmVmPSdOQ29tbW9kaXR5TGlzdC5hc3B4P1R5cGU9MTAnPuWutueUqOeUteWZqDwvQT48L0g0PjxJTlM+Jm5ic3A7LSZuYnNwO+eUn+a0u+WutueUtXzljqjmiL/nlLXlmag8L0lOUz48L1NQQU4+PERJVj48REw+PERUPjwvRFQ+PEREPjxFTT48QSBocmVmPSdOQ29tbW9kaXR5TGlzdC5hc3B4P1R5cGU9MTAwMSc+55Sf5rS75a6255S1PC9BPjwvRU0+PEVNPjxBIGhyZWY9J05Db21tb2RpdHlMaXN0LmFzcHg/VHlwZT0xMDAyJz7ljqjmiL/nlLXlmag8L0E+PC9FTT48RU0+PEEgaHJlZj0nTkNvbW1vZGl0eUxpc3QuYXNweD9UeXBlPTEwMDMnPuS4quS6uuaKpOeQhjwv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");//默认排序
        method.addParameter("__EVENTTARGET", "");//默认排序
        method.addParameter("__EVENTARGUMENT", "");//默认排序
        method.addParameter("SelOrder", "0");//默认排序
        method.addParameter("PAYTYPE", "0");//默认排序
        method.addParameter("AspNetPager1_input", StringUtils.EMPTY + page);
        method.addParameter("AspNetPager1", "go");
        method.addParameter("AspNetPager2_input", "1");
        Map result;//返回结果
        try {
            result = HttpClientUtils.connect(method, "GBK", new Cookie[]{});
        } catch (Exception e) {
            e.printStackTrace();
            result = new HashMap();
            result.put(HttpClientUtils.KEY_IS_SUCCESS, false);
            return result;
        }
        //返回结果 可以不带cookie 但是返回会带cookie
        boolean isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//判是否成功
        if(!isSuccess)
        {
            String errorMessage = "根据页数[" + page + "]获取现金产品列表失败";
            BaseUtils_2.loggerOut(grabJF189, errorMessage);
            BaseUtils_2.alertErrorMessage(errorMessage);
            return result;
        }
        String webContent = (String)result.get(KEY_RESP_STR);//网页内容
        logger.info("+++++++++++返回内容" + webContent);

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
            indexStr = "所需积分：<strong class='orange'>";
            index = webContent.indexOf(indexStr);
            webContent = webContent.substring(index + indexStr.length());
            indexStr = "积分</strong>";
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

        indexStr = "<span id=\"LBMsg2\">第";
        index = webContent.indexOf(indexStr);
        webContent = webContent.substring(index + indexStr.length());
        indexStr = "/";
        index = webContent.indexOf(indexStr);
        page = Integer.parseInt(webContent.substring(0, index));

        int pageSize = moneyGoodsList.size();

        webContent = webContent.substring(index + indexStr.length());
        indexStr = "页";
        index = webContent.indexOf(indexStr);
        int pageCount = Integer.parseInt(webContent.substring(0, index));
        logger.info("page=" + page + ",pageSize=" + pageSize + ",pageCount=" + pageCount);

        MoneyGoodsList moneyGoodsList1 = new MoneyGoodsList(page, pageSize, pageCount, moneyGoodsList);
        result.put(KEY_MONEY_GOODS_LIST, moneyGoodsList1);
        return result;
    }

    /**
     * 根据页数获取产品列表 todo
     * @param page
     * @param cookies
     * @return
     */
    public static Map getGoodsListByPage(GrabJF189_2 grabJF189, int page, Cookie[] cookies) {
        String goodsListUrl = "http://mall.jf.189.cn/Goods/GetGoodsListInfo?categoryType=3&" +
                "ExchangeCountOrderType=0&PriceOrderType=3&AddDateOrderType=0&p=" + page;
        logger.info("第四个url:" + goodsListUrl);
        Map result = HttpClientUtils.getUrl(goodsListUrl, "UTF-8", "GBK", cookies);
        //返回结果 可以不带cookie 但是返回会带cookie
        boolean isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//判是否成功
        if(!isSuccess)
        {
            String errorMessage = "根据页数[" + page + "]获取产品列表失败";
            BaseUtils_2.loggerOut(grabJF189, errorMessage);
            BaseUtils_2.alertErrorMessage(errorMessage);
            return result;
        }
        String webContent = (String)result.get(KEY_RESP_STR);//网页内容
        logger.info("+++++++++++返回内容" + webContent);
        cookies = (Cookie[])result.get(HttpClientUtils.KEY_RESPONSE_COOKIES);//返回头

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
     * 得到总页数
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
     * 刷新网络
     */
    public static void refreshNetWork(GrabJF189_2 grabJF189){
        try {
            //通过process方式发起windows命令行命令执行
            String cmd = "ipconfig /release";
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream(), "GBK"));
            String msg;
            while ((msg = br.readLine()) != null) {
                logger.info(msg);
                BaseUtils_2.loggerOut(grabJF189, msg);//todo
            }

            //通过process方式发起windows命令行命令执行
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
     * 获取ip地址
     * @return
     * @throws UnknownHostException
     */
    public static String getIpAddress(GrabJF189_2 grabJF189) {
        String url = "http://www.ip.cn/";
        Map result = HttpClientUtils.getUrl(url, "UTF-8", "GBK", new Cookie[]{});
        boolean isSuccess = (Boolean)result.get(KEY_IS_SUCCESS);//判是否成功
        if(!isSuccess)
        {
            String errorMessage = "获取ip地址失败！";
            BaseUtils_2.loggerOut(grabJF189, errorMessage);
            BaseUtils_2.alertErrorMessage(errorMessage);
            return StringUtils.EMPTY;
        }
        String webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//网页内容
        logger.info("获取登陆信息，返回内容" + webContent);
        String indexStr = "<p>当前 IP：<code>";
        int index = webContent.indexOf(indexStr);
        webContent = webContent.substring(index + indexStr.length());
        indexStr = "</code>";
        index = webContent.indexOf(indexStr);
        String outIp = webContent.substring(0, index);
        return outIp;
    }

    /**
     * 校验Excel是否正确
     * @param path
     */
    public static void checkExcel(String path, GrabJF189_2 grabJF189) throws Exception
    {
        loggerOut(grabJF189, "载入Excel路径:[" + path + "]");
        HSSFWorkbook workbook;
        HSSFSheet sheet;

        // 1 装载文件看是否有异常
        try {
            workbook = new HSSFWorkbook(new FileInputStream(path));
            sheet = workbook.getSheetAt(0);
        } catch (Exception e) {
            loggerOut(grabJF189, "装载文件异常");
            throw new RuntimeException("装载文件异常：" + path);
        }

        // 2 判是否有数据
        int totalRowNum = sheet.getLastRowNum();
        loggerOut(grabJF189, "文件行数=" + (totalRowNum+1) + "(包括抬头)");
        if(totalRowNum <= 0)
        {
            throw new RuntimeException("该文件无数据");
        }

        // 3 校验第一行数据是否一致
        HSSFRow row = sheet.getRow(0);
        int totalCellNum = row.getLastCellNum();
        loggerOut(grabJF189, "文件列数=" + totalCellNum);
        if(EXCEL_TITLE.length != totalCellNum)//比较个数
        {
            alertWarnMessage("该文件与模板不一致，请重新载入文件！");
            return;
        }

        int index = 0;
        while (index < EXCEL_TITLE.length)//比较是否一致
        {
            try{
                if(!EXCEL_TITLE[index].equals(getHSSFCellValue(row.getCell(index++))))
                {
                    alertWarnMessage("该文件与模板不一致，请重新载入文件！");
                    return;
                }
            } catch (Exception e)
            {
                alertWarnMessage("该文件与模板不一致，请重新载入文件！");
                return;
            }
        }

        /**
         * 4 读取数据到table中
         */
        readExcel2Table(sheet,grabJF189);

        /**
         * 5 逐行数据校验
         */
        for(int i=0;i<grabJF189.inputDtoList.size();i++)
        {
            InputDto_2 dto = (InputDto_2)grabJF189.inputDtoList.get(i);
            if(dto.isValidate)
            {
                if((null == dto.mobile) || "".equals(dto.password))
                {
                    dto.isValidate = false;
                    dto.errorMsg = "手机号和密码不能为空！";
                } else if(!isValidLoginType(dto.getLoginType()))
                {
                    dto.isValidate = false;
                    dto.errorMsg = "登录类型有误！";
                } else
                {
                    dto.isValidate = true;
                }
            }
        }
    }

    /**
     * 判登录类型是否有误
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
     * 提示信息
     * @param message
     */
    public static void alertMessage(String message)
    {
        JOptionPane.showMessageDialog(null, message, "信息", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * 警告信息
     * @param message
     */
    public static void alertWarnMessage(String message)
    {
        JOptionPane.showMessageDialog(null, message, "信息", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * 错误信息
     * @param message
     */
    public static void alertErrorMessage(String message)
    {
        JOptionPane.showMessageDialog(null, message, "信息", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * 将日志写入loggerTextPane
     * @param grabJF189
     * @param string
     */
    public static void loggerOutWithNoLine(GrabJF189_2 grabJF189, String string)
    {
        grabJF189.loggerTextArea.append(string);
        logger.info(string);
    }

    /**
     * 将日志写入loggerTextPane
     * @param grabJF189
     * @param string
     */
    public static void loggerOut(GrabJF189_2 grabJF189, String string)
    {
        grabJF189.loggerTextArea.append(string + "\r\n");
        logger.info(string);
    }

    /**
     * 得到jar包路径
     * @return
     */
    public static String getJarPath()
    {
        JarUtil ju = new JarUtil(JarUtil.class);
        return ju.getJarPath() + "/";
    }

    /**
     * 判网络是不是通的
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
     * 判是否能得到HTML内容
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
     * 得到单元格的值
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
            case HSSFCell.CELL_TYPE_STRING://字符串类型
                cellValue = cell.getRichStringCellValue().getString();
                if (((String)cellValue).trim().equals("")
                        || ((String)cellValue).trim().length() <= 0) {
                    cellValue = "";
                }
                //cellValue = inputEncode((String)cellValue);
                break;
            case HSSFCell.CELL_TYPE_NUMERIC://数字类型
//                if (valueArr.length == 2 && valueArr[1].equals("date")) {
//                    cellValue = cell.getDateCellValue();;
//                }
//                if (valueArr.length == 2 && valueArr[1].equals("timestamp")) {
//                    Date date = cell.getDateCellValue();
//                    SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                    String time=format1.format(date);
//                    cellValue= Timestamp.valueOf(time);
//                }else { // 如果长度为2，说明此列为默认字符串类型
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
     * 读取数据到table中
     * @param sheet
     * @param grabJF189
     */
    private static void readExcel2Table(HSSFSheet sheet, GrabJF189_2 grabJF189)
    {
        // 0 初始化inputDtoList
        grabJF189.inputDtoList = new ArrayList();

        // 1 清空所有行 并 初始化头一行
        clearAllAndInitTitle(grabJF189.table);

        // 2 得到tableModel
        DefaultTableModel tableModel = (DefaultTableModel) grabJF189.table.getModel();

        // 2 逐行新增+修改table+放入inputDtoList
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
                dto.errorMsg = "该行数据载入失败！";
            }
            /**
             * 分俩版本：
             * 1.只支持手机登录方式
             * 2.支持各种登录方式
             */
            /**
             * //加上这段代码，则只支持手机登录方式
             * if(!dto.getLoginType().equals("手机")){
             *      dto.isValidate = false;
             *      dto.errorMsg = "此版本只支持手机登录方式！";
             * }
             */
            grabJF189.inputDtoList.add(dto);
        }
    }

    /**
     * 清空所有行 并 初始化头一行
     * @param table
     */
    public static void clearAllAndInitTitle(JTable table)
    {
        // 1 得到tableModel
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        // 2 清空所有行 并 保留头一行
        for(int i=tableModel.getRowCount()-1;i>=1;i--)
        {
            tableModel.removeRow(i);
        }

        // 3 设置头一行的Title
        for(int i=0;i<EXCEL_TITLE.length;i++)
        {
            tableModel.setValueAt(EXCEL_TITLE[i], 0, i);
        }
    }

    /**
     * 清空所有行 并 初始化头一行
     * @param table
     */
    public static void clearSuccessAllAndInitTitle(JTable table)
    {
        // 1 得到tableModel
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

        // 3 设置头一行的Title
        for(int i=0;i<DETAIL_EXCEL_TITLE.length;i++)
        {
            tableModel.setValueAt(DETAIL_EXCEL_TITLE[i], 0, i);
        }
    }

    /**
     * 获取登陆信息
     * @param cookies
     * @return
     */
    public static Map getLoginInfo(GrabJF189_2 grabJF189, Cookie[] cookies) {
        String url = "http://jf.189.cn/home/ajax.ashx?g=nocachelogin";
        logger.info("登陆成功,获取登陆信息url=" + url);
        Map result = HttpClientUtils.getUrl(url, "UTF-8", "GBK", cookies);//返回结果 可以不带cookie
        boolean isSuccess = (Boolean)result.get(KEY_IS_SUCCESS);//判是否成功
        if(!isSuccess)
        {
            String errorMessage = "获取登陆信息失败！";
            BaseUtils_2.loggerOut(grabJF189, errorMessage);
            BaseUtils_2.alertErrorMessage(errorMessage);
            return result;
        }
        String webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//网页内容
        logger.info("获取登陆信息，返回内容" + webContent);
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
     * 查询订单结果
     * @param orderId
     * @param cookies
     * @return
     * @throws Exception
     */
    public static Map orderDetail(String orderId, Cookie[] cookies) throws Exception {
        String url = "http://jf.189.cn/SelfHelp/OrderDetail.aspx?S=0&ORDERID=" + orderId;
        Map result = HttpClientUtils.getUrl(url, "UTF-8", "GBK", cookies);//返回结果 可以不带cookie
        return result;
    }

    /**
     * 根据电话号码获取省份信息
     *
     * @param mobile
     * @return
     */
    public static String getProvinceByMobile(GrabJF189_2 grabJF189, String mobile){
        String url = "https://uam.ct10000.com/ct10000uam/FindPhoneAreaServlet";
        PostMethod method = new PostMethod(url);
        //参数
        method.addParameter("username", mobile);

        //结果集合
        try {
            Map result = HttpClientUtils.connect(method, "UTF-8", new Cookie[]{});//返回结果
            //返回结果 可以不带cookie 但是返回会带cookie
            boolean isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//判是否成功
            if(!isSuccess)
            {
                String errorMessage = "读取省份信息失败！";
                BaseUtils_2.loggerOut(grabJF189, errorMessage);
                BaseUtils_2.alertErrorMessage(errorMessage);
                return "|";
            }
            String webContent = (String)result.get(KEY_RESP_STR);//网页内容
            logger.info("+++++++++++返回内容" + webContent);
            return webContent;
        } catch (Exception e) {
            e.printStackTrace();
            return "|";
        }
    }

    /**
     * 添加成功记录到表格中
     *
     * @param successTable
     * @param order
     * @param successCount
     */
    public static void putSuccessTable(JTable successTable, Order order, int successCount){
        // 2 得到tableModel
        DefaultTableModel tableModel = (DefaultTableModel) successTable.getModel();
        tableModel.addRow(EMPTY_STRING_ARRAY);
        successTable.setValueAt(order.getMobile(), successCount, 0);
        successTable.setValueAt(order.getUserName(), successCount, 1);

        successTable.setValueAt(order.getGoodsId(), successCount, 2);
        successTable.setValueAt(order.getGoodsName(), successCount, 3);
        successTable.setValueAt(StringUtils.EMPTY + order.getSingleJiFen(), successCount, 4);
        successTable.setValueAt(StringUtils.EMPTY + order.getBuyNum(), successCount, 5);

        successTable.setValueAt(order.getOrderId(), successCount, 6);
        successTable.setValueAt(order.isSuccess()?"成功":"失败", successCount, 7);//成功才保存
        successTable.setValueAt(order.getCardNo(), successCount, 8);
        successTable.setValueAt(order.getPassword(), successCount, 9);
        successTable.setValueAt(order.getDeadline(), successCount, 10);
    }

    /**
     * 打开浏览器访问页面
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
     * 根据mobile查找InputDto
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
     * 保存Excel
     * @param grabJF189
     */
    public static void saveExcel(GrabJF189_2 grabJF189) throws Exception
    {
        /**
         * 1 导出 用户，密码，姓名，剩余积分，成功笔数
         */
        // 判是否加载过Excel
        if(StringUtils.isBlank(grabJF189.rightPanel.filePath))
        {
            alertWarnMessage("当前未加载Excel，请加载Excel！");
            return;
        }

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet;

        // 1.1 装载文件看是否有异常
        sheet = workbook.createSheet();

        // 1.2 得到tableModel
        DefaultTableModel tableModel = (DefaultTableModel) grabJF189.table.getModel();

        // 1.3 从table把数据拷贝到excel中
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

        // 1.4 保存文件
        String fileName = new File(grabJF189.rightPanel.filePath).getName();
        // 保存在 jar包所在lib目录得同级目录out下 并且文件名加 操作结果.xls结尾
        String path = getSaveFileDir() + fileName.substring(0, fileName.lastIndexOf(".xls"))
                + "操作结果" + grabJF189.fetch + ".xls";
        FileOutputStream fOut = new FileOutputStream(path);
        workbook.write(fOut);
        fOut.flush();
        fOut.close();
        loggerOut(grabJF189, "输出[用户操作结果]excel文件成功，路径[" + path + "]");

        /**
         * 2 导出 成功流水：卡密信息 用户信息等等
         */
        workbook = new HSSFWorkbook();

        // 2.1 装载文件看是否有异常
        sheet = workbook.createSheet();

        // 2.2 得到tableModel
        tableModel = (DefaultTableModel) grabJF189.centerPanel.successTable.getModel();

        // 1.3 从table把数据拷贝到excel中
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

        // 1.4 保存文件
        // 保存在 jar包所在lib目录得同级目录out下 并且文件名加 操作结果.xls结尾
        path = getSaveFileDir() + fileName.substring(0, fileName.lastIndexOf(".xls")) + "成功卡密" + grabJF189.fetch + ".xls";
        fOut = new FileOutputStream(path);
        workbook.write(fOut);
        fOut.flush();
        fOut.close();
        loggerOut(grabJF189, "输出[成功卡密]excel文件成功，路径[" + path + "]");

        // 3 提示保存Excel成功
        alertMessage("输出操作结果成功！");
    }

    /**
     * 得到jar包所在lib目录
     * @return
     */
    public static String getSameDirWithLib(){
//        return "D:\\04.my_projects\\grab-JF189\\";//todo
        return getJarPath().substring(0, getJarPath().lastIndexOf("lib"));
    }
}
