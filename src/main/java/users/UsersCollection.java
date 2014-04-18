package users;

import java.util.LinkedList;
import java.util.List;

import users.UserList;

public class UsersCollection {
	private List<UserList> users;
	
	public UsersCollection(){
		this.users = new LinkedList<UserList>();
	}
	public List<UserList> getUsers(){
		return this.users;
	}
	public void addUser(UserList user){
		this.users.add(user);
	}
	public void deleteUser(UserList user){
		this.users.remove(user);
	}
}
