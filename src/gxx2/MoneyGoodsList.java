package gxx2;

import java.util.ArrayList;
import java.util.List;

/**
 * π¶ƒ‹√Ë ˆ
 *
 * @author Gxx
 * @module oa
 * @datetime 14-4-30 20:30
 */
public class MoneyGoodsList {
    int page;
    int pageSize;
    int pageCount;
    List<MoneyGoods> moneyGoodsList = new ArrayList<MoneyGoods>();

    public MoneyGoodsList() {
    }

    public MoneyGoodsList(int page, int pageSize, int pageCount, List<MoneyGoods> moneyGoodsList) {
        this.page = page;
        this.pageSize = pageSize;
        this.pageCount = pageCount;
        this.moneyGoodsList = moneyGoodsList;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<MoneyGoods> getMoneyGoodsList() {
        return moneyGoodsList;
    }

    public void setMoneyGoodsList(List<MoneyGoods> moneyGoodsList) {
        this.moneyGoodsList = moneyGoodsList;
    }
}
