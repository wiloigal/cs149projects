import java.util.*;

public class ShortestRemainingTime {

	private PriorityQueue<Job> waitingJobs;//sort by runtime
	private PriorityQueue<Job> workingJobs;//sort by runtime
	private Job jobBeingWorked;

	int timer = 0;



	static Comparator<Job> compareByArrivalTime = new Comparator<Job>(){
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
	private static Comparator<Job> compareByRemainingTime = new Comparator<Job>(){
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


	/////////Constructors//////////////////////////////////

	public ShortestRemainingTime(){                     
		waitingJobs = new PriorityQueue<>(compareByRemainingTime);
		workingJobs = new PriorityQueue<Job>(compareByRemainingTime);
	}



	private Queue<Job> doJobs(PriorityQueue<Job> queue){
		ArrayList<Job> dummyQ = new ArrayList<>(queue);
		Queue<Job> newQueue = new LinkedList<Job>();//new queue
		//we already have the queue sorted by arrival time
		//need a queue sorted by time remaining
		PriorityQueue<Job> jobsWorking = new PriorityQueue<Job>(compareByRemainingTime);

		//work on job until timer reaches "queue" next arrival time

		timer = (int)queue.peek().expectedRunTime;
		Job jobFinishing = jobsWorking.peek();
		while(timer <= 100){
			if(queue.peek() != null &&(int)queue.peek().arrivalTime <= timer){
				jobsWorking.add(queue.poll());
			}
			if(jobsWorking.size() != 0){
				//testing
				System.out.println(jobsWorking.peek().arrivalTime + "----- " + timer);
				//
				jobsWorking.peek().doJob();
				jobFinishing = jobsWorking.peek();
				if(jobsWorking.peek().isComplete()){
					dummyQ.remove(jobFinishing);
					newQueue.add(jobsWorking.poll());
				}
			}

			timer++;
		}

		return newQueue;

	}

	public static void main(String[] args){
		Random r = new Random();
		PriorityQueue<Job> arg = Job.JOBS();
		ShortestRemainingTime str = new ShortestRemainingTime();
		Queue<Job> q = str.doJobs(arg);

		for(Job j : q)
			System.out.println(j.getTimeLeft() + " " + j.arrivalTime);
		//rand.nextInt((max - min) + 1) + min;
	}

}