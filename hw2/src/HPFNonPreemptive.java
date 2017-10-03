// Title: High priority first(Nonprremptive) algorithm implmentation
// Programer: Haoyang Liu
// Date: 10/01/2017

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class HPFNonPreemptive 
{
	private static char [] AlphaBetTable = {'A','B','C','D','E','F','G',               
			                                'H','I','J','K','L','M','N',
			                                'O','P','Q','R','S','T',
			                                'U','V','W','X','Y','Z'};  // for assign-name use;
	
	private static ArrayList<Integer> arrivalTimeInitializer = new ArrayList<Integer>();  
	private static Random myRandGen = new java.util.Random(0);
	
	private ArrayList<String> timeChart = new ArrayList<String>();
	private process[] processQueue;
	private int quantumTimer = 0;
	
	private ArrayList<process> _1stPriorityQueue = new ArrayList<process>();
	private ArrayList<process> _2ndPriorityQueue = new ArrayList<process>();
	private ArrayList<process> _3rdPriorityQueue = new ArrayList<process>();
	private ArrayList<process> _4thPriorityQueue = new ArrayList<process>();
	
	
	
	public HPFNonPreemptive()
	{
		for(int i = 0;i<100;i++) // assign 0,1,2,3...98,99 to initializer
		{
			arrivalTimeInitializer.add(i);
		}
		Collections.shuffle(arrivalTimeInitializer,myRandGen); // shuffle 0-99 using a randomness seed.
	}


	public void generateProcesses(int processCount) 
	{
		processQueue = new process[processCount];
		for(int i = 0;i<processCount;i++) // create and initialize each process
		{
			// create name for each process
			char [] tempName = {AlphaBetTable[myRandGen.nextInt(26)],
					        AlphaBetTable[myRandGen.nextInt(26)]};
			// randomize runTime(0.1-10) for each process
			float tempRunTime = (float)((myRandGen.nextInt((int)((10-0.1)*10+1))+0.1*10)/10.0);
			
			// randomize priority (1-4) for each process
			int tempPriority = myRandGen.nextInt(4)+1;
			processQueue[i] = new process(tempName,arrivalTimeInitializer.get(i),tempRunTime,tempPriority,0,0);
			
		}
		sortTheQueue();
		groupByPriority();
	}
	
	public void  sortTheQueue() // sort the queue by arrivalTime(using Insertion Sort Algorithm)
	{
        process temp;
        for (int i = 1; i < processQueue.length; i++) 
        {
            for(int j = i ; j > 0 ; j--)
            {
                if(processQueue[j].ArrivalTime() < processQueue[j-1].ArrivalTime())
                {
                    temp = processQueue[j];
                    processQueue[j] = processQueue[j-1];
                    processQueue[j-1] = temp;
                }
            }
        }
        
    }
	
	public void HPFScheduling()
	{
		quantumTimer = 0;
		while(!allFinished()) // as long as there's unfinished jobs
		{
			processJobsIn1stQueue(); //do jobs, if any, in 1st queue 
			// returned, which means currently there's no jobs need to be processed in 1st queue
			if(!_2ndFinishedByTime(quantumTimer)) // if there's any job needs to be processed in 2nd queue
			{
				processJobsIn2ndQueue(); // do jobs, if any in 2nd queue, once we finish one job in 2nd queue, check if there's new arrived job in 1st queue,
			}
			else if(!_3rdFinishedByTime(quantumTimer)) // else if there's any job needs to be processed in 3rd queue
			{
				processJobsIn3rdQueue();
			}
			else if(!_4thFinishedByTime(quantumTimer)) // else if there's any job needs to be processed in 3rd queue
			{
				processJobsIn4thQueue();
			}
			else if(allFinished())   // else if it's all finished.
			{
				return;
			}
			else // else(which means no new jobs arrived, and cpu is idel)
			{
				cpuIdle();
			}
			
		}

	}
	
	public void processJobsIn1stQueue()
	{
		while(!_1stFinishedByTime(quantumTimer)) // as long as there's unfinished job in 1st queue by this time
		{
			
			for(int i = 0;i<_1stPriorityQueue.size();i++) // get every unfinished job in 1st queue by this time
			{
				if(_1stPriorityQueue.get(i).ArrivalTime() <= quantumTimer &&  // if the process has arrived
			       !_1stPriorityQueue.get(i).IsJobDone())                     // and it's not processed yet
				{
					_1stPriorityQueue.get(i).markJobDone(); // mark that job is done(since there's no pause for nonpreemptive)
					// calculate and set turnaround time
					_1stPriorityQueue.get(i).setTurnaroundTime(quantumTimer+_1stPriorityQueue.get(i).Runtime()-_1stPriorityQueue.get(i).ArrivalTime());
					// calculate and set response time
					_1stPriorityQueue.get(i).setResponseTime(quantumTimer-_1stPriorityQueue.get(i).ArrivalTime());
					for(int j = 0;j<Math.ceil(_1stPriorityQueue.get(i).Runtime());j++) // push several times of queue's name into time chart
					{
						timeChart.add("| "+_1stPriorityQueue.get(i).Name()+" ");
					}
					quantumTimer += Math.ceil(_1stPriorityQueue.get(i).Runtime()); // update the quantum timer
					
				}
			}
		}
	}
	
	public void processJobsIn2ndQueue()
	{
			for(int i = 0;i<_2ndPriorityQueue.size();i++) // for each jobs in 2nd queue by this time
			{
				if(_2ndPriorityQueue.get(i).ArrivalTime()<=quantumTimer &&
					!_2ndPriorityQueue.get(i).IsJobDone())
				{
					_2ndPriorityQueue.get(i).markJobDone(); // mark that job is done(since there's no pause for nonpreemptive)
					// calculate and set turnaround time
					_2ndPriorityQueue.get(i).setTurnaroundTime(quantumTimer+_2ndPriorityQueue.get(i).Runtime()-_2ndPriorityQueue.get(i).ArrivalTime());
					// calculate and set response time
					_2ndPriorityQueue.get(i).setResponseTime(quantumTimer-_2ndPriorityQueue.get(i).ArrivalTime());
					for(int j = 0;j<Math.ceil(_2ndPriorityQueue.get(i).Runtime());j++) // push several times of queue's name into time chart
					{
						timeChart.add("| "+_2ndPriorityQueue.get(i).Name()+" ");
					}
					quantumTimer += Math.ceil(_2ndPriorityQueue.get(i).Runtime()); // update the quantum timer
					
					// Everytime we finish one job from 2nd queue, check if there's new arrived 1st queue job!!!!
					processJobsIn1stQueue();
				}
			}
		
	}
	
	public void processJobsIn3rdQueue()
	{
		for(int i = 0;i<_3rdPriorityQueue.size();i++) // for each jobs in 3rd queue by this time
		{
			if(_3rdPriorityQueue.get(i).ArrivalTime()<=quantumTimer &&
				!_3rdPriorityQueue.get(i).IsJobDone())
			{
				_3rdPriorityQueue.get(i).markJobDone(); // mark that job is done(since there's no pause for nonpreemptive)
				// calculate and set turnaround time
				_3rdPriorityQueue.get(i).setTurnaroundTime(quantumTimer+_3rdPriorityQueue.get(i).Runtime()-_3rdPriorityQueue.get(i).ArrivalTime());
				// calculate and set response time
				_3rdPriorityQueue.get(i).setResponseTime(quantumTimer-_3rdPriorityQueue.get(i).ArrivalTime());
				for(int j = 0;j<Math.ceil(_3rdPriorityQueue.get(i).Runtime());j++) // push several times of queue's name into time chart
				{
					timeChart.add("| "+_3rdPriorityQueue.get(i).Name()+" ");
				}
				quantumTimer += Math.ceil(_3rdPriorityQueue.get(i).Runtime()); // update the quantum timer
				
				// Everytime we finish one job from 3rd queue, check if there's new arrived 1st queue job!!!!
				processJobsIn1stQueue();
				// Then check if there's new arrived 2nd queue job!!!!
				processJobsIn2ndQueue();
			}
		}
	}
	
	public void processJobsIn4thQueue()
	{
		for(int i = 0;i<_4thPriorityQueue.size();i++) // for each jobs in 4th queue by this time
		{
			if(_4thPriorityQueue.get(i).ArrivalTime()<=quantumTimer &&
				!_4thPriorityQueue.get(i).IsJobDone())
			{
				_4thPriorityQueue.get(i).markJobDone(); // mark that job is done(since there's no pause for nonpreemptive)
				// calculate and set turnaround time
				_4thPriorityQueue.get(i).setTurnaroundTime(quantumTimer+_4thPriorityQueue.get(i).Runtime()-_4thPriorityQueue.get(i).ArrivalTime());
				// calculate and set response time
				_4thPriorityQueue.get(i).setResponseTime(quantumTimer-_4thPriorityQueue.get(i).ArrivalTime());
				for(int j = 0;j<Math.ceil(_4thPriorityQueue.get(i).Runtime());j++) // push several times of queue's name into time chart
				{
					timeChart.add("| "+_4thPriorityQueue.get(i).Name()+" ");
				}
				quantumTimer += Math.ceil(_4thPriorityQueue.get(i).Runtime()); // update the quantum timer
				
				// Everytime we finish one job from 3rd queue, check if there's new arrived 1st queue job!!!!
				processJobsIn1stQueue();
				// Then check if there's new arrived 2nd queue job!!!!
				processJobsIn2ndQueue();
				// Then check if there's new arrived 3rd queue job!!!!
				processJobsIn3rdQueue();
			}
		}
	}

	public void cpuIdle()
	{
		timeChart.add("|    "); //blank, which means cpu idle
		quantumTimer++;
	}
	public void printProcessQueueInfo()
	{
		for(int i = 0;i<processQueue.length;i++)
		{
			System.out.printf("Name: %s Arrival time: %-4d", processQueue[i].Name(),processQueue[i].ArrivalTime());
			System.out.println("Expected Runtime: "+processQueue[i].Runtime()+" "
		                    +"priority: "+processQueue[i].Priority()+" ");
		}
	}
	public void groupByPriority()
	{
		for(int i = 0;i<processQueue.length;i++)
		{
			if(processQueue[i].Priority() == 1)
			{
				_1stPriorityQueue.add(processQueue[i]);
			}
			else if(processQueue[i].Priority() == 2)
			{
				_2ndPriorityQueue.add(processQueue[i]);
			}
			else if(processQueue[i].Priority() == 3)
			{
				_3rdPriorityQueue.add(processQueue[i]);
			}
			else if(processQueue[i].Priority() == 4)
			{
				_4thPriorityQueue.add(processQueue[i]);
			}
		}
	}
	
	
	public boolean _1stFinished()
	{
		for(int i = 0;i<_1stPriorityQueue.size();i++)
		{
			if(!_1stPriorityQueue.get(i).IsJobDone())
			{
				return false;
			}
		}
		return true;
	}
	public boolean _2ndFinished()
	{
		for(int i = 0;i<_2ndPriorityQueue.size();i++)
		{
			if(!_2ndPriorityQueue.get(i).IsJobDone())
			{
				return false;
			}
		}
		return true;
	}
	public boolean _3rdFinished()
	{
		for(int i = 0;i<_3rdPriorityQueue.size();i++)
		{
			if(!_3rdPriorityQueue.get(i).IsJobDone())
			{
				return false;
			}
		}
		return true;
	}
	public boolean _4thFinished()
	{
		for(int i = 0;i<_4thPriorityQueue.size();i++)
		{
			if(!_4thPriorityQueue.get(i).IsJobDone())
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean _1stFinishedByTime(int time)
	{
		int index = -1;
		for(int i = 0;i<_1stPriorityQueue.size();i++) //get the index
		{
			if(_1stPriorityQueue.get(i).ArrivalTime()<= time)
			{
				index = i;
			}
		}
		if (index == -1) // if no jobs arrived yet,
		{
			return true; // we consider the queue is all finished by this time
		}
		// here index != -1 which means, there's some arrived jobs in the queue
		
		for(int i = 0;i<=index;i++)
		{
			if(!_1stPriorityQueue.get(i).IsJobDone())
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean _2ndFinishedByTime(int time)
	{
		int index = -1;
		for(int i = 0;i<_2ndPriorityQueue.size();i++) //get the index
		{
			if(_2ndPriorityQueue.get(i).ArrivalTime()<= time)
			{
				index = i;
			}
		}
		if (index == -1) // if no jobs arrived yet,
		{
			return true; // we consider the queue is all finished by this time
		}
		// here index != -1 which means, there's some arrived jobs in the queue
		for(int i = 0;i<=index;i++)
		{
			if(!_2ndPriorityQueue.get(i).IsJobDone())
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean _3rdFinishedByTime(int time)
	{
		int index = -1;
		for(int i = 0;i<_3rdPriorityQueue.size();i++) //get the index
		{
			if(_3rdPriorityQueue.get(i).ArrivalTime()<= time)
			{
				index = i;
			}
		}
		
		if (index == -1) // if no jobs arrived yet,
		{
			return true; // we consider the queue is all finished by this time
		}
		// here index != -1 which means, there's some arrived jobs in the queue
		for(int i = 0;i<=index;i++)
		{
			if(!_3rdPriorityQueue.get(i).IsJobDone())
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean _4thFinishedByTime(int time)
	{
		int index = -1;
		for(int i = 0;i<_4thPriorityQueue.size();i++) //get the index
		{
			if(_4thPriorityQueue.get(i).ArrivalTime()<= time)
			{
				index = i;
			}
		}
		if (index == -1) // if no jobs arrived yet,
		{
			return true; // we consider the queue is all finished by this time
		}
		// here index != -1 which means, there's some arrived jobs in the queue
		for(int i = 0;i<=index;i++)
		{
			if(!_4thPriorityQueue.get(i).IsJobDone())
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean allFinished()
	{
		return this._1stFinished()&&
				this._2ndFinished()&&
				this._3rdFinished()&&
				this._4thFinished();
	}
	
	public void printTimeChart()
	{
		System.out.println("\n");
		for(int i = 0;i<timeChart.size();i++)
		{
			if(i!=0 && i%10 == 0)
			{
				System.out.print("|");
				System.out.println("");
			}
			System.out.print(timeChart.get(i));
		}
	}
	public void calculateAndPrintStatistics()
	{
		float _1stAveTurnaround = 0;
		float _1stAveWaiting = 0;
		float _1stAveResp = 0;
		float _1stThruPut = 0;
		
		float _2ndAveTurnaround = 0;
		float _2ndAveWaiting = 0;
		float _2ndAveResp = 0;
		float _2ndThruPut = 0;
		
		float _3rdAveTurnaround = 0;
		float _3rdAveWaiting = 0;
		float _3rdAveResp = 0;
		float _3rdThruPut = 0;
		
		float _4thAveTurnaround = 0;
		float _4thAveWaiting = 0;
		float _4thAveResp = 0;
		float _4thThruPut = 0;
		
		float AveTurnaround = 0;
		float AveWaiting = 0;
		float AveResp = 0;
		float ThruPut = 0;
		
/*
 * calculate statistics for 1st Priority Queue:
 */
		float _1stSumTA = 0;
		for(int i = 0;i<_1stPriorityQueue.size();i++)
		{
			_1stSumTA += _1stPriorityQueue.get(i).TurnaroundTime();
		}
		_1stAveTurnaround = _1stSumTA/(float)(_1stPriorityQueue.size());
		
		float _1stSumWT = 0;
		for(int i = 0;i<_1stPriorityQueue.size();i++)
		{
			_1stSumWT += _1stPriorityQueue.get(i).WaitTime();
		}
		_1stAveWaiting = _1stSumWT/(float)(_1stPriorityQueue.size());
		
		float _1stSumRP = 0;
		for(int i = 0;i<_1stPriorityQueue.size();i++)
		{
			_1stSumRP += _1stPriorityQueue.get(i).ResponseTime();
		}
		_1stAveResp = _1stSumRP/(float)(_1stPriorityQueue.size());
		
		
		int _1stStart = -1;
		int _1stEnd = -1;
		//Find start
		for(int i = 0;i<timeChart.size();i++)
		{
			String name = timeChart.get(i);
	
			for(int j = 0;j<_1stPriorityQueue.size();j++)
			{
				if(name.equals("| "+_1stPriorityQueue.get(j).Name()+" "))
				{
					_1stStart = i;
					break;
				}
			}
			if(_1stStart != -1)
			{
				break;
			}
		}
		//Find End
		for(int i = timeChart.size()-1;i>=0;i--)
		{
			String name = timeChart.get(i);
			for(int j = 0;j<_1stPriorityQueue.size();j++)
			{
				if(name.equals("| "+_1stPriorityQueue.get(j).Name()+" "))
				{
					_1stEnd = i;
					break;
				}
			}
			if(_1stEnd != -1)
			{
				break;
			}
		}
	
		if(_1stEnd != -1 && _1stStart != -1)
		{
			_1stThruPut = _1stPriorityQueue.size()/(float)(_1stEnd - _1stStart+1);
		}
		/*
		 * calculate statistics for 2nd Priority Queue:
		 */
		float _2ndSumTA = 0;
		for(int i = 0;i<_2ndPriorityQueue.size();i++)
		{
			_2ndSumTA += _2ndPriorityQueue.get(i).TurnaroundTime();
		}
		_2ndAveTurnaround = _2ndSumTA/(float)(_2ndPriorityQueue.size());
		
		float _2ndSumWT = 0;
		for(int i = 0;i<_2ndPriorityQueue.size();i++)
		{
			_2ndSumWT += _2ndPriorityQueue.get(i).WaitTime();
		}
		_2ndAveWaiting = _2ndSumWT/(float)(_2ndPriorityQueue.size());
		
		float _2ndSumRP = 0;
		for(int i = 0;i<_2ndPriorityQueue.size();i++)
		{
			_2ndSumRP += _2ndPriorityQueue.get(i).ResponseTime();
		}
		_2ndAveResp = _2ndSumRP/(float)(_2ndPriorityQueue.size());
		
		
		int _2ndStart = -1;
		int _2ndEnd = -1;
		//Find start
		for(int i = 0;i<timeChart.size();i++)
		{
			String name = timeChart.get(i);
			for(int j = 0;j<_2ndPriorityQueue.size();j++)
			{
				if(name.equals("| "+_2ndPriorityQueue.get(j).Name()+" "))
				{
					_2ndStart = i;
					break;
				}
			}
			if(_2ndStart != -1)
			{
				break;
			}
		}
		//Find End
		for(int i = timeChart.size()-1;i>=0;i--)
		{
			String name = timeChart.get(i);
			for(int j = 0;j<_2ndPriorityQueue.size();j++)
			{
				if(name.equals("| "+_2ndPriorityQueue.get(j).Name()+" "))
				{
					_2ndEnd = i;
					break;
				}
			}
			if(_2ndEnd != -1)
			{
				break;
			}
		}
		if(_2ndEnd != -1 && _2ndStart != -1)
		{
			_2ndThruPut = _2ndPriorityQueue.size()/(float)(_2ndEnd - _2ndStart+1);
		}
		
		/*
		 * calculate statistics for 3rd Priority Queue:
		 */
		
		float _3rdSumTA = 0;
		for(int i = 0;i<_3rdPriorityQueue.size();i++)
		{
			_3rdSumTA += _3rdPriorityQueue.get(i).TurnaroundTime();
		}
		_3rdAveTurnaround = _3rdSumTA/(float)(_3rdPriorityQueue.size());
		
		float _3rdSumWT = 0;
		for(int i = 0;i<_3rdPriorityQueue.size();i++)
		{
			_3rdSumWT += _3rdPriorityQueue.get(i).WaitTime();
		}
		_3rdAveWaiting = _3rdSumWT/(float)(_3rdPriorityQueue.size());
		
		float _3rdSumRP = 0;
		for(int i = 0;i<_3rdPriorityQueue.size();i++)
		{
			_3rdSumRP += _3rdPriorityQueue.get(i).ResponseTime();
		}
		_3rdAveResp = _3rdSumRP/(float)(_3rdPriorityQueue.size());
		
		
		int _3rdStart = -1;
		int _3rdEnd = -1;
		//Find start
		for(int i = 0;i<timeChart.size();i++)
		{
			String name = timeChart.get(i);
			for(int j = 0;j<_3rdPriorityQueue.size();j++)
			{
				if(name.equals("| "+_3rdPriorityQueue.get(j).Name()+" "))
				{
					_3rdStart = i;
					break;
				}
			}
			if(_3rdStart != -1)
			{
				break;
			}
		}
		//Find End
		for(int i = timeChart.size()-1;i>=0;i--)
		{
			String name = timeChart.get(i);
			for(int j = 0;j<_3rdPriorityQueue.size();j++)
			{
				if(name.equals("| "+_3rdPriorityQueue.get(j).Name()+" "))
				{
					_3rdEnd = i;
					break;
				}
			}
			if(_3rdEnd != -1)
			{
				break;
			}
		}
		if(_3rdEnd != -1 && _3rdStart != -1)
		{
			_3rdThruPut = _3rdPriorityQueue.size()/(float)(_3rdEnd - _3rdStart+1);
		}
		
		/*
		 * calculate statistics for 4th Priority Queue:
		 */
		
		float _4thSumTA = 0;
		for(int i = 0;i<_4thPriorityQueue.size();i++)
		{
			_4thSumTA += _4thPriorityQueue.get(i).TurnaroundTime();
		}
		_4thAveTurnaround = _4thSumTA/(float)(_4thPriorityQueue.size());
		
		float _4thSumWT = 0;
		for(int i = 0;i<_4thPriorityQueue.size();i++)
		{
			_4thSumWT += _4thPriorityQueue.get(i).WaitTime();
		}
		_4thAveWaiting = _4thSumWT/(float)(_4thPriorityQueue.size());
		
		float _4thSumRP = 0;
		for(int i = 0;i<_4thPriorityQueue.size();i++)
		{
			_4thSumRP += _4thPriorityQueue.get(i).ResponseTime();
		}
		_4thAveResp = _4thSumRP/(float)(_4thPriorityQueue.size());
		
		
		int _4thStart = -1;
		int _4thEnd = -1;
		//Find start
		for(int i = 0;i<timeChart.size();i++)
		{
			String name = timeChart.get(i);
			for(int j = 0;j<_4thPriorityQueue.size();j++)
			{
				if(name.equals("| "+_4thPriorityQueue.get(j).Name()+" "))
				{
					_4thStart = i;
					break;
				}
			}
			if(_4thStart != -1)
			{
				break;
			}
		}
		//Find End
		for(int i = timeChart.size()-1;i>=0;i--)
		{
			String name = timeChart.get(i);
			for(int j = 0;j<_4thPriorityQueue.size();j++)
			{
				if(name.equals("| "+_4thPriorityQueue.get(j).Name()+" "))
				{
					_4thEnd = i;
					break;
				}
			}
			if(_4thEnd != -1)
			{
				break;
			}
		}
		if(_4thEnd != -1 && _4thStart != -1)
		{
			_4thThruPut = _4thPriorityQueue.size()/(float)(_4thEnd - _4thStart+1);
		}
		
		/*
		 * calculate statistics for all:
		 */
		AveTurnaround = (_1stSumTA+_2ndSumTA+_3rdSumTA+_4thSumTA)/processQueue.length;
		AveWaiting = (_1stSumWT+_2ndSumWT+_3rdSumWT+_4thSumWT)/processQueue.length;
		AveResp = (_1stSumRP+_2ndSumRP+_3rdSumRP+_4thSumRP)/processQueue.length;
		ThruPut = processQueue.length/(float)(timeChart.size());
		
		/*
		 * Print all statistics:
		 */
		
		System.out.println("\n\n1st Priority Queue:");
		System.out.printf("   Average turnaround: %.3f",_1stAveTurnaround);
		System.out.printf("  Average waiting time: %.3f",_1stAveWaiting);
		System.out.printf("  Average response time: %.3f",_1stAveResp);
		System.out.printf("  Throughput: %.3f",_1stThruPut );
		
		System.out.println("\n\n2nd Priority Queue:");
		System.out.printf("  Average turnaround: %.3f",_2ndAveTurnaround);
		System.out.printf("  Average waiting time: %.3f",_2ndAveWaiting);
		System.out.printf("  Average response time: %.3f",_2ndAveResp);
		System.out.printf("  Throughput: %.3f",_2ndThruPut );
		
		System.out.println("\n\n3rd Priority Queue:");
		System.out.printf("  Average turnaround: %.3f",_3rdAveTurnaround);
		System.out.printf("  Average waiting time: %.3f",_3rdAveWaiting);
		System.out.printf("  Average response time: %.3f",_3rdAveResp);
		System.out.printf("  Throughput: %.3f",_3rdThruPut );
		
		System.out.println("\n\n4th Priority Queue:");
		System.out.printf("  Average turnaround: %.3f",_4thAveTurnaround);
		System.out.printf("  Average waiting time: %.3f",_4thAveWaiting);
		System.out.printf("  Average response time: %.3f",_4thAveResp);
		System.out.printf("  Throughput: %.3f",_4thThruPut );
		
		System.out.println("\n\nOverall:");
		System.out.printf("  Average turnaround: %.3f",AveTurnaround);
		System.out.printf("  Average waiting time: %.3f",AveWaiting);
		System.out.printf("  Average response time: %.3f",AveResp);
		System.out.printf("  Throughput: %.3f",ThruPut);
	}
	
	public void reset()
	{
		timeChart.clear();
		quantumTimer = 0;
		_1stPriorityQueue.clear();
		_2ndPriorityQueue.clear();
		_3rdPriorityQueue.clear();
		_4thPriorityQueue.clear();
		
	}
	
	public static void main(String [] args)
	{
		HPFNonPreemptive hpf = new HPFNonPreemptive();
		
		// 1st Run,create 20 processes
		System.out.println(" 1st Run:\n");
		hpf.reset();
		hpf.generateProcesses(20);
		hpf.printProcessQueueInfo();
		hpf.HPFScheduling();
		hpf.printTimeChart();
		hpf.calculateAndPrintStatistics();
		
		 // 2nd Run, Create 30 processes
		System.out.println("\n\n 2nd Run:\n");
		hpf.reset();
		hpf.generateProcesses(30);
		hpf.printProcessQueueInfo();
		hpf.HPFScheduling();
		hpf.printTimeChart();
		hpf.calculateAndPrintStatistics();
		
		// 3rd Run,create 40 processes
		System.out.println("\n\n 3rd Run:\n");
		hpf.reset();
		hpf.generateProcesses(40);
		hpf.printProcessQueueInfo();
		hpf.HPFScheduling();
		hpf.printTimeChart();
		hpf.calculateAndPrintStatistics();
		

		// 4th Run,create 50 processes
		System.out.println("\n\n 4th Run:\n");
		hpf.reset();
		hpf.generateProcesses(50);
		hpf.printProcessQueueInfo();
		hpf.HPFScheduling();
		hpf.printTimeChart();
		hpf.calculateAndPrintStatistics();
		
		// 5th Run,create 60 processes
		System.out.println("\n\n 5th Run:\n");
		hpf.reset();
		hpf.generateProcesses(60);
		hpf.printProcessQueueInfo();
		hpf.HPFScheduling();
		hpf.printTimeChart();
		hpf.calculateAndPrintStatistics();
	}

}

/** Introduction **
 * process class with attributes: arrival time, expected run time, priority level,
 * and a boolean value indicating if job is processed or not.
 * Setters and Getters
 * */
class process
{
	private char[] name = new char [2];
	private int arrivalTime;
	private float expectedRunTime;
	private int priority;
	private float turnaroundTime;
	private float responseTime;
	private float waitTime;
	private boolean isJobDone;

	public process(char[] newName, int newArrival,float newRuntime,int newPriority,float newTurnaroundTime,float newResponseTime)
	{
		setName(newName);
		setArrivalTime(newArrival);
		setRuntime(newRuntime);
		setPriority(newPriority);
		this.setTurnaroundTime(newTurnaroundTime);
		this.setResponseTime(newResponseTime);
		isJobDone = false;
	}

	public String Name()
	{
		return String.valueOf(name);
	}
	public void setName(char[] newName)
	{
		for(int i = 0;i<newName.length;i++)
		{
			name[i] = newName[i];
		}
	}

	public int ArrivalTime() { return arrivalTime; }
	public void setArrivalTime(int newArrival) { this.arrivalTime = newArrival; }

	public float Runtime(){ return expectedRunTime; }
	public void setRuntime(float newRuntime) { this.expectedRunTime = newRuntime; }

	public int Priority(){ return priority; }
	public void setPriority(int newPriority) { this.priority = newPriority; }

	public float TurnaroundTime(){ return turnaroundTime; }
	public void setTurnaroundTime(float newTurnaroundTime) { this.turnaroundTime = newTurnaroundTime; }

	public float ResponseTime(){ return responseTime; }
	public void setResponseTime(float newResponseTime) { this.responseTime = newResponseTime; }

	public boolean IsJobDone(){ return isJobDone; }
	public void markJobDone() { isJobDone = true; }

	public float WaitTime() { return turnaroundTime - expectedRunTime;}
}
