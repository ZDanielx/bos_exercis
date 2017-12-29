package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.base.SubAreaService;
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
public class SubAreaAction extends BaseAction<SubArea> {

    //注入service
    @Autowired
    private SubAreaService subAreaService;

    /**
     * 添加分区的方法
     *
     * @return
     */
    @Action(value = "sub_area_save", results = {@Result(name = "success", location = "./pages/base/sub_area.html", type = "redirect")})
    public String addSubArea() {
        subAreaService.save(model);
        return SUCCESS;
    }

    /**
     * 查找所有分区的方法
     */
   /* @Action(value = "", results = {@Result(name = "success", type = "json")})
    public String findAllSubArea() {
        List<SubArea> subAreas = subAreaService.findAll();
        ServletActionContext.getContext().getValueStack().push(subAreas);
        return SUCCESS;
    }*/

    /**
     * 带条件分页的方法
     *
     * @return
     */
    @Action(value = "subArea_pageQuery",results = {@Result(name = "success",type = "json")})
    public String pageQuery() {
        Pageable pageable = new PageRequest(page - 1, rows);
        Specification<SubArea> specification = new Specification<SubArea>() {
            @Override
            public Predicate toPredicate(Root<SubArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<Predicate>();

                //多表查询
                Join<SubArea, Area> areaJoin = root.join("area", JoinType.INNER);
                if (model.getArea() != null && StringUtils.isNotBlank(model.getArea().getProvince())) {
                    Predicate province = cb.like(areaJoin.get("province").as(String.class),
                            "%" + model.getArea().getProvince() + "%");
                    list.add(province);
                }
                if (model.getArea() != null && StringUtils.isNotBlank(model.getArea().getCity())) {
                    Predicate city = cb.like(areaJoin.get("city").as(String.class),
                            "%" + model.getArea().getCity() + "%");
                    list.add(city);
                }
                if (model.getArea() != null && StringUtils.isNotBlank(model.getArea().getDistrict())) {
                    Predicate district = cb.like(areaJoin.get("district").as(String.class),
                            "%" + model.getArea().getDistrict() + "%");
                    list.add(district);
                }
                Join<SubArea, FixedArea> fixedAreaJoin = root.join("fixedArea", JoinType.INNER);
                if (model.getFixedArea() != null && StringUtils.isNotBlank(model.getFixedArea().getId())) {
                    Predicate id = cb.like(fixedAreaJoin.get("id").as(String.class),
                            "%" + model.getFixedArea().getId() + "%");
                    list.add(id);
                }
                //单表查询
                if (StringUtils.isNotBlank(model.getKeyWords())) {
                    Predicate keyWords = cb.like(root.get("keyWords").as(String.class),
                            "%" + model.getKeyWords() + "%");
                    list.add(keyWords);
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        //调用业务层进行查找
        Page<SubArea> page = subAreaService.pageQuery(specification, pageable);
        //将结果压入值栈
        pushPageDataToValueStack(page);
        return SUCCESS;
    }
}
