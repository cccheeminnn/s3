package vttp2022.paf.day26.s3database.controllers;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import vttp2022.paf.day26.s3database.models.Post;
import vttp2022.paf.day26.s3database.repositories.PostRepository;

@Controller
@RequestMapping(path="")
public class PostController {
    
    @Autowired
    private PostRepository postRepo;

    @PostMapping(path="/post",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String postPost(
        @RequestParam("image-file") MultipartFile imageFile, 
        @RequestPart String comment, @RequestPart String poster, Model model) 
    {
        //Get file metadata
        String fileName = imageFile.getOriginalFilename();
        long fileSize = imageFile.getSize();
        String contentType = imageFile.getContentType();
        byte[] buff = new byte[0];

        try {
            buff = imageFile.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        Post p = new Post();
        p.setComments(comment);
        p.setPoster(poster);
        p.setImageType(contentType);
        p.setImage(buff);

        Integer updCount = postRepo.insertPost(p);

        model.addAttribute("updCount", updCount);

        return "results";
    }

    @GetMapping(path="/post/{id}")
    public String getPostById(@PathVariable Integer id, Model model) {
        
        Optional<Post> opt = postRepo.getPost(id);
        Post p = opt.get();

        model.addAttribute("opt", p);
        return "image";
    }
}
