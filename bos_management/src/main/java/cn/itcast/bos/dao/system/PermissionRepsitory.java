package cn.itcast.bos.dao.system;

import cn.itcast.bos.domain.system.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Ricky on 2018/1/15
 */
public interface PermissionRepsitory extends JpaRepository<Permission,Integer>,JpaSpecificationExecutor<Permission> {

    @Query("from Permission p inner join fetch p.roles r inner join fetch r.users u where u.id =?")
    List<Permission> findByUser(Integer id);
}
