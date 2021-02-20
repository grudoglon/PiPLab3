package database;

import java.io.PrintWriter;
import java.sql.*;

public class BasesShotCreater {



    private static String user = "";
    private static String password = "";
    private static String url = "jdbc:postgresql://pg:5432/studs";


    private static DataBaseManagerShots dataBaseManagerShots = null;

    private static String tableSessionCheck = "SELECT FROM information_schema.tables " +
            "WHERE  table_schema = 'schema_name'" +
            "AND table_name   = 'sessions3'\n";

    private static String tableShotsCheck = "SELECT FROM information_schema.tables " +
            "WHERE  table_schema = 'schema_name'" +
            "AND table_name   = 'shots3'\n";

    private static String tableSession =
            "create table sessions3(\n"+
                    "session_id bigserial not null constraint sessions3_pkey primary key,\n"+
                    "session_str_id varchar(255) not null);";



    private static String tableShots =
            "CREATE TABLE shots3 (\n" +
                    "shot_id bigserial not null constraint shots3_pkey primary key,\n" +
                    "x double precision NOT NULL,\n" +
                    "y double precision NOT NULL,\n" +
                    "r double precision NOT NULL,\n" +
                    "rg int NOT NULL,\n" +
                    "start_time varchar( 20 ) NOT NULL,\n" +
                    "script_time bigint NOT NULL,\n" +
                    "session_id bigint NOT NULL,\n" +
                    "FOREIGN KEY(session_id) REFERENCES sessions3(session_id)\n" +
                    ");";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Unable to load class.");
            e.printStackTrace();
        }
    }

    public static DataBaseManagerShots getDataBase() {
        if (dataBaseManagerShots == null) {

            createDataBase();
            System.out.println("Database created");
        }
        return dataBaseManagerShots;
    }

    private static void createDataBase() {
        Connection connection;
        DriverManager.setLogWriter(new PrintWriter(System.out));
        try {
            connection = DriverManager.getConnection(url, user, password);
            dataBaseManagerShots = new DataBaseManagerShots(connection);

            try(PreparedStatement st = connection.prepareStatement(tableSessionCheck)){
                ResultSet rs = st.executeQuery();
                if(rs.next()){
                    System.out.println("Session table already created");
                }else{
                    Statement statement = connection.createStatement();
                    statement.execute(tableSession);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }

            try(PreparedStatement st = connection.prepareStatement(tableShotsCheck)){
                ResultSet rs = st.executeQuery();
                if(rs.next()){
                    System.out.println("Shots table already created");
                }else{
                    Statement statement = connection.createStatement();
                    statement.execute(tableShots);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
