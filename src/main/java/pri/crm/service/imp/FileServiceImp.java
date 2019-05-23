package pri.crm.service.imp;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pri.crm.dao.FileMapper;
import pri.crm.service.FileService;
import pri.crm.vo.FileVo;

@Service("FileService")
public class FileServiceImp implements FileService {
	
	@Autowired
	private FileMapper fileMapper;
	
	@Override
	public List<FileVo> query(FileVo record) {
		return fileMapper.query(record);
	}
	
	@Override
	public List<Map<String, Object>> vquery(Map<String, Object> params) {
		return fileMapper.vquery(params);
	}

	@Override
	public int insert(FileVo record) {
		return fileMapper.insertSelective(record);
	}
	
	@Override
	public int delete(FileVo record) {
		return fileMapper.delete(record);
	}
	
	@Override
	public int update(FileVo record) {
		return fileMapper.update(record);
	}
}
