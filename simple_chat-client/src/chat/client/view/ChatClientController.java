package chat.client.view;

import chat.client.model.Message;
import chat.client.model.MessageType;
import chat.client.model.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Logger;

/**
 * @author Krzysztof Baranski
 */
public class ChatClientController implements Controller
{
	@Override
	public synchronized void handleMessage( Message message ) {
		logger.info( "Handling message: " + message );
		logger.info( "Handler invoking in thread:" + Thread.currentThread() );
		User user;
		switch( message.getType() ) {

			case PLAIN:
				logger.info( "Message from: " + message );
				user = findUser( message.getUserId() );
				if( user != null ) {
					logger.info( "User exists: " + user );
					appendText( fromUserToMeMessageFormatter( user, message ) );
				}
				break;
			case UPDATE_USER:
				logger.info( "Updating user: " + message );
				user = findUser( message.getUserId() );
				if( user != null ) {
					logger.info( "User: " + user + " already exists." );
					Platform.runLater( () -> users.remove( user ) );
					user.setName( message.getText() );
					Platform.runLater( () -> users.add( user ) );
				} else {
					logger.info( "Adding new user" );
					Platform.runLater( () -> users.add( new User( message.getUserId(), message.getText() ) ) );
				}
				break;
			case REMOVE_USER:
				user = findUser( message.getUserId() );
				if( user != null ) {
					Platform.runLater( () -> users.remove( user ) );
				}
				break;
		}
	}

	@Override
	public void setSender( Consumer<Message> sender ) {
		this.sender = sender;
	}

	private User findUser( int id ) {
		logger.info( "Finding user " + id + "..." );
		for( User user : users ) {
			if( user.getId() == id ) {
				logger.info( "Success! " + user );
				return user;
			}
		}
		logger.info( "Fail!" );
		return null;
	}

	private void appendText( String text ) {
		logger.info( "Append text: " + text );
		Platform.runLater( () -> chatBox.appendText( text ) );
	}

	private String fromMeToUserMessageFormatter( User user, Message message ) {
		// -> USERNAME: MESSAGE
		return "→ " + user.getName() + ": " + message.getText() + "\n";
	}

	private String fromUserToMeMessageFormatter( User user, Message message ) {
		// USERNAME: MESSAGE
		return user.getName() + ": " + message.getText() + "\n";
	}

	private void sendMessageAction() {
		logger.info( "Sending message..." );
		writeBox.setEditable( false );
		String text = writeBox.getText();
		writeBox.clear();
		writeBox.setEditable( true );

		if( text == null || text.trim().isEmpty() ) {
			logger.info( "Empty message!" );
			return;
		}

		logger.info( "text: '" + text + "'" );
		ObservableList<User> addressees = usersList.getSelectionModel().getSelectedItems();
		for( User addressee : addressees ) {
			Message message;
			if( addressee == User.ALL ) {
				message = new Message( MessageType.PLAINALL, addressee.getId(), text );
			} else {
				message = new Message( MessageType.PLAIN, addressee.getId(), text );
			}
			sender.accept( message );
			appendText( fromMeToUserMessageFormatter( addressee, message ) );
		}
	}

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextArea chatBox;

	@FXML
	private ListView<User> usersList;

	@FXML
	private TextField writeBox;

	@FXML
	public void onEnter( ActionEvent actionEvent ) {
		logger.info( "Enter pressed" );
		sendMessageAction();
	}

	@FXML
	void sendButtonAction( ActionEvent event ) {
		logger.info( "Send button pressed" );
		sendMessageAction();
	}

	@FXML
	void initialize() {
		assert chatBox != null : "fx:id=\"chatBox\" was not injected: check your FXML file 'main_window.fxml'.";
		assert usersList != null : "fx:id=\"usersList\" was not injected: check your FXML file 'main_window.fxml'.";
		assert writeBox != null : "fx:id=\"writeBox\" was not injected: check your FXML file 'main_window.fxml'.";

		chatBox.setEditable( false );

		users.add( User.ALL );
		Platform.runLater( () -> usersList.setItems( users ) );
		Platform.runLater( () -> usersList.setCellFactory( ( list ) -> new ListCell<User>()
		{
			@Override
			protected void updateItem( User item, boolean empty ) {
				super.updateItem( item, empty );

				if( item == null || empty ) {
					setText( null );
				} else {
					setText( item.getName() );
				}
			}
		} ) );

		writeBox.setOnKeyPressed( new EventHandler<KeyEvent>()
		{
			public void handle( KeyEvent ke ) {
				switch( ke.getCode() ) {
					case UP:
						usersList.getSelectionModel().selectPrevious();
						break;
					case DOWN:
						usersList.getSelectionModel().selectNext();
						break;
				}
			}
		} );
	}

	private Consumer<Message> sender = null;
	private ObservableList<User> users = FXCollections.observableArrayList();
	private static final Logger logger = Logger.getLogger( Controller.class.getName() );
}
