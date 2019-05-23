package pri.crm.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.parsing.ParsingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import pri.crm.service.FileService;
import pri.crm.util.UUIDUtil;
import pri.crm.vo.FileVo;
import pri.crm.vo.JsonResult;
import pri.crm.vo.UserVo;

@RestController
@RequestMapping("/file")
public class FileController {

	@Autowired
	private HttpServletResponse response;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private HttpSession session;

	@Autowired
	private FileService fileService;

	@Value("${upload.path}")
	private String uploadPath;

	@GetMapping("/query")
	public JsonResult query() {
		JsonResult jr = new JsonResult();

		String pageNotemp = request.getParameter("pageNo");
		String fname = request.getParameter("fname");
		String creator = request.getParameter("creator");
		String ftypeTemp = request.getParameter("ftype");

		int pageNo = 1;
		List<String> ftypes = null;

		if (pageNotemp != null && !pageNotemp.toLowerCase().equals("null") && !pageNotemp.isEmpty()) {
			pageNo = Integer.parseInt(pageNotemp);
		}
		if (fname == null || fname.toLowerCase().equals("null") || fname.isEmpty()) {
			fname = null;
		} else {
			fname = fname.replace("*", "%");
			fname = fname.replace("?", "_");
		}
		if (creator == null || creator.toLowerCase().equals("null") || creator.isEmpty()) {
			creator = null;
		} else {
			creator = creator.replace("*", "%");
			creator = creator.replace("?", "_");
		}
		if (ftypeTemp == null || ftypeTemp.toLowerCase().equals("null") || ftypeTemp.isEmpty()) {
			ftypes = null;
		} else {
			String[] ftype = ftypeTemp.replaceAll("\\[?\\]?\"?", "").split(",");
			ftypes = new ArrayList<String>();
			for (String type : ftype) {
				if (type.equals("all")) {
					ftypes = null;
					break;
				}
				ftypes.add(type);
			}
		}

		PageHelper.startPage(pageNo, 10, true);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("fname", fname);
		params.put("creator", creator);
		params.put("ftype", ftypes);
		List<Map<String, Object>> result = fileService.vquery(params);

		PageInfo<Map<String, Object>> data = new PageInfo<>(result);

		jr.setData(data);
		return jr;
	}

	@PostMapping("/upload") // 多文件上传
	public JsonResult upload(@RequestParam("files") List<MultipartFile> files) {
		UserVo user = (UserVo) session.getAttribute("user");
		FileVo file = new FileVo();

		file.setEno(user.getEno());
		return filesUpLoad(files, file);
	}

	@PostMapping("/delete")
	public JsonResult delete() {
		String[] fids = request.getParameter("fids").replaceAll("\\[?\\]?\"?", "").split(",");
		String message = "";

		FileVo fileVo = new FileVo();
		for (String fid : fids) {
			if (fid.isEmpty() || fid.toLowerCase().equals("null"))
				continue;
			fileVo.setFid(Integer.parseInt(fid));
			int result = fileService.delete(fileVo);
			if (result == 0) {
				fileVo = fileService.query(fileVo).get(0);
				message += fileVo.getName() + "删除失败\n";
			}
		}
		JsonResult result = new JsonResult();
		if (message.isEmpty())
			message = "全部删除成功";
		result.setMessage(message);
		return result;
	}

