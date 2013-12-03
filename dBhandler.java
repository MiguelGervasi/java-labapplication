import java.awt.Dimension;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.PreparedStatement;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Arrays;
import java.util.regex.Pattern;

public class dBhandler
{
	public void insertStudent(int id, String fname, String lname) throws Exception {
		Connection conn = getConnection();
		Statement st = conn.createStatement();
		st.executeUpdate("insert into students (id, firstname, lastname) values ('"+id+"', '"+fname+"', '"+lname+"')");
		st.close();
		conn.close();
  	}

	//check if select student is an employee
	public boolean checkEmployeeStatus(String id) throws Exception
	{
		Connection conn = getConnection();
		Statement st = conn.createStatement();
		ResultSet rs=st.executeQuery("select * from employees where id = '"+id+"' ORDER BY firstname");
		int count = 0;
		while (rs.next()){
			count++;
		}

		st.close();
		conn.close();

		if(count == 0)
			return false;
		else
			return true;
	}

	//sign in employee
  	public void EmployeeSignin(JTable table, DefaultTableModel tablemodel, String id, boolean adminModeOn) throws Exception
  	{

		//insert log into logs
		//Signin(table, tablemodel, id, adminModeOn);

		//then insert copy into employeelogs
		Connection conn = getConnection();
      	Statement st = conn.createStatement();
      	SimpleDateFormat dst = new SimpleDateFormat("MM/dd/yyyy");
	  	SimpleDateFormat tst = new SimpleDateFormat("hh:mm a");
	  	Date date = new Date();
	  	Date time = new Date();
	  	String datestamp = dst.format(date);
      	String timestamp = tst.format(time);
		String insertSQL = "INSERT INTO EmployeeLogs (id,datestamp,signin) VALUES (?, ?, ?)";
		PreparedStatement preparedStatement = conn.prepareStatement(insertSQL);
		String signout = "";

	  	int count = 0;

	  	//check if exists so it can copy +1 is a yes, 0 is that it hasn't been inserted into logs yet
	  	ResultSet rse=st.executeQuery("select id,datestamp,signin,signout from logs where id = '"+id+"' and datestamp= '"+datestamp+"' and signin = '"+timestamp+"'");
		while (rse.next())
		{
			count++;
			preparedStatement.setString(1, rse.getString("id"));
			preparedStatement.setString(2, rse.getString("datestamp"));
			preparedStatement.setString(3, rse.getString("signin"));
			signout = rse.getString("signout");
		}

		//check if exists in employee logs if so increment to 2, if not and count = 1, execute the statement to add it in the employeelogs
		ResultSet rs=st.executeQuery("select * from employeelogs where id = '"+id+"' and datestamp= '"+datestamp+"' and signin = '"+timestamp+"'");
		while (rs.next())
		{
			count++;
		}

		if(count == 1 && signout == null) //check if row exists if it does exist in logs then also check if signoutnull
		{
			preparedStatement.executeUpdate();//st.executeUpdate("insert into Logs (ID, Datestamp, SignIn) values ('"+id+"', '"+datestamp+"', '"+timestamp+"')");
		}

      st.close();
   	  conn.close();
  	}

