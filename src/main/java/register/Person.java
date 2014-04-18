package register;
import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import users.UserCount;

public class Person implements Serializable {

	private String name;

	private String email;

    private String password;
    
    public String getUserName()
    {
    	return name;
    }
    
    public void setFirstName(String username)
    {
    	this.name = username;
    }
    
    public String getEmail()
    {
    	return email;
    }
    
    public void setEmail(String email)
    {
    	this.email = email;
    }
    
    public String getPassword()
    {
    	return password;
    }
    
    public void setPassword(String password)
    {
    	this.password = password;
    }
}
