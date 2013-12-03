import org.quartz.*;
import org.quartz.spi.*;
import java.text.ParseException;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;


public class Cron {

    private static class JobRegistry implements JobFactory {

        private final Map<String, Job> jobByName = new HashMap<String, Job>();

        public void register(String name, Job job) {
            jobByName.put(name, job);
        }

        @Override
        public Job newJob(TriggerFiredBundle bundle) throws SchedulerException {
            return jobByName.get(bundle.getJobDetail().getName());
        }
    }

    private final Scheduler quartz;
    private final JobRegistry jobFactory = new JobRegistry();
    private final AtomicInteger sequence = new AtomicInteger();

    public Cron(SchedulerFactory schedulerFactory) throws SchedulerException {
        quartz = schedulerFactory.getScheduler();
        quartz.setJobFactory(jobFactory);
    }

    public void schedule(Job job, String expression) throws ParseException, SchedulerException {
        int id = sequence.getAndIncrement();
        jobFactory.register("job" + id, job);
        JobDetail detail = new JobDetail("job" + id, job.getClass());
        CronTrigger trigger = new CronTrigger("trigger" + id, null, expression);
        quartz.scheduleJob(detail, trigger);
    }

    public void start() throws SchedulerException {
        quartz.start();
    }

    public void shutdown() throws SchedulerException {
        quartz.shutdown();
    }
}