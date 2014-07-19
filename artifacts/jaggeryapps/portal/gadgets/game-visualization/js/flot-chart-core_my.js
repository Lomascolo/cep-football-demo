var pref = new gadgets.Prefs();
var delay;
var chartData = [];

$(function () {
	//initCanvas();	

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

    fetchData();  //uncomment later!!!

    if(pref.getString("pause").toLowerCase() == "yes")
    {
        document.getElementById("pauseBtn").style.visibility = 'visible';
    }


});

function initCanvas(){
	var ctx = document.getElementById('canvas').getContext('2d');

	ctx.fillStyle = "#00A308";
	ctx.beginPath();
	ctx.rect(100, 0, 672, 450);
	ctx.closePath();
	ctx.fill();
}

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
        var url = "../../portal/apis/flot-data-files/dataFile8.jag"; //pref.getString("dataSource");  
	
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
	var tableStr = "<table><tr><th>Player's Name&nbsp;&nbsp;&nbsp;&nbsp;</th><th>Team&nbsp;&nbsp;&nbsp;&nbsp;</th><th>Time Possessed&nbsp;&nbsp;&nbsp;&nbsp;</th><th>Hit Count</th></tr>";
    /*	for (var i = 0; i <= 15; i++) {
        	tableStr = tableStr + "<tr><td>"+series[i][0]+"&nbsp;&nbsp;&nbsp;&nbsp;</td><td align=\"right\">"+series[i][1]+"&nbsp;&nbsp;&nbsp;&nbsp;</td><td align=\"right\">"+series[i][2]+"&nbsp;&nbsp;&nbsp;&nbsp;</td><td align=\"right\">"+series[i][3]+"</td></tr>";
    	}   */
    //	tableStr = tableStr + "</table>";

	var ctx = document.getElementById('canvas').getContext('2d');
	ctx.clearRect(0, 0, 683, 528);
	var pitchImg=document.getElementById("pitch");
	var ballImg=document.getElementById("ball");
	var goalBannerImg=document.getElementById("goal_banner");
	var shotOnGoalBannerImg=document.getElementById("shot_on_goal_banner");
	var offsideBannerImg=document.getElementById("offside_banner");
	var a1Img=document.getElementById("a1");
	var a2Img=document.getElementById("a2");
	var a3Img=document.getElementById("a3");
	var a4Img=document.getElementById("a4");
	var a5Img=document.getElementById("a5");
	var a6Img=document.getElementById("a6");
	var a7Img=document.getElementById("a7");
	var a8Img=document.getElementById("a8");
	var b1Img=document.getElementById("b1");
	var b2Img=document.getElementById("b2");
	var b3Img=document.getElementById("b3");
	var b4Img=document.getElementById("b4");
	var b5Img=document.getElementById("b5");
	var b6Img=document.getElementById("b6");
	var b7Img=document.getElementById("b7");
	var b8Img=document.getElementById("b8");


	ctx.drawImage(pitchImg,0,0);
	//ctx.fillStyle="#CCFFCC";
	//ctx.fillRect(0,0,525, 340);

	ctx.drawImage(a1, series[0][0], series[0][1]);		//light yello for goalie - Nick Gertje

	ctx.drawImage(a2, series[1][0], series[1][1]);		//Dennis Dotterweich - pink    #FFFF00 - yello for the rest of the team

	ctx.drawImage(a3, series[2][0], series[2][1]);		//Niklas Waelzlein - green 

	ctx.drawImage(a4, series[3][0], series[3][1]);		//Wili Sommer - black
	
	ctx.drawImage(a5, series[4][0], series[4][1]);		// Philipp Harlass - bluish purple 
	
	ctx.drawImage(a6, series[5][0], series[5][1]);		//Roman Hartleb -  light blue
	
	ctx.drawImage(a7, series[6][0], series[6][1]);		//Erik Engelhardt - army green
		
	ctx.drawImage(a8, series[7][0], series[7][1]);		//Sandro Schneider - yellow

