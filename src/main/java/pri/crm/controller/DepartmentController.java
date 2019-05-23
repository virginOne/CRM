package pri.crm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pri.crm.service.DepartmentService;
import pri.crm.vo.DepartmentVo;
import pri.crm.vo.JsonResult;

@RestController
@RequestMapping("/deparment")
public class DepartmentController {
	@Autowired
	private DepartmentService departmentService;
	
	@GetMapping("query")
	public JsonResult query(@RequestBody(required=false) DepartmentVo departmentVo) {
		JsonResult jr=new JsonResult();
		List<DepartmentVo> result=departmentService.query(departmentVo);
		jr.setData(result);
		
		return jr;
	}
}
