package servicedesk.database;

import java.sql.Connection;

public class UserDatabase {

    private Connection conn;

    public UserDatabase(Connection conn) {
        this.conn = conn;
    }
}
