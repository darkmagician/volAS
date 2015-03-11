	var promotionToolbar = [ {
			text : '添加',
			iconCls : 'icon-add',
			handler : function() {
				$('#promotionEditorInfo').text('');
				$('#promotionEditor').dialog('open').dialog('setTitle', '添加');
				$('#promotionForm').form('clear');

				enablePromotionEditor(true);
				url = './rs/admin/promotion/add';
				parentdg='#draftPromotionMgr';
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
		

		var dtbodyToolbar = [ {
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
		

		/* ************************************Active Promotion Editor ************************************ */

		var activePromotionToolbar = [ {
			text : '查看',
			iconCls : 'icon-tip',
			handler : function() {
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
		}];