import org.quartz.*;
import org.quartz.spi.*;
import org.quartz.impl.StdSchedulerFactory;
import java.io.File;

public class TaskScheduler{

	public TaskScheduler() throws Exception
	{
        //org.apache.log4j.Logger log= org.apache.log4j.Logger.getLogger(CronTest.class);
		Cron cron = new Cron(new StdSchedulerFactory());
		Job backupjob = new Job() {
			public void execute(JobExecutionContext context) {
				Db_BackUp.backup(); //backupdatabase
			}
		};

		//export
		Job exportjob = new Job() {
					public void execute(JobExecutionContext context) {
						AutomatedExportExcel.export(); //export excel
						AutomatedSendMail.send("Miguel.Gervasi@gmail.com",AutomatedExportExcel.filename); //send excel
					}
		};

		//cron.schedule(job, "*/5 * * * * ?");
		cron.schedule(backupjob, "0 30 21 ? * MON-FRI"); //back up database everyday at 9:30 PM
		cron.schedule(exportjob, "0 00 22 ? * 4"); //export excelfile and send to myemail everyday Thursday at 10:00 PM

		cron.start();
		System.in.read();
		cron.shutdown();
	}

}

/*

              field          allowed values
              -----          --------------
              minute         0-59
              hour           0-23
              day of month   1-31
              month          1-12 (or names, see below)
              day of week    0-7 (0 or 7 is Sun, or use names)

              */