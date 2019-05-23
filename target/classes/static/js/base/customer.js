var layer;
layui.config({
			base : '../../plugins/layui/modules/'
		});
layui.use(['form', 'layer', 'laydate', 'laypage'], function() {
			var form = layui.form(), layer = layui.layer, laypage = layui.laypage;
		});
var option;
// 显示数据字典树形列表
refleshTree();
function refleshTree() {
	$("#org_tree").html("");
	layui.use('tree', function() {
				$.ajax({
							type : 'GET',
							url : '/crm/customer/query',
							dataType : 'json',
							async : false,
							success : function(result) {
								layui.tree({
											elem : '#org_tree' // 传入元素选择器
											,
											click : function(item) {
												option = item;
												showCust();
												pageNo = 0;
												showLinkman();
											},
											nodes : result.data
										});
							}
						});
			});
}

// 增加树节点
function addTree() {
	showAddCustDiv();
}

// 删除树节点
function delTree() {
	if (option == null || option == undefined) {
		layer.msg('请先选择客户');
		return;
	} else {
		layer.confirm('确认删除客户' + option.name + '?', {
					title : '请慎重操作',
					btn : ['确认', '取消'],
					offset : '35%',
					yes : function(index, layerno) {
						$.ajax({
									type : "DELETE",
									url : "/crm/customer/delete",
									data : {
										"cid" : option.cid
									},
									success : function(result) {
										layer.msg(result.message);
										refleshTree();
									},
									error : function(err) {
										layer.msg("删除失败");
									}
								});

						layer.close(index);
					}
				});
	}
}

// 新增客户节点
function showAddCustDiv() {
	var fields = ['name', 'sex', 'telephone', 'addr', 'company'];
	var filedNames = ['客户名称', '性别', '联系电话', '联系地址', '所属公司'];
	var result = true;
	var len = fields.length;

	layer.open({
				title : '添加客户',
				type : 1,
				offset : '20%',
				content : $('#addCustTree'),
				shade : false,
				shadeClose : false,
				btn : ['新增', '取消'],
				yes : function(index, layero) {
					// 按钮【按钮一】的回调

					for (var i = 0; i < len; i++) {
						if ($("#cust_" + fields[i]).val() == "") {
							layer.msg(filedNames[i] + "不能为空");
							return false;
						}
					}

					var cname = document.getElementById('cust_name').value;
					var sex = document.getElementById('cust_sex').value;
					var addr = document.getElementById('cust_addr').value;
					var telephone = document.getElementById('cust_telephone').value;
					var company = document.getElementById('cust_company').value;
					
					if(!checkPhone(telephone)){
						layer.msg("联系电话格式错误");
						return false;
					}
					
					$.ajax({
								async : false,
								type : "POST",
								url : "/crm/customer/insert",
								contentType : 'application/json; charset=utf-8',
								dataType : "json",
								data : JSON.stringify({
											"name" : cname,
											"sex" : sex,
											"address" : addr,
											"telephone" : telephone,
											"company" : company
										}),
								success : function(data) {
									refleshTree(); // 刷新节点
									layer.msg(data.message);
									layer.close(index);
									cname = "";
									sex = "";
									addr = "";
									telephone ="";
					var company = document.getElementById('cust_company').value;
								},
								error : function() {
									layer.msg('新增失败，请重试');
								}
							});
				},
				cancel : function(index, layero) {
					layer.close(index); // 如果设定了yes回调，需进行手工关闭
				}
			});
}

var pageNo = 1;
var totalPage = 0;
function fistPage() {
	if (pageNo == 1) {
		layer.msg("已经是第一页了");
	} else {
		pageNo = 1;
		showLinkman();
		layer.msg('第一页');
	}
}
function prePage() {
	if (pageNo == 1) {
		layer.msg("已经是第一页了");
	} else {
		pageNo -= 1;
		showLinkman();
		layer.msg('第' + pageNo + '页');
	}
}
function nextPage() {
	if (pageNo >= totalPage) {
		layer.msg("已经是最后一页了");
	} else {
		pageNo += 1;
		showLinkman();
		layer.msg('第' + pageNo + '页');
	}
}
function lastPage() {
	if (pageNo >= totalPage) {
		layer.msg("已经是最后一页了");
	} else {
		pageNo = totalPage;
		showLinkman();
		layer.msg('最后一页');
	}
}

function showCust() {
	// 点击菜单选项重现客户信息
	$('#cus_name').val(option.name);
	$('#cus_addr').val(option.address);
	$('#cus_sex').val(option.sex);
	$('#cus_telephone').val(option.telephone);
	$('#cus_company').val(option.company);

	// 点击菜单选项重现联系人信息
	// showLinkman();
}

