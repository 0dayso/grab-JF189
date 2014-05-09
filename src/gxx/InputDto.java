package gxx;

/**
 * excel数据每行对应的输入数据
 * Create by Gxx
 * Time: 2013-10-09 03:22
 */
public class InputDto
{
    /**
     * 手机号码
     */
    String shouJiHaoMa;

    /**
     * 手机密码
     */
    String shouJiMiMa;

    /**
     * 是否查询余额
     */
    boolean isQueryBal;

    /**
     * 是否呼叫转移设置
     */
    boolean isCallDivert;

    /**
     * 无条件呼叫转移号码
     */
    String unConditionNum;

    /**
     * 无应答呼叫转移号码
     */
    String unAnswerNum;

    /**
     * 遇忙时呼叫转移号码
     */
    String callBusyNum;

    /**
     * 是否校验通过
     */
    boolean isValidate;

    /**
     * 错误信息
     */
    String errorMsg;

    /**
     * 构造函数
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
