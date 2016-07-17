/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$('document').ready(function() { 
    

    // Datetime picker setup
    $('#startDate').datetimepicker({
    });
    
    $('#endDate').datetimepicker({
        useCurrent : false,
    });
    
    $("#startDate").on("dp.change", function (e) {
        $('#endDate').data("DateTimePicker").minDate(e.date);
    });
    $("#endDate").on("dp.change", function (e) {
        $('#startDate').data("DateTimePicker").maxDate(e.date);
    });
    
    // Build graph on page load.        
    $("#formDeviceData\\:btnDeviceData").click();
    
    
});

