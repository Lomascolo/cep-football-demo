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

    var setBars = function(data){
        var $newDom = $(    '<div class="l-r-row">' +
                                '<div class="l-wrap">'+
                                    '<div class="l-green"></div>'+
                                '</div>'+
                                '<div class="l-r-text">'+data[0].text+'</div>'+
                                '<div class="r-wrap">'+
                                    '<div class="r-green"></div>'+
                                '</div>' +
                            '</div><div style="clear:both"></div> ');
        var $newDom2 = $(    '<div class="l-r-row">' +
                                '<div class="l-wrap">'+
                                    '<div class="l-green"></div>'+
                                '</div>'+
                                '<div class="l-r-text">'+data[1].text+'</div>'+
                                '<div class="r-wrap">'+
                                    '<div class="r-green"></div>'+
                                '</div>' +
                            '</div><div style="clear:both"></div> ');
        var $newDom3 = $(    '<div class="l-r-row">' +
                                '<div class="l-wrap">'+
                                    '<div class="l-green"></div>'+
                                '</div>'+
                                '<div class="l-r-text">'+data[2].text+'</div>'+
                                '<div class="r-wrap">'+
                                    '<div class="r-green"></div>'+
                                '</div>' +
                            '</div><div style="clear:both"></div> ');

        var l_value = parseInt(data[0].l);
        var r_value = parseInt(data[0].r);
	//alert(data[0].l+", "+data[0].r);
        var l_per = (l_value/(l_value+r_value))*100;
        var r_per = (r_value/(l_value+r_value))*100;

        if(l_per == 100){ l_per = 98 } // fixing a 100% overflowing issue
        if(r_per == 100){ r_per = 98 } // fixing a 100% overflowing issue

        if(l_per == 0){
            $('div.l-green',$newDom).html(l_value).removeClass('l-green').addClass('l-trans');
        }else{
            $('div.l-green',$newDom).width(l_per+"%").html(l_value);
        }

        if(r_per == 0){
            $('div.r-green',$newDom).html(l_value).removeClass('r-green').addClass('r-trans');
        }else{
            $('div.r-green',$newDom).width(r_per+"%").html(r_value);
        }

        var l_value1 = parseInt(data[1].l);
        var r_value1 = parseInt(data[1].r);
        var l_per1 = (l_value1/(l_value1+r_value1))*100;
        var r_per1 = (r_value1/(l_value1+r_value1))*100;
	if(l_per1 == 0){
            $('div.l-green',$newDom2).html(l_value1).removeClass('l-green').addClass('l-trans');
        }else{
            $('div.l-green',$newDom2).width(l_per1+"%").html(l_value1);
        }

        if(r_per1 == 0){
            $('div.r-green',$newDom2).html(l_value1).removeClass('r-green').addClass('r-trans');
        }else{
            $('div.r-green',$newDom2).width(r_per1+"%").html(r_value1);
        }

        var l_value2 = parseInt(data[2].l);
        var r_value2 = parseInt(data[2].r);
        var l_per2 = (l_value2/(l_value2+r_value2))*100;
        var r_per2 = (r_value2/(l_value2+r_value2))*100;
	if(l_per2 == 0){
            $('div.l-green',$newDom3).html(l_value2).removeClass('l-green').addClass('l-trans');
        }else{
            $('div.l-green',$newDom3).width(l_per2+"%").html(l_value2);
        }

        if(r_per2 == 0){
            $('div.r-green',$newDom3).html(l_value2).removeClass('r-green').addClass('r-trans');
        }else{
            $('div.r-green',$newDom3).width(r_per2+"%").html(r_value2);
        }

        $('.l-r-container').html($newDom);
	$('.l-r-container').append($newDom2);
	$('.l-r-container').append($newDom3);
    };


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
        var url = "../../portal/apis/flot-data-files/dataFile11.jag"; //pref.getString("dataSource");  
	
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
	paragraph = "<p>";
	var classA = "teamA";var classB = "teamB";var classId; var player_id;  
var tid; var player_name;
	var playersMap = [[0, "Nick Gertje","A"],[1, "Dennis Dotterweich","A"],[2, "Niklas Waelzlein","A"],[3, "Wili Sommer","A"],
	                  [4, "Philipp Harlass","A"],[5, "Roman Hartleb","A"],[6, "Erik Engelhardt","A"],[7, "Sandro Schneider","A"],
	                  [8, "Leon Krapf","B"],[9, "Kevin Baer","B"],[10, "Luca Ziegler","B"],[11, "Ben Mueller","B"],
	                  [12, "Vale Reitstetter","B"],[13, "Christopher Lee","B"],[14, "Leon Heinze","B"],[15, "Leo Langhans","B"],
	                  [16, "",""]
					];
		player_id = parseInt(series[0][0]);
		if( isNaN(player_id)) {
			player_id = "-";  tid = "-"; player_name = "-";
		}
		else{
			player_name = playersMap[player_id][1];
			tid = playersMap[player_id][2];
			if(player_id <8){
				player_id = player_id+1;
				classId = classA;
			}
			if(player_id >=8){
				player_id = player_id-8+1;
				classId = classB;
			}			
		}	
        	paragraph = paragraph + "<p style=\"font-size:20px;\" class="+classId+">"+player_id+", "+player_name+"  ,"+tid+"<br/><br/></p>";
		paragraph = paragraph + "<p style=\"font-size: 32px;\">"+series[0][1]+"-"+series[0][2]+"<br/><br/><br/></p>";  	//score
	//	paragraph = paragraph + "<p>"+series[0][3]+"-"+series[0][4]+"<br/></p>";	//success passes	
	//	paragraph = paragraph + "<p>"+series[0][5]+"-"+series[0][6]+"<br/></p>";	//shot on goal
	//	paragraph = paragraph + "<p>"+series[0][7]+"-"+series[0][8]+"<br/></p>";	//ball possession

        var data = [{l: series[0][3], r: series[0][4], text:"Passes"},{l: series[0][5], r: series[0][6], text:"Shots on Goal"},{l: series[0][7], r: series[0][8], text:"Possession"}];
       // for(var i=0;i<data.length;i++){
            setBars(data);
        //}
    
    	$('#placeholder').html(paragraph);
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
