// 日期变化  重新加载数据
function reLoadData(){
	if(dateSymbol == 'day'){
		if($("#day").val() == ''){
			return;
		}
		dateValue = $("#day").val();
		showDayData(); // 加载日表格
	}else{
		if($("#week").val() == ''){
			return;
		}
		dateValue = $("#week").val();
		weekTable.Init();
		showWeekData();
	}
	
	loadBaseInfo(); // 加载基础信息
	loadChart(); // 加载图表
}

// 加载图表
function loadChart(){
	var obj = {
			dateSymbol : dateSymbol,
			dateValue : dateValue,
			projectName : $("#projectName").attr('value')
	};
	$.ajax({
        url: ctx + '/loadChart',
        data: obj,
        type: "POST",
        success:function(result){
        	$("#chart").html('');
    		var myChart = echarts.init(document.getElementById('chart'));
    		wellenReportOption.xAxis[0].data = result.name;
    		wellenReportOption.series[0].data = result.order;
    		wellenReportOption.series[1].data = result.arrive;
    		wellenReportOption.series[2].data = result.late;
    		if('day' == dateSymbol){
    			wellenReportOption.xAxis[0].name = '路线号';
    		}else{
    			wellenReportOption.xAxis[0].name = '';
    		}
    		myChart.setOption(wellenReportOption);
        }
    });
}

// 加载基础信息
function loadBaseInfo(){
	var obj = {
			dateSymbol : dateSymbol,
			dateValue : dateValue,
			projectName : $("#projectName").attr('value')
	};
	$.ajax({
        url: ctx + '/loadBaseInfo',
        data: obj,
        type: "POST",
        success:function(result){
        	$("#totalSiteNums").html(result.totalSiteNums);
        	$("#totalArriveNums").html(result.totalArriveNums);
        	$("#totalLateNums").html(result.totalLateNums);
        	$("#shouldArriveNums").html(result.shouldArriveNums);
        	$("#onTimePer").html(result.onTimePer + "%");
        	$("#destroyPer").html(result.destroyPer + "%");
        }
    });
}

// 日期类型下拉选控制
function dateSelectControl(){
	$('#dateSelect').change(function(){
		var s = $(this).children('option:selected').val();
		if(s == 'day'){
			$("#day").css("display", "block");
			$("#week").css("display", "none");
			$("#dayGrid").css("display", "block");
			$("#weekGrid").css("display", "none");
			dateSymbol = 'day';
			//dateValue = $("#day").val();
		}else{
			$("#day").css("display", "none");
			$("#week").css("display", "block");
			$("#dayGrid").css("display", "none");
			$("#weekGrid").css("display", "block");
			dateSymbol = 'week';
			//dateValue = $("#week").val();
		}
		reLoadData(); 
	})
}

//跳转至明细页面
function showDeatil(){
	if("day" == dateSymbol){
		window.open(ctx + '/dayRouteInfo?route=' + 'sss' + "&projectName=" + $("#projectName").attr('value') + "&dateValue=" + dateValue,
				 'newwindow', 'height=530, width=950, top=100, left=150, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no');
	}else{
		window.open(ctx + '/weekRouteInfo?date=' + 'sss' + "&projectName=" + $("#projectName").attr('value') + "&dateValue=" + dateValue,
	  			 'newwindow', 'height=530, width=950, top=100, left=150, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no');
	}
}

// 波次报表Option
var wellenReportOption = {
    title : {
        text: ''
    },
    grid : {
    	y2 : 40,
    	x2 : 120
    },
    tooltip : {
        trigger: 'axis'
    },
    legend: {
        data:['订单数','到达数','迟到数'],
        x:900,
        orient:'vertical'
    },
    calculable : true,
    color : ['#058DC7','#50B432','#ED561B'],
    xAxis : [
        {
            type : 'category',
            name:'路线号',
            nameTextStyle:{
            	color:'black'
            },
          	axisLine:{
            	lineStyle:{
                	color:'grey'
                }
            },
            splitLine:{
            	show : true
            },
            data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
        }
    ],
    yAxis : [
        {
            type : 'value',
          	name:'数量：订单数',
            nameTextStyle:{
            	color:'black'
            },
          	axisTick : {show:true},
            axisLine:{
            	lineStyle:{
                	color:'grey'
                }
            }
        }
    ],
    series : [
        {
            name:'订单数',
            type:'bar',
            barGap:0,
            itemStyle:{
        		normal:{
                	barBorderRadius:0,
                    label:{
                        show:true,
                    	position:'top',
                        textStyle:{
                        	color:'black'
                        }
                    }
                }
            },
            data:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3]
        },
        {
            name:'到达数',
            type:'bar',
            barGap:0,
            itemStyle:{
        		normal:{
                	barBorderRadius:0,
                    label:{
                        show:true,
                    	position:'top',
                        textStyle:{
                        	color:'black'
                        }
                    }
                }
            },
            data:[2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3]
        },
        {
            name:'迟到数',
            type:'bar',
            barGap:0,
            itemStyle:{
        		normal:{
                	barBorderRadius:0,
                    label:{
                        show:true,
                    	position:'top',
                        textStyle:{
                        	color:'black'
                        }
                    }
                }
            },
            data:[2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3]
        }
    ]
};

