package pokerface.Sad.simulation;

public class Passenger {
	int no;   //乘客编号
	State state ;  //所处的状态
	boolean isVIP;  //乘客类型：是否是Pre-Check
	int time = 0;    //从开始安检到结束安检的时间
	int lastCheckFinshTime = 0; 	//上一个Check完成的时间

	public Passenger(int no) {
		super();
		this.no = no;
	}

	@Override
	public String toString() {
		return "Passenger [no=" + no + "]";
	}
}
