//清除content_passage的所有子节点
function clearContentPassage() {
    let contentPassage = document.getElementById("content_passage");
    let childNodes = contentPassage.childNodes;
    for (let i = childNodes.length - 1; i >= 0; i--)
        contentPassage.removeChild(childNodes[i]);
}

//根据参数指定列名，创建Key-Value Header，作为子节点添加到parent下
function createKVHeader(parent, keyTitle, valueTitle) {
    let keyPara = document.createElement("p");
    let keyParaText = document.createTextNode(keyTitle);
    keyPara.className = "keyValue_header";
    keyPara.appendChild(keyParaText);

    let valuePara = document.createElement("p");
    let valueParaText = document.createTextNode(valueTitle);
    valuePara.className = "keyValue_header";
    valuePara.appendChild(valueParaText);

    parent.appendChild(keyPara);
    parent.appendChild(valuePara);
}

//输入Key和Value的值，创建Key-Value Text，返回wrapper节点
function createKVText(keyStr, valueStr) {
    let inputWrapper = document.createElement("div");
    inputWrapper.className = "input_wrapper";

    let keyText = document.createElement("input");
    keyText.className = "key_text";
    keyText.type = "text";
    keyText.value = keyStr;
    inputWrapper.appendChild(keyText);

    let valueText = document.createElement("input");
    valueText.className = "value_text";
    valueText.type = "text";
    valueText.value = valueStr;
    inputWrapper.appendChild(valueText);

    let delButton = document.createElement("input");
    delButton.type = "button";
    delButton.value = "DEL";
    delButton.onclick = function () {
        //点击后从祖父节点中删除父节点
        let delParent = this.parentNode;
        let delGrandpa = delParent.parentNode;
        delGrandpa.removeChild(delParent);
    };
    inputWrapper.appendChild(delButton);

    return inputWrapper;
}

//创建AddButton，返回button节点
function createAddButton() {
    let addButton = document.createElement("input");
    addButton.className = "add_button";
    addButton.type = "button";
    addButton.value = "ADD NEW ITEM";
    addButton.onclick = function () {
        //点击后在AddButton前添加一个值为空的KV Text
        let kvText = createKVText("", "");
        let addButtonParent = this.parentNode;
        addButtonParent.insertBefore(kvText, addButton);
    };
    return addButton;
}

//创建SubmitButton，返回button节点
function createSubmitButton() {
    let submitButton = document.createElement("input");
    submitButton.className = "submit_button";
    submitButton.type = "button";
    submitButton.value = "SUBMIT";
    return submitButton;
}

//组装Key-Value数据，返回组装好的对象
function loadKV() {
    let keys = document.getElementsByClassName("key_text");
    let values = document.getElementsByClassName("value_text");
    let dataLength = keys.length;
    let loadData = [];

    //只有当Key和Value都不为空时才进行传送
    for (let i = 0; i < dataLength; i++)
        if (keys[i].value.length > 0 && values[i].value.length > 0) {
            let obj = {
                key: keys[i].value,
                value: values[i].value
            };
            loadData.push(obj);
        }

    return loadData;
}

//加载主页设置页面
function loadHome() {
    document.getElementById("config_header").innerText = "HOME";
    ajaxHttpRequest("POST", "/getHomeInfo", JSON.stringify(globalAjaxData), function (res) {
        let resJSON = JSON.parse(res);
        let contentPassage = document.getElementById("content_passage");
        let homeWrapper = document.createElement("div");
        homeWrapper.id = "home_wrapper";

        //拼接主页内容，创建textarea节点
        let textareaContent = "";
        for (let i = 0; i < resJSON.contentChips.length; i++)
            textareaContent += resJSON.contentChips[i] + "\n";
        let textarea = document.createElement("textarea");
        let textNode = document.createTextNode(textareaContent);
        textarea.appendChild(textNode);
        homeWrapper.appendChild(textarea);

        //创建图片节点
        let homeImg = document.createElement("img");
        homeImg.src = resJSON.homeImgPath;//加载图片路径
        homeImg.alt = "Home";
        homeWrapper.appendChild(homeImg);

        //创建提交按钮
        let homeSubmit = createSubmitButton();
        homeSubmit.onclick = function () {
            //将此时的信息发送到后端存储
            let data = {
                content: textarea.value
            };
            ajaxHttpRequest("POST", "/setHomeInfo", JSON.stringify(data), function (res) {
                let setResJSON = JSON.parse(res);
                let alertStr = "Code: " + setResJSON.code + "\nStatus: " + setResJSON.status;
                alert(alertStr);
            });
        };
        homeWrapper.appendChild(homeSubmit);

        //将wrapper添加到content_passage
        contentPassage.appendChild(homeWrapper);
    });
}

