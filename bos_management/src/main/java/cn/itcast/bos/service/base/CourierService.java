package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.Courier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification; /**
 * Created by Ricky on 2017/12/25
 */
public interface CourierService {
    public void save(Courier courier);

    Page<Courier> findPageData(Specification<Courier> specification, Pageable pageable);

    public void Batch(Character deltag, String[] idArray);
}
