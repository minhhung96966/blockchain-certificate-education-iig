function applyJob() {
    fire_ajax_submit();
}


function fire_ajax_submit() {

    // Get form
    var form = $('#fileUploadForm')[0];

    var data = new FormData(form);

    data.set("job_id", (location.pathname+location.search).substr(1).replace("#","").split("/")[2]);

    $("#btn").prop("disabled", true);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/student/uploadcv",
        data: data,
        //http://api.jquery.com/jQuery.ajax/
        //https://developer.mozilla.org/en-US/docs/Web/API/FormData/Using_FormData_Objects
        processData: false, //prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        cache: false,
        timeout: 600000,
        success: function (data) {

            $("#result").text(data);
            $("#btn").prop("disabled", false);

        },
        error: function (e) {

            $("#result").text(e.responseText);
            console.log("ERROR : ", e);
            $("#btn").prop("disabled", false);

        }
    });

}