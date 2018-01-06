package cn.itcast.bos.dao.base;


import cn.itcast.bos.domain.base.FixedArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Ricky on 2017/12/28
 */
public interface FixedAreaRepsitory extends JpaSpecificationExecutor<FixedArea>,JpaRepository<FixedArea,String>{
}
