package cn.itcast.bos.quartz;

import cn.itcast.bos.dao.base.PromotionRepsitory;
import cn.itcast.bos.service.base.PromotionService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by Ricky on 2018/1/9
 *
 * 定时设置宣传任务的状态
 */
public class PromotionJob implements Job {

    //注入promotion的Dao层
    @Autowired
    private PromotionService promotionService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

        //每分钟执行一次,当前时间大于promotion的endDate活动已经过期,设置status等于2
        promotionService.updateStatus(new Date());
        System.out.println("定时清理程序启动了.......");
    }
}
