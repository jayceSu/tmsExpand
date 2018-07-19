// 查询按钮点击事件
function queryData(){
	dateFlag1 = true;
	dateFlag2 = true;
	dateFlag3 = true;
	var timeBegin1 = $("#time-1-begin").val();
	var timeEnd1 = $("#time-1-end").val();
	var timeBegin2 = $("#time-2-begin").val();
	var timeEnd2 = $("#time-2-end").val();
	var timeBegin3 = $("#time-3-begin").val();
	var timeEnd3 = $("#time-3-end").val();
	 
	// 校验日期
	if(!checkDate(timeBegin1, timeEnd1, 1)){
		return;
	}
	// 校验日期
	if(!checkDate(timeBegin2, timeEnd2, 2)){
		return;
	}
	// 校验日期
	if(!checkDate(timeBegin3, timeEnd3, 3)){
		return;
	}
	
	if(!dateFlag1 && !dateFlag2 && !dateFlag3){
		alert("请选择日期段！");
		return;
	}
	
	loadAllChart(); // 加载所有图表
	showTableData(); // 加载表格
}

// 校验日期
function checkDate(beginDate, endDate, num){
	if((beginDate != '' && endDate == '') || (beginDate == '' && endDate != '')){
		alert("日期段" + num + "的开始时间或结束时间不能为空！");
		return false;
	}else if(beginDate == '' && endDate == ''){
		if(num == 1){
			dateFlag1 = false;
		}else if(num == 2){
			dateFlag2 = false;
		}else{
			dateFlag3 = false;
		}
	}else{
		if(endDate < beginDate){
			alert("日期段" + num + "的结束时间不能<开始时间！");
			return false;
		}
	}
	return true;
}

// 加载所有图表
function loadAllChart(){
	loadHourCoverAddChart(); // 加载小时覆盖率（累计）图表
	loadHourCoverChart(); // 加载小时覆盖率（分段）图表
	loadMilesChart(); // 加载分段里程表
	loadSitesChart(); // 加载分段站点表
}

// 是否展示图例
function isDisplayLengend(){
	if(!dateFlag1){
		hourCoverChartOption.legend.selected['日期段1'] = false;
		siteMilesOption.legend.selected['日期段1'] = false;
	}else{
		hourCoverChartOption.legend.selected['日期段1'] = true;
		siteMilesOption.legend.selected['日期段1'] = true;
	}
	if(!dateFlag2){
		hourCoverChartOption.legend.selected['日期段2'] = false;
		siteMilesOption.legend.selected['日期段2'] = false;
	}else{
		hourCoverChartOption.legend.selected['日期段2'] = true;
		siteMilesOption.legend.selected['日期段2'] = true;
	}
	if(!dateFlag3){
		hourCoverChartOption.legend.selected['日期段3'] = false;
		siteMilesOption.legend.selected['日期段3'] = false;
	}else{
		hourCoverChartOption.legend.selected['日期段3'] = true;
		siteMilesOption.legend.selected['日期段3'] = true;
	}
}

// 获取参数对象
function getParamObj(){
	var obj = {
			timeBegin1:$("#time-1-begin").val(),  // 日期段1开始日期
            timeEnd1:$("#time-1-end").val(),  // 日期段1结束日期
            timeBegin2:$("#time-2-begin").val(),  // 日期段2开始日期
            timeEnd2:$("#time-2-end").val(),  // 日期段2结束日期
            timeBegin3:$("#time-3-begin").val(),  // 日期段3开始日期
            timeEnd3:$("#time-3-end").val(),  // 日期段3结束日期
			projectName : $("#projectName").attr('value')
	}; 
	return obj;
}

// 加载小时覆盖率（累计）图表
function loadHourCoverAddChart(){
	var obj = getParamObj();
	obj.type = 'hourLocAll';
	
	$.ajax({
        url: ctx + 'transportReport/loadHourCoverAddChart',
        data: obj,
        type: "POST",
        success:function(result){
        	$("#hourCoverAddChart").html('');
        	isDisplayLengend();
        	hourCoverChartOption.title.text = '小时覆盖率（累计值）';
        	hourCoverChartOption.xAxis[0].data = result.name;
        	hourCoverChartOption.series[0].data = result.data1;
        	hourCoverChartOption.series[1].data = result.data2;
        	hourCoverChartOption.series[2].data = result.data3;
        	var hourCoverAddChart = echarts.init(document.getElementById('hourCoverAddChart'));
        	hourCoverAddChart.setOption(hourCoverChartOption);
        }
    });
}

