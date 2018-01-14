package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.take_delivery.WayBill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Ricky on 2018/1/11
 */
public interface WayBillService {
    /**
     * 保存运单的方法
     * @param model
     */
    void save(WayBill model);

    /**
     * 无条件分页查询订单
     * @param pageable
     * @return
     */
    Page<WayBill> pageQuery(WayBill model, Pageable pageable);

    /**
     * 通过运单编号查询运单的方法
     * @param wayBillNum
     * @return
     */
    WayBill findByWayBillNum(String wayBillNum);

    /**
     * 查询所有waybill的方法
     * @return
     */
    List<WayBill> findAll();

}
