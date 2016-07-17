/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



function GraphPoint(deviceData) {
    var listOfPoints;
    //Map the inst power and timeRecorded into a new x, y values
    listOfPoints = deviceData.map(function(obj){
           return { x : new Date(obj.timeRecorded), y : obj.instPower };
       });      
    return listOfPoints;
}

