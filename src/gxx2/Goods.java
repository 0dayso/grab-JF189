package gxx2;

/**
 * π¶ƒ‹√Ë ˆ
 *
 * @author Gxx
 * @module oa
 * @datetime 14-4-30 20:37
 */
public class Goods {
    String GoodsSn;
    String GoodsAttSn;
    String GoodsName;
    int IntegralPrice;
    String image;

    public Goods(String goodsSn, String goodsAttSn, String goodsName, int integralPrice, String image) {
        GoodsSn = goodsSn;
        GoodsAttSn = goodsAttSn;
        GoodsName = goodsName;
        IntegralPrice = integralPrice;
        this.image = image;
    }

    public String getGoodsSn() {
        return GoodsSn;
    }

    public void setGoodsSn(String goodsSn) {
        GoodsSn = goodsSn;
    }

    public String getGoodsAttSn() {
        return GoodsAttSn;
    }

    public void setGoodsAttSn(String goodsAttSn) {
        GoodsAttSn = goodsAttSn;
    }

    public String getGoodsName() {
        return GoodsName;
    }

    public void setGoodsName(String goodsName) {
        GoodsName = goodsName;
    }

    public int getIntegralPrice() {
        return IntegralPrice;
    }

    public void setIntegralPrice(int integralPrice) {
        IntegralPrice = integralPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
