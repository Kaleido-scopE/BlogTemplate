//加载页面时从后端获得照片墙中照片路径
window.onload = function () {
    getCommonInfo();
    ajaxHttpRequest("POST", "/getPictures", JSON.stringify(globalAjaxData), function (res) {
        let resJSON = JSON.parse(res);
        let contentPassage = document.getElementById("content_passage");
        let h3Head = "Pieces of ";
        for (let i = 0; i < resJSON.length; i++) {
            let h3 = document.createElement("h3");
            let text = document.createTextNode(h3Head + resJSON[i].year);
            h3.appendChild(text);
            contentPassage.appendChild(h3);
            for (let j = 0; j < resJSON[i].pathList.length; j++) {
                let img = document.createElement("img");
                img.src = resJSON[i].pathList[j];
                img.alt = "Picture";
                contentPassage.appendChild(img);
            }
        }
    });
};