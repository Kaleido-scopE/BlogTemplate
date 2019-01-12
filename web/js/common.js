//全局的用户名
let defaultUserTel = "15526088820";
let globalAjaxData = {
    userTel: defaultUserTel
};

//为LOG IN标签添加显隐登录窗口功能
document.getElementById("login_alink").onclick = function () {
    let input_box = document.getElementById("login_input_box");
    let computedStyle = document.defaultView.getComputedStyle(input_box, null);
    if (computedStyle.visibility === "hidden")
        input_box.style.visibility = "visible";
    else
        input_box.style.visibility = "hidden";
};

//封装ajax请求
function ajaxHttpRequest(method, url, data, callback) {
    let xhr = new XMLHttpRequest();
    xhr.open(method, url, true);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.setRequestHeader("Accept", "application/json;charset=UTF-8");
    if (method === "POST")
        xhr.send(data);
    else
        xhr.send();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200)
            callback(xhr.responseText);
    }
}

//加载页面时从后端获得包括头像在内的一系列公共信息
function getCommonInfo() {
    ajaxHttpRequest("POST", "/getCommonInfo", JSON.stringify(globalAjaxData), function (res) {
        let resJSON = JSON.parse(res);
        document.getElementById("avatar_img").src = resJSON.avatarPath;
    });
}