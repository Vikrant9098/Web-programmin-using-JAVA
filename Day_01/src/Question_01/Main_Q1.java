package Question_01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main_Q1 
{

    public static void main(String[] args) 
    {

        // Try-with-resources to manage resources like Scanner, Connection, and PreparedStatement automatically
        try (
	            Scanner scanner = new Scanner(System.in); // Scanner for user input
        		
	            // Create a connection to the MySQL database
	            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/day1adj", "root", "cdac");
	            
        		//1 executeQuery() - returns ResultSet
        		//2 executeUpdate() - returns int
        		//3 execute() - returns boolean
        		//? Placeholders in the SQL query
        		// .setString , .setInt() are the methods used to set values for the placeholders 
        		//similarly,we have other .set methods
        		//They work together to make sql queries flexible and secure
        		
        		//Why preparedStatements , whynot normal Statements
        		//PreparedStatements - 
        		//1 When there's user input
        		//2 For repeated queries
        		//3 To prevent SQL injection (Hacking method where attackers trick a database into running harmful commands by entering malicious input into a website or app)
        		//Statement(Normal statement)
        		//1 For simple, one-time queries
        		//2 When there's no user input
        		
        		
        		//.prepareStatement - 
        		//1 Prepares and compiles SQL once for faster execution
        		//2 Prevents SQL injection by using placeholder(?)
        		//3 Binds dynamic values with methods like setInt(),setString()
        		//4 Executes SQL queries securely and efficiently
        		
        		
        		// PreparedStatements for SQL queries
	            PreparedStatement stUpdate = connection.prepareStatement("UPDATE usertable SET password=? WHERE username=?");
	            PreparedStatement psInsert = connection.prepareStatement("INSERT INTO usertable VALUES(?,?,?,?,?)");
	            PreparedStatement psSelectUser = connection.prepareStatement("SELECT * FROM usertable WHERE username=?");
	            PreparedStatement psSelectCity = connection.prepareStatement("SELECT * FROM usertable WHERE city=?");
	        ) 
        {

            // Infinite loop to keep the program running until the user selects the option to exit
            while (true) {
                // Display the menu options to the user
                System.out.println("\n--- User Management System ---");
                System.out.println("1. Register a User");
                System.out.println("2. List All Users Based on City");
                System.out.println("3. Update Password of a User");
                System.out.println("4. Display User Information Based on User Name");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");

                int choice = scanner.nextInt(); // Read user input for choice
                scanner.nextLine(); // Consume the newline character left by nextInt()

                switch (choice) {
                    case 1:
                        // Register a new user by calling the registerUser method
                        registerUser(psInsert, scanner);
                        break;
                    case 2:
                        // List users based on city by calling listUsersByCity method
                        listUsersByCity(psSelectCity, scanner);
                        break;
                    case 3:
                        // Update password of a user by calling updatePassword method
                        updatePassword(psSelectUser, stUpdate, scanner);
                        break;
                    case 4:
                        // Display user info based on username by calling displayUserInfo method
                        displayUserInfo(psSelectUser, scanner);
                        break;
                    case 5:
                        // Exit the program
                        System.out.println("Exiting the system. Goodbye!");
                        return;
                    default:
                        // If the user enters an invalid option, print an error message
                        System.out.println("Invalid choice! Please try again.");
                }
            }

        } catch (SQLException e) {
            // Handle SQL exceptions and print the stack trace
            e.printStackTrace();
        }
    }

    // Method to display user information based on username
    private static void displayUserInfo(PreparedStatement psSelectUser, Scanner sc) throws SQLException 
    {
        System.out.print("Enter Your username: ");
        String username = sc.nextLine(); // Read the username input

        // Set the username parameter for the SQL query
        psSelectUser.setString(1, username);

        // Execute the query and get the result
        ResultSet result = psSelectUser.executeQuery();

        // If a user with the given username exists, display the details
        if (result.next()) {
            do {
                System.out.println(result.getString(1) + " "  // Display each column in the result set
                    + result.getString(2) + " "
                    + result.getString(3) + " "
                    + result.getString(4) + " "
                    + result.getString(5));
                System.out.println("----------------------------------------");
            } while (result.next()); // Continue if there are more results
        } else {
            // If no user is found with the given username, inform the user
            System.out.println("No user found with the username: " + username);
        }
    }

    // Method to update the password of an existing user
    private static void updatePassword(PreparedStatement psSelectUser, PreparedStatement stUpdate, Scanner sc) throws SQLException {
        System.out.print("Enter Your username: ");
        String username = sc.nextLine(); // Read the username input

        // Set the username parameter for the SQL query
        psSelectUser.setString(1, username);

        // Execute the query and get the result
        ResultSet result = psSelectUser.executeQuery();

        // If the user exists in the database, proceed with password update
        if (result.next()) {
            System.out.print("Enter Your new Password: ");
            String newPassword = sc.nextLine(); // Read the new password

            // Set the new password and the username in the update query
            stUpdate.setString(1, newPassword);
            stUpdate.setString(2, username);

            // Execute the update query
            stUpdate.executeUpdate();
            System.out.println("Password Update Successful!!");
        } else {
            // If no user is found with the given username, inform the user
            System.out.println("No user found with the username: " + username);
        }
    }

    // Method to list users based on city
    private static void listUsersByCity(PreparedStatement psSelectCity, Scanner sc) throws SQLException {
        System.out.print("Enter Your city: ");
        String city = sc.nextLine(); // Read the city input

        // Set the city parameter for the SQL query
        psSelectCity.setString(1, city);

        // Execute the query and get the result
        ResultSet result = psSelectCity.executeQuery();

        // If users are found in the given city, display their information
        if (result.next()) {
            do {
                System.out.println(result.getString(1) + " "  // Display each column in the result set
                    + result.getString(2) + " "
                    + result.getString(3) + " "
                    + result.getString(4) + " "
                    + result.getString(5));
                System.out.println("----------------------------------------");
            } while (result.next()); // Continue if there are more results
        } else {
            // If no users are found in the given city, inform the user
            System.out.println("No user found in the city: " + city);
        }
    }

    // Method to register a new user
    private static void registerUser(PreparedStatement psInsert, Scanner sc) throws SQLException {
        System.out.print("Enter Your username: ");
        String username = sc.nextLine(); // Read the username input

        System.out.print("Enter Your password: ");
        String password = sc.nextLine(); // Read the password input

        System.out.print("Enter Your name: ");
        String name = sc.nextLine(); // Read the name input

        System.out.print("Enter Your email: ");
        String email = sc.nextLine(); // Read the email input

        System.out.print("Enter Your city: ");
        String city = sc.nextLine(); // Read the city input

        // Set the parameters for the SQL insert query
        psInsert.setString(1, username);
        psInsert.setString(2, password);
        psInsert.setString(3, name);
        psInsert.setString(4, email);
        psInsert.setString(5, city);

        // Execute the insert query
        psInsert.executeUpdate();
        System.out.println("Registration Successful!!");
    }
}
