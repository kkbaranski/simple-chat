package chat.server.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * @author Krzysztof Baranski
 */
public class MyClientManager implements ClientManager
{
	@Override
	public void addClient( Client client ) {
		logger.info( "Adding client: " + client );
		clients.put( client.getId(), client );
		logger.info( "Added." );
	}

	@Override
	public void removeClient( Client client ) {
		logger.info( "Removing client: " + client );
		clients.remove( client.getId() );
		logger.info( "Removed." );
	}

	@Override
	public int getUnoccupiedId() {
		int id;
		while( clients.containsKey( ( id = new Random().nextInt( 999 ) + 1 ) ) ) ;
		return id;
	}

	@Override
	public Client getClient( int id ) {
		return clients.get( id );
	}

	@Override
	public Stream<Client> getClients() {
		return clients.values().parallelStream();
	}

	private final Map<Integer, Client> clients = new HashMap<>();
	private final static Logger logger = Logger.getLogger( MyClientManager.class.getName() );
}
