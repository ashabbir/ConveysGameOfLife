
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * World class Which extends JFrames has world properties / methods and JFrame features
 * constructor takes two args (int) which are the rows and columns.
 * @author ashabbir
 */
public class WorldFrame extends JFrame {

	//default fram size
    private static final int DEFAULT_HEIGHT = 600;
    private static final int DEFAULT_WIDTH = 400;
	
	//world has max/ min rows and column limitation
	//dont want to create a world for unrealistic dimentions
	//setting range to low as dont want to over load world
    private static final int MAX_ROWS_ALLOWED = 25;
    private static final int MAX_COLUMNS_ALLOWED = 15;
    private static final int MIN_ROWS_ALLOWED = 1;
    private static final int MIN_COLUMNS_ALLOWED = 1;
	
	//UI fields
    private final JTextField txtRow;
    private final JTextField txtColumn;
	private final JLabel lblMessage;
    private JPanel controlPanel;
    private JPanel setupPanel;
    private JPanel gamePanel;
    
	//world fields
	private int rows;
    private int columns;
    private CellButton[][] cells;


	/**
	* Default constructor, create world frame 
	* and lets user decide world dimensions
	* in case if user want to call the worldFrame without params
	*/
	public WorldFrame() {
		this(0,0);
	}


    /**
     * World constructor, set World default and layout
     */
    public WorldFrame(int aRows, int aColumns) {
        super();
		
		setResizable(false);
        rows = aRows;
        columns = aColumns;

		//Set frame prop
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Game of life");

        //center my window to the screen
        setLocationRelativeTo(null);


		/*World Frame have 
		Setup Panel where u can create world
		Conrol panel where u can click to generate or clear world
		game panel which will have the world cells in a gridlayout
		*/

		//setup Panel which contains 
		//a button panel and an error panel for proper layout 
		setupPanel = new JPanel(new GridLayout(2, 1));
		JPanel setupButtonPanel = new JPanel();
		JPanel setupErrorPanel = new JPanel();
		
		
		//button panel will have setup world button 
		//rows and columns input text fields
        JButton btnSetup = new JButton("Setup World");
        txtRow = new JTextField("" + rows, 3);
        txtColumn = new JTextField("" + columns, 3);
        setupButtonPanel.add(btnSetup);
        setupButtonPanel.add(new JLabel("Rows:"));
        setupButtonPanel.add(txtRow);
        setupButtonPanel.add(new JLabel("Columns:"));
        setupButtonPanel.add(txtColumn);
        
		
		//creating and binding actionlistener for setting up world
        btnSetup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (performSetup()) {
                    setupPanel.setVisible(false);
                }
            }
        });

        //Error panel will have error message from setup 
	    //world needs to be in range, and only numaric values are accepted
        lblMessage = new JLabel("Enter rows and colum");
		setupErrorPanel.add(lblMessage); 
		
		//add btn panel and err pnl to setup pnl
		setupPanel.add(setupButtonPanel);
		setupPanel.add(setupErrorPanel);
        add(setupPanel, BorderLayout.NORTH);
        setupPanel.setVisible(true);

		//run setup logic incase rows / column is provided by caller
        if (performSetup()) {
			//if setup was successfull hide setup panel
			//as setup panel has created the world and added to frame
            setupPanel.setVisible(false);
        }
    }


	/**
	* perform setup 
	* @return boolean true if setup was successfull false if it was not successfule
	*/
    public boolean performSetup() {
        boolean status = true;
		//reset message label.
		lblMessage.setText("");
		
		//check if row and column are provided in correct format
        try {
            rows = Integer.parseInt(txtRow.getText());
            columns = Integer.parseInt(txtColumn.getText());
			
			//check if row provided in range
            if (rows > MAX_ROWS_ALLOWED || rows < MIN_ROWS_ALLOWED) {
						lblMessage.setText("Allowed Row range: " + MIN_ROWS_ALLOWED + 
							" : " + MAX_ROWS_ALLOWED);
                status = false;
            }
			
			//check if column provided in range
            if (columns > MAX_COLUMNS_ALLOWED || columns < MIN_COLUMNS_ALLOWED) {
						lblMessage.setText(lblMessage.getText() + " Allowed Column range: " + MIN_COLUMNS_ALLOWED +
							 " : " + MAX_COLUMNS_ALLOWED);
                status = false;
            }
        } catch (NumberFormatException nFE) {
			//catch number format exception when user enters non integers
            txtRow.setBackground(Color.RED);
            txtColumn.setBackground(Color.RED);
            status = false;
			} catch (Exception ex) {
				//catch any other sort of excetion 
	            txtRow.setBackground(Color.RED);
	            txtColumn.setBackground(Color.RED);
	            status = false;	
			}
		
			//if data data encountered return false
		if(!status) {
			return status;
		}

		//if every thing went fine create control panel 
		//add to frame and show
        controlPanel = new JPanel();
        JButton btnGenerate = new JButton("Generate");
        JButton btnClear = new JButton("Clear");
        controlPanel.add(btnGenerate);
        controlPanel.add(btnClear);
        add(controlPanel, BorderLayout.NORTH);

		//start creating game panel which hold the world
        cells = new CellButton[rows][columns];
        gamePanel = new JPanel(new GridLayout(rows, columns));
		//loop through and initialize cells
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cells[i][j] = new CellButton();
				//add cells to game panel
                gamePanel.add(cells[i][j]);
            }
        }

        add(gamePanel);

		//bind generate button with listener 
        btnGenerate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                tick(); //call tick for the world
            }
        });
		
		//bind clear button with listner
        btnClear.addActionListener(new ClearAction());
		//return status which will be true
        return status;
    }

    
	/**
     * performs the tick even for a world, Checks next state then switches the
     * state for all cell
     */
    public void tick() {
        //get live neighbours count for each Cell
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                int liveNeighboutCount = 0;
                for (int x = r - 1; x <= r + 1; x++) {
                    for (int y = c - 1; y <= c + 1; y++) {

                        //dont count self in live enighbours
                        if (x == r && y == c) {
                            continue;
                        }
                        //lower bound check and upper bound
                        if (x < 0 || y < 0 || x >= rows || y >= columns) {
                            continue;
                        }

                        if (cells[x][y].getIsLive()) {
                            liveNeighboutCount++;
                        }
                    }
                }
				//set live neighbour count
                cells[r][c].setLiveNeighbours(liveNeighboutCount);
            }
        }

        //Set nextState to Live or Dead 
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
				//if cell is live 
                if (cells[i][j].getIsLive()) {
					//if live neighbours are 2 or three cell lives
                    if (cells[i][j].getLiveNeighbours() == 2 || 
						cells[i][j].getLiveNeighbours() == 3) {
                        cells[i][j].setNextState(true);
                    } else {
						//cell dies
                        cells[i][j].setNextState(false);
                    }
				} else { //cell is dead
                    if (cells[i][j].getLiveNeighbours() == 3) {
						//live neighbours are 3 then birth
                        cells[i][j].setNextState(true);
                    } 
                }
            }
        }

        //call finish Tick for each Cell, that would move nextState to isLive
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                cells[i][j].finishTick();
            }
        }

    }

    /**
     * action listener to clear up all the selected cells
	 * and restore them to not selected state
	 * also set live neighbours to 0 and next state to false.
     */
    private class ClearAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
			
			//loop through each cell and clear them
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    cells[i][j].setIsLive(false);
                    cells[i][j].setNextState(false);
                    cells[i][j].setLiveNeighbours(0);
                }
            }

        }
    }
}
