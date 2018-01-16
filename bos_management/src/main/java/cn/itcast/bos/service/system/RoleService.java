package cn.itcast.bos.service.system;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.domain.system.User;

import java.util.List;

/**
 * Created by Ricky on 2018/1/15
 */
public interface RoleService {

    /**
     * 根据用户查询角色
     * @param user
     * @return
     */
    List<Role> findByUser(User user);
}
