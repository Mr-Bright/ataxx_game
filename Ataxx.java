package The_Final;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import The_Final.Ai.Nodes;

public class Ataxx extends JFrame implements ActionListener {
	static String type;// 记录游戏类型
	static JFrame jf;// 主面板
	static JButton restart;// 重新开始按钮
	static JButton back;// 悔棋按钮
	static JLabel black;// 黑色棋子数标签
	static JLabel white;// 白色棋子数标签
	static JLabel text;
	static JLabel sign;// 当前黑白棋执棋轮次
	static JButton[][] board = new JButton[7][7];// 棋盘面板按钮矩阵
	static int[][] point = new int[7][7];// 棋盘内部计算矩阵
	static int black_count;// 当前面板上黑色棋子数
	static int white_count;// 当前面板上白色棋子数
	// 初始化图标
	static ImageIcon restart_img = new ImageIcon("./images_final/restart.jpg");
	static ImageIcon back_img = new ImageIcon("./images_final/back.jpg");
	static ImageIcon background = new ImageIcon("./images_final/background.jpg");
	static ImageIcon black_point = new ImageIcon("./images_final/black.jpg");
	static ImageIcon white_point = new ImageIcon("./images_final/white.jpg");
	static ImageIcon black_select = new ImageIcon("./images_final/black_select.jpg");
	static ImageIcon white_select = new ImageIcon("./images_final/white_select.jpg");
	static ImageIcon white_next = new ImageIcon("./images_final/white_next.jpg");
	static ImageIcon background_next = new ImageIcon("./images_final/background_next.jpg");
	static ImageIcon b = new ImageIcon("./images_final/b.png");
	static ImageIcon w = new ImageIcon("./images_final/w.png");

	static boolean selected;// 选中状态值
	static int black_white;// 当前黑白棋轮次，1为黑棋，2为白棋
	static int select_x;// 选中位置的横坐标
	static int select_y;// 选中位置的纵坐标

	private class Node {// 内部类Node，记录每一次落子前的棋盘状态和黑白棋轮次，用于悔棋操作
		int[][] a;// 记录棋盘状态
		int black_white;// 记录黑白棋轮次

		public Node(int[][] x, int bool) {
			// TODO Auto-generated constructor stub
			a = x;
			black_white = bool;
		}
	}

	static Stack<Node> st;// 用于栈式存储每一次落子前的游戏状态，用于悔棋操作

