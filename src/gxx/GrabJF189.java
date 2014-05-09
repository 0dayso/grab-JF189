package gxx;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 抓取jf.189.cn
 *
 * @author Gxx
 * @module oa
 * @datetime 14-4-25 00:13
 */
public class GrabJF189
{
    /**
     * 用户名
     */
    private static final String USER_NAME = "15394184845";
    /**
     * 密码
     */
    private static final String PASSWORD = "123654";
    /**
     * 登陆界面地址 和 登陆提交地址 是同一个
     */
    private String LOGIN_URL = "https://uam.ct10000.com/ct10000uam/login?service=https%3A%2F" +
            "%2Fsso.jf.189.cn%3A443%2F%2Fsso%2Flogin%3Fservice%3Dhttp%3A%2F%2Fjf.189.cn%2FHome%2FLogin." +
            "aspx%3Fref%3DaHR0cDovL2pmLjE4OS5jbi9Ib21lL2luZGV4LmFzcHg%3D&register=registerMB";

    /**
     * 登陆界面地址 和 登陆提交地址 是同一个
     * 固话登录
     */
    public static final String LOGIN_URL_FOR_GUHUA = "https://uam.ct10000.com/ct10000uam/login?service=https%3A%2F" +
            "%2Fsso.jf.189.cn%3A443%2F%2Fsso%2Flogin%3Fservice%3Dhttp%3A%2F%2Fjf.189.cn%2FHome%2FLogin." +
            "aspx%3Fref%3DaHR0cDovL2pmLjE4OS5jbi9Ib21lL2luZGV4LmFzcHg%3D&register=registerPH";
    /**
     * 图片地址
     */
    private String IMAGE_URL = "https://uam.ct10000.com/ct10000uam/validateImg.jsp?rand=";

    /**
     * 处理进程入口
     */
    public void process() throws Exception
    {
        // 1.访问登陆界面地址，获取cookie和spring web flow流(lt)
        Map result = HttpClientUtils.getUrl(LOGIN_URL, "UTF-8", "GBK", new Cookie[]{});//返回结果
        boolean isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//判是否成功
        if(!isSuccess)
        {
            System.out.println("失败！");//是否终止
            return;
        }
        Cookie[] cookies = (Cookie[])result.get(HttpClientUtils.KEY_RESPONSE_COOKIES);//返回头
        String webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//网页内容
        String springWebFlow = getSpringWebFlow(webContent);//解析网页地址得到springWebFlow

        // 2.访问图片地址，下载图片
        result = HttpClientUtils.downloadImage(IMAGE_URL + Math.random(), cookies);//返回结果
        isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//判是否成功
        if(!isSuccess)
        {
            System.out.println("失败！");//是否终止
            return;
        }

        // 3.做登陆
        result = login(springWebFlow, cookies);
        isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//判是否成功
        if(!isSuccess)
        {
            System.out.println("失败！");//是否终止
            return;
        }
        webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//网页内容 这里会返回空的字符串
        cookies = (Cookie[])result.get(HttpClientUtils.KEY_RESPONSE_COOKIES);//返回头
        Map headerMap = (Map)result.get(HttpClientUtils.KEY_RESPONSE_HEADER_MAP);//返回头

        // 4.登陆验证成功，第一次跳转，地址位于ResponseHeaders中的的Location
        String jumpUrl = (String)headerMap.get("Location");
        System.out.println("登陆验证成功，第一次跳转，地址:" + jumpUrl);
        result = HttpClientUtils.getUrl(jumpUrl, "UTF-8", "GBK", new Cookie[]{});//返回结果 可以不带cookie
        isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//判是否成功
        if(!isSuccess)
        {
            System.out.println("失败！");//是否终止
            return;
        }
        webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//网页内容
        System.out.println("第一次跳转，返回内容" + webContent);
        headerMap = (Map)result.get(HttpClientUtils.KEY_RESPONSE_HEADER_MAP);//返回头
        //cookies = (Cookie[])result.get(HttpClientUtils.KEY_RESPONSE_COOKIES);//返回头

        // 5.第二次跳转，解析 第一次跳转 返回内容得到第二次跳转地址
        jumpUrl = getJumpUrl2(webContent);
        System.out.println("第二次跳转，地址:" + jumpUrl);
        result = HttpClientUtils.getUrl(jumpUrl, "UTF-8", "GBK", new Cookie[]{});//返回结果 可以不带cookie
        isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//判是否成功
        if(!isSuccess)
        {
            System.out.println("失败！");//是否终止
            return;
        }
        webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//网页内容
        System.out.println("第二次跳转，返回内容" + webContent);
        headerMap = (Map)result.get(HttpClientUtils.KEY_RESPONSE_HEADER_MAP);//返回头
        //cookies = (Cookie[])result.get(HttpClientUtils.KEY_RESPONSE_COOKIES);//返回头

        // 6.第三次跳转，解析 第二次跳转 返回内容得到第三次跳转地址
        jumpUrl = getJumpUrl3(webContent);
        System.out.println("第三次跳转，地址:" + jumpUrl);
        result = HttpClientUtils.getUrl(jumpUrl, "UTF-8", "GBK", new Cookie[]{});//返回结果 可以不带cookie
        isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//判是否成功
        if(!isSuccess)
        {
            System.out.println("失败！");//是否终止
            return;
        }
        webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//网页内容
        System.out.println("第三次跳转，返回内容" + webContent);
        cookies = (Cookie[])result.get(HttpClientUtils.KEY_RESPONSE_COOKIES);//返回头

        //==============================================================================================
        //读取产品列表 按价格上涨 http://mall.jf.189.cn/Goods/GoodsList
        //http://mall.jf.189.cn/Goods/CanBuyGoodsList

        //http://mall.jf.189.cn:8000/Goods/CanBuyGoodsList

        String canBuyUrl = "http://mall.jf.189.cn/Order/SellOrder?GoodsSn=bbb2a86c-bd84-4665-8947-ddd9f9" +
                "9bd6fa&GoodsAttSn=0a1ba597-3cf8-4799-b7ee-55c118f78a89&GoodNum=1";
        System.out.println("第四个url:" + canBuyUrl);
        result = HttpClientUtils.getUrl(canBuyUrl, "UTF-8", "GBK", cookies);
        //返回结果 可以不带cookie 但是返回会带cookie
        isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//判是否成功
        if(!isSuccess)
        {
            System.out.println("失败！");//是否终止
            return;
        }
        webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//网页内容
        System.out.println("+++++++++++返回内容" + webContent);
        cookies = (Cookie[])result.get(HttpClientUtils.KEY_RESPONSE_COOKIES);//返回头

        System.out.println("第五个url:" + canBuyUrl);
        result = HttpClientUtils.getUrl(canBuyUrl, "UTF-8", "GBK", cookies);
        //返回结果 可以不带cookie 但是返回会带cookie
        isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//判是否成功
        if(!isSuccess)
        {
            System.out.println("失败！");//是否终止
            return;
        }
        webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//网页内容
        System.out.println("+++++++++++返回内容" + webContent);
        //cookies = (Cookie[])result.get(HttpClientUtils.KEY_RESPONSE_COOKIES);//返回头

        String url = "http://jf.189.cn/home/ajax.ashx?g=nocachelogin";
        System.out.println("登陆成功,获取登陆信息url=" + url);
        result = HttpClientUtils.getUrl(url, "UTF-8", "GBK", cookies);//返回结果 可以不带cookie
        isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//判是否成功
        if(!isSuccess)
        {
            String errorMessage = "获取登陆信息失败！";
        }
        webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//网页内容
        System.out.println("获取登陆信息，返回内容" + webContent);
    }

