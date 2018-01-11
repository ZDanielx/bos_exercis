package cn.itcast.bos.dao.base;

import cn.itcast.bos.domain.take_delivery.WorkBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Ricky on 2018/1/11
 */
public interface WorkBillRepository extends JpaRepository<WorkBill,Integer>,JpaSpecificationExecutor<WorkBill>{
}