	//student signin
  	public void Signin(JTable table, DefaultTableModel tablemodel, String id, boolean adminModeOn) throws Exception
  	{
		Connection conn = getConnection();
      	Statement st = conn.createStatement();
      	SimpleDateFormat dst = new SimpleDateFormat("MM/dd/yyyy");
	  	SimpleDateFormat tst = new SimpleDateFormat("hh:mm a");
	  	Date date = new Date();
	  	Date time = new Date();
	  	String datestamp = dst.format(date);
      	String timestamp = tst.format(time);

	  	int count = 0;
      	ResultSet rse=st.executeQuery("select * from logs where id = '"+id+"' and datestamp= '"+datestamp+"' and signin = '"+timestamp+"' order by datestamp, signin");
		while (rse.next())
		{
			count++;
		}
		if(count == 0)
		{
			st.executeUpdate("insert into Logs (ID, Datestamp, SignIn) values ('"+id+"', '"+datestamp+"', '"+timestamp+"')");
		  	tablemodel.setRowCount(0);
		  	ResultSet rs=st.executeQuery("select * from logs where id = '"+id+"' order by datestamp, signin");
		  	//ResultSet rs=st.executeQuery("select * from logs where id = '"+id+"' UNION select * from employeelogs where id = '"+id+"'");
		  	ResultSetMetaData meta = rs.getMetaData();
		  	int numberOfColumns = meta.getColumnCount();
		  	while(rs.next())
		  	{
				Object [] rowData = new Object[numberOfColumns];
				for (int i = 0; i < rowData.length; ++i)
				{
					if(i == 0 && !adminModeOn) //if on guest mode then add mask to ID
					//if(i == 0) //if on guest mode then add mask to ID
						rowData[i] = MaskID(rs.getString(i+1));
					else //else when you sign in last sign in row is displayed normal
						rowData[i] = rs.getObject(i+1);
				}
				tablemodel.addRow(rowData);
		  	}
		  	table.setModel(tablemodel);
	  	}

      st.close();
   	  conn.close();
  	}

	//signout for both student and employees if signout is null do something
  	public void Signout(JTable table, DefaultTableModel tablemodel, String id, String datestamp, String signin) throws Exception
  	{
		Connection conn = getConnection();
      	Statement st = conn.createStatement();
	  	SimpleDateFormat tst = new SimpleDateFormat("hh:mm a");
	  	Date time = new Date();
      	String timestamp = tst.format(time);
	  	String signout = "";
	  	ResultSet rs=st.executeQuery("select * from logs where id = '"+id+"' and datestamp= '"+datestamp+"' and signin = '"+signin+"' order by datestamp, signin");

		//check if exist, if so signout logs
		int count = 0;
		while (rs.next()) {
			signout = rs.getString("signout");
			count++;
	  	}

		//check if exist if so signout logs and employees
	  	int emp = 0;
	  	rs=st.executeQuery("select * from employeelogs where id = '"+id+"' and datestamp= '"+datestamp+"' and signin = '"+signin+"'");
		while (rs.next())
		{
			//signout = rs.getString("signout");
			emp++;
		}

	 	if(signout == null)
	 	{
			String cmd = "";
			if(count > 0)
			{
				cmd = "update logs set signout = '"+timestamp+"' where datestamp = '"+datestamp+"' and signin = '"+signin+"' and id = '"+id+"'";
				st.executeUpdate(cmd);
				if(checkEmployeeStatus(id))
				{
					if(emp > 0){
						cmd = "update employeelogs set signout = '"+timestamp+"' where datestamp = '"+datestamp+"' and signin = '"+signin+"' and id = '"+id+"'";
						st.executeUpdate(cmd);
					}
				}
			}

			tablemodel.setValueAt(timestamp, table.getSelectedRow(), 3);
			table.setModel(tablemodel);
		}else if(signin == timestamp){}

      	st.close();
   	  	conn.close();
  	}
/*
  	public void employeeSignout(String id, String datestamp, String signin) throws Exception
  	{
		Connection conn = getConnection();
      	Statement st = conn.createStatement();
	  	SimpleDateFormat tst = new SimpleDateFormat("hh:mm a");
	  	Date time = new Date();
      	String timestamp = tst.format(time);
	  	String signout = "";
	  	String cmd = "";
	  	/*ResultSet rs=st.executeQuery("select * from logs where id = '"+id+"' and datestamp= '"+datestamp+"' and signin = '"+signin+"'");

		//check if exist, if so signout logs
		int count = 0;
		while (rs.next()) {
			signout = rs.getString("signout");
			count++;
	  	}

		//check if exist if so signout logs and employees
	  	int emp = 0;
	  	ResultSet rs=st.executeQuery("select * from employeelogs where id = '"+id+"' and datestamp= '"+datestamp+"' and signin = '"+signin+"'");
		while (rs.next())
		{
			signout = rs.getString("signout");
			emp++;
		}

		if(emp == 1)
		{
			cmd = "update employeelogs set signout = '"+timestamp+"' where datestamp = '"+datestamp+"' and signin = '"+signin+"' and id = '"+id+"'";
			st.executeUpdate(cmd);

		}//else if(signin == timestamp){}

      	st.close();
   	  	conn.close();
  	}
  	*/

