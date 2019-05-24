package pri.crm.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import pri.crm.service.EmployeeService;
import pri.crm.service.UserService;
import pri.crm.vo.EmployeeVo;
import pri.crm.vo.JsonResult;
import pri.crm.vo.UserVo;

@RestController
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private EmployeeService employeeService;

	// 登录
	@RequestMapping("/login")
	public int login(@RequestParam("userName") String userName, @RequestParam("passWord") String passWord,
			@RequestParam("checkCode") String checkCode, HttpServletRequest request) {
		int code = 1;// 登录状态码

		if (!request.getSession().getAttribute("checkCode").toString().toLowerCase().equals(checkCode.toLowerCase())) {
			code = 0;
		} else {
			UserVo user = new UserVo();
			user.setUsername(userName);
			user.setPassword(passWord);
			List<UserVo> result = userService.query(user);
			if (result == null)
				code = 2;
			else {
				code = 1;
				request.getSession().setAttribute("user", result.get(0));
			}
		}
		return code;
	}

	@PutMapping("changePwd")
	public JsonResult update(@RequestParam("newPwd") String newPwd, @RequestParam("oldPwd") String oldPwd,
			HttpSession session) {
		JsonResult jr = new JsonResult();
		UserVo old = (UserVo) session.getAttribute("user");
		if (old.getPassword().equals(oldPwd)) {
			UserVo user = (UserVo) old.clone();
			user.setPassword(newPwd);
			int rs = userService.update(user);
			if (rs == 0) {
				jr.setMessage("修改失败，多次失败请联系管理员");
			} else {
				jr.setMessage("修改成功");
				session.setAttribute("user", user);
			}
		} else {
			jr.setMessage("修改失败，原密码不正确");
		}
		return jr;
	}

	// 注销
	@RequestMapping("/logout")
	public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().invalidate();
		response.sendRedirect("/crm/login.html");
	}

	// 验证码
	@RequestMapping("/checkCode")
	public void getCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		int width = 87;
		int height = 38;
		Random random = new Random();
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		BufferedImage image = new BufferedImage(width, height, 1);
		Graphics g = image.getGraphics();
		g.setColor(this.getColor(200, 250));
		g.setFont(new Font("Times New Roman", 0, 28));
		g.fillRect(0, 0, width, height);
		for (int i = 0; i < 40; i++) {
			g.setColor(this.getColor(130, 200));
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int x1 = random.nextInt(12);
			int y1 = random.nextInt(12);
			g.drawLine(x, y, x + x1, y + y1);
		}

		String strCode = "";
		for (int i = 0; i < 4; i++) {
			String rand = String.valueOf(codeSequence[random.nextInt(codeSequence.length)]);
			strCode = strCode + rand;
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 13 * i + 15, 28);
		}
		session.setAttribute("checkCode", strCode.toLowerCase());
		g.dispose();

		ImageIO.write(image, "JPEG", response.getOutputStream());
		response.getOutputStream().flush();
	}

	private Color getColor(int fc, int bc) {
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	private char[] codeSequence = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'q', 'a', 'z', 'w', 's', 'x', 'e',
			'd', 'c', 'r', 'f', 'v', 't', 'g', 'b', 'y', 'h', 'n', 'u', 'j', 'm', 'i', 'k', 'o', 'l', 'p', 'Q', 'A',
			'Z', 'W', 'S', 'X', 'E', 'D', 'C', 'R', 'F', 'V', 'T', 'G', 'B', 'Y', 'H', 'N', 'U', 'J', 'M', 'I', 'K',
			'O', 'L', 'P' };

	@RequestMapping("/sys/query")
	public JsonResult query(@RequestBody(required = false) UserVo userVo,
			@RequestParam(name = "pageNo", required = false) int pageNo) {
		JsonResult jr = new JsonResult();

		PageHelper.startPage(pageNo, 10, true);
		if (userVo == null) {
			userVo = new UserVo();
		}
		List<UserVo> result = userService.query(userVo);

		if (result == null)
			jr.setMessage("查询结果为空");
		else {
			List<Map<String, Object>> list = new ArrayList<>();
			for (UserVo user : result) {
				Map<String, Object> map = new HashMap<>();
				map.put("username", user.getUsername());
				map.put("password", user.getPassword());
				map.put("power", user.getPower());
				map.put("powername", user.getPower() == 1 ? "管理员" : "普通用户");
				map.put("eno", user.getEno());

				EmployeeVo vo = new EmployeeVo();
				vo.setEno(user.getEno());
				List<EmployeeVo> t = employeeService.query(vo);
				if (t.isEmpty()) {
					map.put("ename", "员工不存在");
				} else {
					map.put("ename", t.get(0).getName());
				}
				list.add(map);
			}
			PageInfo<Map<String, Object>> data = new PageInfo<>(list);
			jr.setData(data);
			jr.setMessage("查询成功");
		}

		return jr;
	}
	@RequestMapping("/sys/add")
	public JsonResult add(@RequestBody UserVo user) {
		JsonResult jr=new JsonResult();
		
		UserVo t=new UserVo();
		t.setUsername(user.getUsername());
		List<UserVo> tr=userService.query(t);
		if(tr!=null) {
			jr.setMessage("添加失败,不能添加已有账户");
			return jr;
		}
		int result=userService.insert(user);
		if(result>0) {
			jr.setMessage("添加成功");
		}else {
			jr.setMessage("添加失败");
		}
		
		return jr;
	}
	@DeleteMapping("/sys/delete")
	public JsonResult delete(@RequestBody String[] usernames) {
		JsonResult jr=new JsonResult();
		UserVo user=new UserVo();
		int result=0;
		int success=0;
		String message="";
		
		for(String username: usernames) {
			user.setUsername(username);
			result=userService.delete(user);
			if(result>0) {
				message+=username+"删除成功\n";
				success++;
			}
		}
		if(success==usernames.length && success!=0) {
			jr.setMessage("全部删除成功");
		}else {
			jr.setMessage(message);
		}
		
		return jr;
	}
}
