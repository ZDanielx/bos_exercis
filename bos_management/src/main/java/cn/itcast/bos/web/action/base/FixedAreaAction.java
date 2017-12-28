package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.base.FixedAreaService;
import cn.itcast.bos.web.action.base.common.BaseAction;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
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
import java.util.List;

/**
 * Created by Ricky on 2017/12/28
 */
@Controller
@Scope("prototype")
@ParentPackage("json-default")
@Namespace("/")
public class FixedAreaAction extends BaseAction<FixedArea> {

    //注入service
    @Autowired
    private FixedAreaService fixedAreaService;

    /**
     * 添加分区的方法
     *
     * @return
     */
    @Action(value = "fixed_areaSave", results = {@Result(name = "success", location = "./pages/base/fixed_area.html", type = "redirect")})
    public String fixedAreaSave() {
        fixedAreaService.save(model);
        return SUCCESS;
    }

    /**
     * 带条件的分页查询
     * @return
     */
    @Action(value = "fixedArea_pageQuery", results = {@Result(name = "success", type = "json")})
    public String pageQuery() {
        //1.构造分页查询对象
        Pageable pageable = new PageRequest(page - 1, rows);
        //2.构造条件查询对象
        Specification<FixedArea> specification = new Specification<FixedArea>() {
            @Override
            public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();
                //单表查询
                if (StringUtils.isNotBlank(model.getId())) {
                    Predicate id = cb.like(root.get("id").as(String.class),
                            "%" + model.getId() + "%");
                    list.add(id);
                }

                if (StringUtils.isNotBlank(model.getCompany())) {
                    Predicate company = cb.like(root.get("company").as(String.class),
                            "%" + model.getCompany() + "%");
                    list.add(company);
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        //调用业务层完成查询
        Page<FixedArea> page = fixedAreaService.findPageData(specification,pageable);
        //压入值栈
        pushPageDataToValueStack(page);
        return SUCCESS;
    }

    /**
     * 查找所有分区的方法
     * @return
     */
    @Action(value = "findAll_fixed",results = {@Result(name = "success",type = "json")})
    public String findAllFixed(){
        List<FixedArea> fixedAreas = fixedAreaService.findAll();
        ServletActionContext.getContext().getValueStack().push(fixedAreas);
        return SUCCESS;
    }

}
