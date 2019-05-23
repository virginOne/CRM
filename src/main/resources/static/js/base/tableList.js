var layer;
layui.use(['layer'], function() {
	layer=layui.layer;
})
var tableList = {
	init : function() {
		this.uploadConfig();
		this.bindEvent();
		this.queryFiles();
	},
	params : {
		fname : '',
		ftype : '',
		pageNo : 1,
		totalPage:1
	},
	bindEvent : function() {
		$('#editeFile').on('click', this.editeFile);
		$('#downloadFiles').on('click', this.downloadFiles);
		$('#downloadFiles').on('click', this.downloadFiles);
		$('#deleteFiles').on('click', this.deleteFiles);
		$('#btnSearch').on('click', this.search);
		$('#uploadFiles').on('click', this.uploadFiles)
		// 首页
		$('#data_list').on('click', '.laypage_first', this.fistPage);
		$('#play_first').on('click', this.fistPage);
		// 上一页
		$('#data_list').on('click', '.layui-laypage-prev', this.prePage);
		$('#play_pre').on('click', this.prePage);
		// 下一页
		$('#data_list').on('click', '.layui-laypage-next', this.nextPage);
		$('#play_next').on('click', this.nextPage);
		// 最后一页
		$('#data_list').on('click', '.layui-laypage-last', this.lastPage);
		$('#play_last').on('click', this.lastPage);
	},
	fistPage : function() {
		if (this.params.pageNo == 1) {
			layer.msg("已经是第一页了");
		} else {
			tableList.queryFiles(1);
			layer.msg('第一页');
		}
	},
	prePage : function() {
		if (this.params.pageNo == 1) {
			layer.msg("已经是第一页了");
		} else {
			tableList.queryFiles(Number(this.params.pageNo) - 1);
			layer.msg('第' + this.params.pageNo + '页');
		}
	},
	nextPage : function() {
		if (this.params.pageNo >= this.params.totalPage) {
			layer.msg("已经是最后一页了");
		} else {
			tableList.queryFiles(Number(this.params.pageNo) + 1);
			layer.msg('第' + this.params.pageNo + '页');
		}
	},
	lastPage : function() {
		if (this.params.pageNo >= this.params.totalPage) {
			layer.msg("已经是最后一页了");
		} else {
			tableList.queryFiles(this.params.totalPage);
			layer.msg('最后一页');
		}
	},
	search : function() {
		var ftype = new Array();
		$('#fileType input:checkbox[name="filetype"]:checked').each(function() {
					ftype.push($(this).val());
				});
		tableList.params.fname = $('#fileName').val();
		tableList.params.creator = $('#fileCreator').val();
		tableList.params.ftype = JSON.stringify(ftype);
		tableList.params.pageNo = 1;
		tableList.queryFiles(null);
	},
	queryFiles : function(pageNo) {
		this.params.pageNo = pageNo == null ? 1 : pageNo;
		$.ajax({
			async : false,
			type : 'GET',
			data : this.params,
			dataType : 'json',
			url : '/crm/file/query',
			success : function(result) {
				var data = result.data.list;
				var str = '<tr style="font-size:14px;height:35px;">'
						+ '<th style="font-size:14px;">选择</th>'
						+ '<th style="font-size:14px;">文档名称</th>'
						+ '<th style="font-size:14px;">文档类型</th>'
						+ '<th style="font-size:14px;">上传人</th>'
						+ '<th style="font-size:14px;">更新日期</th>'
						+ '</tr>';
				str1 = '<div>';
				str2 = '';
				if (data != null) {
					for (var i = 0; i < data.length; i++) {
						str += '<tr style="font-size:14px;height:35px;">';
						str += '<th style="font-size:14px;"><input type="checkbox" name="fid" value="'
								+ tableList.checkNull(data[i].fid) + '"></th>';
						str += '<th style="font-size:14px;">'
								+ tableList.checkNull(data[i].name) + '</th>';
						str += '<th style="font-size:14px;">'
								+ tableList.checkNull(data[i].suffix
										.toUpperCase()) + '</th>';
						str += '<th style="font-size:14px;">'
								+ tableList.checkNull(data[i].creator)
								+ '</th>';
						str += '<th style="font-size:14px;">'
								+ tableList.checkNull(data[i].time)
								+ '</th>';
						str += '</tr>';
					}
				}
				str += '<tr><td colspan="6">'
						+ '<div class="layui-box layui-laypage layui-laypage-default"'
						+ 'id="layui-laypage-0" style="text-align:center;width:90%;">'
						+ '<a id="fistPage" href="#" class="laypage_first">第一页</a>'
						+ '<a id="prePage"href="#" class="layui-laypage-prev">上一页</a>'
						+ '<a id="nextPage"href="#" class="layui-laypage-next">下一页</a>'
						+ '<a id="lastPage"href="#" class="layui-laypage-last">最后页</a>'
						+ '</div></td></tr>';

				$('#data_list').html(str);
			},
			error:function(){
				layer.msg("查询失败");
			}
		});
	},
	downloadFiles : function() {
		var checkbox = $('#data_list input:checkbox[name="fid"]:checked');
		if (checkbox.length == 0) {
			layer.msg('未选择文件');
			return;
		}
		var form = $('<form action="/crm/file/download" method="post">');
		checkbox.each(function() {
					form.append('<input type="hidden" name="fids" value='
							+ $(this).val() + '>');
				});
		form.appendTo('body').submit().remove();
	},
	uploadFiles : function uploadFile() {
		$('#uploadDiv').attr('index', layer.open({
							title : '请上传文件',
							type : 1,
							content : $('#uploadDiv'),
							shade : 0.2,
							shadeClose : false
						}));
		$('#uploadDiv form').attr('action',
				'/crm/file/upload');
		$('#uploadFile').attr('multiple', 'multiple');
	},
	uploadConfig : function() {
		layui.use(['form', 'layer', 'laydate', 'upload'], function() {
					var form = layui.form(), layer = layui.layer;
					layui.upload({
								url : ''
								,
								title : '请上传售前资料',
								type : 'file',
								ext : 'ppt|pptx|doc|docx|xls|rar|zip|7z|xlsx|bat|jar|txt',
								elem : "#uploadFile" // 绑定元素
								,
								before : function() {
									layer.close($('#uploadDiv').attr('index'));
								},
								success : function(res) {// 上传成功接口,
									if (res.code == "500") {
										layer.msg("上传失败");
										return;
									}
									layer.msg("上传成功");
									tableList.queryFiles(null);
								},
								error : function(xhr, error, exception) {
									layer.msg("上传失败");
								}
							});
				});
	},
	deleteFiles : function() {
		layui.use('layer', function() {
					layer = layui.layer;
					if ($('#data_list input:checkbox[name="fid"]:checked').length == 0) {
						layer.msg('未选择文件');
						return;
					}
					layer.confirm('确认删除?', {
								title : '删除',
								btn : ['确认', '取消'],
								btnAlign : 'c',
								btn1 : function(index, layero) {
									var fids = new Array();
									$('#data_list input:checkbox[name="fid"]:checked')
											.each(function() {
														fids
																.push($(this)
																		.val());
													});
									if ($('#fid').val() != "") {
										fids.push($('#fid').val());
									}
									$.ajax({
												async : true,
												type : 'POST',
												data : {
													'fids' : JSON
															.stringify(fids)
												},
												url : '/crm/file/delete',
												success : function(result) {
													layer.msg(result.message);
													tableList.queryFiles(null);
												},
												error : function() {
													layer.msg('系统出错，请联系管理员');
												}
											});
									layer.close(index);
								}
							})
				});
	},
	editeFile : function() {
		var checkbox = $('#data_list input:checkbox[name="fid"]:checked');
		if (checkbox.length == 0) {
			layer.msg('未选择文件');
			return;
		} else if (checkbox.length > 1) {
			layer.msg('不能同时编辑多个文件');
			return;
		}
		layer.open({
					title : '请上传文件',
					type : 1,
					content : $('#uploadDiv'),
					shade : 0.2,
					shadeClose : false
				});

		$('#uploadDiv form').attr('action',
				'/crm/file/update/' + checkbox[0].value);
		$('#uploadFile').removeAttr('multiple');

	},
	checkNull : function(obj) {
		if (obj == undefined || obj == null || obj.toLowerCase == 'null')
			return '';
		else
			return obj;
	}
};
tableList.init();