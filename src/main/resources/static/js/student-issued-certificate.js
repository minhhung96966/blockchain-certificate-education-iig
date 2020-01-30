function getCertificateTemplate(item, callback) {
    var certTemplateId = $.base64('encode', item.certTemplateId);
    if(typeof callback == "function")
        callback(certTemplateId);
}

function toggleJson(){
	   $('#certsInfo').JSONView('toggle', 1);
}

function showDetails(){
    var items= $('#table-list').bootstrapTable('getSelections');
    if (items.length == 0) {
        sweetAlert("Oops...", "You should select at least one row!", "error");
        return;
    } else {
        $(".modal-details").modal('show');
        getCertificateTemplate(items[0], function (certTemplateId) {
            $.getJSON("/student/getCertificateTemplate/" + certTemplateId, function (certTemplate) {
                $(".modal-details").modal('show');
                var detailsResult1 = items[0];
                detailsResult1["certificateTemplate"] = certTemplate;
                delete detailsResult1["0"];
                $("#certsInfo").JSONView({})
                $("#certsInfo").JSONView(detailsResult1, {
                    collapsed : true
                });
            });
        });
    }
}

function downloadQr(){
	
	$('#downcavas').qrcode("http://");

    var canvas = $('#downcavas');
    console.log(canvas);
    var img = canvas.get(0).toDataURL("image/png");
    //or
    //var img = $(canvas)[0].toDataURL("image/png");
    document.write('<img src="'+img+'"/>');
}


function generateQr() {
	$("#qrcode").html("");
	var items= $('#table-list').bootstrapTable('getSelections');
    if(items.length == 1){
        var detailsResult2 = items[0];
        delete detailsResult2["0"];
        delete detailsResult2["certificateTemplate"];
        var downJson = JSON.stringify(detailsResult2);
    	var qrstr = JSON.stringify(downJson)
    	var w = (screen.availWidth > screen.availHeight ? screen.availWidth : screen.availHeight)/3; 
		var qrcode = new QRCode("qrcode", {width:w, height:w});
		if(qrstr.length > 1024){
			$("#qrcode").html("<p>Sorry the data is too long for the QR generator.</p>");
		}  
		qrcode.makeCode(qrstr);
    	$(".modal-edit").modal('show');
    }else{
        sweetAlert("Oops...", "You should select no more one row!", "error")
    };
}

var downloadjson = function (content, filename) {
    var eleLink = document.createElement('a');
    eleLink.download = filename;
    eleLink.style.display = 'none';
    var blob = new Blob([content]);
    eleLink.href = URL.createObjectURL(blob);
  //  eleLink.href = "data:text/json;charset=utf-8," + encodeURIComponent(content);
    document.body.appendChild(eleLink);
    eleLink.click();
    document.body.removeChild(eleLink);
};

function downloadCerts(){
	var items= $('#table-list').bootstrapTable('getSelections');
    if(items.length == 1){
        getCertificateTemplate(items[0], function (certTemplateId) {
            $.getJSON("/student/getCertificateTemplate/" + certTemplateId, function (certTemplate) {
                var detailsResult3 = items[0];
                detailsResult3["certificateTemplate"] = certTemplate;
                delete detailsResult3["0"];
                var downJson = JSON.stringify(detailsResult3);
                downloadjson(downJson, 'certs.json');
            });
        });
    }else{
        sweetAlert("Oops...", "You should select no more one row!", "error")
    };
}

function generatePDF() {
    var items = $('#table-list').bootstrapTable('getSelections');

    if (items.length == 1) {
        window.location.href = "/student/generatePDF/" + items[0]["id"];
    } else {
        sweetAlert("Oops...", "You should select no more one row!", "error");
    }
}


$(function () {

    var oTable = new TableInit();
    oTable.Init();
    var oButtonInit = new ButtonInit();
    oButtonInit.Init();
});


var TableInit = function () {
    var oTableInit = new Object();
    oTableInit.Init = function () {
        $('#table-list').bootstrapTable({
            url: '/student/postCertsInfoList',
            method: 'post',
            toolbar: '#toolbar',
            striped: true,
            cache: false,
            pagination: true,
            sortable: false,
            sortOrder: "asc",
            queryParams: oTableInit.queryParams,
            sidePagination: "server",
            queryParamsType:'',
            pageNumber:1,
            pageSize: 10,
            pageList: [10, 25, 50, 100],
            search: true,
            strictSearch: true,
            showColumns: true,
            showRefresh: true,
            minimumCountColumns: 2,
            clickToSelect: true,
            height: 500,
            uniqueId: "ID",
            showToggle:true,
            cardView: false,
            detailView: false,
            columns: [{
                checkbox: true
            }, {
                field: 'transactionId',
                title: 'Transaction Id'
            }, {
                field: 'recipient.email',
                title: 'Recipient'
            }, {
                field: 'cstate',
                title: 'State'
            },{
                field: 'badge.issuer.email',
                title:'Issuer'
            }]
        });
    };

    oTableInit.queryParams = function (params) {
    	var studentName = $("#student_name").html();
    	// var queryObject = '{"recipient":"'+studentName+'"}'
    	var queryObject = '{"cstate":"valid","recipient.email":"'+studentName+'","id":"matchAll_'+params.searchText+'"}'
        var queryConditionJson = {'pageNumber':params.pageNumber ,'pageSize':params.pageSize ,'queryObject':queryObject,'rowCount':1,'sortColumn':'_id','sortType':'DESC'};
        // var queryConditionStr = JSON.stringify(queryConditionJson);
        // queryConditionStr = $.base64('encode', queryConditionStr);
        return queryConditionJson;
    };
    return oTableInit;
};


var ButtonInit = function () {
    var oInit = new Object();
    oInit.Init = function () {
    };
    return oInit;
};