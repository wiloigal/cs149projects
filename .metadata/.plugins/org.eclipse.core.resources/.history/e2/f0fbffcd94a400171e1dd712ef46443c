
public class Job {
	public float arrivalTime;
	public float expectedRunTime;
	public int priority;
	

	public Job(float arrivalTime, float expectedRunTime, int priority){
		this.arrivalTime = arrivalTime;
		this.expectedRunTime = expectedRunTime;
		this.priority = priority;
	}
	public void doJob(){
		expectedRunTime -= 1;
		
	}
	public boolean isComplete(){
		return expectedRunTime <= 0;
	}

	public float getExpectedRunTime(){
		return expectedRunTime;
	}
	
}
