


function confirm(warehouseId, admin) {
    if ($("#myConfirm").length > 0) {
        $("#myConfirm").remove();
    }
    var html = "<div class='modal fade' id='myConfirm'>"
        + "<div class='modal-backdrop in' style='opacity:0; '></div>"
        + "<div class='modal-dialog' style='z-index:2901; margin-top:60px; width:400px; '>"
        + "<div class='modal-content'>"
        + "<div class='modal-header'  style='font-size:16px; '>"
        + "<span class='glyphicon glyphicon-envelope'>&nbsp;</span>WARNING!<button type='button' class='close' data-dismiss='modal'>"
        + "<span style='font-size:20px;  ' class='glyphicon glyphicon-remove'></span><tton></div>"
        + "<div class='modal-body text-center' id='myConfirmContent' style='font-size:18px; '>"
        + "确定删除该仓库吗？"
        + "</div>"
        + "<div class='modal-footer ' style=''>"
        + "<button class='btn btn-danger ' id='confirmOk' >确定<tton>"
        + "<button class='btn btn-info ' data-dismiss='modal'>取消<tton>"
        + "</div>" + "</div></div></div>";
    $("body").append(html);

    $("#myConfirm").modal("show");

    $("#confirmOk").on("click", function() {
        $("#myConfirm").modal("hide");
        removeWarehouse(warehouseId, admin);
    });
}

function removeWarehouse(warehouseId, admin){

    $.ajax({
        type: "POST",
        url: "/removeWarehouse",
        data: {
            "id": warehouseId,
            "admin": admin
        },
        success: function (response) {
            alert(response.msg);
            window.location = "/index";
        },
        dataType: "json"
    })
}

function setWarehouseIdOut(id){
    $("#warehouseIdOut").val(id)
}

function setWarehouseIdIn(id){
    $("#warehouseIdIn").val(id)
}