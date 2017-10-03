package hw2;


import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Round robin algorithm
 */
public class RR
{
    private static final float TIME_SLICE = 1;
    
    private static final float MAX_ALLOTED_TIME = 1;
    /**
     * Runs the algorithm on the collection of new processes
     * @param processes a collection of new processes
     * @param targetTime the time at which starved processes are purged
     * @return a string showing statistics about the process' run
     */
    public static String run(Collection<SimProcess> processes, float targetTime)
    {
        ReadyQueue readyQueue = new ReadyQueue();
        LinkedList<SimProcess> waitQueue = new LinkedList<SimProcess>();
        waitQueue.addAll(processes);
        waitQueue.sort(ScheduleHelper.arrivalComp);
        
        float currentTime = 0;
        ArrayList<String> timeline = new ArrayList<String>();
        boolean hasStarved = false;
        while(!readyQueue.isEmpty() || !waitQueue.isEmpty())
        { 
            procSwitch(readyQueue, currentTime);
            hasStarved = checkStarve(processes, readyQueue, waitQueue, currentTime, targetTime, hasStarved);
            if(!readyQueue.isEmpty() || !waitQueue.isEmpty())
            {
                timeline.add(runTimeSlice(readyQueue, waitQueue, currentTime));
                currentTime += TIME_SLICE;
            }
        }
        return ScheduleHelper.formatPriorityOutput(processes, timeline, currentTime);
    }
    
    /**
     * Runs a process and waits for arriving processes for a defined timeslice
     * @param readyQueue the queue of ready processes
     * @param waitQueue the queue of arriving processes
     * @param currentTime the current time thus far
     * @return the name of the process that ran, if any
     */
    private static String runTimeSlice(ReadyQueue readyQueue, LinkedList<SimProcess> waitQueue, float currentTime)
    {
        String ranProcess = "wait";
        if(!readyQueue.isEmpty())
        {
            readyQueue.runCurrentProc(TIME_SLICE, currentTime);
            ranProcess = readyQueue.getCurrentProc().getProcName();
        }
        
        for(SimProcess proc: waitQueue)
        {
            proc.waitForArrival(TIME_SLICE);
            if(proc.getArrivalTime() <= 0)
            {
                readyQueue.add(proc);
                proc.setAllotedTime(MAX_ALLOTED_TIME);
            }
        }
        waitQueue.removeIf((proc) -> proc.getArrivalTime() <= 0);
        
        return ranProcess;
    }
    

    /**
     * A method that removes finished processes and/or performs a process switch,
     * or neither if these actions are unneeded
     * @param readyQueue the queue of ready processes
     * @param currentTime the current time thus far
     */
    private static void procSwitch(ReadyQueue readyQueue, float currentTime)
    {
        //Officially finishing a process should take precedence over switching it out
        if(!readyQueue.isEmpty() && readyQueue.getCurrentProc().getRunTime() <= 0)
        {
            readyQueue.removeCurrentProc(currentTime);
            if(!readyQueue.isEmpty())
                readyQueue.getCurrentProc().setAllotedTime(MAX_ALLOTED_TIME);
        }
        else if(!readyQueue.isEmpty() && readyQueue.getCurrentProc().getAllotedTime() <= 0)
        {
            readyQueue.updateCurrentProc();
            readyQueue.getCurrentProc().setAllotedTime(MAX_ALLOTED_TIME);
        }
        
    }
    
    /**
     * Purges the queues and original collection of starved processes if necessary
     * @param procs the original collection of processes
     * @param readyQueue the queue of ready processes
     * @param waitQueue the queue of arriving processes
     * @param currentTime the current time thus far
     * @param targetTime the time at which to purge starved processes
     * @param hasStarved true if a purge already occurred
     * @return true if a purge occurs, false otherwise
     */
    private static boolean checkStarve(Collection<SimProcess> procs, ReadyQueue readyQueue, LinkedList<SimProcess> waitQueue, float currentTime, float targetTime, boolean hasStarved)
    {
        if(currentTime >= targetTime && !hasStarved)
        {
            readyQueue.evictStarved();
            waitQueue.clear();
            procs.removeIf((proc) -> (proc.getStartTime() < 0));
            hasStarved = true;
        }
        return hasStarved;
    }


public static  class ReadyQueue
{
    private SimProcess currentProc;
    private LinkedList<SimProcess> queue;
    
    /**
     * Initializes a new ReadyQueue
     */
    public ReadyQueue()
    {
        queue = new LinkedList<SimProcess>();
        currentProc = null;
    }
    
