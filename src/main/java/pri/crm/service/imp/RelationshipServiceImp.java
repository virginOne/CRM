package pri.crm.service.imp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pri.crm.dao.RelationshipMapper;
import pri.crm.service.RelationshipService;
import pri.crm.vo.RelationshipVo;

@Service("relationshipService")
public class RelationshipServiceImp implements RelationshipService {
	
	@Autowired
	private RelationshipMapper relationshipMapper;

	@Override
	public int insert(RelationshipVo record) {
		return relationshipMapper.insert(record);
	}

	@Override
	public int insertSelective(RelationshipVo record) {
		return relationshipMapper.insertSelective(record);
	}

	@Override
	public List<Map<String, Object>> query(Map<String,Object> params) {
		return relationshipMapper.query(params);
	}

	@Override
	public int delete(int rid) {
		return relationshipMapper.delete(rid);
	}
	
	@Override
	public int update(RelationshipVo record) {
		return relationshipMapper.update(record);
	}

}
