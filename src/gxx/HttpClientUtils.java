package gxx; /**
 * File Name:    gxx.HttpClientUtils.java
 *
 * File Desc:    HttpClient处理类
 *
 * Product AB:   PAYGATE_1_0_0
 *
 * Product Name: PAYGATE
 *
 * Module Name:  01.core
 *
 * Module AB:    01.core
 *
 * Author:       Gxx
 *
 * History:      2013-04-24 created by Gxx
 */

import gxx2.BaseInterface_2;
import gxx2.BaseUtils_2;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.util.HttpURLConnection;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * HttpClient处理类
 *
 * @author Gxx
 * @module oa
 * @datetime 14-4-11 11:18
 */
public class HttpClientUtils implements BaseInterface_2
{
    /**
     * 日志记录器
     */
    static Logger logger = Logger.getLogger(HttpClientUtils.class);

    /**
     * UTF-8
     */
    public static final String ENCODE_UTF8 = "UTF-8";

    /**
     * UTF-8
     */
    public static final String ENCODE_UTF16 = "UTF-16";

    /**
     * UTF-8
     */
    public static final String ENCODE_GB2312 = "GB2312";

    /**
     * GBK
     */
    public static final String ENCODE_GBK = "GBK";

    /**
     * GBK
     */
    public static final String ENCODE_ISO = "ISO-8859-1";

    /**
     * httpclient post 请求
     *
     * @param url            请求地址
     * @param xml            请求xml参数
     * @param requestEncode  请求编码
     * @param responseEncode 接受编码
     * @return
     */
    public static String post(String url, String xml, String requestEncode, String responseEncode)
    {
        logger.info("httpclient post 请求 开始==============");
        logger.info("url=" + url);
        //PropertyUtil.getInstance().refresh();
        logger.info("requestXml=" + xml);
//        logger.debug("没有使用证书，协议注册443端口信任所有证书，开始=================================");
//        Protocol easyHttps1 = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
//        Protocol.registerProtocol("https", easyHttps1);
//        logger.debug("没有使用证书，协议注册443端口信任所有证书，结束=================================");

        String resultStr = null;
        PostMethod postMethod = new PostMethod(url);//创建POST方法的实例
        postMethod.setRequestBody(xml);//将表单的值放入postMethod中
        postMethod.addRequestHeader("Content", "text/xml,charset=" + requestEncode + "");
        HttpClient httpClient = new HttpClient();//构造HttpClient的实例
        httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, requestEncode);

        try
        {
            int code = httpClient.executeMethod(postMethod);
            logger.info("Response status code: " + code);//返回200为成功
            resultStr = new String(postMethod.getResponseBodyAsString().getBytes(), responseEncode);
            logger.info("resultStr=" + resultStr);
        } catch (Exception e)
        {
            logger.error("HttpClient异常发生~", e);
        } finally
        {
            postMethod.releaseConnection();
        }

