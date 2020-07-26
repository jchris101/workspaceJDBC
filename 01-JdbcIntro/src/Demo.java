import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class Demo {

	public static void main(String[] args) throws Exception {

		Properties props = new Properties();
		try ( FileInputStream fis = new FileInputStream( "conf.properties" ) ) {
			props.load( fis );
		}
		/*
			System.out.println("jdbc.driver.class = " + props.getProperty("jdbc.driver.class"));
			System.out.println("jdbc.url = " + props.getProperty( "jdbc.url" ));
			System.out.println("jdbc.login = " + props.getProperty( "jdbc.login"  ));
			System.out.println("jdbc.password = " + props.getProperty( "jdbc.password" ));
			System.out.println("CORRECTION = " + props.getProperty( "CORRECTION" )); // Correction format géographique
		*/
		
		//Class.forName( props.getProperty( "jdbc.driver.class") );
		Class.forName( "jdbc:mysql://localhost:3306/WebStore" + "?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC");
		//Class.forName( "jdbc:mysql://localhost:3306/WebStore");
		
		/*
			StringBuilder sb = new StringBuilder();
			sb.append(props.getProperty( "jdbc.url" ))
			  .append(props.getProperty( "CORRECTION" )); 
			Class.forName(sb.toString());  
		*/
		
		//Class.forName( props.getProperty( "jdbc.driver.class")+ props.getProperty("CORRECTION") );
		
		String url = props.getProperty( "jdbc.url" );
		String login = props.getProperty( "jdbc.login" );
		String password = props.getProperty( "jdbc.password" );
		
		try ( Connection connection = DriverManager.getConnection( url, login, password ) ) {
		
//			String strSql = "INSERT INTO T_Users (idUser, login, password, connectionNumber) "
//					      + "VALUES ( 6, 'Bourne', 'Jason', 8)";
//			try ( Statement statement = connection.createStatement() ) {
//				statement.executeUpdate( strSql );
//			}

			String strSql = "SELECT * FROM T_Users";
			
			try ( Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery( strSql ) ) {
				
				while( resultSet.next() ) {
					int rsIdUser = resultSet.getInt( 1 );
					String rsLogin = resultSet.getString( 2 );
					String rsPassword = resultSet.getString( "password" );
					int rsConnectionNumber = resultSet.getInt( "connectionNumber" );
					
					System.out.printf( "%d: %s %s - %d\n", rsIdUser, rsLogin, rsPassword, rsConnectionNumber );
				}				
			}			
		}
		
	}

}
