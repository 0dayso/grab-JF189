package gxx; /**
 * File Name:    gxx.HttpClientUtils.java
 *
 * File Desc:    HttpClient������
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
 * HttpClient������
 *
 * @author Gxx
 * @module oa
 * @datetime 14-4-11 11:18
 */
public class HttpClientUtils implements BaseInterface_2
{
    /**
     * ��־��¼��
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
     * httpclient post ����
     *
     * @param url            �����ַ
     * @param xml            ����xml����
     * @param requestEncode  �������
     * @param responseEncode ���ܱ���
     * @return
     */
    public static String post(String url, String xml, String requestEncode, String responseEncode)
    {
        logger.info("httpclient post ���� ��ʼ==============");
        logger.info("url=" + url);
        //PropertyUtil.getInstance().refresh();
        logger.info("requestXml=" + xml);
//        logger.debug("û��ʹ��֤�飬Э��ע��443�˿���������֤�飬��ʼ=================================");
//        Protocol easyHttps1 = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
//        Protocol.registerProtocol("https", easyHttps1);
//        logger.debug("û��ʹ��֤�飬Э��ע��443�˿���������֤�飬����=================================");

        String resultStr = null;
        PostMethod postMethod = new PostMethod(url);//����POST������ʵ��
        postMethod.setRequestBody(xml);//������ֵ����postMethod��
        postMethod.addRequestHeader("Content", "text/xml,charset=" + requestEncode + "");
        HttpClient httpClient = new HttpClient();//����HttpClient��ʵ��
        httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, requestEncode);

        try
        {
            int code = httpClient.executeMethod(postMethod);
            logger.info("Response status code: " + code);//����200Ϊ�ɹ�
            resultStr = new String(postMethod.getResponseBodyAsString().getBytes(), responseEncode);
            logger.info("resultStr=" + resultStr);
        } catch (Exception e)
        {
            logger.error("HttpClient�쳣����~", e);
        } finally
        {
            postMethod.releaseConnection();
        }

