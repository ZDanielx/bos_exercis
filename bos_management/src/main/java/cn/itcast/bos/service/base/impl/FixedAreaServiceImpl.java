package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.FixedAreaRepsitory;
import cn.itcast.bos.domain.base.FixedArea;
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
}
