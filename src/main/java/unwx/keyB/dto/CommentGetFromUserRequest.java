package unwx.keyB.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentGetFromUserRequest extends PieceOfInformationRequest {
    public CommentGetFromUserRequest(@JsonProperty("user_id") Integer start, Short size) {
        super(start, size);
    }
}
