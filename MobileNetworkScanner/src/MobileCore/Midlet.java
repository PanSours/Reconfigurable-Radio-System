package MobileCore;

import Parallels.AutoConnection;
import Parallels.BaseStationConnection;
//import Parallels.BonusHandler;
import Parallels.CPCHandler;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Vector;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

public class Midlet extends MIDlet implements CommandListener {

    public int x, y;
    private Display display;
    private Command exitCommand;
    private Command settingsCommand;
    private Command connectCommand;
    private Command disconnectCommand;
    private Command personinfoCommand;
    private Command mobileinfoCommand;
    private Command automanualCommand;
    private Command networkCommand;
    private Command updateCommand;
    private Command backCommand;
    private Command submitCommand;
    private Form mainForm;
    private Form settingsForm;
    private Form mobileinfoForm;
    private Form personinfoForm;
    private List automanualList;
    private Alert al;
    private Alert al2;
    private Alert al3;
    private TextField Name;
    private TextField Surname;
    private TextField Email;
    private StringItem IMEI;
    private StringItem IMSI;
    private StringItem NETS;
    private StringItem SERVE;
    private StringItem CPU;
    private StringItem OS;
    private StringItem RAM;
    private StringItem Status;
    private Image img;
    private Image img2;
    public ChoiceGroup ChargingModel;
    private ChoiceGroup Services;
    private List connectList;
    private int[] Nets;

    private Vector Profiles;
    public BaseStationProfile bsp;
    public SharedMemory sm;
    private CPCHandler cpc;
    public BaseStationConnection BSC;
//    public BonusHandler bh;
    public boolean manual;
    public boolean serve;
    public UserProfile user;
    RMSRecordListener rl;
    public AutoConnection ac;
    public RecordStore rs;

    public Midlet() throws IOException {
        int ran1 = -1;
        int ran2 = -2;

        display = Display.getDisplay(this);
        manual = false;
        Random rn = new Random();
        while ((ran1 < 0) || (ran2 < 0)) {
            ran1 = rn.nextInt() % 10;
            ran2 = rn.nextInt() % 10;
        }
        x = ran1;
        y = ran2;

        bsp = new BaseStationProfile();
        user = new UserProfile();

        // INITIALIZE SHARED MEMORY PROFILES
        sm = new SharedMemory();

        // 5 POSSIBLE DISPLAYS
        mainForm = new Form("Mobile Network Scanner");
        settingsForm = new Form("Settings");
        personinfoForm = new Form("Personal Information");
        mobileinfoForm = new Form("Mobile Information");
        connectList = new List("Networks List", List.EXCLUSIVE);
        automanualList = new List("Select Mode", List.EXCLUSIVE);

        // GLOBAL COMMAND
        backCommand = new Command("Back", Command.BACK, 1);

        // 3 POSSIBLE COMMANDS ON MAIN SCREEN
        exitCommand = new Command("Exit", Command.EXIT, 1);
        settingsCommand = new Command("Settings", Command.OK, 1);
        connectCommand = new Command("Connect", Command.OK, 0);
        disconnectCommand = new Command("Disconnect", Command.OK, 0);

        // 3 POSSIBLE COMMANDS ON SETTINGS SCREEN
        personinfoCommand = new Command("Personal Information", Command.OK, 1);
        mobileinfoCommand = new Command("Mobile Information", Command.OK, 1);
        automanualCommand = new Command("Mode Selection", Command.OK, 1);
        networkCommand = new Command("Choose", Command.OK, 1);

        // 3 POSSIBLE COMMANDS ON PERSONNAL INFORMATION
        updateCommand = new Command("Update", Command.OK, 1);
        submitCommand = new Command("Submit", Command.OK, 0);

        // PERSONAL INFO TEXT FIELDS
        al = new Alert("Sign Up");
        al.setString("Please give your personal information");
        al.setType(AlertType.INFO);
        al2 = new Alert("Unable To Connect");
        al2.setTimeout(10000);
        al2.setString("Sorry, the system is unable to connect in any network");
        al2.setType(AlertType.ERROR);
        al3 = new Alert("Disconnected");
        al3.setString("Current network stopped serving unexpedetely!Please Choose an other network.");
        al3.setType(AlertType.ERROR);
        al3.setTimeout(10000);
        Name = new TextField("Name:", "", 30, TextField.ANY);
        Surname = new TextField("LastName:", "", 30, TextField.ANY);
        Email = new TextField("Email Adress:", "", 30, TextField.EMAILADDR);
        ChargingModel = new ChoiceGroup("Charging Type:", Choice.EXCLUSIVE);
        ChargingModel.append("FIXED", null);
        ChargingModel.append("METERED", null);
        ChargingModel.append("PACKET", null);
        ChargingModel.append("EXPECTED", null);
        ChargingModel.append("EDGE", null);
        ChargingModel.append("PARIS", null);
        ChargingModel.append("AUCTION", null);
        Services = new ChoiceGroup("Services:", Choice.EXCLUSIVE);
        Services.append("DATA", null);
        Services.append("VOICE", null);
        Services.append("DATA&VOICE", null);
        Status = new StringItem("", "NO SIGNAL");

        // APPEND INFO LIST
        personinfoForm.append(Name);
        personinfoForm.append(Surname);
        personinfoForm.append(Email);
        personinfoForm.append(ChargingModel);
        personinfoForm.append(Services);

        // MOBILE INFO
        this.CPU = new StringItem("CPU: ", "");
        this.RAM = new StringItem("RAM: ", "");
        this.IMEI = new StringItem("IMEI: ", "");
        this.IMSI = new StringItem("IMSI: ", "");
        this.OS = new StringItem("OS: ", "");
        this.NETS = new StringItem("Network Types: ", "");
        this.SERVE = new StringItem("Sharing: ", "");

        // OPEN CPC COMMUNICATION THREAD
        cpc = new CPCHandler(this.sm, this.IMEI.getText(), x, y);
        bsp = new BaseStationProfile();
    }

