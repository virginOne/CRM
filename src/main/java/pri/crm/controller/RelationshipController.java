package pri.crm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import pri.crm.service.RelationshipService;
import pri.crm.vo.JsonResult;
import pri.crm.vo.RelationshipVo;

@RestController
@RequestMapping("/Relationship")
public class RelationshipController {

	@Autowired
	private RelationshipService relationshipService;

	@GetMapping("/query")
	public JsonResult query(@RequestParam("cid") int cid ,@RequestParam(value="pageNo",required=false) int pageNo) {
		JsonResult jr = new JsonResult();
		
		if(pageNo<1) {
			pageNo=1;
		}
		
		PageHelper.startPage(pageNo, 10, true);

		Map<String,Object> params=new HashMap<>();
		params.put("cid", cid);
		PageInfo<Map<String,Object>> data =new PageInfo<>(relationshipService.query(params));

		jr.setData(data);

		return jr;
	}
	
	@PutMapping("/update")
	public JsonResult update(@RequestBody RelationshipVo relationshipVo) {
		JsonResult jr = new JsonResult();
		
		int result=relationshipService.update(relationshipVo);
		if(result>0) {
			jr.setMessage("修改成功");
		}else {
			jr.setMessage("修改失败");
		}

		return jr;
	}
	
	@DeleteMapping("/delete")
	public JsonResult delete(@RequestBody int[] rids) {
		JsonResult jr = new JsonResult();
		int count=0;
		
		for(int rid:rids) {
			int result=relationshipService.delete(rid);
			System.out.println(rid);
			if(result>0) {
				count++;
			}
		}
		if(count==rids.length)
			jr.setMessage("全部删除成功");
		else if(count==0)
			jr.setMessage("全部删除失败");
		else
			jr.setMessage("部分删除成功");

		return jr;
	}
	
	@PostMapping("/insert")
	public JsonResult insert(@RequestBody RelationshipVo relationshipVo) {
		JsonResult jr = new JsonResult();
		
		int result=relationshipService.insertSelective(relationshipVo);
		if(result>0) {
			jr.setMessage("添加成功");
		}else {
			jr.setMessage("添加失败");
		}

		return jr;
	}
}
