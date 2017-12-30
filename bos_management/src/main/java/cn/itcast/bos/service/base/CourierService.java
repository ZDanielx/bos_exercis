package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.Courier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * Created by Ricky on 2017/12/25
 */
public interface CourierService {
    /**
     * 添加快递员的方法
     * @param courier
     */
    public void save(Courier courier);

    /**
     * 带条件分页查询快递员的方法
     * @param specification
     * @param pageable
     * @return
     */
    Page<Courier> findPageData(Specification<Courier> specification, Pageable pageable);

    /**
     * 作废或者还原快递员的方法
     * @param deltag
     * @param idArray
     */
    public void Batch(Character deltag, String[] idArray);

    /**
     * 查询未关联定区的快递员
     * @return
     */
    List<Courier> findNoAssociation();
}
