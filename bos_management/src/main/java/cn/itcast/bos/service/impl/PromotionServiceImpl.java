package cn.itcast.bos.service.impl;

import cn.itcast.bos.dao.base.PromotionRepsitory;
import cn.itcast.bos.domain.take_delivery.Promotion;
import cn.itcast.bos.service.base.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Ricky on 2018/1/5
 */
@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {

    @Autowired
    private PromotionRepsitory promotionRepsitory;

    @Override
    public void save(Promotion model) {
        promotionRepsitory.save(model);
    }
}
