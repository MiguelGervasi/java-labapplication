import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import java.util.Calendar;
import java.sql.*;
import java.io.File;

public class AutomatedExportExcel
{

	public static String filename;

	public static void export()
	{
		System.out.println("Exporting to Excel File...");
		SimpleDateFormat date_format = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat date_format2 = new SimpleDateFormat("MM-dd-yyyy");
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		c2.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
		String day1 = date_format.format(c1.getTime());
		String day4 = date_format.format(c2.getTime());
		String day1filename = date_format2.format(c1.getTime());
		String day4filename = date_format2.format(c2.getTime());

		filename = "timesheets/"+ day1filename + "-" + day4filename + "___SignInSheet.xls";
		File file = new File(filename);

		int counter = 0;
		if(file.exists())
		{
			while(file.exists()) //if file exists add that number after and keep iterating until
			{

				filename = "timesheets/"+ day1filename + "-" + day4filename + "___SignInSheet" + counter + ".xls";
				file = new File(filename);
				counter++;

			}
		}
		if(!file.exists())
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

				String driver = "sun.jdbc.odbc.JdbcOdbcDriver";
				String url = "jdbc:odbc:mdbTEST";
				String username = "";
				String password = "";
				Class.forName(driver);

				Connection con = DriverManager.getConnection(url, username, password);
				Statement st=con.createStatement();
				ResultSet rs=st.executeQuery("Select A.id,A.firstname,A.lastname,B.datestamp,B.signin,B.signout FROM ( STUDENTS as A LEFT OUTER JOIN LOGS AS B ON A.ID = B.ID ) WHERE format([datestamp],'MM/dd/yyyy') BETWEEN '"
				+day1+"' AND '"+day4+"' ORDER BY datestamp, signin");

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
				System.out.println("Exported Successfully!...");

			}catch (Exception e)
			{
				System.out.println(e);
			}
		}

	}
}