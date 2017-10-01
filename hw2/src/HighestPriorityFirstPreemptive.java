import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class HighestPriorityFirstPreemptive
{

    public static final float expected_total_runtime_min = 0.1f;
    public static final float expected_total_runtime_max = 10.0f;

    public static final int totalJobs = 30;

    public static final int arrival_time_quanta_min = 0;
    public static final int arrival_time_quanta_max = 99;

    /**
     * Main Function
     */
    public static void main(String[] args)
    {
        //Container for the list of completed jobs ordered by priority
        ArrayList<MyJob> completedJobs = new ArrayList<>();

        //The list of the 4 priority queues
        ArrayList<ArrayList<MyJob>> listOfPriorityQueues = new ArrayList<>(4);

        //Initialize priority queues for each possible priority (1 to 4)
        ArrayList<MyJob> jobPriorityList1 = new ArrayList<>();
        ArrayList<MyJob> jobPriorityList2 = new ArrayList<>();
        ArrayList<MyJob> jobPriorityList3 = new ArrayList<>();
        ArrayList<MyJob> jobPriorityList4 = new ArrayList<>();

        //Creates a list of sample jobs (basically this is list of pseudo-random processes)
        ArrayList<MyJob> givenJobList = createListOfJobs();
        
        //Total amount of time for which a was used for processing
        float totalResponseTime = 0.0f;

        //Aggregate total response time (used for calculating statistics later)
        for (MyJob single_job : givenJobList)
        {
            totalResponseTime += single_job.getResponseTime();
        }

        //Sorts the given job list by arrival
        givenJobList.sort(new SortByArrival());

        //Assign jobs to correct queue based on priority
        for (MyJob single_job: givenJobList)
        {
            int single_job_priority = single_job.getPriority();

            switch (single_job_priority)
            {
                case 1:
                    jobPriorityList1.add(single_job);
                    break;
                case 2:
                    jobPriorityList2.add(single_job);
                    break;
                case 3:
                    jobPriorityList3.add(single_job);
                    break;
                case 4:
                    jobPriorityList4.add(single_job);
                    break;
            }
        }

        //Add the four priority queues into a list for easier processing
        listOfPriorityQueues.add(0, jobPriorityList1);
        listOfPriorityQueues.add(1, jobPriorityList2);
        listOfPriorityQueues.add(2, jobPriorityList3);
        listOfPriorityQueues.add(3, jobPriorityList4);

        System.out.println("QUEUE OF JOBS BEFORE EXECUTING SCHEDULING ALGORITHM: (SORTED BY ARRIVAL)");

        printJobData(givenJobList);

        System.out.println("QUEUE OF JOBS AFTER EXECUTING SCHEDULING ALGORITHM");

        schedulingAlgorithm(listOfPriorityQueues, completedJobs);

        printJobData(completedJobs);

        printAggregatedStatistics(completedJobs, totalResponseTime);
    }//End Main Function

    /**
     * This function performs the processing of executing the
     * jobs in the listOfPriorityQueues based on the priority
     *
     * @param listOfPriorityQueues: list of queues, such that each queue contains jobs that share the same priority.
     * @param completedJobs: a list that contains the jobs that have been processed and completed.
     */
    public static void schedulingAlgorithm( ArrayList<ArrayList<MyJob>> listOfPriorityQueues, ArrayList<MyJob> completedJobs)
    {
        float waitTime = 0.0f;

        for (ArrayList<MyJob> jobQueue : listOfPriorityQueues)
        {
            while (!jobQueue.isEmpty())
            {
                for (int i = 0; i < jobQueue.size(); i++)
                {
                    MyJob single_job = jobQueue.get(i);

                    //If processing of job is complete then remove from jobQueue and put in completedJobs
                    if (single_job.getResponseTime() <= 0)
                    {
                        completedJobs.add(single_job);
                        single_job.setTurnAroundTime(single_job.getTurnAroundTime() + single_job.getWaitingTime());
                        jobQueue.remove(i);
                    }

                    single_job.setWaitingTime(waitTime);

                    // If time remaining is <= 1, finish job then remove from jobQueue and put in completedJobs
                    if (single_job.getResponseTime() <= 1)
                    {
                        waitTime += single_job.getResponseTime();
                        single_job.setResponseTime(0);
                        completedJobs.add(single_job);
                        single_job.setTurnAroundTime(single_job.getTurnAroundTime() + single_job.getWaitingTime());
                        jobQueue.remove(i);
                    }
                    else
                    {
                        single_job.setResponseTime(single_job.getResponseTime() - 1);
                        waitTime++;
                    }
                }
            }
        }
    }

    public static void printJobData(ArrayList<MyJob> some_job_list)
    {
        System.out.println("-----------------------------------------------------------------------------");

        System.out.printf("%-15s%-15s%-15s%-15s%-15s%-15s\n",
                "Job Name", "Arrival", "Priority", "Response Time", "Waiting Time", "Turn Around Time");

        for (MyJob job : some_job_list)
        {
            System.out.printf("%-15s%-15s%-15s%-15s%-15s%-15s\n",
                    job.getJobName(),
                    job.getArrivalTime(),
                    job.getPriority(),
                    job.getResponseTime(),
                    job.getWaitingTime(),
                    job.getTurnAroundTime());
        }

        System.out.println("-----------------------------------------------------------------------------");
    }

    public static void printAggregatedStatistics(ArrayList<MyJob> completedJobs, float totalResponseTime)
    {
        float avgWait;
        float avgTurnAround;
        float avgResponse;

        float totalWait = 0.0f;
        float totalTurnAround = 0.0f;

        for (MyJob j : completedJobs)
        {
            totalWait += j.getWaitingTime();
            totalTurnAround += j.getTurnAroundTime();
        }

        //amount of time a process has been waiting in the ready queue
        avgWait = totalWait / totalJobs;

        // amount of time to execute a particular process
        avgTurnAround = totalTurnAround / totalJobs;

        //amount of time from when a request was submitted until the first response is produced
        avgResponse = totalResponseTime / totalJobs;

        System.out.println("Total Jobs Completed: " + completedJobs.size());
        System.out.println("Average Wait Time: " + avgWait);
        System.out.println("Average Turn Around: " + avgTurnAround);
        System.out.println("Average Response Time: "+ avgResponse);

        System.out.println("-----------------------------------------------------------------------------");
    }

    public static ArrayList<MyJob> createListOfJobs()
    {
        ArrayList<MyJob> jobPriorityList = new ArrayList<>();
        int[] arrivalArray = new int[100];

        for (int i = arrival_time_quanta_min; i < arrival_time_quanta_max; i++)
        {
            arrivalArray[i] = i + 1;
        }

        int i = 0;
        while (i < totalJobs)
        {
            Random randObject = new Random();

            //Returns the name of job from 0 to 99
            int jobNum = randObject.nextInt(100);

            //Returns 0 to 3 then +1 to fit priority needs of 1 - 4
            int priorityNum = randObject.nextInt(4) + 1;

            if (arrivalArray[jobNum] != -1)
            {
                MyJob single_job = new MyJob();

                single_job.setJobName(Integer.toString(i));
                single_job.setArrivalTime(arrivalArray[jobNum]);
                single_job.setPriority(priorityNum);
                single_job.setResponseTime(randObject.nextFloat()
                                * (expected_total_runtime_max - expected_total_runtime_min)
                                + expected_total_runtime_min);

                jobPriorityList.add(single_job);
                arrivalArray[jobNum] = -1;
                jobPriorityList.get(i).setTurnAroundTime(jobPriorityList.get(i).getResponseTime());
                i++;
            }
        }

        return jobPriorityList;
    }

}//End Class

