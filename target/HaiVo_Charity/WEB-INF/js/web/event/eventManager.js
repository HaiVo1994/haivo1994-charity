class EventManager {
    getListVote(page,size){
        var causesPast = $("#causesPast"),
            url ="event_manager/getVote/" + page + "/" + size;

        getJson(url).done(
            function (data) {
                var html = "";
                causesPast.html(html);
                $.each(data,
                    function (index, proposal) {
                        var linkCause = "/event_manager/create_event/" + proposal.id,
                            imageSrc = "",
                            contentOfVote = proposal.summary != null? proposal.summary: "Chưa Có",
                            listImage = proposal.voteImages,
                            imgIndex = 0;

                        if (listImage.length>0){
                            imgIndex = Math.floor(Math.random() * listImage.length);
                            imageSrc = listImage[imgIndex].source;
                        }
                        else {
                            imageSrc = "/img/post-1.jpg";
                        }
                        html =
                            "<div class='col-md-4 main-card'>" +
                            "<div class='causes'>" +
                            "<div class='causes-img'>" +
                            "<a href='" + linkCause + "'>" +
                            "<img src='" + imageSrc+ "' alt=''>" +
                            "</a>" +
                            "</div>" +
                            "<div class='causes-progress'>" +
                            "<div>" +
                            "<span class='causes-goal'>Goal: <strong>" + proposal.goal + "$</strong></span>" +
                            "</div>" +
                            "</div>" +
                            "<div class='causes-content'>" +
                            "<h3><a href='" + linkCause + "'>" + proposal.tittle + "</a></h3>" +
                            "<p>" + contentOfVote + "</p>" +
                            "<a href='" + linkCause +
                            "' class='primary-button causes-donate'>Tạo Sự Kiện</a>" +
                            "<a class='primary-button causes-donate' href='/vote/" +proposal.id+"'>Xem Chi Tiết</a>" +
                            "</div>" +
                            "</div>" +
                            "</div>";
                        causesPast.append(html);
                    }
                );
            }
        );
    }
    setPage(page,size){
        var previousPage = page - 1,
            pagination = $("#main").find(".pagination"),
            url = "event_manager/getVotePage/" + size;
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

        getJson(url).done(
            function (data) {
                var totalPage = data;
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