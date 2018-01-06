package cn.itcast.bos.dao.base;


import cn.itcast.bos.domain.base.TakeTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by Ricky on 2017/12/30
 */
public interface TakeTimeRepsitory extends JpaSpecificationExecutor<TakeTime>,JpaRepository<TakeTime,Integer>{

}