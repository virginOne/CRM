package pri.crm.dao;

import java.util.List;

import pri.crm.vo.CustomerVo;

public interface CustomerMapper {
	int insert(CustomerVo record);

    int insertSelective(CustomerVo record);
    
    List<CustomerVo> query(CustomerVo record);
    
    int delete(int cid);
    
    int update(CustomerVo record);
}
