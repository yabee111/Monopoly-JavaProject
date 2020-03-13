import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class A1063316_Checkpoint6 {
	public static ArrayList roleal = new ArrayList();
	public static ArrayList landal = new ArrayList();	
	public static Character[] role;
	public static Land[] land;
	public static int turn=0;
	public static int roleCount;
	public static int dice;
	public static String CharacterFirstLine, LandFirstLine;
	public static String[] RoundString=new String[2];
	public static String[] TurnString=new String[2];
	private static final String driver="com.mysql.cj.jdbc.Driver";
	private static final String protocol="jdbc:mysql:";
	static Connection conn=null;

	
	public static void main(String[] args) throws IOException{
		A1063316_GUI demo=new A1063316_GUI();			
	}

	
	public static void Load(String filename, String filename2) throws IOException {
		
		BufferedReader br=new BufferedReader(new FileReader(filename));
		BufferedReader br1=new BufferedReader(new FileReader(filename2));
		
		String line,a,roundFile,turnFile;
		String[] Array=new String[5];
		String[] Array1=new String[2];
		String[] First=new String[2];
		
		roleal = new ArrayList();
		int h=0;
		while((line=br.readLine())!=null) {
			if(h==0) {
				h++;
				CharacterFirstLine=line;
				continue;
			}
			a=line;										
			Array=a.split(",");
			First=CharacterFirstLine.split(",");
			roundFile=First[0];
			turnFile=First[1];
			RoundString=roundFile.split(":");
			TurnString=turnFile.split(":");
			
			for(int i=0;i<Array.length;i++) {
				roleal.add(Array[i]);
			}
		}
		
		roleCount=0;
		roleCount=roleal.size()/5;
		role = new Character[roleCount];
		int count=0;
		for(int d=0;d<roleCount;d++){
			role[d]=new Character(Integer.parseInt((String) roleal.get(count)),Integer.parseInt((String) roleal.get(count+1)),
					Integer.parseInt((String) roleal.get(count+2)),Integer.parseInt((String) roleal.get(count+3)),(String)roleal.get(count+4));
			count +=5;
		}
		
		h=0;
		landal = new ArrayList();
		while((line=br1.readLine())!=null) {
			if(h==0) {
				h++;
				LandFirstLine=line;
				continue;
			}
			a=line;
			Array1=a.split(",");

			for(int i=0;i<Array1.length;i++) {
				landal.add(Array1[i]);
			}
		}
		
		A1063316_GUI.round=Integer.parseInt((String) RoundString[1]);
		for(int i=0;i<roleCount;i++) {
			if(role[i].CHARACTER_NUMBER==Integer.parseInt(TurnString[1])) {
				turn=i;
				break;
			}
		}
		
		br.close();
		br1.close();	
	}

	

	public static void Save(String filename, String filename2) throws IOException {
		//// TODO: You should save the changed variables into original data (filename). ////
		BufferedWriter bw=new BufferedWriter(new FileWriter(filename));
		BufferedWriter bw1=new BufferedWriter(new FileWriter(filename2));

	    bw.write(RoundString[0]+":"+A1063316_GUI.round+","+TurnString[0]+":"+role[turn].CHARACTER_NUMBER);
	    bw.write("\r\n");
		for(int i=0;i<roleCount;i++) {
			bw.write(role[i].location+",");
			bw.write(role[i].CHARACTER_NUMBER+",");
			bw.write(role[i].money+",");
			bw.write(role[i].status+",");
			bw.write(role[i].IMAGE_FILENAME+"");
			bw.write("\r\n");
		}
		bw.flush();
		bw.close();
		
	    bw1.write(LandFirstLine);
	    bw1.write("\r\n");
	    for(int i=0;i<20;i++) {
	    	if(i==0 || i==5 || i==10 || i==15) {
	    		continue;
	    	}
			bw1.write(i+",");
			bw1.write(land[i].owner+"\r\n");
		}
	    bw1.flush();
		bw1.close();
	}	
		
	
	public static void Random() {
		if(role[turn].status>0){
			dice=(int)Math.floor(Math.random()*6)+1;
			A1063316_GUI.oldLocationGUI=role[turn].location;			
			role[turn].location=role[turn].location+dice;
			if(role[turn].location>19) {
				role[turn].location=role[turn].location-20; 
			}
			role[turn].status=role[turn].status-1;
			A1063316_GUI.turnGUI=turn;
			A1063316_GUI.newLocationGUI=role[turn].location;
			turn++;
			while(turn==roleCount) {
				turn=0;
				A1063316_GUI.round++;
				for(int s=0;s<roleCount;s++){
					role[s].status+=1;
				}
			}
		}
		if(role[turn].status<=0) {
			for(int d=turn;d<roleCount;d++) {
				if(role[turn].status==0 && turn<roleCount-1) {
					turn++;
				}else if(role[turn].status==0 && turn==roleCount-1) {
					turn=0;
					A1063316_GUI.round++;
					for(int s=0;s<roleCount;s++){
						role[s].status+=1;
					}
					for(int i=0;i<roleCount;i++) {
						if(role[i].status>0) {
							turn=i;
							break;
						}
					}
				}	
			}
		}
	}
	public static void callDB() {
		try {
			Class.forName(driver).newInstance();
		}catch(Exception err) {
			err.printStackTrace();
		}
		String url="//localhost/";
		String dbName="java_project";
		String username = "root";
		String password = "WERTY54321";
		int count=0;
		
		try {
			conn = DriverManager.getConnection(protocol+url+dbName+"?serverTimezone=UTC",username,password);
			Statement s=conn.createStatement();
			ResultSet rs=null;
			rs=s.executeQuery("SELECT * FROM LAND");
			land=new Land[20];
			int i=0;
			while(rs.next()) { 
				if(i==0 || i==5 || i==10 || i==15) {
					land[i]=new Land(i,0,0,0);
					land[i+1]=new Land(rs.getInt("PLACE_NUMBER"),Integer.parseInt((String) landal.get(count+1)),rs.getInt("LAND_PRICE"),
									   rs.getInt("TOLLS"));
					count+=2;
					i+=2;
				}else {
					land[i]=new Land(rs.getInt("PLACE_NUMBER"),Integer.parseInt((String) landal.get(count+1)),rs.getInt("LAND_PRICE"),
									 rs.getInt("TOLLS"));
					count+=2;
					i++;
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}