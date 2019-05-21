package The_Final;

import java.util.ArrayList;

public class Ai {
	static int[][] now;// 作为当前棋盘内部计算矩阵的拷贝
	ArrayList<Nodes> choose;// 储存最优的落子位置
	static int max_now;// 当前最优的落子后白棋数目
	// Nodes类记录每次的选择棋子和落子位置

	class Nodes {
		int x_select;
		int y_select;
		int x_next;
		int y_next;
		Integer[][] n;

		Nodes(Integer[][] m, int x, int y, int i, int j) {
			n = new Integer[7][7];
			x_select = x;
			y_select = y;
			x_next = i;
			y_next = j;
			for (int a = 0; a < 7; a++) {
				for (int b = 0; b < 7; b++) {
					n[a][b] = m[a][b];
				}
			}
		}
	}

	// 构造函数，根据当前棋盘内部计算矩阵计算最好的行棋
	public Ai(int[][] point) {
		// 初始化
		now = new int[7][7];
		max_now = 0;
		choose = new ArrayList<>();
		// 将now置为当前棋盘内部计算矩阵的拷贝
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				now[i][j] = point[i][j];
			}
		}
		// 遍历整个棋盘，找出每一个白棋的行棋方案，并选择最好的方案
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				if (now[i][j] == 2) {
					compute(i, j);
				}
			}
		}
	}

	// 计算白棋的可选择行棋
	public void compute(int x, int y) {
		// 计算每一个白棋的可行棋范围
		int x_low;
		int x_high;
		int y_low;
		int y_high;
		boolean choose = false;
		if ((x - 2) < 0)
			x_low = 0;
		else
			x_low = x - 2;
		if ((y - 2) < 0)
			y_low = 0;
		else
			y_low = y - 2;
		if ((x + 2) > 6)
			x_high = 6;
		else
			x_high = x + 2;
		if ((y + 2) > 6)
			y_high = 6;
		else
			y_high = y + 2;
		// 判断当前白棋是否存在可行域
		for (int i = x_low; i <= x_high; i++) {
			for (int j = y_low; j <= y_high; j++) {
				if (now[i][j] == 0) {
					choose = true;
				}
			}
		}
		// 不存在可行域则退出
		if (!choose) {
			return;
		} else {
			// System.out.println("fine");
		}
		// 若存在可行域，则对每一种行棋方案计算行棋结果
		for (int i = x_low; i <= x_high; i++) {
			for (int j = y_low; j <= y_high; j++) {
				if (now[i][j] == 0) {
					doing(x, y, i, j);
				}
			}
		}
	}

	// 计算白棋行棋方案的结果并记录最好的行棋方案
	public void doing(int x, int y, int i, int j) {
		// 初始化并生成棋盘拷贝
		int i_low;
		int j_low;
		int i_high;
		int j_high;
		int sum = 0;
		;
		int[][] t = new int[7][7];
		Integer[][] temp = new Integer[7][7];

		for (int a = 0; a < 7; a++) {
			for (int b = 0; b < 7; b++) {
				t[a][b] = now[a][b];
			}
		}
		// 计算新落子的影响范围
		if ((i - 1) < 0)
			i_low = 0;
		else
			i_low = i - 1;
		if ((j - 1) < 0)
			j_low = 0;
		else
			j_low = j - 1;
		if ((i + 1) > 6)
			i_high = 6;
		else
			i_high = i + 1;
		if ((j + 1) > 6)
			j_high = 6;
		else
			j_high = j + 1;
		// 计算新落子后的棋盘状态，变化遵循游戏规则
		if (Math.abs(i - x) > 1 || Math.abs(j - y) > 1) {
			t[i][j] = 2;
			t[x][y] = 0;
		} else {
			t[i][j] = 2;
		}
		for (int a = i_low; a <= i_high; a++) {
			for (int b = j_low; b <= j_high; b++) {
				if (t[a][b] == 1) {
					t[a][b] = 2;
				}
			}
		}
		// 统计新落子后的棋盘白棋总数
		for (int a = 0; a < 7; a++) {
			for (int b = 0; b < 7; b++) {
				if (t[a][b] == 2)
					sum++;
			}
		}
		// 若当前方案的棋盘白棋总数大于记录的最优值
		if (sum > max_now) {
			choose.clear();// 清空记录
			// 将当前方案记录为最好的方案
			for (int a = 0; a < 7; a++) {
				for (int b = 0; b < 7; b++) {
					temp[a][b] = new Integer(t[a][b]);
				}
			}
			// 生成Nodes类保存行棋方案
			Nodes node = new Nodes(temp, x, y, i, j);
			choose.add(node);// 将行棋方案记录在数组中
			max_now = sum;// 更新记录最优值
		}
		// 若当前方案的棋盘白棋总数等于记录的最优值，将记录加入数组作为备选方案
		else if (sum == max_now) {
			for (int a = 0; a < 7; a++) {
				for (int b = 0; b < 7; b++) {
					temp[a][b] = new Integer(t[a][b]);
				}
			}
			Nodes node = new Nodes(temp, x, y, i, j);
			choose.add(node);
		}
	}

	// 从最优方案数组中随机选择一种方案返回
	public Nodes getResult() {
		return choose.get((int) (choose.size() * Math.random()));
	}
}
