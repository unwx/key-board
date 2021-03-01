package unwx.keyB.validators;

import unwx.keyB.dto.PieceOfInformationRequest;

public class PieceOfInformationSelectValidator {

    public boolean isValid(PieceOfInformationRequest request) {
        if (request == null)
            return false;
        if (request.getSelectIndex() == null || request.getSize() == null)
            return false;
        return request.getSelectIndex() >= 0 && request.getSize() >= 2;
    }
}
