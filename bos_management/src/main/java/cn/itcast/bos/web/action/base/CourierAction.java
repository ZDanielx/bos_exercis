package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;
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
import java.util.Map;

/**
 * Created by Ricky on 2017/12/25
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class CourierAction extends ActionSupport implements ModelDriven<Courier> {

    private Courier courier = new Courier();

    @Override
    public Courier getModel() {
        return courier;
    }

    private Integer page;
    private Integer rows;

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    @Autowired
    private CourierService courierService;


    /**
     * 添加快递员的方法
     */
    @Action(value = "courier_save", results = {@Result(name = "success", type = "redirect", location = "./pages/base/courier.html")})
    public String courierSave() {
        courierService.save(courier);
        return SUCCESS;
    }

    /**
     * 分页查询的方法
     */
    @Action(value = "courier_pageQuery",results = {@Result(name = "success",type = "json")})
    public String pageQuery(){
        //调用业务层查询数据结果
        Pageable pageable = new PageRequest(page-1,rows);
        Page<Courier> page = courierService.findPageData(pageable);

        //返回客户端的数据 total和rows
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total",page.getNumberOfElements());
        map.put("rows",page.getContent());

        //将map转换为json数据显示到页面
        ActionContext.getContext().getValueStack().push(map);
        return SUCCESS;
    }
}
