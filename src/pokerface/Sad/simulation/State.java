package pokerface.Sad.simulation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class State{
	int no; //������list������index
	CheckList list; //����list������
	String name; //����״̬������
	boolean isChecking = false;
	final int checkTime; //ͨ���˼�������ʱ��
	int nextPassengerWaitTime = 0; //��һλ�������Ҫ�ĵȴ���ʱ�� ����ֵΪ0����check�и���
	int addPassengerNumDuringCheck = 0; //�ڼ������м����passenger����
	List<Passenger> passengers;  //���ڴ˼��ĳ˿ͼ���
//	pokerface.Sad.simulation.State nextState;  //��һ����
	/**
	 * �˷�����lastState����
	 * @param passenger
	 */
	public void addInCheckQuene(Passenger passenger){
		this.passengers.add(passenger); //����ʼ�˼��ĳ˿ͼ���Passenger����
		passenger.time = passenger.time + this.nextPassengerWaitTime; //�Ƚ�nextState���ڽ��еļ���ʣ��ʱ�����
		this.addPassengerNumDuringCheck++;
//		passenger.state = nextState; //����passenger��״̬
	}
	
	/**
	 * �����в�Ϊ�������һλ�˿�
	 * �������ж���������passenger��time
	 * ���������Ϣ
	 * �������ڼ��˿͵�lastCheckTime
	 * �����ڼ��˿ͼ���nextState����
	 * �����ڼ��˿ʹӶ������Ƴ�
	 */
	public void check(){
		int checkTime = this.checkTime;
		//������Ϊ����ֱ�ӷ���
		if(this.passengers.isEmpty()) return;
//		Passenger passenger = this.passengers.get(0); 
		//���
			//�߳�����
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
		//����
		this.updateListTime(checkTime);	//���¶���������passenger�ĵȴ�ʱ��
		this.outPutCheckMsg(this.passengers.get(0));//����ڴ�Check�ܹ����ѵ�ʱ��
		this.passengers.get(0).lastCheckFinshTime = this.passengers.get(0).time;//����passenger��lastCheckTime
		this.list.nextCheckList.addInCheckList(this.passengers.get(0));  //�����¸�List����ӷ���
		this.passengers.remove(0); 	//��passenger�Ƴ�this�Ķ���
		
	}
	/**
	 * ������ܼ��passenger�ڴ�Check���ѵ�ʱ��(��������ݿ�)
	 * ������ڵȴ���passenger��Ϣ
	 * @param passenger
	 */
	private void outPutCheckMsg(Passenger passenger) {
		int time = (passenger.time-passenger.lastCheckFinshTime);
		if(time==0) time=this.checkTime;
		String sql = "update passenger_msg set "+this.name+"_time='"+time+"' ,"+this.name+"_no="+this.no+" where no="+passenger.no+";";
		System.out.println(sql);
		if(!dbUtil.writeMsg(sql)) System.out.println("д�����ݿ�ʧ��");
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
	 * ���¶����г���ĩβpassenger���������passenger�ĵȴ�ʱ��
	 * ĩβpassenger��time�Ѿ���addInCheckList�и���
	 * @param time
	 */
	public void updateListTime(int time){
		int index = 0;
		for(Passenger p:this.passengers)
		{
			if(index==this.passengers.size()-this.addPassengerNumDuringCheck) break; //����ĩ�ڵ�������
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