	//add into employee list
	public void addEmployee(String id) throws Exception
	{
		Connection conn = getConnection();
		PreparedStatement st = conn.prepareStatement("INSERT INTO Employees SELECT * FROM Students where id = ?");
      	conn.setAutoCommit(true);
      	st.setString(1, id);
      	st.execute(); // select the data from the table
     	st.close();
   	  	conn.close();
	}

	//add into remove from employee list
	public void removeEmployee(String id) throws Exception
	{
		Connection conn = getConnection();
		PreparedStatement st = conn.prepareStatement("delete from Employees where id = ?");
      	conn.setAutoCommit(true);
      	st.setString(1, id);
      	st.execute(); // select the data from the table
     	st.close();
   	  	conn.close();
	}

	//add existing log to employee logs
	public void addtoEmployeeLogs(String id, String datestamp, String signin, String signout) throws Exception
	{
		//check if log is in the employeelogs if not create, if is don't do anything
		if(!checkEmployeeLog(id,datestamp,signin))
		{
			Connection conn = getConnection();
			PreparedStatement st = conn.prepareStatement("INSERT INTO EmployeeLogs SELECT * FROM Logs where id = ? and datestamp = ? and signin = ? and signout = ?");
			conn.setAutoCommit(true);
			st.setString(1, id);
			st.setString(2, datestamp);
			st.setString(3, signin);
			st.setString(4, signout);
			st.execute(); // select the data from the table
			JOptionPane.showMessageDialog(null,"Log successfully Added!","Log Added",JOptionPane.INFORMATION_MESSAGE);
			st.close();
			conn.close();
		}
	}

	//remove existing log from employee list
	public void removefromEmployeeLogs(String id, String datestamp, String signin, String signout) throws Exception
	{
		//check if exists, if not then don't bother
		if(checkEmployeeLog(id,datestamp,signin))
		{
			Connection conn = getConnection();
			PreparedStatement st = conn.prepareStatement("delete from EmployeeLogs where id = ? and datestamp = ? and signin = ? and signout = ?");
			conn.setAutoCommit(true);
			st.setString(1, id);
			st.setString(2, datestamp);
			st.setString(3, signin);
			st.setString(4, signout);
			st.execute(); // select the data from the table
			JOptionPane.showMessageDialog(null,"Log successfully Removed!","Log Removed",JOptionPane.INFORMATION_MESSAGE);
			st.close();
			conn.close();
		}
	}

  	//view all students
	public void selectStudents(DefaultListModel model, JList dbList) throws Exception
	{
		Connection conn = getConnection();
		Statement st = conn.createStatement();
		model.clear();
		ResultSet rs=st.executeQuery("select * from students ORDER BY firstname");
		ResultSetMetaData meta = rs.getMetaData();
		int numberOfColumns = meta.getColumnCount();
		while (rs.next()){
			model.addElement(rs.getString("firstname") + " " + rs.getString("lastname"));
		}

		dbList.setModel(model);
		st.close();
   	  	conn.close();
  	}

  	//view specific student
  	//public void selectStudents(String where) throws Exception {
  	public void selectStudents(int id) throws Exception
  	{
		Connection conn = getConnection();
		Statement st = conn.createStatement();
      	String cmd = "select id,firstname,lastname from students where id = '" + id + "'";
      	st.execute(cmd); // select the data from the table
	  	ResultSet rs = st.getResultSet(); // get any ResultSet that came from our query
	  	if (rs != null) // if rs == null, then there is no ResultSet to view
	  	while ( rs.next() ) // this will step through our data row-by-row
	  	{
	  /* the next line will get the first column in our current row's ResultSet
	   as a String ( getString( columnNumber) ) and output it to the screen */
	  		//System.out.println("\t" + rs.getString(1) + " \t\t" + rs.getString(2) + " \t\t" + rs.getString(3));
	  	}

      	st.close();
   	  	conn.close();
  	}

