var parentDir = "";
var currentDir = "";
$(function() {
	$('#index_table').datagrid({
	    url: 'file_message_js.action?_=' + Math.random(),
	    fitColumns: true,//自动调整单元格宽度
	    rownumbers: true,//显示行号
	    //singleSelect: true,//单选
	    toolbar: '#index_toolbar',
	    columns: [[
	        {field: 'ck', checkbox: true},
	        {field: 'image', title: '', align: 'center', width: 6,
	        	formatter: function(value, row, index){
					return file_type(row, true);
				}},
	        {field: 'filename', title: '名称', width: 52,
				formatter: function(value, row, index){
					if (row.is_dir) {
						return '<a class="fileName" href="javascript:;" onclick="loadFile(0, ' + index + ')">' + value + '</a>';
					} else {
						return '<a class="fileName" href="javascript:;" onclick="downLoad(' + index + ')">' + value + '</a>';
					}
				}},
	        {field: 'action', title: '操作', width: 10,
	        	formatter: function(fsize, row, index){
	        		var str = "";
					if (! row.is_dir) {
						str = '<a href="javascript:;" onclick="downLoad(' + index + ')" title="下载"><img style="border:none" src="script/easyui/themes/icons/download.png"></a> ';
					}
					return str + '<a href="javascript:;" onclick="change(' + index + ')" title="重命名"><img style="border:none" src="script/easyui/themes/icons/pencil.png"></a> ' +
					 '<a href="javascript:;" onclick="rmfile(' + index + ')" title="删除"><img style="border:none" src="script/easyui/themes/icons/edit_remove.png"></a>';
				}},
	        {field: 'filesize', title: '大小', align: 'center', width: 12,
	        	formatter: function(fsize, row, index){
					if (row.is_dir){
						if(row.has_file) {
							return "~";
						} else {
							return "空";
						}
					} else {
						return formatFileSize(fsize);
					}
				}},
	        {field: 'datetime', title:'时间', align: 'center', width: 20}
	    ]], onLoadSuccess: function(data) {
	    	$('#index_path').html(data.current_url);
	    	$('#index_fileSize').html(formatFileSize(data.dirSize));
	    	parentDir = data.moveup_dir_path;
			currentDir = data.current_dir_path;
			index_flag[0] = true;
			file_appendTotree(data);
	    }
	});
	loadIndex();
	//按下enter键登录
	$("#j_username").keydown(function(e) {
		if(e.keyCode==13) index_fmlg();
	});
	$("#j_password").keydown(function(e) {
		if(e.keyCode==13) index_fmlg();
	});
	//登陆框
	$('#index_dd').dialog({
		closed: true,
		title: '&nbsp;登录系统',
		buttons:[{
			text:'登录',
			iconCls:'icon-ok',
			handler: index_fmlg,
		},{
			text:'&nbsp;取消&nbsp;',
			handler:function(){
				$('#index_dd').dialog('close');
			}
		}]
	});
	//定时访问，防止掉线
	setInterval(function() {
		$.ajax({
	    	url: 'file_session_js.action',
			cache: false,
			dataType: 'json',
			success: function(data) {
			}
		});
	}, 1080000);
});
/**
 * 0-是否已经载入datagrid 1-是否支持flash
 */
var index_flag = [false, true];
function file_resize(width, height){
	if(index_flag[0]) {
		$('#index_table').datagrid('resize');
	}
}
/**
 * 格式化输出文件大小
 */
function formatFileSize(fsize) {
	if(fsize < 1024)
		fsize = fsize + " B";
	else
		if(fsize >= 1073741824)
			fsize = (fsize/1073741824).toFixed(2) + " GB";
		else if(fsize >= 1048576)
			fsize = (fsize/1048576).toFixed(1) + " MB";
		else
			fsize = (fsize/1024).toFixed(0) + " KB";
	return fsize;
}
/**
 * 重新载入目录
 * @param dir 0-索引目录 1-根目录 2-当前目录 3-上一目录 4-指导目录
 * @param index 目录名称的索引
 */
function loadFile(dir, index) {
	var path = "";
	switch(dir) {
	case 0:
		path = currentDir +  $('#index_table').datagrid('getRows')[index]["filename"] + "/";
		break;
	case 2:
		path = currentDir;
		break;
	case 3:
		path = parentDir;
		break;
	case 4:
		path = index;
		break;
	}
	$('#index_table').datagrid('options').queryParams = {"path": path};
	$('#index_table').datagrid('load');
}
/**
 * 下载文件
 * @param index 目录名称的索引
 * @param fullpath 完整路径
 */
