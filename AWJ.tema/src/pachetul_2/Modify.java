package pachetul_2;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import pachetul_1.Afis;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
//bibliotecile folosite

public class Modify extends JFrame {//clasa care creeaza frame ul interfetei GUI
	double startTime = System.nanoTime();//inceputul programului
    DisplayPanel displayPanel;
    JButton resetButton, grayButton,
            contIncButton, contDecButton;//declararea butoanelor
    
    public Modify() {
        super();
        Container container = getContentPane();

        displayPanel = new DisplayPanel();
        container.add(displayPanel);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2));

        //se creeaza butoanele cu functiile lor
        resetButton = new JButton("Reset");
        resetButton.addActionListener(new ButtonListener());
        grayButton = new JButton("Grey");
        grayButton.addActionListener(new ButtonListener());

        contIncButton = new JButton(">>");
        contIncButton.addActionListener(new ButtonListener());
        contDecButton = new JButton("<<");
        contDecButton.addActionListener(new ButtonListener());
        //se adauga butoanele panel-ului
        panel.add(grayButton);
        panel.add(resetButton);
  
        panel.add(contDecButton);
        panel.add(contIncButton);
        container.add(BorderLayout.SOUTH, panel);

        addWindowListener(new WindowEventHandler());
        setSize(displayPanel.getWidth(), displayPanel.getHeight() + 10);
        show(); // afisare frame-ul
        double stopTime = System.nanoTime();
        double elapsedTime = (stopTime - startTime) / 10000000000.0;
        //se calculeaza si se afiseaza timpul de executie pentru deschiderea programului
        System.out.println("Timpul de executie pentru deschiderea programului: " + elapsedTime + " secunde");
    }

    class WindowEventHandler extends WindowAdapter {
    	double startTime = System.nanoTime();
        public void windowClosing(WindowEvent e) {
            System.exit(0);
            double stopTime = System.nanoTime();
            double elapsedTime = (stopTime - startTime) / 10000000000.0;

            //System.out.println("Timpul de executie: " + elapsedTime + " secunde");
        }
    }

    public static void main(String arg[]) {
    	double startTime = System.nanoTime();
        new Modify();
        Afis a = new Afis();
        a.intAf();
        double stopTime = System.nanoTime();
        double elapsedTime = (stopTime - startTime) / 10000000000.0;

        System.out.println("Timpul de executie pentru interfata: " + elapsedTime + " secunde");
    }	

    class ButtonListener implements ActionListener {
    	double startTime = System.nanoTime();
        public void actionPerformed(ActionEvent e) {
            JButton temp = (JButton) e.getSource();
            //se implementeaza funtiile fiecarui buton la apasare
            if (temp.equals(grayButton)) {
            	displayPanel.color = false;
                displayPanel.grayOut();
                displayPanel.repaint();
              } 
            else if (temp.equals(resetButton)) {
            	displayPanel.contrastInc = true;
                displayPanel.reset();
                displayPanel.repaint();
              }
            
            
            else if (temp.equals(contIncButton)) {
                displayPanel.contrastInc = true;
                displayPanel.changeScaleFactor();
                displayPanel.rescale();
                displayPanel.repaint();
            }		
            else if (temp.equals(contDecButton)) {
                displayPanel.contrastInc = false;
                displayPanel.changeScaleFactor();
                displayPanel.rescale();
                displayPanel.repaint();
            }
            double stopTime = System.nanoTime();
            double elapsedTime = (stopTime - startTime) / 10000000000.0;
            //se calculeaza si se afiseaza timpul de executie pentru executarea 
            System.out.println("Timpul de executie la click: " + elapsedTime + " secunde");
        }
       
    }
//se creeaza panel-ul de lucru 
class DisplayPanel extends JPanel {
	double startTime = System.nanoTime();
    Image displayImage;
    BufferedImage biSrc, biDest, bi; 
    Graphics2D big;
    RescaleOp rescale;
    float scaleFactor = 1.0f;
    float offset = 10;
    boolean color, contrastInc;
    String path;
    //interfata grafica
    DisplayPanel() {
        setBackground(Color.gray); 
        loadImage();
        setSize(displayImage.getWidth(this),	
                displayImage.getWidth(this));
        createBufferedImages();
    }
    //se incarca imaginea cu ajutorul clasei JFileChooser
    public void loadImage() {
    	
    	//se incarca imaginea dintr-un fisier pe care il va alege utilizatorul
    	JFileChooser fileopen = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("24bit bmp images", "bmp");
	    fileopen.setFileFilter(filter);
	    int ret = fileopen.showDialog(null, "Open file");
	    //fisierul este selectat
	    if (ret == JFileChooser.APPROVE_OPTION)
	    {
	      File selectedFile = fileopen.getSelectedFile();
	      path = selectedFile.getAbsolutePath(); // luam calea spre fisierul respectiv
	    }
	    
	    displayImage = Toolkit.getDefaultToolkit().getImage(path);
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(displayImage, 1);
        //sistemul de exceptii pentru blocare la incarcare imagine
        try {
            mt.waitForAll();
        } catch (Exception e) {
            System.out.println("Exception while loading.");
        }
 
        if (displayImage.getWidth(this) == -1) {
            System.out.println("No bmp file");
            System.exit(0);
        }
    }
    //polimorfism pentru incarcarea imaginii cu path-ul imaginii ce trebuie modificata
    public void loadImage(String path)
    {
    	System.out.println("Enter path here : ");

        try{
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            String s = bufferRead.readLine();
            path = s;
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

      }
    
    // creearea obiectelor de tip BufferedImage pentru fisierele sursa si destinatie
    public void createBufferedImages() {
        biSrc = new BufferedImage(displayImage.getWidth(this),
                                  displayImage.getHeight(this),
                                  BufferedImage.TYPE_INT_RGB);

        big = biSrc.createGraphics();
        big.drawImage(displayImage, 0, 0, this);

        biDest = new BufferedImage(displayImage.getWidth(this),
                                   displayImage.getHeight(this),
                                   BufferedImage.TYPE_INT_RGB);
        bi = biSrc;
    }
    //modificarea parametrului ce modifica contrastul
    public void changeScaleFactor() {
        if (contrastInc) {
            if (scaleFactor < 2)
                scaleFactor = scaleFactor+0.1f; //incrementam contrastul
        }
        else {
            if (scaleFactor > 0)
                scaleFactor = scaleFactor-0.1f; // decrementam contrastul
        }
    }
    //modificarea efectiva a contrastului
    public void rescale() {
        rescale = new RescaleOp(scaleFactor, offset, null);
        rescale.filter(biSrc, biDest);
        bi = biDest;
    }
    //actualizarea imaginii
    public void update(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        paintComponent(g);
    }
    //deseneaza imaginea
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;

        g2D.drawImage(bi, 0, 0, this);
    }
    //crearea imaginii alb-negru
    public void grayOut() {
        ColorConvertOp colorConvert = new ColorConvertOp(ColorSpace
            .getInstance(ColorSpace.CS_GRAY), null);
        colorConvert.filter(bi, bi);
      }
    //revenirea la imaginea initiala
    public void reset() {
        big.setColor(Color.black);
        big.clearRect(0, 0, bi.getWidth(this), bi.getHeight(this));
        big.drawImage(displayImage, 0, 0, this);
      }
}
double stopTime = System.nanoTime();



}