    /**
     * Updates the currently running process to be the correct one. If there
     * already is a currently running process, it is moved to the end of the queue
     */
    public void updateCurrentProc()
    {
        if(currentProc != null)
        {
            add(currentProc);
        }
        if(!queue.isEmpty())
            currentProc = queue.removeFirst();
        else
            currentProc = null;
    }
    
    /**
     * Removes the current process and updates its finishing time
     * @param currentTime the time of removal
     * @return the removed process
     */
    public SimProcess removeCurrentProc(float currentTime)
    {
        if(currentProc == null)
            updateCurrentProc();
        SimProcess removedProc = currentProc;
        removedProc.setFinishTime(currentTime);
        currentProc = null;
        updateCurrentProc();
        return removedProc;
    }

    /**
     * Adds a new process to the queue and updates the current process if necessary
     * @param proc the process to be added
     */
    public void add(SimProcess proc)
    {
        queue.addLast(proc);
        if(currentProc == null)
            updateCurrentProc();
    }
    
    /**
     * Sorts the processes in the queue according to the given comparator
     * @param comp the comparator
     */
    public void sort(Comparator<? super SimProcess> comp)
    {
        queue.sort(comp);
    }
    
    /**
     * Returns the currently running process
     * @return the current process
     */
    public SimProcess getCurrentProc()
    {
        if(currentProc == null)
            updateCurrentProc();
        return currentProc;
    }
    
    /**
     * Adds a new process, but as the currently running process
     * @param currentProc the new process
     */
    public void addAsCurrentProc(SimProcess currentProc)
    {
        if(currentProc != null)
            add(this.currentProc);
        this.currentProc = currentProc;
    }
    
    /**
     * Removed starved processes that never even started
     */
    public void evictStarved()
    {
        queue.removeIf((proc) -> (proc.getStartTime() < 0));
        if(currentProc != null && currentProc.getStartTime() < 0)
        {
            currentProc = null;
            updateCurrentProc();
        }  
    }
    
    /**
     * Checks if the queue is empty, including the currentProc variable
     * @return true if there are no processes running or in the queue
     */
    public boolean isEmpty()
    {
        return queue.isEmpty() && currentProc == null;
    }
    
    /**
     * Runs the current process for the given timeslice
     * If the process never ran before, its start time will be updated
     * @param timeSlice the time to run the process
     * @param currentTime the current time
     */
    public void runCurrentProc(float timeSlice, float currentTime)
    {
        if(currentProc == null)
            updateCurrentProc();
        currentProc.run(timeSlice);
        if(currentProc.getStartTime() < 0)
            currentProc.setStartTime(currentTime);
    }
}

public static class SimProcess
{
    private float arrivalTime;
    private final float initArrivalTime;
    private float runTime;
    private final float initRunTime;
    private int priority;
    private final int initPriority;
    private String procName;
    private float startTime;
    private float finishTime;
    private float allotedTime;
    private float age;
    
    public final static float MAX_ARRIVAL_TIME = 99;
    public final static float MIN_ARRIVAL_TIME = 0;
    public final static float MAX_RUN_TIME = 10;
    public final static float MIN_RUN_TIME = 0.1f;
    public final static int MAX_PRIORITY = 4;
    
    public SimProcess(float arrivalTime, float runTime, int priority, String procName)
    {
        this.arrivalTime = arrivalTime;
        initArrivalTime = arrivalTime;
        this.runTime = runTime;
        initRunTime = runTime;
        this.priority = priority;
        initPriority = priority;
        this.procName = procName;
        startTime = -1;
        finishTime = -1;
        allotedTime = 0;
        age = 0;
    }
    
    /**
     * Returns the current time until arrival
     * @return the time until arrival
     */
    public float getArrivalTime()
    {
        return arrivalTime;
    }
    
    /**
     * Returns the current time left until the process completes
     * @return the time until completion
     */
    public float getRunTime()
    {
        return runTime;
    }
    
    /**
     * Returns the current priority of the process
     * @return the priority
     */
    public int getPriority()
    {
        return priority;
    }
    
    /**
     * Updates the arrival time of the process
     * @param timeDec The amount to be decremented from the arrival time, measured in quanta.
     */
    public void waitForArrival(float waitTime)
    {
        if(waitTime > arrivalTime) 
            arrivalTime = 0;
        else 
            arrivalTime -= waitTime;
    }
    
    /**
     * Updates the runtime
     * @param elapsedTime the timeslice (quanta) in which to run the process and then switch to a new one
     */
    public void run(float elapsedTime)
    {
        if(elapsedTime > runTime)
            runTime = 0;
        else 
            runTime -= elapsedTime;
        decAllotedTime(elapsedTime);
    }
    
