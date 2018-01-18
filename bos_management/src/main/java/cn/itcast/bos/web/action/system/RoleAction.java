package cn.itcast.bos.web.action.system;

import cn.itcast.bos.domain.system.Role;
import cn.itcast.bos.service.system.RoleService;
import cn.itcast.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Created by Ricky on 2018/1/17
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class RoleAction extends BaseAction<Role> {

    //注入service
    @Autowired
    private RoleService roleService;

    private String[] permissionIds;
    private String menuIds;


    public void setPermissionIds(String[] permissionIds) {
        this.permissionIds = permissionIds;
    }

    public void setMenuIds(String menuIds) {
        this.menuIds = menuIds;
    }

    /**

     * 查找所有角色的方法
     *
     * @return
     */
    @Action(value = "role_list", results = {@Result(name = "success", type = "json")})
    public String list() {
        //调用业务层进行查找
        List<Role> roles = roleService.findAll();
        //经结果压入值栈
        ActionContext.getContext().getValueStack().push(roles);
        return SUCCESS;
    }

    /**
     * 角色保存的方法
     *
     * @return
     */
    @Action(value = "role_save", results = {@Result(name = "success", type = "redirect", location = "pages/system/role.html")})
    public String save() {
        roleService.save(model,permissionIds,menuIds);
        return SUCCESS;
    }
}