	@PostMapping("/update/{fid}")
	public JsonResult edite(@PathVariable("fid") int fid, @RequestParam("files") MultipartFile file) {
		UserVo user = (UserVo) session.getAttribute("user");
		FileOutputStream fos = null;
		BufferedInputStream bis = null;

		FileVo fileVo = new FileVo();
		fileVo.setFid(fid);
		fileVo.setEno(user.getEno());

		JsonResult result = new JsonResult();
		String message = "";

		String fileName = file.getOriginalFilename();
		String fname = fileName.substring(0, fileName.lastIndexOf("."));
		String ftype = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		long fsize = file.getSize();
		String fpath = uploadPath + "/" + UUIDUtil.getUUID();
		File f = new File(fpath + "." + ftype);
		try {
			bis = new BufferedInputStream(file.getInputStream());
			while (f.exists()) {
				fpath += String.valueOf(Math.random() * 10);
				f = new File(fpath + "." + ftype);
			}
			if (!f.getParentFile().exists()) {
				f.getParentFile().mkdirs();
			}
			fpath += "." + ftype;
			fos = new FileOutputStream(f);
			byte[] b = new byte[1024];
			while (bis.read(b) != -1) {
				fos.write(b);
			}
			try {
				// 将参数传入实体
				fileVo.setName(fname);
				;
				fileVo.setSuffix(ftype);
				fileVo.setSize(fsize);
				fileVo.setPath(fpath);
				fileVo.setTime(new Date());
				fileService.update(fileVo);
			} catch (Exception e) {
				throw new Exception(e);
			}
		} catch (Exception e) {
			f.delete();
			message = "文件" + fname + "上传失败 \n";
			e.printStackTrace();
		} finally {
			try {
				fos.close();
				bis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		result.setMessage(message);

		result.setMessage("更新成功");
		return result;
	}

	@PostMapping("/download")
	public void download() {
		String[] fids = request.getParameterValues("fids");
		FileVo fileVo = new FileVo();
		ZipOutputStream zos = null;
		try {
			if (fids.length == 1) {
				fileVo.setFid(Integer.parseInt(fids[0]));
				List<FileVo> list = fileService.query(fileVo);
				File file = new File(list.get(0).getPath());
				File tempFile = new File(
						file.getParent() + "\\" + list.get(0).getName() + "." + list.get(0).getSuffix());
				if (tempFile.exists()) {
					tempFile.delete();
					tempFile.createNewFile();
				} else {
					tempFile.createNewFile();
				}
				org.apache.commons.io.FileUtils.copyFile(file, tempFile);
				BufferedInputStream bis = new BufferedInputStream(new FileInputStream(tempFile));
				response.setContentType("application/force-download");
				response.setCharacterEncoding("utf-8");
				response.setHeader("Content-Disposition",
						"attachment;fileName=" + URLEncoder.encode(tempFile.getName(), "utf-8"));
				response.addHeader("Content-Length", "" + tempFile.length());
				BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
				byte[] b = new byte[1024];
				while (bis.read(b) != -1) {
					bos.write(b);
				}
				bis.close();
				bos.close();
				return;
			}
			zos = new ZipOutputStream(response.getOutputStream(), Charset.forName("utf-8"));
			response.setContentType("application/force-download");
			response.setHeader("Content-Disposition", "attachment;fileName=temp.zip");
			for (String fid : fids) {
				fileVo.setFid(Integer.parseInt(fid));
				List<FileVo> list = fileService.query(fileVo);
				for (FileVo tsf : list) {
					File file = new File(tsf.getPath());
					File tempFile = new File(file.getParent() + "\\" + tsf.getName() + "." + tsf.getSuffix());
					if (tempFile.exists()) {
						tempFile.delete();
						tempFile.createNewFile();
					} else {
						tempFile.createNewFile();
					}
					BufferedInputStream bis = null;
					if (file.exists()) {
						try {
							byte[] b = new byte[1024];
							bis = new BufferedInputStream(new FileInputStream(tempFile));
							org.apache.commons.io.FileUtils.copyFile(file, tempFile);
							ZipEntry ze = new ZipEntry(tempFile.getName());
							zos.putNextEntry(ze);
							while (bis.read(b) != -1) {
								zos.write(b);
							}
							bis.close();
						} catch (IOException e) {
							e.printStackTrace();
							tempFile.delete();
						} finally {
							try {
								bis.close();
								tempFile.delete();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(zos!=null)
				zos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public JsonResult filesUpLoad(List<MultipartFile> files, FileVo fileVo) {

		JsonResult result = new JsonResult();
		String message = "";
		FileOutputStream fos = null;
		BufferedInputStream bis = null;

		// 多文件处理
		for (MultipartFile file : files) {
			String fileName = file.getOriginalFilename();
			String fname = fileName.substring(0, fileName.lastIndexOf("."));
			String ftype = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
			long fsize = file.getSize();
			String fpath = uploadPath + "/" + UUIDUtil.getUUID();
			File f = new File(fpath + "." + ftype);
			try {
				bis = new BufferedInputStream(file.getInputStream());
				while (f.exists()) {
					fpath += String.valueOf(Math.random() * 10);
					f = new File(fpath + "." + ftype);
				}
				if (!f.getParentFile().exists()) {
					f.getParentFile().mkdirs();
				}
				fpath += "." + ftype;
				fos = new FileOutputStream(f);
				byte[] b = new byte[1024];
				while (bis.read(b) != -1) {
					fos.write(b);
				}
				try {
					// 将参数传入实体
					fileVo.setName(fname);
					;
					fileVo.setSuffix(ftype);
					fileVo.setSize(fsize);
					fileVo.setPath(fpath);
					fileVo.setTime(new Date());
					fileService.insert(fileVo);
				} catch (Exception e) {
					throw new Exception(e);
				}
			} catch (Exception e) {
				f.delete();
				message += "文件" + fname + "上传失败 \n";
				e.printStackTrace();
			} finally {
				try {
					fos.close();
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		result.setMessage(message);
		return result;
	}
}