	public Ataxx(String str) {// 构造函数，用于创建游戏，str参数决定游戏的类型
		type = str;// 将str参数保存
		selected = false;// 初始化selected为false，即未选中状态
		black_white = 1;// 设置首先为黑棋轮次
		st = new Stack<>();// 初始化记录栈
		jf = new JFrame("Ataxx-" + str);// 初始化主面板
		jf.setResizable(false);// 设置窗口无法调整大小
		jf.setLayout(new FlowLayout());
		// 获取Toolkit类，用于获得屏幕长宽信息
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		// 设置窗口大小，并通过Toolkit类得到的屏幕大小将窗口定位到屏幕中央
		jf.setBounds((int) (toolkit.getScreenSize().getWidth() - 800) / 2,
				(int) (toolkit.getScreenSize().getHeight() - 1050) / 2, 800, 1040);

		// 初始化各显示标签
		black = new JLabel();
		white = new JLabel();
		text = new JLabel("执棋：");
		sign = new JLabel();
		black.setFont(new java.awt.Font("方正字迹-吕建德行楷繁体", 1, 30));
		white.setFont(new java.awt.Font("方正字迹-吕建德行楷繁体", 1, 30));
		text.setFont(new java.awt.Font("方正字迹-吕建德行楷繁体", 1, 40));
		jf.add(black);
		jf.add(white);
		// 设置“重新开始”和“悔棋”按钮的大小，添加图标和事件监听器
		Dimension preferredSize = new Dimension(350, 70);
		Dimension preferredSize1 = new Dimension(100, 100);
		restart = new JButton("重新开始");
		back = new JButton("悔棋");
		// restart.setBorderPainted(false);
		// back.setBorderPainted(false);
		restart.setIcon(restart_img);
		back.setIcon(back_img);
		restart.addActionListener(this);
		back.addActionListener(this);
		restart.setPreferredSize(preferredSize);
		back.setPreferredSize(preferredSize);
		jf.add(restart);
		jf.add(back);
		// 初始化各棋盘按钮，设置大小并添加事件监听器
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				board[i][j] = new JButton();
				board[i][j].setPreferredSize(preferredSize1);
				board[i][j].addActionListener(this);
				board[i][j].setBorderPainted(false);// 取消按钮边框
				jf.add(board[i][j]);
			}
		}
		// 为棋盘内部计算矩阵设置初始状态
		point[0][0] = 1;
		point[0][6] = 2;
		point[6][0] = 2;
		point[6][6] = 1;
		// 添加标记
		jf.add(text);
		jf.add(sign);
		paint();// 根据棋盘内部计算矩阵状态更新棋盘面板矩阵和显示的label信息

		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {// 事件处理函数
		// TODO Auto-generated method stub
		if (e.getSource() == restart) {// 当选择“重新开始”按钮时，转入restart操作
			restart();
			return;
		} else if (e.getSource() == back) {// 当选择“悔棋”按钮时，转入back操作
			back();
			return;
		}
		// 玩家对战模式下，游戏过程处理
		if (type.equals("pvp")) {
			// 若处于未选中状态，进入选择要移动的棋子阶段
			if (!selected) {
				// 找出选中的棋子
				for (int i = 0; i < 7; i++) {
					for (int j = 0; j < 7; j++) {
						if (e.getSource() == board[i][j]) {
							if (point[i][j] == black_white) {// 判断选中的棋子是否为当前黑白棋轮次
								if (black_white == 1)// 判断黑白棋轮次，修改选中棋子的面板图标为被选中状态
									board[i][j].setIcon(black_select);
								else
									board[i][j].setIcon(white_select);
								selected = !selected;// 将选择标志置为已选中
								select_x = i;// 记录选中棋子的横坐标
								select_y = j;// 记录选中棋子的纵坐标
								return;
							}
						}
					}
				}
			}
			// 若处于选中状态，进入落子阶段
			if (selected) {
				// 找出新的下棋位置
				for (int i = 0; i < 7; i++) {
					for (int j = 0; j < 7; j++) {
						if (e.getSource() == board[i][j]) {
							if (i == select_x && j == select_y) { // 若再次点击选中的棋子，则取消选定的棋子
								if (black_white == 1)// 判断黑白棋轮次，修改选中棋子的面板图标为未选中状态
									board[i][j].setIcon(black_point);
								else
									board[i][j].setIcon(white_point);
								selected = !selected;// 将选择标志置为未选中
								return;// 结束，下次点击将进入选择棋子操作
							}
							if (point[i][j] != 0) {// 下棋位置为非空棋盘格则操作无效
								return;
							}
							// 计算下棋位置和选中棋子之间的横纵距离
							int x = Math.abs(i - select_x);
							int y = Math.abs(j - select_y);
							if (x > 2 || y > 2) {// 若横纵距离大于2，则为无效操作
								return;
							} else if (x > 1 || y > 1) {// 若横纵距离其中存在等于2，则执行跳棋操作
								select();// 保存当前棋盘状态
								if (black_white == 1) {// 判断黑白棋下棋轮次
									point[i][j] = 1;// 修改棋盘内部计算矩阵中下棋位置为新落子位置
									point[select_x][select_y] = 0;// 修改棋盘内部计算矩阵中选中棋子位置为空棋盘格
									compute(i, j);// 计算新落子后棋盘对应的变化
									black_white = 2;// 改变黑白棋轮次
								} else {// 白棋操作如上
									point[i][j] = 2;
									point[select_x][select_y] = 0;
									compute(i, j);
									black_white = 1;
								}
							} else {// 若横纵距离均为1，则执行下棋操作
								select();// 保存当前棋盘状态
								if (black_white == 1) {// 判断黑白棋下棋轮次
									point[i][j] = 1;// 修改棋盘内部计算矩阵中下棋位置为新落子位置
									compute(i, j);// 计算新落子后棋盘对应的变化
									black_white = 2;// 改变黑白棋轮次
								} else {// 白棋操作如上
									point[i][j] = 2;
									compute(i, j);
									black_white = 1;
								}
							}

						}
					}
				}
				selected = !selected;// 操作完成后，将选择状态置为未选中状态
				paint();// 根据棋盘内部计算矩阵状态更新棋盘面板矩阵和显示的label信息
				return;
			}
		}
		// 人机对战模式下，游戏过程处理
		else if (type.equals("pve")) {
			if (black_white == 1) {// 判断黑白棋轮次，玩家执黑棋，电脑执白棋
				// 选择棋子阶段操作与玩家对战模式下相同
				if (!selected) {
					paint();
					for (int i = 0; i < 7; i++) {
						for (int j = 0; j < 7; j++) {
							if (e.getSource() == board[i][j]) {
								if (point[i][j] == black_white) {
									board[i][j].setIcon(black_select);
									selected = !selected;
									select_x = i;
									select_y = j;
									return;
								}
							}
						}
					}
				}
				// 黑棋落子阶段与玩家对战模式下相同
				if (selected) {
					for (int i = 0; i < 7; i++) {
						for (int j = 0; j < 7; j++) {
							if (e.getSource() == board[i][j]) {
								if (i == select_x && j == select_y) { // 取消选定的棋子
									board[i][j].setIcon(black_point);
									selected = !selected;
									return;
								}
								if (point[i][j] != 0) {
									return;
								}
								int x = Math.abs(i - select_x);
								int y = Math.abs(j - select_y);
								if (x > 2 || y > 2) {
									return;
								} else if (x > 1 || y > 1) {
									select();
									point[i][j] = 1;
									point[select_x][select_y] = 0;
									compute(i, j);
									black_white = 2;

								} else {
									select();
									point[i][j] = 1;
									compute(i, j);
									black_white = 2;
								}

							}
						}
					}
					selected = !selected;
					paint();
					// 黑棋行棋结束，由电脑执白棋下棋
					if (black_white == 2) {
						select();// 保存当前棋盘状态
						Ai ai = new Ai(point);// 根据当前棋盘状态创建Ai类，计算下一步白棋位置
						Nodes nodes = ai.getResult();// 得到Ai类计算的结果，用Nodes类保存
						// 从Nodes类中获取白棋下棋后的棋盘内部计算矩阵
						for (int i = 0; i < 7; i++) {
							for (int j = 0; j < 7; j++) {
								point[i][j] = nodes.n[i][j].intValue();
							}
						}
						black_white = 1;// 将黑白棋轮次置为黑棋
						paint();// 根据棋盘内部计算矩阵状态更新棋盘面板矩阵和显示的label信息
						// 根据Nodes中的数据绘制白棋的选中棋子和落子位置
						if (Math.abs(nodes.x_select - nodes.x_next) > 1
								|| Math.abs(nodes.y_select - nodes.y_next) > 1) {
							board[nodes.x_select][nodes.y_select].setIcon(background_next);
							board[nodes.x_next][nodes.y_next].setIcon(white_next);
						} else {
							board[nodes.x_select][nodes.y_select].setIcon(white_select);
							board[nodes.x_next][nodes.y_next].setIcon(white_next);
						}
					}
					return;
				}
			}
		}
	}

	// 测试执行main函数
	public static void main(String[] args) {
		Ataxx ataxx = new Ataxx("pve");
	}

	// 根据棋盘内部计算矩阵状态更新棋盘面板矩阵和显示的label信息
	public void paint() {
		// 初始化黑白棋计数
		black_count = 0;
		white_count = 0;
		// 根据棋盘内部计算矩阵状态更新棋盘面板矩阵按钮的图标
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				if (point[i][j] == 1) {
					board[i][j].setIcon(black_point);
					black_count++;
				} else if (point[i][j] == 2) {
					board[i][j].setIcon(white_point);
					white_count++;
				} else {
					board[i][j].setIcon(background);
				}
			}
		}
		// 根据黑白棋轮次更新执棋信息
		if (black_white == 1) {
			sign.setIcon(b);
		} else {
			sign.setIcon(w);
		}
		// 更新黑白棋当前棋子数标签
		black.setText("                                     黑色棋子：" + black_count
				+ "                                                                                 \n");
		white.setText("                                     白色棋子：" + white_count
				+ "                                                                                 \n");
		// 判断胜负
		if ((black_count + white_count) == 49) {// 若棋盘被全部占满，则棋子数目较多一方获胜
			if (black_count > white_count)
				JOptionPane.showMessageDialog(null, "游戏结束，黑棋胜利");
			else
				JOptionPane.showMessageDialog(null, "游戏结束，白棋胜利");
			restart();// 重新开始操作
		}
		if (black_count == 0 || white_count == 0) {// 若一方无棋，则另一方胜利
			if (white_count == 0)
				JOptionPane.showMessageDialog(null, "游戏结束，黑棋胜利");
			else
				JOptionPane.showMessageDialog(null, "游戏结束，白棋胜利");
			restart();
		}
	}

	public void compute(int x, int y) {// 根据新落子的位置计算棋盘内部计算矩阵变化
		// 计算新落子位置产生的变化范围
		int x_low;
		int x_high;
		int y_low;
		int y_high;
		if (x - 1 < 0)
			x_low = 0;
		else
			x_low = x - 1;
		if (x + 1 > 6)
			x_high = 6;
		else
			x_high = x + 1;
		if (y - 1 < 0)
			y_low = 0;
		else
			y_low = y - 1;
		if (y + 1 > 6)
			y_high = 6;
		else
			y_high = y + 1;
		// 对变化范围内的棋子进行操作
		for (int i = x_low; i <= x_high; i++) {
			for (int j = y_low; j <= y_high; j++) {
				if (point[i][j] != 0)// 当变化范围内存在另一方棋子时，将其转化为当前落子的棋子
					point[i][j] = black_white;
			}
		}
	}

	// 重新开始操作
	public void restart() {
		// 将棋盘内部计算矩阵恢复为初始值
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				point[i][j] = 0;
			}
		}
		point[0][0] = 1;
		point[0][6] = 2;
		point[6][0] = 2;
		point[6][6] = 1;
		selected = false;// 将选择标志置为未选中状态
		black_white = 1;// 黑白棋轮次为黑棋
		st.clear();// 清空历史记录栈
		paint();// 根据棋盘内部计算矩阵状态更新棋盘面板矩阵和显示的label信息
	}

	// 悔棋操作
	public void back() {
		if (st.isEmpty()) {// 若历史记录栈空，则不进行任何操作
			return;
		} else if (type.equals("pvp")) {// 若游戏模式为玩家对战
			Node node = st.pop();// 弹出上一条棋盘记录
			point = node.a;// 恢复棋盘内部计算矩阵
			black_white = node.black_white;// 恢复上一次黑白棋轮次
			selected = false;// 将选择标志置为未选中状态
			paint();// 根据棋盘内部计算矩阵状态更新棋盘面板矩阵和显示的label信息
		} else if (type.equals("pve")) {// 若游戏模式为人机对战
			Node node = st.pop();// 连续弹出两条记录，跳过白棋行棋记录
			node = st.pop();
			point = node.a;// 恢复棋盘内部计算矩阵
			black_white = node.black_white;// 黑白棋轮次始终为黑棋
			selected = false;// 将选择标志置为未选中状态
			paint();// 根据棋盘内部计算矩阵状态更新棋盘面板矩阵和显示的label信息
		}
	}

	// 保存当前棋盘状态
	public void select() {
		// 新建数组a，将其作为棋盘内部计算矩阵的拷贝
		int[][] a = new int[7][7];
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				a[i][j] = point[i][j];
			}
		}
		// 根据棋盘拷贝和当前黑白棋轮次生成当前棋盘状态，并存入node中
		Node node = new Node(a, black_white);
		// 将记录加入历史数据栈st中
		st.push(node);
	}
}