        logger.info("httpclient post ���� ����==============");
        return resultStr;
    }

    /**
     * httpclient post url ����
     * @param url            �����ַ
     * @param requestEncode  �������
     * @param responseEncode ���ܱ���
     * @return
     */
    public static String postUrl(String url, String requestEncode, String responseEncode)
    {
        logger.info("httpclient post url ���� ��ʼ==============");
        logger.info("url=" + url);
//        logger.debug("û��ʹ��֤�飬Э��ע��443�˿���������֤�飬��ʼ=================================");
//        Protocol easyHttps1 = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
//        Protocol.registerProtocol("https", easyHttps1);
//        logger.debug("û��ʹ��֤�飬Э��ע��443�˿���������֤�飬����=================================");

        String resultStr = null;
        PostMethod postMethod = new PostMethod(url);//����POST������ʵ��
        HttpClient httpClient = new HttpClient();//����HttpClient��ʵ��
        httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, requestEncode);

        try
        {
            int code = httpClient.executeMethod(postMethod);

            for(int i=0;i<postMethod.getResponseHeaders().length;i++)
            {
                logger.info(postMethod.getResponseHeaders()[i].getName() + "=" + postMethod.getResponseHeaders()[i].getValue());
            }

            logger.info("Response status code: " + code);//����200Ϊ�ɹ�
            resultStr = new String(postMethod.getResponseBodyAsString().getBytes(), responseEncode);
            logger.info("resultStr=" + resultStr);
        } catch (Exception e)
        {
            logger.error("HttpClient�쳣����~", e);
        } finally
        {
            postMethod.releaseConnection();
        }

        logger.info("httpclient post url ���� ����==============");
        return resultStr;
    }

    /**
     * httpclient get url ����
     * @param url            �����ַ
     * @param requestEncode  �������
     * @param responseEncode ���ܱ���
     * @return
     */
    public static Map getUrl(String url, String requestEncode, String responseEncode, Cookie[] cookies)
    {
        logger.info("httpclient get url ���� ��ʼ==============");
        logger.info("url=" + url);
        //�����
        Map result = new HashMap();

        logger.debug("û��ʹ��֤�飬Э��ע��443�˿���������֤�飬��ʼ=================================");
        Protocol easyHttps1 = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
        Protocol.registerProtocol("https", easyHttps1);
        logger.debug("û��ʹ��֤�飬Э��ע��443�˿���������֤�飬����=================================");

        String resultStr = null;
        GetMethod getMethod = new GetMethod(url);//����get������ʵ��
        HttpClient httpClient = new HttpClient();//����HttpClient��ʵ��
        httpClient.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, requestEncode);
        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        try
        {
            //����cookie
            String cookieStr = getCookieStr(cookies);
            logger.info("=======>>>>cookie:" + cookieStr);
            getMethod.setRequestHeader("Cookie", cookieStr);

            //ִ��
            int code = httpClient.executeMethod(getMethod);
            cookies = httpClient.getState().getCookies();
            cookieStr = getCookieStr(cookies);
            logger.info("=======<<<<<<cookie:" + cookieStr);
            result.put(KEY_RESPONSE_COOKIES, cookies);

            //header map
            Map responseHeaderMap = new HashMap();
            //��ȡ���ص�header
            for(int i=0;i<getMethod.getResponseHeaders().length;i++)
            {
                String name = getMethod.getResponseHeaders()[i].getName();
                String value = getMethod.getResponseHeaders()[i].getValue();
                logger.info("__________<<<<HEADER<<<<<" + name + "=" + value);
                responseHeaderMap.put(name, value);
            }
            result.put(KEY_RESPONSE_HEADER_MAP, responseHeaderMap);

            logger.info("Response status code: " + code);//����200Ϊ�ɹ�
            resultStr = new String(getMethod.getResponseBodyAsString().getBytes(), responseEncode);
            logger.info("resultStr=" + resultStr);
            result.put(KEY_RESP_STR, resultStr);
            result.put(KEY_IS_SUCCESS, true);
        } catch (Exception e)
        {
            e.printStackTrace();
            logger.error("HttpClient�쳣����~", e);
            result.put(KEY_IS_SUCCESS, false);
        } finally
        {
            getMethod.releaseConnection();
        }

        logger.info("httpclient get url ���� ����==============");
        return result;
    }

    /**
     * httpclient post props ����
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
     * �������ͨѶ
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
        logger.debug("û��ʹ��֤�飬Э��ע��443�˿���������֤�飬��ʼ=================================");
        Protocol easyHttps1 = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
        Protocol.registerProtocol("https", easyHttps1);
        logger.debug("û��ʹ��֤�飬Э��ע��443�˿���������֤�飬����=================================");

            HttpClient client = new HttpClient();
            int returnCode = client.executeMethod(method);
            if (HttpURLConnection.HTTP_OK != returnCode)
            {
                logger.error("����������������쳣,returnCode=" + returnCode);
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
            logger.error("����������������쳣~", e);
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
                logger.error("����������������쳣~", e);
            }
        }
    }

    /**
     * ����ͼƬ
     * @param imageUrl
     * @param cookies
     * @return
     */
    public static Map downloadImage(String imageUrl, Cookie[] cookies)
    {
        //�����
        Map result = new HashMap();

        logger.info("û��ʹ��֤�飬Э��ע��443�˿���������֤�飬��ʼ=================================");
        Protocol easyHttps1 = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
        Protocol.registerProtocol("https", easyHttps1);
        logger.info("û��ʹ��֤�飬Э��ע��443�˿���������֤�飬����=================================");

        HttpClient client = new HttpClient();
        GetMethod getMethod = new GetMethod(imageUrl);
        try {
            //����cookie
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
            //��ȡ���ص�header
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
     * �������ͨѶ
     * @param method
     * @param responseEncode
     * @param cookies
     * @return
     * @throws Exception
     */
    public static Map connect(PostMethod method, String responseEncode, Cookie[] cookies) throws Exception
    {
        //�����
        Map result = new HashMap();

        InputStream is = null;
        BufferedReader br = null;
        try
        {
            logger.debug("û��ʹ��֤�飬Э��ע��443�˿���������֤�飬��ʼ=================================");
            Protocol easyHttps1 = new Protocol("https", new EasySSLProtocolSocketFactory(), 443);
            Protocol.registerProtocol("https", easyHttps1);
            logger.debug("û��ʹ��֤�飬Э��ע��443�˿���������֤�飬����=================================");

            //����cookie
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
            //��ȡ���ص�header
            for(int i=0;i<method.getResponseHeaders().length;i++)
            {
                String name = method.getResponseHeaders()[i].getName();
                String value = method.getResponseHeaders()[i].getValue();
                responseHeaderMap.put(name, value);
            }
            result.put(KEY_RESPONSE_HEADER_MAP, responseHeaderMap);

            if (HttpURLConnection.HTTP_OK != returnCode)
            {
                logger.error("����������������쳣,returnCode=" + returnCode);
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
            logger.error("����������������쳣~", e);
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
                logger.error("����������������쳣~", e);
            }
            return result;
        }
    }

    /**
     * �õ�cookie�ַ���
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