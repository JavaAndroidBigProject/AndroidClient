package ServerInterface;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * 游戏桌状态信息
 */
public class TableInfo {
	/**
	 * 游戏桌编号
	 */
	public int id;

	/**
	 * 玩家1信息<br>
	 * 若空表示无玩家1
	 */
	public PlayerInfo player1;

	/**
	 * 玩家2信息<br>
	 * 若空表示无玩家2
	 */
	public PlayerInfo player2;

	/**
	 * 构造函数
	 * @param id
	 * 编号
	 * @param play1Name
	 * 玩家1用户名
	 * @param play1Score
	 * 玩家1分数
	 * @param play2Name
	 * 玩家2用户名
	 * @param play2Score
	 * 玩家2分数
	 */
	public TableInfo(int id, String play1Name, int play1Score, String play2Name, int play2Score){
		this.id = id;
		player1 = new PlayerInfo(play1Name, play1Score);
		player2 = new PlayerInfo(play2Name, play2Score);
	}

	/**
	 * 将TableInfo数组转化为字符串表示用于TCP传送
	 * @param tableInfos
	 * TableInfo数组
	 * @return
	 * 字符串表示
	 */
	public static String tableInfoArrayToString(TableInfo [] tableInfos){
		StringBuilder result = new StringBuilder();
		for(TableInfo tableInfo : tableInfos){
			result.append(String.format("%d$",tableInfo.id));
			result.append(String.format("%s$",tableInfo.player1.name));
			result.append(String.format("%d$",tableInfo.player1.score));
			result.append(String.format("%s$",tableInfo.player2.name));
			result.append(String.format("%d$",tableInfo.player2.score));
		}
		result.delete(result.length() - 1, result.length());
		return result.toString();
	}

	public static TableInfo[] stringToTableInfoArray(String string){
		String[] strings = string.split("\\$");
		//倒霉，$是正则表达式的一个通配符
		List<TableInfo> result = new LinkedList<>();
		for(int i = 0; i < strings.length; i += 5){
			TableInfo tableInfo = new TableInfo(
					Integer.parseInt(strings[i + 0]),
					strings[i + 1],
					Integer.parseInt(strings[i + 2]),
					strings[i + 3],
					Integer.parseInt(strings[i + 4])
			);
			result.add(tableInfo);
		}
		TableInfo[] tableInfos = new TableInfo[result.size()];
		for(int i = 0; i < tableInfos.length; ++i){
			tableInfos[i] = result.get(i);
		}
		return tableInfos;
	}
/*
	// 测试代码
	public static void main(String[] args){
		System.out.println("换行分隔，输入3组:id player1.name player1.score player2.name player2.score");
		TableInfo[] tableInfos = new TableInfo[3];
		Scanner scanner = new Scanner(System.in);
		for(int i = 0; i < tableInfos.length; ++i){
			tableInfos[i] = new TableInfo(
					new Scanner(scanner.nextLine()).nextInt(),
					scanner.nextLine(),
					new Scanner(scanner.nextLine()).nextInt(),
					scanner.nextLine(),
					new Scanner(scanner.nextLine()).nextInt()
			);
		}

		String s = TableInfo.tableInfoArrayToString(tableInfos);
		System.out.println(s);
		TableInfo newTableInfos[] = TableInfo.stringToTableInfoArray(s);
		for(TableInfo tableInfo : newTableInfos){
			System.out.println(String.format("%d",tableInfo.id));
			System.out.println(String.format("%s",tableInfo.player1.name));
			System.out.println(String.format("%d",tableInfo.player1.score));
			System.out.println(String.format("%s",tableInfo.player2.name));
			System.out.println(String.format("%d",tableInfo.player2.score));
		}

	}
*/

/*测试结果：
输入：
1
a
1
b
2
2
qwertsdfgag
1212
asdfgadfhadfh
-12121212
3
afdgafg
1444
qqqqqqqqq
2222222
输出：
1$a$1$b$2$2$qwertsdfgag$1212$asdfgadfhadfh$-12121212$3$afdgafg$1444$qqqqqqqqq$2222222
1
a
1
b
2
2
qwertsdfgag
1212
asdfgadfhadfh
-12121212
3
afdgafg
1444
qqqqqqqqq
2222222
*/
}