var form;
layui.use(['form', 'layedit', 'laydate'], function() {
	form = layui.form(), layer = layui.layer, layedit = layui.layedit, laydate = layui.laydate;

	//	// 创建一个编辑器
	//	var editIndex = layedit.build('LAY_demo_editor');
	//	// 自定义验证规则
	//	form.verify({
	//		pass: [/(.+){6,12}$/, '密码必须6到12位'],
	//		content: function(value) {
	//			layedit.sync(editIndex);
	//		}
	//	});

	// 监听提交
	form.on('submit(save)', function(data) {
				//layer.msg(JSON.stringify(data.field));
				updateInfo(JSON.stringify(data.field));
				return false;
			});

	fillData();
});

function showInfo() {
	$.ajax({
				async : true,
				type : 'GET',
				url : '/crm/employee/employeeInfo?t=' + Math.random(),
				dataType : 'json',
				timeout : 60000,
				success : function(result) {
					var employee = result.data;
					$("#eno").val(employee.eno);
					$("#dno").val(employee.dno);
					$("#sex").val(employee.sex);
					$("#name").val(employee.name);
					$("#telephone").val(employee.telephone);
					form.render();

				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					alert('系统错误,请联系系统管理员');
					return false;
				}
			});
}

function fillData() {
	$.ajax({
		async : true,
		type : 'GET',
		url : '/crm/deparment/query?t=' + Math.random(),
		dataType : 'json',
		timeout : 60000,
		success : function(result) {
			var data = result.data;
			if (data.length > 0) {
				var len = data.length;
				var optionHtml = "";
				for (var i = 0; i < len; i++) {
					optionHtml += '<option value="' + data[i].dno + '" '
								 + '>' + data[i].name + '</option>';
				}
				$("#dno").html(optionHtml);
			}
			showInfo();

		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert('系统错误,请联系系统管理员');
			return false;
		}
	});
}

function updateInfo(jsonData) {
	if(!checkPhone($("#telephone").val())){
		layer.msg("手机号码格式出错");
		return false;
	}
	$.ajax({
				async : true,
				type : 'PUT',
				url : '/crm/employee/update?t=' + Math.random(),
				contentType : 'application/json; charset=utf-8',
				data : jsonData,
				dataType : 'json',
				timeout : 60000,
				success : function(result) {
					layer.msg(result.message);
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					alert('系统错误,请联系系统管理员');
					return false;
				}
			});
}

function resetForm() {
	location.href = "info.html?t=" + Math.random();
}
function checkPhone(phone){ 
    if(!(/^1[34578]\d{9}$/.test(phone))){ 
        return false; 
    }else{
    	return true;
    }
}
