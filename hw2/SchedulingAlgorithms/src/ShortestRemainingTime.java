import java.util.*;

public class ShortestRemainingTime {
	
	private ArrayList<Job> jobs;
	private static Comparator<Job> compareByArrivalTime = new Comparator<Job>(){
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
	private static Comparator<Job> compareByExpectedRunTime = new Comparator<Job>(){
		@Override
		public int compare(Job o1, Job o2) {
			// TODO Auto-generated method stub
	
			float oneComparison = o1.expectedRunTime;
			float twoComparison = o2.expectedRunTime;
			
			if(oneComparison < twoComparison)
				return -1;
			if(oneComparison > twoComparison)
				return 1;
			return 0;
		}
		
	};
	/////////Constructors//////////////////////////////////
	public ShortestRemainingTime(){                      //
		jobs = new ArrayList<Job>();					 //
	}													 //
	public ShortestRemainingTime(ArrayList<Job> jobs){   //
		this.jobs = jobs;								 //
	}													 //
	///////////////////////////////////////////////////////
	
	
	private boolean allJobsCompleted(){
		for(Job j: jobs)
			if(!j.isComplete())
				return false;
	
		return true;
	}
	
	private void doJobs(PriorityQueue<Job> queue){
		
		
		Stack<Job> jobsStarted = new Stack<Job>();
		PriorityQueue<Job> readyJobs = new PriorityQueue<Job>(compareByArrivalTime);
		
		
		readyJobs.add(jobs.get(0));
		
		
		for(double f = readyJobs.peek().arrivalTime; f < 100; f+= Double.MIN_VALUE){
			//set job to started if it hasn't started yet and the 
			//new expected run time is less than old expected run time
		
			if(!jobsStarted.contains(readyJobs.peek())){
				jobsStarted.push(readyJobs.peek());
			}
			//do the job set to start
			jobsStarted.peek().doJob();
			
			//if job is complete take it out of started and out of 
			// received and into complete
			if(jobsStarted.peek().isComplete()){
				readyJobs.remove(jobsStarted.peek());
				jobsStarted.pop();
			}
			//if new arrived job has expected 
			//run time less than current
			
			
			
			
				
			
		}

	}
	
	public static void main(String[] args){
		Random rand = new Random();
		StringBuffer sb = new StringBuffer("Josh");

		sb.reverse();
		System.out.println(sb);
		
		ShortestRemainingTime str = new ShortestRemainingTime();
		PriorityQueue<Job> jobsReceived = new PriorityQueue<Job>(compareByArrivalTime);

		for(int i = 0; i < 10; i++){
			jobsReceived.add(new Job(0 + rand.nextFloat()*(99-0), (float)(0.1 + rand.nextFloat()*(10-0.1)),(int)(1 + rand.nextFloat()*(4-1))));
			
			
		}
		for(int i = 0; i < 10; i++)
			System.out.println(jobsReceived.remove().arrivalTime);
		
	}
}