package cn.itcast.bos.service.impl;

import cn.itcast.bos.dao.base.TakeTimeRepsitory;

import cn.itcast.bos.service.base.TakeTimeService;
import cn.itcast.bos.domain.base.TakeTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Ricky on 2017/12/30
 */
@Service
@Transactional
public class TakeTimeServiceImpl implements TakeTimeService {

    //注入Dao
    @Autowired
    private TakeTimeRepsitory takeTimeRepsitory;

    @Override
    public List<TakeTime> findAll() {
        //调用dao层进行查找
        return takeTimeRepsitory.findAll();
    }
}
