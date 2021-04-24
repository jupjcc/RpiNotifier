package mainPkg;

//import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
//import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import configPkg.NotifierSettings;
import enumerationsPkg.Paths;
//import utilsPkg.Os;
import utilsPkg.StatusLogger;
import utilsPkg.TextLogger;
import utilsPkg.Utils;
//import java.util.TimeZone;

public class Main extends Shell
{
   public static String PROG_ID = "RPI Notifier v20210421 Manual test inputs for checking out interface";
   static final int DISPLAY_INTERVAL_MSEC = 1000;
   public static Paths Paths;
   public static StatusLogger StatusLog;
   public static NotifierSettings Settings; 
   private static Display display;
   protected static StyledText stxtStatusLog = null;
   private static Text txtTimer;
   private static Text txtUserVal;
//   private static Button chkPush;
//   private static Button chkEmail;
//   private static Button chkPhone;
   private static Button chkAddedText;
   private static Text txtAddedText;

   private static long dispT0 = 0;
   private static Text txtClk;
   private static Button chkStopWatchOn = null;
   private static float timerOld = 0;
   private static Text txtCurrentLogFile;
   private static TextLogger sLog;
   /**
    * Launch the application.
    * @param args
    */
   public static void main(String args[])
   {
      try
      {
         // addJarToClassPath isn't working now so is commented out
         // the proper swt lib file must be manually selected by BuildPath
         // So here is the calling code to dynamically select the correct SWT jar:
//         File swtJar = new File(Os.getArchFilename("lib/swt"));
//         Os.addJarToClasspath(swtJar);
         
         display = Display.getDefault();
         NotifierSettings sClass = new NotifierSettings();
         String runtimePath = NotifierSettings.DefaultRuntimeFolder; 
         if (args.length == 0)
         {
            System.out.println("No runtime folder specified on command line; using " +
                     runtimePath);
//            System.exit(-1);
         }
         Paths = new Paths(Utils.TerminatedPath(runtimePath));
         Main shell = new Main(display);
         shell.open();
         shell.layout();
         sLog.OpenNewLogFile();
         txtCurrentLogFile.setText(sLog.LogFileName);
         Settings = sClass.GetInstance(StatusLog);
//         Settings.SetLogger(StatusLog);
         txtTimer.setText(Utils.DispFltStr(Settings.StopWatchSec, 0));
         txtUserVal.setText(Utils.DispFltStr(Settings.UserVal, 0));
         chkAddedText.setSelection(Settings.AddedTextOn);
         chkStopWatchOn.setSelection(Settings.StopWatchRunning);
//         chkPush.setSelection(Settings.PushOn);
//         chkEmail.setSelection(Settings.EmailOn);
//         chkPhone.setSelection(Settings.PhoneOn);
         chkAddedText.setSelection(Settings.AddedTextOn);
         txtAddedText.setText(Settings.AddedText);
         dispT0 = System.currentTimeMillis();
         Runnable displayTimer = new Runnable()
         {
            public void run()
            {
               updateDisplay();
               display.timerExec(DISPLAY_INTERVAL_MSEC, this);
            }
         };
         display.timerExec(DISPLAY_INTERVAL_MSEC, displayTimer);
         while (!shell.isDisposed())
         {
            if (!display.readAndDispatch())
            {
               display.sleep();
            }
         }
         Settings.WriteJsonObject();
         sLog.CloseLogFile();
      } catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   /**
    * Create the shell.
    * @param display
    */
   public Main(Display display)
   {
      super(display, SWT.SHELL_TRIM);
      FormLayout formLayout = new FormLayout();
      formLayout.marginRight = 1;
      setLayout(formLayout);
      
      createContents(display);
   }

   /**
    * Create contents of the shell.
    */
   protected void createContents(Display disp)
   {
      setText(PROG_ID);
      setSize(800, 500);
      Group grpStatusLog = new Group(this, SWT.NONE);
      grpStatusLog.setText("Status Log");
      grpStatusLog.setLayout(new GridLayout(4, false));
      FormData fd_grpStatusLog = new FormData();
      fd_grpStatusLog.top = new FormAttachment(60);
      fd_grpStatusLog.right = new FormAttachment(100, -2);
      fd_grpStatusLog.bottom = new FormAttachment(100, -1);
      fd_grpStatusLog.left = new FormAttachment(0, 2);
      grpStatusLog.setLayoutData(fd_grpStatusLog);
      Button btnClearLog = new Button(grpStatusLog, SWT.NONE);
      btnClearLog.setText("Clear");
      btnClearLog.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
      {
         public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
         {
            stxtStatusLog.setText("");
         }
      });
      Button btnNewLog = new Button(grpStatusLog, SWT.NONE);
      btnNewLog.setText("New File");
      btnNewLog.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
      {
         public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
         {
            sLog.OpenNewLogFile();
            txtCurrentLogFile.setText(sLog.LogFileName);
         }
      });
      Label lblCurrentLogFile = new Label(grpStatusLog, SWT.NONE);
      lblCurrentLogFile.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
      lblCurrentLogFile.setText("CurrentFile");
      txtCurrentLogFile = new Text(grpStatusLog, SWT.BORDER);
      txtCurrentLogFile.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
      stxtStatusLog = new StyledText(grpStatusLog, 
               SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.V_SCROLL);
      stxtStatusLog.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
      StatusLog = new StatusLogger(disp, stxtStatusLog);
      sLog = new TextLogger("Session");
      StatusLog.SetFileLogger(sLog);
      
