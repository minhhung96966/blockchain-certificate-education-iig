$(function(){

    $("#apply").attr("href", "/student/apply_job/" + (location.pathname+location.search).substr(1).replace("#","").split("/")[2]);

});