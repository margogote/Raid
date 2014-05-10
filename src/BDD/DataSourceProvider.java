package BDD;

import javax.sql.DataSource;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

public class DataSourceProvider {
		private static MysqlDataSource dataSource;
		
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