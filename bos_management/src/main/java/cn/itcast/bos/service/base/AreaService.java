package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.Area;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

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
}
