package cn.itcast.bos.service.base;


import cn.itcast.bos.domain.base.SubArea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Created by Ricky on 2017/12/28
 */
public interface SubAreaService {
    /**
     * 添加分区的方法
     * @param model
     */
    public void save(SubArea model);

    /**
     * 查找所有分区的方法
     * @return
     */
    public List<SubArea> findAll();

    /**
     * 带条件分页查找
     * @param specification
     * @param pageable
     * @return
     */

   public Page<SubArea> pageQuery(Specification<SubArea> specification, Pageable pageable);
}
