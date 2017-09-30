
public class Job {
	public float arrivalTime;
	public float expectedRunTime;
	public int priority;
	public String name;
	

	public Job(float arrivalTime, float expectedRunTime, int priority, String name){
		this.arrivalTime = arrivalTime;
		this.expectedRunTime = expectedRunTime;
		this.priority = priority;
		this.name = name;
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
