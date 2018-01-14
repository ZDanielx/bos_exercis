package cn.itcast.bos.service.impl;

import cn.itcast.bos.dao.base.WayBillRepsitory;
import cn.itcast.bos.domain.take_delivery.WayBill;
import cn.itcast.bos.index.WayBillIndexRepository;
import cn.itcast.bos.service.base.WayBillService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Ricky on 2018/1/11
 */
@Service
@Transactional
public class WayBillSeviceImpl implements WayBillService {
    @Autowired
    private WayBillRepsitory wayBillRepsitory;
    @Autowired
    private WayBillIndexRepository wayBillIndexRepository;

    @Override
    public void save(WayBill model) {
        WayBill wayBill = wayBillRepsitory.findByWayBillNum(model.getWayBillNum());
        if (wayBill == null || wayBill.getId() == null) {
            //运单号不存在,走添加
            wayBillRepsitory.save(model);
            //保存索引
            wayBillIndexRepository.save(model);
        } else {
            //运单号存在,走修改
            try {
                Integer id = wayBill.getId();
                BeanUtils.copyProperties(wayBill, model);
                wayBill.setId(id);
                wayBillIndexRepository.save(wayBill);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    @Override
    public Page<WayBill> pageQuery(WayBill wayBill, Pageable pageable) {
        //判断waybill中的条件是否存在'
        if (StringUtils.isBlank(wayBill.getWayBillNum()) && StringUtils.isBlank(wayBill.getSendAddress()) && StringUtils.isBlank(wayBill.getRecAddress()) && StringUtils.isBlank(wayBill.getSendProNum()) && (wayBill.getSignStatus() == null || wayBill.getSignStatus() == 0)) {
            //无条件查询,直接查询数据库
            return wayBillRepsitory.findAll(pageable);
        } else {
            //有条件查询
            //must 条件必须成立 and
            //should条件可以成立or
            BoolQueryBuilder query = new BoolQueryBuilder(); //布尔查询,多条件组合查询
            //向组合对象添加条件
            if (StringUtils.isNotBlank(wayBill.getWayBillNum())) {
                //运单号查询
                TermQueryBuilder wayBillNum = new TermQueryBuilder("wayBillNum", wayBill.getWayBillNum());
                query.must(wayBillNum);
            }
            if (StringUtils.isNotBlank(wayBill.getSendAddress())) {
                //发货地 模糊查询
                //情况一:输入"北" 是查询词条的一部分,使用模糊匹配词条查询
                WildcardQueryBuilder wildcardQueryBuilder = new WildcardQueryBuilder("sendAddress", "*" + wayBill.getSendAddress() + "*");

                //情况二:输入"北京市海淀区"是多个词条组合,进行分词后每个词条匹配查询
                QueryStringQueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(wayBill.getSendAddress()).field("sendAddress").defaultOperator(QueryStringQueryBuilder.Operator.AND);

                //两种情况取or关系
                BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
                boolQueryBuilder.should(wildcardQueryBuilder);
                boolQueryBuilder.should(queryStringQueryBuilder);

                query.must(boolQueryBuilder);
            }
            if (StringUtils.isNoneBlank(wayBill.getRecAddress())) {
                //收货地模糊查询
                WildcardQueryBuilder wildcardQueryBuilder = new WildcardQueryBuilder("recAddress", "*" + wayBill.getRecAddress() + "*");

                //词条查询
                QueryStringQueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(wayBill.getRecAddress()).field("recAddress").defaultOperator(QueryStringQueryBuilder.Operator.AND);
                //两种关系取or
                BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
                boolQueryBuilder.should(wildcardQueryBuilder);
                boolQueryBuilder.should(queryStringQueryBuilder);
                query.must(boolQueryBuilder);
            }
            if (StringUtils.isNoneBlank(wayBill.getSendProNum())) {
                //速运类型等值查询
                TermQueryBuilder sendProNum = new TermQueryBuilder("sendProNum", wayBill.getSendProNum());
                query.must(sendProNum);
            }
            if (wayBill.getSignStatus() != null && wayBill.getSignStatus() != 0) {
                //签收状态等值查询
                TermQueryBuilder signStatus = new TermQueryBuilder("signStatus", new WayBill().getSignStatus());
                query.must(signStatus);
            }
            SearchQuery searchQuery = new NativeSearchQuery(query);
            searchQuery.setPageable(pageable); //实现分页效果
            //有条件查询索引库
            return wayBillIndexRepository.search(searchQuery);
        }

    }

    @Override
    public WayBill findByWayBillNum(String wayBillNum) {
        return wayBillRepsitory.findByWayBillNum(wayBillNum);
    }

    @Override
    public List<WayBill> findAll() {
        return wayBillRepsitory.findAll();
    }

}
