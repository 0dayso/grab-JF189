package gxx2;

import org.apache.commons.lang.StringUtils;

/**
 * excel����ÿ�ж�Ӧ����������
 * Create by Gxx
 * Time: 2013-10-09 03:22
 */
public class InputDto_2
{
    /**
     * ��¼��ʽ
     */
    String loginType;
    /**
     * �ֻ�����
     */
    String mobile;

    /**
     * �ֻ�����
     */
    String password;

    /**
     * ����
     */
    String userName;

    /**
     * ʣ�����
     */
    String jf;

    /**
     * �ɹ�����
     */
    String successCount;

    /**
     * �Ƿ�У��ͨ��
     */
    boolean isValidate;

    /**
     * ������Ϣ
     */
    String errorMsg;

    /**
     * ���캯��
     * @param mobile
     * @param password
     */
    public InputDto_2(String loginType, String mobile, String password, String userName, String jf, String successCount) {
        this.loginType = loginType;
        this.mobile = mobile;
        this.password = password;
        this.userName = userName;
        this.jf = jf;
        this.successCount = successCount;
        this.isValidate = true;
        this.errorMsg = StringUtils.EMPTY;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isValidate() {
        return isValidate;
    }

    public void setValidate(boolean validate) {
        isValidate = validate;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getJf() {
        return jf;
    }

    public void setJf(String jf) {
        this.jf = jf;
    }

    public String getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(String successCount) {
        this.successCount = successCount;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}
