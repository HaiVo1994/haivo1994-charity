package org.haivo_charity.controller.login;

import org.apache.commons.io.FilenameUtils;
import org.haivo_charity.model.Category;
import org.haivo_charity.model.Vote;
import org.haivo_charity.model.VoteImage;
import org.haivo_charity.model.support.RegExp;
import org.haivo_charity.model.support.VoteFileUpload;
import org.haivo_charity.service.CategoryService;
import org.haivo_charity.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

@Controller
@RequestMapping("/proposal")
public class ProposalController {
    @Autowired
    private VoteService voteService;
    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView goProposalPage() {
        ModelAndView modelAndView = new ModelAndView("/proposal/requestCreate");
        VoteFileUpload voteFileUpload = new VoteFileUpload();
        Iterable<Category> categories = categoryService.findAll();
        modelAndView.addObject("voteFileUpload", voteFileUpload);
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }
    @RequestMapping(value = "/yourList", method = RequestMethod.GET)
    public ModelAndView listProposal(){
        return new ModelAndView("/proposal/listProposal");
    }
    @RequestMapping(value = "/yourListVote", method = RequestMethod.GET)
    public ModelAndView listVote(){
        return new ModelAndView("/proposal/listProposal");
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        System.out.println("Target=" + target);

        if (target.getClass() == VoteFileUpload.class) {

            // Đăng ký để chuyển đổi giữa các đối tượng multipart thành byte[]
            dataBinder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
        }
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public ModelAndView createProposal(Principal principal, HttpSession session,
                                       @ModelAttribute VoteFileUpload voteFileUpload
    ) {
        String uploadRootPath = session.getServletContext().getRealPath("upload");
        try {
            URL res = getClass().getClassLoader().getResource("database.properties");
            assert res != null;
            Properties prop = new Properties();
            InputStream inputStream = new FileInputStream(Paths.get(res.toURI()).toFile());
            prop.load(inputStream);
            uploadRootPath = prop.getProperty("root");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        File uploadRootDir = new File(uploadRootPath);
        // Tạo thư mục gốc upload nếu nó không tồn tại.
        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdirs();
        }
        CommonsMultipartFile[] fileDatas = voteFileUpload.getFileDatas();
        Vote vote = new Vote();
        vote.setProposal_by(principal.getName());
        vote.setProposal_at(new Date());
        vote.setRepresentative(
                RegExp.removeHTML(voteFileUpload.getRepresentative())
        );
        vote.setLocalVote(
                RegExp.removeHTML(voteFileUpload.getLocalVote())
        );
        vote.setContent(
                RegExp.removeHTML(voteFileUpload.getContent())
        );
        vote.setTittle(
                RegExp.removeHTML(voteFileUpload.getTittle())
        );
        vote.setBeginDate(voteFileUpload.getBeginDate());
        vote.setFinishDate(voteFileUpload.getFinishDate());
        vote.setGoal(voteFileUpload.getGoal());
        vote.setSummary(
                RegExp.removeHTML(voteFileUpload.getSummary())
        );
        vote.setCategories(voteFileUpload.getCategories());

        List<VoteImage> voteImages = new ArrayList<VoteImage>();
        long time = System.currentTimeMillis();
        int countImage = 0;
        String name, nameOfFile;
        for (CommonsMultipartFile fileData : fileDatas) {
            // Tên file gốc tại Client.
            nameOfFile = fileData.getOriginalFilename();
//            System.out.println("Client File Name = " + name);
            if (nameOfFile != null && nameOfFile.length() > 0) {
                name = principal.getName() + "_" + time + "_" + countImage  + "." + FilenameUtils.getExtension(nameOfFile);
                try {
                    // Tạo file tại Server.
                    File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);
                    // Luồng ghi dữ liệu vào file trên Server.
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                    stream.write(fileData.getBytes());
                    stream.close();
                    VoteImage voteImage = new VoteImage();
                    voteImage.setSource("/" + uploadRootDir.getName() + File.separator + name);
                    System.out.println(uploadRootDir + File.separator + name);
                    voteImages.add(voteImage);
                    countImage ++;
                } catch (Exception e) {
                    System.out.println("Error Write file: " + name);
                }
            }
        }
        voteService.createProposal(vote, voteImages);

        Iterable<Category> categories = categoryService.findAll();
        ModelAndView modelAndView = new ModelAndView("/proposal/requestCreate");
        modelAndView.addObject("voteFileUpload", new VoteFileUpload());
        modelAndView.addObject("categories", categoryService.findAll());
        modelAndView.addObject("proposalSuccess", "Đề Nghị Của Bạn Đã Được Gửi, Quản Lý Sẽ Xem Xét Nó");
        return modelAndView;
    }
}
