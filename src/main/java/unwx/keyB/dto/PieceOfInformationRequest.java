package unwx.keyB.dto;

public class PieceOfInformationRequest {

    private final Integer start;
    private final Short size;

    public PieceOfInformationRequest(Integer start, Short size) {
        this.start = start;
        this.size = size;
    }

    public Integer getSelectIndex() {
        return start;
    }

    public Short getSize() {
        return size;
    }
}