    /**
     * Sets the priority of the thread
     * @param priority the new priority
     */
    public void setPriority(int priority)
    {
        this.priority = priority;
    }
    
    /**
     * Lowers the priority value of the process
     */
    public void decPriority()
    {
        if(priority > 1)
        {
            priority--;
        }
    }
    
    /**
     * Gets the name of the process
     * @return the name
     */
    public String getProcName()
    {
        return procName;
    }
    
    /**
     * Gets the start time of the process
     * @return start time
     */
    public float getStartTime()
    {
        return startTime;
    }
    
    /**
     * Sets the start time of the process
     * @param startTime the new start time
     */
    public void setStartTime(float startTime)
    {
        this.startTime = startTime;
    }
    
    /**
     * Gets the finishing time of the process
     * @return the finishing time
     */
    public float getFinishTime()
    {
        return finishTime;
    }
    
    /**
     * Sets the finishing time of the process
     * @param finishTime the new finishing time
     */
    public void setFinishTime(float finishTime)
    {
        this.finishTime = finishTime;
    }
    
    /**
     * Returns the initial arrival time, expected run time, and priority of the process 
     * @return a String with the initial information of the process.
     */
    public String initInfo()
    {
        String info = "Process Name: " + procName;
        info += "\nArrival time: " + initArrivalTime;
        info += "\nEstimated runtime: " + initRunTime;
        info += "\nPriority: " + initPriority;
        info += "\n AverageTurnaround: ";
        return info;
    }
    
    /**
     * Returns the initial arrival time of the process
     * @return the initial arrival time
     */
    public float getInitArrivalTime()
    {
        return initArrivalTime;
    }
    
    /**
     * Returns the initial expected run time
     * @return the initial expected run time
     */
    public float getInitRunTime()
    {
        return initRunTime;
    }
    
    /**
     * Returns the initial priority
     * @return the initial priority
     */
    public int getInitPriority()
    {
        return initPriority;
    }
    
    /**
     * Generates a process with random arrival time, run time, and priority.
     * @param procName the name of the process
     * @return the new process
     */
    public static SimProcess GenRandProcess(String procName)
    {
        Random ran = new Random();
        float arrivalTime = MIN_ARRIVAL_TIME + (MAX_ARRIVAL_TIME - MIN_ARRIVAL_TIME) * ran.nextFloat();          
        float runTime = MIN_RUN_TIME + (MAX_RUN_TIME - MIN_RUN_TIME) * ran.nextFloat();
        int priority = 1 + ran.nextInt(MAX_PRIORITY);
        return new SimProcess(arrivalTime, runTime, priority, procName);
    }
    
    /**
     * Returns several randomly generated processes
     * @param num the number of desired processes
     * @return an ArrayList containing the processes
     */
    public static ArrayList<SimProcess> GenMultiple(int num)
    {
        ArrayList<SimProcess> procs = new ArrayList<SimProcess>();
        for(int i = 1; i <= num; i++)
        {
            procs.add(GenRandProcess("P" + i));
        }
        return procs;
    }
    
    /**
     * Sets the alloted time for the process to run until switching occurs
     * @param allotedTime
     */
    public void setAllotedTime(float allotedTime)
    {
        this.allotedTime = allotedTime;
    }
    
    /**
     * Gets the alloted time for the process to run until switching occurs
     * @return alloted time
     */
    public float getAllotedTime()
    {
        return allotedTime;
    }
    
    /**
     * Decrements the alloted time based on the time that passed
     * @param timeSlice the elapsed time
     */
    public void decAllotedTime(float timeSlice)
    {
        if(timeSlice > allotedTime) 
            allotedTime = 0;
        else
            allotedTime -= timeSlice;
    }
    
    /**
     * Sets the age of the process
     * @param age the new age
     */
    public void setAge(float age)
    {
        this.age = age;
    }
    
    /**
     * Gets the current age of the process
     * @return the current age
     */
    public float getAge()
    {
        return age;
    }
    
    /**
     * Increases the age of the process by the given timeslice
     * @param timeSlice the time elapsed
     */
    public void incAge(float timeSlice)
    {
        age += timeSlice;
    }
}

public static class ScheduleHelper
{
    public static final Comparator<SimProcess> runComp = (p1, p2) -> Float.compare(p1.getRunTime(), p2.getRunTime());
    public static final Comparator<SimProcess> arrivalComp = (p1, p2) -> Float.compare(p1.getArrivalTime(), p2.getArrivalTime());
    public static final String NL = System.getProperty("line.separator");
    
