package cn.itcast.bos.web.action.take_delivery;


import cn.itcast.bos.service.base.PromotionService;
import cn.itcast.bos.web.action.common.BaseAction;
import cn.itcast.bos.domain.take_delivery.Promotion;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;


import java.io.File;
import java.io.IOException;

import java.util.UUID;

/**
 * Created by Ricky on 2018/1/5
 * 处理kindeditor图片上传,管理功能
 */
@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("json-default")
public class PromotionAction extends BaseAction<Promotion> {

    private File titleImgFile;
    private String titleImgFileFileName;

    public void setTitleImgFile(File titleImgFile) {
        this.titleImgFile = titleImgFile;
    }

    public void setTitleImgFileFileName(String titleImgFileFileName) {
        this.titleImgFileFileName = titleImgFileFileName;
    }

    @Autowired
    private PromotionService promotionService;

    @Action(value = "promotion_save", results = {@Result(name = "success", type = "redirect", location = "./pages/take_delivery/promotion.html")})
    public String save() throws IOException {
        //宣传图片 上传在数据表中保存 讯传图路径
        String savePath = ServletActionContext.getServletContext().getRealPath(
                "/upload/");
        String saveUrl = ServletActionContext.getRequest().getContextPath()
                + "/upload/";
        //生成随机图片名
        UUID uuid = UUID.randomUUID();
        String ext = titleImgFileFileName.substring(titleImgFileFileName
                .lastIndexOf("."));
        String randomFileName = uuid + ext;
        //保存图片绝对路径
        File destFile = new File(savePath + "/" + randomFileName);
        FileUtils.copyFile(titleImgFile, destFile);

        //将保存路径 相对工程web访问路径保存到model中
        model.setTitleImg(ServletActionContext.getRequest().getContextPath() + "/upload/" + randomFileName);

        //调用业务层保存活动任务数据
        promotionService.save(model);
        return SUCCESS;
    }

    /**
     * 宣传任务的后台分页功能
     *
     * @return
     */
    @Action(value = "promotion_pageQuery",results = {@Result(name = "success",type = "json")})
    public String pageQuery() {
        //接收前台传送过来的 page 和rows
        Pageable pageable = new PageRequest(page - 1, rows);
        //调用业务层进行分页查找
        Page<Promotion> promotions = promotionService.findPageData(pageable);
        //将查询结果压入值栈
        pushPageDataToValueStack(promotions);
        return SUCCESS;
    }
}
