function updatePwdForm(jsonData)
{

	var saveurl = '/crm/user/changePwd?t=' + Math.random();

	// 验证通过进行提交
	$.ajax({
	      async:false,
		  type: 'PUT',
		  url:saveurl,
		  data: JSON.parse(jsonData),
		  dataType: 'json',
	      success:function(result){
		         layer.msg(result.message);
	      	}
	      	,error:function(XMLHttpRequest, textStatus, errorThrown) {
			   	  alert('系统错误,请联系系统管理员');
			   	  return false;
			  }
     });	
}

			layui.use(['form', 'layedit', 'laydate'], function() {
				var form = layui.form(),
					layer = layui.layer,
					layedit = layui.layedit,
					laydate = layui.laydate;

				// 创建一个编辑器
				var editIndex = layedit.build('LAY_demo_editor');
				// 自定义验证规则
				form.verify({
					pass: [/(.+){0,12}$/, '密码必须0到12位'],
					confirmpwd:function(value,item){
						if ($("#newPwd").val() !=$("#confirmpwd").val())
						{
							return "两次密码不一致";
						}else if($("#newPwd").val()==$("#oldPwd").val()){
							return "新密码与旧密码一致"
						}
					},
					content: function(value) {
						layedit.sync(editIndex);
					}
				});

				// 监听提交
				form.on('submit(save)', function(data) {
					var jsonparams = JSON.stringify(data.field);
					updatePwdForm(jsonparams);
					return false;
				});
			});