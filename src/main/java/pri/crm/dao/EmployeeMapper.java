package pri.crm.dao;

import java.util.List;

import pri.crm.vo.EmployeeVo;

public interface EmployeeMapper {
	
	List<EmployeeVo> query(EmployeeVo recode);
  	int insert(EmployeeVo record);
    	int insertSelective(EmployeeVo record);
    	int update (EmployeeVo record);
    	int delete(EmployeeVo record);
}