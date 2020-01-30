
$(function(){
    getLatestJobs();
});

function getLatestJobs(){
    var queryConditionJson = {'pageNumber':1,'pageSize':10,'queryObject':'{}','rowCount':1,'sortColumn':'published_on','sortType':'DESC'};
    var queryConditionStr = JSON.stringify(queryConditionJson);
    // queryConditionStr = $.base64('encode', queryConditionStr);
    $.ajax({
        url: "/student/getJobsList",
        dataType: 'json',
        type: 'post',
        contentType: 'application/json',
        data: queryConditionStr,
        processData: false,
        success: function( result, textStatus, jQxhr ){

            var content = '';

            if(result.rows){
                $.each(result.rows,
                    function(i, item) {

                        content += "        <div class=\"mb-5\">\n" +
                            "          <div class=\"row align-items-start job-item border-bottom pb-3 mb-3 pt-3\">\n" +
                            "            <div class=\"col-md-2\">\n" +
                            "              <a href=\"/student/job_single/" + item.id + "\"" + "><img src=\"/careers/images/featured-listing-1.jpg\" alt=\"Image\" class=\"img-fluid\"></a>\n" +
                            "            </div>\n" +
                            "            <div class=\"col-md-4\">\n" +
                            "              <span class=\"badge badge-primary px-2 py-1 mb-3\">" + item.job_type + "</span>\n" +
                            "              <h2><a href=\"/student/job_single/" + item.id + "\">" + item.job_title + "</a> </h2>\n" +
                            "              <p class=\"meta\">Publisher: <strong>" + item.publisher + "</strong></p>\n" +
                            "            </div>\n" +
                            "            <div class=\"col-md-3 text-left\">\n" +
                            "              <h3>" + item.city + "</h3>\n" +
                            "              <span class=\"meta\">" + item.country + "</span>\n" +
                            "            </div>\n" +
                            "            <div class=\"col-md-3 text-md-right\">\n" +
                            "              <strong class=\"text-black\">" + item.salary + "</strong>\n" +
                            "            </div>\n" +
                            "          </div>"
                    });

            }else{

            }

            $("#jobs").html(content);
        },
        error: function( jqXhr, textStatus, errorThrown ){
            console.log( errorThrown );
        }
    });
}

