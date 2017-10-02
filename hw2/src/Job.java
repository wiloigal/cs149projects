import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

public class Job{
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
    
	public Job(float arrivalTime, float expectedRunTime, int priority, String name){
		this.arrivalTime = arrivalTime;
		this.expectedRunTime = expectedRunTime;
		this.priority = priority;
		this.name = name;
		this.timeLeft = expectedRunTime;
	}

	public static Comparator<Job> compareByArrivalTime = new Comparator<Job>(){
		@Override
		public int compare(Job o1, Job o2) {
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
	public static Comparator<Job> compareByRemainingTime = new Comparator<Job>(){
		@Override
		public int compare(Job o1, Job o2) {
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

	public static PriorityQueue<Job> JOBS(){
		PriorityQueue<Job> jobs = new PriorityQueue<>(compareByArrivalTime);
		Random r = new Random();
		
		for(int i = 0 ; i < 30; i++){
			Job single_job = new Job(0 + r.nextFloat()*(100 - 0),
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
	
	
	/*
	public static ArrayList<Job> JOBS(){
		ArrayList<Job> jobPriorityList = new ArrayList<>();
        int[] arrivalArray = new int[100];

        //quanta min and max
        for (int i = 0; i < 99; i++)
        {
            arrivalArray[i] = i + 1;
        }

        int i = 0;
        while (i < 30)//total jobs
        {
            Random randObject = new Random();

            //Returns the name of job from 0 to 99
            int jobNum = randObject.nextInt(100);

            //Returns 0 to 3 then +1 to fit priority needs of 1 - 4
            int priorityNum = randObject.nextInt(4) + 1;

            if (arrivalArray[jobNum] != -1)
            {
                Job single_job = new Job();

                single_job.setJobName(Integer.toString(i));
                single_job.setStartTime(arrivalArray[jobNum]);
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

	}*/

	public void setTurnAroundTime(float f){
		turnaroundTime = f;
	}
	private void setPriority(int priorityNum) {
		// TODO Auto-generated method stub
		priority = priorityNum;
		
	}
	private void setJobName(String string) {
		// TODO Auto-generated method stub
		name = string;
		
	}
	public void doJob(){
		timeLeft -= 1;

	}
	public boolean isComplete(){
		return timeLeft < 0;
	}
	public float getTimeLeft(){
		return timeLeft;
	}
	public float getExpectedRunTime(){
		return expectedRunTime;
	}
	public int getStartTime(){
		return startTime;
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
	public String getName(){
		return name;
	}
	public float getArrivalTime(){
		return arrivalTime;
	}
	public int getPriority(){
		return priority;
	}
	public float getWaitingTime(){
		return waitingTime;
	}
	public float getTurnAroundTime(){
		return turnaroundTime;
	}
	public void incWaitingTime(){
		waitingTime +=1;
	}
	public void setExpectedRunTime(float f){
		expectedRunTime = f;
	}
	public void setTimeLeft(Float f){
		timeLeft = f;
	}
	
}