package cn.itcast.bos.index;

import cn.itcast.bos.domain.take_delivery.WayBill;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Created by Ricky on 2018/1/13
 */
public interface WayBillIndexRepository extends ElasticsearchRepository<WayBill,Integer>{
}
