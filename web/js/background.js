//分别用于保存Home和About页中选择的图片文件
let homeImgSelectedFile = null;
let aboutImgSelectedFile = null;

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

//获得选定文件的路径，用于更新img标签的src
function getFileURL(file) {
    let url = null ;
    if (window.createObjectURL !== undefined) {
        url = window.createObjectURL(file) ;
    } else if (window.URL !== undefined) {
        url = window.URL.createObjectURL(file) ;
    } else if (window.webkitURL !== undefined) {
        url = window.webkitURL.createObjectURL(file) ;
    }
    return url;
}

//创建File Browser选择文件，当选择的文件满足大小限制后调用elseCall
function createFileBrowser(elseCall) {
    let fileBrowser = document.createElement("input");
    fileBrowser.type = "file";
    fileBrowser.accept = "image/jpg";
    fileBrowser.onchange = function () {
        let selectedFile = fileBrowser.files[0];
        if (selectedFile.size > 1048576) //选择的图片大于1MB弹出拒绝信息
            alert("Please select a jpg file smaller than 1MB!");
        else //将文件传给回调函数
            elseCall(selectedFile);
    };
    fileBrowser.click();
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
        //为图片设置点击事件，用于更新
        homeImg.onclick = function () {
            createFileBrowser(function (selectedFile) {
                homeImg.src = getFileURL(selectedFile);
                homeImgSelectedFile = selectedFile;
            });
        };
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
            //同时检查图片是否被更改，若是，则提交到后端
            if (homeImgSelectedFile !== null) {
                let formData = new FormData();
                formData.append("file", homeImgSelectedFile);

                ajaxHttpRequest("POST", "/setHomeImg", formData, function (res) {
                    let setResJSON = JSON.parse(res);
                    let alertStr = "Code: " + setResJSON.code + "\nStatus: " + setResJSON.status;
                    alert(alertStr);
                });

                homeImgSelectedFile = null;
            }
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
        //为图片设置点击事件，用于更新
        aboutImg.onclick = function () {
            createFileBrowser(function (selectedFile) {
                aboutImg.src = getFileURL(selectedFile);
                aboutImgSelectedFile = selectedFile;
            });
        };
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
            //同时检查图片是否被更改，若是，则提交到后端
            if (aboutImgSelectedFile !== null) {
                let formData = new FormData();
                formData.append("file", aboutImgSelectedFile);

                ajaxHttpRequest("POST", "/setAboutImg", formData, function (res) {
                    let setResJSON = JSON.parse(res);
                    let alertStr = "Code: " + setResJSON.code + "\nStatus: " + setResJSON.status;
                    alert(alertStr);
                });

                aboutImgSelectedFile = null;
            }
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

    //创建水平分隔线并返回
    function createHorizontalDivider() {
        let horizontalDivider = document.createElement("div");
        horizontalDivider.className = "horizontal_divider";
        return horizontalDivider;
    }

    //创建居中容器并返回
    function createFlexContainer() {
        let flexContainer = document.createElement("div");
        flexContainer.className = "flex_container";
        return flexContainer;
    }

    //创建浮动清除器并返回
    function createFloatClearer() {
        let floatClearer = document.createElement("div");
        floatClearer.className = "float_clearer";
        return floatClearer;
    }

    //创建指定value和点击回调的Button并返回
    function createButtonWithValueAndCallback(btnValue, callback) {
        let button = document.createElement("input");
        button.type = "button";
        button.value = btnValue;
        button.onclick = callback;
        return button;
    }

    //创建指定title的wrapper
    function createTitleWrapper(title) {
        let titleWrapper = document.createElement("div");
        titleWrapper.className = "title_wrapper";

        //创建title文本
        let span = document.createElement("span");
        let spanText = document.createTextNode(title);
        span.appendChild(spanText);

        //创建删除按钮
        let delButton = createButtonWithValueAndCallback("DEL", function () {
            //TODO
        });

        let floatClearer = createFloatClearer();

        titleWrapper.appendChild(span);
        titleWrapper.appendChild(delButton);
        titleWrapper.appendChild(floatClearer);
        return titleWrapper;
    }

    ajaxHttpRequest("POST", "/getPictures", JSON.stringify(globalAjaxData), function (res) {
        let resJSON = JSON.parse(res);
        let contentPassage = document.getElementById("content_passage");
        let spanHead = "Pieces of ";

        for (let i = 0; i< resJSON.length; i++) {
            //创建某一年的块
            let yearWrapper = document.createElement("div");
            yearWrapper.className = "year_wrapper";

            //创建Header
            let header = createTitleWrapper(spanHead + resJSON[i].year);
            yearWrapper.appendChild(header);
            yearWrapper.appendChild(createHorizontalDivider());

            for (let j = 0; j < resJSON[i].pathList.length; j++) {
                //创建图片
                let img = document.createElement("img");
                img.src = resJSON[i].pathList[j];
                img.alt = "Picture";
                yearWrapper.appendChild(img);

                //创建操作按钮
                let cdFlexContainer = createFlexContainer();
                let changePicButton = createButtonWithValueAndCallback("CHANGE", function () {
                    //TODO
                });
                changePicButton.className = "switch_button";
                let delPicButton = createButtonWithValueAndCallback("DEL", function () {
                   //TODO
                });
                delPicButton.className = "switch_button";
                cdFlexContainer.appendChild(changePicButton);
                cdFlexContainer.appendChild(delPicButton);
                yearWrapper.appendChild(cdFlexContainer);
            }

            //在YearWrapper底部创建新增图片和提交图片按钮
            let addPicButton = createButtonWithValueAndCallback("ADD A PICTURE", function () {

            });
            addPicButton.className = "add_pic_button";
            let submitPicButton = createButtonWithValueAndCallback("SUBMIT", function () {

            });
            submitPicButton.className = "submit_pic_button";
            yearWrapper.appendChild(addPicButton);
            yearWrapper.appendChild(submitPicButton);
            yearWrapper.appendChild(createFloatClearer());

            //将本年度图片加入Content
            contentPassage.appendChild(yearWrapper);
        }

        //在底部创建添加年份按钮
        contentPassage.appendChild(createHorizontalDivider());

        let picBottomFlex = createFlexContainer();
        picBottomFlex.id = "pic_bottom_flex";

        let newYearButton = createButtonWithValueAndCallback("ADD A NEW YEAR", function () {
            //TODO
        });

        picBottomFlex.appendChild(newYearButton);
        contentPassage.appendChild(picBottomFlex);
    });
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

        //返回后请求注销session
        ajaxHttpRequest("POST", "/invalidateSession", "");
    };

    //为头像设置点击事件，打开文件浏览器
    document.getElementById("avatar_img").onclick = function () {
        createFileBrowser(function (selectedFile) {
            let formData = new FormData();
            formData.append("file", selectedFile);

            ajaxHttpRequest("POST", "/setAvatar", formData, function (res) {
                let setResJSON = JSON.parse(res);
                let alertStr = "Code: " + setResJSON.code + "\nStatus: " + setResJSON.status;
                alert(alertStr);
                if (setResJSON.code === 1) //上传成功时才更改显示
                    document.getElementById("avatar_img").src = getFileURL(selectedFile);
            });
        });
    }
};
