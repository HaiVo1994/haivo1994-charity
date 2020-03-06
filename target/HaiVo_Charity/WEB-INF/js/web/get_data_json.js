const urlRoot = 'http://localhost:8080/';
var getJson = function (uri) {
    var dataUrl = urlRoot + uri;
    return $.ajax(
        {
            url: dataUrl ,
            method: 'GET',
            dataType: 'json',
            contentType: 'application/json'
        }
    );
};

var postJson = function (uri, object) {
    var dataUrl = urlRoot + uri;
    return $.ajax(
        {
            url: dataUrl ,
            method: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(object)
        }
    );
};
function getStringDate(date){
    var month = date.getMonth() +1;
    return date.getDate()  + "/" + month + "/" + date.getFullYear()
}

class JsonHome{
    getNumber_AmountDonate(local) {
        getJson('donateRest/getAmountDonate').done(
            function (data) {
                if (typeof data === "undefined")
                    data = 0;
                $(local).html(data + "$");
            }
        );
    }

    getNumberVolunteer (local) {
        getJson('volunteerRest/getAmountVolunteer').done(
            function (data) {
                $(local).html(data);
            }
        );
    }

    getNumberVolunteerRegisterEvent(local){
        getJson('volunteerRest/getNumberVolunteerRegisterEvent').done(
            function (data) {
                $(local).html(data);
            }
        );
    }

    getNumberEventFinish(local){
        getJson('voteRest/countEventFinish').done(
            function (data) {
                $(local).html(data);
            }
        );
    }

    static getList(value, raised){
        if (typeof raised === "undefined")
            raised = 0;
        var totalDonate = raised;
        var percentDonate = ((totalDonate/value.goal)*100).toFixed(2),
            linkCause = "/vote/" + value.id,
            imageSrc = "",
            contentOfVote = value.summary != null? value.summary: "Chưa Có",
            listImage = value.voteImages,
            imgIndex = 0;
        if (listImage.length>0){
            imgIndex = Math.floor(Math.random() * listImage.length);
            imageSrc = listImage[imgIndex].source;
        }
        else {
            imageSrc = "/img/post-1.jpg";
        }
        var progressBar = percentDonate>100? 100: percentDonate;
        var html =
            "<div class='col-md-4 main-card'>" +
                "<div class='causes'>" +
                    "<div class='causes-img'>" +
                        "<a href='" + linkCause + "'>" +
                            "<img src='" + imageSrc+ "' alt=''>" +
                        "</a>" +
                    "</div>" +
                    "<div class='causes-progress'>" +
                        "<div class='causes-progress-bar'>" +
                            "<div style='width: "+ progressBar +"%;'>" +
                                "<span>"+ percentDonate +"%</span>" +
                            "</div>" +
                        "</div>" +
                        "<div>" +
                            "<span class='causes-raised'>Đã Được: <strong>" + totalDonate + "$</strong></span>" +
                            "<span class='causes-goal'>Mục Tiêu: <strong>" + value.goal + "$</strong></span>" +
                        "</div>" +
                    "</div>" +
                    "<div class='causes-content'>" +
                        "<h3><a href='" + linkCause + "'>" + value.tittle + "</a></h3>" +
                        "<p class='summaryOfVote'>" + contentOfVote + "</p>" +
                        "<a href='" + linkCause + "' class='primary-button causes-donate'>Chi Tiết</a>" +
                    "</div>" +
                "</div>" +
            "</div>";
        return html;
    }

    getTop9VoteByDate () {
        getJson('voteRest/getTopVote/6').done(
            function (data) {
                $.each(data,
                    function (index, value) {
                        var urlJson = 'donateRest/donateAmount/' + value.id;
                        getJson(urlJson).done(
                            function (raised) {
                                $("#causes").find(".container").find(".row").append(
                                    JsonHome.getList(value, raised)
                                );
                            }
                        );
                    }
                );
            }
        );
    };
}

