package pri.crm.service.imp;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pri.crm.dao.EmployeeMapper;
import pri.crm.service.EmployeeService;
import pri.crm.vo.EmployeeVo;


@Service("EmployeeService")
public class EmployeeImp implements EmployeeService {
	
	@Autowired
	private EmployeeMapper employeeMapper;
	
	@Override
	public List<EmployeeVo> query(EmployeeVo record) {
		return employeeMapper.query(record);
	}
	
	@Override
	public int update(EmployeeVo record) {
		return employeeMapper.update(record);
	}
	
	@Override
	public int delete(EmployeeVo record) {
		return employeeMapper.delete(record);
	}
	
	@Override
	public int insert(EmployeeVo record) {
		return employeeMapper.insertSelective(record);
	}
}
