package pokerface.Sad.simulation;

public class Passenger {
	int no;   //�˿ͱ��
	State state ;  //������״̬
	boolean isVIP;  //�˿����ͣ��Ƿ���Pre-Check
	int time = 0;    //�ӿ�ʼ���쵽���������ʱ��
	int lastCheckFinshTime = 0; 	//��һ��Check��ɵ�ʱ��

	public Passenger(int no) {
		super();
		this.no = no;
	}

	@Override
	public String toString() {
		return "Passenger [no=" + no + "]";
	}
}