class JsonVote{
    getCategory(){
        // if (typeof $.cookie('categoryMenuList') === 'undefined'){
        //     this.setCookieCategory()
        // }
        // var data = $.cookie()
        getJson("categoryRest/listCategory").done(
            function (data) {
                $.each(data,
                    function (key, value) {
                        $(".widget-category").find("ul").append(
                            "<li><a href='/category/"+ key +"'>" +
                            value +
                            "</a></li>"
                        );
                    });
            }
        );
    }
    setCookieCategory(){

    }
    getAmountDonate(id){
        var urlJson = 'donateRest/donateAmount/' + id;
        console.log(parseInt($("#main").find(".causes-goal").find("strong").html()));
        getJson(urlJson).done(
            function (raised) {
                if (typeof raised === "undefined")
                    raised = 0;
                var processBar = $("#main").find(".causes-progress-bar").find("div"),
                    percentDonate = ((raised/ parseInt($("#main").find(".causes-goal").find("strong").html()))*100).toFixed(2);
                var progressBarShow = percentDonate>100? 100: percentDonate;
                processBar.attr("style", "width:" + progressBarShow + "%");
                processBar.find("span").html(percentDonate + "%");
                $("#main").find(".causes-raised").find("strong").html(raised + "$");
            }
        );
    }
    getData(id){
        var votePage = this;
        getJson("voteRest/"+id).done(
            function (data) {
                var main = $("#main"),
                    listImage = data.voteImages,
                    imgIndex = 0,
                    imgLocation = main.find(".article-img").find("img");
                if (listImage.length>0){
                    imgIndex = Math.floor(Math.random() * listImage.length);
                    imgLocation.attr("src", listImage[imgIndex].source);
                }
                else {
                    imgLocation.attr("src", "/img/post-1.jpg");
                }
                main.find(".article-content").find(".article-title").html(data.tittle);
                var dateBegin = new Date(data.beginDate),
                    dateFinish = new Date(data.finishDate),
                    toDay = new Date();
                if (dateFinish>=toDay){
                    $("#createEventButton").hide();
                }
                else {
                    $("#createEventButton").show();
                }
                main.find(".article-content").find(".article-meta")
                    .append("<li>Ngày Bắt Đầu: " + getStringDate(dateBegin) + "</li>");
                main.find(".article-content").find(".article-meta")
                    .append("<li>Ngày Kết Thúc: " + getStringDate(dateFinish) + "</li>");
                $("#contentOfVote").append("<p class='content'>" + data.content + "</p>");
                main.find(".causes-goal").find("strong").html(data.goal);
                main.find(".causes-goal").append("$");
                votePage.getAmountDonate(id);
            }
        );
    }

    getTag(id){
        getJson('categoryRest/categoryOfVote/' + id).done(
            function (category) {
                $.each(category,
                    function (index, row) {
                        $("#main").find(".article-tags-share").find(".tags").append("<li><a href='/category/"+ row.id +"'>" + row.name + "</a></li>");
                    })
            }
        );
    }

