package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.CourierRepsitory;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.service.base.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Ricky on 2017/12/25
 */
@Service
@Transactional
public class CourierServiceImpl implements CourierService{

    @Autowired
    private CourierRepsitory courierRepsitory;

    /**
     * 添加快递员的方法
     * @param courier
     */
    @Override
    public void save(Courier courier) {
        courierRepsitory.save(courier);
    }

    /**
     * 无条件分页的查询方法
     * @param pageable
     * @return
     */
    @Override
    public Page<Courier> findPageData(Pageable pageable) {
        return courierRepsitory.findAll(pageable);
    }
}
