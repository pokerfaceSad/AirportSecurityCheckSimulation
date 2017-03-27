package pokerface.Sad.simulation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class State{
	int no; //在所在list所处的index
	CheckList list; //所在list的引用
	String name; //所处状态的名称
	boolean isChecking = false;
	final int checkTime; //通过此检查所需的时间
	int nextPassengerWaitTime = 0; //下一位检查者需要的等待的时间 ，初值为0，在check中更新
	int addPassengerNumDuringCheck = 0; //在检查过程中加入的passenger数量
	List<Passenger> passengers;  //处于此检查的乘客集合
//	pokerface.Sad.simulation.State nextState;  //下一项检查
	/**
	 * 此方法被lastState调用
	 * @param passenger
	 */
	public void addInCheckQuene(Passenger passenger){
		this.passengers.add(passenger); //将开始此检查的乘客加入Passenger队列
		passenger.time = passenger.time + this.nextPassengerWaitTime; //先将nextState正在进行的检查的剩余时间更新
		this.addPassengerNumDuringCheck++;
//		passenger.state = nextState; //更新passenger的状态
	}
	
	/**
	 * 若队列不为空则检查第一位乘客
	 * 更新所有队列中所有passenger的time
	 * 输出队列信息
	 * 更新正在检查乘客的lastCheckTime
	 * 将正在检查乘客加入nextState队列
	 * 将正在检查乘客从队列中移除
	 */
	public void check(){
		int checkTime = this.checkTime;
		//若队列为空则直接返回
		if(this.passengers.isEmpty()) return;
//		Passenger passenger = this.passengers.get(0); 
		//检查
			//线程休眠
//			System.out.println(this.passengers.get(0)+"is checked in "+this);
			this.addPassengerNumDuringCheck = 0;
			System.out.println(this.passengers.get(0));
			System.out.println(this.name);
			if(this.passengers.get(0).isVIP&&this.name.equals("BaggageBodyCheck")){
				Properties pro = null;
				try {
					pro = Main.getProperties();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				checkTime = new Integer(pro.getProperty("preCheckerBaggageBodyCheckTime"));
			}
			try {
				for(int i=checkTime;i>0;i--)
				{
					this.nextPassengerWaitTime = i-1;
					Thread.currentThread().sleep(1);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		//检查后
		this.updateListTime(checkTime);	//更新队列中所有passenger的等待时间
		this.outPutCheckMsg(this.passengers.get(0));//输出在此Check总共花费的时间
		this.passengers.get(0).lastCheckFinshTime = this.passengers.get(0).time;//更新passenger的lastCheckTime
		this.list.nextCheckList.addInCheckList(this.passengers.get(0));  //调用下个List的入队方法
		this.passengers.remove(0); 	//将passenger移出this的队列
		
	}
	/**
	 * 输出接受检查passenger在此Check花费的时间(输出到数据库)
	 * 输出正在等待的passenger信息
	 * @param passenger
	 */
	private void outPutCheckMsg(Passenger passenger) {
		int time = (passenger.time-passenger.lastCheckFinshTime);
		if(time==0) time=this.checkTime;
		String sql = "update passenger_msg set "+this.name+"_time='"+time+"' ,"+this.name+"_no="+this.no+" where no="+passenger.no+";";
		System.out.println(sql);
		if(!dbUtil.writeMsg(sql)) System.out.println("写入数据库失败");
//		System.out.println(passenger+" pass "+this+" with "+(passenger.time-passenger.lastCheckFinshTime)+"s");
	
	}
	

	@Override
	public String toString() {
		return "State [name=" + name + ", no=" + no + "]";
	}

	public State(String name, int time,int no) {
		super();
		this.no = no;
		this.name = name;
		this.checkTime = time;
		this.passengers = new ArrayList<Passenger>();
	}

	/**
	 * 更新队列中除了末尾passenger以外的所有passenger的等待时间
	 * 末尾passenger的time已经在addInCheckList中更新
	 * @param time
	 */
	public void updateListTime(int time){
		int index = 0;
		for(Passenger p:this.passengers)
		{
			if(index==this.passengers.size()-this.addPassengerNumDuringCheck) break; //若是末节点则跳过
			p.time = p.time + time;
			index++;
		}
	}

	
}
class Service implements Runnable{

	State state = null;
	public Service(State state) {
		super();
		this.state = state;
	}

	@Override
	public void run() {
		while(true)
		{
			if(Main.finsh.get(0).passengers.size()==Main.passengersNum) break;
			this.state.check();
		}
	}
	
	
	
}