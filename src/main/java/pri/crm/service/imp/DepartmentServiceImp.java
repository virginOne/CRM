package pri.crm.service.imp;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pri.crm.dao.DepartmentMapper;
import pri.crm.service.DepartmentService;
import pri.crm.vo.DepartmentVo;

@Service("DepartmentService")
public class DepartmentServiceImp implements DepartmentService{
	
	@Autowired
	private DepartmentMapper departmentMapper;

	@Override
	public List<DepartmentVo> query(DepartmentVo record){
		return departmentMapper.query(record);
	}
}