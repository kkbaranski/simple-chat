package chat.server.server;

import chat.server.client.Client;
import chat.server.model.Message;

import java.util.function.Consumer;

/**
 * @author Krzysztof Baranski
 */
public interface ClientHandler
{
	public Consumer<Message> getHandler( Client client );
}
