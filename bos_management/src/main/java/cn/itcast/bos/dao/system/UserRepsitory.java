package cn.itcast.bos.dao.system;

import cn.itcast.bos.domain.system.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Ricky on 2018/1/15
 */
public interface UserRepsitory extends JpaRepository<User,Integer>,JpaSpecificationExecutor<User>{
    User findByUsername(String username);
}
