import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import java.util.Calendar;
import java.sql.*;

public class CreateExcelFile
{
	public CreateExcelFile(String filename, String day1, String day4, String option)
	{
		try
		{
			long DAY_IN_MS = 1000 * 60 * 60 * 24;
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			Calendar c = Calendar.getInstance();
			HSSFWorkbook hwb=new HSSFWorkbook();
			HSSFSheet sheet =  hwb.createSheet("new sheet");

			HSSFRow rowhead = sheet.createRow((short)0);
			rowhead.createCell((short) 0).setCellValue("Date");
			rowhead.createCell((short) 1).setCellValue("ID");
			rowhead.createCell((short) 2).setCellValue("First Name");
			rowhead.createCell((short) 3).setCellValue("Last Name");
			rowhead.createCell((short) 4).setCellValue("Sign In");
			rowhead.createCell((short) 5).setCellValue("Sign Out");

			String url = "jdbc:mysql://localhost/lab_db";
			String username = "lab_admin";
			String password = "mypassword";
			Class.forName ("com.mysql.jdbc.Driver");

			Connection con = DriverManager.getConnection(url, username, password);
			Statement st=con.createStatement();
			ResultSet rs=null;

			switch(option){
				case "Student Logs":
					rs=st.executeQuery("Select A.id,A.firstname,A.lastname,B.datestamp,B.signin,B.signout FROM ( STUDENTS as A LEFT OUTER JOIN LOGS AS B ON A.ID = B.ID ) WHERE datestamp BETWEEN '"
					+day1+"' AND '"+day4+"' ORDER BY datestamp, signin");
					break;
				case "Employee Logs":
					rs=st.executeQuery("Select A.id,A.firstname,A.lastname,B.datestamp,B.signin,B.signout FROM ( EMPLOYEES as A LEFT OUTER JOIN LOGS AS B ON A.ID = B.ID ) WHERE datestamp BETWEEN '"
					+day1+"' AND '"+day4+"' ORDER BY datestamp, signin");
					break;
				}

			int i=1;
			while(rs.next())
			{
				HSSFRow row = sheet.createRow((short)i);
				row.createCell((short) 0).setCellValue(rs.getString("datestamp"));
				row.createCell((short) 1).setCellValue(Integer.parseInt(rs.getString("id")));
				row.createCell((short) 2).setCellValue(rs.getString("firstname"));
				row.createCell((short) 3).setCellValue(rs.getString("lastname"));
				row.createCell((short) 4).setCellValue(rs.getString("signin"));
				row.createCell((short) 5).setCellValue(rs.getString("signout"));
				i++;
			}

			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);

			FileOutputStream fileOut =  new FileOutputStream(filename);
			hwb.write(fileOut);
			fileOut.close();
			JOptionPane.showMessageDialog(null,"Your excel file has been generated!","Export...",JOptionPane.INFORMATION_MESSAGE);

		}catch ( Exception ex )
		{
			System.out.println(ex);
		}

    }
}