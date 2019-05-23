package pri.crm.service;

import java.util.List;
import java.util.Map;

import pri.crm.vo.RelationshipVo;

public interface RelationshipService {

	int insert(RelationshipVo record);

    int insertSelective(RelationshipVo record);
    
    List<Map<String,Object>> query(Map<String,Object> params);
    
   int delete(int rid);
   
   int update(RelationshipVo record);
}
