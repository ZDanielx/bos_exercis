package cn.itcast.bos.dao.base;

import cn.itcast.bos.domain.base.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by Ricky on 2017/12/25
 */
public interface CourierRepsitory extends JpaRepository<Courier, Integer>, JpaSpecificationExecutor<Courier> {

    @Query(value = "update Courier set deltag=? where id = ?")
    @Modifying
    public void Batch(Character deltag, Integer id);
}
