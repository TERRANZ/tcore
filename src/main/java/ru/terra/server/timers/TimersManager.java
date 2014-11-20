package ru.terra.server.timers;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.terra.server.config.Config;
import ru.terra.server.constants.ConfigConstants;

/**
 * User: Vadim Korostelev
 * Date: 09.09.13
 * Time: 16:20
 */
public class TimersManager {

    private static TimersManager instance = new TimersManager();
    private Scheduler scheduler;
    private Config config = Config.getConfig();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private TimersManager() {
    }

    public static TimersManager getInstance() {
        return instance;
    }

    public void start() {
        logger.info("Starting timers manager...");
        try {
            SchedulerFactory sf = new StdSchedulerFactory();
            Scheduler sched = sf.getScheduler();
            JobDetail job = JobBuilder.newJob(SessionExpirationJob.class)
                    .withIdentity("SessionExpirationJob", "group1")
                    .build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger1", "group1")
                    .startNow()
                    .withSchedule(
                            SimpleScheduleBuilder.simpleSchedule()
                                    .withIntervalInSeconds(Integer.parseInt(config.getValue(ConfigConstants.SESSION_TTL, ConfigConstants.SESSION_TTL_DEFAULT)))
                                    .repeatForever()
                    )
                    .build();
            sched.scheduleJob(job, trigger);
            sched.start();
        } catch (SchedulerException se) {
            se.printStackTrace();
            logger.error("Error while initializing quartz", se);
        }
        while (true) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
