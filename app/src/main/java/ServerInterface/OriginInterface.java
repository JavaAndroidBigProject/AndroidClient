package ServerInterface;


import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 原始业务类，安卓客户端业务类和PC客户端业务类基于其实现<br>
 * 与服务器使用TCP协议通信<br>
 * 每次通信都是发送或接收一行规定格式的字符串
 */
public abstract class OriginInterface {

	/**
	 * 服务器地址
	 */
	private InetAddress inetAddress;

	/**
	 * 服务器端口
	 */
	private int port;

	/**
	 * 套接字
	 */
	private Socket socket;

	/**
	 * 套接字输出流
	 */
	private PrintStream printStream;

	/**
	 * 监听服务器消息线程
	 */
	ListenThread listenThread;

	/**
	 * 发送请求桌面状态的心跳包线程
	 */
	GettingTablesThread gettingTablesThread;

	/**
	 * 构造函数
	 * @param inetAddress
	 * 服务器地址
	 * @param port
	 * 服务器端口
	 */
	public OriginInterface(InetAddress inetAddress, int port){
		this.inetAddress = inetAddress;
		this.port = port;
	}

	/**
	 * 退出游戏,将正常断开与服务器连接
	 */
	private void quit(){
		if(listenThread != null && listenThread.isAlive()){
			listenThread.discontect();
		}
	}

	/**
	 * 连接服务器
	 * @return
	 * 是否连接成功
	 */
	private boolean connect(){
		if(socket != null && socket.isConnected())
			return true;
		try {
			socket = new Socket(inetAddress, port);
			printStream = new PrintStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			onConnectionFail(e.getMessage());
			return false;
		}
		listenThread = new ListenThread(socket, this);
		listenThread.start();
		gettingTablesThread = new GettingTablesThread(this);
		gettingTablesThread.start();
		return true;
	}

	/**
	 * 写字符串进套接字
	 * @param string
	 * 字符串
	 */
	private void writeInSocket(String string){
		try{
			printStream.println(string);
			printStream.flush();
		}catch (Exception e){
			e.printStackTrace();
			onLostConnection(e.getMessage());
		}
	}

	/**
	 * 请求注册<br>
	 * 向服务器发送 REGISTER#用户名#密码
	 * @param username
	 * 用户名
	 * @param password
	 * 密码
	 */
	final public void register(String username, String password){
		if(username.contains("#") || password.contains("#") || username.contains("$") || password.contains("$")){
			onRespondRegister(false,"用户名和密码不得含有#或$");
			return;
		}

		if(username.equals("empty")){
			onRespondRegister(false,"用户名不可以是empty");
			return;
		}

		if(!connect())
			return;
		writeInSocket("REGISTER#" + username + "#" +password);
	}

	/**
	 * 请求登陆<br>
	 * 向服务器发送 LOGIN#用户名#密码
	 * @param username
	 * 用户名
	 * @param password
	 * 密码
	 */
	final public void login(String username, String password){
		if(!connect())
			return;
		if(username.contains("#") || password.contains("#") || username.contains("$") || password.contains("$")){
			onRespondLogin(false, 0, "用户名和密码不得含有#或$");
			return;
		}
		writeInSocket("LOGIN#" + username + "#" +password);
	}

	/**
	 * 请求所有游戏桌状态<br>
	 * 向服务器发送 GET_TABLES
	 * 继承者不要调用
	 */
	final void getTables(){
		writeInSocket("GET_TABLES");
	}
	
	/**
	 * 请求进入游戏桌<br>
	 * 向服务器发送 ENTER_TABLES#游戏桌编号
	 * @param tableId
	 * 游戏桌编号
	 */
	final public void enterTable(int tableId){
		writeInSocket("ENTER_TABLES#" + tableId);
	}

	/**
	 * 请求举手<br>
	 * 向服务器发送 HAND_UP
	 */
	final public void handUp(){
		writeInSocket("HAND_UP");
	}

	/**
	 * 请求落子<br>
	 * 向服务器发送 MOVE#行#列
	 * @param row
	 * 行
	 * @param col
	 * 列
	 */
	final public void move(int row, int col){
		writeInSocket("MOVE#" + row + "#" + col);
	}

	/**
	 * 请求认输<br>
	 * 向服务器发送 GIVE_UP
	 */
	final public void giveUp(){
		writeInSocket("GIVE_UP");
	}

	/**
	 * 请求悔棋<br>
	 * 向服务器发送 RETRACT
	 */
	final public void retract(){
		writeInSocket("RETRACT");
	}

	/**
	 * 响应对手悔棋<br>
	 * 向服务器发送 RESPOND_RETRACT#是否同意
	 * @param ifAgree
	 * 是否同意
	 */
	final public void respondRetract(boolean ifAgree){
		writeInSocket("RESPOND_RETRACT#" + ifAgree);
	}

