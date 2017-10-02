import java.util.PriorityQueue;

public class FirstComeFirstServed {

    private PriorityQueue<Job> jobs;
    private int timer;

    public FirstComeFirstServed(PriorityQueue<Job> jobs) {
        this.jobs = jobs;
        this.timer = 0;
    }

    public void FCFS() {
        Job currentJob;

        while (timer < 100) {
            currentJob = jobs.poll();

            //waits for job to arrive
            while (currentJob.getArrivalTime() > timer) {
                timer++;
            }

            int runTime = (int) currentJob.getExpectedRunTime();
            timer += runTime;

        }
    }

    public static void main(String[] args) {
        PriorityQueue<Job> temp = Job.JOBS();
        FirstComeFirstServed trial = new FirstComeFirstServed(temp);
        trial.FCFS();
    }


}
