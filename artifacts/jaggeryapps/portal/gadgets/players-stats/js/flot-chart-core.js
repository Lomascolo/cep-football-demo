var pref = new gadgets.Prefs();
var delay;
var chartData = [];

$(function () {	

    //$('#placeholder').html("Hello World");
    //var plot = $.plot("#placeholder", chartData, options);
	var pauseBtn = $("button.pause");
    pauseBtn.click(function () {
	$(this).toggleClass('btn-warning');
	togglePause($(this));
    });
	$(".reset").click(function(){
		fetchData();
	});

    fetchData();

    if(pref.getString("pause").toLowerCase() == "yes")
    {
        document.getElementById("pauseBtn").style.visibility = 'visible';
    }


});


function togglePause(btnElm){
	var pauseBtn = btnElm;
	if(pauseBtn.hasClass('btn-warning')){
       		clearTimeout(delay);
	}
	else{
		if(isNumber(pref.getString("updateGraph")) && !(pref.getString("updateGraph") == "0"))
        		{
            			delay = setTimeout(function(){fetchData()}, pref.getString("updateGraph")*1000);
        		}	
	}    	
}

var drawChart = function(data,options){

	$.plot("#placeholder", data, options);

        var previousPoint = null;
        $("#placeholder").bind("plothover", function (event, pos, item) {

            if ($("#enablePosition:checked").length > 0) {
                var str = "(" + pos.x.toFixed(2) + ", " + pos.y.toFixed(2) + ")";
                $("#hoverdata").text(str);
            }


            if (item) {
                if (previousPoint != item.dataIndex) {

                    previousPoint = item.dataIndex;

                    $("#tooltip").remove();
                    var x = item.datapoint[0].toFixed(2),
                        y = item.datapoint[1].toFixed(2);

                    showTooltip(item.pageX, item.pageY,
                        item.series.label + " of aaa" + x + " = " + y);
                }
            } else {
                $("#tooltip").remove();
                previousPoint = null;
            }
        });


        // connect graph and overview graph

        $("#placeholder").bind("plotselected", function (event, ranges) {

            // clamp the zooming to prevent eternal zoom

            if (ranges.xaxis.to - ranges.xaxis.from < 0.00001) {
                ranges.xaxis.to = ranges.xaxis.from + 0.00001;
            }

            if (ranges.yaxis.to - ranges.yaxis.from < 0.00001) {
                ranges.yaxis.to = ranges.yaxis.from + 0.00001;
            }

            // do the zooming

            plot = $.plot("#placeholder", data,
                $.extend(true, {}, options, {
                    xaxis: { min: ranges.xaxis.from, max: ranges.xaxis.to },
                    yaxis: { min: ranges.yaxis.from, max: ranges.yaxis.to }
                })
            );

            overview.setSelection(ranges, true);
        });

        $("#overview").bind("plotselected", function (event, ranges) {
            plot.setSelection(ranges);
        });
}

function fetchData() {
        var url = "../../portal/apis/flot-data-files/dataFile7.jag"; //pref.getString("dataSource");  
	
        $.ajax({
            url: url,
            type: "GET",
            dataType: "json",
            success: onDataReceived
        });
	var pauseBtn = $("button.pause");
	togglePause(pauseBtn);
}
function onDataReceived(series) {
	var tableStr = "<table id=\"hor-minimalist-b\"><tr><th>ID&nbsp;&nbsp;&nbsp;&nbsp;</th><th>Player's Name&nbsp;&nbsp;&nbsp;&nbsp;</th><th align=\"right\">Team</th><th align=\"right\" style=\"width:70px;\">Shots on Goal</th><th align=\"right\" style=\"width:70px;\">Success Passes</th><th align=\"right\" style=\"width:70px;\">Failed Passes</th></tr>";
	var classA = "a";var classB = "b";var classId; var player_id;   
	for (var i = 0; i <= 15; i++) {
		if(i>7) classId=classA; else classId=classB;
		if(series[i][2].length == 0) series[i][2]= "-";
		if(series[i][3].length == 0) series[i][3]= "-";
		if(series[i][4].length == 0) series[i][4]= "-";
if(i <8) player_id = i+1;
if(i >=8) player_id = i-8+1;
        	tableStr = tableStr + "<tr class="+classId+"><td>"+player_id+"</td><td>"+series[i][0]+"</td><td align=\"right\">"+series[i][1]+"</td><td align=\"right\">"+series[i][2]+"</td><td align=\"right\">"+series[i][3]+"</td><td align=\"right\">"+series[i][4]+"</td></tr>";
    	} 
    	tableStr = tableStr + "</table>";
    
    	$('#placeholder').html(tableStr);
        chartData = series[0];
        //var data = series[0];//console.info(data);
        var chartOptions = options;
        var _chartData = [];
        //addSeriesCheckboxes(chartData);
        $.each(chartData, function(key, val) {
            _chartData.push(chartData[key]);
        });
        //console.info(chartData);
        drawChart(_chartData,chartOptions);
    var seriesContainer = $("#optionsRight");
    seriesContainer.find(":checkbox").click(function(){
        filterSeries(chartData);
    });
}

function showTooltip(x, y, contents) {
        $("<div id='tooltip'>" + contents + "</div>").css({
            top: y + 5,
            left: x + 5
            }).appendTo("body").fadeIn(200);
}
function addSeriesCheckboxes(data){
	// insert checkboxes 
	var seriesContainer = $("#optionsRight .series-toggle");
        seriesContainer.html("");
	var objCount = 0;
	for (var key in data) {
		if (data.hasOwnProperty(key)) {
		    objCount++;
		  }
	}
	if(objCount > 1){
		$.each(data, function(key, val) {
			seriesContainer.append("<li><input type='checkbox' name='" + key +
				"' checked='checked' id='id" + key + "'></input>" +
				"<label for='id" + key + "' class='seriesLabel'>"
				+ val.label + "</label></li>");
		});
	}
}
function filterSeries(data){
	var filteredData = [];
	var seriesContainer = $("#optionsRight");
	seriesContainer.find("input:checked").each(function () {
		var key = $(this).attr("name");
        if (key && data[key]) {
            filteredData.push(data[key]);
        }
        drawChart(filteredData,options);
	});
}
function isNumber(n) {
  return !isNaN(parseFloat(n)) && isFinite(n);
}
