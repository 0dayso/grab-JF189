package gxx2;

/**
 * π¶ƒ‹√Ë ˆ
 *
 * @author Gxx
 * @module oa
 * @datetime 14-4-30 20:37
 */
public class MoneyGoods {
    String goodsId;
    String goodsName;
    int price;
    String image;

    public MoneyGoods() {
    }

    public MoneyGoods(String goodsId, String goodsName, int price, String image) {
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.price = price;
        this.image = image;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
