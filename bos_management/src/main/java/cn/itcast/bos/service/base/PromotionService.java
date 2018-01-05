package cn.itcast.bos.service.base;

import cn.itcast.bos.domain.take_delivery.Promotion;
import org.springframework.stereotype.Service;

/**
 * Created by Ricky on 2018/1/5
 */

public interface PromotionService {
    /**
     * 保存活动任务数据的方法
     * @param model
     */
    void save(Promotion model);
}
