package chat.server.server;

import chat.server.client.Client;
import chat.server.client.ClientManager;
import chat.server.model.Message;
import chat.server.model.MessageType;

import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * @author Krzysztof Baranski
 */
public class MyClientHandler implements ClientHandler
{
	public MyClientHandler( ClientManager clientManager ) {
		this.clientManager = clientManager;
	}

	@Override
	public Consumer<Message> getHandler( Client client ) {
		return new HandleMessageConsumer( client, clientManager )::accept;
	}

	private static class HandleMessageConsumer implements Consumer<Message>
	{

		public HandleMessageConsumer( Client client, ClientManager clientManager ) {
			this.client = client;
			this.clientManager = clientManager;
		}

		@Override
		public void accept( Message message ) {
			logger.info( "HANDLE MESSAGE: " + message + " FOR CLIENT: " + client );
			Message forwarded;
			switch( message.getType() ) {
				case PLAIN:
					int destinationClientId = message.getClientId();
					String text = message.getText();
					forwarded = new Message( MessageType.PLAIN, client.getId(), text );
					clientManager.sendTo( destinationClientId, forwarded );
					break;
				case PLAINALL:
					forwarded = new Message( MessageType.PLAIN, client.getId(), message.getText() );
					clientManager.sendToAllExcept( client, forwarded );
					break;
				case UPDATE_NAME:
					String newName = message.getText();
					client.setName( newName );
					clientManager.sendToAllExcept( client,
					                               new Message( MessageType.UPDATE_CLIENT, client.getId(), client.getName() ) );
					break;
				case LIST_CLIENTS:
					clientManager.getClientsExcept( client )
					             .forEach( c -> clientManager.sendTo( client,
					                                                  new Message( MessageType.UPDATE_CLIENT,
					                                                               c.getId(),
					                                                               c.getName() ) ) );
					break;
				case HELP:
					clientManager.sendTo( client, new Message( MessageType.HELP, client.getId(), client.getName() ) );
					break;
				default:
					logger.warning( "Cannot handle!" );
					clientManager.sendTo( client, new Message( MessageType.ERROR, 0, "0_0 Unexpected error" ) );
			}
		}

		private final Client client;
		private final ClientManager clientManager;
		private static final Logger logger = Logger.getLogger( HandleMessageConsumer.class.getName() );
	}

	private final ClientManager clientManager;
	private static final Logger logger = Logger.getLogger( MyClientHandler.class.getName() );
}
