package service;

import java.sql.*;

public class JdbcService {
    private Connection conn;
    private Statement stmt;

    public JdbcService(String connectionString, String user, String password) {
        try {
            this.conn = DriverManager.getConnection(connectionString, user, password);
            this.stmt = conn.createStatement();

        } catch (SQLException sql_e) {
            throw new RuntimeException(sql_e);
        }
    }

    public Connection getConn() {
        return conn;
    }

    public Statement getStmt() {
        return stmt;
    }

    private void test(String QUERY) throws SQLException {
        ResultSet rs = stmt.executeQuery(QUERY);
        while (rs.next()) {
            System.out.print("ID: " + rs.getInt("id"));
            System.out.print(", Text: " + rs.getString("question_text"));
            System.out.println();
        }
    }

    public void close() {
        try {
            if (stmt != null && !stmt.isClosed()) stmt.close();
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
