package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.take_delivery.Order;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * Created by Ricky on 2018/1/11
 */
public interface OrderService {

    /**
     * 添加自动分单的方法
     * @param order
     */
    @Path("/order")
    @POST
    @Consumes({"application/xml","application/json"})
    public void addOrder(Order order);

    /**
     * 根据findByOrderNum查询order的方法
     * @param orderNum
     * @return
     */
    Order findByOrderNum(String orderNum);
}
