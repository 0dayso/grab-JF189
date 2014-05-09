package gxx2;

/**
 * ����
 *
 * @author Gxx
 * @module oa
 * @datetime 14-5-2 18:08
 */
public class Order {
    /**
     * �û���Ϣ
     */
    String mobile;
    String userName;

    /**
     * ��Ʒ��Ϣ
     */
    String goodsId;
    String goodsName;
    int singleJiFen;
    int buyNum;

    /**
     * ������Ϣ
     */
    String orderId;
    boolean isSuccess;
    String cardNo;
    String password;
    String deadline;

    /**
     * ���캯��
     * @param mobile
     * @param userName
     * @param goodsId
     * @param goodsName
     * @param singleJiFen
     * @param buyNum
     * @param orderId
     * @param isSuccess
     * @param cardNo
     * @param password
     * @param deadline
     */
    public Order(String mobile, String userName, String goodsId, String goodsName, int singleJiFen, int buyNum,
                 String orderId, boolean isSuccess, String cardNo, String password, String deadline) {
        this.mobile = mobile;
        this.userName = userName;
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.singleJiFen = singleJiFen;
        this.buyNum = buyNum;
        this.orderId = orderId;
        this.isSuccess = isSuccess;
        this.cardNo = cardNo;
        this.password = password;
        this.deadline = deadline;
    }

    /**
     * ��дtoString ����
     * @return
     */
    public String toString(){
        String result = "�ֻ���:[" + mobile + "],�û�����:[" + userName + "],��Ʒid:[" + goodsId + "]," +
                "��Ʒ����:[" + goodsName + "],���ʻ���:[" + singleJiFen + "],����:[" + buyNum + "]," +
                "������:[" + orderId + "],�Ƿ�ɹ�:[" + isSuccess + "],����:[" + cardNo + "]," +
                "����:[" + password + "],��Ч��:[" + deadline + "]";
        return result;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getSingleJiFen() {
        return singleJiFen;
    }

    public void setSingleJiFen(int singleJiFen) {
        this.singleJiFen = singleJiFen;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }
}
