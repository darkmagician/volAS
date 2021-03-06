<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta charset="UTF-8">
<title>SMART NETWORK APPLICATION ENGINE</title>
<link rel="stylesheet" type="text/css"
	href="./static/themes/gray/easyui.css">
<link rel="stylesheet" type="text/css" href="./static/themes/icon.css">
<link rel="stylesheet" type="text/css" href="./static/style.css">
<link rel="shortcut icon" href="./static/images/favicon.ico"
	type="image/x-icon" />
<script type="text/javascript" src="./static/jquery.min.js"></script>
<script type="text/javascript" src="./static/jquery.easyui.min.js"></script>
<script type="text/javascript" src="./static/ace/ace.js" charset="utf-8"></script>

</head>
<body class="easyui-layout">
	<!-- ************************************ Delete Window Start************************************ -->
	<div id="confirm" class="easyui-dialog"
		style="width: 600px; height: 200px; padding: 10px 20px" closed="true"
		buttons="#confirmButtons">
		<div class="ftitle">是否确定该操作？</div>
		<label id="confirmInfo"  style="color:red;"></label>
		<form id="confirmForm" method="post">
			<input name="id" type="hidden">
		</form>
	</div>
	<div id="confirmButtons">
		<a href="javascript:void(0)" class="easyui-linkbutton c6"
			iconCls="icon-ok" onclick="confirmAction()" style="width: 90px">确定</a>
		<a href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-cancel"
			onclick="javascript:$('#confirm').dialog('close')"
			style="width: 90px">取消</a>
	</div>

	<!-- ************************************ Password Window Start************************************ -->
	<div id="passdlg" class="easyui-dialog"
		style="width: 600px; height: 300px; padding: 10px 20px" closed="true"
		buttons="#passButtons">
		<div class="ftitle">修改密码</div>
		<label id="passInfo"  style="color:red;"></label>
		<form id="passForm" method="post">
			<div class="fitem">
				<label>旧密码:</label> <input name="oldpass" class="easyui-textbox" type="password"
					required="true">
			</div>
			<div class="fitem">
				<label>新密码:</label> <input name="newpass" class="easyui-textbox" type="password"
					required="true">
			</div>
			<div class="fitem">
				<label>确认密码:</label> <input name="newpass2" class="easyui-textbox" type="password"
					required="true">
			</div>			
		</form>
	</div>
	<div id="passButtons">
		<a href="javascript:void(0)" class="easyui-linkbutton c6"
			iconCls="icon-ok" onclick="updatePassword()" style="width: 90px">确定</a>
		<a href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-cancel"
			onclick="javascript:$('#passdlg').dialog('close')"
			style="width: 90px">取消</a>
	</div>
	
	<!-- ************************************ Delete Window End************************************ -->
<s:if test="%{operator.tenantId==0}">	

	<!-- ************************************ Tenant Editor Start************************************ -->
	<div id="tenantEditor" class="easyui-dialog"
		style="width: 600px; height: 280px; padding: 10px 20px" closed="true"
		buttons="#tenantButtons">
		<div class="ftitle">租户信息</div>
		<label id="tenantEditorInfo"  style="color:red;"></label>
		<form id="tenantForm" method="post">
			<div class="fitem">
				<label>名字:</label> <input name="name" class="easyui-textbox"
					required="true">
			</div>
			<div class="fitem">
				<label>描述:</label> <input name="description" class="easyui-textbox">
			</div>
			<div class="fitem">
				<label>周期:</label> 
				<select name="cycleType"
					class="easyui-combobox" required="true">
					<option value="0">每月</option>
				</select>	
			</div>
			<input name="id" type="hidden"> <input name="creationTime"
				type="hidden"> <input name="updateTime" type="hidden">

		</form>
	</div>
	<div id="tenantButtons">
		<a href="javascript:void(0)" class="easyui-linkbutton c6"
			iconCls="icon-ok" onclick="saveTenant()" style="width: 90px">保存</a> <a
			href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-cancel"
			onclick="javascript:$('#tenantEditor').dialog('close')"
			style="width: 90px">取消</a>
	</div>


	<!-- ************************************ Tenant Editor End************************************ -->