function downLoad(index, fullpath) {
	var file = "";
	if(fullpath != null) {
		file = fullpath;
	} else {
		file = currentDir + $('#index_table').datagrid('getRows')[index]["filename"];
	}
	$('#file_path').val(encodeURI(file));
	$('#file_form').submit();
}
var dialog;
var uploadbutton;
/**
 * 上传文件到当前目录
 */
function upLoad() {
	//初始化uploadify
	$('#file_uploads').uploadify({
		'auto': false,
		'swf': 'script/uploadify/uploadify.swf',
		'uploader': 'file_upload_no.action',
		'fileSizeLimit': index_datas.fileSizeLimit,
		'fileObjName': 'imgFile',
		'width': 108,
		//'progressData' : 'speed',
		//'debug': true,
		//'method': 'get'
		onFallback: function() {
			//alert('Flash不兼容，请更换浏览器或者更新flash插件！');
			index_flag[1] = false;
		/*}, onUploadStart : function(file) {
            alert('Starting to upload ' + file.name);*/
        }, onUploadSuccess: function(file, data) {
			data = $.parseJSON(data);
			//index_each(file);
			if(data.status == 0) {
				index_mess(file.name + "上传成功！", 4);
			} else {
				file_uploadMess[0] = file_uploadMess[0] + file.name + " => " + data.mess + "\n";
				file_uploadMess[1] ++;
				if(data.login == false) {
					index_mess(data.mess, 3);
					$('#file_uploads').uploadify('stop');
					index_login();
				}
			}
		}, onUploadError: function(file, ecode, emsg, estring) {
			index_mess(file.name + " " + estring, 3);
		}, onQueueComplete: function(data) {
			if(file_uploadMess[1] != 0) {
				alert(file_uploadMess[1] + " 个文件上传失败：\n" + file_uploadMess[0]);
			}
			loadFile(2);
		}
	});
	if(index_flag[1]) {//支持uploadify
		$('#file_uploads_dlg').dialog('open');
		return;
	}
	dialog = null;
	dialog = KindEditor.dialog({
		title : '文件上传',
		width: '342px',
		body : '<div class="fileupload"><p>文件将上传到当前文件夹&nbsp;<span id="afupmes"></span>' +
				'<img src="images/loading.gif" id="loading" style="display:none;"></p>' +
				'<input class="ke-input-text" id="upinput" type="test" value="" readonly="readonly" />&nbsp;&nbsp;' +
				'<input type="button" id="uploadButton" value="浏览"/></div>',
		closeBtn : {
			name : '关闭',
			click : function(e) {
				dialog.remove();
			}
		},
		yesBtn : {
			name : '上传',
			click : function(e) {
				$("#afupmes").html("");
				$("#loading").show();
				if($("#upinput").val() == "") {
					$("#loading").hide();
					$("#afupmes").html("未选中文件！");
					index_mess("未选中文件！", 3);
					return;
				}
				uploadbutton.submit();
			}
		},
		noBtn : {
			name : '取消',
			click : function(e) {
				dialog.remove();
			}
		}
	});
	uploadbutton = KindEditor.uploadbutton({
		button : KindEditor('#uploadButton')[0],
		fieldName : 'imgFile',
		url : 'file_upload_no.action',
		afterUpload : function(data) {
			$("#loading").hide();
			if(data.status == 0) {
				dialog.remove();
				index_mess("上传成功！", 4);
				loadFile(2);
			} else {
				index_mess(data.mess, 3);
				if(data.login == false) {
					dialog.remove();
					index_login();
				}
				var length = data.mess.length;
				if(length > 8) {
					$("#afupmes").html(data.mess.substring(0, 7) + "...");
					$("#afupmes").attr("title", data.mess);
				} else {
					$("#afupmes").html(data.mess);
				}
				$("#upinput").val("");
			}
			$("title").html("文件管理");
		}, afterError : function(str) {
			$("#loading").hide();
			$("#upinput").val("");
			$("#afupmes").html("上传失败！");
			index_mess("上传失败！", 3);
			$("title").html("文件管理");
		}
	});
	//将dir参数加到form表单中，目的防止目录名中文问题
	$("form.ke-form").append("<input type='hidden' name='dir' value='" +
			currentDir + "'>");
	uploadbutton.fileBox.change(function(e) {
		$("#upinput").val(uploadbutton.fileBox[0].value);
		$("#afupmes").html("");
	});
}
var file_uploadMess = new Array();
/**
 * uploadify上传文件
 */
