import org.quartz.*;
import org.quartz.spi.*;
import org.quartz.impl.StdSchedulerFactory;


public class CronTest{

	public CronTest() throws Exception
	{
        //org.apache.log4j.Logger log= org.apache.log4j.Logger.getLogger(CronTest.class);
		Cron cron = new Cron(new StdSchedulerFactory());
		Job job = new Job() {
			public void execute(JobExecutionContext context) {
				Db_BackUp.backup();
				System.out.println("job executed");

			}
		};
		//cron.schedule(job, "*/5 * * * * ?");
		cron.schedule(job, "0 30 21 ? * MON-FRI"); //correct answer

		cron.start();
		System.in.read();
		cron.shutdown();
	}

	public static void main(String[] args) throws Exception{
		new CronTest();
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