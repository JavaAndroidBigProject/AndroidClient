package ServerInterface;

import java.net.Socket;
import java.util.Scanner;

/**
 * 发桌面请求的心跳包
 */
public class GettingTablesThread extends Thread{
	OriginInterface originInterface;

	public GettingTablesThread(OriginInterface originInterface){
		this.originInterface = originInterface;
	}

	@Override
	public void run() {
		try {
			while (originInterface.listenThread.isAlive()) {
				originInterface.getTables();
				sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
