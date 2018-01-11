package cn.itcast.bos.dao.base;


import cn.itcast.bos.domain.take_delivery.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

/**
 * Created by Ricky on 2018/1/5
 */
public interface PromotionRepsitory extends JpaRepository<Promotion, Integer>, JpaSpecificationExecutor<Promotion> {

    @Query("update Promotion set status='2' where status='1'and endDate<?")
    @Modifying
    public void updateStatus(Date now);
}
