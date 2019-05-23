$(document).ready(function() {
	var layer;
	layui.config({
		base : '../../plugins/layui/modules/'
	});
	layui.use([ 'form', 'layer', 'laydate' ], function() {
		var form = layui.form();
		layer = layui.layer;
		searchAccount(1);
	});
	$("#searBtn").on("click", function() {
		searchAccount(1);
	});
	$("#deleteBtn").on("click",function(){
		deleteuser();
	});
	$("#addBtn").unbind();
	$("#addBtn").on("click",function(){
		adduser();
	});
	$("#editeBtn").on("click",function(){
		edit();
	});
	var pageNo = 1;
	var totalPage = 1;
	var tableData=null;
	function searchAccount(pNo) {
		pageNo=pNo == null ? 1 : pageNo;
		var username = $("#usernameSear").val();
		var param = {
			"username" : username
		};

		$.ajax({
				async : false,
				type : 'POST',
				url : '/crm/user/sys/query?pageNo='+pageNo+'&t='
						+ Math.random(),
				dataType : 'json',
				contentType:'application/json',
				data :  JSON.stringify(param),
				timeout : 60000,
				success : function(result) {
					var pageData = result.data;
					if (pageData == null) {
						return false;
					}
					messgage=result.message;
					totalPage = pageData.lastPage;
					tableData=pageData.list;
					var data = pageData.list;
					var html = "";
					for (var i = 0; i < data.length; i++) {
						html += '<tr style="font-size:14px;height:35px;">';
						html += '<td style="font-size:14px;"><input type="checkbox" name="index" value="'+
						i + '"></td>';
						html += '<td style="font-size:14px;">'
								+ data[i].username
								+ '</td>';
						html += '<td style="font-size:14px;">'
								+ data[i].powername
								+ '</td>';
						html += '<td style="font-size:14px;">'
								+ data[i].eno + '</td>';
						html += '<td style="font-size:14px;">'
								+ data[i].ename + '</td>';

					}
					$("#dataList").html(html);

				},
				error : function(XMLHttpRequest,
						textStatus, errorThrown) {
					alert('系统错误,请联系系统管理员');
					return false;
				}
			});
	}

	function adduser() {
		$("#account_username").val(null);
		$("#account_password").val(null);
		$("#power").val(null);
		$("#eno").val(null);
		var emInfo=getEminfo();
		if(emInfo!=null){
			var html="";
			for(var i=0;i<emInfo.length;i++){
				html+='<option value="'+emInfo[i].eno+'">'+emInfo[i].name+'</option>';
			}
			$("#employee").html(html);
		}
		layer.open({
			title : '请填写账号信息',
			type : 1,
			content : $('#adduser'),
			shade : false,
			shadeClose : false,
			btn : [ '新增', '取消' ],
			yes : function(index, layero) {
				if($("#account_username").val()==null || $("#account_username").val()==""){
					layer.msg("用户名不能为空");
					return false;
				}
				if($("#account_password").val()==null || $("#account_password").val()==""){
					layer.msg("密码不能为空");
				}
				var param = {
					"username" : $("#account_username").val(),
					"password":$("#account_password").val(),
					"power":$("#power").val(),
					"eno":$("#employee").val()
				};
				$.ajax({
				async : false,
				type : 'POST',
				url : '/crm/user/sys/add?t='
						+ Math.random(),
				dataType : 'json',
				contentType:'application/json',
				data :  JSON.stringify(param),
				timeout : 60000,
				success : function(result) {
					layer.msg(result.message);
					searchAccount(1);
				},
				error : function(XMLHttpRequest,
						textStatus, errorThrown) {
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
		$("#account_username").val("");
		$("#account_password").val("");
		$("#power").val("");
		$("#eno").val("");
	}
	
	function deleteuser(){
		var optionIndex=new Array();
		$('#dataList input:checkbox[name="index"]:checked').each(function(){
			optionIndex.push($(this).val());
		});
		if(optionIndex.length==0){
			layer.msg("未选择被删除选项");
			return false;
		}
		var usernamesDel=new Array();
		for(var i=0;i<optionIndex.length;i++){
			usernamesDel.push(tableData[optionIndex[i]].username);
		}
		$.ajax({
			async : false,
			type : 'DELETE',
			contentType:'application/json',
			url : '/crm/user/sys/delete?t='
					+ Math.random(),
			dataType : 'json',
			data : JSON.stringify(usernamesDel),
			timeout : 60000,
			success : function(result) {
				layer.msg(result.message);
				searchAccount(1);
			},
			error : function(XMLHttpRequest,
					textStatus, errorThrown) {
				alert('系统错误,请联系系统管理员');
				return false;
			}
		});
	}
	function getEminfo(){
		var array=null;
		var l=$.ajax({
			async : false,
			type : 'POST',
			contentType:'application/json',
			url : '/crm/employee/sys/query?t='
					+ Math.random(),
			dataType : 'json',
			timeout : 60000,
			success : function(result) {
				array=result.data;
			},
			error : function(XMLHttpRequest,
					textStatus, errorThrown) {
				alert('系统错误,请联系系统管理员');
				return false;
			}
		});
		return array;
	}

	function edit() {
		var optionIndex=new Array();
		$('#dataList input:checkbox[name="index"]:checked').each(function(){
			optionIndex.push($(this).val());
		});
		if(optionIndex.length==0){
			layer.msg("未选择被编辑选项");
			return false;
		}else if(optionIndex.length>1){
			layer.msg("不能同时编辑多个选项");
			return false;
		}
		var emInfo=getEminfo();
		if(emInfo!=null){
			var html="";
			for(var i=0;i<emInfo.length;i++){
				html+='<option value="'+emInfo[i].eno+'">'+emInfo[i].name+'</option>';
			}
			$("#employee").html(html);
		}
		index=optionIndex[0];
		$("#account_username").val(tableData[index].username);
		$("#account_password").val(tableData[index].password);
		$("#power").val(tableData[index].power);
		$("#employee").val(tableData[index].eno);
		layer.open({
			title : '请修改账号信息',
			type : 1,
			content : $('#adduser'),
			shade : false,
			shadeClose : false,
			btn : [ '确定', '取消' ],
			yes : function(index, layero) {
			if($("#account_username").val()==null || $("#account_username").val()==""){
				layer.msg("用户名不能为空");
				return false;
			}
			if($("#account_password").val()==null || $("#account_password").val()==""){
				layer.msg("密码不能为空");
			}
			var username=$("#account_username").val();
			var passwor=$("#account_password").val();
			var power=$("#power").val();
			var eno=$("#eno").val();
			var param={
				"username":username,
				"password":password,
				"power":power,
				"eno":eno
			};
				// 按钮【按钮一】的回调
				$.ajax({
					async : false,
					type : 'GET',
					contentType:'application/json',
					url : '/crm/user/sys/query?t='
							+ Math.random(),
					dataType : 'json',
					data : JSON.stringify(param),
					timeout : 60000,
					success : function(result) {
						layer.msg(result.message);
						searchAccount(1);
					},
					error : function(XMLHttpRequest,
							textStatus, errorThrown) {
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
		
		
		var usernamesDel=new Array();
		
	}
	function fistPage() {
		if (pageNo <= 1) {
			layer.msg("已经是第一页了");
		} else {
			tableList.queryFiles(1);
			layer.msg('第一页');
		}
	}
	function prePage() {
		if (pageNo <= 1) {
			layer.msg("已经是第一页了");
		} else {
			tableList.queryFiles(Number(pageNo) - 1);
			layer.msg('第' + pageNo + '页');
		}
	}
	function nextPage() {
		if (pageNo >= totalPage) {
			layer.msg("已经是最后一页了");
		} else {
			tableList.queryFiles(Number(pageNo) + 1);
			layer.msg('第' + pageNo + '页');
		}
	}
	function lastPage() {
		if (pageNo >= totalPage) {
			layer.msg("已经是最后一页了");
		} else {
			tableList.queryFiles(totalPage);
			layer.msg('最后一页');
		}
	}

});