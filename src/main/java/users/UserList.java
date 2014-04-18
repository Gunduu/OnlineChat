package users;
public class UserList{

	private String name = null;

	public UserList() {
	}

	public UserList(String user){
		this.setName(user);
	}

	public void setName(String user)
	{
		this.name = user;
	}

	public String getName(){
		return this.name;
	}

}
