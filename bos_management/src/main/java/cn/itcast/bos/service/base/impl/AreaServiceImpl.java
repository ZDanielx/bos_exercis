package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.AreaRepsitory;
import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Ricky on 2017/12/27
 */
@Service
@Transactional
public class AreaServiceImpl implements AreaService {

    //注入DAO
    @Autowired
    private AreaRepsitory areaRepsitory;

    @Override
    public void saveBatch(List<Area> areas) {
        areaRepsitory.save(areas);
    }

    @Override
    public Page<Area> findPageData(Specification<Area> specification, Pageable pageable) {
        return areaRepsitory.findAll(specification, pageable);
    }

    @Override
    public List<Area> findAll() {

        return areaRepsitory.findAll();
    }

    @Override
    public void save(Area model) {
        areaRepsitory.save(model);
    }
}
