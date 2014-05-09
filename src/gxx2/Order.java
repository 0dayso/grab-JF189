package gxx2;

/**
 * 订单
 *
 * @author Gxx
 * @module oa
 * @datetime 14-5-2 18:08
 */
public class Order {
    /**
     * 用户信息
     */
    String mobile;
    String userName;

    /**
     * 产品信息
     */
    String goodsId;
    String goodsName;
    int singleJiFen;
    int buyNum;

    /**
     * 订单信息
     */
    String orderId;
    boolean isSuccess;
    String cardNo;
    String password;
    String deadline;

    /**
     * 构造函数
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
     * 重写toString 方法
     * @return
     */
    public String toString(){
        String result = "手机号:[" + mobile + "],用户名字:[" + userName + "],产品id:[" + goodsId + "]," +
                "产品名称:[" + goodsName + "],单笔积分:[" + singleJiFen + "],笔数:[" + buyNum + "]," +
                "订单号:[" + orderId + "],是否成功:[" + isSuccess + "],卡号:[" + cardNo + "]," +
                "密码:[" + password + "],有效期:[" + deadline + "]";
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
