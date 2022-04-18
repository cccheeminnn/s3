package vttp2022.paf.day26.s3database.controllers;

import java.io.IOException;
import java.util.UUID;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.json.Json;
import jakarta.json.JsonObject;

@RestController
@RequestMapping(path="/post/s3")
public class PostS3Controller {
    
    @Autowired
    private AmazonS3 s3;

    @PostMapping
    public ResponseEntity<JsonObject> post(
        @RequestParam("image-file") MultipartFile imageFile,
        @RequestPart String comment, 
        @RequestPart String poster) 
        {
            JsonObject result; 
            String fileName = imageFile.getOriginalFilename();
            long fileSize = imageFile.getSize();    
            String contentType = imageFile.getContentType();
            byte[] buff = new byte[0];
    
            try {
                buff = imageFile.getBytes();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String uuid = UUID.randomUUID().toString().substring(0, 8);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType);
            metadata.setContentLength(fileSize);

            //Upload to bucket
            try {
                PutObjectRequest putReq = new PutObjectRequest(
                    "bigcontainer", 
                    "%s/images/%s".formatted(poster, uuid), 
                    imageFile.getInputStream(), 
                    metadata);
                putReq.setCannedAcl(CannedAccessControlList.PublicRead);
                s3.putObject(putReq);

                result = Json.createObjectBuilder()
                    .add("objId", uuid)
                    .build();
                
                return ResponseEntity.ok().body(result);

            } catch (IOException e) {
                e.printStackTrace();
                
                result = Json.createObjectBuilder()
                    .add("error", e.getMessage())
                    .build();
                
                return ResponseEntity.status(500).body(result);

            }
        }
}
