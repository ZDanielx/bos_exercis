package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.CourierRepsitory;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


}
