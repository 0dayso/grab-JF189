package gxx;

/**
 * excel����ÿ�ж�Ӧ����������
 * Create by Gxx
 * Time: 2013-10-09 03:22
 */
public class InputDto
{
    /**
     * �ֻ�����
     */
    String shouJiHaoMa;

    /**
     * �ֻ�����
     */
    String shouJiMiMa;

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
     * �Ƿ�У��ͨ��
     */
    boolean isValidate;

    /**
     * ������Ϣ
     */
    String errorMsg;

    /**
     * ���캯��
     * @param shouJiHaoMa
     * @param shouJiMiMa
     * @param isQueryBal
     * @param isCallDivert
     * @param unConditionNum
     * @param unAnswerNum
     * @param callBusyNum
     */
    public InputDto(String shouJiHaoMa, String shouJiMiMa, boolean isQueryBal, boolean isCallDivert,
                    String unConditionNum, String unAnswerNum, String callBusyNum)
    {
        this.shouJiHaoMa = shouJiHaoMa;
        this.shouJiMiMa = shouJiMiMa;
        this.isQueryBal = isQueryBal;
        this.isCallDivert = isCallDivert;
        this.unConditionNum = unConditionNum;
        this.unAnswerNum = unAnswerNum;
        this.callBusyNum = callBusyNum;
        this.isValidate = true;
    }

    public String getShouJiHaoMa() {
        return shouJiHaoMa;
    }

    public void setShouJiHaoMa(String shouJiHaoMa) {
        this.shouJiHaoMa = shouJiHaoMa;
    }

    public String getShouJiMiMa() {
        return shouJiMiMa;
    }

    public void setShouJiMiMa(String shouJiMiMa) {
        this.shouJiMiMa = shouJiMiMa;
    }

    public String getUnConditionNum() {
        return unConditionNum;
    }

    public void setUnConditionNum(String unConditionNum) {
        this.unConditionNum = unConditionNum;
    }

    public String getUnAnswerNum() {
        return unAnswerNum;
    }

    public void setUnAnswerNum(String unAnswerNum) {
        this.unAnswerNum = unAnswerNum;
    }

    public String getCallBusyNum() {
        return callBusyNum;
    }

    public void setCallBusyNum(String callBusyNum) {
        this.callBusyNum = callBusyNum;
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

    public boolean isQueryBal() {
        return isQueryBal;
    }

    public void setQueryBal(boolean queryBal) {
        isQueryBal = queryBal;
    }

    public boolean isCallDivert() {
        return isCallDivert;
    }

    public void setCallDivert(boolean callDivert) {
        isCallDivert = callDivert;
    }
}
