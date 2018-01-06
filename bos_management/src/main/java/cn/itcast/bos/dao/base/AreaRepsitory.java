package cn.itcast.bos.dao.base;


import cn.itcast.bos.domain.base.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Ricky on 2017/12/27
 */
public interface AreaRepsitory extends JpaRepository<Area,String>,JpaSpecificationExecutor<Area>{
}
