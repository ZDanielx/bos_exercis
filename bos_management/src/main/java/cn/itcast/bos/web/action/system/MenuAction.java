package cn.itcast.bos.web.action.system;

import cn.itcast.bos.domain.system.Menu;
import cn.itcast.bos.domain.system.User;
import cn.itcast.bos.service.system.MenuService;
import cn.itcast.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Created by Ricky on 2018/1/16
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class MenuAction extends BaseAction<Menu> {

    //注入service
    @Autowired
    private MenuService menuService;

    /**
     * 查询所有菜单的方法
     *
     * @return
     */
    @Action(value = "menu_list", results = {@Result(name = "success", type = "json")})
    public String list() {
        //调用业务层查讯
        List<Menu> menus = menuService.findAll();
        //将结果存入值栈
        ActionContext.getContext().getValueStack().push(menus);
        return SUCCESS;
    }

    /**
     * 菜单数据的保存方法
     *
     * @return
     */
    @Action(value = "menu_save", results = {@Result(name = "success", type = "redirect", location = "pages/system/menu.html")})
    public String save() {
        //调用业务层保存数据
        menuService.save(model);
        return SUCCESS;
    }

    /**
     * 展示菜单的方法
     *
     * @return
     */
    @Action(value = "menu_showMenu", results = {@Result(name = "success", type = "json")})
    public String showMenu() {
        //调用业务层,查找当前用户所具有的菜单列表
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        //查询菜单列表
        List<Menu> menus = menuService.findByUser(user);
        //经查询结果返回到值栈
        ActionContext.getContext().getValueStack().push(menus);
        return SUCCESS;
    }
}
