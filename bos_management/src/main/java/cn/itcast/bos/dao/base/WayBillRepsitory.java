package cn.itcast.bos.dao.base;

import cn.itcast.bos.domain.take_delivery.WayBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Ricky on 2018/1/11
 */
public interface WayBillRepsitory extends JpaSpecificationExecutor<WayBill>,JpaRepository<WayBill,Integer>{

    WayBill findByWayBillNum(String wayBillNum);
}
