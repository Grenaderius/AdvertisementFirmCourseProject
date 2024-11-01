package SQL.InteractionsInterface;

import java.io.IOException;
import java.sql.SQLException;

public interface SQLInteractions {
    void createView() throws SQLException, IOException;
}
