package cn.itcast.bos.web.action.base;

import cn.itcast.bos.service.base.CourierService;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.Standard;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private String ids;
    private String flag;

    public void setIds(String ids) {
        this.ids = ids;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    //注入service
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
    @Action(value = "courier_pageQuery", results = {@Result(name = "success", type = "json")})
    public String pageQuery() {
        //调用业务层查询数据结果
        Pageable pageable = new PageRequest(page - 1, rows);
        //封装条件查询对象 specification
        Specification<Courier> specification = new Specification<Courier>() {
            @Override
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();

                //简单表的查询
                if (StringUtils.isNotBlank(courier.getCourierNum())) {
                    Predicate p1 = cb.equal(
                            root.get("courierNum").as(String.class),
                            courier.getCourierNum());
                    list.add(p1);
                }

                if (StringUtils.isNotBlank(courier.getCompany())) {
                    Predicate p2 = cb.like(
                            root.get("company").as(String.class),
                            "%" + courier.getCompany() + "%");
                    list.add(p2);
                }

                if (StringUtils.isNotBlank(courier.getType())) {
                    Predicate p3 = cb.like(
                            root.get("type").as(String.class),
                            "%" + courier.getType() + "%");
                    list.add(p3);
                }

                //多表查询
                Join<Courier, Standard> standardJoin = root.join("standard",
                        JoinType.INNER);
                if (courier.getStandard() != null
                        && StringUtils.isNotBlank(courier.getStandard().getName())) {
                    Predicate p4 = cb.like(
                            standardJoin.get("name").as(String.class),
                            "%" + courier.getStandard().getName() + "%");
                    list.add(p4);
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };

        //调用业务层返回page
        Page<Courier> page = courierService.findPageData(specification, pageable);
        //返回客户端的数据 total和rows
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", page.getTotalElements());
        map.put("rows", page.getContent());

        //将map转换为json数据显示到页面
        ActionContext.getContext().getValueStack().push(map);
        return SUCCESS;
    }

    /**
     * 快递员作废和还原的方法
     */
    @Action(value = "courier_delBatch", results = {@Result(name = "success", location = "./pages/base/courier.html", type = "redirect")})
    public String courierBatch() {
        char[] temp = flag.toCharArray();
        Character deltag = temp[0];
        //按,分割ids
        String[] idArray = ids.split(",");
        //判断需求是需要作废还是还原
        if (deltag == '1') {
            //调用业务层批量恢复
            courierService.Batch(deltag, idArray);
        } else if (deltag == '0') {
            //调用业务层,批量作废
            courierService.Batch(deltag, idArray);
        }
        return SUCCESS;
    }

    /**
     * 查找未关联定区的快递员
     * @return
     */
    @Action(value = "courier_findnoassociation",results = {@Result(name = "success",type = "json")})
    public String findnoassociation(){
        //调用业务层查找未关联的快递员
        List<Courier> couriers = courierService.findNoAssociation();
        //将查询结果压入值栈
        ActionContext.getContext().getValueStack().push(couriers);
        return SUCCESS;
    }

}
