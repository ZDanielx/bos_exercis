package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.base.Courier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable; /**
 * Created by Ricky on 2017/12/25
 */
public interface CourierService {
    public void save(Courier courier);

    public Page<Courier> findPageData(Pageable pageable);
}
