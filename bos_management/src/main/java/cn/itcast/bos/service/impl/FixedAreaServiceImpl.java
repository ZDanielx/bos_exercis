package cn.itcast.bos.service.impl;

import cn.itcast.bos.dao.base.CourierRepsitory;
import cn.itcast.bos.dao.base.FixedAreaRepsitory;
import cn.itcast.bos.dao.base.TakeTimeRepsitory;
import cn.itcast.bos.domain.base.Courier;
import cn.itcast.bos.domain.base.FixedArea;
import cn.itcast.bos.domain.base.TakeTime;
import cn.itcast.bos.service.base.FixedAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Ricky on 2017/12/28
 */
@Service
@Transactional
public class FixedAreaServiceImpl implements FixedAreaService {

    //注入Dao
    @Autowired
    private FixedAreaRepsitory fixedAreaRepsitory;

    @Override
    public void save(FixedArea model) {
        fixedAreaRepsitory.save(model);
    }

    @Override
    public Page<FixedArea> findPageData(Specification<FixedArea> specification, Pageable pageable) {
        return fixedAreaRepsitory.findAll(specification, pageable);
    }

    @Override
    public List<FixedArea> findAll() {
        return fixedAreaRepsitory.findAll();
    }

    //注入dao层
    @Autowired
    private CourierRepsitory courierRepsitory;
    @Autowired
    private TakeTimeRepsitory takeTimeRepsitory;

    @Override
    public void associationCourierToFixedArea(FixedArea model, Integer courierId, Integer takeTimeId) {
        FixedArea fixedArea = fixedAreaRepsitory.findOne(model.getId());

        Courier courier = courierRepsitory.findOne(courierId);

        TakeTime takeTime = takeTimeRepsitory.findOne(takeTimeId);
        //将收派标准关联到快递员上
        courier.setTakeTime(takeTime);
        //快递员关联到定区
        fixedArea.getCouriers().add(courier);

        fixedAreaRepsitory.save(fixedArea);
    }
}
