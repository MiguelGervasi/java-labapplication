//copyfile
import java.io.*;
import java.nio.file.*;
import java.nio.channels.*;


public class CopyFileTest
{

	public static void main(String[] args)
	{
		new CopyFileTest();
	}

	public CopyFileTest()
	{
		File source=new File("lab_db2.accdb");
		File destination=new File("backups/lab_db2_backup.accdb");

		try{copyFile(source,destination);}catch(Exception e){System.out.println(e);}

	}
    public static void copyFile(File sourceFile, File destFile) throws IOException
    {
		int counter = 0;
		if(destFile.exists()) {
			while(destFile.exists())
			{
				destFile = new File("backups/lab_db_backup"+counter+".accdb");
				counter++;
			}
		}
		else if(!destFile.exists()) {
			destFile.createNewFile();
		}

		FileChannel source = null;
		FileChannel destination = null;
		try {
		source = new RandomAccessFile(sourceFile,"rw").getChannel();
		destination = new RandomAccessFile(destFile,"rw").getChannel();

		long position = 0;
		long count    = source.size();

		source.transferTo(position, count, destination);
		}
		finally {
		if(source != null) {
		source.close();
		}
		if(destination != null) {
		   destination.close();
		}
    }
 }
}