import java.awt.*;
import javax.swing.*;

/**
* game runner can work with any types or args 
* 0 args will take u to world and ask for setup 
* 1 arg 'number' will take u to world and ask for column setup
* 2 or more 'number' args will take u to world and setup the world for u
* @author ashabbir
*/
public class GameRunner {
	//static as are used by static method
	
	private static int rows , columns;
	
	public static void main (String[] args) {
		//check for args if we have more then one first is row second is column
		if(args.length > 1)
			{
				try 
				{
					rows = Integer.parseInt(args[0]);
					columns = Integer.parseInt(args[1]);
				} 
				catch (Exception ex)
				{
					//if we cant parse we have wrong args
					//let the user know but proceed with program
					System.out.println("args format not correct..");	
					columns = 0;
					rows = 0;
				} 		
			}
			else if(args.length > 0) // we have 1 arg that must be the row
				{
					try 
					{
						rows = Integer.parseInt(args[0]);
						
					} 
					catch (Exception ex)
					{
						//if we cant parse we have wrong args
						//let the user know but proceed with program
						System.out.println("args format not correct..");	
						rows = 0 ;
						
					} 		
				}
				
				
				//invoke world with rows and colums 
				EventQueue.invokeLater(new Runnable()  {
					public void run() {
						//if world has no row/column or jst rows still show frame
						//frame will deal with this issue
						WorldFrame frame = new WorldFrame(rows , columns);
						frame.setVisible(true); 
					}
		});
	}

}

