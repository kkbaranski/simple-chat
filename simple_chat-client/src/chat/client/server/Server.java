package chat.client.server;

import chat.client.model.Message;

import java.util.function.Consumer;

/**
 * @author Krzysztof Baranski
 */
public interface Server extends Runnable
{
	public void start();

	public void setHandler( Consumer<Message> handler );

	public void setByeMessage( Message byeMessage );

	public void setHelloMessage( Message helloMessage );

	public void sendMessage( Message message );
}
