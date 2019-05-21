package The_Final;

import java.util.ArrayList;

public class Ai {
	static int[][] now;// ��Ϊ��ǰ�����ڲ��������Ŀ���
	ArrayList<Nodes> choose;// �������ŵ�����λ��
	static int max_now;// ��ǰ���ŵ����Ӻ������Ŀ
	// Nodes���¼ÿ�ε�ѡ�����Ӻ�����λ��

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

	// ���캯�������ݵ�ǰ�����ڲ�������������õ�����
	public Ai(int[][] point) {
		// ��ʼ��
		now = new int[7][7];
		max_now = 0;
		choose = new ArrayList<>();
		// ��now��Ϊ��ǰ�����ڲ��������Ŀ���
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				now[i][j] = point[i][j];
			}
		}
		// �����������̣��ҳ�ÿһ����������巽������ѡ����õķ���
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				if (now[i][j] == 2) {
					compute(i, j);
				}
			}
		}
	}

	// �������Ŀ�ѡ������
	public void compute(int x, int y) {
		// ����ÿһ������Ŀ����巶Χ
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
		// �жϵ�ǰ�����Ƿ���ڿ�����
		for (int i = x_low; i <= x_high; i++) {
			for (int j = y_low; j <= y_high; j++) {
				if (now[i][j] == 0) {
					choose = true;
				}
			}
		}
		// �����ڿ��������˳�
		if (!choose) {
			return;
		} else {
			// System.out.println("fine");
		}
		// �����ڿ��������ÿһ�����巽������������
		for (int i = x_low; i <= x_high; i++) {
			for (int j = y_low; j <= y_high; j++) {
				if (now[i][j] == 0) {
					doing(x, y, i, j);
				}
			}
		}
	}

	// ����������巽���Ľ������¼��õ����巽��
	public void doing(int x, int y, int i, int j) {
		// ��ʼ�����������̿���
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
		// ���������ӵ�Ӱ�췶Χ
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
		// ���������Ӻ������״̬���仯��ѭ��Ϸ����
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
		// ͳ�������Ӻ�����̰�������
		for (int a = 0; a < 7; a++) {
			for (int b = 0; b < 7; b++) {
				if (t[a][b] == 2)
					sum++;
			}
		}
		// ����ǰ���������̰����������ڼ�¼������ֵ
		if (sum > max_now) {
			choose.clear();// ��ռ�¼
			// ����ǰ������¼Ϊ��õķ���
			for (int a = 0; a < 7; a++) {
				for (int b = 0; b < 7; b++) {
					temp[a][b] = new Integer(t[a][b]);
				}
			}
			// ����Nodes�ౣ�����巽��
			Nodes node = new Nodes(temp, x, y, i, j);
			choose.add(node);// �����巽����¼��������
			max_now = sum;// ���¼�¼����ֵ
		}
		// ����ǰ���������̰����������ڼ�¼������ֵ������¼����������Ϊ��ѡ����
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

	// �����ŷ������������ѡ��һ�ַ�������
	public Nodes getResult() {
		return choose.get((int) (choose.size() * Math.random()));
	}
}
