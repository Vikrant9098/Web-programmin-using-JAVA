package Question_02;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class task 
{
	//Step 2 - Establish the connection to the database
	public static Connection getConnection() 
	{
		String url = "jdbc:mysql://127.0.0.1:3306/day1_webusingjava";
		String username ="root";
		String password ="Vikrant@4";
		
		try
		{
			//Creating a connection to the database
			return DriverManager.getConnection(url, username, password);
			//It returns the Connection Object which is the part of the java.sql package
		}
		catch(SQLException e)
		{
			System.out.println("Error: Unable to connect to the database");
			e.printStackTrace();
			return null;
		}
	}

	//Step 3 - Create the method to create a table
	public static void createTable(Connection connection)
	{
		Scanner scanner = new Scanner(System.in);
		
		//1 Accept the table name
		System.out.println("Enter the Table name: ");
		String tableName = scanner.nextLine();
		
		//StringBuilder - Used when modifying the strings frequently(more efficient)
		//String - Best for cases when the string won't change
		
		//2 StringBuilder to dynamically build the SQL query for creating the table
		StringBuilder createTableSQL = new StringBuilder("Create TABLE " + tableName + " (");
		
		
		//Use of arraylist since it grows automatically and allows fast access
		//to elements by index, making it simple and efficient for storing items
		
		//List to store columns and the primary key column
		List<String> columns = new ArrayList<>();

		String primaryKeyColumn = null;
		
		//Loop to allow the user to add columns, set the primary key, or save
		while(true)
		{
			System.out.println("1: Add column");
			System.out.println("2: Set Primary Key");
			System.out.println("3: Save");
			System.out.print("Select an option: ");
			
			int choice = scanner.nextInt();
			
			scanner.nextLine(); //Consume newline
			
			switch(choice)
			{
				case 1://Add column 
				{
					
					System.out.println("Enter the column name: ");
					String columnName = scanner.nextLine();
					
					//Show the available data types for the user to select
					System.out.println("Select a data type: ");
					System.out.println("1: VARCHAR");
					System.out.println("2: INT");
					System.out.println("3: FLOAT");
					System.out.print("Choose a datatype: ");
					
					int dataTypeChoice = scanner.nextInt();
					
					scanner.nextLine(); //Consume newline
					
					//Define the datatype based on user choice
					String dataType = "";
					
					switch(dataTypeChoice)
					{
					case 1:
								dataType = "VARCHAR(255)"; //Default varchar length
								break;
					
					case 2:
								dataType = "INT";
								break;
					
					case 3:
								dataType = "FLOAT";
								break;
					
					default:
								System.out.println("Invalid choice ! Defaulting to VARCHAR.");
								dataType = "VARCHAR(255)";
								break;
							
					}
					
					//Add the column definition to the list
					columns.add(columnName + " " + dataType);
					
					break;
				}
				
				case 2: //Set Primary Key 
				{
					
					
					System.out.println("Select a column to set as Primary Key: ");
					//To display the entries of the columns in the 'columns'
					for(int i = 0 ; i < columns.size() ; i++)
					{
						System.out.println(i + 1 + ". " + columns.get(i));
					}
					
					System.out.println("Select column index for Primary Key: ");
					int pkChoice = scanner.nextInt() - 1; // Subtract 1 for 0 - based index 
					scanner.nextLine(); //Consume newline
					
					//Set the primary key column if valid choice is made
					if(pkChoice >= 0 && pkChoice < columns.size())
					{
						//columns - list containing column definition (ex. "id INT")
						//get(pkChoice) - fetcher the element from the list at the index phChoice in these case (ex. pkChoice is 0 , it will get "id INT")
						//split(" ")  - method to split the string into parts based on spaces
						//ex."id INT", it will split into two parts: ["id","INT"]
						//[0] - This selects the first part from the split array(i.e "id")
						primaryKeyColumn = columns.get(pkChoice).split(" ")[0]; //Extract the column name from the columns defination
						System.out.println("Primary key set to: " + primaryKeyColumn);
					}
					else
					{
						System.out.println("Invalid Choice.");
					}
					
					break;
				}
				
				case 3://Save step
				{
					if(columns.size() == 0)
					{
						System.out.println("No columns added. Please add at least one column. ");
						break;
					}
					
					//Add primary key constraint if defined
					if(primaryKeyColumn != null)
					{
						createTableSQL.append(", PRIMARY KEY (" + primaryKeyColumn + ")");
					}
					
					//Add columns to the SQL Query
					for(String column : columns)
					{
						createTableSQL.append(column).append(", ");
					}
					
					//Remove the trailing comma
					createTableSQL.setLength(createTableSQL.length() - 2);
					createTableSQL.append("); ");
					
					//Execute the SQL Query to create the table
					//Statement is used to execute the SQL queries
					//try-with-resource is used to close the statement automatically when done,
					//even if there is an error
					try(Statement statement = connection.createStatement())
					{
						//executeUpdate - used to run SQL statements like INSERT, UPDATE, DELETE or CREATE (in short which modify the database)
						statement.executeUpdate(createTableSQL.toString());
						System.out.println("Table " + tableName + " created Successfully. ");
					}
					catch(SQLException e)
					{
						System.out.println("Error: Unable to create the table.");
						e.printStackTrace();
					}
					
					return;
				}
				
				default: 
				{
					System.out.println("Invalid choice. Try again !! ");
					
					break;
				}
			}
		}
	}

	//Step 4 - Create the method to display a table
	public static void displayColumns(Connection connection)
	{
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Enter the table name to display columns: ");
		String tableName = scanner.nextLine();
		
		//Query to get the column names of the table
		String query = "SHOW COLUMNS FROM " + tableName;
		
		//ResultSet is a Object 
		//Its return type is ResultSet itself
		//ResultSet is used only for SELECT queries,where data is returned
		//it holds the data returned from a SELECT query, allowing you to 
		//process the results row by row
		try(Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query))
		{
			System.out.println("Columns in tables " + tableName + ":");
			//ResultSet.next() - 
			// It is similar to a cursor in that it moves through the 
			//rows of the result set one by one 
			//next() moves the cursor to the next row in the ResultSet
			//Returns true if there is another row to process and false
			//if there are no more rows
			while(resultSet.next())
			{
				//getString() is used to retrieve a column value as a 
				//string from the current row of the ResultSet
				//We can pass the columnname or columnindex to getString() 
				//it returns the value in that column as a String
				System.out.println(resultSet.getString("Field")); //Display the column name
			}
		}
		catch(SQLException e)
		{
			System.out.println("Error: Unable to retrieve columns. ");
			e.printStackTrace();
		}
	
		
	}
	
	
};
