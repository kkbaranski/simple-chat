package chat.server.server;

import chat.server.client.Client;
import chat.server.client.ClientManager;
import chat.server.client.MyClient;
import chat.server.config.Config;
import chat.server.protocol.Protocol;

import javax.swing.event.ChangeListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Krzysztof Baranski
 */
public class ClientListener implements Runnable
{
	public ClientListener( int port, ClientManager clientManager, Protocol protocol, ClientHandler clientHandler ) {
		this.port = port;
		this.clientManager = clientManager;
		this.protocol = protocol;
		this.clientHandler = clientHandler;
	}


	@Override
	public void run() {
		logger.info( "Server started. " + server );
		while( thread == Thread.currentThread() ) {
			try {
				logger.info( "Waiting for a client ..." );
				Socket clientSocket = server.accept();
				int clientId = clientManager.getUnoccupiedId();
				Client client = new MyClient( clientId, clientSocket, clientManager, protocol );
				//client.setHelloMessage(  );
				//client.setByeMessage(  );
				client.setMessageHandler( clientHandler.getHandler( client ) );
				executorService.execute( client );
			} catch( IOException e ) {
				logger.log( Level.SEVERE, e.getMessage(), e );
				stop();
			}
		}
	}

	public void start() {
		try {
			logger.info( "Starting server on port " + port );
			server = new ServerSocket( port );
		} catch( IOException e ) {
			logger.log( Level.SEVERE, e.getMessage(), e );
		}
		if( thread == null ) {
			thread = new Thread( this );
			logger.info( thread.getClass() + " started. " + thread );
			thread.start();
		}
	}

	private void stop() {
		logger.info( "Stopping thread" );
		if( thread != null ) {
			Thread tmp = thread;
			thread = null;
			tmp.interrupt();
		}
	}

	private ServerSocket server = null;
	private Thread thread = null;
	private final int port;
	private final ClientManager clientManager;
	private final Protocol protocol;
	private final ClientHandler clientHandler;
	private final ExecutorService executorService = Executors.newFixedThreadPool( Config.maxNumberOfClients() );
	private final static Logger logger = Logger.getLogger( ChangeListener.class.getName() );
}
