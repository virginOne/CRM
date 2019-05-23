package pri.crm.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pri.crm.dao.CustomerMapper;
import pri.crm.service.CustomerService;
import pri.crm.vo.CustomerVo;

@Service("customerService")
public class CustomerServiceImp implements CustomerService {
	@Autowired
	private CustomerMapper customerMapper;
	
	@Override
	public int insert(CustomerVo record) {
		return customerMapper.insert(record);
	}
	
	@Override
	public int insertSelective(CustomerVo record) {
		return customerMapper.insertSelective(record);
	}
	
	@Override
	public List<CustomerVo> query(CustomerVo record) {
		return customerMapper.query(record);
	}
	
	@Override
	public int delete(int cid) {
		return customerMapper.delete(cid);
	}
	
	@Override
	public int update(CustomerVo record) {
		return customerMapper.update(record);
	}
}
