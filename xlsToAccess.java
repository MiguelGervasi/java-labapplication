import java.io.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.*;
import java.util.*;
import java.sql.*;
import java.util.Set;
import java.util.HashSet;
import javax.swing.JOptionPane;

public class xlsToAccess
{
	private static String driver;
	private static String url;
	private static String username;
	private static String password;
	private static Connection conn;
	private static PreparedStatement sql_statement;
	private static String jdbc_insert_sql;
	private static HSSFSheet my_worksheet;
	private static String import_action;

	public xlsToAccess(String file, String action) throws Exception
	{
		import_action = action;
		String url = "jdbc:mysql://localhost/lab_db";
		String username = "lab_admin";
		String password = "mypassword";
		Class.forName ("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(url, username, password);
		sql_statement = null;
		jdbc_insert_sql = "";
		/* Create Connection objects */
		/* We should now load excel objects and loop through the worksheet data */
		FileInputStream input_document = new FileInputStream(new File(file));
		/* Load workbook */
		HSSFWorkbook my_xls_workbook = new HSSFWorkbook(input_document);
		/* Load worksheet */
		my_worksheet = my_xls_workbook.getSheetAt(0);
		// we loop through and insert data
		Iterator<Row> rowIterator = my_worksheet.iterator();
		Set<Student> difference;
		Set<Logs> unique;
		Iterator<Student> it;
		Iterator<Logs> un_it;
		int size;
		int un_size;
		int count;
		int count_;

		switch(import_action)
		{
			case "Logs Only":

				jdbc_insert_sql = "INSERT INTO LOGS(DATESTAMP, ID, SIGNIN, SIGNOUT) VALUES (?,?,?,?)";
				sql_statement = conn.prepareStatement(jdbc_insert_sql);
				unique = getUniqueLogs();
				un_it = unique.iterator();
				un_size = unique.size();
				count = 0;
				while (un_it.hasNext())
				{
					if(un_size != 0)
					{
						Logs log = un_it.next();
						sql_statement.setString(1, log.datestamp);
						sql_statement.setString(2, log.toString());
						sql_statement.setString(3, log.signin);
						sql_statement.setString(4, log.signout);
						sql_statement.executeUpdate();
						count++;
					}
				}
				//System.out.println(count + " New Records Added!");
				JOptionPane.showMessageDialog(null,count + " New Records Added!","",JOptionPane.INFORMATION_MESSAGE);
				break;



			case "Students Only":

				jdbc_insert_sql = "INSERT INTO Students (ID, firstname, lastname) VALUES(?,?,?)";
				sql_statement = conn.prepareStatement(jdbc_insert_sql);
				difference = getUniqueStudents();
				it = difference.iterator();
				size = difference.size();
				count_ = 0;
				while (it.hasNext())
				{
					if(size != 0)
					{
						Student student = it.next();
						sql_statement.setInt(1, student.id);
						sql_statement.setString(2, student.firstname);
						sql_statement.setString(3, student.lastname);
						sql_statement.executeUpdate();
						count_++;
					}
				}
				//System.out.println(count_ + " New Students Added!");
				JOptionPane.showMessageDialog(null,count_ + " New Students Added!","",JOptionPane.INFORMATION_MESSAGE);
				break;

			case "Employee Logs Only":

				jdbc_insert_sql = "INSERT INTO EmployeeLogs(DATESTAMP, ID, SIGNIN, SIGNOUT) VALUES (?,?,?,?)";
				sql_statement = conn.prepareStatement(jdbc_insert_sql);
				unique = getUniqueLogs();
				un_it = unique.iterator();
				un_size = unique.size();
				count = 0;
				while (un_it.hasNext())
				{
					if(un_size != 0)
					{
						Logs log = un_it.next();
						sql_statement.setString(1, log.datestamp);
						sql_statement.setString(2, log.toString());
						sql_statement.setString(3, log.signin);
						sql_statement.setString(4, log.signout);
						sql_statement.executeUpdate();
						count++;
					}
				}
				//System.out.println(count + " New Records Added!");
				JOptionPane.showMessageDialog(null,count + " New Records Added!","",JOptionPane.INFORMATION_MESSAGE);
				//after adding to employeelogs add to logs
				new xlsToAccess(file,"Logs Only");
				break;

			case "Employees Only":

				jdbc_insert_sql = "INSERT INTO Employees (ID, firstname, lastname) VALUES(?,?,?)";
				sql_statement = conn.prepareStatement(jdbc_insert_sql);
				difference = getUniqueStudents();
				it = difference.iterator();
				size = difference.size();
				count_ = 0;
				while (it.hasNext())
				{
					if(size != 0)
					{
						Student student = it.next();
						sql_statement.setInt(1, student.id);
						sql_statement.setString(2, student.firstname);
						sql_statement.setString(3, student.lastname);
						sql_statement.executeUpdate();
						count_++;
					}
				}
				//System.out.println(count_ + " New Students Added!");
				JOptionPane.showMessageDialog(null,count_ + " New Students Added!","",JOptionPane.INFORMATION_MESSAGE);
				break;


			case "Both Students / Logs":
				new xlsToAccess(file,"Students Only");
				new xlsToAccess(file,"Logs Only");
				break;

			case "Both Employees / Employee Logs":
				new xlsToAccess(file,"Employees Only");
				new xlsToAccess(file,"Employee Logs Only");
				break;

		}

		input_document.close();
		conn.commit();
		conn.close();
	}
	//getUniqueListofStudents
	public static Set<Student> getUniqueStudents() throws Exception
	{
		Set dbstudents = new HashSet();
		Set xlsstudents = new HashSet();

		for (int j=1; j< my_worksheet.getLastRowNum() + 1; j++) {
			Row row = my_worksheet.getRow(j);
			Cell Datestampcell = row.getCell(0);
			Cell IDcell = row.getCell(1);
			Cell FCell = row.getCell(2);
			Cell LCell = row.getCell(3);
			Cell Signincell = row.getCell(4);
			Cell Signoutcell = row.getCell(5);

			IDcell.setCellType(Cell.CELL_TYPE_STRING);
			xlsstudents.add(new Student(Integer.parseInt(IDcell.getStringCellValue()),FCell.getStringCellValue(),LCell.getStringCellValue()));
		}

		Statement st = conn.createStatement();
		ResultSet rs = null;
		if(import_action == "Students Only" || import_action == "Both Students / Logs")
			rs=st.executeQuery("select * from students ORDER BY firstname");
		else if(import_action == "Employees Only" || import_action == "Both Employees / Employee Logs")
			rs=st.executeQuery("select * from employees ORDER BY firstname");
		ResultSetMetaData meta = rs.getMetaData();
		while (rs.next()){
			dbstudents.add(new Student(rs.getInt("id"),rs.getString("firstname"),rs.getString("lastname")));
		}
		xlsstudents.removeAll(dbstudents);
		return xlsstudents;
	}





	public static Set<Logs> getUniqueLogs() throws Exception
	{
		Set<Logs> dblogs = new HashSet<Logs>();
		Set<Logs> xlslogs = new HashSet<Logs>();

		for (int j=1; j< my_worksheet.getLastRowNum() + 1; j++) {
				Row row = my_worksheet.getRow(j);
				Cell Datestampcell = row.getCell(0);
				Cell IDcell = row.getCell(1);
				Cell FCell = row.getCell(2);
				Cell LCell = row.getCell(3);
				Cell Signincell = row.getCell(4);
				Cell Signoutcell = row.getCell(5);

			IDcell.setCellType(Cell.CELL_TYPE_STRING);
			xlslogs.add(new Logs(Integer.parseInt(IDcell.getStringCellValue()),Datestampcell.getStringCellValue(),Signincell.getStringCellValue(),Signoutcell.getStringCellValue()));
		}

		Statement st = conn.createStatement();
		ResultSet rs = null;
		if(import_action == "Logs Only" || import_action == "Both Students / Logs")
			rs=st.executeQuery("select * from logs ORDER BY datestamp, signin");
		else if(import_action == "Employee Logs Only" || import_action == "Both Employees / Employee Logs")
			rs=st.executeQuery("select * from employeelogs ORDER BY datestamp, signin");
		ResultSetMetaData meta = rs.getMetaData();
		while (rs.next())
		{
			dblogs.add(new Logs(rs.getInt("id"),rs.getString("datestamp"),rs.getString("signin"),rs.getString("signout")));
		}
		//System.out.println(xlslogs.size());
		//System.out.println(dblogs.size());

		xlslogs.removeAll(dblogs);
		//System.out.println(xlslogs.size());

		return xlslogs;
	}





}