var layer;
layui.use([ 'form', 'layedit', 'laydate' ],function() {
	var form = layui.form(), layer = layui.layer, layedit = layui.layedit, laydate = layui.laydate;
});
$(function() {
	getEmployeeByPage(1);
})
$(function() {
	$("#first_page").click(function() {
		if (Number($('#pageNo').val()) == 1) {
			layer.msg("已经是第一页了");
		} else {
			getEmployeeByPage(1);
			layer.msg('第一页');
		}
	})

	$("#pre_page").click(function() {
		if (Number($('#pageNo').val()) == 1) {
			layer.msg("已经是第一页了");
		} else {
			getEmployeeByPage(Number($('#pageNo').val()) - 1);
			layer.msg('第' + (Number($('#pageNo').val()) - 1) + '页');
		}
	})

	$("#next_page").click(function() {
		if (Number($('#pageNo').val()) == Number($('#totalPage').val())) {
			layer.msg("已经是最后一页了");
		} else {
			getEmployeeByPage(Number($('#pageNo').val()) + 1);
			layer.msg('第' + (Number($('#pageNo').val()) + 1) + '页');
		}
	})

	$("#last_page").click(function() {
		if (Number($('#pageNo').val()) == Number($('#totalPage').val())) {
			layer.msg("已经是最后一页了");
		} else {
			getEmployeeByPage(Number($('#totalPage').val()));
			layer.msg('最后一页');
		}
	})
});
var deptData = null;
fillData();
//获取部门信息
function fillData() {
	$.ajax({
		async : false,
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
					optionHtml += '<option value="' + data[i].dno + '" ' + '>'
							+ data[i].name + '</option>';
				}
				$("#dno").html(optionHtml);
				deptData = data;
			}

		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert('系统错误,请联系系统管理员');
			return false;
		}
	});
}
function updateInfo(jsonData) {
	$.ajax({
		async : true,
		type : 'PUT',
		url : '/crm/employee/sys/update?t=' + Math.random(),
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
function addEmployeePage() {
	$("#ename").val('');
	$("#telephone").val('');
	fillData();
	layer.open({
		type : 1,
		title : '添加员工', //不显示标题栏
		shade : false,
		content : $('#addEmployee'),
		shade : false,
		shadeClose : false,
		btn : [ '新增', '取消' ],
		yes : function(index, layero) {
			if ($("#ename").val() == null || $("#ename").val() == "") {
				layer.msg("员工名不能为空");
				return false;
			}
			if(!checkPhone($("#telephone").val())){
				layer.msg("手机号码格式出错");
				return false;
			}
			var param = {
				"name" : $("#ename").val(),
				"sex" : $("#sex").val(),
				"dno" : $("#dno").val(),
				"telephone" : $("#telephone").val()
			};
			$.ajax({
				async : false,
				type : 'POST',
				url : '/crm/employee/sys/add?t=' + Math.random(),
				dataType : 'json',
				contentType : 'application/json',
				data : JSON.stringify(param),
				timeout : 60000,
				success : function(result) {
					layer.msg(result.message);
					getEmployeeByPage(1);
				},
				error : function(XMLHttpRequest, textStatus, errorThrown) {
					alert('系统错误,请联系系统管理员');
					return false;
				}
			});

			layer.close(index); // 如果设定了yes回调，需进行手工关闭

		},
		cancel : function(index, layero) {
			// 右上角关闭回调

			// return false 开启该代码可禁止点击该按钮关闭
			layer.close(index); // 如果设定了yes回调，需进行手工关闭
		}
	});
}

var tableData = null;
function getEmployeeByPage(pageNumber) {
	var param = {
		'name' : $("#searchText").val()
	};
	$.ajax({
		type : 'POST',
		url : '/crm/employee/sys/query?t=' + Math.random() + '&pageNo='
				+ pageNumber,
		contentType : 'application/json',
		dataType : 'json',
		data : JSON.stringify(param),
		async : true,
		success : function(result) {
			var page_data = result.data;
			var data = page_data.list;
			if (data == null) {
				return false;
			} else {
				$('#pageNo').val(pageNumber);
				$("#totalPage").val(page_data.lastPage);
				totalPage = page_data.lastPage;
				$("#tableList").html(listEmployee(data));
			}
		},
		error : function() {

		}
	});
}
function listEmployee(data) {
	tableData = data;
	var str = "";
	for ( var i in data) {
		str = str
				+ "<tr style='font-size: 14px; height: 35px;'>"
				+ "<th style='font-size: 14px;'><input name='index' type='checkbox' value='"
				+ i + "'></th>" + "<th style='font-size: 14px;'>" + data[i].eno
				+ "</th>" + "<th style='font-size: 14px;'>" + data[i].name
				+ "</th>" + "<th style='font-size: 14px;'>" + data[i].sex
				+ "</th>" + "<th style='font-size: 14px;'>"
				+ getDeptNameByDno(data[i].dno) + "</th>"
				+ "<th style='font-size: 14px;'>" + data[i].telephone + "</th>"
				+ "</tr>";
	}
	return str;
}

function getDeptNameByDno(dno) {
	fillData();
	for ( var i in deptData) {
		if (deptData[i].dno == dno)
			return deptData[i].name;
	}
	return '';
}

function editEmployeePage() {
	var checkArray = $('#tableList input:checkbox[name="index"]:checked');
	if (checkArray.length == 0) {
		layer.msg("请选择要修改的员工", {
			offset : '40%'
		});
		return;
	}
	if (checkArray.length > 1) {
		layer.msg("不能同时编辑多个员工信息", {
			offset : '40%'
		});
	}
	var checkid = $(checkArray[0]).val();
	if (checkArray.length == 1) {
		var optionData=tableData[checkid];
		fillData();
		$("#ename").val(optionData.name);
		$("#sex").val(optionData.sex);
		$("#dno").val(optionData.dno);
		$("#telephone").val(optionData.telephone);
		layer.open({
			type : 1,
			title : '修改员工信息', //不显示标题栏
			shade : false,
			content : $('#addEmployee'),
			shade : false,
			shadeClose : false,
			btn : [ '确定', '取消' ],
			yes : function(index, layero) {
				if ($("#ename").val() == null || $("#ename").val() == "") {
					layer.msg("员工名不能为空");
					return false;
				}
				if(!checkPhone($("#telephone").val())){
					layer.msg("手机号码格式出错");
					return false;
				}
				var param = {
					"eno":optionData.eno,
					"name" : $("#ename").val(),
					"sex" : $("#sex").val(),
					"dno" : $("#dno").val(),
					"telephone" : $("#telephone").val()
				};
				$.ajax({
					async : false,
					type : 'PUT',
					url : '/crm/employee/sys/update?t=' + Math.random(),
					dataType : 'json',
					contentType : 'application/json',
					data : JSON.stringify(param),
					timeout : 60000,
					success : function(result) {
						layer.msg(result.message);
						getEmployeeByPage(1);
					},
					error : function(XMLHttpRequest, textStatus, errorThrown) {
						alert('系统错误,请联系系统管理员');
						return false;
					}
				});

				layer.close(index); // 如果设定了yes回调，需进行手工关闭

			},
			cancel : function(index, layero) {
				// 右上角关闭回调

				// return false 开启该代码可禁止点击该按钮关闭
				layer.close(index); // 如果设定了yes回调，需进行手工关闭
			}
		});
	}
}

function deleteEmployee() {
	var optionIndex = new Array();
	$('#tableList input:checkbox[name="index"]:checked').each(function() {
		optionIndex.push($(this).val());
	});
	if (optionIndex.length == 0) {
		layer.msg("未选择被删除选项");
		return false;
	}
	var employees = new Array();
	for (var i = 0; i < optionIndex.length; i++) {
		employees.push(tableData[optionIndex[i]]);
	}
	$.ajax({
		async : false,
		type : 'DELETE',
		contentType : 'application/json',
		url : '/crm/employee/sys/delete?t=' + Math.random(),
		dataType : 'json',
		data : JSON.stringify(employees),
		timeout : 60000,
		success : function(result) {
			layer.msg(result.message);
			getEmployeeByPage(1);
		},
		error : function(XMLHttpRequest, textStatus, errorThrown) {
			alert('系统错误,请联系系统管理员');
			return false;
		}
	});
}
function checkPhone(phone){ 
    if(!(/^1[34578]\d{9}$/.test(phone))){ 
        return false; 
    }else{
    	return true;
    }
}
