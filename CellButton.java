import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * Cell class Extends JButton, contains Cell Properties which are isLive, nextState liveNeghbours
 * and jbutton features as well Color is representation of state
 * @author ashabbir
 */
public class CellButton extends JButton {
    //Class variable declaration section

    private boolean isLive;
    private boolean nextState;
    private int liveNeighbours;
    private Color color;

    /**
     * Cell constructor, set cell values and default layout
     */
    public CellButton() {
        super();
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setIsLive(false);

        //on click Change State 
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                setIsLive(!getIsLive());
            }
        });
    }

    /**
     * public accessor for isLive Field of the Cell
     */
    public Boolean getIsLive() {
        return isLive;
    }

    /**
     * public accessor, gets liveNeighbours for the cell
     */
    public int getLiveNeighbours() {
        return liveNeighbours;
    }

    /**
     * public modifier, set nextState of the cell
     */
    public void setNextState(Boolean nextState) {
        this.nextState = nextState;
    }

    /**
     * public modifier, sets liveNeighburs for the cell
     */
    public void setLiveNeighbours(int liveNeighbours) {
        this.liveNeighbours = liveNeighbours;
    }

    /**
     * once all the calculations are done finish tick would replace current
     * state with future state
     */
    public void finishTick() {
        setIsLive(nextState);
        nextState = false;
        liveNeighbours = 0;
    }

    /**
     * public modifier for isLive, it wil change the isLive field also will
     * change the appreance of the button.
     */
    public void setIsLive(boolean state) {
        isLive = state;
        setBackground(state ? Color.BLUE : Color.YELLOW);
        setOpaque(true);
    }
}
