package cn.itcast.bos.web.action.base.common;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.springframework.data.domain.Page;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 抽取Action公共的代码来实现简化开发
 * Created by Ricky on 2017/12/27
 */
public abstract class BaseAction<T> extends ActionSupport implements ModelDriven<T> {

    //模型驱动
    protected T model;

    @Override
    public T getModel() {
        return model;
    }

    //构造器,完成model的实例化
    public BaseAction() {
        //构造子类Action对象,获取继承父类的泛型
        //AreaAction extends BaseAction<Area>
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        //获取类型第一个泛型参数
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Class<T> modelClass = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        try {
            model = modelClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            System.out.println("模型构造失败");
        }
    }

    //接收分页查询参数
    protected Integer page;
    protected Integer rows;

    public void setPage(Integer page) {
        this.page = page;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    protected void pushPageDataToValueStack(Page<T> pageData){
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", pageData.getNumberOfElements());
        result.put("rows", pageData.getContent());

        ActionContext.getContext().getValueStack().push(result);
    }
}

