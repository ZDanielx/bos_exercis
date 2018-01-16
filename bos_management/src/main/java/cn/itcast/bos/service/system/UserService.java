package cn.itcast.bos.service.system;

import cn.itcast.bos.domain.system.User;

/**
 * Created by Ricky on 2018/1/15
 */
public interface UserService {
    /**
     * 根据用户名查询用户的方法
     * @param username
     * @return
     */
    User findByUsername(String username);
}
