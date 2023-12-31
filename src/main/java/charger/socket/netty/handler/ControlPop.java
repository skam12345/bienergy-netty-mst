package charger.socket.netty.handler;

import java.sql.Connection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import charger.socket.model.ChargerDataModel;
import charger.socket.mysql.QueryExcuteClass;
import io.netty.channel.ChannelPipeline;

public class ControlPop {
	private int excel = 0;
	public ControlPop() {
		
	}
	public void run(Connection conn, QueryExcuteClass execute, ChannelPipeline pipline, LoginHandler loginHandler, ControlHandler control, int count) throws Exception {
		ExecutorService service = Executors.newFixedThreadPool(1);
		service.execute(() -> {
			try {
				if(loginHandler.getCopy() != null) {
					while(true) {
						Thread.sleep(4000);
						int sendNo = execute.callCommand(loginHandler.getCopy().substring(0, 12));
						ChargerDataModel model = execute.callChargerData(sendNo);
						String code = execute.checkCommand(loginHandler.getCopy().substring(0, 12), model.getPlugNo());
						if(code != null) {
							if(code.equals("0AH02")) {
								List<Integer> sendNoList = execute.callStartCommand(loginHandler.getCopy().substring(0, 12));
								System.out.println("충전 시작합니다.");	
								control.setCode(code);
								loginHandler.setModel(model);
								control.setChargerDataModel(model);
								control.setValues(loginHandler.getCopy());
								System.out.println("명령을 대기합니다.");
							}
							if(code.equals("0AH03")) {
								List<Integer> sendNoList = execute.callStopCommand(loginHandler.getCopy().substring(0, 12));
								System.out.println("충전을 중지합니다.");
								control.setCode(code);
								loginHandler.setModel(model);
								control.setChargerDataModel(model);
								control.setValues(loginHandler.getCopy());
								control.setPipline(pipline);
							}    						
						}else {
							continue;
						}
					}
				}else {
					run(conn, execute, pipline, loginHandler, control, count);
					Thread.sleep(1000);
				}
			}catch(Exception e) {
				e.printStackTrace();
			
			}
		});
    }
}
