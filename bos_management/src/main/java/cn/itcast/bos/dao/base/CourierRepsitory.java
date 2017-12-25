package cn.itcast.bos.dao.base;

import cn.itcast.bos.domain.base.Courier;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Ricky on 2017/12/25
 */
public interface CourierRepsitory extends JpaRepository<Courier,Integer>{
}
