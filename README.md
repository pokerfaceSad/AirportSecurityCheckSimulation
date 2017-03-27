# 2017美赛D题用到的机场安检排队模拟

仿真模拟机场安检过程

1.建立模拟案件过程的模型

	A区域
	1> ID Check
	B区域
	2> Baggage and Body Screening
	C区域
	3> Collect and Exit

2.输入数据


Java 
	Thread{
		State;
		State.check();
		
	}
	
	检查类
		State{
			time； //通过此检查的时间
			List<passenger> passengers;  //处于此检查的乘客集合
			nextCheck;  //下一项检查
			addInCheckList(passenger){
				this.passengers.add(passenger); //将开始此检查的乘客加入Passenger队列
				passenger.state = nextCheck; //更新passenger的状态
			}
			finshCheck(passenger){
				this.nextCheck.addInCheck();  //将passenger加入nexCheck的队列
				this.passengers.get(0).remove(); //将passenger移出this的队列
			}
		}
	乘客类
		Passenger{
			state;  //所处的状态
			type;  //乘客类型：是否是Pre-Check
			
		}

	main{
	
		实例化对应的Check对象
		实例化要输入的passenger集合 List<Passenger> 

	}
