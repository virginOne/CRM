package pri.crm.service;

import java.util.List;

import pri.crm.vo.EmployeeVo;

public interface EmployeeService {
	
	public List<EmployeeVo> query(EmployeeVo record);
	public int update(EmployeeVo record);
	public int delete(EmployeeVo record);
	public int insert(EmployeeVo record);
}
