import java.sql.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.*;
import javax.swing.border.Border;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import javax.sound.sampled.*;

class kbc_copy extends JFrame implements ActionListener {
    JLabel l1, l2;
    JTextField t1;
    JButton b1;
    Font f, f1;

    kbc_copy() {
        setVisible(true);
        setLocation(600, 300);
        setLayout(null);
        setSize(500, 500);
        f = new Font("Arial", Font.BOLD, 18);
        f1 = new Font("Monotonic", Font.ITALIC, 15);
        l1 = new JLabel("Name");
        l2 = new JLabel("All The Best!");
        t1 = new JTextField();
        b1 = new JButton("Submit");
        l1.setFont(f);
        b1.setFont(f);
        t1.setFont(f1);
        l2.setFont(f);
        add(t1);
        add(l1);
        add(b1);
        add(l2);
        b1.addActionListener(this);
        l1.setBounds(100, 100, 100, 50);
        t1.setBounds(210, 100, 220, 40);
        l2.setBounds(170, 220, 200, 50);
        b1.setBounds(180, 280, 150, 40);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String args[]) {
        new kbc_copy();
    }

    public void actionPerformed(ActionEvent aee) {
        if (aee.getSource() == b1) {
            Connection cn = null;
            PreparedStatement prstm = null;
            ResultSet rs = null;
            String name = null;
            int userId = 0;
            try {
                name = t1.getText();
                cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kbc", "root", "1234a");
                prstm = cn.prepareStatement("INSERT INTO user(name) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
                prstm.setString(1, name);
                prstm.executeUpdate();
                rs = prstm.getGeneratedKeys();
                if (rs.next()) {
                    userId = rs.getInt(1);  // Get the generated user ID
                }
            } catch (Exception e) {
                System.out.println("error in inserting");
                e.printStackTrace();
            } finally {
                // Clean up resources
                try {
                    if (rs != null) rs.close();
                    if (prstm != null) prstm.close();
                    if (cn != null) cn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            new KBC(userId, name);

            try {
                File file = new File("Kbc Background.wav");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.start();
                clip.close();
                audioStream.close();
            } catch (Exception io) {
            }
        }
    }
}

class KBC extends JFrame implements ActionListener {
    JButton b1, b2;
    JLabel background;
    Font f;
    int userid;
    String name = null;

    KBC(int userid, String name) {
        this.name = name;
        this.userid = userid;
        f = new Font("Arial", Font.BOLD, 22);
        b1 = new JButton("Start New Game");
        b2 = new JButton("History");
        Border border = BorderFactory.createLineBorder(Color.WHITE);
        b1.setBorder(border);
        b2.setBorder(border);
        setSize(2000, 1100);
        setVisible(true);
        setLocation(10, 10);
        ImageIcon img = new ImageIcon("KBC.jpg");
        background = new JLabel(img);
        setContentPane(background);
        setLayout(null);
        setSize(1999, 999);
        setSize(2000, 1100);
        background.add(b1);
        background.add(b2);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        background.setBounds(0, 0, 2000, 1100);
        b1.setBounds(800, 850, 250, 50);
        b2.setBounds(1600, 50, 250, 50);
        b1.setFont(f);
        b2.setFont(f);
        b1.setBackground(new Color(40, 25, 60));
        b1.addActionListener(this);
        b1.setForeground(Color.lightGray);
        b2.setBackground(new Color(40, 25, 60));
        b2.addActionListener(this);
        b2.setForeground(Color.lightGray);
        Connection cn = null;
        PreparedStatement prstm = null;
        try {
            cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kbc", "root", "1234a");
            prstm = cn.prepareStatement("update first set flag=0");
            prstm.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (prstm != null) prstm.close();
                if (cn != null) cn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == b1) {
            KBCA1 obj = new KBCA1(userid, name);
            obj.load();
        } else if (e.getSource() == b2) {
            new History(userid, name);
        }
    }
}

class KBCA1 extends JFrame implements ActionListener,Runnable
{
	public static int z;
	public static int x;
	JLabel a[];
	JLabel background2,l1,timer,lifeline;
	JButton back,b1,b2,b3,b4,next,l4,l2,l3;
	Random r=new Random();
	int rn,flag=2;
	String ans,option="";
	Font f1,f2,f3;
	PreparedStatement prstm;
	Connection cn;
	ResultSet rst,rs;
	Statement stm;
	Thread t;
	String amount; 
	static int dd=0;
	int userId;
	String name;
	KBCA1(int userid,String name)
	{
		this.name=name;
		userId= userid;
		z=0;
		x=50;
		setTitle("KBC");
		setSize(2000,1100);
		setVisible(true);
		setLocation(0,0);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1999,999);
		setSize(2000,1100);
		setFocusable(false);
		Border border=BorderFactory.createLineBorder(Color.BLACK);
		Border border1=BorderFactory.createLineBorder(Color.WHITE);
		f1=new Font("Arial",Font.BOLD,30);
		f2=new Font("Arial",Font.BOLD,18);
		a=new JLabel[17];
		ImageIcon img=new ImageIcon("kbcFinal1.png");
		background2=new JLabel(img);
		back=new JButton("End Game");
		lifeline=new JLabel("Lifelines");
		background2.add(lifeline);
		lifeline.setFont(f1);
		lifeline.setBounds(1400,30,300,25);
		lifeline.setForeground(Color.lightGray);
		l1=new JLabel();
		b1=new JButton();
		b2=new JButton();
		b3=new JButton();
		b4=new JButton();
		l4=new JButton("50-50");
		l2=new JButton("Expert");
		l3=new JButton("Double dip");
		l4.setFont(f2);
		l4.setBounds(1250,70,100,25);
		l4.setBackground(Color.PINK);
		l4.addActionListener(this);
		l2.setFont(f2);
		l2.setBounds(1380,70,120,25);
		l2.setBackground(Color.PINK);
		l2.addActionListener(this);
		l3.setFont(f2);
		l3.setBounds(1520,70,150,25);
		l3.setBackground(Color.PINK);
		l3.addActionListener(this);
		background2.add(l2);
		background2.add(l3);
		background2.add(l4);
		next=new JButton("Next");
		timer=new JLabel();
		setContentPane(background2);
		setLayout(null);
		background2.add(timer);
		timer.setFont(f1);
		timer.setOpaque(false);
		timer.setForeground(Color.lightGray);
		add(next);
		next.setFont(f1);
		next.setBounds(1650,900,200,60);
		timer.setBounds(850,650,100,100);
		background2.setBounds(0,0,2000,1100);
		l1.setBounds(300,400,1200,150);
		b1.setBounds(300,600,500,80);
		b2.setBounds(1000,600,500,80);
		b3.setBounds(300,730,500,80);
		b4.setBounds(1000,730,500,80);
		l1.setOpaque(true);
		b1.setOpaque(true);
		b2.setOpaque(true);
		b3.setOpaque(true);
		b4.setOpaque(true);
		l1.setBackground(new Color(125,50,255));
		l1.setForeground(Color.lightGray);
		background2.add(l1);
		b1.setBackground(new Color(140,50,255));
		b1.setForeground(Color.lightGray);
		background2.add(b1);
		b2.setBackground(new Color(140,50,255));
		b2.setForeground(Color.lightGray);
		background2.add(b2);
		b3.setBackground(new Color(140,50,255));
		b3.setForeground(Color.lightGray);
		background2.add(b3);
		b4.setBackground(new Color(140,50,255));
		b4.setForeground(Color.lightGray);
		background2.add(b4);
		background2.add(back);
		back.setBounds(20,30,130,40);
		b1.addActionListener(this);
		b2.addActionListener(this);
		b3.addActionListener(this);
		b4.addActionListener(this);
		back.addActionListener(this);
		next.addActionListener(this);
		back.setBorder(border);
		l1.setBorder(border);
		b1.setBorder(border1);
		b2.setBorder(border1);
		b3.setBorder(border1);
		b4.setBorder(border1);
		back.setFont(f2);
		back.setBackground(new Color(150,30,20));
		back.setForeground(Color.lightGray);
		next.setEnabled(false);


		for(int i=0;i<=16;i++)
		{
			a[i]=new JLabel(""+(i+1)+"    "+x);
			background2.add(a[i]);
			a[i].setFont(f2);
			a[i].setOpaque(false);
			a[i].setForeground(Color.lightGray);
			x+=x;
		}
		a[0].setBounds(1700,510,150,20);
		a[1].setBounds(1700,480,150,20);
		a[2].setBounds(1700,450,150,20);
		a[3].setBounds(1700,420,150,20);
		a[4].setBounds(1700,390,150,20);
		a[5].setBounds(1700,360,150,20);
		a[6].setBounds(1700,330,150,20);
		a[7].setBounds(1700,300,150,20);
		a[8].setBounds(1700,270,150,20);
		a[9].setBounds(1700,240,150,20);
		a[10].setBounds(1700,210,150,20);
		a[11].setBounds(1700,180,150,20);
		a[12].setBounds(1700,150,150,20);
		a[13].setBounds(1700,120,150,20);
		a[14].setBounds(1700,90,150,20);
		a[15].setBounds(1700,60,150,20);
		a[16].setBounds(1700,30,150,20);
		t=new Thread();
	}

	public void load()
	{
		int SrNo;
		try{
			cn=DriverManager.getConnection("jdbc:mysql://Localhost:3306/kbc","root","1234a");
			stm=cn.createStatement();

			do{
				rn=r.nextInt(16);
				String query="select SrNo,flag from first where SrNo="+rn+""; 
				rst=stm.executeQuery(query);
				rst.next();
				SrNo=rst.getInt(1);
				flag=rst.getInt(2);
			}while(flag!=0);
			rs=stm.executeQuery("select * from first where SrNo="+SrNo);
			rs.next();
			l1.setText(""+rs.getString(2));
			l1.setFont(f1);
			b1.setText("A.  "+rs.getString(4));
			b1.setFont(f1);
			b2.setText("B.  "+rs.getString(5));
			b2.setFont(f1);
			b3.setText("C.  "+rs.getString(6));
			b3.setFont(f1);
			b4.setText("D.  "+rs.getString(7));
			b4.setFont(f1);
			ans=rs.getString(3);
			prstm=cn.prepareStatement("update first set flag=1 where SrNo="+SrNo+"");
			prstm.execute();
		}catch(Exception e)
		{
			e.printStackTrace();
		}

		a[z].setOpaque(true);
		a[z].setBackground(new Color(170,50,10));
		t=new Thread(this);
		t.start();
		next.setEnabled(false);
	}


	public void actionPerformed(ActionEvent ae) 
	{
		JButton btn=new JButton();
		if(ae.getSource()==back)
		{
			int ans=JOptionPane.showConfirmDialog(null,"Do you really want to exit?");
			if(ans==0)
			{
				new KBC(userId,name);
			}
			if(t.isAlive())
			{
				t.suspend();
			}
		}

		else if(ae.getSource()==l3)
		{
			dd=1;
		}

		else if(ae.getSource()==b1||ae.getSource()==b2||ae.getSource()==b3||ae.getSource()==b4)
		{
			next.setEnabled(true);
			option=ae.getActionCommand();
			btn=(JButton)ae.getSource();
			if(ans.equals(option))
			{
				btn.setBackground(new Color(40,150,55));
				a[z].setBackground(new Color(20,140,30));
				if(z>15)
				{
					t.suspend();
					amount=((a[z].getText()).substring(3));
					JOptionPane.showMessageDialog(null,"Congratulations!\nYou have won Rs "+amount);
					try {
						cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kbc", "root", "1234a");
						prstm = cn.prepareStatement("INSERT INTO winnings (user_id, amount_won) VALUES (?, ?)");
						prstm.setInt(1, userId);  // Insert user ID
						prstm.setString(2, amount);  // Insert amount won
						prstm.executeUpdate();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					System.exit(0);
				}
				z++;
			}
			else
			{
				if(dd==0)
				{
					btn.setBackground(new Color(150,30,20));
					t.suspend();
					JOptionPane.showMessageDialog(null,"Better luck next time","Error",JOptionPane.ERROR_MESSAGE);
					if(z>0)
					{
						amount=((a[z-1].getText()).substring(3));
						JOptionPane.showMessageDialog(null,"You have won Rs "+amount);
						try {
							cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kbc", "root", "1234a");
							prstm = cn.prepareStatement("INSERT INTO winnings (user_id, amount_won) VALUES (?, ?)");
							prstm.setInt(1, userId);  // Insert user ID
							prstm.setString(2, amount);  // Insert amount won
							prstm.executeUpdate();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					new KBC(userId,name);
				}
				else
				{
					btn.setBackground(new Color(150,30,20));
					t.suspend();
					JOptionPane.showMessageDialog(null,"One more chance\n to select correct answer");
					Thread t1=new Thread(this);
					t1.start();
					btn.setBackground(new Color(140,50,255));
					l3.setEnabled(false);
					dd=0;
				}

			}
			if(t.isAlive())
			{
				t.suspend();
			}
		}
		else if(ae.getSource()==next)
		{
			load();
			b1.setBackground(new Color(140,50,255));
			b2.setBackground(new Color(140,50,255));
			b3.setBackground(new Color(140,50,255));
			b4.setBackground(new Color(140,50,255));
		}
		else if(ae.getSource()==l2)
		{
			try{
				ans=rs.getString(3);
				String ans1=ans.substring(4);
				if(ans1.equals(rs.getString(4)))
				{
					b1.setBackground(new Color(40,150,55));
				}
				else if(ans1.equals(rs.getString(5)))
				{
					b2.setBackground(new Color(40,150,55));
				}
				else if(ans1.equals(rs.getString(6)))
				{
					b3.setBackground(new Color(40,150,55));
				}
				else if(ans1.equals(rs.getString(7)))
				{
					b4.setBackground(new Color(40,150,55));
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			l2.setEnabled(false);

		}
		else if(ae.getSource()==l4)
		{
			try{
				ans=rs.getString(3);
				String ans1=ans.substring(4);
				if(ans1.equals(rs.getString(4))||ans1.equals(rs.getString(7)))
				{
					b3.setText("");
					b2.setText("");
				}
				else if(ans1.equals(rs.getString(5))||ans1.equals(rs.getString(6)))
				{
					b1.setText("");
					b4.setText("");
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			l4.setEnabled(false);

		}
	}

	public void run()
	{
		try{
			for(int i=60;i>=0;i--)
			{
				timer.setText(" "+i);
				Thread.sleep(1000);
			}
			b1.setEnabled(false);
			b2.setEnabled(false);
			b3.setEnabled(false);
			b4.setEnabled(false);
			next.setEnabled(false);
			amount=((a[z-1].getText()).substring(3));
			JOptionPane.showMessageDialog(null,"Time UP\n You have won Rs "+amount);
			try {
				cn = DriverManager.getConnection("jdbc:mysql://localhost:3306/kbc", "root", "1234a");
				prstm = cn.prepareStatement("INSERT INTO winnings (user_id, amount_won) VALUES (?, ?)");
				prstm.setInt(1, userId);  // Insert user ID
				prstm.setString(2, amount);  // Insert amount won
				prstm.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.exit(0);
		}
		catch(Exception e){
		}			
	}
}

class History extends JFrame {
    public History(int userId, String name) {
        Object[][] data = getUserWinningHistory(userId, name);
        Object[] colnames = {"userID", "Amount won"};
        JTable jt = new JTable(data, colnames);
        JScrollPane js = new JScrollPane(jt);
        add(js);
        setSize(600, 600);
        setVisible(true);
    }

    private Object[][] getUserWinningHistory(int userId, String name) {
        ArrayList<Object[]> rowData = new ArrayList<>();
        String query = "SELECT * FROM winnings WHERE user_id IN (SELECT id FROM user WHERE name=?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = connect();
            pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();

            if (rs == null) {
                JOptionPane.showMessageDialog(null, "No History to show");
            } else {
                while (rs.next()) {
                    int userID = rs.getInt("user_id");
                    int amountWon = Integer.parseInt(rs.getString("amount_won").trim());
                    rowData.add(new Object[]{userID, amountWon});
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving data: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return rowData.toArray(new Object[0][0]);
    }

    private Connection connect() {
        String url = "jdbc:mysql://localhost:3306/kbc";
        String username = "root";
        String password = "1234a";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        return conn;
    }
}

