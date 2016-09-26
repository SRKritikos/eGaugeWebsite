/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$('document').ready(function() { 
    function loadTheGraph() {
        console.log("loading the graph");
        // Build graph on page load.        
        requestGraph();
    }
    
    function loadDateTimePiokers() {
        $('#startDate').datetimepicker({
            defaultDate : new Date()
        });

        $('#endDate').datetimepicker({
            // Get the long of a new date object whose date is set to a day before today
            defaultDate : new Date(new Date().setDate(new Date().getDate()-1))
        });
    }
    /*
    $("#startDate").on("dp.change", function (e) {
        $('#endDate').data("DateTimePicker").maxDate(e.date);
    });
    $("#endDate").on("dp.change", function (e) {
        $('#startDate').data("DateTimePicker").minDate(e.date);
    });
   
    */
 
   
    //Display the instantaneous device readings
    function instDataDisplay() {
        $("#formInstData\\:btnUpdateInstData").click();
        setInterval(function(){
            console.log("Clicking button");
            $("#formInstData\\:btnUpdateInstData").click();
        }, 60000);
    }
    
    instDataDisplay();
    loadDateTimePiokers();
    loadTheGraph();
});

