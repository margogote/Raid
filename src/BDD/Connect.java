package BDD;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connect {

	public Connection Connect() {
		String url = "jdbc:mysql://localhost/raidzultat";
		String user = "root";
		String passwd = "";

		Connection conn = null;
		//String requeteSQL = "SELECT nomCompetition, idCompetition FROM competition";
		//String nomC, idC;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver O.K.");

			conn = DriverManager.getConnection(url, user, passwd);
			System.out.println("Connexion effective !");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	
}
