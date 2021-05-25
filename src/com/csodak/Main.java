

import javax.swing.*;

import static java.lang.Integer.parseInt;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;



/**
 * @version 1.0
 * @since skeleton
 * @since 2021.03.21
 */
public class Main {


    public static void main(String[] args) throws IOException {
        /*try (var br = new BufferedReader(new InputStreamReader(System.in))){
            Command.GetInstance().HandleCommands();
        }*/
        JFrame mainFrame = new JFrame();
        mainFrame.setTitle("(A)steroids");
        FlowLayout fl = new FlowLayout();
        mainFrame.setLayout(fl);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        AsteroidFieldContainer asteroidFieldContainer = new AsteroidFieldContainer();
        Controller.getInstance().SetAsteroidFieldContainer(asteroidFieldContainer);
        ControlPanel controlPanel = new ControlPanel();
        Controller.getInstance().SetControlPanel(controlPanel);

        JDialog newgamedialog = new NewGameDialog();



        fl.setVgap(0);
        fl.setHgap(0);


        mainFrame.add(asteroidFieldContainer);
        mainFrame.add(controlPanel);
        mainFrame.setVisible(true);

        asteroidFieldContainer.setBackground(new Color(0,53,79));
        controlPanel.setBackground(new Color(43,43,43));
        asteroidFieldContainer.setPreferredSize(new Dimension(1280, 720));
        controlPanel.setPreferredSize(new Dimension(controlPanel.getSize().width, 720));
        mainFrame.pack();

        //System.out.println(controlPanel.getSize());
    }

    /**
     * Terminál ablakot törli az inicalizálás után
     */
    public static void ClearScr(){
        System.out.println("\n___INIT FINISHED___\n");
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}

