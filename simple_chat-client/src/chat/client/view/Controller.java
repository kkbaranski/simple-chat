package chat.client.view;

import chat.client.model.Message;

import java.util.function.Consumer;


public interface Controller
{
	public void handleMessage( Message message );

	public void setSender( Consumer<Message> sender );
}
















	/*



	@FXML
	void sendButtonAction( ActionEvent event ) {
		tField.setDisable( true );
		String text = tField.getText();
		tField.clear();
		tField.setDisable( false );

		if(this==Connector.getController()) {
			System.out.println("CHOCIAZ TYLE");
		}

		ObservableList<User> addressees = usersList.getSelectionModel().getSelectedItems();
		for( User addressee : addressees ) {
			Message message = new Message( MessageType.PLAIN, addressee.getId(), text );
			if( Connector.getServer() != null ) {
				Connector.getServer().sendMessage( message );
				appendText( "Me -> " + addressee.getName() + ": " + text );

			} else {
				logger.log( Level.SEVERE, "Server is not set" );

				//System.exit( 1 );
			}
		}
		System.out.println( "BUTTON HANDLER!" );
	}



	@FXML
	void initialize() {
		assert tField != null : "fx:id=\"tField\" was not injected: check your FXML file 'main_window.fxml'.";
		assert textChat != null : "fx:id=\"textChat\" was not injected: check your FXML file 'main_window.fxml'.";
		assert usersList != null : "fx:id=\"usersList\" was not injected: check your FXML file 'main_window.fxml'.";

		textChat.setDisable( true );


		//usersList.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE);
		// Init ListView.
		usersList.setItems( users );
		usersList.setCellFactory( ( list ) -> new ListCell<User>()
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
		} );
	}







}*/