    getTop3Vote(){
        var linkVote = "";
        getJson('voteRest/getTopVote/3').done(
            function (votes) {
                $.each(votes,
                    function (index, vote) {
                        var urlJson = 'donateRest/donateAmount/' + vote.id;
                        $("#top3Vote").html("<h3 class=\"widget-title\">Gần Nhất</h3>");
                        getJson(urlJson).done(
                            function (raised) {
                                if (typeof raised === "undefined")
                                    raised = 0;
                                var percentDonate = ((raised/vote.goal)*100).toFixed(2),
                                    listImage = vote.voteImages,
                                    imgIndex = 0,
                                    imgLocation = "",
                                    processBar = percentDonate>100?100: percentDonate;
                                if (listImage.length>0){
                                    imgIndex = Math.floor(Math.random() * listImage.length);
                                    imgLocation = listImage[imgIndex].source;
                                }
                                else {
                                    imgLocation = "/img/post-1.jpg";
                                }
                                linkVote = "/vote/" + vote.id;
                                $("#top3Vote").append(
                                    "<div class='widget-causes main-card'>" +
                                    "<a href='" + linkVote + "'>" +
                                    "<div class='widget-img'>" +
                                    "<img src='" +imgLocation+ "' alt=''>" +
                                    "</div>" +
                                    "<div class='widget-content'>" +
                                    "<div id='voteTittle'>"+ vote.tittle +"</div>" +
                                    "<div class='causes-progress'>" +
                                    "<div class='causes-progress-bar'>" +
                                    "<div style='width: " + processBar + "%;'></div>\n" +
                                    "</div>" +
                                    "</div>" +
                                    "</div>" +
                                    "</a>" +
                                    "<div>" +
                                    "<span class='causes-raised'>Đã Được: <strong>" + raised + "</strong></span> -" +
                                    "<span class='causes-goal'>Mục Tiêu: <strong>" + vote.goal + "</strong></span>" +
                                    "</div>" +
                                    "</div>"
                                );
                            }
                        );
                    }
                );
            }
        );
    }

    donateNoLogin(id){
        var url = "donateRest/donateNoLogin/" + id;
        var form = $("#formDonate");
        var votePage = this;
        votePage.hideErrorModal();
        var object = {
            "name": form.find("input[name='name']").val(),
            "email": form.find("input[name='email']").val(),
            "amount": form.find("input[name='amount']").val(),
            "hide": $("#isHidedAny").val()
        };
        postJson( url, object).done(
            function (data) {
                if(data.status === "OK"){
                    $("#donateModal").modal("hide");
                    votePage.clearModal();
                    votePage.getAmountDonate(id);
                    votePage.getTop3Vote();
                    var messengerModal = "Cảm Ơn Bạn Vì Đã Đóng Góp!";
                    $("#alertModal").find(".modal-body").find("p").html(messengerModal);
                    $("#alertModal").modal('show');
                    transferPage(0);
                }
                else{
                    // if (!(typeof data.errorForEmail === "undefined")){
                    //     form.find(".errorForEmailVolunteer").show();
                    //     form.find(".errorForEmailVolunteer").html(data.errorForEmail);
                    // }
                    if (!(typeof data.errorForMoney === "undefined")){
                        form.find(".errorForMoneyDonate").show();
                        form.find(".errorForMoneyDonate").html(data.errorForMoney);
                    }
                }
            }
        )
    }
    donateLogin(id){
        var url = "donateRest/donateLogier/" + id;
        var form = $("#formDonateLogin");
        var votePage = this;
        form.find(".errorForMoneyDonate").hide();
        var object = {
            "amount": $("#moneyDonateLogin").val(),
            "hide": $("#isHidedLogin").val()
        };
        postJson( url, object).done(
            function (data) {
                if(data.status === "OK"){
                    $("#donateModal").modal("hide");
                    votePage.clearModal();
                    votePage.getAmountDonate(id);
                    votePage.getTop3Vote();
                    var messengerModal = "Cảm Ơn Bạn Vì Đã Đóng Góp!";
                    $("#alertModal").find(".modal-body").find("p").html(messengerModal);
                    $("#alertModal").modal('show');
                    transferPage(0);
                }
                else{
                    if (!(typeof data.errorForMoney === "undefined")){
                        form.find(".errorForMoneyDonate").show();
                        form.find(".errorForMoneyDonate").html(data.errorForMoney);
                    }
                }
            }
        )
    }
    hideErrorModal(){
        // var form = $("#formDonate");
        $(".errorForEmailVolunteer").hide();
        $(".errorForPhoneVolunteer").hide();
        $(".errorForMoneyDonate").hide();
    }
    clearModal(){
        var form = $("#formDonate");
        this.hideErrorModal();
        form.find("input[name='name']").val("");
        form.find("input[name='email']").val("");
        form.find("input[name='phone']").val("");
        form.find("input[name='amount']").val(0);
    }

