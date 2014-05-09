package gxx;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * ץȡjf.189.cn
 *
 * @author Gxx
 * @module oa
 * @datetime 14-4-25 00:13
 */
public class GrabJF189
{
    /**
     * �û���
     */
    private static final String USER_NAME = "15394184845";
    /**
     * ����
     */
    private static final String PASSWORD = "123654";
    /**
     * ��½�����ַ �� ��½�ύ��ַ ��ͬһ��
     */
    private String LOGIN_URL = "https://uam.ct10000.com/ct10000uam/login?service=https%3A%2F" +
            "%2Fsso.jf.189.cn%3A443%2F%2Fsso%2Flogin%3Fservice%3Dhttp%3A%2F%2Fjf.189.cn%2FHome%2FLogin." +
            "aspx%3Fref%3DaHR0cDovL2pmLjE4OS5jbi9Ib21lL2luZGV4LmFzcHg%3D&register=registerMB";

    /**
     * ��½�����ַ �� ��½�ύ��ַ ��ͬһ��
     * �̻���¼
     */
    public static final String LOGIN_URL_FOR_GUHUA = "https://uam.ct10000.com/ct10000uam/login?service=https%3A%2F" +
            "%2Fsso.jf.189.cn%3A443%2F%2Fsso%2Flogin%3Fservice%3Dhttp%3A%2F%2Fjf.189.cn%2FHome%2FLogin." +
            "aspx%3Fref%3DaHR0cDovL2pmLjE4OS5jbi9Ib21lL2luZGV4LmFzcHg%3D&register=registerPH";
    /**
     * ͼƬ��ַ
     */
    private String IMAGE_URL = "https://uam.ct10000.com/ct10000uam/validateImg.jsp?rand=";

    /**
     * ����������
     */
    public void process() throws Exception
    {
        // 1.���ʵ�½�����ַ����ȡcookie��spring web flow��(lt)
        Map result = HttpClientUtils.getUrl(LOGIN_URL, "UTF-8", "GBK", new Cookie[]{});//���ؽ��
        boolean isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//���Ƿ�ɹ�
        if(!isSuccess)
        {
            System.out.println("ʧ�ܣ�");//�Ƿ���ֹ
            return;
        }
        Cookie[] cookies = (Cookie[])result.get(HttpClientUtils.KEY_RESPONSE_COOKIES);//����ͷ
        String webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//��ҳ����
        String springWebFlow = getSpringWebFlow(webContent);//������ҳ��ַ�õ�springWebFlow

        // 2.����ͼƬ��ַ������ͼƬ
        result = HttpClientUtils.downloadImage(IMAGE_URL + Math.random(), cookies);//���ؽ��
        isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//���Ƿ�ɹ�
        if(!isSuccess)
        {
            System.out.println("ʧ�ܣ�");//�Ƿ���ֹ
            return;
        }

        // 3.����½
        result = login(springWebFlow, cookies);
        isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//���Ƿ�ɹ�
        if(!isSuccess)
        {
            System.out.println("ʧ�ܣ�");//�Ƿ���ֹ
            return;
        }
        webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//��ҳ���� ����᷵�ؿյ��ַ���
        cookies = (Cookie[])result.get(HttpClientUtils.KEY_RESPONSE_COOKIES);//����ͷ
        Map headerMap = (Map)result.get(HttpClientUtils.KEY_RESPONSE_HEADER_MAP);//����ͷ

        // 4.��½��֤�ɹ�����һ����ת����ַλ��ResponseHeaders�еĵ�Location
        String jumpUrl = (String)headerMap.get("Location");
        System.out.println("��½��֤�ɹ�����һ����ת����ַ:" + jumpUrl);
        result = HttpClientUtils.getUrl(jumpUrl, "UTF-8", "GBK", new Cookie[]{});//���ؽ�� ���Բ���cookie
        isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//���Ƿ�ɹ�
        if(!isSuccess)
        {
            System.out.println("ʧ�ܣ�");//�Ƿ���ֹ
            return;
        }
        webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//��ҳ����
        System.out.println("��һ����ת����������" + webContent);
        headerMap = (Map)result.get(HttpClientUtils.KEY_RESPONSE_HEADER_MAP);//����ͷ
        //cookies = (Cookie[])result.get(HttpClientUtils.KEY_RESPONSE_COOKIES);//����ͷ

        // 5.�ڶ�����ת������ ��һ����ת �������ݵõ��ڶ�����ת��ַ
        jumpUrl = getJumpUrl2(webContent);
        System.out.println("�ڶ�����ת����ַ:" + jumpUrl);
        result = HttpClientUtils.getUrl(jumpUrl, "UTF-8", "GBK", new Cookie[]{});//���ؽ�� ���Բ���cookie
        isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//���Ƿ�ɹ�
        if(!isSuccess)
        {
            System.out.println("ʧ�ܣ�");//�Ƿ���ֹ
            return;
        }
        webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//��ҳ����
        System.out.println("�ڶ�����ת����������" + webContent);
        headerMap = (Map)result.get(HttpClientUtils.KEY_RESPONSE_HEADER_MAP);//����ͷ
        //cookies = (Cookie[])result.get(HttpClientUtils.KEY_RESPONSE_COOKIES);//����ͷ

        // 6.��������ת������ �ڶ�����ת �������ݵõ���������ת��ַ
        jumpUrl = getJumpUrl3(webContent);
        System.out.println("��������ת����ַ:" + jumpUrl);
        result = HttpClientUtils.getUrl(jumpUrl, "UTF-8", "GBK", new Cookie[]{});//���ؽ�� ���Բ���cookie
        isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//���Ƿ�ɹ�
        if(!isSuccess)
        {
            System.out.println("ʧ�ܣ�");//�Ƿ���ֹ
            return;
        }
        webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//��ҳ����
        System.out.println("��������ת����������" + webContent);
        cookies = (Cookie[])result.get(HttpClientUtils.KEY_RESPONSE_COOKIES);//����ͷ

        //==============================================================================================
        //��ȡ��Ʒ�б� ���۸����� http://mall.jf.189.cn/Goods/GoodsList
        //http://mall.jf.189.cn/Goods/CanBuyGoodsList

        //http://mall.jf.189.cn:8000/Goods/CanBuyGoodsList

        String canBuyUrl = "http://mall.jf.189.cn/Order/SellOrder?GoodsSn=bbb2a86c-bd84-4665-8947-ddd9f9" +
                "9bd6fa&GoodsAttSn=0a1ba597-3cf8-4799-b7ee-55c118f78a89&GoodNum=1";
        System.out.println("���ĸ�url:" + canBuyUrl);
        result = HttpClientUtils.getUrl(canBuyUrl, "UTF-8", "GBK", cookies);
        //���ؽ�� ���Բ���cookie ���Ƿ��ػ��cookie
        isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//���Ƿ�ɹ�
        if(!isSuccess)
        {
            System.out.println("ʧ�ܣ�");//�Ƿ���ֹ
            return;
        }
        webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//��ҳ����
        System.out.println("+++++++++++��������" + webContent);
        cookies = (Cookie[])result.get(HttpClientUtils.KEY_RESPONSE_COOKIES);//����ͷ

        System.out.println("�����url:" + canBuyUrl);
        result = HttpClientUtils.getUrl(canBuyUrl, "UTF-8", "GBK", cookies);
        //���ؽ�� ���Բ���cookie ���Ƿ��ػ��cookie
        isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//���Ƿ�ɹ�
        if(!isSuccess)
        {
            System.out.println("ʧ�ܣ�");//�Ƿ���ֹ
            return;
        }
        webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//��ҳ����
        System.out.println("+++++++++++��������" + webContent);
        //cookies = (Cookie[])result.get(HttpClientUtils.KEY_RESPONSE_COOKIES);//����ͷ

        String url = "http://jf.189.cn/home/ajax.ashx?g=nocachelogin";
        System.out.println("��½�ɹ�,��ȡ��½��Ϣurl=" + url);
        result = HttpClientUtils.getUrl(url, "UTF-8", "GBK", cookies);//���ؽ�� ���Բ���cookie
        isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//���Ƿ�ɹ�
        if(!isSuccess)
        {
            String errorMessage = "��ȡ��½��Ϣʧ�ܣ�";
        }
        webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//��ҳ����
        System.out.println("��ȡ��½��Ϣ����������" + webContent);
    }

