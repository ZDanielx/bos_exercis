package cn.itcast.bos.domain.page;

import cn.itcast.bos.domain.take_delivery.Promotion;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import java.util.List;

/**
 * Created by Ricky on 2018/1/6
 * //自定义分页封装实体类
 */
@XmlRootElement(name = "pageBean")
@XmlSeeAlso(value = {Promotion.class})
public class PageBean<T>{
    private long totalCount;//总记录数
    private List<T> pageData;//当前页数据

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getPageData() {
        return pageData;
    }

    public void setPageData(List<T> pageData) {
        this.pageData = pageData;
    }


}
