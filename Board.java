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

/*Board���ṩ�û���ʼѡ����壬������ʾͬ������Ϸ��Ҷ�ս���˻���ս��ѡ�����
 * 
 * 
 */
public class Board extends JFrame implements ActionListener {
	static JFrame jf;// ��������
	static JButton pvp;// ��Ҷ�սѡ��ť
	static JButton pve;// �˻���սѡ��ť
	static JPanel imagePanel;// ��������
	static ImageIcon background;// ����ͼƬ
	static ImageIcon pvpicon;// ��Ҷ�ս��ťͼ��
	static ImageIcon pveicon;// �˼Ҷ�ս��ťͼ��

	public Board() {
		// ��ʼ������
		jf = new JFrame("��սͬ����");
		jf.setLayout(new BorderLayout());
		// ��ȡToolkit�࣬���ڻ����Ļ������Ϣ
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		// ��ʼ����ͼ��
		background = new ImageIcon("./images_final/board2.jpg");
		pvpicon = new ImageIcon("./images_final/pvp.png");
		pveicon = new ImageIcon("./images_final/pve.png");
		JLabel label = new JLabel(background);
		label.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());// ���ݱ���ͼƬ��С����label��С
		imagePanel = (JPanel) jf.getContentPane();// ���jf�ĵײ㻭��
		imagePanel.setOpaque(false);// ���ײ㻭������Ϊ�����
		imagePanel.setLayout(new FlowLayout());
		jf.getLayeredPane().setLayout(null);
		jf.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));// ��label��ӵ��ײ㣬������˱���ͼƬ
		jf.setBounds((int) (toolkit.getScreenSize().getWidth() - background.getIconWidth()) / 2,
				(int) (toolkit.getScreenSize().getHeight() - background.getIconHeight()) / 2, background.getIconWidth(),
				background.getIconHeight() + 35);// ���ô��ڴ�С����ͨ��Toolkit��õ�����Ļ��С�����ڶ�λ����Ļ����
		jf.setResizable(false);// ����jf��С���ɵ���
		// ��ʼ����ť
		pvp = new JButton("���˶�ս");
		pve = new JButton("�˻���ս");
		// Ϊ��ť���ô�С
		Dimension preferredSize = new Dimension(400, 130);
		pvp.setPreferredSize(preferredSize);
		pve.setPreferredSize(preferredSize);
		// ȡ����ť�ı߿�
		pvp.setBorderPainted(false);
		pve.setBorderPainted(false);
		// ȡ����ť����ɫ���
		pvp.setContentAreaFilled(false);
		pve.setContentAreaFilled(false);
		// Ϊ��ť����ͼ��
		pvp.setIcon(pvpicon);
		pve.setIcon(pveicon);
		// Ϊ��ť����¼�������
		pvp.addActionListener(this);
		pve.addActionListener(this);
		// ����ť��ӵ������
		jf.add(pvp);
		jf.add(pve);

		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//jf.setVisible(false);
		if (e.getSource() == pvp) {// ���û��������Ҷ�ս����ťʱ������Ataxx��ʱ���������pvp��
			new Ataxx("pvp");
		} else
			new Ataxx("pve");// ����ҵ�����˻���ս����ťʱ������Ataxx��ʱ���������pve��
	}

	public static void main(String[] args) {
		new Board();// ���ù��캯��������Board�࣬������Ϸ
	}
}
