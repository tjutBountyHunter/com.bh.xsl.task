package com.xsl.task.vo;

import java.io.Serializable;
import java.util.List;

//分页所使用的对象
public class PageObject implements Serializable {
    private long total;
    private List<?> data;

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PageObject{" +
                "total=" + total +
                ", data=" + data +
                '}';
    }
}
