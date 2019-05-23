package pri.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pri.crm.service.CustomerService;
import pri.crm.vo.CustomerVo;
import pri.crm.vo.JsonResult;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	
	@Autowired
	private CustomerService customerService;
	
	@GetMapping("/query")
	public JsonResult query(@RequestBody(required=false) CustomerVo customerVo) {
		JsonResult jr=new JsonResult();
		
		jr.setData(customerService.query(customerVo));
		
		return jr;
	}
	
	@PostMapping("/insert")
	public JsonResult insert(@RequestBody CustomerVo customerVo) {
		JsonResult jr=new JsonResult();
		
		int result=customerService.insertSelective(customerVo);
		
		if(result>0) {
			jr.setMessage("添加成功");
		}else {
			jr.setMessage("添加失败");
		}
		
		return jr;
	}
	
	@PutMapping("/update")
	public JsonResult update(@RequestBody CustomerVo customerVo) {
		JsonResult jr=new JsonResult();
		
		int result=customerService.update(customerVo);
		
		if(result>0) {
			jr.setMessage("更新成功");
		}else {
			jr.setMessage("更新失败");
		}
		
		return jr;
	}
	
	@DeleteMapping("/delete")
	public JsonResult delete(@RequestParam("cid") int cid) {
		JsonResult jr=new JsonResult();
		
		int result=customerService.delete(cid);
		
		if(result>0) {
			jr.setMessage("删除成功");
		}else {
			jr.setMessage("删除失败");
		}
		
		return jr;
	}
	
}