  //update students where replace either fname lname or id where id = certainnumber or name = certain name
  //two ways write where clause or write id to identify student
  //public void updateStudents(String replace, String where) throws Exception {





  	public void ManageLogs(JTable table, DefaultTableModel tablemodel, String [] olddata, String [] newdata, int choice) throws Exception
  	{
		if(choice == 0)//update
		{
			updateLogs(table,tablemodel,olddata,newdata);
		}
		else if(choice == 1)//add log to employeelogs
		{

			addtoEmployeeLogs(olddata[0],olddata[1],olddata[2],olddata[3]);
		}
		else if(choice == 2)//remove log from employee logs
		{
			removefromEmployeeLogs(olddata[0],olddata[1],olddata[2],olddata[3]);
		}
	}

  	public void updateLogs(JTable table, DefaultTableModel tablemodel, String [] olddata, String [] newdata) throws Exception
  	{
		Connection conn = getConnection();
	  	PreparedStatement st = conn.prepareStatement("UPDATE logs SET id = ?, datestamp = ?, signin = ?, signout = ? WHERE id = ? and datestamp = ? and signin = ? and signout = ?");

		st.setString(1, newdata[0]);
		st.setString(2, newdata[1]);
		st.setString(3, newdata[2]);
		st.setString(4, newdata[3]);

		st.setString(5, olddata[0]);
		st.setString(6, olddata[1]);
		st.setString(7, olddata[2]);
		st.setString(8, olddata[3]);

	  	PreparedStatement st2 = conn.prepareStatement("UPDATE employeelogs SET id = ?, datestamp = ?, signin = ?, signout = ? WHERE id = ? and datestamp = ? and signin = ? and signout = ?");

		st2.setString(1, newdata[0]);
		st2.setString(2, newdata[1]);
		st2.setString(3, newdata[2]);
		st2.setString(4, newdata[3]);

		st2.setString(5, olddata[0]);
		st2.setString(6, olddata[1]);
		st2.setString(7, olddata[2]);
		st2.setString(8, olddata[3]);

		//field validator
		String errmsg = "";
		if(!(Pattern.matches("(\\d){1,9}", newdata[0])) || !isValidDate(newdata[1],"MM/dd/yyyy") || newdata[1].length() > 10 || !isValidDate(newdata[2],"hh:mm a") || !isValidDate(newdata[3],"hh:mm a") || newdata[2].length() > 8 || newdata[3].length() > 8)
		{
			if(!(Pattern.matches("(\\d){1,9}", newdata[0]))) //check for id
				errmsg += "Please enter a valid Student ID\n";
			if(!isValidDate(newdata[1],"MM/dd/yyyy") || newdata[1].length() > 10) //check for date format and length
				errmsg += "Incorrect Date Format.\n";
			if(!isValidDate(newdata[2],"hh:mm a") || !isValidDate(newdata[3],"hh:mm a") || newdata[2].length() > 8 || newdata[3].length() > 8) //check for time format and length time in and time out
				errmsg += "Incorrect Time Format.\n";
			JOptionPane.showMessageDialog(null, errmsg, "Input Validation", JOptionPane.ERROR_MESSAGE); //display error message
		}else
		{
			if(checkEmployeeLog(olddata[0],olddata[1],olddata[2])) //if it is an employee log update both logs and employee logs, if not then just update logs
			{
				st.executeUpdate();
				st2.executeUpdate();
			}
			else
			{
				st.executeUpdate();
			}

			//set new values
			tablemodel.setValueAt(newdata[0], table.getSelectedRow(), 0);
			tablemodel.setValueAt(newdata[1], table.getSelectedRow(), 1);
			tablemodel.setValueAt(newdata[2], table.getSelectedRow(), 2);
			tablemodel.setValueAt(newdata[3], table.getSelectedRow(), 3);

			table.setModel(tablemodel);
			JOptionPane.showMessageDialog(null,"Log successfully Updated!","Log Added",JOptionPane.INFORMATION_MESSAGE);

		}

      	st.close();
   	  	conn.close();
  	}