        logger.info("httpclient post 请求 结束==============");
        return resultStr;
    }

    /**
     * httpclient post url 请求
     * @param url            请求地址
     * @param requestEncode  请求编码
     * @param responseEncode 接受编码
     * @return
     */
    public static String postUrl(String url, String requestEncode, String responseEncode)
    {
        logger.info("httpclient post url 请求 开始==============");
        logger.info("url=" + url);
//        logger.debug("没有使用证书，协议注册443端口信任所有证书，开始=================================");
//        Protocol easyHttps1 = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
//        Protocol.registerProtocol("https", easyHttps1);
//        logger.debug("没有使用证书，协议注册443端口信任所有证书，结束=================================");

        String resultStr = null;
        PostMethod postMethod = new PostMethod(url);//创建POST方法的实例
        HttpClient httpClient = new HttpClient();//构造HttpClient的实例
        httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, requestEncode);

        try
        {
            int code = httpClient.executeMethod(postMethod);

            for(int i=0;i<postMethod.getResponseHeaders().length;i++)
            {
                logger.info(postMethod.getResponseHeaders()[i].getName() + "=" + postMethod.getResponseHeaders()[i].getValue());
            }

            logger.info("Response status code: " + code);//返回200为成功
            resultStr = new String(postMethod.getResponseBodyAsString().getBytes(), responseEncode);
            logger.info("resultStr=" + resultStr);
        } catch (Exception e)
        {
            logger.error("HttpClient异常发生~", e);
        } finally
        {
            postMethod.releaseConnection();
        }

        logger.info("httpclient post url 请求 结束==============");
        return resultStr;
    }

    /**
     * httpclient get url 请求
     * @param url            请求地址
     * @param requestEncode  请求编码
     * @param responseEncode 接受编码
     * @return
     */
    public static Map getUrl(String url, String requestEncode, String responseEncode, Cookie[] cookies)
    {
        logger.info("httpclient get url 请求 开始==============");
        logger.info("url=" + url);
        //结果集
        Map result = new HashMap();

        logger.debug("没有使用证书，协议注册443端口信任所有证书，开始=================================");
        Protocol easyHttps1 = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
        Protocol.registerProtocol("https", easyHttps1);
        logger.debug("没有使用证书，协议注册443端口信任所有证书，结束=================================");

        String resultStr = null;
        GetMethod getMethod = new GetMethod(url);//创建get方法的实例
        HttpClient httpClient = new HttpClient();//构造HttpClient的实例
        httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, requestEncode);
        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        try
        {
            //传入cookie
            String cookieStr = getCookieStr(cookies);
            logger.info("=======>>>>cookie:" + cookieStr);
            getMethod.setRequestHeader("Cookie", cookieStr);

            //执行
            int code = httpClient.executeMethod(getMethod);
            cookies = httpClient.getState().getCookies();
            cookieStr = getCookieStr(cookies);
            logger.info("=======<<<<<<cookie:" + cookieStr);
            result.put(KEY_RESPONSE_COOKIES, cookies);

            //header map
            Map responseHeaderMap = new HashMap();
            //获取返回的header
            for(int i=0;i<getMethod.getResponseHeaders().length;i++)
            {
                String name = getMethod.getResponseHeaders()[i].getName();
                String value = getMethod.getResponseHeaders()[i].getValue();
                logger.info("__________<<<<HEADER<<<<<" + name + "=" + value);
                responseHeaderMap.put(name, value);
            }
            result.put(KEY_RESPONSE_HEADER_MAP, responseHeaderMap);

            logger.info("Response status code: " + code);//返回200为成功
            resultStr = new String(getMethod.getResponseBodyAsString().getBytes(), responseEncode);
            logger.info("resultStr=" + resultStr);
            result.put(KEY_RESP_STR, resultStr);
            result.put(KEY_IS_SUCCESS, true);
        } catch (Exception e)
        {
            e.printStackTrace();
            logger.error("HttpClient异常发生~", e);
            result.put(KEY_IS_SUCCESS, false);
        } finally
        {
            getMethod.releaseConnection();
        }

        logger.info("httpclient get url 请求 结束==============");
        return result;
    }

    /**
     * httpclient post props 请求
     * @param url
     * @param props
     * @param requestEncode
     * @param responseEncode
     * @return
     */
    public static String postProps(String url, Properties props, String requestEncode, String responseEncode) throws Exception
    {
        PostMethod method = new PostMethod(url);
        Enumeration enums = props.keys();
        while (enums.hasMoreElements())
        {
            String key = (String)enums.nextElement();
            String value = props.getProperty(key);
            if(StringUtils.isNotBlank(value))
            {
                method.addParameter(key, value);
            }
        }
        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,requestEncode);
        return connect(method, responseEncode);
    }

    /**
     * 与服务器通讯
     * @param method
     * @param responseEncode
     * @return
     * @throws Exception
     */
    public static String connect(PostMethod method, String responseEncode) throws Exception
    {
        InputStream is = null;
        BufferedReader br = null;
        try
        {
        logger.debug("没有使用证书，协议注册443端口信任所有证书，开始=================================");
        Protocol easyHttps1 = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
        Protocol.registerProtocol("https", easyHttps1);
        logger.debug("没有使用证书，协议注册443端口信任所有证书，结束=================================");

            HttpClient client = new HttpClient();
            int returnCode = client.executeMethod(method);
            if (HttpURLConnection.HTTP_OK != returnCode)
            {
                logger.error("与服务器交互发生异常,returnCode=" + returnCode);
            }
            is = method.getResponseBodyAsStream();
            br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), responseEncode));
            StringBuffer response = new StringBuffer();
            String readLine;
            while (((readLine = br.readLine()) != null))
            {
                response.append(readLine);
            }
            //logger.info("response.toString=====>" + response.toString());
            return response.toString();
        } catch (Exception e)
        {
            logger.error("与服务器交互发生异常~", e);
            throw new RuntimeException("与服务器交互发生异常~", e);
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
                logger.error("与服务器交互发生异常~", e);
            }
        }
    }

    /**
     * 下载图片
     * @param imageUrl
     * @param cookies
     * @return
     */
    public static Map downloadImage(String imageUrl, Cookie[] cookies)
    {
        //结果集
        Map result = new HashMap();

        logger.info("没有使用证书，协议注册443端口信任所有证书，开始=================================");
        Protocol easyHttps1 = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
        Protocol.registerProtocol("https", easyHttps1);
        logger.info("没有使用证书，协议注册443端口信任所有证书，结束=================================");

        HttpClient client = new HttpClient();
        GetMethod getMethod = new GetMethod(imageUrl);
        try {
            //传入cookie
            String cookieStr = getCookieStr(cookies);
            logger.info("=======>>>>>>>cookie:" + cookieStr);
            getMethod.setRequestHeader("Cookie", cookieStr);

            client.executeMethod(getMethod);
            cookies = client.getState().getCookies();
            cookieStr = getCookieStr(cookies);
            logger.info("=======<<<<<<cookie:" + cookieStr);
            result.put(KEY_RESPONSE_COOKIES, cookies);

            //header map
            Map responseHeaderMap = new HashMap();
            //获取返回的header
            for(int i=0;i<getMethod.getResponseHeaders().length;i++)
            {
                String name = getMethod.getResponseHeaders()[i].getName();
                String value = getMethod.getResponseHeaders()[i].getValue();
                responseHeaderMap.put(name, value);
            }
            result.put(KEY_RESPONSE_HEADER_MAP, responseHeaderMap);

            String name = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            String checkImgRoute = BaseUtils_2.getImgCacheDir() + name + ".jpg";
            File storeFile = new File(checkImgRoute);
            FileOutputStream fileOutputStream = new FileOutputStream(storeFile);
            FileOutputStream output = fileOutputStream;
            output.write(getMethod.getResponseBody());
            output.close();
            result.put(KEY_CHECK_IMG_ROUTE, checkImgRoute);
            result.put(KEY_IS_SUCCESS, true);
        } catch (HttpException e) {
            e.printStackTrace();
            result.put(KEY_IS_SUCCESS, false);
        } catch (IOException e) {
            e.printStackTrace();
            result.put(KEY_IS_SUCCESS, false);
        }
        return result;
    }

    /**
     * 与服务器通讯
     * @param method
     * @param responseEncode
     * @param cookies
     * @return
     * @throws Exception
     */
    public static Map connect(PostMethod method, String responseEncode, Cookie[] cookies) throws Exception
    {
        //结果集
        Map result = new HashMap();

        InputStream is = null;
        BufferedReader br = null;
        try
        {
            logger.debug("没有使用证书，协议注册443端口信任所有证书，开始=================================");
            Protocol easyHttps1 = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
            Protocol.registerProtocol("https", easyHttps1);
            logger.debug("没有使用证书，协议注册443端口信任所有证书，结束=================================");

            //传入cookie
            String cookieStr = getCookieStr(cookies);
            logger.info("=======>>>>>>>>cookie:" + cookieStr);
            method.setRequestHeader("Cookie", cookieStr);

            HttpClient client = new HttpClient();
            int returnCode = client.executeMethod(method);

            cookies = client.getState().getCookies();
            cookieStr = getCookieStr(cookies);
            logger.info("=======<<<<<<cookie:" + cookieStr);
            result.put(KEY_RESPONSE_COOKIES, cookies);

            //header map
            Map responseHeaderMap = new HashMap();
            //获取返回的header
            for(int i=0;i<method.getResponseHeaders().length;i++)
            {
                String name = method.getResponseHeaders()[i].getName();
                String value = method.getResponseHeaders()[i].getValue();
                responseHeaderMap.put(name, value);
            }
            result.put(KEY_RESPONSE_HEADER_MAP, responseHeaderMap);

            if (HttpURLConnection.HTTP_OK != returnCode)
            {
                logger.error("与服务器交互发生异常,returnCode=" + returnCode);
            }
            is = method.getResponseBodyAsStream();
            br = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(), responseEncode));
            StringBuffer response = new StringBuffer();
            String readLine;
            while (((readLine = br.readLine()) != null))
            {
                response.append(readLine);
            }
            result.put(KEY_IS_SUCCESS, true);
            result.put(KEY_RESP_STR, response.toString());
        } catch (Exception e)
        {
            result.put(KEY_IS_SUCCESS, false);
            logger.error("与服务器交互发生异常~", e);
            throw new RuntimeException("与服务器交互发生异常~", e);
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
                logger.error("与服务器交互发生异常~", e);
            }
            return result;
        }
    }

    /**
     * 得到cookie字符串
     * @param cookies
     * @return
     */
    public static String getCookieStr(Cookie[] cookies){
        String cookieStr = StringUtils.EMPTY;
        for(Cookie cookie : cookies) {
            if(StringUtils.isNotBlank(cookieStr)){
                cookieStr += "; ";
            }
            cookieStr += cookie.toString();
        }
        return cookieStr;
    }
}