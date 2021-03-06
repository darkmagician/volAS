<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
	<!-- ************************************ Promotion Editor Start************************************ -->
	<div id="promotionEditor" class="easyui-dialog"
		style="width:700px; height: 600px; padding: 10px 20px" closed="true"
		buttons="#promotionButtons">
		<label id="promotionEditorInfo"  style="color:red;"></label>
		<div id="promotionTabs" class="easyui-tabs" style="height: 500px;" data-options="onSelect:onPromTabs">
			<div title="基本信息" style="padding: 20px;">
				<form id="promotionForm" method="post">
					<div class="fitem">
						<label>名字:</label> <input name="name" id="promotionName" class="easyui-textbox"
							required="true"/>
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
							required="true"/>
						<i>可供抢红包的总流量(单位:字节)</i> 	
					</div>
					<div class="fitem">
						<label>开始时间:</label> <input name="startTimeStr" id="promotionStartTimeStr"
							class="easyui-datetimebox" required="true"/>
						<i>活动开始时间</i>	
					</div>
					<div class="fitem">
						<label>结束时间:</label> <input name="endTimeStr" id="promotionEndTimeStr"
							class="easyui-datetimebox" required="true"/>
						<i>活动结束时间</i> 	
					</div>
					<div class="fitem">
						<label>红包过期时间:</label> <input name="bonusExpirationTimeStr" id="promotionBonusExpirationTimeStr"
							class="easyui-datetimebox" required="true"/>
						<i>激活所抢红包的最后期限</i> 	
					</div>
		
					<div class="fitem">
						<label>描述:</label> <input name="description" class="easyui-textbox" id="promotionDescription"/>
						<i>活动说明</i> 
					</div>
					<div class="fitem">
						<label>规则描述方式:</label>
						 <select name="ruleType" id="promotionRuleType"
							class="easyui-combobox" required="true" data-options="onSelect: function(rec){updateRuleType(rec.value);}">
							<option value="0">规则表</option>
							<option value="1">规则脚本</option>
						</select>
					</div>
					<input name="rule" type="hidden"/>
					<input name="tenantId" type="hidden"/> <input name="id"
						type="hidden"/> <input name="creationTime" type="hidden">
					<input name="updateTime" type="hidden">
		
				</form>
			</div>	
			<div title="规则表" style="padding: 10px;">
				<div id="dtTabs" class="easyui-tabs" data-options="fit:true,plain:true,onSelect:onDTTabs">
					<div title="规则表定义" style="padding: 1px;" >
						<table id="dtheader" class="easyui-datagrid" 
							style="height: 400px;"
							data-options="rownumbers:true,singleSelect:true,toolbar:'#dtheaderToolbar',onDblClickRow:doubleHeader,onClickRow:singleHeader">
							<thead>
								<tr>
									<th field="title" width="120" data-options="editor:{type:'text',options:{required:true}}">名字</th>
									<th field="src0" width="120" data-options="formatter:function(value,row){return row.src;},editor:{type:'combobox',options:{valueField:'value',textField:'value',data: dataColSrc,required:true}}">来源</th>
									<th field="defaultVal" width="120" editor="text">默认值</th>
									<th field="type" width="120" data-options="formatter:function(value,row){return lookup(dataColType,'value','label',value);},editor:{type:'combobox',options:{valueField:'value',textField:'label',data: dataColType}}">类型</th>
									<th field="desc" width="180" editor="text">描述</th>
								</tr>
							</thead>
						</table>
					</div>
					<div title="规则表" style="padding: 1px;">
						<table id="dtbody" class="easyui-datagrid" 
							style="height: 400px;"
							data-options="rownumbers:true,singleSelect:true,toolbar:'#dtbodyToolbar',onDblClickRow:doubleBody,onClickRow:singleBody">
						</table>
					</div>
				</div>	
			</div>
			<div title="规则脚本" style="padding: 20px;">
		 		<pre id="promotionRule"></pre>
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
	 <div id="dtheaderToolbar" style="padding:2px 5px;">
		<a id="dtheadAdd" href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-add" plain="true"
			onclick="javascript:insertRecord(coldg)"
			style="width: 60px">添加</a>		
		<a id="dtheadRemove" href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-remove" plain="true"
			onclick="javascript:removeRecord(coldg)"
			style="width: 60px">删除</a>	
		<a id="dtheadUp" href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-add" plain="true"
			onclick="javascript:MoveUp(coldg)"
			style="width: 60px">上移</a>	
		<a id="dtheadDown" href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-add" plain="true"
			onclick="javascript:MoveDown(coldg)"
			style="width: 60px">下移</a>										
	</div>
	 <div id="dtbodyToolbar" style="padding:2px 5px;">
		<a id="dtbodyAdd" href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-add" plain="true"
			onclick="javascript:insertRecord(bodydg)"
			style="width: 60px">添加</a>		
		<a id="dtbodyRemove" href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-remove" plain="true"
			onclick="javascript:removeRecord(bodydg)"
			style="width: 60px">删除</a>	
		<a id="dtbodyUp" href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-add" plain="true"
			onclick="javascript:MoveUp(bodydg)"
			style="width: 60px">上移</a>	
		<a id="dtbodyDown" href="javascript:void(0)" class="easyui-linkbutton"
			iconCls="icon-add" plain="true"
			onclick="javascript:MoveDown(bodydg)"
			style="width: 60px">下移</a>										
	</div>	
		
	<script type="text/javascript">
	var dataColSrc=<%=com.vol.admin.action.MainAction.getPromotionsourcevals()%>;
	</script>
	<script type="text/javascript" src="./static/snae_promotion.js"></script>
