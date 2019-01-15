//当前验证码是否过期
let isExpired = false;

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
    let telValue = document.getElementById("tel").value;
    if (!(/^1[34578]\d{9}$/.test(telValue))) //验证电话号码是否合法
        alert("Please input correct telephone num!");
    else {
        let getCodeButton = document.getElementById("get_code_button");
        getCodeButton.disabled = true;
        document.getElementById("login_button").disabled = false;

        let data = {
            inputtedTel: telValue
        };
        ajaxHttpRequest("POST", "/verify", JSON.stringify(data), function (res) {
            isExpired = false;
        });

        let cnt = 60;
        let interval = setInterval(updateVeriButton, 1000);//60秒内不可再请求验证码
        function updateVeriButton() {
            cnt--;
            getCodeButton.value = cnt + "s";
            if (cnt === 0) {
                getCodeButton.disabled = false;
                getCodeButton.value = "Get Verification Code";
                window.clearInterval(interval);
                isExpired = true;
            }
        }
    }
};

//为登录按钮添加点击事件
document.getElementById("login_button").onclick = function () {
    let data = {
        inputtedTel : document.getElementById("tel").value,
        veriCode : document.getElementById("veri_code").value
    };
    if (isExpired)
        alert("The verification code has expired!");
    else
        ajaxHttpRequest("POST", "/login", JSON.stringify(data), function (res) {
            let resJSON = JSON.parse(res);
            if (resJSON.code === 1) //验证成功才跳转
                window.location.href = "/background.html";
            else
                alert(resJSON.status);
        });
};