/*
 * Graphic is the GUI of BaseStation program. Contains 3 different panels and 2 menu items. The first panel
 * is a map where we draw our Base Station and Mobiles clients. The 2nd panel contains Base Station's information.
 * The 3rd panel is a table with the records of connected Mobile Clients. 
 */
package basestation;

import java.awt.*;
import java.io.FileReader;
import java.util.Properties;
import java.util.Scanner;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Graphic extends JFrame {

    JTextArea ta;
    GridLayout gl;
    ImagePanel imagePanel;
    JPanel TextPanel;
    JTextArea txt;
    //JTextArea printer;
    public JMenuItem Start;
    public JMenuItem Stop;
    public JMenuItem Refresh;
    public JMenuItem Exit;
    public JMenuItem Help;
    public JMenuItem Help2;
    JTable table;
    public myActionListener ml;
    int X, Y;

    public Graphic() {

        super("Base Station");
        this.setSize(1400, 700);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setVisible(true);
        this.ml = new myActionListener(this);

    }

    public void createTextPanels() {

        TextPanel = new JPanel();
        TextPanel.setSize(550, 700);
        TextPanel.setLocation(700, 0);
        Font font = new Font("Verdana", Font.BOLD, 15);
        Font font2 = new Font("Verdana", Font.BOLD, 10);
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER);
        TextPanel.setLayout(fl);
        this.add(TextPanel);

        //Initialize properties text
        txt = new JTextArea();
        txt.setText("Network_ID= \n");
        txt.append("Basestation_ID= \n");
        txt.append("Network_Type= \n");
        txt.append("Frequency= \n");
        txt.append("Max_Bitrate= \n");
        txt.append("Guaranteed_Bitrate= \n");
        txt.append("Provider= \n");
        txt.append("Charge_Type= \n");
        txt.append("X= \n");
        txt.append("Y= \n");
        txt.append("Radius_Coverage= \n");
        txt.append("Port= \n");
        txt.append("Signal_Strength= \n");
        txt.append("Load_Level= \n");
        txt.setSize(600, 200);
        txt.setColumns(30);
        txt.setBackground(Color.black);
        txt.setForeground(Color.white);
        txt.setFont(font);
        txt.setEditable(false);

        TextPanel.add(txt);

        // not used in this version, maybe needed later
        /*
         printer = new JTextArea();
        
         printer.setSize(600, 200);
         printer.setText("Print Output...\n");
         printer.setColumns(50);
         printer.setRows(50);
         printer.setBackground(Color.white);
         printer.setForeground(Color.black);
         printer.setFont(font2);
         printer.setEditable(false);
        
         TextPanel.add(printer);
         */
    }

    public void createHelpFrame() {

        try {

            //Read a text file named MyTextFile.txt  
            //You can download this text file at the link below  
            //You also can change to other text file by change it's name  
            //Don't forget to put .txt  
            //If your text file locate at other location,  
            //you must put it full location.  
            //For example :  
            //C:\\Documents and Settings\\evergreen\\MyTextFile.txt  
            FileReader readTextFile = new FileReader("HelpFile.txt");

            //Create a scanner object from FileReader  
            Scanner fileReaderScan = new Scanner(readTextFile);

            //Create a String that will store all text in the text file  
            String storeAllString = "";

            //Put all text from text file into created String  
            while (fileReaderScan.hasNextLine()) {
                String temp = fileReaderScan.nextLine() + "\n";
                storeAllString = storeAllString + temp;
            }

            //Set JTextArea text to created String  
            JTextArea textArea = new JTextArea(storeAllString);
            textArea.setEditable(false);

            //Set JTextArea to line wrap  
            textArea.setLineWrap(true);

            //Set JTextArea text to word wrap  
            textArea.setWrapStyleWord(true);

            //Create scrollbar for text area  
            JScrollPane scrollBarForTextArea = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

            //Create a window using JFrame with title ( Open text file into JTextArea )  
            JFrame frame = new JFrame("Open text file into JTextArea");

            //Add scrollbar into JFrame  
            frame.add(scrollBarForTextArea);

            //Set default close operation for JFrame  
            frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

            //Set JFrame size  
            frame.setSize(500, 500);

            //Make JFrame locate at center on screen  
            frame.setLocationRelativeTo(null);

            //Make JFrame visible  
            frame.setVisible(true);

        } catch (Exception exception) {
            //Print Error in file processing if it can't process your text file  
            System.out.println("Error in file processing");
        }

    }

    public void recreateText() {

        Font font = new Font("Verdana", Font.BOLD, 15);

        txt.setText("Network_ID= \n");
        txt.append("Basestation_ID= \n");
        txt.append("Network_Type= \n");
        txt.append("Frequency= \n");
        txt.append("Max_Bitrate= \n");
        txt.append("Guaranteed_Bitrate= \n");
        txt.append("Provider= \n");
        txt.append("Charge_Type= \n");
        txt.append("X= \n");
        txt.append("Y= \n");
        txt.append("Radius_Coverage= \n");
        txt.append("Port= \n");
        txt.append("Signal_Strength= \n");
        txt.append("Load_Level= \n");
        txt.setSize(600, 200);
        txt.setColumns(30);
        txt.setBackground(Color.black);
        txt.setForeground(Color.white);
        txt.setFont(font);
        txt.setEditable(false);

    }

    public void printOutput(String s) {

        //printer.append(s +"\n");
    }

    public void createImagePanel() {

        // Map panel creation
        imagePanel = new ImagePanel(new ImageIcon("images/map.jpg").getImage());
        imagePanel.setLocation(0, 0);
        this.add(imagePanel);
        this.setVisible(true);

    }

    public void addJMenu() {

        // Menu
        JMenu BS = new JMenu("BaseStation");
        JMenu Help = new JMenu("Help");

        Start = new JMenuItem("Connect");
        Stop = new JMenuItem("Disconnect");
        Refresh = new JMenuItem("Refresh");
        Exit = new JMenuItem("Exit");

        Help2 = new JMenuItem("Help...");

        Start.addActionListener(this.ml);
        Stop.addActionListener(this.ml);
        Exit.addActionListener(this.ml);
        Refresh.addActionListener(this.ml);
        Help2.addActionListener(this.ml);

        Stop.setEnabled(false);
        Refresh.setEnabled(false);

        BS.add(Start);
        BS.add(Stop);
        BS.add(new JSeparator());
        BS.add(Refresh);
        BS.add(new JSeparator());
        BS.add(Exit);

        Help.add(Help2);

        JMenuBar bar = new JMenuBar();
        bar.add(BS);
        bar.add(Help);
        this.setJMenuBar(bar);

    }

    public void printBaseStationInfo(String s) {

        ta.setText(s);

    }

    public synchronized void UpdateMobiles(Vector<MobileHandler> mt) {

        imagePanel.removeAll();
        Font font = new Font("Verdana", Font.CENTER_BASELINE, 2);

        // Draw basestation icon on map
        ImagePanel bsi = new ImagePanel(new ImageIcon("images/basestation.png").getImage());
        bsi.setLocation((this.X * 60 + 15), (this.Y * 60 + 15));
        imagePanel.add(bsi);
        imagePanel.repaint();

        // Draw each Mobile Device
        for (int i = 0; i < mt.size(); i++) {

            if (mt.get(i).Type.equals("CONNECTED")) {

                ImagePanel img = new ImagePanel(new ImageIcon("images/mobile.png").getImage());
                img.setLocation((mt.get(i).X * 60 + 15), (mt.get(i).Y * 60 + 15));
                JTextField tf = new JTextField();
                tf.setColumns(2);
                //tf.setFont(font);
                tf.setSize(30, 17);
                tf.setBackground(Color.YELLOW);
                tf.setText(mt.get(i).IMEI);
                img.add(tf);
                //img.repaint();
                imagePanel.add(img);

            }

            imagePanel.repaint();

        }

        // Print information for each Mobile Device
        table.removeAll();
        for (int i = 0; i < mt.size(); i++) {

            if (!mt.get(i).Type.equals("UNKNOWN")) {

                table.setValueAt(i + 1, i, 0);
                table.setValueAt(mt.get(i).IMEI, i, 1);
                table.setValueAt(mt.get(i).IMSI, i, 2);
                table.setValueAt(mt.get(i).X, i, 3);
                table.setValueAt(mt.get(i).Y, i, 4);
                table.setValueAt(mt.get(i).Type, i, 5);

            }

        }

    }

    public void clearTable() {

        for (int i = 0; i < 20; i++) {

            table.setValueAt("", i, 0);
            table.setValueAt("", i, 1);
            table.setValueAt("", i, 2);
            table.setValueAt("", i, 3);
            table.setValueAt("", i, 4);
            table.setValueAt("", i, 5);

        }

    }

    public void SetBaseStation(int x, int y, Properties props) {

        //Set x,y
        this.X = x;
        this.Y = y;

        // Draw basestation icon on map
        ImagePanel img = new ImagePanel(new ImageIcon("images/basestation.png").getImage());
        img.setLocation(x * 60 - 15, y * 60 - 15);
        imagePanel.add(img);
        imagePanel.repaint();

        //Print basestation properties
        txt.setText("Network_ID= " + props.getProperty("Network_ID") + "\n");
        txt.append("Basestation_ID= " + props.getProperty("Basestation_ID") + "\n");
        txt.append("Network_Type= " + props.getProperty("Network_Type") + "\n");
        txt.append("Frequency= " + props.getProperty("Frequency") + "\n");
        txt.append("Max_Bitrate= " + props.getProperty("Max_Bitrate") + "\n");
        txt.append("Guaranteed_Bitrate= " + props.getProperty("Guaranteed_Bitrate") + "\n");
        txt.append("Provider= " + props.getProperty("Provider") + "\n");
        txt.append("Charge_Type= " + props.getProperty("Charge_Type") + "\n");
        txt.append("X= " + props.getProperty("x") + "\n");
        txt.append("Y= " + props.getProperty("y") + "\n");
        txt.append("Radius_Coverage= " + props.getProperty("Radius_Coverage") + "\n");
        txt.append("Port= " + props.getProperty("Port") + "\n");
        txt.append("Signal_Strength= " + props.getProperty("Signal_Strength") + "\n");
        txt.append("Load_Level= " + 0 + "%");

    }

    public void setTable() {

        String data[][] = {{"", "", "", "", "", ""}, {"", "", "", "", "", ""}, {"", "", "", "", "", ""}, {"", "", "", "", "", ""}, {"", "", "", "", "", ""}, {"", "", "", "", "", ""}, {"", "", "", "", "", ""}, {"", "", "", "", "", ""}, {"", "", "", "", "", ""}, {"", "", "", "", "", ""}, {"", "", "", "", "", ""}, {"", "", "", "", "", ""}, {"", "", "", "", "", ""}, {"", "", "", "", "", ""}, {"", "", "", "", "", ""}, {"", "", "", "", "", ""}, {"", "", "", "", "", ""}, {"", "", "", "", "", ""}, {"", "", "", "", "", ""}, {"", "", "", "", "", ""}, {"", "", "", "", "", ""}, {"", "", "", "", "", ""}};
        String col[] = {"No", "IMEI", "IMSI", "X", "Y", "Status          "};
        DefaultTableModel model = new DefaultTableModel(data, col);

        table = new JTable(model) {
            @Override
            public boolean isCellEditable(int rowIndex, int vColIndex) {
                return false;
            }
        };

        table.sizeColumnsToFit(10);
        table.setSize(600, 700);
        JScrollPane pane = new JScrollPane(table);
        pane.setSize(100, 700);
        TextPanel.add(pane);

    }

    public synchronized void updateProperties(Properties props, int lv) {

        //Update basestation properties
        txt.setText("Network_ID= " + props.getProperty("Network_ID") + "\n");
        txt.append("Basestation_ID= " + props.getProperty("Basestation_ID") + "\n");
        txt.append("Network_Type= " + props.getProperty("Network_Type") + "\n");
        txt.append("Frequency= " + props.getProperty("Frequency") + "\n");
        txt.append("Max_Bitrate= " + props.getProperty("Max_Bitrate") + "\n");
        txt.append("Guaranteed_Bitrate= " + props.getProperty("Guaranteed_Bitrate") + "\n");
        txt.append("Provider= " + props.getProperty("Provider") + "\n");
        txt.append("Charge_Type= " + props.getProperty("Charge_Type") + "\n");
        txt.append("X= " + props.getProperty("x") + "\n");
        txt.append("Y= " + props.getProperty("y") + "\n");
        txt.append("Radius_Coverage= " + props.getProperty("Radius_Coverage") + "\n");
        txt.append("Port= " + props.getProperty("Port") + "\n");
        txt.append("Signal_Strength= " + props.getProperty("Signal_Strength") + "\n");
        txt.append("Load_Level= " + lv + "%");

    }

    public void DrawGraphics() {

        //Initialize Enviroment
        this.setLayout(null);
        this.createImagePanel();
        this.createTextPanels();
        this.setTable();
        this.addJMenu();
        this.setVisible(true);

    }

    public static void main(String[] args) {

        Graphic g = new Graphic();
        g.DrawGraphics();

    }

}

class ImagePanel extends JPanel {

    private Image img;

    public ImagePanel(String img) {
        this(new ImageIcon(img).getImage());
    }

    public ImagePanel(Image img) {

        this.img = img;
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setLayout(null);
        setSize(size);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }

}
