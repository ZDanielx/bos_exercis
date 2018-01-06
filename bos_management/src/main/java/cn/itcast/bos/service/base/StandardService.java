package cn.itcast.bos.service.base;


import cn.itcast.bos.domain.base.Standard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * Created by Ricky on 2017/12/23
 */
public interface StandardService {
    public void save(Standard standard);


    public Page<Standard> findPageData(Pageable pageable);

    public List<Standard> findAll();
}