class MyJob
{

    private String jobName; // name of job
    private int arrivalTime; // arrival instance
    private float responseTime; // amount of time for which a was used for processing instructions
    private int priority; // can only be 1 to 4

    private float waitingTime; // amount of time a process has been waiting in the queue
    private float turnAroundTime; // amount of time to execute a particular process

    /**
     * Default Constructor
     */
    public MyJob()
    {
        this.jobName = "DEFAULT";
        this.arrivalTime = -1;
        this.responseTime = -1;
        this.priority = -1;
    }

    /**
     * Constructor
     *
     * @param jobName
     * @param arrivalTime
     * @param responseTime
     * @param priority
     */
    public MyJob(String jobName, int arrivalTime, float responseTime, int priority)
    {
        this.jobName = jobName;
        this.arrivalTime = arrivalTime;
        this.responseTime = responseTime;
        this.priority = priority;
    }

    public String getJobName()
    {
        return this.jobName;
    }

    public int getArrivalTime()
    {
        return this.arrivalTime;
    }

    public float getResponseTime()
    {
        return this.responseTime;
    }

    public int getPriority()
    {
        return this.priority;
    }

    public float getWaitingTime()
    {
        return this.waitingTime;
    }

    public float getTurnAroundTime()
    {
        return this.turnAroundTime;
    }

    public void setJobName(String jobName)
    {
        this.jobName = jobName;
    }

    public void setArrivalTime(int arrivalTime)
    {
        this.arrivalTime = arrivalTime;
    }

    public void setResponseTime(float responseTime)
    {
        this.responseTime = responseTime;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    public void setWaitingTime(float waitingTime)
    {
        this.waitingTime = waitingTime;
    }

    public void setTurnAroundTime(float turnAroundTime)
    {
        this.turnAroundTime = turnAroundTime;
    }

}//End Class

class SortByArrival implements Comparator<MyJob>
{
    @Override
    public int compare(MyJob j1, MyJob j2)
    {
        if ((j1.getArrivalTime() - j2.getArrivalTime()) >= 0)
        {
            return 1;
        }
        else
        {
            return -1;
        }
    }

}//End Class
