package pri.crm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import pri.crm.service.DepartmentService;
import pri.crm.service.EmployeeService;
import pri.crm.vo.EmployeeVo;
import pri.crm.vo.JsonResult;
import pri.crm.vo.UserVo;

@RestController
@RequestMapping("employee")
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private DepartmentService departmentService;
	
	@GetMapping("employeeInfo")
	public JsonResult employeeInfo(HttpSession session) {
		JsonResult jr=new JsonResult();
		EmployeeVo employee=(EmployeeVo) session.getAttribute("employee");
		UserVo user=(UserVo) session.getAttribute("user");
		Map<String, Object> userInfo=new HashMap<>();
		if(employee==null) {
			employee=new EmployeeVo();
			if(user.getEno()==null) {
				employee.setName("非员工用户");
			}else {
				employee.setEno(user.getEno());
				List<EmployeeVo> result=employeeService.query(employee);
				if(result.isEmpty()) {
					employee=new EmployeeVo();
					employee.setName("无此员工信息");
				}else {
					employee=result.get(0);
				}
			}
			session.setAttribute("employee", employee);
		}
		
		userInfo.put("username", user.getUsername());
		userInfo.put("power", user.getPower()==1 ? "管理员":"普通用户");
		userInfo.put("name", employee.getName());
		userInfo.put("eno", user.getEno());
		userInfo.put("sex", employee.getSex());
		userInfo.put("telephone", employee.getTelephone());
		userInfo.put("dno", employee.getDno());
		
		jr.setData(userInfo);
		return jr;
	}
	
	@PostMapping("/sys/query")
	public JsonResult query(@RequestBody(required = false) EmployeeVo employeeVo,@RequestParam(name="pageNo",required=false)Integer pageNo){
		JsonResult jr=new JsonResult();
		if(employeeVo==null) {
			employeeVo=new EmployeeVo();
		}
		List<EmployeeVo> list;
		if(pageNo!=null) {
			PageHelper.startPage(pageNo, 10, true);
			list=employeeService.query(employeeVo);
			PageInfo<EmployeeVo> pageInfo=new PageInfo<>(list);
			jr.setData(pageInfo);
		}else {
			list=employeeService.query(employeeVo);
			jr.setData(list);
		}
		
		return jr;
	}
	@RequestMapping("/sys/add")
	public JsonResult add(@RequestBody EmployeeVo employeeVo) {
		JsonResult jr=new JsonResult();
		
		int result=employeeService.insert(employeeVo);
		if(result>0) {
			jr.setMessage("添加成功");
		}else {
			jr.setMessage("添加失败");
		}
		
		return jr;
	}
	@PutMapping("/sys/update")
	public JsonResult sysUpdate(@RequestBody EmployeeVo employeeVo ,HttpSession session){
		JsonResult jr=new JsonResult();
		
		int result=employeeService.update(employeeVo);
		if(result>0) {
			jr.setMessage("修改成功");
			UserVo user=(UserVo) session.getAttribute("user");
			if(user.getEno().intValue()==employeeVo.getEno().intValue()) {
				session.setAttribute("employee", employeeVo);
			}
		}else {
			jr.setMessage("修改失败，多次失败请联系管理员");
		}
		
		return jr;
	}
	@PutMapping("update")
	public JsonResult update(@RequestBody EmployeeVo employeeVo ,HttpSession session){
		JsonResult jr=new JsonResult();
		UserVo user=(UserVo) session.getAttribute("user");
		employeeVo.setEno(user.getEno());
		int result=employeeService.update(employeeVo);
		if(result>0) {
			jr.setMessage("修改成功");
			session.setAttribute("employee", employeeVo);
		}else {
			jr.setMessage("修改失败，多次失败请联系管理员");
		}
		
		return jr;
	}
	
	@DeleteMapping("/sys/delete")
	public JsonResult delete(@RequestBody EmployeeVo[] employees) {
		JsonResult jr=new JsonResult();
		int result=0;
		int success=0;
		String message="";
		
		for(EmployeeVo employee: employees) {
			result=employeeService.delete(employee);
			if(result>0) {
				message+=employee.getName()+"删除成功\n";
				success++;
			}
		}
		if(success==employees.length && success!=0) {
			jr.setMessage("全部删除成功");
		}else {
			jr.setMessage(message);
		}
		
		return jr;
	}
}