	/**
	 * 发送消息<br>
	 * 向服务器发送 SEND_MESSAGE#消息内容
	 * @param message
	 * 消息内容
	 */
	final public void sengMessage(String message){
		if(message.contains("#")){
			onReceiveMessage("[系统提示]消息中不得含有#.");
			return;
		}
		writeInSocket("SEND_MESSAGE#" + message);
	}

	/**
	 * 请求退出游戏桌<br>
	 * 向服务器发送 QUIT_TABLE
	 */
	final public void quitTable(){
		writeInSocket("QUIT_TABLE");
	}

	/**
	 * 当收到注册响应<br>
	 * 服务器返回 ON_RESPOND_REGISTER#是否注册成功#注册失败的原因
	 * @param ifRegistered
	 * 是否注册成功
	 * @param reason
	 * 注册失败的原因
	 */
	abstract public void onRespondRegister(boolean ifRegistered, String reason);

	/**
	 * 当连接服务器失败
	 * @param reason
	 * 连接失败原因
	 */
	abstract public void onConnectionFail(String reason);

	/**
	 * 当失去服务器连接
	 * @param reason
	 * 失去服务器连接原因
	 */
	abstract public void onLostConnection(String reason);

	/**
	 * 当收到登陆响应<br>
	 * 服务器返回 ON_RESPOND_LOGIN#是否登陆成功#玩家的分数
	 * @param ifLogined
	 * 是否登陆成功
	 * @param score
	 * 玩家的分数
	 * @param reason
	 * 登陆失败的原因
	 */
	abstract public void onRespondLogin(boolean ifLogined, int score, String reason);

	/**
	 * 当收到请求各游戏桌状态响应<br>
	 * 服务器返回 ON_RESPOND_GET_TABLES#由TableInfo.tableInfoArrayToString()生成的字符串
	 * @param tableInfos
	 * 各游戏桌状态
	 */
	abstract public void onRespondGetTables(TableInfo[] tableInfos);

	/**
	 * 当收到请求进入游戏桌响应<br>
	 * 服务器返回 ON_RESPOND_ENTER_TABLE#游戏桌编号#是否进入游戏桌#进入失败的原因
	 * @param tableId
	 * 游戏桌编号
	 * @param ifEntered
	 * 是否进入游戏桌
	 * @param reason
	 * 进入失败的原因
	 */
	abstract public void onRespondEnterTable(int tableId, boolean ifEntered, String reason);

	/**
	 * 当所在游戏桌状态变化<br>
	 * 服务器返回 ON_TABLE_CHANGE#自己用户名#自己分数#对手用户名#对手分数#自己是否举手#对手是否举手#游戏是否进行中#棋盘的逻辑数组#自己是否执黑子#是否轮到自己下
	 * @param myInfo
	 * 自己的信息
	 * @param opponentInfo
	 * 对手信息
	 * @param ifMyHandUp
	 * 自己是否举手
	 * @param ifOpponentHandUp
	 * 对手是否举手
	 * @param isPlaying
	 * 游戏是否进行中
	 * @param board
	 * 棋盘的逻辑数组，1表黑棋，2表白旗，0表空
	 * @param isBlack
	 * 自己是否执黑子
	 * @param isMyTurn
	 * 是否轮到自己下
	 */
	abstract public void onTableChange(
										PlayerInfo myInfo,
										PlayerInfo opponentInfo,
										boolean ifMyHandUp,
										boolean ifOpponentHandUp,
										boolean isPlaying,
										int board [][],
										boolean isBlack,
										boolean isMyTurn);

	/**
	 * 当游戏结束<br>
	 * 服务器返回 ON_GAME_OVER#是否是平局#是否是自己赢#是否是某一方认输
	 * @param isDraw
	 * 是否是平局
	 * @param ifWin
	 * 是否是自己赢
	 * @param ifGiveUp
	 * 是否是某一方认输
	 */
	abstract public void onGameOver(boolean isDraw, boolean ifWin, boolean ifGiveUp);

	/**
	 * 当收到请求悔棋响应<br>
	 * 服务器返回 ON_RESPOND_RETRACT#ifAgree
	 * @param ifAgree
	 * 对手是否同意悔棋，若同意，会随后收到onBoardChange
	 */
	abstract public void onRespondRetract(boolean ifAgree);

	/**
	 * 当收到对手请求悔棋<br>
	 * 服务器返回 ON_OPPONENT_RETRACT
	 */
	abstract public void onOpponentRetract();

	/**
	 * 当收到消息<br>
	 * 服务器返回 ON_RECEIVE_MESSAGE#消息内容
	 * @param message
	 * 消息内容
	 */
	abstract public void onReceiveMessage(String message);

	/**
	 * 当请求退出游戏桌响应<br>
	 * 服务器返回 ON_RESPOND_QUIT_TABLE
	 */
	abstract public void onRespondQuitTable();

}
