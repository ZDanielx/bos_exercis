package cn.itcast.bos.service.base;


import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.domain.take_delivery.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.ws.rs.*;
import java.util.Date;

/**
 * Created by Ricky on 2018/1/5
 */

public interface PromotionService {
    /**
     * 保存活动任务数据的方法
     *
     * @param model
     */
    void save(Promotion model);

    /**
     * 活动任务的分页查询功能
     *
     * @param pageable
     * @return
     */
    Page<Promotion> findPageData(Pageable pageable);

    /**
     * 根据page和rows返回分页数据
     *
     * @return
     */
    @Path("/pageQuery")
    @GET
    @Produces({"application/xml", "application/json"})
    PageBean<Promotion> findpageData(@QueryParam("page") int page, @QueryParam("rows") int rows);

    /**
     * 根据id查询promotion的方法
     *
     * @param id
     * @return
     */
    @Path("/promotion/{id}")
    @GET
    @Produces({"application/xml", "application/json"})
    public Promotion findById(@PathParam("id") Integer id);

    /**
     * 定时过时任务的方法
     *
     * @param date
     */
    void updateStatus(Date date);
}