function file_upload() {
	/*alert($('#file_uploads').uploadify('settings','fileSizeLimit'));
	return;*/
	if($('#file_uploads-queue >div').length == 0) {
		index_mess("请先选择文件！", 3);
		return;
	}
	$.ajax({
		url: 'file_session_js.action',
		async: false,
		cache: false,
		dataType: 'json',
		success: function(data) {
			if(data.status == 0) {
				index_mess("开始上传...", 4);
				file_uploadMess[0] = "";
				file_uploadMess[1] = 0;
				$('#file_uploads').uploadify('settings', 'formData', {
					'json': data.session,
					'dir': currentDir
				});
				$('#file_uploads').uploadify('upload','*');
			} else {
				index_mess(data.mess, 3);
				if(data.login == false) {
					index_login();
				}
			}
		}
	});
}
/**
 * 打开更新名称窗口
 * @param index
 */
function change(index) {
	var oldname = $('#index_table').datagrid('getRows')[index]["filename"];
	dialog = KindEditor.dialog({
		title : '重命名',
		width: '280px',
		body : '<div class="fileupload"><p>请输入新的名称&nbsp;<span id="afupmes"></span>' +
				'<img src="images/loading.gif" id="loading" style="display:none;"></p>' +
				'<input class="ke-input-text" id="upinput" type="test" value="' + oldname + '" /></div>',
		closeBtn : {
			name : '关闭',
			click : function(e) {
				dialog.remove();
			}
		}, yesBtn : {
			name : '更改',
			click : function(e) {
				changeName(oldname);
			}
		}, noBtn : {
			name : '取消',
			click : function(e) {
				dialog.remove();
			}
		}
	});
	$("#upinput").keydown(function(e) {
		if(e.keyCode==13) {
			changeName(oldname);
		}
	});
}
/**
 * 更改名称
 */
