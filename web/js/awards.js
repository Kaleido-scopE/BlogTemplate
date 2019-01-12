//加载页面时从后端获得获奖信息
window.onload = function () {
    getCommonInfo();
    ajaxHttpRequest("POST", "/getAwardsInfo", JSON.stringify(globalAjaxData), function (res) {
        let resJSON = JSON.parse(res);
        let contentPassage = document.getElementById("content_passage");
        let textHead = "•   ";
        let textMid = "   ——   ";
        for (let i = 0; i < resJSON.awardsList.length; i++) {
            let span = document.createElement("span");
            let spanText = document.createTextNode(resJSON.awardsList[i].detail);
            span.appendChild(spanText);
            let h4 = document.createElement("h4");
            let h4TextHead = document.createTextNode(textHead + resJSON.awardsList[i].time + textMid);
            h4.appendChild(h4TextHead);
            h4.appendChild(span);
            contentPassage.appendChild(h4);//在div content_passage中依次添加各经历项
        }
    });
};