    getListDonate(id,page,size){
        var table = $("#listPersonDonate").find("tbody"),
            url = "donateRest/getListDonateOfVote/" + id + "/" + page + "/" + size;
        getJson(url).done(
            function (data) {
                table.html("");
                var date;
                $.each(data,
                    function (index, donate) {
                        date= new Date(donate.date);
                        table.append(
                            "<tr>" +
                            "<td>" + donate.personName + "</td>"+
                            "<td>" + donate.amount + "</td>"+
                            "<td>" + date + "</td>"+
                            "</tr>"
                        );
                    });

            }
        );
    }
}

class JsonCategory {
    getListCategory(){
        getJson("categoryRest/listCategory").done(
            function (data) {
                $.each(data,
                    function (key, value) {
                        $("#listCause").append(
                            "<li><a href='/category/"+ key +"'>" +
                            value +
                            "</a></li>"
                        );
                    });
            }
        );
    }
    getVoteByCategory (id, page, size, search) {
        $("#causesByCategory").html("");
        var jsonData;
        if (!(search == null)){
            var searchObject  =
                {
                    "search" : search
                };
            jsonData = postJson("voteRest/getListByTittle/" + page + "/" + size, searchObject)
        }
        else
        if (id != null){
            jsonData = getJson("voteRest/getListPageCategory/" + id + "/" + page + "/" + size);
        }
        else{
            jsonData = getJson("voteRest/getListPage/" + page + "/" + size);
        }
        jsonData.done(
            function (data) {
                $.each(data,
                    function (index, value) {
                        var urlJson = 'donateRest/donateAmount/' + value.id;
                        getJson(urlJson).done(
                            function (raised) {
                                $("#causesByCategory").append(
                                    JsonHome.getList(value, raised)
                                );
                            }
                        );
                    }
                );
            }
        );
    }

    setPage(id, page, size, search){
        var jsonData,
            pagination = $("#main").find(".pagination");
        if (!(search == null)){
            var searchObject  ={
                "search" : search
            };
            jsonData = postJson("voteRest/countByTittle", searchObject)
        }
        else
        if (id != null){
            jsonData = getJson("categoryRest/countVote/" + id);
        }
        else{
            jsonData = getJson("categoryRest/countAll");
        }

        var previousPage = page - 1;
        if (previousPage >= 0){
            pagination.html("<li>" +
                "<a class='page-link' tabindex='-1' onclick='transferPage(" + previousPage + ");'>Previous</a>" +
                "</li>");
        }
        else {
            pagination.html("<li class='disabled'>" +
                "<a class='page-link' onclick='transferPage(" + page + ");' tabindex='-1'>Previous</a>" +
                "</li>");
        }

        jsonData.done(
            function (data) {
                var totalPage = Math.ceil(data/size);
                var show = 0;
                for (var i=0; i<totalPage;i++){
                    show = i + 1;
                    if (i === page)
                        pagination.append("<li class='active'>" +
                            "<a onclick='transferPage(" + i + ");' tabindex='-1'>" + show + "</a>" +
                            "</li>");
                    else
                        pagination.append("<li value=''>" +
                            "<a onclick='transferPage(" + i + ");' tabindex='-1'>" + show + "</a>" +
                            "</li>");
                }
                var nextPage = page + 1;
                if (nextPage<totalPage){
                    pagination.append("<li>" +
                        "<a onclick='transferPage(" + nextPage + ");' tabindex='-1'>Next</a>" +
                        "</li>");
                }
                else{
                    pagination.append("<li class='disabled'>" +
                        "<a onclick='transferPage(" + page + ");' tabindex='-1'>Next</a>" +
                        "</li>");
                }

            }
        );
    }
}