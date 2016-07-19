/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$('document').ready(function() { 
    function loadTheGraph() {
        console.log("loading the graph");
        // Build graph on page load.        
        $("#formDeviceData\\:btnDeviceData").click();

    }
    loadTheGraph();
    
    $(function () {
        $('#startDate').datetimepicker({
            defaultDate : new Date()
        });
    });
    
    $(function () {
        $('#endDate').datetimepicker({
            defaultDate : new Date(new Date().setDate(new Date().getDate()-1))
        });
    });

    /*
    $("#startDate").on("dp.change", function (e) {
        $('#endDate').data("DateTimePicker").maxDate(e.date);
    });
    $("#endDate").on("dp.change", function (e) {
        $('#startDate').data("DateTimePicker").minDate(e.date);
    });
    
   

    
   
    */
});

