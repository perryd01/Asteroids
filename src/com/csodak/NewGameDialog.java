

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A játék kezdetekor nyílik meg.
 * Be lehet rajta állítani az aszteroidák, játékosok, ufók számát és az aszteroidák közötti kapcsolatot.
 * @since 2021.04.29.
 */
public class NewGameDialog extends JDialog {
    private JSpinner asteroidCount= new JSpinner(new SpinnerNumberModel(15, 12, 100, 1));
    private JSpinner asteroidDensity= new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
    private JSpinner playerCount= new JSpinner(new SpinnerNumberModel(2, 2, 10, 1));
    private JSpinner ufoCount= new JSpinner(new SpinnerNumberModel(1, 0, 10, 1));
    private final JButton okBtn = new JButton("Generate");

    public NewGameDialog(){
        JFrame f= new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JDialog d = new JDialog(f,"Generate", true);
        d.setLayout( new FlowLayout() );
        d.getContentPane().setBackground(Color.darkGray);

        GridLayout gl = new GridLayout(6, 3);
        d.setLayout(gl);

        JLabel aa = new  JLabel ("Asteroids");
        aa.setForeground(Color.WHITE);
        d.add(aa);

        JPanel fill0 = new JPanel();
        fill0.setBackground(Color.darkGray);
        d.add(fill0);

        JPanel fill1 = new JPanel();
        fill1.setBackground(Color.darkGray);
        d.add(fill1);

        JPanel fill2 = new JPanel();
        fill2.setBackground(Color.darkGray);
        d.add(fill2);

        JLabel ac = new  JLabel ("Count");
        ac.setForeground(Color.WHITE);
        d.add(ac);
        d.add(asteroidCount);

        JPanel fill3 = new JPanel();
        fill3.setBackground(Color.darkGray);
        d.add(fill3);

        JLabel ad = new  JLabel ("Density");
        ad.setForeground(Color.WHITE);
        d.add(ad);
        d.add(asteroidDensity);
        JLabel pc = new  JLabel ("Player count");
        pc.setForeground(Color.WHITE);
        d.add(pc);

        JPanel fill4 = new JPanel();
        fill4.setBackground(Color.darkGray);
        d.add(fill4);
        d.add(playerCount);

        JLabel uc = new  JLabel ("Ufo count");
        uc.setForeground(Color.WHITE);
        d.add(uc);
        JPanel fill5 = new JPanel();
        fill5.setBackground(Color.darkGray);
        d.add(fill5);
        d.add(ufoCount);

        JPanel fill6 = new JPanel();
        fill6.setBackground(Color.darkGray);
        d.add(fill6);
        okBtn.setBackground(Color.DARK_GRAY);
        okBtn.setForeground(Color.WHITE);

        okBtn.setBorder(BorderFactory.createLineBorder(Color.blue));
        d.add(okBtn);

        okBtn.addActionListener ( new ActionListener()
        {
            public void actionPerformed( ActionEvent e )
            {
                ///Game generate
                int ac = (Integer) asteroidCount.getValue();
                int ad = (Integer) asteroidDensity.getValue();
                int pc = (Integer) playerCount.getValue();
                int uc = (Integer) ufoCount.getValue();
                Controller.getInstance().NewGame(ac,ad,pc,uc);
                d.setVisible(false);
            }
        });

        d.setSize(300,300);
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - d.getWidth()) / 2;
        final int y = (screenSize.height - d.getHeight()) / 2;
        d.setLocation(x, y);
        d.setVisible(true);

    }


}
