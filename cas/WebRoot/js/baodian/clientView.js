var client_data = {};
client_data.changeIndex = -1;
$(function() {
	client_reflesh();
});
/**
 * 刷新table
 */
function client_reflesh() {
	$.getJSON('client/list_js?_=' + Math.random(), function(data) {
		client_data.rows = data;
		var str = '<tr><th>id</th>' +
			'<th>name</th>' +
			'<th>domain</th>' +
			'<th>ip</th>' +
			'<th>change</th>' +
			'<th>delete</th>' +
			'<th>pubKey</th>' +
			'<th>modKey</th>' +
			'</tr>';
		$.each(data, function(k, v) {
			str = str + '<tr>' +
				'<td>' + v.id + '</td>' +
				'<td>' + v.name + '</td>' +
				'<td><a href="' + v.domain + '" target="_blank">' + v.domain + '</a></td>' +
				'<td>' + v.ip + '</td>' +
				'<td><a href="javascript:;" onclick="client_edit(' + k + ')">编辑</a></td>' +
				'<td><a href="javascript:;" onclick="client_delete(' + k + ')">删除</a></td>' +
				'<td>' + v.pubKey + '</td>' +
				'<td>' + v.modKey + '</td>' +
				'</tr>';
		});
		$('#client_table').html(str);
	});
}
/**
 * 进入编辑状态
 */
function client_edit(rowIndex) {
	client_data.changeIndex = rowIndex;
	$('#client_name').val(client_data.rows[rowIndex].name);
	$('#client_domain').val(client_data.rows[rowIndex].domain);
	$('#client_ip').val(client_data.rows[rowIndex].ip);
}
/**
 * 添加或修改
 */
function client_saveOrChange(isChange) {
	var params = {};
	var domain = $('#client_domain').val();
	if(domain.length<4 || domain.length>63) {
		client_showMess('请正确输入domain（4~63个字符）！');
		alert('请正确输入domain（4~63个字符）！');
		return;
	}
	params.domain = domain;
	params.ip = $('#client_ip').val();
	params.name = $('#client_name').val();
	
	var rowIndex = client_data.changeIndex;
	if(isChange) {//更新
		if(rowIndex < 0) {
			client_showMess('请先选择！');
			alert('请先选择！');
			return;
		}
		params.id = client_data.rows[rowIndex].id;
	} else {
		if(! confirm('确定要添加吗？')) {
			return;
		}
	}
	
	$.ajax({
		url: 'client/saveOrChange_js',
		cache: false,
		type: 'post',
		data: params,
		dataType: 'json',
		success: function(data) {
			if(data.status == 0) {
				client_reflesh();
				client_showMess(data.mess);
				if(! isChange) {//更新
					$('#client_name').val('');
					$('#client_domain').val('');
					$('#client_ip').val('');
				}
			} else {
				client_showMess(data.mess + '\r\n详细原因：' + data.detail + '\r\n提交时间：');
			}
		}
	});
}
/**
 * 更新RSA密钥
 */
function client_changeRsa() {
	var rowIndex = client_data.changeIndex;
	if(rowIndex < 0) {
		client_showMess('请先选择！');
		alert('请先选择！');
		return;
	}
	if(confirm('确定要更新域名《' + client_data.rows[rowIndex].domain + '》的RSA密钥吗？')) {
		$.getJSON('client/changeRsa_js?id=' + client_data.rows[rowIndex].id + '&_=' + Math.random(), function(data) {
			if(data.status == 0) {
				client_reflesh();
			}
			client_showMess(data.mess);
		});
	}
}
/**
 * 删除
 */
function client_delete(rowIndex) {
	if(confirm('确定要删除域名《' + client_data.rows[rowIndex].domain + '》吗？')) {
		$.getJSON('client/delete_js?id=' + client_data.rows[rowIndex].id + '&_=' + Math.random(), function(data) {
			if(data.status == 0) {
				client_reflesh();
			}
			client_showMess(data.mess);
		});
	}
}
/**
 * 显示信息
 */
function client_showMess(mess) {
	$('#client_mess').val(mess + new Date() + '\r\n' + $('#client_mess').val());
}