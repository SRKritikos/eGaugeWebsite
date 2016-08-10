/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function requestGraph() {
    var urlPath = "http://localhost:8080/eGaugeWebService/web/data/data"
    var rstartDate = $("#formDeviceData\\:requestStartDate").val();
    var rendDate = $("#formDeviceData\\:requestEndDate").val();
    
    var startDate = formatToRequestDate(new Date(rstartDate));
    var endDate = formatToRequestDate(new Date(rendDate));
    console.log(startDate, endDate);
    
    $.ajax({
        url : urlPath,
        data : {
            "startDate" : startDate,
            "endDate" : endDate
        }         
    }).done(function(data) {
        buildGraph(data);
    })
    
}

function formatToRequestDate(dateObj) {
    return dateObj.getFullYear() + "-" + (dateObj.getMonth() + 1) 
        + "-" + dateObj.getDate() + " "  + dateObj.getHours() + ":" 
        + dateObj.getMinutes() + ":" + dateObj.getSeconds();
}