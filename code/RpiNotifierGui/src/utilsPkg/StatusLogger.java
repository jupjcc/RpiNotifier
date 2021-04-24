package utilsPkg;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;

import enumerationsPkg.Colors;

public class StatusLogger
{
   public enum LogLevel
   {
      INFO, WARN, ERROR, PROMPT
   }
   
   private StyledText stxt;
   private Display myDisplay;
   private TextLogger fileLogger = null;
   
   public StatusLogger(Display disp, StyledText stxt)
   {
      this.myDisplay = disp;
      this.stxt = stxt;
   }
   
   public void SetFileLogger(TextLogger tlog)
   {
      fileLogger = tlog;
   }
   
   public void LogStatus(final String msgArg, final LogLevel level, boolean timeStamp)
   {
      final String msg;
      if (timeStamp)
      {
         msg = TimeStamps.TimeOnly() + ": " + msgArg;
      }
      else
      {
         msg = msgArg;
      }
      myDisplay.asyncExec(new Runnable()
      {
         public void run()
         {
            if (level != LogLevel.INFO)
            {
               int cPos = stxt.getText().length();
               stxt.append(msg);
               StyleRange sr=new StyleRange();
               sr.start=cPos;
               sr.length=msg.length();
               if (level == LogLevel.ERROR)
               {
                  sr.foreground = Colors.RED;
               }
               else if (level == LogLevel.PROMPT)
               {
                  sr.foreground = Colors.BLUE;
               }
               else
               {
                  sr.foreground = Colors.ORANGE;
               }
               sr.fontStyle=SWT.BOLD;
               stxt.setStyleRange(sr);
            }
            else
            {
               stxt.append(msg);
            }     // end if lvl1=info
            if (fileLogger != null)
            {
               fileLogger.WriteToLog(msg);
            }
            // move caret to end
            stxt.setSelection(stxt.getCharCount());
         }
      });
   }
   
   public void LogStatus(final String msg, final LogLevel level)
   {
      LogStatus(msg, level, false);
   }
   
   public void LogStatus(final String msg)
   {
      LogStatus(msg, LogLevel.INFO, true);
   }
   
}
