// 日期类型下拉选控制
function dateSelectControl(){
	$('#dateSelect').change(function(){
		var s = $(this).children('option:selected').val();
		if(s == 'day'){
			$("#day").css("display", "block");
			$("#week").css("display", "none");
			$("#dayGrid").css("display", "block");
			$("#weekGrid").css("display", "none");
			$("#showDetailButton").css("display", "none");
			dateSymbol = 'day';
		}else if(s == 'week'){
			$("#day").css("display", "none");
			$("#week").css("display", "block");
			$("#dayGrid").css("display", "none");
			$("#weekGrid").css("display", "block");
			$("#showDetailButton").css("display", "block");
			dateSymbol = 'week';
		}
		reLoadData(); 
	})
}

//日期变化  重新加载数据
function reLoadData(){
	if(dateSymbol == 'day'){
		if($("#day").val() == ''){
			return;
		}
		dateValue = $("#day").val();
		showDayData(); // 加载日表格
	}else if(dateSymbol == 'week'){
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

//加载图表
function loadChart(){
	var obj = {
			dateSymbol : dateSymbol,
			dateValue : dateValue,
			projectName : $("#projectName").attr('value')
	};
	$.ajax({
        url: ctx + 'hourReport/loadChart',
        data: obj,
        type: "POST",
        success:function(result){
        	$("#chart").html('');
    		var myChart = echarts.init(document.getElementById('chart'));
    		if(dateSymbol == 'day'){
    			singleOption.series[0].data = result.result[0];
    			myChart.setOption(singleOption);
    		}else{
    			multiOption.series[0].data = result.result[0];
    			multiOption.series[1].data = result.result[1];
    			multiOption.series[2].data = result.result[2];
    			multiOption.series[3].data = result.result[3];
    			multiOption.series[4].data = result.result[4];
    			multiOption.series[5].data = result.result[5];
    			multiOption.series[6].data = result.result[6];
    			myChart.setOption(multiOption);
    		}
        }
    });
}

//加载基础信息
function loadBaseInfo(){
	var obj = {
			dateSymbol : dateSymbol,
			dateValue : dateValue,
			projectName : $("#projectName").attr('value')
	};
	$.ajax({
        url: ctx + 'hourReport/loadBaseInfo',
        data: obj,
        type: "POST",
        success:function(result){
        	$("#totalSiteNum").html(result.totalSiteNum);
        	$("#totalArriveNum").html(result.totalArriveNum);
        }
    });
}

// 跳转至明细页面
function showDeatil(){
	window.open(ctx + '/hourWeekInfo?hour=' + "sss"+ "&projectName=" + $("#projectName").attr('value') + "&dateValue=" + dateValue,
			 'newwindow', 'height=530, width=950, top=100, left=150, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no')
}

// 单条线的折线图
var singleOption = {
    tooltip : {
        trigger: 'axis'
    },
    legend: {
        data:['']
    },
    calculable : true,
    xAxis : [
        {
            type : 'category',
            boundaryGap : false,
          	name:'小时数',
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
            data : ['24','48','72','96','120','144','168','192','216','240','264','288','312', "336", "360", "384", "408"]
        }
    ],
    yAxis : [
        {
            type : 'value',
          	name:'数量：站点数',
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
    color : ['#97B552'],
    series : [
        {
            name:'',
            type:'line',
            stack: '总量',
            smooth : true,
            data:[120, 132, 101, 134, 90, 230, 210]
        }
    ]
};

// 多条线的折线图
var multiOption = {
    tooltip : {
        trigger: 'axis'
    },
    legend: {
        data:['周一','周二','周三','周四','周五','周六','周日']
    },
    calculable : true,
    xAxis : [
        {
            type : 'category',
            boundaryGap : false,
          	name:'小时数',
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
            data : ['24','48','72','96','120','144','168','192','216','240','264','288','312', "336", "360", "384", "408"]
        }
    ],
    yAxis : [
        {
            type : 'value',
          	name:'数量：站点数',
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
    color : ['#97B552','#2EC7C9','#B6A2DE','#FFB980','#D87A80','#A3ACC2','#EAD83D'],
    series : [
        {
            name:'周一',
            type:'line',
            smooth : true,
            data:[120, 132, 101, 134, 90, 230, 210]
        },
        {
            name:'周二',
            type:'line',
            smooth : true,
            data:[220, 182, 191, 234, 290, 330, 310]
        },
        {
            name:'周三',
            type:'line',
            stack: '总量',
            smooth : true,
            data:[150, 232, 201, 154, 190, 330, 410]
        },
        {
            name:'周四',
            type:'line',
            smooth : true,
            data:[320, 332, 301, 334, 390, 330, 320]
        },
        {
            name:'周五',
            type:'line',
            smooth : true,
            data:[820, 932, 901, 934, 1290, 1330, 1320]
        },
        {
            name:'周六',
            type:'line',
            smooth : true,
            data:[820, 932, 901, 934, 1290, 1330, 1320]
        },
        {
            name:'周日',
            type:'line',
            smooth : true,
            data:[820, 932, 901, 934, 1290, 1330, 1320]
        }
    ]
};

// 日表格
var DayTableInit = function () {
    var oTableInit = new Object();
    
    //初始化Table
    oTableInit.Init = function () {
        $('#tb_day').bootstrapTable({
            url: ctx + 'hourReport/loadDayGrid',         //请求后台的URL（*）
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
            pageList: [10, 25],        //可供选择的每页的行数（*）
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
                fileName: '小时到达率报表' + dateValue + '数据',              //文件名称设置  
                worksheetName: 'Sheet1',          //表格工作区名称  
                tableName: '详细信息',  
                excelstyles: ['background-color', 'color', 'font-size', 'font-weight'],  
                //onMsoNumberFormat: DoOnMsoNumberFormat  
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
                title: '发货日'
            }, {
                field: 'hour',
                title: '小时'
            }, {
                field: 'arriveSite',
                title: '到达站点数'
            }, {
                field: 'addArriveSite',
                title: '累计到达站点数'
            }, {
                field: 'totalSite',
                title: '总站点数'
            }, {
                field: 'hourRate',
                title: '小时覆盖率'
            } ]
        });
    };

    //得到查询的参数
    oTableInit.queryParams = function (params) {
    	this.exportOptions.fileName = '小时到达率报表' + dateValue + '数据';
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
    
    //初始化Table
    oTableInit.Init = function () {
        $('#tb_week').bootstrapTable({
            url: ctx + 'hourReport/loadWeekGrid',         //请求后台的URL（*）
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
            pageList: [10, 25],        //可供选择的每页的行数（*）
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
                fileName: '小时到达率报表' + dateValue.replace("W",'') + '周数据',              //文件名称设置  
                worksheetName: 'Sheet1',          //表格工作区名称  
                tableName: '详细信息',  
                excelstyles: ['background-color', 'color', 'font-size', 'font-weight'],  
                //onMsoNumberFormat: DoOnMsoNumberFormat  
            },
            //双击触发的事件，当双击就会获取row，row就是该整行的内容，其中"row.playerName"中"playerName"是data-field定义的字段，（如果在js中定义，就是field定义的字段,）可以通过该方法获取该行所有列的值
            onDblClickRow: function (row) {
            	 window.open(ctx + '/hourWeekInfo?hour=' + row.hour + "&projectName=" + $("#projectName").attr('value') + "&dateValue=" + dateValue,
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
                title: '周'
            }, {
                field: 'hour',
                title: '小时'
            }, {
                field: 'arriveSite',
                title: '到达总站点数'
            }, {
                field: 'totalSite',
                title: '周总站点数'
            }, {
                field: 'hourRate',
                title: '小时覆盖率'
            } ]
        });
    };

    //得到查询的参数
    oTableInit.queryParams = function (params) {
    	this.exportOptions.fileName = '小时到达率报表' + dateValue.replace("W",'') + '周数据';
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