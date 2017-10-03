import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

public class ShortestJobFirst
{

    public static void main(String[] args) {
        //Arraylists created to store jobs waiting to be completed and completed jobs, for loops adds jobs to arraylist
        ArrayList<MarkJob> jobs = new ArrayList<MarkJob>();
        ArrayList<MarkJob> completedJobs = new ArrayList<MarkJob>();
        for (int i =0; i < 26; i++) {
            MarkJob b = new MarkJob();
            b.name = i;

            jobs.add(b);
        }

        //Sorts the pending jobs arraylist by arrival time, if arrival time the same, sorts by run time
        Collections.sort(jobs, new Comparator<MarkJob>()
        {
            @Override
            public int compare(MarkJob o1, MarkJob o2)
            {
                if (o1.arrivalTime < o2.arrivalTime) {
                    return -1;
                }
                if (o1.arrivalTime > o2.arrivalTime) {
                    return 1;
                }
                if (o1.runTime < o2.runTime) {
                    return -1;
                }
                if (o1.runTime > o2.runTime) {
                    return 1;
                }
                return 0;
            }
        });

        float currentQuanta = 0;

        for (int i =0; i<jobs.size();) {
            //If this is the first job to arrive, take the first job in the jobs arraylist
            if (completedJobs.size() == 0) {
                MarkJob current = jobs.get(0);
                if (current.arrivalTime == jobs.get(1).arrivalTime) {
                    if (current.runTime > jobs.get(1).runTime) {
                        current = jobs.get(1);
                    }
                }
                currentQuanta = current.arrivalTime+current.runTime;
                current.startTime = current.arrivalTime;
                current.quantaWhenJobFinished = currentQuanta;
                current.turnaroundTime = current.quantaWhenJobFinished - current.arrivalTime;
                current.waitingTime = current.startTime - current.arrivalTime;

                //Job gets added to the arraylist with completed jobs and removed from the arraylist with the remaining jobs
                completedJobs.add(current);
                jobs.remove(current);
            } else {
                //Adds jobs that have arrived and are ready to be run
                ArrayList<MarkJob> validJobs = new ArrayList<>();
                for (int j = 0; j<jobs.size(); j++) {
                    MarkJob current = jobs.get(j);
                    if (currentQuanta >= current.arrivalTime) {
                        validJobs.add(current);
                    } else {
                        break;
                    }
                }
                MarkJob job;
                //If jobs are waiting to be ran, the one with the least runtime is selected and ran
                if (validJobs.size() > 0) {
                    job = validJobs.get(0);
                    for (int j = 1; j<validJobs.size(); j++) {
                        if (validJobs.get(j).runTime < job.runTime) {
                            job = validJobs.get(j);
                        }
                    }
                    job.startTime = currentQuanta;
                    currentQuanta+= job.runTime;
                    job.quantaWhenJobFinished = currentQuanta;
                    job.turnaroundTime = job.quantaWhenJobFinished - job.arrivalTime;
                    job.waitingTime = job.startTime - job.arrivalTime;


                    completedJobs.add(job);
                    jobs.remove(job);
                } else {
                    //If no jobs are waiting to be ran, the one with the next arrival time is selected and ran
                    job = jobs.get(0);
                    job.startTime = job.arrivalTime;
                    currentQuanta = job.arrivalTime + job.runTime;
                    job.quantaWhenJobFinished = currentQuanta;
                    job.turnaroundTime = job.quantaWhenJobFinished - job.arrivalTime;
                    job.waitingTime = job.startTime - job.arrivalTime;


                    completedJobs.add(job);
                    jobs.remove(job);
                }
            }

        }

        float totalTurnAround = 0;
        float totalWaitingTime = 0;
        float totalResponseTime = 0;

        System.out.printf("%-15s%-15s%-15s%-15s%-15s%-15s%-15s\n",
                "Job Name", "Arrival", "Priority", "Response Time", "Waiting Time", "Run Time", "Turn Around Time");
        for (int i = 0; i < completedJobs.size(); i++) {
            MarkJob job = completedJobs.get(i);
            totalTurnAround += job.turnaroundTime;
            totalWaitingTime += job.waitingTime;
            totalResponseTime += job.responseTime;
            System.out.printf("%-15s%-15s%-15s%-15s%-15s%-15s%-15s\n",
                    job.name,
                    job.arrivalTime,
                    job.priority,
                    job.responseTime,
                    job.waitingTime,
                    job.runTime,
                    job.turnaroundTime
                    );
        }
        System.out.println("-----------------------------------------------------------------------------");
        System.out.println("Total Jobs Completed: " + completedJobs.size());
        System.out.println("Average Wait Time: " + (totalWaitingTime/ completedJobs.size()));
        System.out.println("Average Turn Around: " + (totalTurnAround / completedJobs.size()));
        System.out.println("Average Response Time: " + (totalResponseTime / completedJobs.size()));
        System.out.println("-----------------------------------------------------------------------------");

    }


}

class MarkJob {
    Random r = new Random();

    int name;
    float arrivalTime;
    float runTime;
    int priority;

    float startTime;
    float quantaWhenJobFinished;


    float turnaroundTime;
    float waitingTime;
    float responseTime;

    //Constructor for a job
    public MarkJob() {
        //Random numbers for arrival time, run time and priority and response time
        float randomArrival = r.nextInt(99)+1;
        float randomRunTime = (float)(r.nextFloat() * 9.9 + .1);
        int randomPriority = r.nextInt(4)+1;

        responseTime = (float)(r.nextFloat() * 9.9 + .1);


        this.arrivalTime = randomArrival;
        this.runTime = randomRunTime;
        this.priority = randomPriority;
    }
}