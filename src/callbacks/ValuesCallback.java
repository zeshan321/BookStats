package callbacks;

import java.sql.ResultSet;
import java.util.UUID;

public abstract class ValuesCallback {
    public abstract void onRequestComplete(UUID uuid, ResultSet rs);
}