// 日表格
var DayTableInit = function () {
    var oTableInit = new Object();
    
//    oTableInit.resetPageNo = function(){
//    	this.pageNumber = 1;
//    }
    
    //初始化Table
    oTableInit.Init = function () {
        $('#tb_day').bootstrapTable({
            url: ctx + '/loadDayGrid',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: false,                     //是否启用排序
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "client",           //分页方式：client客户端分页，server服务端分页（*）
            responseHandler: function(data){
                return data.rows;
            },
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
//            pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
            search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 1,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            height: 527,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                  //是否显示父子表
            showExport: true,                    //是否显示导出按钮
            exportDataType: "all",             //basic', 'all', 'selected'
            exportTypes:['excel'],	    //导出类型
            //exportButton: $('#btn_export'),     //为按钮btn_export  绑定导出事件  自定义导出按钮(可以不用)
            exportOptions:{  
                ignoreColumn: [0,0],            //忽略某一列的索引  
                fileName: '波次报表' + dateValue + '数据',              //文件名称设置  
                worksheetName: 'Sheet1',          //表格工作区名称  
                tableName: '路线信息',  
                excelstyles: ['background-color', 'color', 'font-size', 'font-weight'],  
                //onMsoNumberFormat: DoOnMsoNumberFormat  
            },
            //双击触发的事件，当双击就会获取row，row就是该整行的内容，其中"row.playerName"中"playerName"是data-field定义的字段，（如果在js中定义，就是field定义的字段,）可以通过该方法获取该行所有列的值
            onDblClickRow: function (row) {
            	 window.open(ctx + '/dayRouteInfo?route=' + row.route + "&projectName=" + $("#projectName").attr('value') + "&dateValue=" + dateValue,
            			 'newwindow', 'height=530, width=950, top=100, left=150, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no')
            },
            columns: [{
                checkbox: false
            }, {
                field: 'Number',
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                }
            },{
                field: 'date',
                title: '日期'
            }, {
                field: 'route',
                title: '路线号'
            }, {
                field: 'siteNums',
                title: '总站点数'
            }, {
                field: 'arriveNums',
                title: '总到达'
            }, {
                field: 'lateNums',
                title: '总迟到'
            }, {
                field: 'shouldNums',
                title: '应到站点数'
            }, {
                field: 'onTimePer',
                title: '准点率'
            }, {
                field: 'destroyPer',
                title: '破损率'
            } ]
        });
    };

    //得到查询的参数
    oTableInit.queryParams = function (params) {
    	this.exportOptions.fileName = '波次报表' + dateValue + '数据';
        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            limit: params.limit,   //页面大小
            offset: this.pageNumber,  //页码
            search:params.search,  //搜索框内容
            dateValue:dateValue,  //日期
            projectName:$("#projectName").attr('value') //项目名称
        };
        return temp;
    };
    return oTableInit;
};



// 周表格
var WeekTableInit = function () {
    var oTableInit = new Object();
    
//    oTableInit.resetPageNo = function(){
//    	this.pageNumber = 1;
//    }
    
    //初始化Table
    oTableInit.Init = function () {
        $('#tb_week').bootstrapTable({
            url: ctx + '/loadWeekGrid',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: false,                     //是否启用排序
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "client",           //分页方式：client客户端分页，server服务端分页（*）
            responseHandler: function(data){
                return data.rows;
            },
            pageNumber:1,                       //初始化加载第一页，默认第一页
            pageSize: 7,                       //每页的记录行数（*）
//            pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
            search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
            strictSearch: true,
            showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            minimumCountColumns: 1,             //最少允许的列数
            clickToSelect: true,                //是否启用点击选中行
            height: 411,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "ID",                     //每一行的唯一标识，一般为主键列
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            cardView: false,                    //是否显示详细视图
            detailView: false,                  //是否显示父子表
            showExport: true,                    //是否显示导出按钮
            exportDataType: "all",             //basic', 'all', 'selected'
            exportTypes:['excel'],	    //导出类型
            //exportButton: $('#btn_export'),     //为按钮btn_export  绑定导出事件  自定义导出按钮(可以不用)
            exportOptions:{  
                ignoreColumn: [0,0],            //忽略某一列的索引  
                fileName: '波次报表' + dateValue.replace("W",'') + '周数据',              //文件名称设置  
                worksheetName: 'Sheet1',          //表格工作区名称  
                tableName: '路线信息',  
                excelstyles: ['background-color', 'color', 'font-size', 'font-weight'],  
                //onMsoNumberFormat: DoOnMsoNumberFormat  
            },
            //双击触发的事件，当双击就会获取row，row就是该整行的内容，其中"row.playerName"中"playerName"是data-field定义的字段，（如果在js中定义，就是field定义的字段,）可以通过该方法获取该行所有列的值
            onDblClickRow: function (row) {
            	window.open(ctx + '/weekRouteInfo?date=' + row.date + "&projectName=" + $("#projectName").attr('value') + "&dateValue=" + dateValue,
           			 'newwindow', 'height=530, width=950, top=100, left=150, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no')
            },
            columns: [{
                checkbox: false
            }, {
                field: 'Number',
                title: '序号',
                formatter: function (value, row, index) {
                    return index+1;
                }
            }, {
                field: 'weekNum',
                title: '第几周'
            }, {
                field: 'day',
                title: '周'
            }, {
                field: 'siteNums',
                title: '总站点数'
            }, {
                field: 'arriveNums',
                title: '总到达'
            }, {
                field: 'lateNums',
                title: '总迟到'
            }, {
                field: 'shouldNums',
                title: '应到站点数'
            }, {
                field: 'onTimePer',
                title: '准点率'
            }, {
                field: 'destroyPer',
                title: '破损率'
            } ]
        });
    };

    //得到查询的参数
    oTableInit.queryParams = function (params) {
    	this.exportOptions.fileName = '波次报表' + dateValue.replace("W",'') + '周数据';
        var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            limit: params.limit,   //页面大小
            offset: this.pageNumber,  //页码
            search:params.search,  //搜索框内容
            dateValue:dateValue,  //日期
            projectName:$("#projectName").attr('value') //项目名称
        };
        return temp;
    };
    return oTableInit;
};

