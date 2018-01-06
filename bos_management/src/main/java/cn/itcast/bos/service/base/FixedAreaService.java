package cn.itcast.bos.service.base;


import cn.itcast.bos.domain.base.FixedArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Created by Ricky on 2017/12/28
 */
public interface FixedAreaService {

    /**
     * 添加定区的方法
     * @param model
     */
   public void save(FixedArea model);

    /**
     * 带条件分页查询定区的方法
     * @param specification
     * @param pageable
     * @return
     */
   public Page<FixedArea> findPageData(Specification<FixedArea> specification, Pageable pageable);

    /**
     * 查询所有定区的方法
     * @return
     */
    public List<FixedArea> findAll();

    /**
     * 快递员关联定区的方法
     * @param model
     * @param courierId
     * @param takeTimeId
     */
    void associationCourierToFixedArea(FixedArea model, Integer courierId, Integer takeTimeId);
}
