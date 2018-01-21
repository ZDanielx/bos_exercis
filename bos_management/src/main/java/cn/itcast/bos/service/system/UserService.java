package cn.itcast.bos.service.system;

import cn.itcast.bos.domain.system.User;

import java.util.List;

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

    /**
     * 查询所有用户的方法
     * @return
     */
    List<User> findAll();

    /**
     * 用户的保存方法
     * @param roleIds
     * @param model
     */
    void save(String[] roleIds, User model);
}