</s:if>
	
	<!-- ************************************ Operator Editor Start************************************ -->
	<div id="operatorEditor" class="easyui-dialog"
		style="width: 600px; height: 350px; padding: 10px 20px" closed="true"
		buttons="#operatorButtons">
		<div class="ftitle">管理员信息</div>
		<label id="operatorEditorInfo"  style="color:red;"></label>
		<form id="operatorForm" method="post">
			<div class="fitem">
				<label>名字:</label> <input id="operatorName" name="name" class="easyui-textbox"
					required="true">
			</div>
			<div class="fitem">
				<label>邮箱:</label> <input name="email" class="easyui-textbox" validType="email"
					required="true">
			</div>	
			<div class="fitem">
				<label>手机:</label> <input name="phone" class="easyui-textbox"
					required="true">
			</div>				
			<div class="fitem">
				<label>所属租户:</label> <input id="operatorTenantId" name="tenantId"
					class="easyui-combobox"
					data-options="valueField:'id',textField:'text'">
			</div>
			<div class="fitem">
				<label>描述:</label> <input name="description" class="easyui-textbox">
			</div>			
			<input name="id" type="hidden"> <input name="creationTime"
				type="hidden"> <input name="updateTime" type="hidden">

		</form>
	</div>
	<div id="operatorButtons">
		<a href="javascript:void(0)" class="easyui-linkbutton c6"
			iconCls="icon-ok" onclick="saveOperator()" style="width: 90px">保存</a>
		<a href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-cancel"
			onclick="javascript:$('#operatorEditor').dialog('close')"
			style="width: 90px">取消</a>
	</div>
	<!-- ************************************ Operator Editor End************************************ -->

	<s:include value="promotion.jsp" />
	
	<!-- ************************************ Promotion History Toolbar************************************ -->
	 <div id="historyPromotionTB" style="padding:2px 5px;">
		时间从: <input id="fromDate" class="easyui-datebox" style="width:110px">
		到: <input id="toDate" class="easyui-datetimebox" style="width:110px">
		名字:<input id="searchName" class="easyui-textbox"></input>
		<a href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-search"
			onclick="searchPromotionHistory()"
			style="width: 90px">查找</a>		
	</div>
	 <div id="bonusTB" style="padding:2px 5px;">
		用户名:<input id="searchBonusUserName" class="easyui-textbox"></input>
		<a href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-search"
			onclick="searchBonus()"
			style="width: 90px">查找</a>		
	</div>
	 <div id="quotaTB" style="padding:2px 5px;">
		用户名:<input id="searchQuotaUserName" class="easyui-textbox"></input>
		<a href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-search"
			onclick="searchQuota()"
			style="width: 90px">查找</a>		
	</div>	
	<!-- ************************************ Promotion Editor End************************************ -->
	<div id="header" data-options="region:'north'"
		style="height: 120px; padding: 20px; background:#D3D3D3">
		<h2>流量管理系统</h2>
		<p>SMART NETWORK APPLICATION ENGINE.</p>
		<label>当前租户:</label>
<s:if test="%{operator.tenantId==0}">
		<input id="currentTenant" class="easyui-combobox"
				data-options="valueField:'id',textField:'text', onSelect: function(rec){
						changeCurrentPromotion(rec.id);
					}">	
					
		<label>REST ID:</label><label id="currentTenantId"/></label>				
</s:if>
<s:else>
		<s:property value="tenantName" />
		<label>REST ID:</label><label id="currentTenantId"><s:property value="operator.tenantId" /></label>
