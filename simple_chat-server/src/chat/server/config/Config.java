package chat.server.config;

import java.util.Properties;

/**
 * @author krzysztof
 */
public class Config
{
	private static final Properties prop = new Properties()
	{
		@Override
		public String getProperty( String key ) {
			if( key.equals( "server_port" ) ) {
				return String.valueOf( 9911 );
			} else if( key.equals( "max_number_of_clients" ) ) {
				return String.valueOf( 100 );
			}
			return null;
		}
	};

	/*static {
		prop = new Properties();
		InputStream input = null;
		try {
			input = new FileInputStream( "config.properties" );
			prop.load( input );
		} catch( IOException e ) {
			e.printStackTrace();
		} finally {
			if( input != null ) {
				try {
					input.close();
				} catch( IOException e ) {
					e.printStackTrace();
				}
			}
		}
	}*/

	public static int getInt( String property ) {
		return Integer.parseInt( prop.getProperty( property ) );
	}

	public static String getString( String property ) {
		return prop.getProperty( property );
	}

	public static int serverPort() {return getInt( "server_port" );}

	public static int maxNumberOfClients() {return getInt( "max_number_of_clients" );}
}
