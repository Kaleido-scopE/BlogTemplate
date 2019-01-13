//为LOG IN标签添加显隐登录窗口功能
document.getElementById("login_alink").onclick = function () {
    let input_box = document.getElementById("login_input_box");
    let computedStyle = document.defaultView.getComputedStyle(input_box, null);
    if (computedStyle.visibility === "hidden")
        input_box.style.visibility = "visible";
    else
        input_box.style.visibility = "hidden";
};

//初始时登录按钮不可点击，需要点击一次验证码按钮后才可以
document.getElementById("login_button").disabled = true;

//为获取验证码按钮添加点击事件
document.getElementById("get_code_button").onclick = function () {
    document.getElementById("login_button").disabled = false;
    let data = {
        inputtedTel : document.getElementById("tel").value
    };
    ajaxHttpRequest("POST", "/verify", JSON.stringify(data), function (res) {

    });
};

//为登录按钮添加点击事件
document.getElementById("login_button").onclick = function () {
    let data = {
        inputtedTel : document.getElementById("tel").value,
        veriCode : document.getElementById("veri_code").value
    };
    ajaxHttpRequest("POST", "/login", JSON.stringify(data), function (res) {
        let resJSON = JSON.parse(res);
        if (resJSON.code === 1)
            window.location.href = "/background.html";
        else
            alert(resJSON.status);
    });
};