import java.util.*;

public class ShortestRemainingTime {

	private static final int totalJobs = 30;
	
	//Will help to calculate average runtime
    public float totalResponseTime = 0.0f;
	int timer = 0;
	
	/**
	 * Method gets called to take the queue of jobs in cpu
	 * and increment the waiting time of the jobs waiting
	 * @param q entire jobs seen by cpu that haven't finished
	 */
	private void incrementWaitingJobs(PriorityQueue<JoshJob> q){
		ArrayList<JoshJob> dummyList = new ArrayList<JoshJob>(q);
		dummyList.remove(0);
		for(JoshJob j: dummyList)
			j.incWaitingTime();
	}
	/**
	 * Meat and Bones
	 * 
	 * @param queue jobs sorted by arrival time
	 * @return queue after scheduling algorithm has ran
	 */
	private Queue<JoshJob> processJobs(PriorityQueue<JoshJob> queue){
		
		//Create dummy queue. Sorted by arrivalTime
		//Create empty queue to return
		//Create empty queue that sorts by remaining time**
		PriorityQueue<JoshJob> dummyQ = new PriorityQueue<>(queue);
		Queue<JoshJob> newQueue = new LinkedList<JoshJob>();
		PriorityQueue<JoshJob> jobsWorking = new PriorityQueue<JoshJob>(JoshJob.compareByRemainingTime);
		
		//Sets up the ability to get the average response time
        for (JoshJob single_job : dummyQ)
            totalResponseTime += single_job.getExpectedRunTime();
             
		//timer goes to the time the first job is seen
		timer = (int)queue.peek().getArrivalTime();
		
		//simplification
		JoshJob jobFinishing;
		JoshJob jobAdding;
		
		//loops while timer is under 100 quanta or if a job
		//entered cpu and hasn't finished
		//if there are any jobs seen by the cpu, process it 
		//and increment the waiting time on any other jobs 
		//if the job is complete, set the endtime and move 
		//the job from dummyQ to the queue to return
		
		//if dummyQ has another element and the new element has
		//been seen by cpu
		//remove from dummyQ add to working jobs and set start time
		while(timer  < 100 || jobsWorking.peek() != null){
			if(jobsWorking.size() != 0){
				incrementWaitingJobs(jobsWorking);
				jobsWorking.peek().doJob();
				if(jobsWorking.peek().isComplete()){
					jobFinishing = jobsWorking.poll();
					jobFinishing.setEndTime(timer);
					jobFinishing.setTurnAroundTime(jobFinishing.getTurnAroundTime() + jobFinishing.getWaitingTime());
					dummyQ.remove(jobFinishing);
					newQueue.add(jobFinishing);
				}
			}
			if(dummyQ.peek() != null && (int)dummyQ.peek().getArrivalTime() <= timer){
				jobAdding = dummyQ.poll();
				jobAdding.setStartTime(timer);
				jobsWorking.add(jobAdding);
			}
			timer++;
		};
		
		return newQueue;

	}
	public static void printJobData(Queue<JoshJob> list)
    {
		ArrayList<JoshJob> some_job_list = new ArrayList<JoshJob>(list);
		some_job_list.sort(JoshJob.compareByArrivalTime);
        System.out.println("-----------------------------------------------------------------------------");

        System.out.printf("%-15s%-15s%-15s%-15s%-15s%-15s\n",
                "Job Name", "Arrival", "Priority", "Response Time", "Waiting Time", "Turn Around Time");

        for (JoshJob job : some_job_list)
        {
            System.out.printf("%-15s%-15s%-15s%-15s%-15s%-15s\n",
                    job.getName(),
                    job.getArrivalTime(),
                    job.getPriority(),
                    job.getExpectedRunTime(),
                    job.getWaitingTime(),
                    job.getTurnAroundTime()
                    );
        }

        System.out.println("-----------------------------------------------------------------------------");
    }

    public static void printAggregatedStatistics(Queue<JoshJob> completedJobs, float totalResponseTime)
    {
        float avgWait;
        float avgTurnAround;
        float avgResponse;

        float totalWait = 0.0f;
        float totalTurnAround = 0.0f;

        for (JoshJob j : completedJobs)
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

	public static void main(String[] args){
		
		PriorityQueue<JoshJob> arg = JoshJob.JOBS();
		System.out.println("--------------SHORTEST TIME REMAINING--------------");
		System.out.println("QUEUE OF JOBS BEFORE EXECUTING SCHEDULING ALGORITHM: (SORTED BY ARRIVAL)");

	    printJobData(arg);

	    System.out.println("QUEUE OF JOBS AFTER EXECUTING SCHEDULING ALGORITHM");

		ShortestRemainingTime str = new ShortestRemainingTime();
		
		Queue<JoshJob> q = str.processJobs(arg);
		printJobData(q);
		printAggregatedStatistics(q, str.totalResponseTime);
		
		
		
	}

}
/**
 * This class contains all the information for a job and algorithm statistics
 *
 */
class JoshJob{
	private final float arrivalTime;
	private float expectedRunTime;
	private int priority;
	private String name;

	private float timeLeft = 0;
	private float turnaroundTime = 0;
	private float waitingTime= 0 ;
	