//加载个人简介设置页面
function loadAbout() {
    document.getElementById("config_header").innerText = "ABOUT";
    ajaxHttpRequest("POST", "/getAboutInfo", JSON.stringify(globalAjaxData), function (res) {
        let resJSON = JSON.parse(res);
        let contentPassage = document.getElementById("content_passage");
        let aboutWrapper = document.createElement("div");
        aboutWrapper.id = "about_wrapper";

        //创建Header
        createKVHeader(aboutWrapper, "Item:", "Content:");

        //为已存在的信息创建输入框
        for (let i = 0; i < resJSON.aboutList.length; i++) {
            let wrapper = createKVText(resJSON.aboutList[i].itemName, resJSON.aboutList[i].itemContent);
            aboutWrapper.appendChild(wrapper);
        }

        //创建AddButton
        let aboutAddButton = createAddButton();
        aboutWrapper.appendChild(aboutAddButton);

        //创建图片节点
        let aboutImg = document.createElement("img");
        aboutImg.src = resJSON.aboutImgPath;//加载图片路径
        aboutImg.alt = "About";
        aboutWrapper.appendChild(aboutImg);

        //创建包裹按钮的Flex容器和提交按钮
        let flexContainer = document.createElement("div");
        flexContainer.className = "flex_container";
        let aboutSubmit = createSubmitButton();
        aboutSubmit.onclick = function () {
            //将此时的信息发送到后端存储
            let data = loadKV();
            ajaxHttpRequest("POST", "/setAboutInfo", JSON.stringify(data), function (res) {
                let setResJSON = JSON.parse(res);
                let alertStr = "Code: " + setResJSON.code + "\nStatus: " + setResJSON.status;
                alert(alertStr);
            });
        };
        flexContainer.appendChild(aboutSubmit);
        aboutWrapper.appendChild(flexContainer);

        //将wrapper添加到content_passage
        contentPassage.appendChild(aboutWrapper);
    });
}

//加载经历设置页面
function loadExperience() {
    document.getElementById("config_header").innerText = "EXPERIENCE";
    ajaxHttpRequest("POST", "/getExperienceInfo", JSON.stringify(globalAjaxData), function (res) {
        let resJSON = JSON.parse(res);
        let contentPassage = document.getElementById("content_passage");
        let experienceWrapper = document.createElement("div");
        experienceWrapper.id = "experience_wrapper";

        //创建Header
        createKVHeader(experienceWrapper, "Time Range:", "Experience:");

        //为已存在的信息创建输入框
        for (let i = 0; i < resJSON.experienceList.length; i++) {
            let wrapper = createKVText(resJSON.experienceList[i].itemTitle, resJSON.experienceList[i].itemContent);
            experienceWrapper.appendChild(wrapper);
        }

        //创建AddButton
        let experienceAddButton = createAddButton();
        experienceAddButton.className = "submit_button";//重写AddButton的类名，换用SubmitButton的CSS
        experienceWrapper.appendChild(experienceAddButton);

        //创建换行符
        let br = document.createElement("br");
        experienceWrapper.appendChild(br);

        //创建SubmitButton
        let experienceSubmit = createSubmitButton();
        experienceSubmit.onclick = function () {
            //将此时的信息发送到后端存储
            let data = loadKV();
            ajaxHttpRequest("POST", "/setExperienceInfo", JSON.stringify(data), function (res) {
                let setResJSON = JSON.parse(res);
                let alertStr = "Code: " + setResJSON.code + "\nStatus: " + setResJSON.status;
                alert(alertStr);
            });
        };
        experienceWrapper.appendChild(experienceSubmit);

        //将wrapper添加到content_passage
        contentPassage.appendChild(experienceWrapper);
    });
}

//加载获奖设置页面
function loadAwards() {
    document.getElementById("config_header").innerText = "AWARDS";
    ajaxHttpRequest("POST", "/getAwardsInfo", JSON.stringify(globalAjaxData), function (res) {
        let resJSON = JSON.parse(res);
        let contentPassage = document.getElementById("content_passage");
        let awardsWrapper = document.createElement("div");
        awardsWrapper.id = "awards_wrapper";

        //创建Header
        createKVHeader(awardsWrapper, "Time:", "Award Name:");

        //为已存在的信息创建输入框
        for (let i = 0; i < resJSON.awardsList.length; i++) {
            let wrapper = createKVText(resJSON.awardsList[i].time, resJSON.awardsList[i].detail);
            awardsWrapper.appendChild(wrapper);
        }

        //创建AddButton
        let awardsAddButton = createAddButton();
        awardsAddButton.className = "submit_button";//重写AddButton的类名，换用SubmitButton的CSS
        awardsWrapper.appendChild(awardsAddButton);

        //创建换行符
        let br = document.createElement("br");
        awardsWrapper.appendChild(br);

        //创建SubmitButton
        let awardsSubmit = createSubmitButton();
        awardsSubmit.onclick = function () {
            //将此时的信息发送到后端存储
            let data = loadKV();
            ajaxHttpRequest("POST", "/setAwardsInfo", JSON.stringify(data), function (res) {
                let setResJSON = JSON.parse(res);
                let alertStr = "Code: " + setResJSON.code + "\nStatus: " + setResJSON.status;
                alert(alertStr);
            });
        };
        awardsWrapper.appendChild(awardsSubmit);

        //将wrapper添加到content_passage
        contentPassage.appendChild(awardsWrapper);
    });
}

//加载图片墙设置页面
function loadPictures() {
    document.getElementById("config_header").innerText = "PICTURES";
    // ajaxHttpRequest("POST", "/getPictures", JSON.stringify(globalAjaxData), function (res) {
    //     let resJSON = JSON.parse(res);
    //     let contentPassage = document.getElementById("content_passage");
    //
    // });
}

//加载页面时获取头像信息及主页信息
window.onload = function () {
    getCommonInfo();
    loadHome();

    //为nav中各a标签添加点击事件
    document.getElementById("home_entrance").onclick = function () {
        clearContentPassage();
        loadHome();
    };
    document.getElementById("about_entrance").onclick = function () {
        clearContentPassage();
        loadAbout();
    };
    document.getElementById("experience_entrance").onclick = function () {
        clearContentPassage();
        loadExperience();
    };
    document.getElementById("awards_entrance").onclick = function () {
        clearContentPassage();
        loadAwards();
    };
    document.getElementById("pictures_entrance").onclick = function () {
        clearContentPassage();
        loadPictures();
    };

    //为返回按钮添加点击事件
    document.getElementById("back_button").onclick = function () {
        window.location.href = "/";
    }
};
