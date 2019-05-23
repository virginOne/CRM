package pri.crm.service;

import java.util.List;

import pri.crm.vo.DepartmentVo;

public interface DepartmentService {
	List<DepartmentVo> query(DepartmentVo record);
}