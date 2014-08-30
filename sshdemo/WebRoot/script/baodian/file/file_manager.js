var uploadbutton;
var dialog;
$(function() {
	$('#upload').click(function() {
		var dialog = null;
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
					formSubmit();
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
				if(data.error == 0) {
					dialog.remove();
					index_mess("上传成功！", 2);
					loadFile(currentDir);
				} else {
					var length = data.message.length;
					if(length > 8) {
						$("#afupmes").html(data.message.substring(0, 7) + "...");
						$("#afupmes").attr("title", data.message);
					} else {
						$("#afupmes").html(data.message);
					}
					index_mess(data.message, 1);
					$("#upinput").val("");
				}
				$("title").html("文件管理");
			}, afterError : function(str) {
				$("#loading").hide();
				$("#upinput").val("");
				$("#afupmes").html("上传失败！");
				index_mess("上传失败！", 1);
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
	});
	$('#mkdir').click(function() {
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
					mkdir();
				}
			},
			noBtn : {
				name : '取消',
				click : function(e) {
					dialog.remove();
				}
			}
		});
	});
	loadFile();
	$('#file_uploads').uploadify({
		'auto': false,
		'swf': 'script/uploadify/uploadify.swf',
		'uploader': 'file_upload_no.action',
		'debug': true,
		'fileSizeLimit': '100MB',
		'fileObjName': 'imgFile',
		//'method': 'get'
		onFallback: function() {
			alert('Flash不兼容，请更换浏览器或者更新flash插件！');
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
					//index_login();
				}
			}
		}, onUploadError: function(file, ecode, emsg, estring) {
			index_mess(file.name + " " + estring, 3);
		}, onQueueComplete: function(data) {
			if(file_uploadMess[1] != 0) {
				alert(file_uploadMess[1] + " 个文件上传失败：\n" + file_uploadMess[0]);
			}
		}
	});
});
var file_uploadMess = new Array();
/**
 * uploadify上传文件
 */
function file_upload() {
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
					//index_login();
				}
			}
		}
	});
}

