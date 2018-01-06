package cn.itcast.bos.web.action.base;


import cn.itcast.bos.service.base.StandardService;
import cn.itcast.bos.domain.base.Standard;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Ricky on 2017/12/23
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class StandardAction extends ActionSupport implements ModelDriven<Standard> {

    private Standard standard = new Standard();

    @Autowired
    private StandardService standardService;

    @Override
    public Standard getModel() {
        return standard;
    }

    private Integer page;//页码
    private Integer rows;//每页的记录数

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    /**
     * 添加的方法
     */
    @Action(value = "standard_save", results = {@Result(name = "success", location = "./pages/base/standard.html", type = "redirect")})
    public String save() {
        standardService.save(standard);
        return SUCCESS;
    }

    /**
     * 分页查询的方法
     */
    @Action(value = "standard_pageQuery",results = {@Result(name = "success",type = "json")})
    public String pageQuery() {
        //调用业务层查询数据结果
        Pageable pageable = new PageRequest(page-1, rows);
        Page<Standard> page = standardService.findPageData(pageable);

        //返回客户端的数据 total和rows
        Map<String, Object> restul = new HashMap<String, Object>();
        restul.put("total", page.getNumberOfElements());
        restul.put("rows", page.getContent());
        //将map转换为json数据返回,使用Struts2-json-plugin插件
        ActionContext.getContext().getValueStack().push(restul);
        return SUCCESS;
    }

    /**
     * 查找所有的方法
     */
    @Action(value = "standard_findAll",results = {@Result(name = "success",type = "json")})
    public String standardFindAll(){
        //查找到所有信息
        List<Standard> standards = standardService.findAll();
        //将list数据转换为json数据返回
        ActionContext.getContext().getValueStack().push(standards);
        return SUCCESS;
    }

}