	//update students, update students and logs based on choice when right clicking on students from Jlist, delete, and delete logs
  	public void ManageStudents(JTable table, DefaultTableModel tablemodel, JList dbList, DefaultListModel model, String [] olddata, String [] newdata, int choice) throws Exception
  	{
		Connection conn = getConnection();
		PreparedStatement st = conn.prepareStatement("UPDATE students SET id = ?, firstname = ?, lastname = ? WHERE id = ?");
		PreparedStatement stx = conn.prepareStatement("UPDATE logs SET id = ? where id = ?");
		Statement s = conn.createStatement();

		Statement sty = conn.createStatement();
		Statement stz = conn.createStatement();

		st.setString(1, newdata[0]);
		st.setString(2, newdata[1]);
		st.setString(3, newdata[2]);
		st.setString(4, olddata[0]);

		String student1 = "" + olddata[1] + " " + olddata[2] + " (#" + olddata[0] + ")";
		String student2 = "" + newdata[1] + " " + newdata[2] + " (#" + newdata[0] + ")";

		//validate id, firstname, lastname
		String errormsg = "";
		if (!(Pattern.matches("^[a-zA-Z]+$", newdata[2])) || !(Pattern.matches("^[a-zA-Z]+$", newdata[1])) || !(Pattern.matches("(\\d){1,9}", newdata[0]))) {
			if (!(Pattern.matches("^[a-zA-Z]+$", newdata[1])) || !(Pattern.matches("^[a-zA-Z]+$", newdata[2])))
				errormsg += "Please enter a valid First Name and/or Last Name.\n";
			if (!(Pattern.matches("(\\d){1,9}", newdata[0])))
				errormsg += "Please enter a valid Student ID. \n";
			JOptionPane.showMessageDialog(null, errormsg, "Input Validation", JOptionPane.ERROR_MESSAGE); //display error message
		}else //continue when passes validation
		{
			if(choice == 0)//update student
			{
				int result2 = JOptionPane.showConfirmDialog(null,"Are you sure you would like to update " + student1 + " to " + student2 + "?","Updating..." , JOptionPane.YES_NO_OPTION);
				if(result2 == 0)
				{
					st.executeUpdate();
					model.setElementAt(newdata[1] + " " + newdata[2], dbList.getSelectedIndex());
				}

			}
			else if (choice == 1)//update student and logs
			{


				s.execute("select * from logs where id = '"+olddata[0]+"' order by datestamp, signin");

				String logset = "";

				ResultSet rs = s.getResultSet(); // get any ResultSet that came from our query
				while ( rs.next() ) // this will step through our data row-by-row
				{
					logset += "" + rs.getString(1) + " \t" + rs.getString(2) + " \t" + rs.getString(3) + " \t" +  rs.getString(4) + "\n";
				}

				s.close();

				String separator = "---------------------------------------------------------------------------------------------------";

				JTextArea textArea = new JTextArea();
				JTextArea textArea2 = new JTextArea();
				textArea.setHighlighter(null);
				textArea.setEditable(false);
				textArea2.setHighlighter(null);
				textArea2.setEditable(false);

				JScrollPane jcroll = new JScrollPane(textArea);
				JPanel p = new JPanel();
				textArea.setText("Are you sure you would like to update student " + student1 + "?\nYou will also update his/her logs from the database?  Click YES to Continue.\n\n**Before Changes**\n\n" + student1 + "\n\nLogs that will be Updated\n"
				+separator+"\nID\tDate\tSignIn\tSignOut\n" + separator + "\n" + logset + "" +separator+"\n\n**After Changes**\n\n- " + student1 + " will be updated to " + student2 + "\n- All Log ID's that were (#" + olddata[0] + ") will get updated to (#" + newdata[0] + ")\n\n -- End of Update");
				jcroll.setPreferredSize(new Dimension(400,200));

				p.add(jcroll);

				int count = 0;
				int result = JOptionPane.showConfirmDialog(null,p,"Updating..." , JOptionPane.YES_NO_OPTION);
				if(result == 0)
				{
					stx.setString(1, newdata[0]);
					stx.setString(2, olddata[0]);

					st.executeUpdate();
					stx.executeUpdate();

					model.setElementAt(newdata[1] + " " + newdata[2], dbList.getSelectedIndex());
					while(count < table.getRowCount())
					{
						tablemodel.setValueAt(newdata[0], count, 0);
						tablemodel.setValueAt(newdata[0], count, 0);
						tablemodel.setValueAt(newdata[0], count, 0);
						tablemodel.setValueAt(newdata[0], count, 0);
						count++;
					}
				}
			}
			else if(choice == 2)//delete student
			{

				int result = JOptionPane.showConfirmDialog(null,"Are you sure you would like to delete " + olddata[1] + " " + olddata[2] + " (#" + olddata[0] + ") from the database?","Deleting..." , JOptionPane.YES_NO_OPTION);
				if(result == 0)
				{
					sty.execute("delete from Students where id = '"+olddata[0]+"'");

					int selectedIndex = dbList.getSelectedIndex();

					model.remove(selectedIndex);
					tablemodel.setRowCount(0);

					dbList.setModel(model);
					table.setModel(tablemodel);
				}
			}
			else if(choice == 3)//delete student and logs
			{
				s.execute("select * from Logs where id = '"+olddata[0]+"' order by datestamp, signin");

				String dataset = "";

				ResultSet rs = s.getResultSet(); // get any ResultSet that came from our query
				while ( rs.next() ) // this will step through our data row-by-row
				{
					dataset += "" + rs.getString(1) + " \t" + rs.getString(2) + " \t" + rs.getString(3) + " \t" +  rs.getString(4) + "\n";
				}

				s.close();

				String separator = "---------------------------------------------------------------------------------------------------";
				JTextArea textArea = new JTextArea();
				textArea.setHighlighter(null);
				textArea.setEditable(false);
				JScrollPane jcroll = new JScrollPane(textArea);
				textArea.setText("Are you sure you would like to delete student " + olddata[1] + " " + olddata[2] + " (#" + olddata[0] + ")?\nYou will also delete his/her logs from the database?  Click YES to Continue.\n\n Logs to Be Deleted \n"+separator+"\nID\tDate\tSignIn\tSignOut\n" + separator + "\n" + dataset);
				textArea.setPreferredSize(new Dimension(400,200));

				int result = JOptionPane.showConfirmDialog(null,jcroll,"Deleting..." , JOptionPane.YES_NO_OPTION);
				if(result == 0)
				{

					sty.execute("delete from Students where id = '"+olddata[0]+"'");
					stz.execute("delete from Logs where id = '"+olddata[0]+"'");

					int selectedIndex = dbList.getSelectedIndex();

					model.remove(selectedIndex);
					tablemodel.setRowCount(0);

					dbList.setModel(model);
					table.setModel(tablemodel);
				}
			}
			//addemployeee to employee table
			else if(choice == 4)
			{
				int result = JOptionPane.showConfirmDialog(null,"Are you sure you would like to add this person to the Employee List?","Deleting..." , JOptionPane.YES_NO_OPTION);
				if(result == 0)
				{
					this.addEmployee(olddata[0]);
				}

			}
			//removeemployeee from employee table
			else if(choice == 5)
			{
				int result = JOptionPane.showConfirmDialog(null,"Are you sure you would like to add this person to the Employee List?","Deleting..." , JOptionPane.YES_NO_OPTION);
				if(result == 0)
				{
					this.removeEmployee(olddata[0]);
				}
			}
			dbList.setModel(model);
			table.setModel(tablemodel);

		}

      	st.close();
      	stx.close();
      	sty.close();
      	stz.close();
   	  	conn.close();
  	}

