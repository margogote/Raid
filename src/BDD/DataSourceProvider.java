package BDD;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

/**
 * Permet de renseigner les informations nécessaire à la connexion à la base
 * de données
 * 
 * @author philippesmessaert
 * 
 */
public class DataSourceProvider {
	private static MysqlDataSource dataSource;

	/**
	 * Permet de créer une connexion selon les informations spécifiés A
	 * L'INTERIEUR DE LA CLASSE
	 * 
	 * @return dataSource , l'object donnant les informations pour la connexion
	 */
	public static DataSource getDataSource() {
		if (dataSource == null) {
			dataSource = new MysqlDataSource();
			dataSource.setServerName("localhost");
			dataSource.setPort(3306);
			dataSource.setDatabaseName("raidzultat");
			dataSource.setUser("root");
			dataSource.setPassword("root");
		}
		System.out.println("Passage par DataSourceProvider");
		return dataSource;
	}

}
