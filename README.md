 Hello,
 This file is the only dependency you need if you don't clone the repo. This project has only been ran on IntelliJ so that's the reference I'm gonna be using. 
[23-lib.zip](https://github.com/it-sd-capstone/capstone-project-varr/files/14962397/23-lib.zip)

Create a project first, and then select 
  1. File > Project Structure
  2. Select the + and select JARs or Directories
  3. Choose all the JAR files from the download ./lib
  4. Change the scope of JAR files beginning wit apiguardian, junit, and opentest(should be nine files) to be test
  5. Rest should be compile
  6. Select OK and save selection

To ensure that the validation works, 
  1. First create a a package for main called edu.cvtc.varr under src
  2. In the main.java, this is the content of it

    package edu.cvtc.varr;
    
    import java.sql.*;
    
    public class Main {
        public static final String DATABASE_NAME = "projectVarr ";
        public static final String DATABASE_PATH = DATABASE_NAME + ".db";
        private static final int TIMEOUT_STATEMENT_S = 5;
    
    
        public static void main(String[] args) {
            System.out.println("Hello world!");
        }
    
        public static Connection createConnection() {
            Connection result = null;
            try {
                result = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_PATH);
                Statement command = result.createStatement();
                command.setQueryTimeout(TIMEOUT_STATEMENT_S);
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
            return result;
        }
    
    
        public static ResultSet queryRaw(Connection db, String sql) {
            ResultSet result = null;
            try {
                Statement statement = db.createStatement();
                result = statement.executeQuery(sql);
            }
            catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
            return result;
        }
    }

  3. Create another file called test
  4. Create the edu.cvtc.varr package and create MainTest.java file
  5. Right-click on test folder and select
  6. Mark Directory as > Test Sources Root
  7. This will be the testing code
     
    package edu.cvtc.varr;
    import org.junit.jupiter.api.Test;
    import static org.junit.jupiter.api.Assertions.*;
    import java.sql.*;
    class MainTest {
        @Test
        void createConnection() {
            assertDoesNotThrow(
                    () -> {
                        Connection db = Main.createConnection();
                        assertNotNull(db);
                        assertFalse(db.isClosed());
                        db.close();
                        assertTrue(db.isClosed());
                    }
            );
        }
    
        @Test
        void queryRaw() {
            assertDoesNotThrow(
                    () -> {
                        try (Connection db = Main.createConnection()) {
                            ResultSet rows = Main.queryRaw(db, "SELECT 5 AS result");
                            assertNotNull(rows);
                            assertTrue(rows.next());
                            int result = rows.getInt("result");
                            assertEquals(5, result);
                            rows.close();
                            db.close();
                        }
                    }
            );
        }
    }
  6. Run MainTest.java and ensure that the tests passes for validation. 
     
  
