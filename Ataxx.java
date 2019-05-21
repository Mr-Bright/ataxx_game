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
	static String type;// ��¼��Ϸ����
	static JFrame jf;// �����
	static JButton restart;// ���¿�ʼ��ť
	static JButton back;// ���尴ť
	static JLabel black;// ��ɫ��������ǩ
	static JLabel white;// ��ɫ��������ǩ
	static JLabel text;
	static JLabel sign;// ��ǰ�ڰ���ִ���ִ�
	static JButton[][] board = new JButton[7][7];// ������尴ť����
	static int[][] point = new int[7][7];// �����ڲ��������
	static int black_count;// ��ǰ����Ϻ�ɫ������
	static int white_count;// ��ǰ����ϰ�ɫ������
	// ��ʼ��ͼ��
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

	static boolean selected;// ѡ��״ֵ̬
	static int black_white;// ��ǰ�ڰ����ִΣ�1Ϊ���壬2Ϊ����
	static int select_x;// ѡ��λ�õĺ�����
	static int select_y;// ѡ��λ�õ�������

	private class Node {// �ڲ���Node����¼ÿһ������ǰ������״̬�ͺڰ����ִΣ����ڻ������
		int[][] a;// ��¼����״̬
		int black_white;// ��¼�ڰ����ִ�

		public Node(int[][] x, int bool) {
			// TODO Auto-generated constructor stub
			a = x;
			black_white = bool;
		}
	}

	static Stack<Node> st;// ����ջʽ�洢ÿһ������ǰ����Ϸ״̬�����ڻ������

	public Ataxx(String str) {// ���캯�������ڴ�����Ϸ��str����������Ϸ������
		type = str;// ��str��������
		selected = false;// ��ʼ��selectedΪfalse����δѡ��״̬
		black_white = 1;// ��������Ϊ�����ִ�
		st = new Stack<>();// ��ʼ����¼ջ
		jf = new JFrame("Ataxx-" + str);// ��ʼ�������
		jf.setResizable(false);// ���ô����޷�������С
		jf.setLayout(new FlowLayout());
		// ��ȡToolkit�࣬���ڻ����Ļ������Ϣ
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		// ���ô��ڴ�С����ͨ��Toolkit��õ�����Ļ��С�����ڶ�λ����Ļ����
		jf.setBounds((int) (toolkit.getScreenSize().getWidth() - 800) / 2,
				(int) (toolkit.getScreenSize().getHeight() - 1050) / 2, 800, 1040);

		// ��ʼ������ʾ��ǩ
		black = new JLabel();
		white = new JLabel();
		text = new JLabel("ִ�壺");
		sign = new JLabel();
		black.setFont(new java.awt.Font("�����ּ�-�������п�����", 1, 30));
		white.setFont(new java.awt.Font("�����ּ�-�������п�����", 1, 30));
		text.setFont(new java.awt.Font("�����ּ�-�������п�����", 1, 40));
		jf.add(black);
		jf.add(white);
		// ���á����¿�ʼ���͡����塱��ť�Ĵ�С�����ͼ����¼�������
		Dimension preferredSize = new Dimension(350, 70);
		Dimension preferredSize1 = new Dimension(100, 100);
		restart = new JButton("���¿�ʼ");
		back = new JButton("����");
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
		// ��ʼ�������̰�ť�����ô�С������¼�������
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				board[i][j] = new JButton();
				board[i][j].setPreferredSize(preferredSize1);
				board[i][j].addActionListener(this);
				board[i][j].setBorderPainted(false);// ȡ����ť�߿�
				jf.add(board[i][j]);
			}
		}
		// Ϊ�����ڲ�����������ó�ʼ״̬
		point[0][0] = 1;
		point[0][6] = 2;
		point[6][0] = 2;
		point[6][6] = 1;
		// ��ӱ��
		jf.add(text);
		jf.add(sign);
		paint();// ���������ڲ��������״̬�����������������ʾ��label��Ϣ

		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {// �¼�������
		// TODO Auto-generated method stub
		if (e.getSource() == restart) {// ��ѡ�����¿�ʼ����ťʱ��ת��restart����
			restart();
			return;
		} else if (e.getSource() == back) {// ��ѡ�񡰻��塱��ťʱ��ת��back����
			back();
			return;
		}
		// ��Ҷ�սģʽ�£���Ϸ���̴���
		if (type.equals("pvp")) {
			// ������δѡ��״̬������ѡ��Ҫ�ƶ������ӽ׶�
			if (!selected) {
				// �ҳ�ѡ�е�����
				for (int i = 0; i < 7; i++) {
					for (int j = 0; j < 7; j++) {
						if (e.getSource() == board[i][j]) {
							if (point[i][j] == black_white) {// �ж�ѡ�е������Ƿ�Ϊ��ǰ�ڰ����ִ�
								if (black_white == 1)// �жϺڰ����ִΣ��޸�ѡ�����ӵ����ͼ��Ϊ��ѡ��״̬
									board[i][j].setIcon(black_select);
								else
									board[i][j].setIcon(white_select);
								selected = !selected;// ��ѡ���־��Ϊ��ѡ��
								select_x = i;// ��¼ѡ�����ӵĺ�����
								select_y = j;// ��¼ѡ�����ӵ�������
								return;
							}
						}
					}
				}
			}
			// ������ѡ��״̬���������ӽ׶�
			if (selected) {
				// �ҳ��µ�����λ��
				for (int i = 0; i < 7; i++) {
					for (int j = 0; j < 7; j++) {
						if (e.getSource() == board[i][j]) {
							if (i == select_x && j == select_y) { // ���ٴε��ѡ�е����ӣ���ȡ��ѡ��������
								if (black_white == 1)// �жϺڰ����ִΣ��޸�ѡ�����ӵ����ͼ��Ϊδѡ��״̬
									board[i][j].setIcon(black_point);
								else
									board[i][j].setIcon(white_point);
								selected = !selected;// ��ѡ���־��Ϊδѡ��
								return;// �������´ε��������ѡ�����Ӳ���
							}
							if (point[i][j] != 0) {// ����λ��Ϊ�ǿ����̸��������Ч
								return;
							}
							// ��������λ�ú�ѡ������֮��ĺ��ݾ���
							int x = Math.abs(i - select_x);
							int y = Math.abs(j - select_y);
							if (x > 2 || y > 2) {// �����ݾ������2����Ϊ��Ч����
								return;
							} else if (x > 1 || y > 1) {// �����ݾ������д��ڵ���2����ִ���������
								select();// ���浱ǰ����״̬
								if (black_white == 1) {// �жϺڰ��������ִ�
									point[i][j] = 1;// �޸������ڲ��������������λ��Ϊ������λ��
									point[select_x][select_y] = 0;// �޸������ڲ����������ѡ������λ��Ϊ�����̸�
									compute(i, j);// ���������Ӻ����̶�Ӧ�ı仯
									black_white = 2;// �ı�ڰ����ִ�
								} else {// �����������
									point[i][j] = 2;
									point[select_x][select_y] = 0;
									compute(i, j);
									black_white = 1;
								}
							} else {// �����ݾ����Ϊ1����ִ���������
								select();// ���浱ǰ����״̬
								if (black_white == 1) {// �жϺڰ��������ִ�
									point[i][j] = 1;// �޸������ڲ��������������λ��Ϊ������λ��
									compute(i, j);// ���������Ӻ����̶�Ӧ�ı仯
									black_white = 2;// �ı�ڰ����ִ�
								} else {// �����������
									point[i][j] = 2;
									compute(i, j);
									black_white = 1;
								}
							}

						}
					}
				}
				selected = !selected;// ������ɺ󣬽�ѡ��״̬��Ϊδѡ��״̬
				paint();// ���������ڲ��������״̬�����������������ʾ��label��Ϣ
				return;
			}
		}
		// �˻���սģʽ�£���Ϸ���̴���
		else if (type.equals("pve")) {
			if (black_white == 1) {// �жϺڰ����ִΣ����ִ���壬����ִ����
				// ѡ�����ӽ׶β�������Ҷ�սģʽ����ͬ
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
				// �������ӽ׶�����Ҷ�սģʽ����ͬ
				if (selected) {
					for (int i = 0; i < 7; i++) {
						for (int j = 0; j < 7; j++) {
							if (e.getSource() == board[i][j]) {
								if (i == select_x && j == select_y) { // ȡ��ѡ��������
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
					// ��������������ɵ���ִ��������
					if (black_white == 2) {
						select();// ���浱ǰ����״̬
						Ai ai = new Ai(point);// ���ݵ�ǰ����״̬����Ai�࣬������һ������λ��
						Nodes nodes = ai.getResult();// �õ�Ai�����Ľ������Nodes�ౣ��
						// ��Nodes���л�ȡ���������������ڲ��������
						for (int i = 0; i < 7; i++) {
							for (int j = 0; j < 7; j++) {
								point[i][j] = nodes.n[i][j].intValue();
							}
						}
						black_white = 1;// ���ڰ����ִ���Ϊ����
						paint();// ���������ڲ��������״̬�����������������ʾ��label��Ϣ
						// ����Nodes�е����ݻ��ư����ѡ�����Ӻ�����λ��
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

	// ����ִ��main����
	public static void main(String[] args) {
		Ataxx ataxx = new Ataxx("pve");
	}

	// ���������ڲ��������״̬�����������������ʾ��label��Ϣ
	public void paint() {
		// ��ʼ���ڰ������
		black_count = 0;
		white_count = 0;
		// ���������ڲ��������״̬��������������ť��ͼ��
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
		// ���ݺڰ����ִθ���ִ����Ϣ
		if (black_white == 1) {
			sign.setIcon(b);
		} else {
			sign.setIcon(w);
		}
		// ���ºڰ��嵱ǰ��������ǩ
		black.setText("                                     ��ɫ���ӣ�" + black_count
				+ "                                                                                 \n");
		white.setText("                                     ��ɫ���ӣ�" + white_count
				+ "                                                                                 \n");
		// �ж�ʤ��
		if ((black_count + white_count) == 49) {// �����̱�ȫ��ռ������������Ŀ�϶�һ����ʤ
			if (black_count > white_count)
				JOptionPane.showMessageDialog(null, "��Ϸ����������ʤ��");
			else
				JOptionPane.showMessageDialog(null, "��Ϸ����������ʤ��");
			restart();// ���¿�ʼ����
		}
		if (black_count == 0 || white_count == 0) {// ��һ�����壬����һ��ʤ��
			if (white_count == 0)
				JOptionPane.showMessageDialog(null, "��Ϸ����������ʤ��");
			else
				JOptionPane.showMessageDialog(null, "��Ϸ����������ʤ��");
			restart();
		}
	}

	public void compute(int x, int y) {// ���������ӵ�λ�ü��������ڲ��������仯
		// ����������λ�ò����ı仯��Χ
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
		// �Ա仯��Χ�ڵ����ӽ��в���
		for (int i = x_low; i <= x_high; i++) {
			for (int j = y_low; j <= y_high; j++) {
				if (point[i][j] != 0)// ���仯��Χ�ڴ�����һ������ʱ������ת��Ϊ��ǰ���ӵ�����
					point[i][j] = black_white;
			}
		}
	}

	// ���¿�ʼ����
	public void restart() {
		// �������ڲ��������ָ�Ϊ��ʼֵ
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				point[i][j] = 0;
			}
		}
		point[0][0] = 1;
		point[0][6] = 2;
		point[6][0] = 2;
		point[6][6] = 1;
		selected = false;// ��ѡ���־��Ϊδѡ��״̬
		black_white = 1;// �ڰ����ִ�Ϊ����
		st.clear();// �����ʷ��¼ջ
		paint();// ���������ڲ��������״̬�����������������ʾ��label��Ϣ
	}

	// �������
	public void back() {
		if (st.isEmpty()) {// ����ʷ��¼ջ�գ��򲻽����κβ���
			return;
		} else if (type.equals("pvp")) {// ����ϷģʽΪ��Ҷ�ս
			Node node = st.pop();// ������һ�����̼�¼
			point = node.a;// �ָ������ڲ��������
			black_white = node.black_white;// �ָ���һ�κڰ����ִ�
			selected = false;// ��ѡ���־��Ϊδѡ��״̬
			paint();// ���������ڲ��������״̬�����������������ʾ��label��Ϣ
		} else if (type.equals("pve")) {// ����ϷģʽΪ�˻���ս
			Node node = st.pop();// ��������������¼���������������¼
			node = st.pop();
			point = node.a;// �ָ������ڲ��������
			black_white = node.black_white;// �ڰ����ִ�ʼ��Ϊ����
			selected = false;// ��ѡ���־��Ϊδѡ��״̬
			paint();// ���������ڲ��������״̬�����������������ʾ��label��Ϣ
		}
	}

	// ���浱ǰ����״̬
	public void select() {
		// �½�����a��������Ϊ�����ڲ��������Ŀ���
		int[][] a = new int[7][7];
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				a[i][j] = point[i][j];
			}
		}
		// �������̿����͵�ǰ�ڰ����ִ����ɵ�ǰ����״̬��������node��
		Node node = new Node(a, black_white);
		// ����¼������ʷ����ջst��
		st.push(node);
	}
}
