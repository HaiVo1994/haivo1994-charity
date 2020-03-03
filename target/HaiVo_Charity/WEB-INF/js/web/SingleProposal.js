class SingleProposal {
    getData(id){
        getJson("proposal_admin/getProposal/"+id).done(
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
                    dateEnd = new Date(data.finishDate);
                main.find(".article-content").find(".article-meta").append("<li>Ngày Bắt Đầu:" + getStringDate(dateBegin) + "</li>");
                main.find(".article-content").find(".article-meta").append("<li>Ngày Kết Thúc:" + getStringDate(dateEnd) + "</li>");
                $("#contentOfVote").append("<p class='content'>" + data.content + "</p>");
                main.find(".causes-goal").find("strong").html(data.goal);
                main.find(".causes-goal").append("$");
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
    accept(id){
        var dataUrl = urlRoot + "proposal_admin/acceptProposal/" + id;
        $.ajax({
                url: dataUrl ,
                method: 'PUT',
                dataType: 'json',
                contentType: 'application/json'
            })
            .always(function (data) {
                $("#donateModal").modal("hide");
                alert("Bạn Đã Chấp Nhận Đề Nghị");
            }
        );
    }
}