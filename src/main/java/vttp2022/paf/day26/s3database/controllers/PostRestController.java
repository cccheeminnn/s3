package vttp2022.paf.day26.s3database.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vttp2022.paf.day26.s3database.models.Post;
import vttp2022.paf.day26.s3database.repositories.PostRepository;

@RestController
@RequestMapping(path="/post")
public class PostRestController {
    
    @Autowired
    private PostRepository postRepo;

    @GetMapping(path="/{postId}/image")
    public ResponseEntity<byte[]> getPostImage(@PathVariable Integer postId) {
        
        Optional<Post> opt = postRepo.getPost(postId);
        if (opt.isEmpty())
            return ResponseEntity.notFound().build();
        
        final Post p = opt.get();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", p.getImageType());
        headers.add("Cache-Control", "max-age=604800");

        return ResponseEntity.ok()
            .headers(headers)
            .body(p.getImage());
    }

}
