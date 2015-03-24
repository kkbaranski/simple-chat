package chat.client.connector;

import chat.client.model.Message;
import chat.client.server.Server;
import chat.client.view.Controller;
import javafx.application.Platform;

/**
 * @author Krzysztof Baranski
 */
public class Connector
{
	public synchronized void messageHandler( Message message ) {
		Platform.runLater( () -> controller.handleMessage( message ) );
	}

	public synchronized void messageSender( Message message ) {
		server.sendMessage( message );
	}

	public synchronized void startServer() {
		this.server.start();
	}

	public static ConnectorBuilder builder() {
		return new ConnectorBuilder();
	}

	public Server getServer() {
		return server;
	}

	public Controller getController() {
		return controller;
	}

	public static class ConnectorBuilder
	{
		public void setServer( Server server ) {
			this.server = server;
		}

		public void setController( Controller controller ) {
			this.controller = controller;
		}

		public Connector connect() throws InstantiationException {
			if( server == null || controller == null ) {
				throw new InstantiationException( "Server or connector not set" );
			}
			return new Connector( server, controller );
		}

		private Server server = null;
		private Controller controller = null;
	}

	private Connector( Server server, Controller controller ) {
		this.server = server;
		this.controller = controller;
		this.server.setHandler( controller::handleMessage );
		this.controller.setSender( server::sendMessage );
	}

	private final Server server;
	private final Controller controller;
}
