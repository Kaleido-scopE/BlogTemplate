//加载页面时从后端获得主页信息
window.onload = function () {
    getCommonInfo();
    ajaxHttpRequest("POST", "/getHomeInfo", JSON.stringify(globalAjaxData), function (res) {
        let resJSON = JSON.parse(res);
        let homeImg = document.getElementById("home_img");
        let parent = homeImg.parentNode;
        for (let i = 0; i < resJSON.contentChips.length; i++) {
            let para = document.createElement("p");
            let text = document.createTextNode(resJSON.contentChips[i]);
            para.appendChild(text);
            parent.insertBefore(para, homeImg);//在home_img标签前依次插入各内容段
        }
        homeImg.src = resJSON.homeImgPath;//加载图片路径
    });
};