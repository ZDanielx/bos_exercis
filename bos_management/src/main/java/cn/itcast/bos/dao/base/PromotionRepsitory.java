package cn.itcast.bos.dao.base;


import cn.itcast.bos.domain.take_delivery.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Ricky on 2018/1/5
 */
public interface PromotionRepsitory extends JpaRepository<Promotion,Integer>,JpaSpecificationExecutor<Promotion>{

}