// 加载小时覆盖率（分段）图表
function loadHourCoverChart(){
	var obj = getParamObj();
	obj.type = 'hourLoc';
	
	$.ajax({
        url: ctx + 'transportReport/loadHourCoverAddChart',
        data: obj,
        type: "POST",
        success:function(result){
        	$("#hourCoverChart").html('');
        	isDisplayLengend();
        	hourCoverChartOption.title.text = '小时覆盖率（分段值）';
        	hourCoverChartOption.xAxis[0].data = result.name;
        	hourCoverChartOption.series[0].data = result.data1;
        	hourCoverChartOption.series[1].data = result.data2;
        	hourCoverChartOption.series[2].data = result.data3;
        	var hourCoverChart = echarts.init(document.getElementById('hourCoverChart'));
        	hourCoverChart.setOption(hourCoverChartOption);
        }
    });
}

// 加载分段里程表
function loadMilesChart(){
	var obj = getParamObj();
	obj.type = 'averageKm';
	
	$.ajax({
        url: ctx + 'transportReport/loadHourCoverAddChart',
        data: obj,
        type: "POST",
        success:function(result){
        	$("#milesChart").html('');
        	isDisplayLengend();
        	siteMilesOption.title.text = '分段里程表';
        	siteMilesOption.yAxis[0].name = '数量：公里数';
        	siteMilesOption.xAxis[0].data = result.name;
        	siteMilesOption.series[0].data = result.data1;
        	siteMilesOption.series[1].data = result.data2;
        	siteMilesOption.series[2].data = result.data3;
        	var milesChart = echarts.init(document.getElementById('milesChart'));
        	milesChart.setOption(siteMilesOption);
        }
    });
}

// 加载分段站点表
function loadSitesChart(){
	var obj = getParamObj();
	obj.type = 'cou';
	
	$.ajax({
        url: ctx + 'transportReport/loadHourCoverAddChart',
        data: obj,
        type: "POST",
        success:function(result){
        	$("#sitesChart").html('');
        	isDisplayLengend();
        	siteMilesOption.title.text = '分段站点数';
        	siteMilesOption.yAxis[0].name = '数量：到达数';
        	siteMilesOption.xAxis[0].data = result.name;
        	siteMilesOption.series[0].data = result.data1;
        	siteMilesOption.series[1].data = result.data2;
        	siteMilesOption.series[2].data = result.data3;
        	var milesChart = echarts.init(document.getElementById('sitesChart'));
        	milesChart.setOption(siteMilesOption);
        }
    });
}

// 小时覆盖率Option
var hourCoverChartOption = {
    title : {
        text: '小时覆盖率（累计值）',
        x: 'center',
        textStyle: {
        	fontSize: 14,
            fontWeight: 'normal',
            color: '#333'
        }
    },
    grid : {
    	y2 : 40,
    	x2 : 120
    },
    tooltip : {
        trigger: 'axis',
        //在这里设置
        formatter: function(params){
        	if(params.length == 0){
        		return "";
        	}
        	var tip = params[0].name + "<br/>";
        	for(var i=0;i<params.length;i++){  
        		tip += "<div style=\"border-radius:5px; width:10px;height:10px;display:inline-block;background-color:" + params[i].color + " \"></div>&nbsp;" + params[i].seriesName + " : " + params[i].data + "%<br/>";
        	}
        	return tip;
        }
    },
    legend: {
        data:['日期段1','日期段2','日期段3'],
        selected:{
        	'日期段1' : true,
        	'日期段2' : true,
        	'日期段3' : true
        },
        x:900,
        orient:'vertical'
    },
    calculable : true,
    color : ['#2EC7C9','#FFB980','#B6A2DE'],
    xAxis : [
        {
            type : 'category',
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
            data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
        }
    ],
    yAxis : [
        {
            type : 'value',
          	name:'百分比：小时覆盖率',
            nameTextStyle:{
            	color:'black'
            },
          	axisTick : {show:true},
          	axisLabel : { 
          		formatter: function(value){ 
          			return value + "%"; 
          		} 
      		}, 
            axisLine:{
            	lineStyle:{
                	color:'grey'
                }
            }
        }
    ],
    series : [
        {
            name:'日期段1',
            type:'line',
            smooth : true,
            // 显示数值
            itemStyle : { 
            	normal: {
            		label : {
            			show: false,
                        textStyle:{
                        	color:'black',
                        	fontSize: 12,
                        }
            		}
            	}
            },
            data:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3]
        },
        {
            name:'日期段2',
            type:'line',
            smooth : true,
            data:[2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3]
        },
        {
            name:'日期段3',
            type:'line',
            smooth : true,
            data:[2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3]
        }
    ]
};

