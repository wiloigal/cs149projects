import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class FirstComeFirstServed {

    private ArrayList<Job> jobs;
    private static ArrayList<Job> completedJobs;

    public static final float expected_total_runtime_min = 0.1f;
    public static final float expected_total_runtime_max = 10.0f;

    public static final int arrival_time_quanta_min = 0;
    public static final int arrival_time_quanta_max = 99;

    public static final int totalJobs = 30;

    private static int timer;

    public FirstComeFirstServed() {
        jobs = createListOfJobs();
        jobs.sort(new SortByArrival());
        completedJobs = new ArrayList<>();
        timer = 0;
    }

    public static void FCFS(ArrayList<Job> jobs) {
        timer = jobs.get(0).getArrivalTime();
        jobs.get(0).setWaitingTime(0);
        for (int i = 0; i < jobs.size(); i++) {
            if (timer < 100 && jobs.get(i).getArrivalTime() < 100) {
                if (timer > jobs.get(i).getArrivalTime()) {
                    jobs.get(i).setWaitingTime(timer - jobs.get(i).getArrivalTime());
                }
                timer += jobs.get(i).getResponseTime();
                jobs.get(i).setTurnAroundTime(timer - jobs.get(i).getArrivalTime());
                completedJobs.add(jobs.get(i));
            }
        }
    }


    public static void main(String[] args) {
        FirstComeFirstServed test = new FirstComeFirstServed();
        test.FCFS(test.jobs);

        float totalResponseTime = 0.0f;
        for (Job j : test.jobs) {
            totalResponseTime += j.getResponseTime();
        }
        printJobData(completedJobs);
        printAggregatedStatistics(completedJobs, totalResponseTime);
    }

    public static void printJobData(ArrayList<Job> some_job_list)
    {
        System.out.println("-----------------------------------------------------------------------------");

        System.out.printf("%-15s%-15s%-15s%-15s%-15s%-15s\n",
                "Job Name", "Arrival", "Priority", "Response Time", "Waiting Time", "Turn Around Time");

        for (Job job : some_job_list)
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

    public static void printAggregatedStatistics(ArrayList<Job> completedJobs, float totalResponseTime)
    {
        float totalWait = 0.0f;
        float totalTurnAround = 0.0f;

        //Aggregate the total wait time and total turn around time for all completed job chucks
        for (Job j : completedJobs)
        {
            totalWait += j.getWaitingTime();
            totalTurnAround += j.getTurnAroundTime();
        }

        //average time a process has been waiting in the job queue
        float avgWait = totalWait / completedJobs.size();

        //average time to execute a particular process
        float avgTurnAround = totalTurnAround / completedJobs.size();

        //average time from when a request was submitted
        float avgResponse = totalResponseTime / completedJobs.size();

        System.out.println("Total Jobs Completed: " + completedJobs.size());
        System.out.println("Average Wait Time: " + avgWait);
        System.out.println("Average Turn Around: " + avgTurnAround);
        System.out.println("Average Response Time: "+ avgResponse);

        System.out.println("-----------------------------------------------------------------------------");
    }

    public static ArrayList<Job> createListOfJobs() {

        ArrayList<Job> randomJobList = new ArrayList<>(); // list of jobs
        int[] arrivalArray = new int[arrival_time_quanta_max]; // to ensure that no 2 jobs will have the same arrival time.

        //populate a temp list of possible arrival times
        // (used for ensuring that no 2 jobs will share the same arrival time)
        for (int i = arrival_time_quanta_min; i < arrival_time_quanta_max; i++) {
            arrivalArray[i] = i + 1;
        }

        int i = 0;
        while (i < totalJobs) {
            Random randObject = new Random();

            //Returns the arrival time of job from 0 to 99 (Random)
            int jobNum = randObject.nextInt(arrival_time_quanta_max);

            //Returns 0 to 3 then +1 to fit priority needs of 1 - 4 (Random)
            int priorityNum = randObject.nextInt(4) + 1;

            // (used for ensuring that no 2 jobs will share the same arrival time)
            if (arrivalArray[jobNum] != -1) {
                Job single_job = new Job();

                single_job.setJobName(Integer.toString(i));
                single_job.setArrivalTime(arrivalArray[jobNum]);
                single_job.setPriority(priorityNum);
                single_job.setResponseTime(randObject.nextFloat()
                        * (expected_total_runtime_max - expected_total_runtime_min)
                        + expected_total_runtime_min);

                randomJobList.add(single_job);
                arrivalArray[jobNum] = -1; // (used for ensuring that no 2 jobs will share the same arrival time)
                randomJobList.get(i).setTurnAroundTime(randomJobList.get(i).getResponseTime());
                i++;
            }
        }

        return randomJobList;
    }

    static class SortByArrival implements Comparator<Job> {
        @Override
        public int compare(Job j1, Job j2) {
            if ((j1.getArrivalTime() - j2.getArrivalTime()) >= 0) {
                return 1;
            } else {
                return -1;
            }
        }

    }
}
