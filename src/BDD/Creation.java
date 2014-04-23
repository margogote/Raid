package BDD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Creation {

	/*public static void main(String[] args) {

		String url = "jdbc:mysql://localhost/raidzultat";
		String user = "root";
		String passwd = "";

		Connection conn = null;
		String requeteSQL = "SELECT nomCompetition, idCompetition FROM competition";
		String nomC, idC;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			System.out.println("Driver O.K.");

			conn = DriverManager.getConnection(url, user, passwd);
			System.out.println("Connexion effective !");

			Statement stm = conn.createStatement();
			ResultSet res = stm.executeQuery(requeteSQL);

			while (res.next()) {
				System.out.println("Nom : " + res.getString(1));
				System.out.println("Id : " + res.getString(2));
			}

			conn.close();
			res.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
*/
}
