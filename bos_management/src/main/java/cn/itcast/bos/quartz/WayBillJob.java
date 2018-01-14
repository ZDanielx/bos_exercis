package cn.itcast.bos.quartz;

import cn.itcast.bos.dao.base.WayBillRepsitory;
import cn.itcast.bos.domain.take_delivery.Order;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.base.WayBillService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ricky on 2018/1/13
 */
public class WayBillJob implements Job{
    @Autowired
    private WayBillService wayBillService;
    @Autowired
    private WayBillIndexRepository wayBillIndexRepository;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        List<WayBill> wayBills = wayBillService.findAll();
        System.out.println("石敏是傻逼");
        for (WayBill wayBill : wayBills) {
         /*   Order order = wayBill.getOrder();
            order = null;
            wayBill.setOrder(order);*/
            wayBillIndexRepository.save(wayBill);
        }
     }
}
