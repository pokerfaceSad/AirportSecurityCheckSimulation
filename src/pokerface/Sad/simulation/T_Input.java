package pokerface.Sad.simulation;

import java.util.List;

/**
 * 控制乘客流入的线程
 *
 */
public class T_Input implements Runnable{
	CheckList firstList = Main.firstStateList;
	List<Thread> threadList;
	public T_Input(List<Thread> threadList) {
		this.threadList = threadList;
	}
	@Override
	public void run() {
		
		for(Thread thread:threadList)
			thread.start();
		while(true){
			this.input(7);
			System.out.println(Main.arrivalPassengerNum);
			
			System.out.println("线程已全部启动");
			try {
				Thread.currentThread().sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(Main.arrivalPassengerNum>=Main.passengersNum)
				break;
		}
		
	}
	/**
	 * 输入客流
	 */
	public void input(int num){
		Passenger passenger = null;
		for(int i=0;i<num;i++){
			passenger = new Passenger(Main.arrivalPassengerNum);
			passenger.isVIP = T_Input.getRandom();
			this.firstList.addInCheckList(passenger);
			if(!dbUtil.initPassenger(passenger.no,passenger.isVIP?"true":"false"))
			{
				System.out.println("初始化失败");
				return;
			}
			Main.arrivalPassengerNum++;
		}
	}
	public static boolean getRandom(){
//		if((int)(1+Math.random()*100)<=85)
//			return true;
//		else
			return false;
	}
}