// 显示客户联系人表格数据
function showLinkman() {
	$.ajax({
		type : 'GET',
		url : '/crm/Relationship/query',
		dataType : 'json',
		data : {
			"cid" : option.cid,
			"pageNo" : pageNo
		},
		async : true,
		success : function(result) {
			totalPage = result.data.total;
			data = result.data.list;
			if (data != null) {
				var leng = data.length;
			}
			var datahtml = "";
			if (leng > 0) {
				for (i in data) {
					datahtml += "<tr style='font-size:14px;height:35px;'><td style='font-size:14px;'><input type='checkbox' name='rid' value='"
							+ data[i].rid + "'>";
					datahtml += "</td><td class='dv_tb_jy_td' align='center'>"
							+ replaceNull(data[i].name);
					datahtml += "</td><td class='dv_tb_jy_td' align='center'>"
							+ replaceNull(data[i].sex);
					datahtml += "</td><td class='dv_tb_jy_td' align='center'>"
							+ replaceNull(data[i].telephone);
					datahtml += "</td><td class='dv_tb_jy_td' align='center'>"
							+ replaceNull(data[i].address);
					datahtml += "</td><td class='dv_tb_jy_td' align='center'>"
							+ replaceNull(data[i].company);
					datahtml += "</td><td class='dv_tb_jy_td' align='center'>"
							+ replaceNull(data[i].relationship);
					+"</td></tr>"
				}
				$("#linkmanList").html(datahtml);
			} else {
				$("#linkmanList")
						.html("<td  colspan='20' class='dv_tb_jy_td' align='center'> 没有数据！  </td>");
			}
		},
		error : function() {
			alert('客户联系人查询失败，请重试');
		}
	});
}

function delLinkman() {

	if ($('#linkmanList input:checkbox[name="rid"]:checked').length == 0) {
		layer.msg('未选择选项');
		return;
	}
	layer.confirm('确认删除?', {
				title : '删除',
				btn : ['确认', '取消'],
				offset : '30%',
				btnAlign : 'c',
				btn1 : function(index, layero) {
					var rids = new Array();
					$('#linkmanList input:checkbox[name="rid"]:checked').each(
							function() {
								rids.push($(this).val());
							});
					$.ajax({
								async : true,
								type : 'DELETE',
								contentType : 'application/json; charset=utf-8',
								dataType : "json",
								data : JSON.stringify(rids),
								url : '/crm/Relationship/delete',
								success : function(result) {
									layer.msg(result.message);
									showLinkman();
								},
								error : function() {
									layer.msg('系统出错，请联系管理员');
								}
							});
					layer.close(index);
				}
			})

}

function addlinkman() {
	if (option == null || option == undefined) {
		layer.msg('请先选择客户');
		return;
	}
	$.ajax({
				type : 'GET',
				url : '/crm/customer/query',
				dataType : 'json',
				async : false,
				success : function(result) {
					var html;
					var data=result.data;
					if(data!=null){
						for(i in data){
							if(data[i].cid==option.cid)
								continue;
							html+='<option value="'+data[i].cid+'">'+data[i].name+'</option>';
						}
						$("#link_cid").html(html);
					}
				}
	});
	layer.open({
		title : '请填写客户关系',
		type : 1,
		content : $('#addlinkman'),
		offset : '30%',
		shade : false,
		shadeClose : false,
		btn : ['新增', '取消'],
		yes : function(index, layero) {
			// 按钮【按钮一】的回调
			var cid=$("#link_cid").val();
			var relationship=$("#link_relationship").val();
			
			if(cid==""){
				layer.msg("关系人不能为空");
				return false;
			}
			if(relationship==""){
				layer.msg("关系不能为空");
				return false;
			}
			$.ajax({
						type : "POST",
						contentType : 'application/json; charset=utf-8',
						dataType : "json",
						data : JSON.stringify({
							"cid1":option.cid,
							"cid2":cid,
							"relationship":relationship
						}),
						url : '/crm/Relationship/insert',
						success : function(data) {
							layer.msg(data.message);
							showLinkman();
						},
						error : function() {
							layer.msg('新增失败，请重试');
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

function modCustomer() {
	if (option == null || option == undefined) {
		layer.msg('请先选择客户');
		return;
	}
	var params = {
		"cid" : option.cid,
		"name" : $('#cus_name').val(),
		"address" : $('#cus_addr').val(),
		"sex" : $('#cus_sex').val(),
		"telephone" : $('#cus_telephone').val(),
		"company" : $('#cus_company').val()
	};
	if(!checkPhone(telephone)){
		layer.msg("联系电话格式错误");
		return false;
	}
	
	$.ajax({
				async : false,
				type : "PUT",
				url : "/crm/customer/update",
				contentType : 'application/json; charset=utf-8',
				dataType : "json",
				data : JSON.stringify(params),
				success : function(data) {
					refleshTree(); // 刷新节点
					layer.msg(data.message);
					option = params;
					layer.close(index);
				},
				error : function() {
					layer.msg('新增失败，请重试');
				}
			});
}
function replaceNull(obj) {

	if (obj == undefined || obj == null || obj == "null" || obj == "undefined") {
		return "";
	} else {
		return obj;
	}
}
function checkPhone(phone){ 
    if(!(/^1[34578]\d{9}$/.test(phone))){ 
        return false; 
    }else{
    	return true;
    }
}