    /**
     * Formats output showing the timeline of processes, the avg turnaround time, 
     * the avg waiting time, the avg response time, and the throughput
     * @param procs The collection of processes
     * @param timeline An ArrayList showing when each process ran
     * @param time The time of completion of the algorithm
     * @return A String with the information in the description
     */
    public static String formatOutput(Collection<SimProcess> procs, ArrayList<String> timeline, float time)
    {
        StringBuilder output = new StringBuilder(); //Since we're appending a lot of data in loops, StringBuilder is preferred to normal String
        procs.forEach((proc) -> {output.append(proc.initInfo() + NL + NL);});
        //This is in case we're collecting statistics on only a subset of completed processes
        if(timeline != null)
        {
            output.append(NL);
            output.append(formatTimeline(timeline));
        }
        
        float totalFinishTime = 0;
        float totalWaitTime = 0;
        float totalResponseTime = 0;
        

        for(SimProcess proc: procs)
        {
            totalFinishTime += proc.getFinishTime();
            totalWaitTime += proc.getFinishTime() - proc.getInitRunTime() - proc.getInitArrivalTime();
            totalResponseTime += proc.getStartTime();
        }
        float avgTurnaround = 0;
        float avgWait = 0;
        float avgResponse = 0;
        if(procs.size() > 0)
        {
            avgTurnaround = totalFinishTime / procs.size();
            
            avgWait = totalWaitTime / procs.size();
            avgResponse = totalResponseTime / procs.size();
        }
        output.append(NL + "Average turnaround time: " + avgTurnaround);
        output.append(NL + "Average wait time: " + avgWait);
        output.append(NL + "Average response time: " + avgResponse);
        
        float throughput = 0;
        if(time > 0)
            throughput = procs.size() / time; 
        output.append(NL + "Throughput: " + throughput + " processes per quantum");
        return output.toString();
    }
    
    /**
     * This method formats statistical output for each priority level present in the collection of processes.
     * At the very end, it includes output for the whole collection like normal.
     * @param procs The collection of processes
     * @param timeline An ArrayList showing when each process ran
     * @param time The time of completion of the algorithm
     * @return a String with information about each individual priority level and all completed processes as a whole.
     */
    public static String formatPriorityOutput(Collection<SimProcess> procs, ArrayList<String> timeline, float time)
    {
        ArrayList<LinkedList<SimProcess>> queues = new ArrayList<LinkedList<SimProcess>>();
        for(int i = 0; i < SimProcess.MAX_PRIORITY; i++)
        {
            queues.add(new LinkedList<SimProcess>());
        }
        for(SimProcess proc: procs)
        {
            //I'm separating this based on original priority because of aging
            queues.get(proc.getInitPriority() - 1).addLast(proc);
        }
        
        StringBuilder output = new StringBuilder();
        for(int i = 0; i < SimProcess.MAX_PRIORITY; i++)
        {
            LinkedList<SimProcess> queue = queues.get(i);
            output.append("Statistics for priority " + (i + 1) + NL);
            output.append(formatOutput(queue, null, time) + NL + NL);
        }
        
        output.append(NL + NL + "Overall statistics");
        output.append(NL + formatOutput(procs, timeline, time));
        
        return output.toString();
    }
    
    private static String formatTimeline(ArrayList<String> timeline)
    {
        StringBuilder timeChart = new StringBuilder();
        for(int i = 0; i < timeline.size(); i++)
        {
            String currentQuant = String.format("Q%03d: ", i);
            String currentProc = String.format("%4s", timeline.get(i));
            timeChart.append("|" + currentQuant + currentProc + "|");
            
            if((i + 1) % 10 == 0)
                timeChart.append(NL);
        }
        return timeChart.toString();
    }
    
     
}

private static void testRR() throws FileNotFoundException
{
	final int MAX_ALG_ITER = 6;
	final String NL = System.getProperty("line.separator");
	final int MAX_PROCS_RR = 40;
	final float TARGET_TIME = 100;
    try {
    	PrintWriter out = new PrintWriter("RR_OUT.txt");
        out.println("**********************************************");
        out.println("Running RR " + MAX_ALG_ITER + " times.");
        out.println("**********************************************" + NL + NL);
        for(int i = 1; i <= MAX_ALG_ITER; i++)
        {
            out.println("RR run #" + i);
            out.println(RR.run(SimProcess.GenMultiple(MAX_PROCS_RR), TARGET_TIME) + NL + NL);
        }
    }
    catch (Exception e){
    	System.out.println("error");
    } 
}

public static void main(String[] args) throws Exception
{
	testRR();
    System.out.println("Output printed to different file");
	}

}