function formSubmit() {
	$("#afupmes").html("");
	$("#loading").show();
	if($("#upinput").val() == "") {
		$("#loading").hide();
		$("#afupmes").html("未选中文件！");
		index_mess("未选中文件！", 1);
		return;
	}
	uploadbutton.submit();
}
function mkdir() {
	index_mess("新建中...", 0);
	$("#afupmes").html("");
	$("#loading").show();
	var str = $("#upinput").val();
	if(str == "") {
		$("#loading").hide();
		$("#afupmes").html("请输入文件名");
		index_mess("请输入文件名", 1);
		return;
	} else if(str.indexOf("'") != -1) {
		$("#loading").hide();
		$("#afupmes").html("名称不能包...");
		$("#afupmes").attr("title", "名称不能包含 '\/:*?\"<>|符号");
		index_mess("名称不能包含 '\/:*?\"<>|符号", 1);
		return;
	}
	$.post("file_mkdir.action", {"dir": currentDir + str}, function(data) {
		if(data.error == 1) {
			var length = data.message.length;
			if(length > 6) {
				$("#afupmes").html(data.message.substring(0, 5) + "...");
				$("#afupmes").attr("title", data.message);
			} else {
				$("#afupmes").html(data.message);
			}
			index_mess(data.message, 1);
		} else {
			loadFile(currentDir);
			dialog.remove();
			index_mess("新建成功！", 2);
		}
		$("#loading").hide();
	}, "json");
}
var parentDir;
var currentDir = "";
function loadFile(path) {
	$.post("file_message_no.action", {"path": path, "token":  Math.random()}, function(files) {
		if(files.message != null) {
			$("#f_tbody").prepend("<tr class=\"n_tr\"><td class=\"fmess\">" + files.message + "</td></tr>");
			return;
		}
		parentDir = files.moveup_dir_path;
		currentDir = files.current_dir_path;
		var length = files.current_url.length;
		if(length > 35) {
			$("#currentDir").html("..." + files.current_url.substring(length-35,length));
		} else {
			$("#currentDir").html(files.current_url);
		}
		$("#currentDir").attr("title", files.current_url);
		$("#f_tbody").empty();
		if(files.rows.length == 0) {
			$("#f_tbody").prepend("<tr class=\"n_tr\"><td class=\"fmess\">文件夹为空</td></tr>");
			return;
		}
		var str = "";
		$.each(files.rows, function(i, val) {
			if(val.is_dir) {
				if(val.has_file) str = "~";
				else str = "空";
				$("#f_tbody").append('<tr class="n_tr">' +
					'<td class="ficon"><img src="script/kindeditor/plugins/filemanager/images/folder-16.gif"></td>' +
					'<td class="fname"><a href="javascript:;" ' +
						'onclick="loadFile(\'' + files.current_dir_path + val.filename + '/\')">' +
						val.filename +'</a>' + '</td>'+
					'<td class="faction">' +
						'<a href="javascript:;" onclick="loadFile(\'' + files.current_dir_path + val.filename + '/\')">打开</a>&nbsp;&nbsp;' +
						'<a href="javascript:;" onclick="change(\'' + val.filename + '/\')">重命名</a>&nbsp;&nbsp;' +
						'<a href="javascript:;" onclick="rmdir(\'' + val.filename + '\')">删除</a></td>' +
					'<td class="ftime">' + val.datetime + '</td>' +
					'<td class="ftype">文件夹</td>' +
					'<td class="fsize">' + str +'</td></tr>');
			} else {
				var image = "file";
				if(val.is_photo) {
					image = "image";
					str = "预览";
				} else str="下载";
				var fsize = val.filesize;
				if(fsize < 1000)
					fsize = fsize + " B";
				else
					if(fsize > 1000000)
						fsize = (fsize/1000000).toFixed(1) + " MB";
					else
						fsize = (fsize/1000).toFixed(1) + " KB";
				$("#f_tbody").append('<tr class="n_tr">' +
					'<td class="ficon"><img src="script/kindeditor/plugins/filemanager/images/' + image + '-16.gif"></td>' +
					'<td class="fname"><a href="' + files.current_url + val.filename +
						'" target="_blank">'+ val.filename + '</a></td>' +
					'<td class="faction">' +
						'<a href="' + files.current_url + val.filename + '" target="_blank">' + str + '</a>&nbsp;&nbsp;' +
						'<a href="javascript:;" onclick="change(\'' + val.filename.replace(/'/g, "\\\'") + '/\')">重命名</a>&nbsp;&nbsp;' +
						'<a href="javascript:;" onclick="rmfile(\'' + val.filename.replace(/'/g, "\\\'") + '\')">删除</a></td>' +
					'<td class="ftime">' + val.datetime + '</td>' +
					'<td class="ftype">' + val.filetype + '</td>' +
					'<td class="fsize">' + fsize + '</td></tr>');
			}
		});
	}, "json");
}
function rmfile(fname) {
	if(confirm("确定要删除文件《" + fname + "》吗？"))
		remove(fname);
}
function rmdir(fname) {
	if(confirm("确定要删除文件夹《" + fname + "》吗？"))
		remove(fname);
}
function remove(fname) {
	index_mess("删除中...", 0);
	$.post("file_remove.action", {"dir": currentDir + fname}, function(data) {
		if(data.error == 1) {
			index_mess(data.message, 1);
		} else {
			index_mess("删除成功！", 2);
			loadFile(currentDir);
		}
	}, "json");
}
/**
 * 打开更新名称窗口
 */
function change(oldname) {
	//去掉后面的'/'
	oldname = oldname.substring(0, oldname.length-1);
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
	if(str.indexOf("'") != -1) {
		$("#loading").hide();
		$("#afupmes").html("名称不能包...");
		$("#afupmes").attr("title", "名称不能包含 '\/:*?\"<>|符号");
		index_mess("名称不能包含 '\/:*?\"<>|符号", 1);
		return;
	}
	$.post("file_change_no.action", {"dir": currentDir + str, "path": currentDir + oldname}, function(data) {
		if(data.error == 1) {
			var length = data.message.length;
			if(length > 6) {
				$("#afupmes").html(data.message.substring(0, 5) + "...");
				$("#afupmes").attr("title", data.message);
			} else {
				$("#afupmes").html(data.message);
			}
			index_mess(data.message, 1);
		} else {
			loadFile(currentDir);
			dialog.remove();
			index_mess("更改成功！", 2);
		}
		$("#loading").hide();
	}, "json");
}