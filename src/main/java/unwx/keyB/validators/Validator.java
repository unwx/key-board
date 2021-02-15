package unwx.keyB.validators;

public abstract class Validator {

    protected boolean areAttributesAreNotNull(Object... objects) {
        for (Object o : objects) {
            if (o == null)
                return false;
        }
        return true;
    }
}
