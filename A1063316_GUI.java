import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class A1063316_GUI extends JFrame implements ActionListener, Runnable{
	public static final int WIDTH=700;
	public static final int HEIGHT=700;
	public static int round=1;
	public static JButton ExitButton, saveButton, loadButton, DiceButton;
	public static JLabel DisplayDice, RoundNumber, TurnPlayer;
	public static JLabel characterLabel[], label[];
	public static Image characterImgs[];
	public static ImageIcon image[];
	public static int oldLocationGUI=0, newLocationGUI=0; //location before/after dice
	public static int denominatorTime; //Thread sleep denominatorTime
	public static int x = 0, y=0; //x,y walkcount
	public static int turnGUI=0; //=turn
	public static int location[][] ; // 19 map xy
	public static int walk=0;
	Image offScreenImage = null;
	public int[][] characterLocation= {{560,585},{595,585},{560,625},{595,625}};
	public static boolean first=true;

	public A1063316_GUI() {
		super();

		if(first==true) {
			startFrame start=new startFrame();
			start.setVisible(true);
		}
		if(first!=true){
			setSize(WIDTH,HEIGHT);
			setResizable(false);  //Let the user cannot change the frame size.
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLayout(new BorderLayout());
	
			JPanel upbiggerPanel = new JPanel(new GridLayout(1,6));
			upbiggerPanel.setPreferredSize(new Dimension(660,50));
			upbiggerPanel.setBackground(Color.WHITE);
			
			JPanel UpLeftPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,25));
			UpLeftPanel.setBackground(Color.WHITE);
			JPanel UpRightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,25));
			UpRightPanel.setBackground(Color.WHITE);
		
			JPanel CenterPanel = new JPanel();
			CenterPanel.setLayout(new BorderLayout());
	
			JPanel RightPanel = new JPanel();
			RightPanel.setPreferredSize(new Dimension(40,700));
			RightPanel.setBackground(Color.WHITE);
			
			JPanel LeftPanel = new JPanel();
			LeftPanel.setPreferredSize(new Dimension(40,700));
			LeftPanel.setBackground(Color.WHITE);
			
			JPanel DownPanel = new JPanel();
			DownPanel.setBackground(Color.WHITE);
			DownPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
			
			ExitButton = new JButton("Exit");
			ExitButton.setPreferredSize(new Dimension(70,20));
			ExitButton.addActionListener(this);
			DownPanel.add(ExitButton);
			
			add(upbiggerPanel, BorderLayout.NORTH);
			add(RightPanel, BorderLayout.EAST);
			add(LeftPanel, BorderLayout.WEST);
			add(DownPanel, BorderLayout.SOUTH);
			
	//		save button
			saveButton = new JButton("save");
			saveButton.setPreferredSize(new Dimension(80,25));
			saveButton.addActionListener(this);
			UpLeftPanel.add(saveButton);
			upbiggerPanel.add(UpLeftPanel);
			
	//		load button
			loadButton = new JButton("load");
			loadButton.setPreferredSize(new Dimension(80,25));
			loadButton.addActionListener(this);
			UpRightPanel.add(loadButton);
			upbiggerPanel.add(UpRightPanel);
			
			characterLabel = new JLabel[A1063316_Checkpoint6.roleCount];
			for(int i=0;i<A1063316_Checkpoint6.roleCount;i++) {
				characterLabel[i]=new JLabel("<html>Character "+A1063316_Checkpoint6.role[i].CHARACTER_NUMBER+"<br>"+" "
											+String.valueOf(A1063316_Checkpoint6.role[i].money)+"</html>", JLabel.CENTER); 
				characterLabel[i].setFont(new Font("Arial", Font.BOLD, 15));
				upbiggerPanel.add(characterLabel[i]);
			}
			
	//		image 
			image = new ImageIcon[20];
			for(int i=0;i<=19;i++) {
				image[i] = new ImageIcon(i+".png");
			}
			
	//		label add image
			label = new JLabel[20];
			for(int i=0;i<=19;i++) {
				label[i] = new JLabel(image[i]);
			}
			
	//		draw character		
			characterImgs = new Image[A1063316_Checkpoint6.roleCount+2];
			for(int m=0;m<A1063316_Checkpoint6.roleCount;m++) {
				try {
					characterImgs[A1063316_Checkpoint6.role[m].CHARACTER_NUMBER]=
							ImageIO.read(new File("Character_"+A1063316_Checkpoint6.role[m].CHARACTER_NUMBER+".png"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		
	//		Down
			JPanel CenterDownPanel = new JPanel();
			CenterDownPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
			CenterDownPanel.setBackground(Color.WHITE);
			
			for(int i=5;i>=0;i--) {
				CenterDownPanel.add(label[i]);
			}
			CenterPanel.add(CenterDownPanel, BorderLayout.SOUTH);
	
	//		Up
			JPanel CenterUpPanel = new JPanel();
			CenterUpPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 1));
			CenterUpPanel.setBackground(Color.WHITE);
			
			for(int h=10;h<16;h++) {
				CenterUpPanel.add(label[h]);
			}
			CenterPanel.add(CenterUpPanel, BorderLayout.NORTH);
			
	//		Left
			JPanel CenterLeftPanel = new JPanel();
			CenterLeftPanel.setLayout(new GridLayout(4,1,1,1));
			CenterLeftPanel.setPreferredSize(new Dimension(120,670));
			CenterLeftPanel.setBackground(Color.WHITE);
			
			for(int i=9;i>=6;i--) {
				CenterLeftPanel.add(label[i]);
			}
			CenterPanel.add(CenterLeftPanel, BorderLayout.WEST);
		
	//		Right
			JPanel CenterRightPanel = new JPanel();
			CenterRightPanel.setLayout(new GridLayout(4,1,1,1));
			CenterRightPanel.setPreferredSize(new Dimension(120,670));
			CenterRightPanel.setBackground(Color.WHITE);
			
			for(int i=16;i<20;i++) {
				CenterRightPanel.add(label[i]);
			}
			CenterPanel.add(CenterRightPanel, BorderLayout.EAST);
			
			location=new int[20][2];
			location[0][0]=570;
			location[0][1]=640;
			
	//		up & down location
			int count=6;
			for(int i=1;i<=4;i++) {
				location[i][0]=570-45*(2*i-1)-60;
				location[i+(2*count+1)][0]=570-45*(2*i-1)-60;
				location[i][1]=640;
				location[i+(2*count+1)][1]=168;
				count--;
			}
			
			location[5][0]=570-(45*8+120);
			location[5][1]=640;
			
	//		right&left location	
			count=6;
			int countother=1;
			for(int i=6;i<=9;i++) {
				location[i][0]=90;
				location[i+(2*count+1)][0]=570;
				location[i][1]=640-60-44*(2*countother-1);
				location[i+(2*count+1)][1]=640-60-44*(2*countother-1);
				countother++;
				count--;
			}
			
			location[10][0]=570-(45*8+120);
			location[10][1]=640-60-44*8-60;
			location[15][0]=570;
			location[15][1]=640-60-44*8-60;
	
	//		center part
			JPanel CenterCenterPanel = new JPanel(new FlowLayout());
			CenterPanel.add(CenterCenterPanel,BorderLayout.CENTER);
			CenterCenterPanel.setBackground(Color.WHITE);
			
			ImageIcon DiceImage = new ImageIcon("Dice.png");
			ImageIcon Title = new ImageIcon("title.png");
			ImageIcon DisplayDicenum = new ImageIcon("display_dicenum.png");
	
			DisplayDice =new JLabel() {
				@Override
				public void paintComponent(Graphics g) {
				    super.paintComponent(g);
				    g.drawImage(DisplayDicenum.getImage(),0,0,null);
				    super.paintComponent(g);
					}
			};
			DisplayDice.setPreferredSize(new Dimension(111,100));
			DisplayDice.setText("0");
			DisplayDice.setFont(new Font("Arial", Font.BOLD, 30));
			DisplayDice.setHorizontalAlignment(DisplayDice.CENTER);
			
			DiceButton = new JButton(DiceImage);
			DiceButton.addActionListener(new Dice());
			DiceButton.setOpaque(true);
			DiceButton.setBorder(null);
			DiceButton.setContentAreaFilled(false);
			
			JLabel DiceTitle = new JLabel(Title);
			RoundNumber = new JLabel("Round"+round);
			RoundNumber.setPreferredSize(new Dimension(300,50));
			RoundNumber.setFont(new Font("Arial", Font.BOLD, 20));
	        	RoundNumber.setHorizontalAlignment(RoundNumber.CENTER);
	
			TurnPlayer = new JLabel("Turn Character"+A1063316_Checkpoint6.TurnString[1]);
			TurnPlayer.setFont(new Font("Arial", Font.BOLD, 20));
			
			CenterCenterPanel.add(DiceTitle);
			CenterCenterPanel.add(DiceButton);
			CenterCenterPanel.add(DisplayDice);
			CenterCenterPanel.add(RoundNumber);
			CenterCenterPanel.add(TurnPlayer);
	
			CenterPanel.add(CenterCenterPanel, BorderLayout.CENTER);
			add(CenterPanel, BorderLayout.CENTER);
			
			loadLocation();
			A1063316_Checkpoint6.callDB();
			
			for(int i=0;i<=19;i++) {
				if(A1063316_Checkpoint6.land[i].owner!=0) {
					for(int j=0;j<A1063316_Checkpoint6.roleCount;j++) {
						if(A1063316_Checkpoint6.land[i].owner==A1063316_Checkpoint6.role[j].CHARACTER_NUMBER) {
							label[i].setText(String.valueOf(A1063316_Checkpoint6.land[i].owner));
							label[i].setFont(new Font("Arial", Font.BOLD, 40));
							label[i].setVerticalTextPosition(SwingConstants.CENTER);
							label[i].setHorizontalTextPosition(SwingConstants.CENTER);
						}
					}
				}
			}
		}
	}
	
	
	private class Dice implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			DiceButton.setEnabled(false);
			A1063316_Checkpoint6.Random();
			WalkThread();
			DisplayDice.setText(String.valueOf(A1063316_Checkpoint6.dice));
		}
	}

	public void actionPerformed(ActionEvent e) {
		String buttonString = e.getActionCommand() ;
		if(buttonString.equals("save")) {
			try {
				A1063316_Checkpoint6.Save("Character.txt","Land.txt" );
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}else if(buttonString.equals("load")) {
			try {
				setVisible(false);	
				A1063316_Checkpoint6.Load("Character.txt","Land.txt");
				A1063316_GUI demo2=new A1063316_GUI();
			    demo2.setVisible(true);
				loadLocation();
				repaint();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}else if(buttonString.equals("Exit")) {
			System.exit(0);
		}
	}
	
	protected class startFrame extends JFrame implements ActionListener{
		
		private static final int WIDTH=200;
		private static final int HEIGHT=200;
		private JButton startButton,loadButton,ExitButton;
		public startFrame() {
			super();
			setSize(WIDTH,HEIGHT);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setResizable(false);
			setLayout(new GridLayout(3,1));
			setTitle("");
			
			startButton=new JButton("Start");
			startButton.addActionListener(this);
			
			loadButton=new JButton("Load");
			loadButton.addActionListener(this);
			
			ExitButton=new JButton("Exit");
			ExitButton.addActionListener(this);
			
			add(startButton);
			add(loadButton);
			add(ExitButton);
			
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String s = e.getActionCommand();
			if(s.equals("Start")) {
				try {
					File saveFilerole=new File("Character.txt");
					File saveFileland=new File("Land.txt");
					FileWriter fw=new FileWriter(saveFilerole);
					FileWriter fw1=new FileWriter(saveFileland);
						fw.write("Round:1,Turn:1"+"\r\n");
				       for(int u=0;u<4;u++) {
				    	  	fw.write("0,");
				    	  	fw.write((u+1)+",");
				    	  	fw.write("2000,");
				    	  	fw.write("1,");
				    	  	fw.write("Character_"+(u+1)+".png"+"\r\n");
				       }
				       fw.close();
				       fw1.write("LOCATION_NUMBER, owner"+"\r\n");
				       for(int i=1;i<=19;i++) {
				    	   if(i!=5 || i!=10 || i!=15) {
				    		   fw1.write(i+","+"0"+"\r\n");
				    	   }
				       }
				    fw1.close();
					A1063316_Checkpoint6.Load("Character.txt","Land.txt");
					try {
						A1063316_Checkpoint6.callDB();
						A1063316_Checkpoint6.conn.close();
					}catch (SQLException err) {
						err.printStackTrace();
					}
					first=false;
					this.setVisible(false);	
					A1063316_GUI demo1=new A1063316_GUI();
				    demo1.setVisible(true);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if(s.equals("Load")) {
				try {
					first=false;
					A1063316_Checkpoint6.Load("Character.txt","Land.txt");
					this.setVisible(false);	
					A1063316_GUI demo=new A1063316_GUI();
				    demo.setVisible(true);
					loadLocation();
					repaint();
				} catch (IOException e1) {
					JOptionPane.showMessageDialog(A1063316_GUI.this,String.format("Without record."),
									   String.format("Wrong!"), JOptionPane.PLAIN_MESSAGE);
				}
			}
			if(s.equals("Exit")) {
				System.exit(0);
			}
		}
		
	}
	
	public void setCharacterLabel(int who) {
		characterLabel[who].setText("<html>Character "+A1063316_Checkpoint6.role[who].CHARACTER_NUMBER+"<br>"+" "
				+String.valueOf(A1063316_Checkpoint6.role[who].money)+"</html>");
	}   
	
	public void BuyEvent() { 
		if(A1063316_Checkpoint6.land[newLocationGUI].owner==0 && newLocationGUI!=5 && newLocationGUI!=0 && newLocationGUI!=15 && newLocationGUI!=10) {
			if(A1063316_Checkpoint6.role[turnGUI].money>0 && 
					A1063316_Checkpoint6.role[turnGUI].money>A1063316_Checkpoint6.land[newLocationGUI].LAND_PRICE) {
				int result=JOptionPane.showConfirmDialog
						(A1063316_GUI.this,String.format("Do you want to buy this land?\r\n$"+A1063316_Checkpoint6.land[newLocationGUI].LAND_PRICE),
										   String.format("Buy or not?"),JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
				if (result==JOptionPane.YES_OPTION) {
						A1063316_Checkpoint6.role[turnGUI].money-=A1063316_Checkpoint6.land[newLocationGUI].LAND_PRICE;
						setCharacterLabel(turnGUI);
						label[newLocationGUI].setText(String.valueOf(A1063316_Checkpoint6.role[turnGUI].CHARACTER_NUMBER));
						label[newLocationGUI].setFont(new Font("Arial", Font.BOLD, 40));
						label[newLocationGUI].setVerticalTextPosition(SwingConstants.CENTER);
						label[newLocationGUI].setHorizontalTextPosition(SwingConstants.CENTER);
						repaint();
						A1063316_Checkpoint6.land[newLocationGUI].owner=A1063316_Checkpoint6.role[turnGUI].CHARACTER_NUMBER;
				}
			} 
		}else if(A1063316_Checkpoint6.land[newLocationGUI].owner!=0 && 
				 A1063316_Checkpoint6.land[newLocationGUI].owner!=A1063316_Checkpoint6.role[turnGUI].CHARACTER_NUMBER) {
			JOptionPane.showMessageDialog
				(A1063316_GUI.this,String.format("This land is belong to Character_"+A1063316_Checkpoint6.land[newLocationGUI].owner+", Character_"
				+A1063316_Checkpoint6.role[turnGUI].CHARACTER_NUMBER+" should pay $"+A1063316_Checkpoint6.land[newLocationGUI].TOLLS),
				String.format("Pay tolls!!"), JOptionPane.PLAIN_MESSAGE);
			A1063316_Checkpoint6.role[turnGUI].money-=A1063316_Checkpoint6.land[newLocationGUI].TOLLS;
			int gettollsrole=0;
			for(gettollsrole=0;gettollsrole<A1063316_Checkpoint6.roleCount;gettollsrole++) {
				if(A1063316_Checkpoint6.role[gettollsrole].CHARACTER_NUMBER==A1063316_Checkpoint6.land[newLocationGUI].owner) {
					break;
				}
			}
			A1063316_Checkpoint6.role[gettollsrole].money+=A1063316_Checkpoint6.land[newLocationGUI].TOLLS;
			setCharacterLabel(turnGUI);
			setCharacterLabel(gettollsrole);
			}
	}
	
	@Override
	public void update(Graphics g) {
		if(offScreenImage == null)
			offScreenImage = createImage(WIDTH,HEIGHT);
		Graphics goff = offScreenImage.getGraphics();
		paint(goff);
		g.drawImage(offScreenImage,0,0,null);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		for(int draw=0;draw<A1063316_Checkpoint6.roleCount;draw++) {
			if(draw==turnGUI) {
				g.drawImage(characterImgs[A1063316_Checkpoint6.role[turnGUI].CHARACTER_NUMBER],characterLocation[turnGUI][0],characterLocation[turnGUI][1],null);
			}
			if(draw!=turnGUI) {
				g.drawImage(characterImgs[A1063316_Checkpoint6.role[draw].CHARACTER_NUMBER],characterLocation[draw][0],characterLocation[draw][1],null);
			}
		}
	}                                                                                                                                                                                                                                                                                                                                                                                                                            

	@Override
	public void run() {
        walk=oldLocationGUI+1;
		denominatorTime=Math.abs(location[oldLocationGUI][0]-location[newLocationGUI][0])+
						Math.abs(location[oldLocationGUI][1]-location[newLocationGUI][1]);
		for(int gogo=0;gogo<A1063316_Checkpoint6.dice;gogo++) {
        	if(walk==20) {
				walk=0;
			}
        	x=0;
    		y=0;
    		if(location[walk][1]==location[oldLocationGUI][1]) {
   				if(oldLocationGUI==4 || oldLocationGUI==0 || oldLocationGUI==10 || oldLocationGUI==14) {
   					while(x<=115) {
  						if(oldLocationGUI==10 || oldLocationGUI==14) {
    						characterLocation[turnGUI][0]+=1;
   						}else {
    						characterLocation[turnGUI][0]-=1;
   						}
   			           	repaint();
   			        	try {
   			           		if(A1063316_Checkpoint6.dice>=1 && A1063316_Checkpoint6.dice<=3) {
   			           			Thread.sleep(2000/denominatorTime);
   			           		}else if(A1063316_Checkpoint6.dice>3) {
   			           			Thread.sleep(3000/denominatorTime);
   			           		}
   			           	}catch(InterruptedException e) {
   			           		e.printStackTrace();
   			           	}
   						x++;
   					}
   				}else {
   					while(x<=90){
   						if(oldLocationGUI>10 && oldLocationGUI<15) {
    						characterLocation[turnGUI][0]+=1;
   						}else {
   							characterLocation[turnGUI][0]-=1;
   						}
   			           	repaint();
   			           	try {
   			           		if(A1063316_Checkpoint6.dice>=1 && A1063316_Checkpoint6.dice<=3) {
   			           			Thread.sleep(2000/denominatorTime);
   			           		}else if(A1063316_Checkpoint6.dice>3) {
   			           			Thread.sleep(3000/denominatorTime);
   			           		}
   			           	}catch(InterruptedException e) {
   			           		e.printStackTrace();
   			           	}
   						x++;
   					}
   				}
    		}
    		if(location[walk][0]==location[oldLocationGUI][0]) {
    			if(oldLocationGUI==5 || oldLocationGUI==9 || oldLocationGUI==15 || oldLocationGUI==19) {
   					while(y<=118) {
   						if(oldLocationGUI==15 || oldLocationGUI==19) {
   							characterLocation[turnGUI][1]+=1;
   						}else {
   							characterLocation[turnGUI][1]-=1;
   						}
   			           	repaint();
   			           	try {
   			           		if(A1063316_Checkpoint6.dice>=1 && A1063316_Checkpoint6.dice<=3) {
   			           			Thread.sleep(2000/denominatorTime);
   			           		}else if(A1063316_Checkpoint6.dice>3) {
   			           			Thread.sleep(3000/denominatorTime);
   			           		}
   			           	}catch(InterruptedException e) {
   			           		e.printStackTrace();
   			           	}
   						y++;
   					}
   				}else {
   					while(y<=85){
   						if(oldLocationGUI>15 && oldLocationGUI<19) {
    						characterLocation[turnGUI][1]+=1;
   						}else {
   							characterLocation[turnGUI][1]-=1;
   						}
   			           	repaint();
   			           	try {
   			           		if(A1063316_Checkpoint6.dice>=1 && A1063316_Checkpoint6.dice<=3) {
   			           			Thread.sleep(2000/denominatorTime);
   			           		}else if(A1063316_Checkpoint6.dice>3) {
   			           			Thread.sleep(3000/denominatorTime);
   			           		}
   			           	}catch(InterruptedException e) {
   			           		e.printStackTrace();
   			           	}
  						y++;
   					}
   				}
  			}
    		x=0;
    		y=0;
	       	oldLocationGUI++;
			if(oldLocationGUI==20) {
				oldLocationGUI=0;
				A1063316_Checkpoint6.role[turnGUI].money+=2000;
				setCharacterLabel(turnGUI);
			}
			walk=oldLocationGUI+1;
			if(walk==20) {
				walk=0;
			}
        }
		BuyEvent();
		TurnPlayer.setText("Turn Character"+A1063316_Checkpoint6.role[A1063316_Checkpoint6.turn].CHARACTER_NUMBER);
		RoundNumber.setText("Round"+round);
		DiceButton.setEnabled(true);
	}
		
	public void WalkThread() {
		Thread walkThread=new Thread(this);
		walkThread.start();
	}
	
	public void loadLocation() {
		for(int i=0;i<A1063316_Checkpoint6.roleCount;i++) {
			int walkwalkX=0;
			int walkwalkY=0;
			int end=A1063316_Checkpoint6.role[i].location;
				for(int gg=0;gg<end;gg++) {
					if(gg<5) {
						if(gg==0 || gg==4) {
							walkwalkX-=115;
						}else if(gg<4 && gg>0) {
							walkwalkX-=90;
						}
					}else if(gg>=5 && gg<=9) {
						if(gg==5 || gg==9) {
							walkwalkY-=118;
						}else if(gg<9 && gg>5) {
							walkwalkY-=85;
						}
					}else if(gg>=10 && gg<=14) {
						if(gg==10 || gg==14) {
							walkwalkX+=115;
						}else if(gg>10 && gg<14) {
							walkwalkX+=90;
						}
					}else if(gg>=15 && gg<=19) {
						if(gg==15|| gg==19) {
							walkwalkY+=118;
						}else{
							walkwalkY+=85;
						}
					}
				}
				characterLocation[i][0]+=walkwalkX;
				characterLocation[i][1]+=walkwalkY;
			}
		}		
}
