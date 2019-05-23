package pri.crm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import pri.crm.vo.UserVo;

@Mapper
public interface UserMapper {
    int insert(UserVo record);
    int insertSelective(UserVo record);
    List<UserVo> query(UserVo record);
    int delete (UserVo record);
    int update(UserVo record);
}