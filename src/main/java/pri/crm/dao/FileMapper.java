package pri.crm.dao;

import java.util.List;
import java.util.Map;

import pri.crm.vo.FileVo;

public interface FileMapper {
	
	List<FileVo> query(FileVo record);
	
    int insert(FileVo record);

    int insertSelective(FileVo record);
    
    int delete(FileVo record);
    
    int update(FileVo record);
    
    List<Map<String,Object>> vquery(Map<String, Object> params);
}