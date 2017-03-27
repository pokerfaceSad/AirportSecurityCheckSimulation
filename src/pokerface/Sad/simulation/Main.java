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
		 * ��ʼ����̬����
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
		 * ��ʼ������
		 */
		
		//ʵ����Check����
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
		 * ʵ����CheckList����
		 */
		CheckList idCheckList = new CheckList("IDCheck");
		CheckList baggageBodyCheckList = new CheckList("BaggageBodyCheck");
		CheckList collectCheckList = new CheckList("Collect");
		idCheckList.nextCheckList = baggageBodyCheckList;
		baggageBodyCheckList.nextCheckList = collectCheckList;
		collectCheckList.nextCheckList = finsh;
		firstStateList = idCheckList;
		/**
		 * ��ʼ��State�������List
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
		 * ��ʼ��ServiceList
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

		//��ʼ���߳�
//		Thread t_IDCheck = new Thread(new Service(idCheck));
//		Thread t_BaggageBodyScreening = new Thread(new Service(baggageBodyScreening));
//		Thread t_Collect = new Thread(new Service(collect));
		
		//ʵ����Ҫ�����passenger���� List<Passenger> 
		/*List<Passenger> passengers = new ArrayList<Passenger>();
		for(int i=0;i<passengersNum;i++)
		{
			passengers.add(new Passenger(i+1));
		}*/
		//������passenger����firtsState�Ķ���
		/*for(Passenger passenger:passengers)
		{
			firstStateList.get(firstStateList.findMinQueue()).passengers.add(passenger);
			if(!dbUtil.initPassenger(passenger.no))
				{
					System.out.println("��ʼ��ʧ��");
					return;
				}
		}*/
		System.out.println("��ʼ�����");
		
		/**
		 * ��ʼ����ɣ���ʼģ��
		 */
		/**
		 * ���������߳�
		 */
		new Thread(new T_Input(threadList)).start();
//		System.out.println(arrivalPassengerNum);
//		for(Thread thread:threadList)
//			thread.start();
//		System.out.println("�߳���ȫ������");
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