</s:else>
		<div style="width: 400px; height: 50px; position: absolute; right: 10px; bottom: 10px">
			<label>当前用户:</label>
			<s:property value="operator.name" />
			<a href="logout.jsp">注销</a>
			<a href="javascript:openPassDLG()">修改密码</a>
		</div>
	</div>
	<div data-options="region:'center'">

		<div id="tt" class="easyui-tabs">
			<div title="关于" style="padding: 20px;">流量管理系统</div>
<s:if test="%{operator.tenantId==0}">			
			<div title="租户管理" style="padding: 20px;">

				<table id="tenantMgr" class="easyui-datagrid" title="租户列表"
					style="height: 700px"
					data-options="rownumbers:true,singleSelect:true,url:'./rs/admin/tenant/paging',toolbar:tenantToolbar,pagination:true">
					<thead>
						<tr>
							<th field="name" width="120">名字</th>
							<th field="description" width="180" align="right">描述</th>
							<th field="cycleType" width="180" align="right" formatter="formatCycle">周期</th>
							<th field="updateTime" width="180" align="right"
								formatter="formatDate">更新时间</th>
							<th field="creationTime" width="180" align="right"
								formatter="formatDate">创建时间</th>
						</tr>
					</thead>
				</table>
			</div>
			<div title="账户管理" style="padding: 20px;">
				<table id="operatorMgr" class="easyui-datagrid" title="账户列表"
					style="height: 700px"
					data-options="rownumbers:true,singleSelect:true,url:'./rs/admin/operator/paging',toolbar:operatorToolbar,pagination:true">
					<thead>
						<tr>
							<th field="name" width="120">名字</th>
							<th field="tenantId" width="180" align="right"
								formatter="formatTenant">所属租户</th>
							<th field="description" width="180" align="right">描述</th>
							<th field="updateTime" width="180" align="right"
								formatter="formatDate">更新时间</th>
							<th field="creationTime" width="180" align="right"
								formatter="formatDate">创建时间</th>
						</tr>
					</thead>
				</table>
			</div>