  	//delete by placing date and sign in time //not using this method it is extra just keep in case
  	public void deleteLogs(int selectedRowIndex,JTable table, DefaultTableModel tablemodel, String id, String datestamp, String Signin) throws Exception
  	{
		Connection conn = getConnection();
      	Statement st = conn.createStatement();

      	if(checkEmployeeLog(id,datestamp,Signin)) //check if there is an alike employee log
      	{
			String cmd = "delete from employeelogs where id = '"+id+"' and datestamp = '" + datestamp + "' and signin = '" + Signin+ "'";
      		st.execute(cmd); // select the data from the table
		}

      	String cmd = "delete from logs where id = '"+id+"' and datestamp = '" + datestamp + "' and signin = '" + Signin+ "'";
      	st.execute(cmd); // select the data from the table

	  	tablemodel.removeRow(selectedRowIndex);
	  	table.setModel(tablemodel);

		st.close();
   	  	conn.close();
  	}


  //delete students where id = ??? fill in where clause, for example deleteStudents("where id = '2');
  	public void deleteStudents(String where) throws Exception
  	{
		Connection conn = getConnection();
      	Statement st = conn.createStatement();
      	String cmd = "delete from Students " + where;
      	st.execute(cmd); // select the data from the table
	  	ResultSet rs = st.getResultSet(); // get any ResultSet that came from our query
	  	if (rs != null) // if rs == null, then there is no ResultSet to view
	  	while ( rs.next() ) // this will step through our data row-by-row
	  	{
			  //System.out.println("\t" + rs.getString(1) + " \t\t" + rs.getString(2) + " \t\t" + rs.getString(3));
	  	}

      	st.close();
   	  	conn.close();
  	}

