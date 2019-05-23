package pri.crm.dao;

import java.util.List;

import pri.crm.vo.DepartmentVo;

public interface DepartmentMapper {
    int insert(DepartmentVo record);

    int insertSelective(DepartmentVo record);

    List<DepartmentVo> query(DepartmentVo record);
}