package cn.itcast.bos.service.impl;

import cn.itcast.bos.dao.base.PromotionRepsitory;

import cn.itcast.bos.domain.page.PageBean;
import cn.itcast.bos.service.base.PromotionService;
import cn.itcast.bos.domain.take_delivery.Promotion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Created by Ricky on 2018/1/5
 */
@Service
@Transactional
public class PromotionServiceImpl implements PromotionService {
    //注入Dao
    @Autowired
    private PromotionRepsitory promotionRepsitory;

    @Override
    public void save(Promotion model) {
        promotionRepsitory.save(model);
    }

    @Override
    public Page<Promotion> findPageData(Pageable pageable) {
        return promotionRepsitory.findAll(pageable);
    }

    @Override
    public PageBean<Promotion> findpageData(int page, int rows) {
        //构造pageable对象
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<Promotion> pageData = promotionRepsitory.findAll(pageable);
        //封装到pageBean
        PageBean<Promotion> pageBean = new PageBean<Promotion>();
        pageBean.setPageData(pageData.getContent());
        pageBean.setTotalCount(pageData.getTotalElements());
        return pageBean;
    }


}
