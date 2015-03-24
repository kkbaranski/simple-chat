package chat.server.client;

import chat.server.model.Message;

import java.util.stream.Stream;

/**
 * @author Krzysztof Baranski
 */
public interface ClientManager
{
	public void addClient( Client client );

	public void removeClient( Client client );

	public int getUnoccupiedId();

	public Client getClient( int id );

	public Stream<Client> getClients();

	public default Stream<Client> getClientsExcept( int id ) {
		return getClients().filter( client -> client.getId() != id );
	}

	public default Stream<Client> getClientsExcept( Client client ) {
		return getClientsExcept( client.getId() );
	}

	public default void sendTo( int id, Message message ) {
		getClient( id ).sendMessage( message );
	}

	public default void sendTo( Client client, Message message ) {
		client.sendMessage( message );
	}

	public default void sendToAll( Message message ) {
		getClients().forEach( client -> client.sendMessage( message ) );
	}

	public default void sendToAllExcept( int id, Message message ) {
		getClients().filter( client -> client.getId() != id ).forEach( client -> client.sendMessage( message ) );
	}

	public default void sendToAllExcept( Client client, Message message ) {
		getClients().filter( _client -> !_client.equals( client ) ).forEach( _client -> _client.sendMessage( message ) );
	}
}
