package vttp2022.paf.day26.s3database.repositories;

import java.sql.ResultSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vttp2022.paf.day26.s3database.models.Post;

@Repository
public class PostRepository {
    
    private static final String SQL_INSERT_POST = 
        "insert into post (photo, comment, poster, mediatype) values (?, ?, ?, ?)";
    
    private static final String SQL_GET_POST_BY_POST_ID = 
        "select post_id, photo, comment, poster, mediatype from post where post_id = ?";

    @Autowired
    private JdbcTemplate template;

    public Integer insertPost(Post post) 
    {
        Integer updCount = 
            template.update(SQL_INSERT_POST, 
                post.getImage(), 
                post.getComments(), 
                post.getPoster(), 
                post.getImageType()
            );
        return updCount;
    }

    public Optional<Post> getPost(Integer postId) 
    {
        //Day26 slide 20
        return template.query(
            SQL_GET_POST_BY_POST_ID,
            (ResultSet rs) -> {
                if (!rs.next())
                    return Optional.empty();
                final Post post = Post.populate(rs);
                return Optional.of(post);
            },
            postId);
    }
}
