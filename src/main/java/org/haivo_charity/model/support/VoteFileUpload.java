package org.haivo_charity.model.support;

import org.haivo_charity.model.Vote;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class VoteFileUpload extends Vote {
    private CommonsMultipartFile[] fileDatas;
    public VoteFileUpload(){
    }
    public CommonsMultipartFile[] getFileDatas() {
        return fileDatas;
    }
    public void setFileDatas(CommonsMultipartFile[] fileDatas) {
        this.fileDatas = fileDatas;
    }
}
