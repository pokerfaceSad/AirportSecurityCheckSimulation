package pokerface.Sad.simulation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Main {

	static int passengersNum ;
	static int arrivalPassengerNum = 0;
	static CheckList firstStateList = null;
	static int idCheckListSize;
	static int baggageBodyCheckListSize;
	static int collectCheckListSize;
	static int idChectTime;
	static int baggageBodyCheckTime;
	static int collectCheckTime;
	static CheckList finsh = new CheckList("finsh");
	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		/**
		 * 初始化静态变量
		 */
		Properties pro = Main.getProperties();
		passengersNum = new Integer(pro.getProperty("passengersNum"));
		idCheckListSize = new Integer(pro.getProperty("idCheckListSize"));
		baggageBodyCheckListSize = new Integer(pro.getProperty("baggageBodyCheckListSize"));
		collectCheckListSize = new Integer(pro.getProperty("collectCheckListSize"));
		idChectTime = new Integer(pro.getProperty("idChectTime"));
		baggageBodyCheckTime = new Integer(pro.getProperty("baggageBodyCheckTime"));
		collectCheckTime = new Integer(pro.getProperty("collectCheckTime"));
		finsh.add(new State("finsh", 0, 0));
		/**
		 * 初始化对象
		 */
		
		//实例化Check对象
		/*
		List<State> checkList = new ArrayList<State>();
		
		State idCheck = new State("IDCheck", 10);
		State baggageBodyScreening = new State("Baggage and Body Screening", 30);
		State collect = new State("Collect and Exit", 20);
		idCheck.nextState = baggageBodyScreening;
		baggageBodyScreening.nextState = collect;
		collect.nextState = finsh;
		checkList.add(idCheck);
		checkList.add(baggageBodyScreening);
		checkList.add(collect);
		firstState = idCheck;
		*/
		/**
		 * 实例化CheckList对象
		 */
		CheckList idCheckList = new CheckList("IDCheck");
		CheckList baggageBodyCheckList = new CheckList("BaggageBodyCheck");
		CheckList collectCheckList = new CheckList("Collect");
		idCheckList.nextCheckList = baggageBodyCheckList;
		baggageBodyCheckList.nextCheckList = collectCheckList;
		collectCheckList.nextCheckList = finsh;
		firstStateList = idCheckList;
		/**
		 * 初始化State对象加入List
		 */
		State state = null;
		for(int i=0;i<idCheckListSize;i++){
			state = new State(idCheckList.checkName, Main.idChectTime, i);
			state.list = idCheckList;
			idCheckList.add(state);
		}
		for(int i=0;i<baggageBodyCheckListSize;i++){
			state = new State(baggageBodyCheckList.checkName, Main.baggageBodyCheckTime, i);
			state.list = baggageBodyCheckList;
			baggageBodyCheckList.add(state);
		}
		for(int i=0;i<collectCheckListSize;i++){
			state = new State(collectCheckList.checkName, Main.collectCheckTime, i);
			state.list = collectCheckList;
			collectCheckList.add(state);
		}
		/**
		 * 初始化ServiceList
		 */
		List<Thread> threadList = new ArrayList<Thread>();
		List<Thread> t_idCheckList = new ArrayList<Thread>();
		List<Thread> t_baggageBodyCheckList = new ArrayList<Thread>();
		List<Thread> t_collectCheckList = new ArrayList<Thread>();
		Thread t = null;
		for(State idCheck:idCheckList)
		{
			t = new Thread(new Service(idCheck));
			t.setName(idCheck.name+idCheck.no);
			t_idCheckList.add(t);
			threadList.add(t);
		}
		for(State baggageBodyCheck:baggageBodyCheckList)
		{
			t = new Thread(new Service(baggageBodyCheck));
			t.setName(baggageBodyCheck.name+baggageBodyCheck.no);
			t_baggageBodyCheckList.add(t);
			threadList.add(t);
		}
		for(State collectCheck:collectCheckList)
		{	
			t = new Thread(new Service(collectCheck));
			t.setName("t_"+collectCheck.name+collectCheck.no);
			t_collectCheckList.add(t);
			threadList.add(t);
		}

		//初始化线程
//		Thread t_IDCheck = new Thread(new Service(idCheck));
//		Thread t_BaggageBodyScreening = new Thread(new Service(baggageBodyScreening));
//		Thread t_Collect = new Thread(new Service(collect));
		
		//实例化要输入的passenger集合 List<Passenger> 
		/*List<Passenger> passengers = new ArrayList<Passenger>();
		for(int i=0;i<passengersNum;i++)
		{
			passengers.add(new Passenger(i+1));
		}*/
		//将所有passenger加入firtsState的队列
		/*for(Passenger passenger:passengers)
		{
			firstStateList.get(firstStateList.findMinQueue()).passengers.add(passenger);
			if(!dbUtil.initPassenger(passenger.no))
				{
					System.out.println("初始化失败");
					return;
				}
		}*/
		System.out.println("初始化完成");
		
		/**
		 * 初始化完成，开始模拟
		 */
		/**
		 * 启动所有线程
		 */
		new Thread(new T_Input(threadList)).start();
//		System.out.println(arrivalPassengerNum);
//		for(Thread thread:threadList)
//			thread.start();
//		System.out.println("线程已全部启动");
//		t_IDCheck.start();
//		t_BaggageBodyScreening.start();
//		t_Collect.start();
		while(true)
		{
			if(!Main.isThreadAllAlive(threadList)) break;
		}
//		while(true){
//			if((!t_IDCheck.isAlive())&
//			   (!t_BaggageBodyScreening.isAlive())&
//			   (!t_Collect.isAlive())) 
//			break;
//		}
//		
//		for(Passenger passenger:finsh.get(0).passengers)
//		{
//			System.out.println(passenger+" finsh check with "+passenger.time+"s");
//		}
	}
	public static Properties getProperties() throws FileNotFoundException, IOException{
		
		Properties pro = new Properties();
		pro.load(new FileInputStream("Main.properties"));
		return pro;
			
	}
	public static boolean isThreadAllAlive(List<Thread> threadList){
		for(Thread t:threadList)
		{
			if(!t.isAlive()) 
			{
				return false;
			}
		}	
		return true;
	}
}