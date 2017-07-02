/**
 * Created by Metr_yumora on 02.07.2017.
 */

$(document).ready(function () {
    fire_ajax_submit("departments");
});

function fire_ajax_submit(type) {
    if (type === "departments") {
        $("#students").html();
    }

    var select;
    if (type === "groups") {
        select = $("#departments")
    } else if (type === "students") {
        select = $("#groups")
    }
    if (type !== "students")
        $("#submitGroup").prop("disabled", true);
    $("#submitStudent").prop("disabled", true);
    var requestData = (type === "departments" ? null : select.val());

    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: "/get/" + type,
        data: requestData,
        dataType: 'json',
        cache: false,
        timeout: 600000,
        success: function (data) {
            if (type === "students")
                $("#submitGroup").prop("disabled", false);
            var result = "<option disabled selected></option>";
            for (var i = 0; i < data.items.length; i++) {
                result += "<option value='" +
                    data.items[i].id +
                    "'>" + data.items[i].name + "</option>"
            }
            $("#" + type).html(result);
            console.log("SUCCESS : ", data);
        },
        error: function (e) {
            console.log("ERROR : ", e);
            $('#error').html(e.responseText)
        }
    });
}