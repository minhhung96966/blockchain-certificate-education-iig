/**
 * @author rxl635@student.bahm.ac.uk
 * Copyright 2017,Rujia,Yifan.
 * Licensed under MIT (https://github.com/BlockTechCert/BTCert/blob/master/LICENSE)
 */

$(function() {
    $('#myTab a:first').tab('show');
})

$('#myTab a').click(function(e) {
    e.preventDefault();
    $(this).tab('show');
})

$(function() {
    var id = getQueryString("id");
    getAppliedJobDetail(id);

    var picker = new Pikaday({
        field : document.getElementById('created'),
        toString : function(date, format) {
            return dateFns.format(date, format);
        },
        parse : function(dateString, format) {
            return dateFns.parse(dateString);
        },
        onSelect : function(selectedDate) {
        }
    });

    var picker1 = new Pikaday({
        field : document.getElementById('expires'),
        toString : function(date, format) {
            return dateFns.format(date, format);
        },
        parse : function(dateString, format) {
            return dateFns.parse(dateString);
        },
        onSelect : function(selectedDate) {
        }
    });

})

function getAppliedJobDetail(id) {
    var queryConditionStr = $.base64('encode', id);
    $.getJSON("/hr/getAppliedJobDetail/" + queryConditionStr, function(result) {
        $.each(result, function(name, item) {
            $("#" + name).val(item);
        });
    });
    $("#alert-message-fail").hide();
    $("#alert-message-success").hide();
}


function downloadCV() {
    $.get("/hr/downloadCV/" + getQueryString("id"), function(result) {
        result = eval('(' + result + ')');
        if (result.success == 1) {
            swal({
                title : "Approved!",
                text : "Download CV successfully!",
                type : "success",
                showCancelButton : false,
                confirmButtonText : "OK",
                closeOnConfirm : false
            }, function() {
                location.reload();
            });

        } else {
            swal("Oops...", "Something happened", "error");
        }
    });
}

function downloadCertUIT() {
    $.get("/hr/downloadCertUIT/" + getQueryString("id"), function(result) {
        result = eval('(' + result + ')');
        if (result.success == 1) {
            swal({
                title : "Approved!",
                text : "Download Certificate UIT  successfully!",
                type : "success",
                showCancelButton : false,
                confirmButtonText : "OK",
                closeOnConfirm : false
            }, function() {
                location.reload();
            });

        } else {
            swal("Oops...", "Something happened", "error");
        }
    });
}

function downloadCertIIG() {
    $.get("/hr/downloadCertIIG/" + getQueryString("id"), function(result) {
        result = eval('(' + result + ')');
        if (result.success == 1) {
            swal({
                title : "Approved!",
                text : "Download Certificate IIG successfully!",
                type : "success",
                showCancelButton : false,
                confirmButtonText : "OK",
                closeOnConfirm : false
            }, function() {
                location.reload();
            });

        } else {
            swal("Oops...", "Something happened", "error");
        }
    });
}


function checkAppliedJob(apply_state) {
    var id = getQueryString("id");

    var postData = {
        "apply_state" : apply_state,
        "id": id
    }

    $.get("/hr/checkAppliedJob", postData, function(result) {
        result = eval('(' + result + ')');
        if (result.job_id !== '') {
            swal({
                title : "Approved!",
                text : "The Applicant has been Approved.",
                type : "success",
                showCancelButton : false,
                confirmButtonText : "OK",
                closeOnConfirm : false
            }, function() {
                window.location.href = "/hr/applied_job/" + result.job_id;
            });

        } else {
            swal("Oops...", "Something happened", "error");
        }
    });
}


function verifyCertUIT() {
    $.get("/hr/verifyCertUIT/" + getQueryString("id"), function(result) {
        $("#btn-verifyUIT").attr( "disabled", "disabled" );
        if(result) {
            $.get("http://localhost:3002/api/queries/selectHistorianRecordsByTrxId?transactionId=" + result, function(result1) {
                if(result1 && result1[0].transactionType === "org.degree.AddRoster") {
                    var certificate = result1[0].eventsEmitted[0].results[0];
                    $.get("http://localhost:3002/api/CertificateTemplate/" + certificate.templateId.replace("resource:org.degree.CertificateTemplate#", ""), function(template) {
                        checkInBlockchain('UIT', true, certificate.recipientProfile.name + " - " + template.badge.name + " - " + template.badge.description + " - " + certificate.classification);

                        checkIsNotExpires('UIT');
                        checkIsNotRevoked('UIT');
                    });

                } else {
                    checkInBlockchain('UIT', false, '');
                }
            });
        } else {
            checkInBlockchain('UIT', false, '');
        }
    });
}

function verifyCertIIG() {
    $.get("/hr/verifyCertIIG/" + getQueryString("id"), function(result) {
        $("#btn-verifyIIG").attr( "disabled", "disabled" );
        if(result) {
            $.get("http://localhost:3002/api/queries/selectHistorianRecordsByTrxId?transactionId=" + result, function(result1) {
                if(result1 && result1[0].transactionType === "org.degree.AddRoster") {
                    var certificate = result1[0].eventsEmitted[0].results[0];
                    $.get("http://localhost:3002/api/CertificateTemplate/" + certificate.templateId.replace("resource:org.degree.CertificateTemplate#", ""), function(template) {
                        checkInBlockchain('IIG', true, certificate.recipientProfile.name + " - " + template.badge.name + " - " + template.badge.description + " - " + certificate.classification);

                        checkIsNotExpires('IIG');
                        checkIsNotRevoked('IIG');
                    });

                } else {
                    checkInBlockchain('IIG', false, '');
                }
            });
        } else {
            checkInBlockchain('IIG', false, '');
        }
    });
}

function checkInBlockchain(certificate_type, result, dataSuccess) {
    inuptResult(result, "<i class='fa fa-check' aria-hidden='true'></i> Success: ","Certificate is on blockchain - " + dataSuccess,"<i class='fa fa-times' aria-hidden='true'></i> <span style = 'color:red'> Failure: </span>","Certificate invalid",certificate_type);
    return true;
}

function checkIsNotExpires(certificate_type) {
    inuptResult(true, "<i class='fa fa-check' aria-hidden='true'></i> Success: ","Certificate is not expired","<i class='fa fa-times' aria-hidden='true'></i> <span style = 'color:red'> Failure: </span>","Check Is Not Expired",certificate_type);
    return true;
}

function checkIsNotRevoked(certificate_type) {
    inuptResult(true, "<i class='fa fa-check' aria-hidden='true'></i> Success: ","Certificate is not revoked","<i class='fa fa-times' aria-hidden='true'></i> <span style = 'color:red'> Failure: </span>","Check Is Not Revoked",certificate_type);
    return true;
}

function inuptResult(flag,progress_sccess,result_seccess,progress_failure,result_failure,certificate_type){
    if(flag){
        printResult(progress_sccess,result_seccess,certificate_type)
    }else{
        printResult(progress_failure,result_failure,certificate_type)
    }
}

function printResult(progress,result,certificate_type){
    var output = [];
    output.push('<tr><td class="span12"><strong>', progress, '</strong></td><td>', result, '</td></tr>');
    //document.getElementById('list').innerHTML = '<table class="table table-striped table-hover">' + output.join('') + '</table>' + document.getElementById('list').innerHTML;
    if (certificate_type === 'IIG') {
        $("#resultTableIIG").append(output.join(''))
    } else if (certificate_type === 'UIT') {
        $("#resultTableUIT").append(output.join(''))
    }
}