</s:if>			
			<div title="活动定义" style="padding: 20px;">
				<table id="draftPromotionMgr" class="easyui-datagrid" title="活动列表"
					style="height: 700px"
					data-options="rownumbers:true,singleSelect:true,toolbar:promotionToolbar,pagination:true">
					<thead>
						<tr>
							<th field="name" width="120">名字</th>
							<th field="startTime" width="180" align="right"
								formatter="formatDate">开始时间</th>
							<th field="endTime" width="180" align="right"
								formatter="formatDate">结束时间</th>
							<th field="volumeType" width="180" align="right"
								formatter="formatVolumeType">流量类型</th>
							<th field="maximum" width="180" align="right"
								formatter="formatVolume">总流量</th>
							<th field="bonusExpirationTime" width="180" align="right"
								formatter="formatDate">红包失效时间</th>
							<th field="description" width="180" align="right">描述</th>

							<th field="updateTime" width="180" align="right"
								formatter="formatDate">更新时间</th>
							<th field="creationTime" width="180" align="right"
								formatter="formatDate">创建时间</th>
						</tr>
					</thead>
				</table>
			</div>
			<div title="当前活动" style="padding: 20px;">
				<table id="activePromotionMgr" class="easyui-datagrid" title="活动列表"
					style="height: 700px"
					data-options="rownumbers:true,singleSelect:true,toolbar:activePromotionToolbar,pagination:true">
					<thead>
						<tr>
							<th field="name" width="120">名字</th>
							<th field="startTime" width="180" align="right"
								formatter="formatDate">开始时间</th>
							<th field="endTime" width="180" align="right"
								formatter="formatDate">结束时间</th>
							<th field="volumeType" width="180" align="right"
								formatter="formatVolumeType">流量类型</th>
							<th field="maximum" width="180" align="right"
								formatter="formatVolume">总流量</th>
							<th field="balance" width="180" align="right"
								formatter="formatLeftVolume">剩余流量</th>	
							<th field="bonusExpirationTime" width="180" align="right"
								formatter="formatDate">红包失效时间</th>
							<th field="description" width="180" align="right">描述</th>

							<th field="updateTime" width="180" align="right"
								formatter="formatDate">更新时间</th>
							<th field="creationTime" width="180" align="right"
								formatter="formatDate">创建时间</th>
						</tr>
					</thead>
				</table>
			</div>	
			<div title="历史活动" style="padding: 20px;">
				<table id="historyPromotionMgr" class="easyui-datagrid" title="活动列表"
					style="height: 700px"
					data-options="rownumbers:true,singleSelect:true,toolbar:'#historyPromotionTB',pagination:true">
					<thead>
						<tr>
							<th field="name" width="120">名字</th>
							<th field="startTime" width="180" align="right"
								formatter="formatDate">开始时间</th>
							<th field="endTime" width="180" align="right"
								formatter="formatDate">结束时间</th>
							<th field="volumeType" width="180" align="right"
								formatter="formatVolumeType">流量类型</th>
							<th field="maximum" width="180" align="right"
								formatter="formatVolume">总流量</th>
							<th field="balance" width="180" align="right"
								formatter="formatLeftVolume">剩余流量</th>	
							<th field="bonusExpirationTime" width="180" align="right"
								formatter="formatDate">红包失效时间</th>
							<th field="description" width="180" align="right">描述</th>

							<th field="updateTime" width="180" align="right"
								formatter="formatDate">更新时间</th>
							<th field="creationTime" width="180" align="right"
								formatter="formatDate">创建时间</th>
						</tr>
					</thead>
				</table>
			</div>	
			<div title="用户红包" style="padding: 20px;">
				<table id="bonusMgr" class="easyui-datagrid" title="活动列表"
					style="height: 700px"
					data-options="rownumbers:true,singleSelect:true,toolbar:'#bonusTB',pagination:true">
					<thead>
						<tr>
							<th field="promotionName" width="180">活动</th>
							<th field="userName" width="120">来源者</th>
							<th field="targetUserName" width="120">拥有者</th>
							<th field="size" width="180" align="right"
								formatter="formatVolume">大小</th>
							<th field="volumeType" width="180" align="right"
								formatter="formatVolumeType">流量类型</th>
							<th field="expirationTime" width="180" align="right"
								formatter="formatDate">过期时间</th>								
							<th field="updateTime" width="180" align="right"
								formatter="formatDate">更新时间</th>
							<th field="creationTime" width="180" align="right"
								formatter="formatDate">创建时间</th>
						</tr>
					</thead>
				</table>
			</div>	
			<div title="用户流量" style="padding: 20px;">
				<table id="quotaMgr" class="easyui-datagrid" title="活动列表"
					style="height: 700px"
					data-options="rownumbers:true,singleSelect:true,toolbar:'#quotaTB',pagination:true">
					<thead>
						<tr>
							<th field="userName" width="120">用户</th>
							<th field="volumeType" width="180" align="right"
								formatter="formatVolumeType">流量类型</th>
							<th field="maximum" width="180" align="right"
								formatter="formatVolume">总流量</th>
							<th field="balance" width="180" align="right"
								formatter="formatLeftVolume">剩余流量</th>	
							<th field="expirationTime" width="180" align="right"
								formatter="formatDate">过期时间</th>
							<th field="activationTime" width="180" align="right"
								formatter="formatActivationDate">激活时间</th>									
							<th field="updateTime" width="180" align="right"
								formatter="formatDate">更新时间</th>
							<th field="creationTime" width="180" align="right"
								formatter="formatDate">创建时间</th>
						</tr>
					</thead>
				</table>
			</div>														
		</div>
	</div>

	<script type="text/javascript">
		function openPassDLG(){
				$('#passInfo').text('');
				$('#passdlg').dialog('open').dialog('setTitle', '修改密码');
				$('#passForm').form('clear');
		}
		function updatePassword(){
			$('#passForm').form(
					'submit',
					{
						onSubmit: function(param){
							if(submitting) {
								alert('正在提交中。。。 请不要重复提交。');
								return false;
							}
							
							if(!$(this).form('validate')){
								return false;
							}
							if(this.newpass.value === this.newpass2.value){	
								submitting=true;
								return true;
							}
							$('#passInfo').text("新密码不一致，请重新输入！");
							return false;
						},
						url : './rs/admin/operator/changepass',
						success : function(result) {
							submitting=false;
							var result = eval('(' + result + ')');
							if (result.code == 2001) {
								$('#passdlg').dialog('close'); // close the dialog
								
							} else {
								$('#passInfo').text(
										'错误: ' + result.message);
							}
						}
					});		
		}
	
		function formatDateBox(date){
			var h=date.getHours();
			var M=date.getMinutes();
			var s=date.getSeconds();
			
			var y = date.getFullYear();
			var m = date.getMonth()+1;
			var d = date.getDate();
			
			function f(s){
				return (s<10?"0":"")+s;
			};
				
			return f(m)+'/'+f(d)+'/'+f(y)+' '+f(h)+':'+f(M)+':'+f(s);
		};
		
		function formatCycle(val,row){
			if(val==0){
				return '每月';
			}
			return val;

		 }
		
		 function formatDate(val,row){
			 return formatDateBox(new Date(val));
		 }

		 function formatActivationDate(val,row){
			 if(val==0){
				 return '未激活';
			 }
			 return formatDateBox(new Date(val));
		 }
		 
		 var K=1000;
		 var M=K*K;
		 var G=M*K;
		 function formatVolume(val,row){
			 if(val>=G){
				 return formatNumber(val/G) +"G";
			 }
			 if(val>=M){
				 return formatNumber(val/M) +"M";
			 }
			 if(val>=K){
				 return formatNumber(val/K)+"K";
			 }
			 return val+"B";
		 }
		 
		 function formatNumber(val){
			 var str = ''+val;
			 var dot = str.indexOf('.');
			 if(dot < 0 || dot+4 >= str.length){
				 return str;
			 }else{
				 return str.substr(0, dot+4);
			 }
		 }

		 
		 function formatLeftVolume(val,row){
			 var used = val/row.maximum;
			 var value = formatVolume(val,row);
			 if (used < 0.15){
				 return '<span style="color:red;">'+value+'</span>';
			 } else if (used < 0.50){
				 return '<span style="color:blue;">'+value+'</span>';
			 } else if (used < 1){
				 return '<span style="color:green;">'+value+'</span>';
			 } else {
				 return value;
			 }
		}
		 
		 
		 function formatVolumeType(val,row){
			switch(val){
			case 0:
				return '移动流量';
			case 1: 
				return 'wifi流量';
			case 2:
				return '定向流量';
			}
			return val;
		 }
		 
		var url;
		var parentdg;
		var confirmurl;
		var afterConfirm;

		function searchQuota(){
			if(!currentTenantId){
				return;
			}
			if(0==currentTenantId){
				return;
			}
			
			var searchName = $('#searchQuotaUserName').textbox('getValue');
		    $('#quotaMgr').datagrid({
		        url:'./rs/admin/quota/'+currentTenantId+'/paging',
		        queryParams:{
		        	name:searchName
		        }
		        });				
		}
		function searchBonus(){
			if(!currentTenantId){
				return;
			}
			if(0==currentTenantId){
				return;
			}
			
			var searchName = $('#searchBonusUserName').textbox('getValue');
		    $('#bonusMgr').datagrid({
		        url:'./rs/admin/bonus/'+currentTenantId+'/paging',
		        queryParams:{
		        	name:searchName
		        }
		        });			
		}

		
		function searchPromotionHistory(){
			if(!currentTenantId){
				return;
			}
			if(0==currentTenantId){
				return;
			}
			
			var fromDate = parseDateToLong( $('#fromDate').datetimebox('getValue'));
			var toDate = parseDateToLong( $('#toDate').datetimebox('getValue'));
			var searchName = $('#searchName').textbox('getValue');
		    $('#historyPromotionMgr').datagrid({
		        url:'./rs/admin/promotion/'+currentTenantId+'/historypaging',
		        queryParams:{
		        	from:fromDate,
		        	to:toDate,
		        	name:searchName
		        }
		        });
		}
		
		function parseDateToLong(date ){
			var t = Date.parse( date);
			return isNaN(t)? '':t;
		}
		
		function loadPromotion(){
			if(!currentTenantId){
				return;
			}
			if(0==currentTenantId){
				return;
			}
		    $('#draftPromotionMgr').datagrid({
		        url:'./rs/admin/promotion/'+currentTenantId+'/draftpaging'
		        });
		    $('#activePromotionMgr').datagrid({
		        url:'./rs/admin/promotion/'+currentTenantId+'/activepaging'
		        }); 
		}
		
		function confirmAction() {
			$('#confirmForm').form(
					'submit',
					{
						onSubmit: function(param){
							if(submitting) {
								alert('正在提交中。。。 请不要重复提交。');
								return false;
							}
							 submitting=true;
							 return true;
						},
						url : confirmurl,
						success : function(result) {
							submitting=false;
							var result = eval('(' + result + ')');
							if (result.code == 2001) {
								$('#confirm').dialog('close'); // close the dialog
								afterConfirm();

							} else {
								$('#confirmInfo').text(
										'错误: ' + result.message);
							}
						}
					});
		}	
		<!-- ************************************ Operator Editor ************************************ -->


		function saveOperator() {
			$('#operatorForm').form(
					'submit',
					{
						onSubmit: function(param){
							if(submitting) {
								alert('正在提交中。。。 请不要重复提交。');
								return false;
							}
							 submitting=true;
							 return true;
						},
						url : url,
						success : function(result) {
							submitting=false;
							var result = eval('(' + result + ')');
							if (result.code == 2001) {
								$('#operatorEditor').dialog('close'); // close the dialog
								$(parentdg).datagrid('reload'); // reload the user data
							} else {
								$('#operatorEditorInfo').text(
										'错误: ' + result.message);
							}
						}
					});
		}	
				
		<!-- ************************************ Promotion Editor ************************************ -->

	

		var submitting=false;
		

		<s:if test="%{operator.tenantId==0}">

		<!-- ************************************ Tenant selector ************************************ -->
		var tenantMap;
		
		function loadTenant(){
			$.ajax({
			     type: "POST",
			     url: "./rs/admin/tenant/select",
			     async: true,
			     contentType: "application/x-www-form-urlencoded",
				 dataType: "json",
				 success: function(data){
				 	tenantMap=data;
				 	
					var options = new Array();
					$.each(data, function(k, v) {
					    var item = new Object();
					    item.id=k;
					    item.text=v;
					    options.push(item);
				    });
					$('#currentTenant').combobox('loadData', options);
					
				    var item = new Object();
				    item.id=0;
				    item.text='超级管理员';
				    options.push(item);
					$('#operatorTenantId').combobox('loadData', options);
				 }
			});
		}
	
		
		loadTenant();
		
		
		function formatTenant(val, row){
			if(val == 0){
				return "超级管理员";
			}else{
				return tenantMap[val];
			}
		}
		
		function changeCurrentPromotion(id){
			currentTenantId=id;
			$('#currentTenantId').text(currentTenantId);
			loadPromotion();
		}
		
		<!-- ************************************ Tenant Editor ************************************ -->
		var tenantToolbar = [ {
			text : '添加',
			iconCls : 'icon-add',
			handler : function() {
				$('#tenantEditorInfo').text('');
				$('#tenantEditor').dialog('open').dialog('setTitle', '添加');
				$('#tenantForm').form('clear');
				url = './rs/admin/tenant/add';
				parentdg='#tenantMgr';
			}
		}, {
			text : '编辑',
			iconCls : 'icon-edit',
			handler : function() {
				var row = $('#tenantMgr').datagrid('getSelected');
				if (row) {
					$('#tenantEditorInfo').text('');
					$('#tenantEditor').dialog('open').dialog('setTitle', '编辑');
					$('#tenantForm').form('load', row);
					url = './rs/admin/tenant/update';
					parentdg='#tenantMgr';
				}

			}
		}, '-', {
			text : '删除',
			iconCls : 'icon-remove',
			handler : function() {
				var row = $('#tenantMgr').datagrid('getSelected');
				if (row) {
					$('#confirmInfo').text('');
					$('#confirm').dialog('open').dialog('setTitle', '删除租户:'+row.name);
					$('#confirmForm').form('load', row);
					confirmurl = './rs/admin/tenant/delete';
					afterConfirm=function(){
						$('#tenantMgr').datagrid('reload'); // reload the user data
						loadTenant();
					}
				}
			}
		} ];

		function saveTenant() {
			$('#tenantForm').form(
					'submit',
					{
						onSubmit: function(param){
							if(submitting) {
								alert('正在提交中。。。 请不要重复提交。');
								return false;
							}
							 submitting=true;
							 return true;
						},
						url : url,
						success : function(result) {
							submitting=false;
							var result = eval('(' + result + ')');
							if (result.code == 2001) {
								$('#tenantEditor').dialog('close'); // close the dialog
								$(parentdg).datagrid('reload'); // reload the user data
								loadTenant();
							} else {
								$('#tenantEditorInfo').text(
										'错误: ' + result.message);
							}
						}
					});
		}
		
		<!-- ************************************ Operator Toolbar ************************************ -->
		var operatorToolbar = [ {
			text : '添加',
			iconCls : 'icon-add',
			handler : function() {
				$('#operatorEditorInfo').text('');
				$('#operatorEditor').dialog('open').dialog('setTitle', '添加');
				$('#operatorName').textbox('readonly',false);
				$('#operatorForm').form('clear');
				url = './rs/admin/operator/add';
				parentdg='#operatorMgr';
			}
		}, {
			text : '编辑',
			iconCls : 'icon-edit',
			handler : function() {
				var row = $('#operatorMgr').datagrid('getSelected');
				if (row) {
					$('#operatorEditorInfo').text('');
					$('#operatorEditor').dialog('open').dialog('setTitle', '编辑');
					$('#operatorName').textbox('readonly',true);
					$('#operatorForm').form('load', row);
					url = './rs/admin/operator/update';
					parentdg='#operatorMgr';
				}

			}
		}, '-', {
			text : '删除',
			iconCls : 'icon-remove',
			handler : function() {
				var row = $('#operatorMgr').datagrid('getSelected');
				if (row) {
					$('#confirmInfo').text('');
					$('#confirm').dialog('open').dialog('setTitle', '删除管理员:'+row.name);
					$('#confirmForm').form('load', row);
					confirmurl = './rs/admin/operator/delete';
					afterConfirm=function(){
						$('#operatorMgr').datagrid('reload'); // reload the user data
					}
				}
			}
		} ,'-', {
			text : '重置密码',
			iconCls : 'icon-remove',
			handler : function() {
				var row = $('#operatorMgr').datagrid('getSelected');
				if (row) {
					$('#confirmInfo').text('');
					$('#confirm').dialog('open').dialog('setTitle', '重置密码。 管理员:'+row.name);
					$('#confirmForm').form('load', row);
					confirmurl = './rs/admin/operator/resetpass';
					afterConfirm=function(){
						
					}
				}
			}
		} ];		
		var currentTenantId=0;
	</s:if>
	<s:else>
		var currentTenantId=<s:property value="operator.tenantId" />;
		loadPromotion();
	</s:else>			

	</script>
</body>

</html>