      Group grpTimer = new Group(this, SWT.NONE);
      grpTimer.setText("Stopwatch");
      grpTimer.setLayout(new GridLayout(6, false));
      FormData fd_grpTimer = new FormData();
      fd_grpTimer.right = new FormAttachment(100, -2);
      fd_grpTimer.top = new FormAttachment(6, 6);
      fd_grpTimer.left = new FormAttachment(0, 2);
      grpTimer.setLayoutData(fd_grpTimer);
      
      chkStopWatchOn = new Button(grpTimer, SWT.CHECK);
      GridData gd_chkStopWatchOn = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
      gd_chkStopWatchOn.widthHint = 60;
      chkStopWatchOn.setLayoutData(gd_chkStopWatchOn);
      chkStopWatchOn.setText("Run");
      chkStopWatchOn.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
      {
         public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
         {
            Settings.StopWatchRunning = chkStopWatchOn.getSelection();
            setOptionSwitch("Stop Watch", chkStopWatchOn.getSelection());
         }
      });
      
      Button btnResetTimer = new Button(grpTimer, SWT.NONE);
      btnResetTimer.setText("Reset");
      btnResetTimer.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
      {
         public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
         {
            dispT0 = System.currentTimeMillis();
            txtTimer.setText("0");
            sendNotify("Server timer reset\n");
            timerOld = 0;
         }
      });
      
      txtTimer = new Text(grpTimer, SWT.BORDER | SWT.CENTER);
      GridData gd_txtTimer = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
      gd_txtTimer.widthHint = 50;
      txtTimer.setLayoutData(gd_txtTimer);
      txtTimer.addModifyListener(new ModifyListener()
      {
         public void modifyText(ModifyEvent e)
         {
            try
            {
               float f = Float.parseFloat(txtTimer.getText());
               Settings.StopWatchSec = f;
            }
            catch (Exception xx)
            {}
         }
      });
      Label lblTimerSec = new Label(grpTimer, SWT.NONE);
      lblTimerSec.setText("sec.");
      
      Group grpOpts = new Group(this, SWT.NONE);
      grpOpts.setLayout(new GridLayout(6, false));
      FormData fd_grpOpts = new FormData();
      fd_grpOpts.top = new FormAttachment(grpTimer);
      
      Label lblClk = new Label(grpTimer, SWT.NONE);
      GridData gd_lblClk = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
      gd_lblClk.horizontalIndent = 20;
      lblClk.setLayoutData(gd_lblClk);
      lblClk.setText("Clock time");
      
      txtClk = new Text(grpTimer, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
      GridData gd_txtClk = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
      gd_txtClk.widthHint = 150;
      txtClk.setLayoutData(gd_txtClk);
      fd_grpOpts.right = new FormAttachment(100, -2);
      fd_grpOpts.left = new FormAttachment(0, 2);
      grpOpts.setLayoutData(fd_grpOpts);
      
      Label lblUserData = new Label(grpOpts, SWT.NONE);
      lblUserData.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
      lblUserData.setText("User Data");
      
      txtUserVal = new Text(grpOpts, SWT.BORDER | SWT.CENTER);
      txtUserVal.setText("0");
      txtUserVal.setToolTipText("numeric value");
      GridData gd_txtUserVal = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
      gd_txtUserVal.widthHint = 60;
      gd_txtUserVal.heightHint = 14;
      txtUserVal.setLayoutData(gd_txtUserVal);
      
      Group grpAddedText = new Group(this, SWT.NONE);
      FormData fd_grpAddedText = new FormData();
      fd_grpAddedText.top = new FormAttachment(grpOpts, 1);
      
//      chkPush = new Button(grpOpts, SWT.CHECK);
//      GridData gd_chkPush = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
//      gd_chkPush.horizontalIndent = 12;
//      chkPush.setLayoutData(gd_chkPush);
//      chkPush.setText("Push");
//      chkPush.setToolTipText("Send notification");
//      chkPush.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
//      {
//         public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
//         {
//            Settings.PushOn = chkPush.getSelection();
//            setOptionSwitch("Push", chkPush.getSelection());
//         }
//      });
//      
//      chkEmail = new Button(grpOpts, SWT.CHECK);
//      GridData gd_chkEmail = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
//      gd_chkEmail.horizontalIndent = 12;
//      chkEmail.setLayoutData(gd_chkEmail);
//      chkEmail.setText("Email");
////      chkEmail.setEnabled(false);
//      chkEmail.setToolTipText("Send email");
//      chkEmail.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
//      {
//         public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
//         {
//            Settings.EmailOn = chkEmail.getSelection();
//            setOptionSwitch("Email", chkEmail.getSelection());
//         }
//      });
//      chkEmail.setToolTipText("Emails not supported yet - tbd if important enough");
//      
//      chkPhone = new Button(grpOpts, SWT.CHECK);
//      GridData gd_chkPhone = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
//      gd_chkPhone.horizontalIndent = 12;
//      chkPhone.setLayoutData(gd_chkPhone);
//      chkPhone.setText("Phone");
//      chkPhone.setEnabled(false);
//      chkPhone.setToolTipText("Send Phone");
//      chkPhone.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
//      {
//         public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
//         {
//            Settings.PhoneOn = chkPhone.getSelection();
//            setOptionSwitch("Phone", chkPhone.getSelection());
//         }
//      });
//      chkPhone.setToolTipText("Text msging not supported yet - tbd if important enough");
      
      Button btnSend = new Button(grpOpts, SWT.NONE);
      GridData gd_btnSend = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
      gd_btnSend.horizontalIndent = 24;
      btnSend.setLayoutData(gd_btnSend);
      btnSend.setText("Send Notification Data");
      btnSend.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
      {
         public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
         {
            sendMsg();
         }
      });
      GridLayout gl_grpAddedText = new GridLayout(2, false);
      gl_grpAddedText.verticalSpacing = 1;
      grpAddedText.setLayout(gl_grpAddedText);
      
      fd_grpAddedText.bottom = new FormAttachment(grpStatusLog);
      fd_grpAddedText.right = new FormAttachment(100, -1);
      fd_grpAddedText.left = new FormAttachment(0, 1);
      grpAddedText.setLayoutData(fd_grpAddedText);
      
      chkAddedText = new Button(grpAddedText, SWT.CHECK);
      
      Button btnClearAddedText = new Button(grpAddedText, SWT.NONE);
      btnClearAddedText.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));
      btnClearAddedText.setText("Clear");
      btnClearAddedText.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
      {
         public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
         {
            txtAddedText.setText("");
            Settings.AddedText = "";
         }
      });
      txtAddedText = new Text(grpAddedText, SWT.BORDER | SWT.MULTI);
      txtAddedText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
      chkAddedText.setText("Added Text");
      chkAddedText.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter()
      {
         public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
         {
            Settings.AddedTextOn = chkAddedText.getSelection();
            setOptionSwitch("AddedText", chkAddedText.getSelection());
            if (chkAddedText.getSelection())
            {
               Settings.AddedText = txtAddedText.getText();
            }
         }
      });
      
      txtUserVal.addModifyListener(new ModifyListener()
      {
         public void modifyText(ModifyEvent e)
         {
            try
            {
               float f = Float.parseFloat(txtUserVal.getText());
               Settings.UserVal = f;
               sendNotify("Server timer reset\n");
            }
            catch (Exception xx)
            {}
         }
      });
   }
      
   // spawn a python file to notify all listeners
   static void sendNotify(String msg)
   {
      String[] cmd =
            {
               "python",
               "./notificationSscript.py",
               msg
            };
      try
      {
         Runtime.getRuntime().exec(cmd);
      }
      catch (Exception se)
      {
         StatusLog.LogStatus(Utils.ExceptionString(se) +
              "\n Exception sending notification\n", StatusLogger.LogLevel.ERROR);
      }
   }
   
   private void setOptionSwitch(String name, boolean setOn)
   {
      String onOff = setOn? "On": "Off";
      StatusLog.LogStatus(name + " set to " + onOff + "\n",
                          StatusLogger.LogLevel.INFO, true);
   }
   
   private static void updateDisplay()
   {
      DateFormat dFmtClk=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//      dFmtClk.setTimeZone(TimeZone.getTimeZone("GMT"));
      final String curTime = dFmtClk.format(Calendar.getInstance().getTime());
      display.syncExec(new Runnable()
      {
         public void run()
         {
            if (chkStopWatchOn.getSelection())
            {
               long dtTimer = System.currentTimeMillis() - dispT0;
               double dtf = dtTimer / 1000.;
               Settings.StopWatchSec = (float)dtf;
               if (Settings.StopWatchSec > 60 &&
                   timerOld < 60)
               {
                  sendNotify("Timer > 60 sec");
               }
               txtTimer.setText(Utils.DispFltStr(Settings.StopWatchSec, 2));
            }
            txtClk.setText(curTime);
         } // end run
      }); // end display.asyncExec
//      sendMsg();
   }
   private static void sendMsg()
   {
//      if (chkPush.getSelection() || chkEmail.getSelection() ||
//          chkPhone.getSelection())
      {
         String active = chkStopWatchOn.getSelection() ?
               "Running @" + Utils.DispFltStr(Settings.StopWatchSec, 1) +
                      " sec\n": "Off\n";
         String msg = "Stop Watch is " + active;
         if (chkAddedText.getSelection())
         {
            msg += "Added Text:\n" + Settings.AddedText;
         }
         msg += "\n";
         
         String opts = "";
//         if (chkPush.getSelection())
         {
            opts += "push ";
         }
//         if (chkEmail.getSelection())
//         {
//            opts += "email ";
//         }
//         if (chkPhone.getSelection())
//         {
//            opts += "phone";
//         }
         try
         {
            SpontitIo.Send(msg, opts);
            StatusLog.LogStatus("Message Sent <\n" + msg + ">\n", StatusLogger.LogLevel.PROMPT);
         }
         catch (Exception se)
         {
            StatusLog.LogStatus(Utils.ExceptionString(se) +
                     "\nexception sending message\n", StatusLogger.LogLevel.ERROR);
            
         }
      }
//      else
//      {
//         StatusLog.LogStatus("At least one of Push, Email, or Phone option must be checked\n", 
//                  StatusLogger.LogLevel.WARN);
//      }
   }
   public static void LogMsg(String msg)
   {
      StatusLog.LogStatus(msg,StatusLogger.LogLevel.INFO, false);
   }
   public static void LogMsg(String msg, boolean timeStamp)
   {
      StatusLog.LogStatus(msg,StatusLogger.LogLevel.INFO, timeStamp);
   }
      
   @Override
   protected void checkSubclass()
   {
      // Disable the check that prevents subclassing of SWT components
   }
}
