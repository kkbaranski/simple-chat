package chat.server.client;

import chat.server.model.Message;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Krzysztof Baranski
 */
public interface Client extends Runnable
{
	public int getId();

	public String getName();

	public void setName( String name );

	public void sendMessage( Message message );

	public default void setHelloMessage( Message message ) {
		setHelloMessage( client -> message );
	}

	public void setHelloMessage( Function<Client, Message> messageFunction );

	public default void setByeMessage( Message message ) {
		setByeMessage( client -> message );
	}

	public void setByeMessage( Function<Client, Message> messageFunction );

	public void setMessageHandler( Consumer<Message> handler );
}
