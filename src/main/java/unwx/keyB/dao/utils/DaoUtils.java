package unwx.keyB.dao.utils;

import org.hibernate.Session;

import java.math.BigInteger;
import java.util.List;

public class DaoUtils {

    public Long convertBigintToLong(BigInteger i) {
        return i.longValue();
    }

    public void pushQueries(Session session, List<String> sqlRequests) {
        for (String s : sqlRequests) {
            session.createSQLQuery(s).executeUpdate();
        }
    }
}
