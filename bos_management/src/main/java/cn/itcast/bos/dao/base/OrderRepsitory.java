package cn.itcast.bos.dao.base;

import cn.itcast.bos.domain.take_delivery.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Ricky on 2018/1/11
 */
public interface OrderRepsitory extends JpaSpecificationExecutor<Order>,JpaRepository<Order,Integer>{
}
