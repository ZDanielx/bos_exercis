package cn.itcast.bos.service.impl;

import cn.itcast.bos.dao.base.CourierRepsitory;
import cn.itcast.bos.service.base.CourierService;
import cn.itcast.bos.domain.base.Courier;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

/**
 * Created by Ricky on 2017/12/25
 */
@Service
@Transactional
public class CourierServiceImpl implements CourierService {
    //注入CourierRepsitory对象
    @Autowired
    private CourierRepsitory courierRepsitory;

    /**
     * 添加快递员的方法
     *
     * @param courier
     */
    @Override
    @RequiresPermissions("courier:add")
    public void save(Courier courier) {
        courierRepsitory.save(courier);
    }

    /**
     * 条件分页查询
     *
     * @param specification
     * @param pageable
     * @return
     */
    @Override
    public Page<Courier> findPageData(Specification<Courier> specification, Pageable pageable) {
        return courierRepsitory.findAll(specification, pageable);
    }

    /**
     * 批量作废或恢复的方法
     *
     * @param idArray
     * @param deltag
     */
    @Override
    public void Batch(Character deltag, String[] idArray) {
        //调用Dao实现update操作,将deltage修改为flag
        for (String ids : idArray) {
            Integer id = Integer.parseInt(ids);
            courierRepsitory.Batch(deltag,id);
        }

    }

    /**
     * 查找未添加分区的快递员信息
     * @return
     */
    @Override
    public List<Courier> findNoAssociation() {
        //封装specification
        Specification<Courier> courierSpecification = new Specification<Courier>() {
            @Override
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //查询条件,判定列表size是否为空
                Predicate fixedAreas = cb.isEmpty(root.get("fixedAreas").as(Set.class));
                return fixedAreas;
            }
        };
        return courierRepsitory.findAll(courierSpecification);
    }


}
