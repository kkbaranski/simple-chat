package chat.server;


import chat.server.client.ClientManager;
import chat.server.client.MyClientManager;
import chat.server.config.Config;
import chat.server.protocol.MyProtocol;
import chat.server.protocol.Protocol;
import chat.server.server.ClientHandler;
import chat.server.server.ClientListener;
import chat.server.server.MyClientHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Krzysztof Baranski
 */
public class Main
{
	public static void main( String[] args ) {
		logger.info( "simple-chat started" );

		int port = Config.serverPort();
		if( args.length == 1 ) {
			try {
				port = Integer.parseUnsignedInt( args[0] );
			} catch( NumberFormatException e ) {
				logger.log( Level.SEVERE, "port cannot be parsed", e );
			}
		}

		logger.info( "server port = " + port );
		ClientManager clientManager = new MyClientManager();
		Protocol protocol = new MyProtocol();
		ClientHandler clientHandler = new MyClientHandler( clientManager );
		ClientListener clientListener = new ClientListener( port, clientManager, protocol, clientHandler );
		clientListener.start();
		logger.info( "main thread stopped" );
	}

	private static final Logger logger = Logger.getLogger( Main.class.getName() );
}
