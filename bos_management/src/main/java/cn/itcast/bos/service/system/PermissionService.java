package cn.itcast.bos.service.system;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.domain.system.User;

import java.util.List;

/**
 * Created by Ricky on 2018/1/15
 */
public interface PermissionService {
    /**
     * 根据用户查询权限
     * @param user
     * @return
     */
    List<Permission> findByUser(User user);

    /**
     * 查询所有权限信息
     */
    List<Permission> findAll();

    /**
     * 保存添加权限的方法
     * @param model
     */
    void save(Permission model);
}
