//为LOG IN标签添加显隐登录窗口功能
document.getElementById("login_alink").onclick = function () {
    let input_box = document.getElementById("login_input_box");
    let computedStyle = document.defaultView.getComputedStyle(input_box, null);
    if (computedStyle.visibility === "hidden")
        input_box.style.visibility = "visible";
    else
        input_box.style.visibility = "hidden";
};

// //图片转码功能
// function getBase64(imgUrl) {
//     let reader = new FileReader();
//     console.log(reader.readAsDataURL());
// }