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
	
		var coldg = new Object();
		coldg.id='#dtheader';
		coldg.editing=undefined;
		coldg.endEdit=function(){
			 var ed = $(this.id).datagrid('getEditor', {index:this.editing,field:'src0'});
			 var value = $(ed.target).combobox('getText');
			 $(this.id).datagrid('getRows')[this.editing]['src'] = value;
		}
		coldg.beginEdit=function(){
			 var ed = $(this.id).datagrid('getEditor', {index:this.editing,field:'src0'});
			 var value = $(this.id).datagrid('getRows')[this.editing]['src']; 
			 $(ed.target).combobox('setText', value);
		}

		var dtheaderToolbar = [ {
			text : '添加',
			iconCls : 'icon-add',
			handler : function() {
				insertRecord(coldg);
			}
		}, {
			text : '删除',
			iconCls : 'icon-remove',
			handler : function() {
				removeRecord(coldg);
			}
		} ,  {
            text: '上移', iconCls: 'icon-up', handler: function () {
                MoveUp(coldg);
            }
        }, {
            text: '下移', iconCls: 'icon-down', handler: function () {
                MoveDown(coldg);
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
		
		/* ************************************data grid function ************************************ */
		var dataColType=[{label: '数字',value: 'N'},{label: '字符串',value: 'S'}];
		var dataColSrc=[{value: 'username'},{value: 'bonusBalance'}];
		
		var colIdx=1;
	
		function editCol(index,row){
			editRecord(coldg,index);
		}
		function singleClick(index,row){
			if (coldg.editing != index) {
				endEditing(coldg);
			}
		}

		function lookup(list, keyName, valueName, key){
			var i=0, len=list.length;
			for(;i<len;i++){
				if(list[i][keyName]==key){
					return list[i][valueName];
				}
			}
			return '';
		}
		
		function addCol(){
			var col = new Object();
			col.field=colIdx++;
			col.width=80;
			return col;
		}

		function endEditing(dg){

			if (dg.editing == undefined){return true}
			var dgId = dg.id;
			if ($(dgId).datagrid('validateRow', dg.editing)){
				if(dg.endEdit){
					dg.endEdit();
				}
				$(dgId).datagrid('endEdit', dg.editing);
				dg.editing = undefined;
				return true;
			} else {
				$(dgId).datagrid('selectRow', dg.editing);
				return false;
			}
		}

		 function insertRecord(dg){
			if(!endEditing(dg)){
				return;
			}
			var record = addCol();
			var dgId = dg.id;
			$(dgId).datagrid('appendRow',record);
			var	index = $(dgId).datagrid('getRows').length-1;
			$(dgId).datagrid('selectRow', index).datagrid('beginEdit', index);
			dg.editing = index;
		}

		 function removeRecord(dg){
		 	var dgId = dg.id;
		 	var row = $(dgId).datagrid('getSelected');
		 	if(row){
			    var index = $(dgId).datagrid('getRowIndex', row);
				$(dg.id).datagrid('cancelEdit', index)
				.datagrid('deleteRow',index);
				dg.editing = undefined;
		 	}
		}
		 
		 
		 function editRecord(dg,index) {
				
			if (dg.editing != index) {
				var dgId = dg.id;
				if (endEditing(dg)) {
					$(dgId).datagrid('selectRow', index).datagrid('beginEdit', index);
					dg.editing = index;
					if(dg.beginEdit){
						dg.beginEdit();
					}
				} 
			}
		}
		 
		 
		function MoveUp(dg) {
			if(!endEditing(dg)){
				return;
			}
			var dgId = dg.id;
		    var row = $(dgId).datagrid('getSelected');
		    var index = $(dgId).datagrid('getRowIndex', row);
		   if (index != 0) {
				var toup = $(dgId).datagrid('getData').rows[index];
				var todown = $(dgId).datagrid('getData').rows[index - 1];
				$(dgId).datagrid('getData').rows[index] = todown;
				$(dgId).datagrid('getData').rows[index - 1] = toup;
				$(dgId).datagrid('refreshRow', index);
				$(dgId).datagrid('refreshRow', index - 1);
				$(dgId).datagrid('selectRow', index - 1);
			}
		     
		}

		function MoveDown(dg) {
			if(!endEditing(dg)){
				return;
			}
			var dgId = dg.id;
		    var row = $(dgId).datagrid('getSelected');
		    var index = $(dgId).datagrid('getRowIndex', row);
			if (index >= 0) {
				var rows = $(dgId).datagrid('getRows').length;
				if (index != rows - 1) {
					var todown = $(dgId).datagrid('getData').rows[index];
					var toup = $(dgId).datagrid('getData').rows[index + 1];
					$(dgId).datagrid('getData').rows[index + 1] = todown;
					$(dgId).datagrid('getData').rows[index] = toup;
					$(dgId).datagrid('refreshRow', index);
					$(dgId).datagrid('refreshRow', index + 1);
					$(dgId).datagrid('selectRow', index + 1);
				}
			}
		     
		}
		