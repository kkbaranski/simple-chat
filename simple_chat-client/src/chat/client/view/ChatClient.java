package chat.client.view;

import chat.client.connector.Connector;
import chat.client.model.Message;
import chat.client.model.MessageType;
import chat.client.model.MyProtocol;
import chat.client.model.Protocol;
import chat.client.server.MyServer;
import chat.client.server.Server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.List;
import java.util.logging.Logger;

public class ChatClient extends Application
{
	@Override
	public void init() throws Exception {
		super.init();
		logger.info( "init()" );
		List<String> rawArguments = getParameters().getRaw();

		if( rawArguments.size() != 2 && rawArguments.size() != 3 ) {
			System.out.println( "Usage: java ChatClient server_address server_port [username]" );
			System.exit( 1 );
		}
		String serverAddress = rawArguments.get( 0 );
		int serverPort = Integer.parseInt( rawArguments.get( 1 ) );
		Protocol protocol = new MyProtocol();
		String username = "";
		if( rawArguments.size() == 3 ) {
			username = rawArguments.get( 2 );
		}

		Server server = new MyServer( serverAddress, serverPort, protocol );
		server.setHelloMessage( new Message( MessageType.TEXT, 0, username ) );

		serverUIConnectorBuilder.setServer( server );
	}

	@Override
	public void start( Stage primaryStage ) throws Exception {
		FXMLLoader loader = new FXMLLoader( getClass().getResource( "main_window.fxml" ) );
		Pane pane = loader.load();
		primaryStage.setTitle( "Chat Client" );
		primaryStage.setScene( new Scene( pane, 300, 300 ) );
		primaryStage.show();

		Controller controller = loader.<Controller>getController();
		this.serverUIConnectorBuilder.setController( controller );

		Connector serverUIConnector = this.serverUIConnectorBuilder.connect();
		serverUIConnector.startServer();
	}

	public static void main( String[] args ) {
		launch( args );
	}

	private Connector.ConnectorBuilder serverUIConnectorBuilder = Connector.builder();
	private static final Logger logger = Logger.getLogger( ChatClient.class.getName() );
}