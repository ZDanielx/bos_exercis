package cn.itcast.bos.web.action.base;

import cn.itcast.bos.domain.base.Area;
import cn.itcast.bos.service.base.AreaService;
import cn.itcast.bos.utils.PinYin4jUtils;
import cn.itcast.bos.web.action.base.common.BaseAction;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
     * 批量数据导入
     *
     * @return
     * @throws IOException
     */
    @Action(value = "area_batchImport")
    public String batchImport() throws IOException {
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
}
