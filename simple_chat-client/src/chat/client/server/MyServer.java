package chat.client.server;

import chat.client.model.Message;
import chat.client.model.Protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Krzysztof Baranski
 */
public class MyServer implements Server
{
	public MyServer( String serverAddress, int serverPort, Protocol protocol ) {
		this.serverAddress = serverAddress;
		this.serverPort = serverPort;
		this.protocol = protocol;
	}

	@Override
	public void run() {
		logger.info( "Starting server..." );
		try {
			connect();
			open();
		} catch( IOException e ) {
			logger.log( Level.SEVERE, e.getMessage(), e );
			System.exit( 1 );
		}
		logger.info( "Server started." );
		try {
			String line;

			sendMessage( helloMessage );
			while( in != null && ( line = in.readLine() ) != null ) {
				logger.info( "reading: " + line );
				Message message = protocol.parseLine( line );
				if( handler != null ) {
					handler.accept( message );
				}
			}
			sendMessage( byeMessage );
		} catch( IOException e ) {
			logger.log( Level.SEVERE, e.getMessage(), e );
		} finally {
			close();
		}
		logger.exiting( getClass().getName(), "Server stopped" );
	}

	@Override
	public void start() {
		if( server == null ) {
			server = new Thread( this );
			server.setDaemon( true );
			server.start();
		}
	}

	@Override
	public synchronized void setHandler( Consumer<Message> handler ) {
		this.handler = handler;
	}

	@Override
	public void setByeMessage( Message byeMessage ) {
		this.byeMessage = byeMessage;
	}

	@Override
	public void setHelloMessage( Message helloMessage ) {
		this.helloMessage = helloMessage;
	}

	@Override
	public synchronized void sendMessage( Message message ) {
		logger.info( "sendMessage()" );
		if( out != null && message != null ) {
			logger.info( "sending: " + message );
			out.println( protocol.createLine( message ) );
			out.flush();
		}
	}

	private void connect() throws IOException {
		logger.info( "Connecting to server..." );
		this.socket = new Socket( serverAddress, serverPort );
		logger.exiting( getClass().getName(), "Connected: " + socket );
	}

	private void open() throws IOException {
		logger.info( "Opening streams..." );
		this.in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
		this.out = new PrintWriter( socket.getOutputStream(), true );
		logger.exiting( getClass().getName(), "Opened." );
	}

	private void close() {
		if( this.in != null ) try {
			this.in.close();
			if( this.out != null ) this.out.close();
			if( this.socket != null ) this.socket.close();
		} catch( IOException e ) {
			e.printStackTrace();
		}
	}

	private final String serverAddress;
	private final int serverPort;
	private final Protocol protocol;

	private Thread server = null;
	private Socket socket = null;
	private BufferedReader in = null;
	private PrintWriter out = null;
	private Consumer<Message> handler = null;
	private Message helloMessage = null;
	private Message byeMessage = null;

	private static final Logger logger = Logger.getLogger( Server.class.getName() );
}
