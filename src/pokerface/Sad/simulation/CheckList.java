package pokerface.Sad.simulation;

import java.util.ArrayList;

/**
 * ��ͬ���͵ļ����Ŀ����
 * @author 79864
 *
 */
public class CheckList extends ArrayList<State>{
//	List<State> checkList = null;
	String checkName;
	CheckList nextCheckList = null;
	public CheckList(String name) {
		super();
//		this.checkList = new ArrayList<State>();
		this.checkName = name;
	}
	/**
	 * �ҳ�����������̶��е�index
	 */
	public int findMinQueue(){
		int minIndex = 0;
		int minSize = this.get(0).passengers.size();
//		int minSize = this.checkList.get(0).passengers.size();
		int index = 0;
//		for(State check:this.checkList){
		for(State check:this){
			if(check.passengers.size()<minSize) 
			{
				minIndex = index;
				minSize = check.passengers.size();
			}
			index++;
		}
		return minIndex;
	}
	/**
	 * �ҵ�list�г�����̵Ķ��н�Passenger����
	 * @param passenger
	 */
	public void addInCheckList(Passenger passenger){
		int MinIndex = this.findMinQueue();
		this.get(MinIndex).addInCheckQuene(passenger);
//		this.checkList.get(MinIndex).addInCheckQuene(passenger);
		//passenger.state = nextState; //����passenger��״̬
	}
}
