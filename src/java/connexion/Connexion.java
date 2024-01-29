/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connexion;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Family
 */
public class Connexion {
    public static Connection connection() throws Exception {
            String userName = "postgres" ;
            String password = "1815" ; 
            String url = "jdbc:postgresql://localhost:5432/voyage";
            Class.forName("org.postgresql.Driver");
            Connection connect =  DriverManager.getConnection(url, userName ,password);
            return connect ; 

    }
}
    

