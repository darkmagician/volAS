	
	/* ************************************ Decision Table Editor ************************************ */
		var coldg = {};
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
		coldg.updated=false;
		
		
		

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
		
		
		var bodydg = {};
		bodydg.id='#dtbody';
		bodydg.editing=undefined;
		
		var dtbodyToolbar = [ {
			text : '添加',
			iconCls : 'icon-add',
			handler : function() {
				insertRecord(bodydg);
			}
		}, {
			text : '删除',
			iconCls : 'icon-remove',
			handler : function() {
				removeRecord(bodydg);
			}
		} ,  {
            text: '上移', iconCls: 'icon-up', handler: function () {
                MoveUp(bodydg);
            }
        }, {
            text: '下移', iconCls: 'icon-down', handler: function () {
                MoveDown(bodydg);
            }
        }];
		
		function onDTTabs(title,index){
			if(index == 1){
				refreshDTBody();
			}
		}
		
		
		function refreshDTBody(data){
			if(!data){
				if(!endEditing(coldg)){
					alert('请检查规则表的列定义。');
					return;
				}
				if(!coldg.updated){
					return;
				}
			}
			
			var inputs = $('#dtheader').datagrid('getData').rows;
			var len = inputs.length;
			var cols = new Array(len+1);
			var i=0;
			for(;i<len;i++){
				cols[i]=inputs[i];
			}
			var output = {};
			output.field='c0';
			output.width=80;
			output.title='红包流量';
			output.editor='text';
			cols[len]=output;
			if(!data){
				data = $('#dtbody').datagrid('getData').rows;
			}
			$('#dtbody').datagrid(
					{
						columns:[cols],
						data:data
					}		
			)
			
			coldg.updated=false;
			
		}
		
		function getDTContent(){
			if(!endEditing(coldg)){
				alert('请检查规则表的列定义。');
				return undefined;
			}
			if(!endEditing(bodydg)){
				alert('请检查规则表的规则。');
				return undefined;
			}
			var inputs = $('#dtheader').datagrid('getData').rows;
			var clen = inputs.length;
			var cols = new Array(clen);
			var i,j;
			for(i=0;i<clen;i++){
				var col= {};
				col.title = inputs[i].title;
				col.src   = inputs[i].src;
				col.desc  = inputs[i].desc;
				col.defaultVal = inputs[i].defaultVal;
				col.type  = inputs[i].type;
				cols[i]= col;
			}
			var data = $('#dtbody').datagrid('getData').rows;
			var dlen = data.length;
			var dtcontent = new Array(dlen);
			for(j=0;j<dlen;j++){
				var r=new Array(clen+1);
				for(i=0;i<clen;i++){
					r[i]=data[j][inputs[i].field];
				}
				r[clen]=data[j].c0;//输出红包列
				dtcontent[j]=r;	
			}
			
			var dt = {};
			dt.cols=cols;
			dt.data=dtcontent;
			return JSON.stringify(dt);
			
		}
		
		function editRule(row){
			var type = row.ruleType;
			updateRuleType(type);
			if(type == 0){
				var dt = JSON.parse(row.rule);
				fillDTObject(dt);
				$('#promotionRule').val('');
			}else if(type == 1){
				$('#promotionRule').val(row.rule);
				var dt = {};
				dt.cols= [];
				dt.data= [];
				fillDTObject(dt);
			}
			$('#promotionTabs').tabs('select', 0);

		}
		
		function initRule(){
			var dt = {};
			dt.cols= [];
			dt.data= [];
			fillDTObject(dt);
			updateRuleType(-1);
			$('#promotionRule').val('');
			$('#promotionTabs').tabs('select', 0);
		}
		
		function fillDTObject(dt){
			
			var clen = dt.cols.length;
			var dlen = dt.data.length;
			var i,j;
			for(i=0;i<clen;i++){
				addCol(dt.cols[i]);
				for(j=0;j<dlen;j++){
					dt.data[j][dt.cols[i].field]=dt.data[j][i];
				}
			}
			for(j=0;j<dlen;j++){
				dt.data[j].c0=dt.data[j][clen];//输出红包列
			}
			$('#dtheader').datagrid('loadData',dt.cols);
			refreshDTBody(dt.data);
		}
		/* ************************************Draft Promotion Editor ************************************ */
		var promotionToolbar = [ {
			text : '添加',
			iconCls : 'icon-add',
			handler : function() {
				$('#promotionEditorInfo').text('');
				$('#promotionEditor').dialog('open').dialog('setTitle', '添加');
				$('#promotionForm').form('clear');
				initRule();
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
					row.startTimeStr=formatDateBox(new Date(row.startTime));
					row.endTimeStr=formatDateBox(new Date(row.endTime));
					row.bonusExpirationTimeStr=formatDateBox(new Date(row.bonusExpirationTime));
					editRule(row);
					enablePromotionEditor(true);
					$('#promotionForm').form('load', row);
					url = './rs/admin/promotion/update';
					parentdg='#draftPromotionMgr';
					
					$('#promotionEditor').dialog('open').dialog('setTitle', '编辑');
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
		
		function updateRuleType(type){
			if(type==0){
				$('#promotionTabs').tabs('disableTab', 2);
				$('#promotionTabs').tabs('enableTab', 1);
			}else if(type==1){
				$('#promotionTabs').tabs('disableTab', 1);
				$('#promotionTabs').tabs('enableTab', 2);
			}else{
				$('#promotionTabs').tabs('disableTab', 1);
				$('#promotionTabs').tabs('disableTab', 2);
			}		
		}
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
							 var type = this.ruleType.value;
							 if(type == 1){
								 this.rule.value=$('#promotionRule').val();
							 }else if(type == 0){
								 this.rule.value=getDTContent();
								 if(this.rule.value == undefined){
									 return false;
								 }
							 }
							 
							 
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
	
		function doubleHeader(index,row){
			editRecord(coldg,index);
		}
		function singleHeader(index,row){
			if (coldg.editing != index) {
				endEditing(coldg);
			}
		}
		
		function doubleBody(index,row){
			editRecord(bodydg,index);
		}
		function singleBody(index,row){
			if (bodydg.editing != index) {
				endEditing(bodydg);
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
		
		function addCol(col){
			if(!col){
				col = {};
			}
			col.field='c'+colIdx++;
			col.width=80;
			col.editor='text';
			col.src0='';
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
				dg.updated=true;
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
			dg.updated=true;
		}

		 function removeRecord(dg){
		 	var dgId = dg.id;
		 	var row = $(dgId).datagrid('getSelected');
		 	if(row){
			    var index = $(dgId).datagrid('getRowIndex', row);
				$(dg.id).datagrid('cancelEdit', index)
				.datagrid('deleteRow',index);
				dg.editing = undefined;
				dg.updated=true;
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
				dg.updated=true;
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
					dg.updated=true;
				}
			}
		     
		}
		