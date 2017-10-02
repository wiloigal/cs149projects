import java.util.PriorityQueue;
import java.util.Random;

public class Job {
	public float arrivalTime;
	public float expectedRunTime;
	public int priority;
	public String name;

	private float timeLeft;
	private int startTime = 0;
	private int endTime = 0;


	public Job(float arrivalTime, float expectedRunTime, int priority, String name){
		this.arrivalTime = arrivalTime;
		this.expectedRunTime = expectedRunTime;
		this.priority = priority;
		this.name = name;
		this.timeLeft = expectedRunTime;
	}

	public static PriorityQueue<Job> JOBS(){
		PriorityQueue<Job> jobs = new PriorityQueue<>(ShortestRemainingTime.compareByArrivalTime);
		Random r = new Random();
		
		//random = min + r.nextFloat() * (max - min)
		jobs.add(new Job(0 + r.nextFloat()*(100 - 0),0 + r.nextFloat()*(10-0), r.nextInt((4 - 1) + 1) +1, "A"));
		jobs.add(new Job(0 + r.nextFloat()*(100 - 0),0 + r.nextFloat()*(10-0), r.nextInt((4 - 1) + 1) +1, "B"));
		jobs.add(new Job(0 + r.nextFloat()*(100 - 0),0 + r.nextFloat()*(10-0), r.nextInt((4 - 1) + 1) +1, "C"));
		jobs.add(new Job(0 + r.nextFloat()*(100 - 0),0 + r.nextFloat()*(10-0), r.nextInt((4 - 1) + 1) +1, "D"));
		jobs.add(new Job(0 + r.nextFloat()*(100 - 0),0 + r.nextFloat()*(10-0), r.nextInt((4 - 1) + 1) +1, "E"));
		jobs.add(new Job(0 + r.nextFloat()*(100 - 0),0 + r.nextFloat()*(10-0), r.nextInt((4 - 1) + 1) +1, "F"));
		jobs.add(new Job(0 + r.nextFloat()*(100 - 0),0 + r.nextFloat()*(10-0), r.nextInt((4 - 1) + 1) +1, "G"));
		//jobs.add(new Job(0 + r.nextFloat()*(100 - 0),0 + r.nextFloat()*(10-0), r.nextInt((4 - 1) + 1) +1, "H"));
		
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
	public int getEndTime(){
		return endTime;
	}
	public void setStartTime(int time){
		startTime = time;
	}
	public void setEndTime(int time){
		endTime = time;
	}
}