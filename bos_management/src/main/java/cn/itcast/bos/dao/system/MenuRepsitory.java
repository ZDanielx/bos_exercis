package cn.itcast.bos.dao.system;

import cn.itcast.bos.domain.system.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Ricky on 2018/1/16
 */
public interface MenuRepsitory extends JpaSpecificationExecutor<Menu>,JpaRepository<Menu,Integer>{

    @Query("from Menu m inner join fetch m.roles r inner join fetch r.users u where u.id = ? order by m.priority")
    List<Menu> findByUser(Integer id);

}
