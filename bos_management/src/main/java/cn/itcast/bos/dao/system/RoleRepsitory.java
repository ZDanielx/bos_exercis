package cn.itcast.bos.dao.system;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Ricky on 2018/1/15
 */
public interface RoleRepsitory extends JpaSpecificationExecutor<Role>,JpaRepository<Role,Integer>{

    @Query("from Role r inner join fetch r.users u where u.id = ?")
    List<Role> findByUser(Integer id);
}
