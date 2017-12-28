package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.utils.PinYin4jUtils;
import cn.itcast.bos.web.action.base.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ricky on 2017/12/27
 */
@Controller
@Scope("prototype")
@ParentPackage("json-default")
@Namespace("/")
public class AreaAction extends BaseAction<Area> {

    //注入业务层对象
    @Autowired
    private AreaService areaService;

    //接受上传文件
    private File file;
    private String fileFileName;

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    public void setFile(File file) {
        this.file = file;
    }

    /**
     * 添加区域的方法
     *
     * @return
     */
    @Action(value = "area_save", results = {@Result(name = "success", location = "./pages/base/area.html", type = "redirect")})
    public String addArea() {
        areaService.save(model);
        return SUCCESS;
    }


    /**
     * 批量数据导入
     *
     * @return
     * @throws IOException
     */
    @Action(value = "area_batchImport")
    public String batchImport() {
        String msg = "";
        try {
            List<Area> areas = new ArrayList<Area>();
            //编写解析代码逻辑
            //判断是.xls文件还是.xlsx文件
            Workbook workbook = null;
            //1,加载excle文件对象
            if (fileFileName.endsWith("xls")) {
                //基于.xls格式解析HSSF
                workbook = new HSSFWorkbook(new FileInputStream(file));
            } else if (fileFileName.endsWith("xlsx")) {
                //基于.xlsx文件格式解析XSSF
                workbook = new XSSFWorkbook(new FileInputStream(file));
            }

            //2.读取一个sheet
            Sheet sheetAt = workbook.getSheetAt(0);
            //3.读取sheet中每一行的数据
            for (Row row : sheetAt) {
                //一行数据对应一个区域
                if (row.getRowNum() == 0) {
                    //第一行表头,跳过
                    continue;
                }
                //跳过空行
                if (row.getCell(0) == null || StringUtils.isBlank(row.getCell(0).getStringCellValue())) {
                    continue;
                }
                Area area = new Area();
                area.setId(row.getCell(0).getStringCellValue());
                area.setProvince(row.getCell(1).getStringCellValue());
                area.setCity(row.getCell(2).getStringCellValue());
                area.setDistrict(row.getCell(3).getStringCellValue());
                area.setPostcode(row.getCell(4).getStringCellValue());
                //基于pinyin4j生成城市编码和简码
                String province = area.getProvince();
                String city = area.getCity();
                String district = area.getDistrict();
                province = province.substring(0, province.length() - 1);
                city = city.substring(0, city.length() - 1);
                district = district.substring(0, district.length() - 1);

                //简码
                String[] headArray = PinYin4jUtils.getHeadByString(province + city + district);
                StringBuffer buffer = new StringBuffer();
                for (String headStr : headArray) {
                    buffer.append(headStr);
                }
                String shortcode = buffer.toString();
                area.setShortcode(shortcode);
                //城市编码
                String citycode = PinYin4jUtils.hanziToPinyin(city, "");
                area.setCitycode(citycode);

                areas.add(area);
            }
            //调用业务层
            areaService.saveBatch(areas);

            msg = "上传成功!";
        } catch (Exception e) {
            msg = "上传失败!";
            e.printStackTrace();
        }
        ActionContext.getContext().getValueStack().push(msg);
        return NONE;
    }

    /**
     * 带条件的分页查寻
     *
     * @return
     */
    @Action(value = "area_pageQuery", results = {@Result(name = "success", type = "json")})
    public String pageQuery() {
        //构造分页查询对象
        Pageable pageable = new PageRequest(page - 1, rows);
        //构造条件查询对象
        Specification<Area> specification = new Specification<Area>() {
            @Override
            public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

                List<Predicate> list = new ArrayList<Predicate>();
                if (StringUtils.isNotBlank(model.getProvince())) {
                    Predicate p1 = cb.like(root.get("province").as(String.class), "%" + model.getProvince() + "%");
                    list.add(p1);
                }
                if (StringUtils.isNotBlank(model.getCity())) {
                    Predicate p2 = cb.like(root.get("city").as(String.class), "%" + model.getCity() + "%");
                    list.add(p2);
                }
                if (StringUtils.isNotBlank(model.getDistrict())) {
                    Predicate p3 = cb.like(root.get("district").as(String.class), "%" + model.getDistrict() + "%");
                    list.add(p3);
                }

                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        //调用业务层来完成查询
        Page<Area> page = areaService.findPageData(specification, pageable);
        //压入值栈
        pushPageDataToValueStack(page);
        return SUCCESS;
    }

    /**
     * 查找所有区域的方法
     *
     * @return
     */
    @Action(value = "findAll_area",results = {@Result(name = "success",type = "json")})
    public String findAllArea() {
        List<Area> areas = areaService.findAll();
        ServletActionContext.getContext().getValueStack().push(areas);
        return SUCCESS;
    }

    /**
     * 导出数据的方法
     *
     * @return 文件的二进制数据
     */
    @Action(value = "area_export")
    public String areaExport() {
        //1.查询所有数据
        List<Area> areas = areaService.findAll();
        //List<Area> areas = new ArrayList<Area>();
        //2.将数据写入Excel文件中
        //创建workbook对象
        Workbook workbook = new HSSFWorkbook();
        //规定50000条数据一个sheet
        int size = areas.size();
        int pageSize = 50000;
        //总的sheet数
        int count = (size % pageSize) == 0 ? size / pageSize : (size / pageSize + 1);
        for (int i = 0; i < count; i++) {
            //2.2创建sheet
            Sheet sheet = workbook.createSheet("区域数据" + i);
            //2.3创建row 表头行'
            Row row = sheet.createRow(0);
            row.createCell(0).setCellValue("区域编号");
            row.createCell(1).setCellValue("省份");
            row.createCell(2).setCellValue("城市");
            row.createCell(3).setCellValue("区域");
            row.createCell(4).setCellValue("邮编");

            //2.4遍历集合,创
            // 建表数据
            for (int j = i * pageSize; j < (i + 1) * pageSize; j++) {
                if (j >= size) {
                    break;
                }
                Row row1 = sheet.createRow(sheet.getLastRowNum() + 1);
                row1.createCell(0).setCellValue(areas.get(j).getId());
                row1.createCell(1).setCellValue(areas.get(j).getProvince());
                row1.createCell(2).setCellValue(areas.get(j).getCity());
                row1.createCell(3).setCellValue(areas.get(j).getDistrict());
                row1.createCell(4).setCellValue(areas.get(j).getPostcode());
            }
        }
        try {
            //3将Excel文件写到客户端,提供文件下载的操作
            String fileName = "qwr.xls";
            ServletOutputStream outputStream = ServletActionContext.getResponse().getOutputStream();
            //一个流,两个头 content-type
            ServletActionContext.getResponse().setContentType(ServletActionContext.getServletContext().getMimeType(fileName));
            //content-disposition
            ServletActionContext.getResponse().setHeader("content-disposition", "attachment;fileName=" + fileName);
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return NONE;
    }
}
