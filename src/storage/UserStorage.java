package storage;

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

import user.*;

public class UserStorage extends BaseStorage{

public UserStorage(String filePath) {
    	super(filePath);
    }

    public ArrayList<User> load() {
    	/* function of this method:
    	 * 1. read user data from filePath
    	 * 2. convert it into an array of User objects
    	 * 3. return these objects and recording the count
    	 */
    	
    	ArrayList<User> users = new ArrayList<>();
    	
        try {
        	// read user data from filePath
        	Scanner sc = new Scanner(new File(filePath));

        	while(sc.hasNextLine()){
        	String line = sc.nextLine();
           	// use -1 to trail the empty String
        	String[] p = line.split(",", -1);

             // String --> attribute
        	String role = p[0];
        	String userId = p[1];
        	String password = p[2];
        	String name = p[3];
        	String faculty = p[4];
        	String contact = p[5];
        	String programme = p[6];
        	
        	User user = null;

        	// create object, instantiate depends on the role
        	if(role.equals("Student")){
        		user = new Student(userId,password,name,faculty,contact,programme);
        	}
        	else if(role.equals("Staff")){
        		user = new Staff(userId,password,name,faculty,contact);
        	}
        	else if(role.equals("Admin")){
        		user = new Admin(userId,password,name,faculty,contact);
        	}
        	
        	if (user != null) {
        	    users.add(user);
        	}

        	// store into array
        	users.add(user);
            }

        	sc.close();
        } catch (Exception e) {
            System.out.println("Error loading users.");
        }

        return users;
    }

    public void save(ArrayList<User> users) {
        try {
        	PrintWriter writer = new PrintWriter(filePath);

            for (User u : users) {
                String line = "";

                if(u instanceof Student){
                	Student s = (Student)u;
                	line = "Student,"+ s.getUserId()+","+ s.getPassword() + "," + 
                	s.getName() + ","+ s.getFacultyOrDepartment()+","+
                	s.getContactNumber() + ","+ s.getProgramme();
                }
                else if(u instanceof Staff){
                	line = "Staff," + u.getUserId()+","+ u.getPassword() + "," +
                	u.getName() + "," + u.getFacultyOrDepartment() + "," +
                	u.getContactNumber() + ",NULL";
                }
                else if(u instanceof Admin){
                	line = "Admin," + u.getUserId() + "," + u.getPassword() + "," +
                	u.getName()+"," + u.getFacultyOrDepartment() + "," +
                	u.getContactNumber() + ",NULL";
                }
                
                writer.println(line);
            }

            writer.close();
        }
        catch (Exception e) {
        	System.out.println("Error saving users.");
        }
    }
}