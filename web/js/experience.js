//加载页面时从后端获得经历信息
window.onload = function () {
    getCommonInfo();
    ajaxHttpRequest("POST", "/getExperienceInfo", JSON.stringify(globalAjaxData), function (res) {
        let resJSON = JSON.parse(res);
        let contentPassage = document.getElementById("content_passage");
        let textHead = "➤   ";
        for (let i = 0; i < resJSON.experienceList.length; i++) {
            let h4 = document.createElement("h4");
            let h4Text = document.createTextNode(textHead + resJSON.experienceList[i].itemTitle);
            h4.appendChild(h4Text);
            let para = document.createElement("p");
            let paraText = document.createTextNode(resJSON.experienceList[i].itemContent);
            para.appendChild(paraText);
            contentPassage.appendChild(h4);
            contentPassage.appendChild(para);//在div content_passage中依次添加各经历项
        }
    });
};