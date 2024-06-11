package servicedesk.database;

import java.sql.Connection;

public class TicketDatabase {

    private Connection conn;

    public TicketDatabase(Connection conn) {
        this.conn = conn;
    }
}