    public void startApp() {

        cpc.start();
        manual = true;

        if (RecordStore.listRecordStores() != null) {
            loadProperties();
            loadUserProfile();
            BSC = new BaseStationConnection(this.IMEI.getText(), this.IMSI.getText(), x, y, bsp, true);
            ac = new AutoConnection(this, IMEI.getText(), IMSI.getText(), Nets, x, y);
            BSC.ac = ac;
            ac.start();
            rl = new RMSRecordListener(this);
            displayMainForm();
        } else {
            readProperties();
            BSC = new BaseStationConnection(this.IMEI.getText(), this.IMSI.getText(), x, y, bsp, true);
            ac = new AutoConnection(this, IMEI.getText(), IMSI.getText(), Nets, x, y);
            rl = new RMSRecordListener(this);
            BSC.ac = ac;
            ac.start();
            displaypersoninfoForm();
        }
//            if(serve==true){
//                bh = new BonusHandler(sm);
//                bh.start();
//            }

    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {

        cpc.shouldRun = false;
        BSC.disconnect = true;
//        if(serve==true)
//        bh.shouldRun = false;
        try {
            cpc.join();
            BSC.join();
//            if(serve==true)
//            bh.join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void commandAction(Command c, Displayable dsplbl) {
        int waits = 0;

        if (c == exitCommand) {
            destroyApp(true);
            notifyDestroyed();
        } else if (c == backCommand) {
            if ((dsplbl == personinfoForm) || (dsplbl == mobileinfoForm) || (dsplbl == automanualList)) {
                displaySettingsForm();
            } else if ((dsplbl == connectList) || (dsplbl == settingsForm)) {
                displayMainForm();
            }
        } else if (c == networkCommand) {
            connectManual();
            displayMainForm();
        } else if (c == disconnectCommand) {
            disconnect();
            displayMainForm();
        } else if (c == connectCommand) {
            if (manual == true) {
                displayConnectForm();
            }
        } else if (c == settingsCommand) {
            displaySettingsForm();
        } else if (c == submitCommand) {
            saveUserProfile();
            displayMainForm();
        } else if (c == updateCommand) {
            if (dsplbl == personinfoForm) {
                updateUserProfile();
                displayMainForm();
            } else {
                if (automanualList.getSelectedIndex() == 0) {
                    if (manual == false) {
                        System.out.println("MANUAL");
                        manual = true;
                        BSC.manual = true;
                    }
                } else {
                    if (manual == true) {
                        System.out.println("AUTO");
                        synchronized (ac) {
                            manual = false;
                            BSC.manual = false;
                            ac.notify();
                        }
                    }
                }
                displaySettingsForm();
            }
        } else if (c == personinfoCommand) {
            displaypersoninfoForm();
        } else if (c == mobileinfoCommand) {
            displaymobileinfoForm();
        } else if (c == automanualCommand) {
            displayautomanualList();
        }

    }

    public void displayMainForm() {

        mainForm.deleteAll();
        mainForm.removeCommand(disconnectCommand);
        mainForm.removeCommand(connectCommand);
        mainForm.removeCommand(disconnectCommand);
        mainForm.removeCommand(settingsCommand);
        mainForm.addCommand(exitCommand);
        if (manual == false) {
            if (BSC.connected == false) {
                Status.setText("NO SIGNAL");
                mainForm.append(Status);
            } else {
                Status.setText(BSC.bsp.Basestation_ID);
                mainForm.append(Status);
            }
        } else {
            if (BSC.connected == false) {
                Status.setText("NO SIGNAL");
                mainForm.append(Status);
                mainForm.addCommand(connectCommand);
            } else {
                Status.setText(BSC.bsp.Basestation_ID);
                mainForm.append(Status);
                mainForm.addCommand(disconnectCommand);
            }
        }
        mainForm.addCommand(settingsCommand);
        mainForm.setCommandListener(this);
        if (bsp == null) {
            display.setCurrent(al2, mainForm);
        }
        if (manual == true) {
            if (BSC.disconnected == false) {
                display.setCurrent(mainForm);
            } else if (BSC.disconnected == true) {
                BSC.disconnected = false;
                display.setCurrent(al3, mainForm);
            }
        } else {
            display.setCurrent(mainForm);
        }
    }

    private void displaySettingsForm() {

        settingsForm.deleteAll();
        settingsForm.addCommand(backCommand);
        settingsForm.addCommand(personinfoCommand);
        settingsForm.addCommand(mobileinfoCommand);
        settingsForm.addCommand(automanualCommand);
        settingsForm.setCommandListener(this);
        if ((BSC.disconnected == true) && (manual == true)) {
            BSC.disconnected = false;
            display.setCurrent(al3, settingsForm);
        } else {
            display.setCurrent(settingsForm);
        }
    }

    private void displaymobileinfoForm() {

        mobileinfoForm.deleteAll();
        mobileinfoForm.append(this.IMEI);
        mobileinfoForm.append(this.IMSI);
        mobileinfoForm.append(this.CPU);
        mobileinfoForm.append(this.RAM);
        mobileinfoForm.append(this.OS);
        mobileinfoForm.append(this.NETS);
        mobileinfoForm.append(this.SERVE);
        mobileinfoForm.addCommand(backCommand);
        mobileinfoForm.setCommandListener(this);
        display.setCurrent(mobileinfoForm);

    }

    public void displaypersoninfoForm() {
        Ticker ti = new Ticker("Please fill all the gaps");

        rs = null;
        try {
            rs = RecordStore.openRecordStore("UserProfile", true);
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
        try {
            if (rs.getNumRecords() != 0) {
                personinfoForm.removeCommand(exitCommand);
                personinfoForm.removeCommand(submitCommand);
                personinfoForm.addCommand(backCommand);
                personinfoForm.addCommand(updateCommand);
                personinfoForm.setCommandListener(this);
                display.setCurrent(personinfoForm);
            } else {
                personinfoForm.setTicker(ti);
                personinfoForm.addCommand(exitCommand);
                personinfoForm.addCommand(submitCommand);
                personinfoForm.setCommandListener(this);
                display.setCurrent(al, personinfoForm);
            }
            rs.closeRecordStore();

        } catch (RecordStoreException ex) {
            personinfoForm.setTicker(ti);
            personinfoForm.addCommand(exitCommand);
            personinfoForm.addCommand(submitCommand);
            personinfoForm.setCommandListener(this);
            display.setCurrent(al, personinfoForm);
        }

    }

    public void displayautomanualList() {
        automanualList.deleteAll();
        automanualList.append("MANUAL", null);
        automanualList.append("AUTO", null);
        if (manual == true) {
            automanualList.setSelectedIndex(0, true);
        } else {
            automanualList.setSelectedIndex(1, true);
        }
        automanualList.addCommand(backCommand);
        automanualList.addCommand(updateCommand);
        automanualList.setCommandListener(this);
        display.setCurrent(automanualList);

    }

    private void displayConnectForm() {
        BaseStationProfile bs;
        connectList.deleteAll();
        Profiles = new Vector();

        Profiles = sm.getFriendly(ChargingModel.getSelectedIndex(), Nets);
        for (int i = 0; i < Profiles.size(); i++) {
            bs = (BaseStationProfile) Profiles.elementAt(i);
            connectList.append(bs.Basestation_ID, null);
        }
        connectList.addCommand(backCommand);
        connectList.addCommand(networkCommand);
        connectList.setCommandListener(this);
        if (!Profiles.isEmpty()) {
            display.setCurrent(connectList);
        } else {
            display.setCurrent(al2, mainForm);
        }
    }

    // READ PROPERTY FILE
    //Load properties
    private void readProperties() {
        String[] substrings;
        String[] subs;
        String[] Netsubs;

        InputStream is = getClass().getResourceAsStream("./props1.txt");
        StringBuffer sb = new StringBuffer();
        try {
            int chars, i = 0;
            while ((chars = is.read()) != -1) {
                sb.append((char) chars);
            }
        } catch (Exception e) {
            System.err.println(e);
            System.err.println("Problem with property file!");
        }
        System.out.println(sb.toString());
        substrings = stringToArray(sb.toString(), "\n");

        try {

            rs = RecordStore.openRecordStore("Properties", true);

            subs = stringToArray(substrings[0], "=");
            String s = subs[1] + " Hz";
            byte[] data = new byte[s.getBytes().length];
            data = s.getBytes();
            this.CPU.setText(s);
            rs.addRecord(data, 0, data.length);
            subs = stringToArray(substrings[1], "=");
            s = subs[1] + " MB";
            data = s.getBytes();
            this.RAM.setText(s);
            rs.addRecord(data, 0, data.length);
            subs = stringToArray(substrings[2], "=");
            s = subs[1];
            data = s.getBytes();
            this.IMEI.setText(s);
            rs.addRecord(data, 0, data.length);
            subs = stringToArray(substrings[3], "=");
            s = subs[1];
            data = s.getBytes();
            this.IMSI.setText(s);
            rs.addRecord(data, 0, data.length);
            subs = stringToArray(substrings[4], "=");
            s = subs[1];
            data = s.getBytes();
            this.OS.setText(s);
            rs.addRecord(data, 0, data.length);

            subs = stringToArray(substrings[5], "=");
            s = subs[1];
            Netsubs = stringToArray(s, ",");
            Nets = new int[Netsubs.length];
            for (int i = 0; i < Netsubs.length; i++) {
                if (Netsubs[i].equals("GSM")) {
                    Nets[i] = 1;
                    System.out.println("1");
                } else if (Netsubs[i].equals("UMTS")) {
                    Nets[i] = 2;
                    System.out.println("2");
                } else if (Netsubs[i].equals("WLAN")) {
                    Nets[i] = 3;
                } else if (Netsubs[i].equals("WIMAX")) {
                    Nets[i] = 4;
                }
            }

            data = s.getBytes();
            this.NETS.setText(s);
            rs.addRecord(data, 0, data.length);
            subs = stringToArray(substrings[6], "=");
            s = subs[1];
            data = s.getBytes();
            this.SERVE.setText(s);
            rs.addRecord(data, 0, data.length);
            if (s.equals("TRUE")) {
                serve = true;
            } else {
                serve = false;
            }

        } catch (RecordStoreException ex) {
            System.err.println(ex);
            System.exit(1);
        }

    }

    public static String[] stringToArray(String a, String delimeter) {
        String c[] = new String[0];
        String b = a;
        while (true) {
            int i = b.indexOf(delimeter);
            String d = b;
            if (i >= 0) {
                d = b.substring(0, i);
            }
            String e[] = new String[c.length + 1];
            System.arraycopy(c, 0, e, 0, c.length);
            e[e.length - 1] = d;
            c = e;
            b = b.substring(i + delimeter.length(), b.length());
            if (b.length() <= 0 || i < 0) {
                break;
            }
        }
        return c;
    }

    private void loadProperties() {
        byte[] byteInputData;
        int length;
        String[] val;
        val = new String[7];

        try {
            RecordStore rs = RecordStore.openRecordStore("Properties", true);

            for (int i = 1; i <= rs.getNumRecords(); i++) {
                byteInputData = new byte[rs.getRecordSize(i)];
                length = rs.getRecord(i, byteInputData, 0);
                val[i - 1] = new String(byteInputData, 0, length);
            }

            this.CPU.setText(val[0]);
            this.RAM.setText(val[1]);
            this.IMEI.setText(val[2]);
            this.IMSI.setText(val[3]);
            this.OS.setText(val[4]);
            String[] Netsubs = stringToArray(val[5], ",");
            Nets = new int[Netsubs.length];
            for (int i = 0; i < Netsubs.length; i++) {
                if (Netsubs[i].equals("GSM")) {
                    Nets[i] = 1;
                } else if (Netsubs[i].equals("UMTS")) {
                    Nets[i] = 2;
                } else if (Netsubs[i].equals("WLAN")) {
                    Nets[i] = 3;
                } else if (Netsubs[i].equals("WIMAX")) {
                    Nets[i] = 4;
                }
            }
            for (int i = 0; i < Nets.length; i++) {
                System.out.println(Nets[i] + "b");
            }
            this.NETS.setText(val[5]);
            this.SERVE.setText(val[6]);
            if (val[6].equals("TRUE")) {
                serve = true;
            } else {
                serve = false;
            }

            rs.closeRecordStore();

        } catch (RecordStoreException ex) {
            System.err.println(ex);
            System.exit(1);
        }

    }

    private void saveUserProfile() {

        try {
            rs = RecordStore.openRecordStore("UserProfile", true);

            byte[] data = new byte[Name.getString().getBytes().length];
            data = Name.getString().getBytes();
            user.Name_ID = rs.getNextRecordID();
            rs.addRecord(data, 0, data.length);
            data = Surname.getString().getBytes();
            user.Surname_ID = rs.getNextRecordID();
            rs.addRecord(data, 0, data.length);
            data = Email.getString().getBytes();
            user.Email_ID = rs.getNextRecordID();
            rs.addRecord(data, 0, data.length);
            data = Integer.toString(ChargingModel.getSelectedIndex()).getBytes();
            user.ChargingModel_ID = rs.getNextRecordID();
            rs.addRecord(data, 0, data.length);
            data = Integer.toString(Services.getSelectedIndex()).getBytes();
            user.Services_ID = rs.getNextRecordID();
            rs.addRecord(data, 0, data.length);
            rs.closeRecordStore();

        } catch (RecordStoreException ex) {
            System.err.println(ex);
            System.exit(1);
        }
    }

    private void updateUserProfile() {
        try {
            rs = RecordStore.openRecordStore("UserProfile", true);
            rs.addRecordListener(rl);
            RecordEnumeration re = rs.enumerateRecords(null, null, false);

            while (re.hasNextElement()) {
                byte[] data = new byte[Name.getString().getBytes().length];
                data = Name.getString().getBytes();
                rs.setRecord(re.nextRecordId(), data, 0, data.length);
                data = Surname.getString().getBytes();
                rs.setRecord(re.nextRecordId(), data, 0, data.length);
                data = Email.getString().getBytes();
                rs.setRecord(re.nextRecordId(), data, 0, data.length);
                data = Integer.toString(ChargingModel.getSelectedIndex()).getBytes();
                rs.setRecord(re.nextRecordId(), data, 0, data.length);
                data = Integer.toString(Services.getSelectedIndex()).getBytes();
                rs.setRecord(re.nextRecordId(), data, 0, data.length);
            }
        } catch (RecordStoreException ex) {
            System.err.println(ex);
            System.exit(1);
        }
    }

    private void loadUserProfile() {
        byte[] byteInputData;
        int length;
        String[] val;

        val = new String[13];

        try {

            rs = RecordStore.openRecordStore("UserProfile", true);

            for (int i = 1; i <= rs.getNumRecords(); i++) {
                byteInputData = new byte[rs.getRecordSize(i)];
                length = rs.getRecord(i, byteInputData, 0);
                val[i - 1] = new String(byteInputData, 0, length);
            }

            user.Name = val[0];
            user.Name_ID = 1;
            Name.insert(val[0], 0);
            user.Surname = val[1];
            user.Surname_ID = 2;
            Surname.insert(val[1], 0);
            user.Email = val[2];
            user.Email_ID = 3;
            Email.insert(val[2], 0);

            user.ChargingModel_ID = 4;

            if (val[3].equals("0")) {
                user.ChargingModel = 0;
                ChargingModel.setSelectedIndex(0, true);
            } else if (val[3].equals("1")) {
                user.ChargingModel = 1;
                ChargingModel.setSelectedIndex(1, true);
            } else if (val[3].equals("2")) {
                user.ChargingModel = 2;
                ChargingModel.setSelectedIndex(2, true);
            } else if (val[3].equals("3")) {
                user.ChargingModel = 3;
                ChargingModel.setSelectedIndex(3, true);
            } else if (val[3].equals("4")) {
                user.ChargingModel = 4;
                ChargingModel.setSelectedIndex(4, true);
            } else if (val[3].equals("5")) {
                user.ChargingModel = 5;
                ChargingModel.setSelectedIndex(5, true);
            } else if (val[3].equals("6")) {
                user.ChargingModel = 6;
                ChargingModel.setSelectedIndex(6, true);
            }

            user.Services_ID = 5;
            if (val[4].equals("0")) {
                user.Services = 0;
                Services.setSelectedIndex(0, true);
            } else if (val[4].equals("1")) {
                user.Services = 1;
                Services.setSelectedIndex(1, true);
            } else if (val[4].equals("2")) {
                user.Services = 2;
                Services.setSelectedIndex(2, true);
            }
            rs.closeRecordStore();
        } catch (RecordStoreException ex) {
            System.err.println(ex);
            System.exit(1);
        }

    }

    private void connectManual() {
        if (BSC.isAlive()) {
            BSC.disconnect = true;
            try {
                BSC.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        bsp = (BaseStationProfile) Profiles.elementAt(connectList.getSelectedIndex());
        BSC = new BaseStationConnection(this.IMEI.getText(), this.IMSI.getText(), x, y, bsp, true);
        BSC.ac = ac;
        ac.bsp = bsp;
        BSC.start();
        while ((BSC.connected == false) && (BSC.disconnected == false)) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void disconnect() {
        if (BSC.shouldRun == true) {
            BSC.disconnect = true;
            while (BSC.connected == true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            try {
                BSC.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

}
