/* 
This is the view!
 */
// TODO: Create request to end point for one days worth of data 
//       Once we have data we want to create a graph that shows the data interms of watzH / DATE

function buildGraph(data) {
    console.log("IN BUILD GRAPH");
    var test = $("#formDeviceData\\:deviceDataField").val();
    console.log(test);
    console.log(data);
    var  names = data.devices.map(function(obj) {
        return obj.name;
    });
    console.log(names);
    var data = data.devices.map(function(obj){
        return obj.deviceData;
    });

    console.log(data);
    var graphData = getGraphData(data, names);
    generateGraph(graphData);
    
}

/**
 * Given data and names return a list of point data to 
 * use for plotting the graph
 * 
 * Data - list of list where each index contains device data.
 * Names -  list of names for the campuses
 */
function getGraphData(data, names) {
    var graphPoints =  [];
    data.forEach(function (listObj) {
       graphPoints.push(GraphPoint(listObj)); 
    });
    
    return buildGraphData(graphPoints, names);
}
/**
 * Build a list of graph point objects to use for 
 * 
 * graphPoints - list of x, y value objects
 * names - list of names for the campuses
 */
function buildGraphData(graphPoints, names) {
    var graphData = [];
    names.forEach(function(name, i){
        graphData.push({
            key : name,
            values : graphPoints[i]
        });
    });
    
    return graphData;
 
}

/**
 * Use NVD3 charts to build graph
 * graphData - the data objects to plot the points on the graph
 */
function generateGraph(graphData) {

  nv.addGraph(function() {
    var chart = nv.models.lineWithFocusChart()
                .margin({left: 100})  
                .showLegend(true) 
                .showYAxis(true)        
                .showXAxis(true)
                .height(500)
                .width(850);

      chart.xAxis
          .tickFormat( function(date) {
              return d3.time.format("%x")(new Date(date));
      });
      
            chart.x2Axis.tickFormat( function(date) {
              return d3.time.format("%x")(new Date(date));
      });
      
      chart.yDomain([0,10000]);
      chart.yAxis
          .tickFormat(d3.format(',.2f'));

      chart.y2Axis
          .tickFormat(d3.format(',.2f'));


      d3.select('#graph-holder svg')
          .datum(graphData)
          .call(chart)
          .style({'height': 500 })

      nv.utils.windowResize(chart.update);
    return chart;
  });
  
}