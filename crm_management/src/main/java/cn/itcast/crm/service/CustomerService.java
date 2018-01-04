package cn.itcast.crm.service;

import java.util.List;

import javax.ws.rs.*;

import cn.itcast.crm.domain.Customer;

/**
 * 客户操作
 * 
 * @author itcast
 *
 */
public interface CustomerService {

	// 查询所有未关联客户列表
	@Path("/noassociationcustomers")
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<Customer> findNoAssociationCustomers();

	// 已经关联到指定定区的客户列表
	@Path("/associationfixedareacustomers/{fixedareaid}")
	@GET
	@Produces({ "application/xml", "application/json" })
	public List<Customer> findHasAssociationFixedAreaCustomers(
			@PathParam("fixedareaid") String fixedAreaId);

	// 将客户关联到定区上 ， 将所有客户id 拼成字符串 1,2,3
	@Path("/associationcustomerstofixedarea")
	@PUT
	public void associationCustomersToFixedArea(
			@QueryParam("customerIdStr") String customerIdStr,
			@QueryParam("fixedAreaId") String fixedAreaId);

	/**
	 * 保存客户信息的方法
	 * @param customer
	 */
	@Path("/customer")
	@POST
	@Consumes({ "application/xml", "application/json" })
	public void regist(Customer customer);

	/**
	 * 根据电话查询,用户状态的方法
	 * @param telephone
	 * @return
	 */
	@Path("/customer/telephone/{telephone}")
	@GET
	@Consumes({"application/xml","application/json"})
	public Customer findByTelephone(@PathParam("telephone") String telephone);

	@Path("/customer/updatetype/{telephone}")
	@GET
	public void undateType(@PathParam("telephone") String telephone);
}
