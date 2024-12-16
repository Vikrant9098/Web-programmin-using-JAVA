package Question_02;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main_Q2 
{
	public static void main(String[] args)
	{
		//Step 1 - Establish the Connection
		Connection connection = task.getConnection();
		
		if(connection == null)
		{
			System.out.println("Exiting the program due to database connection failure");
			return;
		}
		
		Scanner scanner = new Scanner(System.in);
		
		
		while(true)
		{
			System.out.println("1: Create Table");
			System.out.println("2: Display Columns of a Table");
			System.out.println("0: Exit");
			System.out.print("Select an option: ");
			
			int choice = scanner.nextInt();
			
			scanner.nextLine(); //Consume newline - to avoid the problems like skipping the next input or reading an empty line 
			
			switch(choice)
			{
				case 1:	
							//Step 2 - Create the method to create a table
						 	task.createTable(connection);
						 	break;
				
				case 2:
							//Step 3 - Call the method to display columns
							task.displayColumns(connection);
							break;
				
				case 3:
							//Step 4 - Exit the program
							System.out.println("Exiting the program... ");
							
							try
							{
								connection.close(); //Close the connection
							}
							catch(SQLException e)
							{
								e.printStackTrace();
							}
							
							return;
					
				 default:
					 		System.out.println("Invalid choice. Please Select again ! ");
					 	  
			}
			
			
		}
	}

}
