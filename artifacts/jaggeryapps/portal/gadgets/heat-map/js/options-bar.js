var options = {
    legend: {
        show: true
    },
    series: {
        bars: {
            show: true,
            barWidth: 0.6,
            align: "center"
            }
    },
    grid: {
        hoverable: true,
        clickable: true
    },
    yaxis: {
        show:true
    },
    xaxis: {
        show: true,
	ticks: [[0,"Nick"],[1,"Dennis"],[2,"Niklas"],[3,"Wili"],[4,"Philipp"],[5,"Roman"],[6,"Erik"],[7,"Sandro"],[8,"LeonK"],[9,"Kevin"],[10,"Luca"],[11,"Ben"],[12,"Vale"],[13,"Christopher"],[14,"LeonH"],[15,"Leo"]]
    },
	zoom: {
		interactive: true
	},
    selection: {
        mode: "xy"
    }
};


var overviewOptions = {
    legend: {
        show: false
    },
    series: {
        bars: {
            show: true,
            barWidth: 0.6,
            align: "center"
    },
        shadowSize: 0
    },
    xaxis: {
        show: true,
        ticks: 4
    },
    yaxis: {
        ticks: 3
    },
    grid: {
        color: "#999"
    },
    selection: {
        mode: "xy"
    }
};