function changeName(oldname) {
	index_mess("更改中...", 0);
	$("#afupmes").html("");
	$("#loading").show();
	var str = $("#upinput").val();
	if(str == "") {
		$("#loading").hide();
		$("#afupmes").html("请输入文件名");
		index_mess("请输入文件名", 1);
		return;
	}
	if(str == oldname) {
		$("#loading").hide();
		$("#afupmes").html("未更改！");
		index_mess("未更改！", 2);
		return;
	}
	if(str.match(/\/|\\|:|\*|\?|"|<|>|\|/g) != null) {
		$("#loading").hide();
		$("#afupmes").html("名称不能包...");
		$("#afupmes").attr("title", "名称不能包含 \/:*?\"<>|符号");
		index_mess("名称不能包含 \/:*?\"<>|符号", 1);
		return;
	}
	if(str.length > 50) {
		$("#loading").hide();
		$("#afupmes").html("名称不能超...");
		$("#afupmes").attr("title", "名称不能超过50个字符！");
		index_mess("名称不能超过50个字符！", 1);
		return;
	}
	$.post("file_change_no.action", {"dir": currentDir + str, "path": currentDir + oldname}, function(data) {
		if(data.status == 1) {
			var length = data.mess.length;
			if(length > 6) {
				$("#afupmes").html(data.mess.substring(0, 5) + "...");
				$("#afupmes").attr("title", data.mess);
			} else {
				$("#afupmes").html(data.mess);
			}
			index_mess(data.mess, 1);
			if(data.login == false) {
				dialog.remove();
				index_login();
			}
		} else {
			loadFile(2);
			dialog.remove();
			index_mess("更改成功！", 2);
		}
		$("#loading").hide();
	}, "json");
}
/**
 * 打开创建文件夹窗口
 */
function mkdir() {
	dialog = KindEditor.dialog({
		title : '新建文件夹',
		width: '280px',
		body : '<div class="fileupload"><p>请输入文件夹名称&nbsp;<span id="afupmes"></span>' +
				'<img src="images/loading.gif" id="loading" style="display:none;"></p>' +
				'<input class="ke-input-text" id="upinput" type="test" value="新建文件夹" /></div>',
		closeBtn : {
			name : '关闭',
			click : function(e) {
				dialog.remove();
			}
		},
		yesBtn : {
			name : '创建',
			click : function(e) {
				file_mkdir();
			}
		},
		noBtn : {
			name : '取消',
			click : function(e) {
				dialog.remove();
			}
		}
	});
	$("#upinput").keydown(function(e) {
		if(e.keyCode==13) {
			file_mkdir();
		}
	});
}
/**
 * 创建文件夹
 */
function file_mkdir() {
	index_mess("新建中...", 0);
	$("#afupmes").html("");
	$("#loading").show();
	var str = $("#upinput").val();
	if(str == "") {
		$("#loading").hide();
		$("#afupmes").html("请输入文件名");
		index_mess("请输入文件名", 1);
		return;
	} else if(str.match(/\/|\\|:|\*|\?|"|<|>|\|/g) != null) {
		$("#loading").hide();
		$("#afupmes").html("名称不能包...");
		$("#afupmes").attr("title", "名称不能包含 \/:*?\"<>|符号");
		index_mess("名称不能包含 \/:*?\"<>|符号", 1);
		return;
	}
	$.post("file_mkdir.action", {"dir": currentDir + str}, function(data) {
		if(data.status == 1) {
			var length = data.mess.length;
			if(length > 6) {
				$("#afupmes").html(data.mess.substring(0, 5) + "...");
				$("#afupmes").attr("title", data.mess);
			} else {
				$("#afupmes").html(data.mess);
			}
			index_mess(data.mess, 1);
			if(data.login == false) {
				index_login();
				dialog.remove();
			}
		} else {
			loadFile(2);
			dialog.remove();
			index_mess("新建成功！", 2);
		}
		$("#loading").hide();
	}, "json");
}
/**
 * 删除文件或者目录
 * @param index 目录名称的索引
 */
function rmfile(index) {
	var row = $('#index_table').datagrid('getRows')[index];
	var fname = row.filename;
	if(confirm("确定要删除" + (row.is_dir?"目录":"文件") + "《" + fname + "》吗？")) {
		index_mess("删除中...", 0);
		$.post("file_remove.action", {"dir": currentDir + fname}, function(data) {
			if(data.status == 1) {
				index_mess(data.mess, 1);
				if(data.login == false) {
					index_login();
				}
			} else {
				index_mess("删除成功！", 2);
				loadFile(2);
			}
		}, "json");
	}
}
/**
 * datagrid加载后把内容加到目录树下
 * @param data
 */
function file_appendTotree(data) {
	if(data.current_dir_path == "") {//根目录
		$('#file_tree').tree({
			animate: true,
			data: gridToTree(data.rows),
			onDblClick: function(node) {
				if(node.attributes.is_dir) {
					loadFile(4, node.attributes.fullpath + "/");
				} else {
					downLoad(-1, node.attributes.fullpath);
				}
			}
		});
		return;
	}
	var filePaths = data.current_dir_path.split("/");
	var nodes = $('#file_tree').tree('getRoots');
	var node = null;
	$.each(filePaths, function(k, v) {
		if(v != "") {
			node = file_findNode(nodes, v);
			if(node != null) {
				nodes = $('#file_tree').tree('getChildren', node.target);
				if(nodes == null) {
					return false;
				}
			} else {
				return false;
			}
		}
	});
	if(node == null) {
		return;
	}
	$('#file_tree').tree('select', node.target);
	//先删除原来目录下的文件
	nodes = $('#file_tree').tree('getChildren', node.target);
	if(nodes != null) {
		$.each(nodes, function(k, v) {
			$('#file_tree').tree('remove', v.target);
		});
	}
	//然后再添加
	$('#file_tree').tree('append', {
		parent: node.target,
		data: gridToTree(data.rows)
	});
}
/**
 *  根据名字在查找目录
 */
function file_findNode(nodes, name) {
	var node = null;
	$.each(nodes, function(k, v) {
		if(v.text == name) {
			node = v;
			return false;	
		}
	});
	return node;
}
/**
 * 根据扩展名显示文件图标
 * @param file 文件信息
 * @param full 是否返回完整div
 */
function file_type(file, full) {
	if (file.is_dir){
		if(full) {
			return '<span class="img-file ico-file ico-file-dir"></span>'; 
		} else {
			return "ico-file ico-file-dir";
		}
	} else {
		var str = file.filetype;
		if(str.match(/^[0-9]+$/) || str=='dir' || str==file.filename) {
			str = "ico-file";
		} else {
			str = "ico-file ico-file-" + str;
		}
		return full==true? '<span class="img-file ' + str + '"></span>' : str;
	}
}
/**
 * 将数据从datagrid转换成tree
 * @param rows
 */
function gridToTree(rows) {
	var trees = new Array();
	$.each(rows, function(k, row) {
		trees.push({
			"text": row.filename,
			"attributes": row,
			"iconCls": file_type(row)
		});
	});
	return trees;
}
/**
 * 打开移动文件对话框
 */
function file_openMove() {
	$('#file_move_tree').tree({
		url: "file_message_js.action",
		loadFilter: function(data, parent) {
			if(data.status == 1) {
				index_mess(data.mess, 3);
				if(data.login == false) {
					$('#file_move_dlg').dialog('close');
					index_login();
				}
				return false;
			}
			var rows = new Array();
			$.each(data.rows, function(k, v) {
				if(v.is_dir) {
					rows.push({
						"id": v.fullpath,
						"text": v.filename,
						"state": "closed"
					});
				}
			});
			if(data.current_dir_path == "") {
				return [{
					"id": "",
					"text": "根目录",
					children: rows
				}];
			}
			return rows;
		}
	});
	$('#file_move_dlg').dialog('open');
}
/**
 * 移动文件
 */
function file_move_saveDir() {
	var dir = $('#file_move_tree').tree('getSelected');
	if(dir == null) {
		index_mess("请先选择要移动到目录！", 3);
		return;
	}
	var fs = $('#index_table').datagrid('getSelections');
	if(fs.length == 0) {
		index_mess("请先选择要移动的文档或目录！", 3);
		return;
	}
	var files = new Array();
	var isSame = false;
	$.each(fs, function(k, file) {
		files.push(file.filename);
		if(file.fullpath == dir.id) {
			isSame = true;
			return false;
		}
	});
	if(isSame) {
		index_mess("要移动的文件或目录和目标相同，请先将其移除！", 3);
		return;
	}
	if(dir.id+"/"==currentDir || (dir.id=="" && currentDir=="")) {
		index_mess("目标目录相同，请选择其他目录！", 3);
		return;
	}
	var params = {"dir": dir.id==""? "": (dir.id+"/"), "paths": files, "path": currentDir};
	index_mess("移动中...", 0);
	$.post("file_move_no.action", $.param(params, true), function(data) {
		if(data.status == 1) {
			index_mess(data.mess, 1);
			if(data.login == false) {
				$('#file_move_dlg').dialog('close');
				index_login();
				return;
			}
		} else {
			index_mess(data.mess, 2);
			$('#file_move_dlg').dialog('close');
		}
		//loadFile(4, dir.id);
		loadFile(2);
	}, "json");

}
/**------------------------------------------------------------------------------*/
/**
 * 首页布局
 * @param reload true表示重新加载，false表示展示
 */
function loadIndex(reload) {
	if(index_user.id == "") {
		$("#index_user").html("未登录&nbsp;");
	} else {
		$("#index_user").html("欢迎 " + index_user.name + " (" + index_user.depm + ")&ensp;");
	}
}
/**
 * 打开登录系统框
 */
function index_login() {
	$('#index_dd').dialog('open');
	$("#j_username").val("").focus();
	$("#j_password").val("");
}
/**
 * 登录系统
 */
function index_fmlg() {
	if($("#j_username").val() == "") {
		index_mess("用户名为空", 3);
		$("#j_username").focus();
		return;
	}
	if($("#j_password").val() == "") {
		index_mess("密码为空", 3);
		$("#j_password").focus();
		return;
	}
	index_mess("登录中...", 0);
	serverLogin();
}
/**
 * 服务器端登录，先取得公钥，然后再验证账号密码
 */
function serverLogin() {
	var params = {};
	params.j_ajax = true;
	params.renew = true;//强制为重新登录
	params.service = index_clientUrl;
	$.ajax({
		url: index_serverUrl,
		data: params,
		dataType: 'jsonp',
		success: function(data) {
			params = {};
			params.execution = data.execution;
			params.lt = data.lt;
			//params.submit = "登录";
			params._eventId = 'submit';
			params.j_ajax = true;
			params.renew = true;
			//rsa加密
			var rsa = new RSAKey();
			rsa.setPublic(data.rsa_modKey, data.rsa_pubKey);
			var username = $('#j_username').val();
			username = username.toLowerCase();
			var password = $('#j_password').val();
			password = $.encoding.digests.hexSha1Str(username + password);
			params.username = username;
			params.password = rsa.encrypt(password);
			$.ajax({
				url: index_serverUrl,
				data: params,
				dataType: 'jsonp',
				success: function(data) {
					if(data.status == 1) {
						index_mess(data.mess, 1);
						return;
					}
					//服务端登录成功，然后到客户端进行验证
					clientCheck(data.clientUrl);
				}
			});
		}
	});
}
/**
 * 客户端验证登录
 */
function clientCheck(clientUrl, user, needAdd) {
	$.ajax({
		url: clientUrl + '&j_ajax=true',
		data: user,
		type: 'post',
		async: false,
		cache: false,
		dataType: 'json',
		success: function(data) {
			if(data.status == 1) {
				index_mess(data.mess, 1);
				return;
			}
			if(needAdd) {//注册
				if(data.login == false) {
					index_mess(data.mess, 1);
					return;
				}
			} else {//登录
				if(data.login == false) {
					index_mess('请先补充用户信息！如果页面无跳转，请刷新此页面！', 1);
					$('#index_dd').dialog('close');
					window.open(index_noUserUrl.substring(1));
					return;
				}
			}
			index_mess('登录成功，欢迎 ' + data.name, 2);
			$('#index_dd').dialog('close');
			index_user.id = data.id;
			index_user.name = data.name;
			index_user.account = data.account;
			index_user.depm = data.depm;
			loadIndex(true);
			loadFile(1);
		}
	});
}
/**
 * 弹出更改密码框
 */
function index_changePS() {
	$("#index_pw_span").html("");
	$('#index_fm div input').val("");
	//使用easyUI的清除方法会出现提示框不消失的情况
	//$('#index_fm').form('clear');
	$('#index_ps').dialog('open');
}
/**
 * 更改密码
 */
function index_changePW() {
	if(!$("#index_fm").form('validate')) {
		return;
	}
	index_mess("更改中...", 0);
	//0旧密码 1新密码 2确定密码
	var params = $("#index_fm").serializeArray();
	if(params[1].value != "" && (params[1].value != params[2].value)) {
		index_mess("两次密码不相同！", 1);
		return;
	}
	//置为空，防止密码明文传送
	params[2].value = '';
	if(index_user.id == "") {
		index_mess("请先登录", 2);
		index_login();
		return;
	}
	//获取RSA公钥
	var isError = false;
	$.ajax({
		url: 'util_rsaKey_no.action',
		cache: false,
		async: false,
		type: 'get',
		dataType: 'json',
		success: function(data) {
			if(data.status == 1) {
				index_mess(data.mess, 1);
				if(data.login == false) {
					index_login();
				}
				isError = true;
			}
			//rsa加密
			var rsa = new RSAKey();
			rsa.setPublic(data.rsa_modKey, data.rsa_pubKey);
			//旧密码
			var password = $.encoding.digests.hexSha1Str(index_user.account + params[0].value);
			params[0].value = rsa.encrypt(password);
			//新密码
			password = $.encoding.digests.hexSha1Str(index_user.account + params[1].value);
			params[1].value = rsa.encrypt(password);
		}
	});
	if(isError) {
		return;
	}
	$.post("user_changePW_js.action", params, function(data) {
		if(data.status == 0) {
			index_mess("更改成功", 2);
			$('#index_ps').dialog('close');
		} else {
			index_mess(data.mess, 1);
			if(data.login == false) {
				index_login();
			}
			if(data.password == false) {
				$("#index_pw_span").html(data.mess);
			}
		}
		
	}, "json");
}
/**
 * 将ztree转换为easyui树
 * @param ztree [{id,name,pId}]
 * @returns etree [{id,text,children:[{..}]}]
 */
function index_changeTree(ztree, data) {
	if(ztree.length == 0)
		return [];
	var etree = [];
	$.each(ztree, function(k, v) {
		if(v.pId == null) {
			etree.push(index_node(v, data));
		}
	});
	index_createTree(etree, ztree, data);
	return etree;
}
/**
 * 生成一个节点
 * @param data {state:"closed"} 为null时从znode取，否则直接用
 */
function index_node(znode, data) {
	var node = {
		id: znode.id,
		text: znode.name
	};
	if(data != null) {
		$.each(data, function(k, v) {
			if(v == null) {
				node[k] = znode.k;
			} else {
				node[k] = v;
			}
		});
	}
	return node;
}
/**
 * 递归从ztree树取数据，然后构建easyui树
 */
function index_createTree(etree, ztree, data) {
	$.each(etree, function(ek, ev) {
		$.each(ztree, function(zk, zv) {
			if(zv.pId == ev.id) {
				if(ev.children == null) {
					ev.children = [index_node(zv, data)];
				} else {
					ev.children.push(index_node(zv, data));
				}
			}
		});
		if(ev.children != null) {
			index_createTree(ev.children, ztree, data);
		}
	});
}