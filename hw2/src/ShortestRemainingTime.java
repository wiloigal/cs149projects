import java.util.*;

public class ShortestRemainingTime {
	
	private PriorityQueue waitingJobs;//sort by arrival time
	private PriorityQueue<Job> readyJobs;
	private Job jobBeingWorked;
	
	int startTime = 0;
	int finishTime = 0;
	
	
	
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
	
	public ShortestRemainingTime(){                     
		waitingJobs = new PriorityQueue<>(compareByArrivalTime);
		readyJobs = new PriorityQueue<Job>(compareByArrivalTime);
	}
										 
	
	
	private void doJobs(PriorityQueue<Job> queue){
		//add jobs that have been seen by now
		while(!queue.isEmpty() || !readyJobs.isEmpty() ||!waitingJobs.isEmpty()){
			while(!queue.isEmpty() && queue.peek().arrivalTime <= finishTime)
				readyJobs.add(queue.poll());
			
			jobBeingWorked = readyJobs.poll();
			
			startTime = Math.max((int) Math.ceil(jobBeingWorked.arrivalTime), finishTime);
			finishTime = startTime + 1;
		}
	
	}
	
	public static void main(String[] args){
		Random rand = new Random();
		
		ShortestRemainingTime str = new ShortestRemainingTime();
		str.doJobs();
	}
	
}