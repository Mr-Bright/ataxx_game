package The_Final;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*Board类提供用户初始选择面板，负责显示同化棋游戏玩家对战和人机对战的选择界面
 * 
 * 
 */
public class Board extends JFrame implements ActionListener {
	static JFrame jf;// 基本窗口
	static JButton pvp;// 玩家对战选择按钮
	static JButton pve;// 人机对战选择按钮
	static JPanel imagePanel;// 背景画布
	static ImageIcon background;// 背景图片
	static ImageIcon pvpicon;// 玩家对战按钮图标
	static ImageIcon pveicon;// 人家对战按钮图标

	public Board() {
		// 初始化窗口
		jf = new JFrame("决战同化棋");
		jf.setLayout(new BorderLayout());
		// 获取Toolkit类，用于获得屏幕长宽信息
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		// 初始化各图标
		background = new ImageIcon("./images_final/board2.jpg");
		pvpicon = new ImageIcon("./images_final/pvp.png");
		pveicon = new ImageIcon("./images_final/pve.png");
		JLabel label = new JLabel(background);
		label.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());// 根据背景图片大小设置label大小
		imagePanel = (JPanel) jf.getContentPane();// 获得jf的底层画布
		imagePanel.setOpaque(false);// 将底层画布设置为无填充
		imagePanel.setLayout(new FlowLayout());
		jf.getLayeredPane().setLayout(null);
		jf.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));// 将label添加到底层，即添加了背景图片
		jf.setBounds((int) (toolkit.getScreenSize().getWidth() - background.getIconWidth()) / 2,
				(int) (toolkit.getScreenSize().getHeight() - background.getIconHeight()) / 2, background.getIconWidth(),
				background.getIconHeight() + 35);// 设置窗口大小，并通过Toolkit类得到的屏幕大小将窗口定位到屏幕中央
		jf.setResizable(false);// 设置jf大小不可调整
		// 初始化按钮
		pvp = new JButton("多人对战");
		pve = new JButton("人机对战");
		// 为按钮设置大小
		Dimension preferredSize = new Dimension(400, 130);
		pvp.setPreferredSize(preferredSize);
		pve.setPreferredSize(preferredSize);
		// 取消按钮的边框
		pvp.setBorderPainted(false);
		pve.setBorderPainted(false);
		// 取消按钮的颜色填充
		pvp.setContentAreaFilled(false);
		pve.setContentAreaFilled(false);
		// 为按钮设置图标
		pvp.setIcon(pvpicon);
		pve.setIcon(pveicon);
		// 为按钮添加事件监听器
		pvp.addActionListener(this);
		pve.addActionListener(this);
		// 将按钮添加到面板上
		jf.add(pvp);
		jf.add(pve);

		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//jf.setVisible(false);
		if (e.getSource() == pvp) {// 当用户点击“玩家对战”按钮时，创建Ataxx类时输入参数“pvp”
			new Ataxx("pvp");
		} else
			new Ataxx("pve");// 当玩家点击“人机对战”按钮时，创建Ataxx类时输入参数“pve”
	}

	public static void main(String[] args) {
		new Board();// 调用构造函数，创建Board类，启动游戏
	}
}
