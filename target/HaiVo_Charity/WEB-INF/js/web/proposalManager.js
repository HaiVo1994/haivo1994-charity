class ProposalManager {
    getListProposal(page,size){
        var proposalList = $("#proposalList");
        getJson("proposal_admin/getAllProposal/" + page + "/" + size).done(
            function (data) {
                var html = "";
                proposalList.html(html);
                $.each(data,
                    function (index, proposal) {
                        var linkCause = "/proposal_admin/proposal/" + proposal.id,
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
                                        "<p class='summaryOfVote'>" + contentOfVote + "</p>" +
                                        "<a href='" + linkCause + "' class='primary-button causes-donate'>Chi Tiết</a>" +
                                    "</div>" +
                                "</div>" +
                            "</div>";
                        proposalList.append(html);
                    }
                );
            }
        );
    }
    setPage(page,size){
        var previousPage = page - 1,
            pagination = $("#main").find(".pagination");
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

        getJson("proposal_admin/getProposalPage/" + size).done(
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