  	public boolean checkEmployeeLog(String id, String datestamp, String timestamp) throws Exception
  	{
		Connection conn = getConnection();
      	Statement st = conn.createStatement();
	  	int count = 0;
	  	boolean isEmployeeLog;

	  	//check if exists so it can copy +1 is a yes, 0 is that it hasn't been inserted into logs yet
	  	ResultSet rse=st.executeQuery("select id,datestamp,signin,signout from employeelogs where id = '"+id+"' and datestamp= '"+datestamp+"' and signin = '"+timestamp+"'");
		while (rse.next())
		{
			count++;
		}

		st.close();
   	  	conn.close();

   	  	if(count == 0)
   	  	{
			isEmployeeLog = false;
   	  	}else
   	  	{
   	  		isEmployeeLog = true;
		}

		return isEmployeeLog;
	}

  	public boolean checkLog(String id, String datestamp, String timestamp) throws Exception
  	{
		Connection conn = getConnection();
      	Statement st = conn.createStatement();
	  	int count = 0;

	  	//check if exists so it can copy +1 is a yes, 0 is that it hasn't been inserted into logs yet
	  	ResultSet rse=st.executeQuery("select id,datestamp,signin,signout from logs where id = '"+id+"' and datestamp= '"+datestamp+"' and signin = '"+timestamp+"'");
		while (rse.next())
		{
			count++;
		}

		st.close();
   	  	conn.close();

   	  	if(count > 0)
   	  	{
   	  		return true;
   	  	}else
   	  	{
   	  		return false;
		}
	}


  	public void commit(JTable table, DefaultTableModel tablemodel) throws Exception
  	{
		Connection conn = getConnection();
		conn.commit();
		table.setModel(tablemodel);
  		conn.close();
  	}

  	public void rollback(JTable table, DefaultTableModel tablemodel) throws Exception
  	{
		Connection conn = getConnection();
		conn.rollback();
		table.setModel(tablemodel);
  	 	conn.close();
  	}

	public String MaskID(String in)
	{
		final char[] ca = in.toCharArray();
		Arrays.fill(ca, 0, in.length(), 'X');
		return new String(ca);
	}

	boolean isValidDate(String input,String formatstr) {
		SimpleDateFormat format = new SimpleDateFormat(formatstr);
		try {
			format.parse(input);
          	return true;
     	}
     	catch(Exception e){
          	return false;
     	}
	}
    //Connect to database
  	public Connection getConnection() throws Exception
  	{
		//final String fileName = "db\\lab_db_official.accdb";
		//String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
      	//String url = "jdbc:odbc:mdbTEST";
      	//String url = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ="+fileName;
		String url = "jdbc:mysql://localhost/lab_db";
		String username = "lab_admin";
      	String password = "mypassword";
      	Class.forName ("com.mysql.jdbc.Driver");
      	//Class.forName(driver);
      	return DriverManager.getConnection(url, username, password);
  	}

}