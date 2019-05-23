package pri.crm.service;

import java.util.List;

import pri.crm.vo.UserVo;

public interface UserService {
	public List<UserVo> query(UserVo user);
	public int update(UserVo user);
	public int delete(UserVo user);
	public int insert(UserVo user);
}