    /**
     * ���� ��һ����ת �������ݵõ��ڶ�����ת��ַ
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
     * ���� �ڶ�����ת �������ݵõ���������ת��ַ
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
     * ������ҳ��ַ�õ�springWebFlow
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
     * ����½
     *
     * @param springWebFlow
     * @param cookies
     * @return
     */
    public Map login(String springWebFlow, Cookie[] cookies) throws Exception{
        PostMethod method = new PostMethod(LOGIN_URL);
        //����
        method.addParameter("forbidpass", "null");
        method.addParameter("forbidaccounts", "null");
        method.addParameter("authtype", "c2000004");
        method.addParameter("open_no", "1");
        method.addParameter("customFileld02", "27");
        method.addParameter("areaname", "����");
        method.addParameter("submitBtn1", "�����ύ.....");
        method.addParameter("_eventId", "submit");
        method.addParameter("c2000004RmbMe", "on");
        method.addParameter("customFileld01", "3");
        method.addParameter("password_r", "");
        method.addParameter("password_c", "");
        //�û���
        method.addParameter("username", USER_NAME);
        //����
        method.addParameter("password", PASSWORD);
        //ͼƬ��֤��
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
        method.addParameter("randomId", input.readLine());
        //spring web flow��
        method.addParameter("lt", springWebFlow);

        for(NameValuePair nameValuePair :  method.getParameters()){
            System.out.println(nameValuePair.getName() + "=" + nameValuePair.getValue());
        }

        //�������
        Map result = HttpClientUtils.connect(method, "UTF-8", cookies);//���ؽ��
        //���ؽ��
        return result;
    }

    /**
     * main����
     * @param param
     * @throws Exception
     */
    public static void main(String[] param) throws Exception
    {
        //����������
        new GrabJF189().process();

//        String sellOrderUrl = "http://mall.jf.189.cn/Order/SellOrder?GoodsSn=bbb2a86c-bd84-4665-8947-ddd9f99bd6fa&GoodsAttSn=0a1ba597-3cf8-4799-b7ee-55c118f78a89&GoodNum=1";
//        Map result = HttpClientUtils.getUrl(sellOrderUrl, "UTF-8", "GBK", new Cookie[]{});
//        //���ؽ�� ���Բ���cookie ���Ƿ��ػ��cookie
//        boolean isSuccess = (Boolean)result.get(HttpClientUtils.KEY_IS_SUCCESS);//���Ƿ�ɹ�
//        if(!isSuccess)
//        {
//            System.out.println("ʧ�ܣ�");//�Ƿ���ֹ
//            return;
//        }
//        String webContent = (String)result.get(HttpClientUtils.KEY_RESP_STR);//��ҳ����
//        System.out.println("+++++++++++��������" + webContent);
    }
}
