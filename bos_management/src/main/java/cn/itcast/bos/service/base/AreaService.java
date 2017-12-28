package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Created by Ricky on 2017/12/27
 */

public interface AreaService {
    /**
     * 批量数据导入
     * @param areas
     */
    public void saveBatch(List<Area> areas);

    /**
     * 带条件的分页查询
     * @param specification
     * @param pageable
     * @return
     */
    public Page<Area> findPageData(Specification<Area> specification, Pageable pageable);

    /**
     * 查找所有区域的方法
     */
    public List<Area> findAll();

    public void save(Area model);
}
