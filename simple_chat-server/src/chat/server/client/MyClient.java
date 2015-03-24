package chat.server.client;

import chat.server.client.utils.Utils;
import chat.server.model.Message;
import chat.server.model.MessageType;
import chat.server.protocol.Protocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Krzysztof Baranski
 */
public class MyClient implements Client
{
	public MyClient( int id, Socket socket, ClientManager clientManager, Protocol protocol ) {
		this.id = id;
		this.socket = socket;
		this.clientManager = clientManager;
		this.protocol = protocol;
		this.name = Utils.defaultName( id );
	}

	@Override
	public void run() {
		logger.info( "chat.server.client.Client " + this.getName() + "(" + this.getId() + ") started." );
		try {
			openStreams();
			readName();
			clientManager.addClient( this );
			sendHelloMessage();
			clientManager.sendToAllExcept( this, new Message( MessageType.UPDATE_CLIENT, this.getId(), this.getName() ) );
			clientManager.getClientsExcept( this )
			             .forEach( client -> this.sendMessage( new Message( MessageType.UPDATE_CLIENT,
			                                                                client.getId(),
			                                                                client.getName() ) ) );
			String line;
			while( ( line = in.readLine() ) != null ) {
				logger.info( "reading: " + line );
				Message message = protocol.parseLine( line );
				if( handler != null ) {
					handler.accept( message );
				} else {
					logger.log( Level.WARNING, "handler not set!" );
				}
			}
			sendByeMessage();
		} catch( IOException e ) {
			logger.log( Level.SEVERE, e.getMessage(), e );
		} finally {
			try {
				closeStreams();
			} catch( IOException e ) {
				logger.log( Level.SEVERE, e.getMessage(), e );
			}
			clientManager.removeClient( this );
			clientManager.sendToAll( new Message( MessageType.REMOVE_CLIENT, this.getId(), this.getName() ) );
		}
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName( String name ) {
		this.name = name;
	}

	@Override
	public synchronized void sendMessage( Message message ) {
		if( this.out != null ) {
			this.out.println( protocol.toString( message ) );
		}
	}

	@Override
	public void setHelloMessage( Function<Client, Message> messageFunction ) {
		logger.info( "Setting hello message..." );
		this.helloMessage = messageFunction;
		logger.info( "OK" );
	}

	@Override
	public void setByeMessage( Function<Client, Message> messageFunction ) {
		logger.info( "Setting bye message..." );
		this.byeMessage = messageFunction;
		logger.info( "OK" );
	}

	@Override
	public void setMessageHandler( Consumer<Message> handler ) {
		logger.info( "Setting handler..." );
		this.handler = handler;
		logger.info( "OK" );
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) return true;
		if( o == null || getClass() != o.getClass() ) return false;
		MyClient myClient = (MyClient) o;
		return id == myClient.id;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		return "chat.server.client.MyClient{" +
		       "id=" + id +
		       ", socket=" + socket +
		       ", name='" + name + '\'' +
		       '}';
	}

	private void openStreams() throws IOException {
		logger.info( "Opening streams..." );
		this.in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
		this.out = new PrintWriter( socket.getOutputStream(), true );
		logger.info( "Opened." );
	}

	private void closeStreams() throws IOException {
		logger.info( "Closing streams..." );
		if( socket != null ) socket.close();
		if( in != null ) in.close();
		if( out != null ) out.close();
		logger.info( "Closed." );
	}

	private void readName() throws IOException {
		logger.info( "Reading name..." );
		String name = this.in.readLine();
		if( name != null && name.matches( protocol.getNamePattern().pattern() ) ) {
			logger.info( "name = '" + name + "'" );
			this.setName( name );
		}
		logger.info( "Done." );
	}

	private void sendHelloMessage() {
		if( helloMessage != null ) {
			logger.info( "Sending hello message..." );
			Message message = helloMessage.apply( this );
			sendMessage( message );
			logger.info( "Sent: '" + message + "'" );
		}
	}

	private void sendByeMessage() {
		if( byeMessage != null ) {
			logger.info( "Sending bye message..." );
			Message message = byeMessage.apply( this );
			sendMessage( message );
			logger.info( "Sent: '" + message + "'" );
		}
	}

	private final int id;
	private final Socket socket;
	private final ClientManager clientManager;
	private final Protocol protocol;
	private String name;
	private Function<Client, Message> helloMessage = null;
	private Function<Client, Message> byeMessage = null;
	private Consumer<Message> handler = null;

	private BufferedReader in = null;
	private PrintWriter out = null;
	private static final Logger logger = Logger.getLogger( Client.class.getName() );
}
