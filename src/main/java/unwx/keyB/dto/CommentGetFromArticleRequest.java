package unwx.keyB.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentGetFromArticleRequest extends PieceOfInformationRequest {
    public CommentGetFromArticleRequest(@JsonProperty("article_id") Integer start, Short size) {
        super(start, size);
    }
}
