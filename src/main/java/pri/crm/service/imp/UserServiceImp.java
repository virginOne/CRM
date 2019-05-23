package pri.crm.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pri.crm.dao.UserMapper;
import pri.crm.service.UserService;
import pri.crm.vo.UserVo;

@Service("UserService")
public class UserServiceImp implements UserService {

	@Autowired
	private UserMapper userMapper;
	
	public List<UserVo> query(UserVo user) {
		List<UserVo> result=userMapper.query(user);
		if(result.isEmpty())
			return null;
		else
			return result;
	};
	
	@Override
	public int update(UserVo user) {
		return userMapper.update(user);
	}
	
	@Override
	public int delete(UserVo user) {
		return userMapper.delete(user);
	}
	
	@Override
	public int insert(UserVo user) {
		return userMapper.insert(user);
	}
}
