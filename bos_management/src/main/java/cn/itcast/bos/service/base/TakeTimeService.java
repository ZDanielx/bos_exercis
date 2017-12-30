package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.TakeTime;

import java.util.List;

/**
 * Created by Ricky on 2017/12/30
 */
public interface TakeTimeService {
    /**
     * 查询所有收派时间
     * @return
     */
    List<TakeTime> findAll();

}
