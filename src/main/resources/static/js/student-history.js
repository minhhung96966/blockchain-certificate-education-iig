/**
 * @author rxl635@student.bahm.ac.uk
 * Copyright 2017,Rujia,Yifan.
 * Licensed under MIT (https://github.com/BlockTechCert/BTCert/blob/master/LICENSE)
 */
$(function(){
    getHistoryCertificateList(1);
    $("#checkAll").click(function() {
        $('input[name="subBox"]').attr("checked",this.checked);
    });
    $("input[name='subBox']").click(function(){
        $("#checkAll").attr("checked", $("input[name='subBox']").length == $("input[name='subBox']:checked").length ? true : false);
    });
});

function generatePageHTML(number) {
    var currentPage =parseInt($(".current-page").val());

    if(currentPage==1){
        var str = '<ul class="pagination">';
    }else{
        var str = '<ul class="pagination"><li><a onclick="getHistoryCertificateList(' + (currentPage - 1) + ')">&laquo;</a></li>';
    }

    if(currentPage==number){
        var strAfter  = '</ul>';
    }else{
        var strAfter  = '<li><a onclick="getHistoryCertificateList(' + (currentPage + 1) + ')">&raquo;</a></li></ul>';
    }

    for(var i=1;i<=number;i++){
        if(i==$(".current-page").val()){
            str += '<li class="active"><a onclick="getHistoryCertificateList(' + i + ')"> ' + i  +  ' </a></li>';
        }else{
            str += '<li><a onclick="getHistoryCertificateList(' + i + ')"> ' + i  +  ' </a></li>';
        }
    }
    str += strAfter;
    $("#pages").html(str);

}

function getHistoryCertificateList(pagenumber){
    var queryConditionJson = {'pageNumber':pagenumber,'pageSize':10,'queryObject':'{}','rowCount':1,'sortColumn':'apply_time','sortType':'DESC'};
    var queryConditionStr = JSON.stringify(queryConditionJson);
    // queryConditionStr = $.base64('encode', queryConditionStr);
    $.ajax({
        url: "/student/getStuConfList",
        dataType: 'json',
        type: 'post',
        contentType: 'application/json',
        data: queryConditionStr,
        processData: false,
        success: function( result, textStatus, jQxhr ){
            var str='';
            var numbers = 0;
            if(result.rows){
                $.each(result.rows,
                    function(i, item) {

                        var isdisabled = '';
                        if(item.apply_state==="passed")
                            isdisabled = 'disabled';

                        var deletestr = "<td><button type='button' class='btn btn-info view' " +
                            "onclick='delStuConf(\"" + item.id + "\")'" + isdisabled + ">Delete</button></td> ";

                        var editstr = "<td><button type='button' class='btn btn-info view' " +
                            "data-toggle='modal' data-target='.modal-edit' " +
                            "onclick='getCertificate(\"" + item.id + "\")'>Edit</button></td>";

                        // data-raw='" + JSON.stringify(item) + "'

                        str+=("<tr id='id" + item.id + "'><td>" + item.id + "</td><td>" +
                        item.apply_time + "</td><td>"+ item.handle_time +"</td><td>" +
                        prefix_state(item.apply_state) + "</td><td>" + item.apply_note + "</td>" + editstr + deletestr);
                    });
                $(".sbcon").html(str);
                $(".current-page").val(result.pageNumber);
                numbers = Math.ceil(parseInt(result.total) / parseInt(result.pageSize));
                console.log(numbers);
                generatePageHTML(numbers);
            }else{
                $(".table-recent-apply").hide();
            }

            $(".loadingsb").hide();

        },
        error: function( jqXhr, textStatus, errorThrown ){
            console.log( errorThrown );
        }
    });

}

function delSelected() {
    var item= $('#table-list').bootstrapTable('getSelections');
    // console.log(item);
    for(var current in item){
        console.log(item[current].id);
    }
}

function editSelected() {
    var item= $('#table-list').bootstrapTable('getSelections');
    if(item.length==1){
        $(".modal-edit").modal('show');
        getCertificate(item[0].id);
    }else{sweetAlert("Oops...", "You can only select one row!", "error")};
}

function getCertificate(id){
    var queryConditionStr = $.base64('encode', id);
    $.getJSON("/student/getStuConf/" + queryConditionStr,function(result){
        console.log(result);
        $.each(result,
            function(name, item) {
                $("#"+name).val(item);
            });
    });
    $("#alert-message-fail").hide();
    $("#alert-message-success").hide();
}


function updateStuConf(){

    var id = $("id").val();
    var queryArray = ["given_name","family_name","birthday","apply_type","identity",
        "identity_type","file_hash","apply_note","apply_state","apply_time","handle_time","id","user_id"];
    var queryConditionStr = "{";
    $.each(queryArray,function(i, item) { queryConditionStr += '"' + item + '" : "' + $("#"+item).val() + '", ';});
    queryConditionStr = queryConditionStr.substring(0,queryConditionStr.length-1) + "}";
    queryConditionStr = $.base64('encode', queryConditionStr);

    $.getJSON("/student/updateStuConf/" + queryConditionStr,function(result){
        if(result.success == 1){
            $("#alert-message-fail").hide();
            $("#alert-message-success").fadeIn();
        }else{
            $("#alert-message-success").hide();
            $("#alert-message-fail").fadeIn();
        }
    });
}


function delStuConf(id){
    swal({
            title: "Are you sure?",
            text: "You will not be able to recover this applicant",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "Yes, delete it!",
            closeOnConfirm: false
        },
        function(){
            //console.log(id)
            id = $.base64('encode', id);
            $.getJSON("/student/delStuConf/" + id,function(result){
                if(result.success == "1"){
                    /* swal("Deleted!", "Your imaginary file has been deleted.", "success");
                     window.location.href = "/student";*/
                    swal({
                            title: "Deleted!",
                            text: "Your Applicant has been deleted.",
                            type: "success",
                            showCancelButton: false,
                            confirmButtonText: "OK",
                            closeOnConfirm: false
                        },
                        function(){
                            window.location.href = "/student/student_home";
                        });
                }else{
                    swal("Oops...", "Something happened", "error");
                }
            });

        });
}