//---------------------------Team B-----------------------------------------------
	ctx.drawImage(b1, series[8][0], series[8][1]);
	ctx.drawImage(b2, series[9][0], series[9][1]);
	ctx.drawImage(b3, series[10][0], series[10][1]);
	ctx.drawImage(b4, series[11][0], series[11][1]);
	ctx.drawImage(b5, series[12][0], series[12][1]);
	ctx.drawImage(b6, series[13][0], series[13][1]);
	ctx.drawImage(b7, series[14][0], series[14][1]);
	ctx.drawImage(b8, series[15][0], series[15][1]);	

	ctx.drawImage(ballImg, series[16][0], series[16][1]);

	var vx = parseInt(series[18][1])/10;
	var vy = parseInt(series[18][0])/10;
	var ball_x = parseInt(series[16][0])+7;	
	var ball_y = parseInt(series[16][1])+7;
	
	var ts_start = parseFloat(10753295594424116);
	var ts = parseFloat(series[16][2]); 
	ts = ts - ts_start;
	var ts_sec_int = parseInt(ts/1000000000000);
	var ts_min = parseInt(ts/60000000000000);
	var ts_min_in_sec = ts_min*60;
	var ts_sec = ts_sec_int - ts_min_in_sec;
	var time_now = ts_min+":"+ ts_sec;
	//var time_now = ts+", "+ts_sec_int+", "+ts_min+", "+ts_min_in_sec+", "+ts_sec;

	ctx.fillStyle = "#3366CC";
  	ctx.font = "bold 16px Arial";
  	//ctx.fillText(series[16][2], 100, 100);
	ctx.fillText(time_now, 330, 50);


	var ts = parseFloat(series[16][2]); 

	//ctx.drawImage(goalBannerImg, 40, 80);
	if(ts > 11177235131637084 & ts < 11180791512378016 ){	//goal at 7.06
		ball_x = parseInt(series[16][0])+3;	
		ball_y = parseInt(series[16][1])+3;
		ctx.drawImage(goalBannerImg, 40, 80);
	}
	if(ts > 11550636734462112 & ts < 11555227290776920 ){	//goal at 13.2_
		ctx.drawImage(goalBannerImg, 40, 80);
	}
	if(ts > 12080897585078434 & ts < 12087442428103816 ){	//goal at 22.20
		ctx.drawImage(goalBannerImg, 40, 80);
	}

	//series[17][0] = 1;
	if(series[17][0] == 1){
		//alert("shotongoal!");
		sessionStorage.setItem('sog_banner', 200);
		ctx.drawImage(shot_on_goal_banner, 40, 60);
		//alert("ok!");		
		ctx.strokeStyle = "#FE2E2E";    //#F78181  #FE2E2E-pinky
		ctx.setLineDash([2,5]);
		ctx.lineWidth=5;		//1.5
	}	
	
	var sog_banner = sessionStorage.getItem('sog_banner');
	if(sog_banner>0){
		ctx.drawImage(shot_on_goal_banner, 40, 60);
		//alert(sog_banner);		
		ctx.strokeStyle = "#FE2E2E";
		ctx.setLineDash([2,5]);
		ctx.lineWidth=5;
		sog_banner = sog_banner - 1;
		sessionStorage.setItem('sog_banner', sog_banner);
	}
	
	else{
		ctx.strokeStyle = "#E6E6E6";
		ctx.setLineDash([2,3]);
		ctx.lineWidth=0.5;
	}
    	ctx.beginPath();
   	ctx.moveTo(ball_x , ball_y);
  	ctx.lineTo(ball_x+vx/15 , ball_y+vy/15);  
	//alert("from:"+ball_x+","+ball_y+"to:"+vx+","+vy);
	//alert("When added| "+(ball_x+vx)+","+(ball_y+vy));
	
        ctx.closePath();
  	ctx.stroke();


	//series[19][0] = 1;
	var passer = parseInt(series[19][1]);
	var passee = parseInt(series[19][2]);
	var offside_banner = sessionStorage.getItem('offside_banner');
	
	//ts = 11556951947647030; series[19][0] = 1;   //offside
//	if(ts > 11125701490000000 & ts < 11126731490000000){		//11125716487165896
//		passer = 6;
//		passee = 4; 
//		series[19][0] = 1;
//	}
	
	var offside702 = sessionStorage.getItem('offside702');
	if(ts > 11176292010000000 & ts < 11176861290000000){		//7.02 offside 11175293512012326-window-detection   1500000000  11176861290000000 was 11175292010000000
		passer = 6;
		passee = 7; 
		series[19][0] = 1;
		sessionStorage.setItem('offside702', 200);		
	}
//	if(ts > 11185904470000000 & ts < 11185900470000000){		//11185929466974702  
//		passer = 13;
//		passee = 14; 
//		series[19][0] = 1;
//	}

//	ctx.fillStyle = "#3366CC";		//delete later
//  	ctx.font = "bold 16px Arial";
  	//ctx.fillText(series[16][2], 100, 100);
//	ctx.fillText(series[16][2], 330, 350);
	
	if((series[19][0] == 1) & !(ts > 11185929466974702 & ts < 11210990245380356) & !(ts > 11550636734462112 & ts < 11568338996699528 ) ){  //11185382059524124 This timestamp range is for filtering out the wrong offside after the goal.
		//alert("offside!");
		sessionStorage.setItem('offside_banner', 200);
		ctx.drawImage(offsideBannerImg, 60, 90);

		//cirlcing offside-passer
		ctx.strokeStyle = "#FB580D";
		ctx.setLineDash([20,15]);
		ctx.lineWidth=3;
		ctx.beginPath();
		ctx.arc(parseInt(series[passer][0])+8, parseInt(series[passer][1])+8, 15, 0, Math.PI*2, true); 
		ctx.closePath();
		ctx.stroke();	
		//cirlcing offside-passee
		ctx.strokeStyle = "#FCC360";
		ctx.beginPath();
		ctx.arc(parseInt(series[passee][0])+8, parseInt(series[passee][1])+8, 15, 0, Math.PI*2, true); 
		ctx.closePath();
		ctx.stroke();

	}

	if(offside_banner>0){
		offside702 = sessionStorage.getItem('offside702');
		if(offside702 > 0){
			offside702 = offside702 - 1;
			sessionStorage.setItem('offside702', offside702);
			passer = 6;
			passee = 7; 
		}		

		ctx.drawImage(offsideBannerImg, 60, 90);
		//alert(sog_banner);		
		offside_banner = offside_banner - 1;
		sessionStorage.setItem('offside_banner', offside_banner);

		//cirlcing offside-passer
		ctx.strokeStyle = "#FB580D";
		ctx.setLineDash([20,15]);
		ctx.lineWidth=3;
		ctx.beginPath();
		ctx.arc(parseInt(series[passer][0])+8, parseInt(series[passer][1])+8, 15, 0, Math.PI*2, true); 
		ctx.closePath();
		ctx.stroke();	
		//cirlcing offside-passee
		ctx.strokeStyle = "#FCC360";
		ctx.beginPath();
		ctx.arc(parseInt(series[passee][0])+8, parseInt(series[passee][1])+8, 15, 0, Math.PI*2, true); 
		ctx.closePath();
		ctx.stroke();
	}

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
