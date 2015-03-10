<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
	<!-- ************************************ Promotion Editor Start************************************ -->
	<div id="promotionEditor" class="easyui-dialog"
		style="width:700px; height: 800px; padding: 10px 20px" closed="true"
		buttons="#promotionButtons">
		<div class="ftitle">活动信息</div>
		<label id="promotionEditorInfo"  style="color:red;"></label>
		<div id="tt" class="easyui-tabs">
			<div title="基本信息" style="padding: 20px;">
				<form id="promotionForm" method="post">
					<div class="fitem">
						<label>名字:</label> <input name="name" id="promotionName" class="easyui-textbox"
							required="true">
						<i>活动标题</i> 
					</div>
					<div class="fitem">
						<label>流量类型:</label> <select name="volumeType" id="promotionVolumeType"
							class="easyui-combobox" required="true">
							<option value="0">移动流量</option>
							<option value="1">wifi流量</option>
							<option value="2">定向流量</option>
						</select>
						<i>所抢红包的流量类型</i> 
					</div>
					<div class="fitem">
						<label>总流量:</label> <input name="maximum"  id="promotionMaximum" class="easyui-numberbox"
							required="true">
						<i>可供抢红包的总流量(单位:字节)</i> 	
					</div>
					<div class="fitem">
						<label>开始时间:</label> <input name="startTimeStr" id="promotionStartTimeStr"
							class="easyui-datetimebox" required="true">
						<i>活动开始时间</i>	
					</div>
					<div class="fitem">
						<label>结束时间:</label> <input name="endTimeStr" id="promotionEndTimeStr"
							class="easyui-datetimebox" required="true">
						<i>活动结束时间</i> 	
					</div>
					<div class="fitem">
						<label>红包过期时间:</label> <input name="bonusExpirationTimeStr" id="promotionBonusExpirationTimeStr"
							class="easyui-datetimebox" required="true">
						<i>激活所抢红包的最后期限</i> 	
					</div>
		
					<div class="fitem">
						<label>描述:</label> <input name="description" class="easyui-textbox" id="promotionDescription">
						<i>活动说明</i> 
					</div>
					<div class="fitem">
						<label>规则描述方式:</label>
						<div class="easyui-radio" id="radio">
							<input type="radio" name="ruleType" value="0" checked label="规则表">
							<input type="radio" name="ruleType" value="1" label="规则脚本">
						</div>
					</div>
					<input name="rule" type="hidden">
					<input name="tenantId" type="hidden"> <input name="id"
						type="hidden"> <input name="creationTime" type="hidden">
					<input name="updateTime" type="hidden">
		
				</form>
			</div>	
			<div title="规则表" style="padding: 20px;">
					<table id="dtheader" class="easyui-datagrid" title="规则表定义"
						style="height: 700px"
						data-options="rownumbers:true,singleSelect:true,toolbar:dtheaderToolbar">
						<thead>
							<tr>
								<th field="name" width="120">名字</th>
								<th field="source" width="120">来源</th>
								<th field="description" width="180" align="right">描述</th>
							</tr>
						</thead>
					</table>
					<table id="dtbody" class="easyui-datagrid" title="规则表"
						style="height: 700px"
						data-options="rownumbers:true,singleSelect:true,toolbar:dtbodyToolbar">
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
			<div title="规则脚本" style="padding: 20px;">
		 		<textarea id="promotionRule" cols="100" rows="20"></textarea>
			</div>			
		</div>			
	</div>
	<div id="promotionButtons">
		<a href="javascript:void(0)" class="easyui-linkbutton c6" id="promotionOK"
			iconCls="icon-ok" onclick="savePromotion()" style="width: 90px">保存</a>
		<a href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-cancel"
			onclick="javascript:$('#promotionEditor').dialog('close')"
			style="width: 90px">取消</a>
	</div>


	<script type="text/javascript">
		 
		var dtheaderToolbar = [ {
			text : '添加',
			iconCls : 'icon-add',
			handler : function() {
			}
		}, {
			text : '编辑',
			iconCls : 'icon-edit',
			handler : function() {
				var row = $('#draftPromotionMgr').datagrid('getSelected');
				if (row) {
					$('#promotionEditorInfo').text('');
					$('#promotionEditor').dialog('open').dialog('setTitle', '编辑');
					row.startTimeStr=formatDateBox(new Date(row.startTime));
					row.endTimeStr=formatDateBox(new Date(row.endTime));
					row.bonusExpirationTimeStr=formatDateBox(new Date(row.bonusExpirationTime));
					enablePromotionEditor(true);
					$('#promotionForm').form('load', row);
					url = './rs/admin/promotion/update';
					parentdg='#draftPromotionMgr';
				}

			}
		}, '-', {
			text : '删除',
			iconCls : 'icon-remove',
			handler : function() {
				var row = $('#draftPromotionMgr').datagrid('getSelected');
				if (row) {
					$('#confirmInfo').text('');
					$('#confirm').dialog('open').dialog('setTitle', '删除活动:'+row.name);
					$('#confirmForm').form('load', row);
					confirmurl = './rs/admin/promotion/delete';
					afterConfirm=function(){
						$('#draftPromotionMgr').datagrid('reload'); // reload the user data
					}
				}
			}
		} , '-', {
			text : '激活',
			iconCls : 'icon-save',
			handler : function() {
				var row = $('#draftPromotionMgr').datagrid('getSelected');
				if (row) {
					$('#confirmInfo').text('');
					$('#confirm').dialog('open').dialog('setTitle', '激活活动:'+row.name);
					$('#confirmForm').form('load', row);
					confirmurl = './rs/admin/promotion/active/'+row.id;
					afterConfirm=function(){
						$('#draftPromotionMgr').datagrid('reload'); // reload the user data
						$('#activePromotionMgr').datagrid('reload'); // reload the user data
					}
				}
			}
		}];
		
		 
		var url;
		var parentdg;
				

		var submitting=false;
		function savePromotion() {
			$('#promotionForm').form(
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
							 if(!(this.tenantId.value>0)){
								 if(!currentTenantId){
									 alert("请选择租户，再提交");
									 return false;
								 }
								 this.tenantId.value=currentTenantId;
								
							 }
							 param.startTime = parseDateToLong(this.startTimeStr.value);
							 param.bonusExpirationTime = parseDateToLong(this.bonusExpirationTimeStr.value);
							 param.endTime = parseDateToLong(this.endTimeStr.value);
							 submitting=true;
							 return true;
						},
						url : url,
						success : function(result) {
							submitting=false;
							var result = eval('(' + result + ')');
							if (result.code == 2001) {
								$('#promotionEditor').dialog('close'); // close the dialog
								$(parentdg).datagrid('reload'); // reload the user data
							} else {
								$('#promotionEditorInfo').text(
										'错误: ' + result.message);
							}
						}
					});
		}	
		
		function enablePromotionEditor(enable){
			var b = enable?'enable':'disable'
			$('#promotionName').textbox(b);
			$('#promotionVolumeType').combobox(b);
			$('#promotionMaximum').numberbox(b);
			$('#promotionStartTimeStr').datetimebox(b);
			$('#promotionEndTimeStr').datetimebox(b);
			$('#promotionBonusExpirationTimeStr').datetimebox(b);
			$('#promotionDescription').textbox(b);
			$('#promotionRule').attr("disabled",!enable);
			$('#promotionOK').linkbutton(b);
			
		}
		<!-- ************************************Active Promotion Editor ************************************ -->
		function viewPromotion(){
		
				var row = $('#activePromotionMgr').datagrid('getSelected');
				if (row) {
					$('#promotionEditorInfo').text('');
					$('#promotionEditor').dialog('open').dialog('setTitle', '查看');
					
					row.startTimeStr=formatDateBox(new Date(row.startTime));
					row.endTimeStr=formatDateBox(new Date(row.endTime));
					row.bonusExpirationTimeStr=formatDateBox(new Date(row.bonusExpirationTime));
					enablePromotionEditor(false);
					$('#promotionForm').form('load', row);

				}
		}

		function editPromotion(){
				var row = $('#draftPromotionMgr').datagrid('getSelected');
				if (row) {
					$('#promotionEditorInfo').text('');
					$('#promotionEditor').dialog('open').dialog('setTitle', '编辑');
					row.startTimeStr=formatDateBox(new Date(row.startTime));
					row.endTimeStr=formatDateBox(new Date(row.endTime));
					row.bonusExpirationTimeStr=formatDateBox(new Date(row.bonusExpirationTime));
					enablePromotionEditor(true);
					$('#promotionForm').form('load', row);
					url = './rs/admin/promotion/update';
					parentdg='#draftPromotionMgr';
				}
		}
					
		function createPromotion(){
				$('#promotionEditorInfo').text('');
				$('#promotionEditor').dialog('open').dialog('setTitle', '添加');
				$('#promotionForm').form('clear');

				enablePromotionEditor(true);
				url = './rs/admin/promotion/add';
				parentdg='#draftPromotionMgr';
		}
	</script>
</body>

</html>