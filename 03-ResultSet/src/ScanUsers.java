import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class ScanUsers {
	
	private static BufferedReader keyboard = new BufferedReader( new InputStreamReader( System.in ) );

	public static void main(String[] args) throws Exception {

		Properties props = new Properties();
		try ( FileInputStream fis = new FileInputStream( "conf.properties" ) ) {
			props.load( fis );
		}
		
		Class.forName( props.getProperty( "jdbc.driver.class" ) );
		
		String url = props.getProperty( "jdbc.url" );
		String dbLogin = props.getProperty( "jdbc.login" );
		String dbPassword = props.getProperty( "jdbc.password" );
		
		try ( Connection connection = DriverManager.getConnection( url, dbLogin, dbPassword ) ) {
			String strSql = "SELECT * FROM T_Users";
			try ( Statement statement = connection.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE ) ) {
				try ( ResultSet resultSet = statement.executeQuery( strSql ) ) {
					resultSet.next();
					
					boolean exit = false;
					while( ! exit ) {

						System.out.println( "Current user: " + resultSet.getInt( 1 ) + ": "
								 + resultSet.getString( 2 ) + " "
								 + resultSet.getString( "password" ) + " - "
								 + resultSet.getInt( "connectionNumber" ) + " connection(s)" );

						System.out.print( "Enter a command: " );
						String command = keyboard.readLine();
						switch( command ) {
							case "first":
								resultSet.first();
								break;
							case "previous":
								resultSet.previous();
								break;
							case "next":
								resultSet.next();
								break;
							case "last":
								resultSet.last();
								break;
							case "update":
								resultSet.updateString( 2, resultSet.getString( 2 ).toUpperCase() );
								resultSet.updateString( 3, resultSet.getString( 3 ).toUpperCase() );
								resultSet.updateInt( 4, resultSet.getInt( "connectionNumber" ) + 1 );
								resultSet.updateRow();
								break;
							case "exit":
								exit = true;
								break;
							default:
								System.out.println( "Bad command " + command );
						}
					}					
					
				}
			}
		}
		
		System.out.println( "Bye bye" );
	}

}
