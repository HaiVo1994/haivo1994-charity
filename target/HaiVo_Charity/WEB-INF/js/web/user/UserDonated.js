class UserDonated {
    getVoteByUser (page, size) {
        var thisClass = this;
        $("#causesByCategory").html("");
        getJson("donated/listVote/" +page + "/" + size).done(
            function (data) {
                $.each(data, function (index,row) {
                    $("#causesByCategory").append(thisClass.getCard(row));
                });
            }
        );
    }

    getCard(data){
        var imageSrc = "",
            contentOfVote = data.vote.summary != null? data.vote.summary: "Chưa Có",
            listImage = data.vote.voteImages,
            imgIndex = 0;
        if (listImage.length>0){
            imgIndex = Math.floor(Math.random() * listImage.length);
            imageSrc = listImage[imgIndex].source;
        }
        else {
            imageSrc = "/img/post-1.jpg";
        }
        var html =
            "<div class='col-md-4 main-card'>" +
            "<div class='causes'>" +
            "<div class='causes-img'>" +
            "<a href='" + data.link + "'>" +
            "<img src='" + imageSrc+ "' alt=''>" +
            "</a>" +
            "</div>" +
            "<div class='causes-progress'>" +
            "<div>" +
            "<span class='causes-raised'>Bạn Đã Đóng Góp: <strong>" + data.amountDonated + "$</strong></span>" +
            "</div>" +
            "</div>" +
            "<div class='causes-content'>" +
            "<h3><a href='" + data.link + "'>" + data.vote.tittle + "</a></h3>" +
            "<p class='summaryOfVote'>" + contentOfVote + "</p>" +
            "<a href='" + data.link + "' class='primary-button causes-donate'>Chi Tiết</a>" +
            "</div>" +
            "</div>" +
            "</div>";
        return html;
    }

    setPage(page, size){
        var pagination = $("#main").find(".pagination");

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

        getJson("donated/listVote/" + size).done(
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