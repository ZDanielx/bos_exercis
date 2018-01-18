package cn.itcast.bos.web.action.system;

import cn.itcast.bos.domain.system.Permission;
import cn.itcast.bos.service.system.PermissionService;
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
public class PermissionAction extends BaseAction<Permission> {

    //注入service
    @Autowired
    private PermissionService permissionService;

    /**
     * 查询所权限信息
     *
     * @return
     */
    @Action(value = "permission_list", results = {@Result(name = "success", type = "json")})
    public String list() {
        //调用业务层查询数据
        List<Permission> permissions = permissionService.findAll();
        //将数据传输到前台页面
        ActionContext.getContext().getValueStack().push(permissions);
        return SUCCESS;
    }

    /**
     * 添加去哪先的方法
     *
     * @return
     */
    @Action(value = "permission_save", results = {@Result(name = "success", type = "redirect", location = "pages/system/permission.html")})
    public String save() {
        //调用业务层进行保存
        permissionService.save(model);
        return SUCCESS;
    }
}
