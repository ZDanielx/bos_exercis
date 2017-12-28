package cn.itcast.bos.service.base.impl;

import cn.itcast.bos.dao.base.SubAreaRepsitory;
import cn.itcast.bos.domain.base.SubArea;
import cn.itcast.bos.service.base.SubAreaService;
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
public class SubAreaServiceImpl implements SubAreaService {

    //注入Dao
    @Autowired
    private SubAreaRepsitory subAreaRepsitory;

    @Override
    public void save(SubArea model) {
        subAreaRepsitory.save(model);
    }

    @Override
    public List<SubArea> findAll() {
        return subAreaRepsitory.findAll();
    }

    @Override
    public Page<SubArea> pageQuery(Specification<SubArea> specification, Pageable pageable) {
        return subAreaRepsitory.findAll(specification, pageable);
    }
}
