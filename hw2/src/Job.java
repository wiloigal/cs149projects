import java.util.PriorityQueue;
import java.util.Random;

public class Job {
    public float arrivalTime;
    public float expectedRunTime;
    public int priority;
    public String name;

    private float timeLeft;
    private float turnAroundTime;
    private float waitingTime;
    private float responseTime;

    private float startTime;
    private float endTime;


    public Job(float arrivalTime, float expectedRunTime, int priority, String name){
        this.arrivalTime = arrivalTime;
        this.expectedRunTime = expectedRunTime;
        this.priority = priority;
        this.name = name;
        this.timeLeft = expectedRunTime;

        turnAroundTime = 0;
        waitingTime = 0;
        responseTime = 0;

        startTime = 0;
        endTime = 0;
    }
    public static PriorityQueue<Job> JOBS(){
        PriorityQueue<Job> jobs = new PriorityQueue<>(ShortestRemainingTime.compareByArrivalTime);
        Random r = new Random();

        char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        //random = min + r.nextFloat() * (max - min)
        for (int i = 0; i < alphabet.length; i++) {
            String letter = Character.toString(alphabet[i]);
            Job j = new Job(0 + r.nextFloat()*(100 - 0),0 + r.nextFloat()*(10-0), r.nextInt((4 - 1) + 1) +1, letter);
            jobs.add(j);
        }

        return jobs;

    }
    public void doJob(){
        timeLeft -= 1;

    }
    public boolean isComplete(){
        return timeLeft <= 0;
    }
    public float getTimeLeft(){
        return timeLeft;
    }
    public float getExpectedRunTime(){
        return expectedRunTime;
    }
    public void setStartTime(int time){
        startTime = time;
    }
    public void setEndTime(int time){
        endTime = time;
    }

}
