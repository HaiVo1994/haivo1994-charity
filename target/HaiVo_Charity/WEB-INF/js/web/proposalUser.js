class ProposalUserManager {
    getListProposal(page,size,accept){
        var proposalList = $("#proposalList"),
            url ="";
        if (!accept){
            url = "proposalRest/getAllProposalUser/" + page + "/" + size;
        }
        else {
            url = "proposalRest/getAllVoteUser/" + page + "/" + size;
        }
        getJson(url).done(
            function (data) {
                var html = "";
                proposalList.html(html);
                $.each(data,
                    function (index, proposal) {
                        var linkCause = "/loginManagement/managerProposal/" + proposal.id,
                            imageSrc = "",
                            contentOfVote = proposal.summary != null? proposal.summary: "Chưa Có",
                            listImage = proposal.voteImages,
                            imgIndex = 0,
                            buttonText = "Sửa";
                        if (accept){
                            linkCause = "/vote/" + proposal.id;
                            buttonText = "Chi Tiết"
                        }
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
                            "<p class='summaryOfVote'>" + contentOfVote + "</p>" +
                            "<a href='" + linkCause +
                                "' class='primary-button causes-donate'>" + buttonText + "</a>" +
                            "</div>" +
                            "</div>" +
                            "</div>";
                        proposalList.append(html);
                    }
                );
            }
        );
    }
    setPage(page,size, accept){
        var previousPage = page - 1,
            pagination = $("#main").find(".pagination"),
            url = "";
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
        if (!accept){
            url = "proposalRest/getProposalUser/" + size;
        }
        else {
            url = "proposalRest/getVoteUser/" + size;
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