	private int startTime = 0;
	private int endTime = 0;
	
	public static final float expected_total_runtime_min = 0.1f;
    public static final float expected_total_runtime_max = 10.0f;
    
    /**
     * Constructor
     * @param arrivalTime time job enters cpu
     * @param expectedRunTime
     * @param priority
     * @param name job name
     */
	public JoshJob(float arrivalTime, float expectedRunTime, int priority, String name){
		this.arrivalTime = arrivalTime;
		this.expectedRunTime = expectedRunTime;
		this.priority = priority;
		this.name = name;
		this.timeLeft = expectedRunTime;
	}

	public static Comparator<JoshJob> compareByArrivalTime = new Comparator<JoshJob>(){
		@Override
		public int compare(JoshJob o1, JoshJob o2) {
			// TODO Auto-generated method stub

			float oneComparison = o1.arrivalTime;
			float twoComparison = o2.arrivalTime;

			if(oneComparison < twoComparison)
				return -1;
			if(oneComparison > twoComparison)
				return 1;
			return 0;
		}
	};
	public static Comparator<JoshJob> compareByRemainingTime = new Comparator<JoshJob>(){
		@Override
		public int compare(JoshJob o1, JoshJob o2) {
			// TODO Auto-generated method stub
			float oneComparison = o1.getTimeLeft();
			float twoComparison = o2.getTimeLeft();

			if(oneComparison < twoComparison)
				return -1;
			if(oneComparison > twoComparison)
				return 1;
			return 0;
		}

	};
	/**
	 * Randomly generate 30 jobs
	 * @return priority queue of 30 jobs
	 */
	public static PriorityQueue<JoshJob> JOBS(){
		PriorityQueue<JoshJob> jobs = new PriorityQueue<>(compareByArrivalTime);
		Random r = new Random();
		
		for(int i = 0 ; i < 30; i++){
			JoshJob single_job = new JoshJob(0 + r.nextFloat()*(100 - 0),
					         0 + r.nextFloat()*(10-0), 
					         r.nextInt((4 - 1) + 1) +1, 
					         Integer.toString(i));
			jobs.add(single_job);
			single_job.setExpectedRunTime(r.nextFloat()
                    * (expected_total_runtime_max - expected_total_runtime_min)
                    + expected_total_runtime_min);
			single_job.setTurnAroundTime(single_job.getExpectedRunTime());
			single_job.setTimeLeft(single_job.getExpectedRunTime());

		}
		return jobs;	
	}
	/**
	 * sets the turnaround time for the job
	 * @param f the amount to set the turnaround time
	 */
	public void setTurnAroundTime(float f){
		turnaroundTime = f;
	}
	/**
	 * Sets the priority of the job
	 * @param priorityNum priority to set
	 */
	private void setPriority(int priorityNum) {
		// TODO Auto-generated method stub
		priority = priorityNum;
		
	}
	/**
	 * Sets the job name
	 * @param string name to set
	 */
	private void setJobName(String string) {
		// TODO Auto-generated method stub
		name = string;
		
	}
	/**
	 * Subtracts one quanta from the jobs remaining time
	 */
	public void doJob(){
		timeLeft -= 1;

	}
	/**
	 * Job is complete or not
	 * @return if job still has time left to complete
	 */
	public boolean isComplete(){
		return timeLeft < 0;
	}
	/**
	 * The time left in job
	 * @return time left to complete
	 */
	public float getTimeLeft(){
		return timeLeft;
	}
	/**
	 * Amount of quanta to complete job
	 * @return the initial run time
	 */
	public float getExpectedRunTime(){
		return expectedRunTime;
	}
	/**
	 * The time this job enterred the cpu
	 * @return the start time
	 */
	public int getStartTime(){
		return startTime;
	}
	/**
	 * The end time of this job
	 * @return the end time
	 */
	public int getEndTime(){
		return endTime;
	}
	/**
	 * sets the start time of this job
	 * @param time to set as start
	 */
	public void setStartTime(int time){
		startTime = time;
	}
	/**
	 * sets the end time of this job
	 * @param time to time to set as the end
	 */
	public void setEndTime(int time){
		endTime = time;
	}
	/**
	 * the name of this job
	 * @return the name
	 */
	public String getName(){
		return name;
	}
	/**
	 * arrival time of this job
	 * @return the time
	 */
	public float getArrivalTime(){
		return arrivalTime;
	}
	/**
	 * priority number of this job
	 * @return the priority number
	 */
	public int getPriority(){
		return priority;
	}
	/**
	 * Waiting time of this job
	 * @return the total waiting time
	 */
	public float getWaitingTime(){
		return waitingTime;
	}
	/**
	 * Turn around time of this job
	 * @return the turnaround time
	 */
	public float getTurnAroundTime(){
		return turnaroundTime;
	}
	/**
	 * Set the expected runtime of this job
	 * @param f time to set 
	 */
	public void setExpectedRunTime(float f){
		expectedRunTime = f;
	}
	/**
	 * Sets the time left in this job
	 * @param f time to set
	 */
	public void setTimeLeft(Float f){
		timeLeft = f;
	}
	
	/**
	 * add a quanta to this jobs wating time
	 */
	public void incWaitingTime(){
		waitingTime +=1;
	}
}
