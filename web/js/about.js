//加载页面时从后端获得个人信息
window.onload = function () {
    getCommonInfo();
    ajaxHttpRequest("POST", "/getAboutInfo", JSON.stringify(globalAjaxData), function (res) {
        let resJSON = JSON.parse(res);
        let aboutImg = document.getElementById("about_img");
        let parent = aboutImg.parentNode;
        for (let i = 0; i < resJSON.aboutList.length; i++) {
            let span = document.createElement("span");
            let spanText = document.createTextNode(resJSON.aboutList[i].itemContent);
            span.appendChild(spanText);
            let para = document.createElement("p");
            let paraTextHead = document.createTextNode(resJSON.aboutList[i].itemName + ": ");
            para.appendChild(paraTextHead);
            para.appendChild(span);
            parent.insertBefore(para, aboutImg);//在about_img标签前依次插入各内容段
        }
        aboutImg.src = resJSON.aboutImgPath;//加载图片路径
    });
};