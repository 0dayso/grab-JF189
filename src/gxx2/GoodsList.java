package gxx2;

import java.util.List;

/**
 * π¶ƒ‹√Ë ˆ
 *
 * @author Gxx
 * @module oa
 * @datetime 14-4-30 20:30
 */
public class GoodsList {
    int page;
    int pageSize;
    int pageCount;
    List<Goods> goodsList;

    public GoodsList() {
    }

    public GoodsList(int page, int pageSize, int pageCount, List<Goods> goodsList) {
        this.page = page;
        this.pageSize = pageSize;
        this.pageCount = pageCount;
        this.goodsList = goodsList;
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

    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }
}
