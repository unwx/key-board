package unwx.keyB.dto;

public class PieceOfInformationRequest {

    private final int startIndex;
    private final short size;

    public PieceOfInformationRequest(int startIndex, short size) {
        this.startIndex = startIndex;
        this.size = size;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public short getSize() {
        return size;
    }
}
