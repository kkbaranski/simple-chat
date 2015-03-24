package chat.client.model;

/**
 * @author Krzysztof Baranski
 */
public class User
{
	public static final User ALL = new User( 0, "ALL" );

	public User( int id ) {
		this( id, "" );
	}

	public User( int id, String name ) {
		this.name = name;
		this.id = id;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals( Object o ) {
		if( this == o ) return true;
		if( o == null || getClass() != o.getClass() ) return false;
		User user = (User) o;
		return id == user.id;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public String toString() {
		return "User{" +
		       "id=" + id +
		       ", name='" + name + '\'' +
		       '}';
	}

	private final int id;
	private String name;
}