    /**
     * 解析 第一次跳转 返回内容得到第二次跳转地址
     * @param webContent
     * @return
     */
    private String getJumpUrl2(String webContent) {
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
    private String getJumpUrl3(String webContent) {
        String indexStr = "window.top.location.href=\"";
        String jumpUrl = webContent.substring(webContent.indexOf(indexStr) + indexStr.length());
        indexStr = "\"";
        jumpUrl = jumpUrl.substring(0, jumpUrl.indexOf(indexStr));
        return jumpUrl;
    }

    /**
     * 解析网页地址得到springWebFlow
     * @param webContent
     * @return
     */
    public String getSpringWebFlow(String webContent) {
        String indexStr = "name=\"lt\" value=\"";
        webContent = webContent.substring(webContent.indexOf(indexStr) + indexStr.length());
        indexStr = "\"";
        String springWebFlow = webContent.substring(0, webContent.indexOf(indexStr));
        System.out.println(springWebFlow);
        return springWebFlow;
    }

    /**
     * 做登陆
     *
     * @param springWebFlow
     * @param cookies
     * @return
     */
    public Map login(String springWebFlow, Cookie[] cookies) throws Exception{
        PostMethod method = new PostMethod(LOGIN_URL);
        //参数
        method.addParameter("forbidpass", "null");
        method.addParameter("forbidaccounts", "null");
        method.addParameter("authtype", "c2000004");
        method.addParameter("open_no", "1");
        method.addParameter("customFileld02", "27");
        method.addParameter("areaname", "陕西");
        method.addParameter("submitBtn1", "正在提交.....");
        method.addParameter("_eventId", "submit");
        method.addParameter("c2000004RmbMe", "on");
        method.addParameter("customFileld01", "3");
        method.addParameter("password_r", "");
        method.addParameter("password_c", "");
        //用户名
        method.addParameter("username", USER_NAME);
        //密码
        method.addParameter("password", PASSWORD);
        //图片验证码
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        method.addParameter("randomId", input.readLine());
        //spring web flow流
        method.addParameter("lt", springWebFlow);

        for(NameValuePair nameValuePair :  method.getParameters()){
            System.out.println(nameValuePair.getName() + "=" + nameValuePair.getValue());
        }

        //结果集合
        Map result = HttpClientUtils.connect(method, "UTF-8", cookies);//返回结果
        //返回结果
        return result;
    }

    /**
     * main函数
     * @param param
     * @throws Exception
     */
    public static void main(String[] param) throws Exception
    {
        //处理进程入口
        new GrabJF189().process();

//        String sellOrderUrl = "http://mall.jf.189.cn/Order/SellOrder?GoodsSn=bbb2a86c-bd84-4665-8947-ddd9f99bd6fa&GoodsAttSn=0a1ba597-3cf8-4799-b7ee-55c118f78a89&GoodNum=1";
//        Map result = HttpClientUtils.getUrl(sellOrderUrl, "UTF-8", "GBK", new Cookie[]{});
//        //返回结果 可以不带cookie 但是返回会带cookie
//        boolean isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//判是否成功
//        if(!isSuccess)
//        {
//            System.out.println("失败！");//是否终止
//            return;
//        }
//        String webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//网页内容
//        System.out.println("+++++++++++返回内容" + webContent);
    }
}