// 站点 里程Option
var siteMilesOption = {
    title : {
        text: '分段里程表',
        x: 'center',
        textStyle: {
        	fontSize: 14,
            fontWeight: 'normal',
            color: '#333'
        }
    },
    grid : {
    	y2 : 40,
    	x2 : 120
    },
    tooltip : {
        trigger: 'axis'
    },
    legend: {
        data:['日期段1','日期段2','日期段3'],
        selected:{
        	'日期段1' : true,
        	'日期段2' : true,
        	'日期段3' : true
        },
        x:900,
        orient:'vertical'
    },
    calculable : true,
    color : ['#2EC7C9','#FFB980','#B6A2DE'],
    xAxis : [
        {
            type : 'category',
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
            data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
        }
    ],
    yAxis : [
        {
            type : 'value',
          	name:'数量：公里数',
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
            name:'日期段1',
            type:'bar',
            barGap:0,
            itemStyle:{
        		normal:{
                	barBorderRadius:0,
                    label:{
                        show:false,
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
            name:'日期段2',
            type:'bar',
            barGap:0,
            itemStyle:{
        		normal:{
                	barBorderRadius:0,
                    label:{
                        show:false,
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
            name:'日期段3',
            type:'bar',
            barGap:0,
            itemStyle:{
        		normal:{
                	barBorderRadius:0,
                    label:{
                        show:false,
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

// 表格
var tableInit = function () {
    var oTableInit = new Object();
    
    //初始化Table
    oTableInit.Init = function () {
        $('#tb').bootstrapTable({
            url: ctx + 'transportReport/loadGrid',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            toolbar: '#toolbar',                //工具按钮用哪个容器
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            pagination: true,                   //是否显示分页（*）
            sortable: true,                     //是否启用排序
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
                fileName: '运输服务水平报表数据',              //文件名称设置  
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
                },
                align: 'center',
            },{
                field: 'date',
                title: '日期段',
                align: 'center',
            }, {
                field: 'startDate',
                title: '开始日期',
                align: 'center',
            }, {
                field: 'endDate',
                title: '结束日期',
                align: 'center',
            }, {
                field: 'endHour',
                title: '小时',
                align: 'center',
                sortable: true
            }, {
                field: 'cou',
                title: '分段站点',
                align: 'center',
                sortable: true
            }, {
                field: 'hourLoc',
                title: '小时到达率',
                align: 'center',
            }, {
                field: 'hourLocAll',
                title: '累加小时到达率',
                align: 'center',
            }, {
                field: 'allLocNum',
                title: '总站点数',
                align: 'center',
                sortable: true
            }, {
                field: 'averageKm',
                title: '平均公里',
                align: 'center',
                sortable: true
            }, {
                field: 'locKm',
                title: '总公里',
                align: 'center',
                sortable: true
            } ]
        });
    };

    // 得到查询的参数
    oTableInit.queryParams = function (params) {
    	this.exportOptions.fileName = '运输服务水平报表数据';
        var temp = {   // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            limit: params.limit,   // 页面大小
            offset: this.pageNumber,  // 页码
            search:params.search,  // 搜索框内容
            timeBegin1:$("#time-1-begin").val(),  // 日期段1开始日期
            timeEnd1:$("#time-1-end").val(),  // 日期段1结束日期
            timeBegin2:$("#time-2-begin").val(),  // 日期段2开始日期
            timeEnd2:$("#time-2-end").val(),  // 日期段2结束日期
            timeBegin3:$("#time-3-begin").val(),  // 日期段3开始日期
            timeEnd3:$("#time-3-end").val(),  // 日期段3结束日期
            projectName:$("#projectName").attr('value') // 项目名称
        };
        return temp;
    };
    return oTableInit;
};