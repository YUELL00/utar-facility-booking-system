package storage;

import java.io.*;

public class UserStorage extends BaseStorage{

    private String filePath;

    public UserStorage(String filePath) {
    	super(filePath);
    }

    public User[] load(int[] count) {
    	/* function of this method:
    	 * 1. read user data from filePath
    	 * 2. convert it into an array of User objects
    	 * 3. return these objects and recording the count
    	 */
    	
        User[] users = new User[100]; // max 100 users
        count[0] = 0;

        try {
        	// read user data from filePath
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;

            while ((line = br.readLine()) != null) {
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
                if (role.equals("Student")) {
                    user = new Student(userId, password, name, faculty, contact, programme);
                } else if (role.equals("Staff")) {
                    user = new Staff(userId, password, name, faculty, contact);
                } else if (role.equals("Admin")) {
                    user = new Admin(userId, password, name, faculty, contact);
                }

                // store into array
                users[count[0]] = user;
                count[0]++;
            }

            br.close();
        } catch (Exception e) {
            System.out.println("Error loading users.");
        }

        return users;
    }

    public void save(User[] users, int count) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));

            for (int i = 0; i < count; i++) {
                User u = users[i];
                String line = "";

                if (u instanceof Student) {
                    Student s = (Student) u;
                    line = "Student," + s.getUserId() + "," + s.getPassword() + "," +
                           s.getName() + "," + s.getFacultyOrDepartment() + "," +
                           s.getContactNumber() + "," + s.getProgramme();
                } else if (u instanceof Staff) {
                    line = "Staff," + u.getUserId() + "," + u.getPassword() + "," +
                           u.getName() + "," + u.getFacultyOrDepartment() + "," +
                           u.getContactNumber() + ",NULL";
                } else if (u instanceof Admin) {
                    line = "Admin," + u.getUserId() + "," + u.getPassword() + "," +
                           u.getName() + "," + u.getFacultyOrDepartment() + "," +
                           u.getContactNumber() + ",NULL";
                }

                bw.write(line);
                bw.newLine();
            }

            bw.close();
        } catch (Exception e) {
            System.out.println("Error saving users.");
        }
    }
}