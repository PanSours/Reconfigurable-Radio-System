/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;

/**
 * @author Kiddo
 */
public class Midlet extends MIDlet implements CommandListener {

    private Display display;
    private Command exitCommand;
    private Command startCommand;
    private Command stopCommand;
    private Command backCommand;
    private Command nextCommand;

    private Form mainForm;
    private Form nextForm;

    thread t;

    public Midlet() {

        display = Display.getDisplay(this);
        exitCommand = new Command("Exit", Command.EXIT, 0);
        /* GIA XRISI THREAD */
        startCommand = new Command("Start", Command.ITEM, 0);
        stopCommand = new Command("Stop", Command.ITEM, 0);
        t = new thread();

        /* GIA XRISI ENALLAKTIKIS FORMAS */
        backCommand = new Command("Back", Command.BACK, 0);
        nextCommand = new Command("Next", Command.ITEM, 0);
    }

    public void startApp() {

        mainForm = new Form("Kentriki");
        mainForm.addCommand(exitCommand);
        mainForm.addCommand(startCommand);
        mainForm.addCommand(nextCommand);
        mainForm.setCommandListener(this);

        display.setCurrent(mainForm);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {

        if (t.isAlive()) {
            t.flag = false;
            try {
                t.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

    }

    public void commandAction(Command cmnd, Displayable dsplbl) {

        if (cmnd == exitCommand) {
            destroyApp(false);
            notifyDestroyed();
        } else if (cmnd == startCommand) {
            t.start();
            while (!t.isAlive()) {

            }
            displayKentriki();
        } else if (cmnd == stopCommand) {
            t.flag = false;
            try {
                t.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            displayKentriki();
        } else if (cmnd == backCommand) {
            displayKentriki();
        } else if (cmnd == nextCommand) {
            displayNext();
        }

    }

    public void displayKentriki() {

        mainForm.removeCommand(startCommand);
        mainForm.removeCommand(stopCommand);

        mainForm.addCommand(nextCommand);
        mainForm.addCommand(exitCommand);
        if (t.isAlive()) {
            mainForm.addCommand(stopCommand);
        } else {
            mainForm.addCommand(startCommand);
        }
        display.setCurrent(mainForm);
    }

    private void displayNext() {
        nextForm = new Form("New Form");
        TextField tf = new TextField("Write:", "", 20, TextField.ANY);
        nextForm.append(tf);
        nextForm.addCommand(backCommand);
        nextForm.setCommandListener(this);

        display.setCurrent(nextForm);
    }

}
