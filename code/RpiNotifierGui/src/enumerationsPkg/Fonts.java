package enumerationsPkg;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

public class Fonts
{
   static public Font CmdTextFont=new Font(
                      Display.getDefault(), "Courier New", 8, SWT.NORMAL);
   static public Font DspTextFont=new Font(
                      Display.getDefault(), "Courier New", 8, SWT.NORMAL);
   static public Font HelpTextFont=new Font(
                       Display.getDefault(), "Arial", 8, SWT.NORMAL);
   static public Font LabelFont=new Font(
                      Display.getDefault(), "Arial", 8, SWT.NORMAL);
   static public Font BoldFont=new Font(Display.getDefault(),
                                        "Tahoma", 8, SWT.BOLD);
   static public Font BigBoldFont=new Font(Display.getDefault(),
                           "Tahoma", 10, SWT.BOLD);
   static public Font BoldFixed=new Font(Display.getDefault(),
                                        "Courier New", 8, SWT.BOLD);
   static public Font BigFixed=new Font(Display.getDefault(),
                           "Courier New", 10, SWT.NORMAL);

   //  swt system resources are normally freed when program exits
   //  but if a problem occurs during exit we might have a resouce leak
   //  this code may help in such a situation
   public static void Close()
   {
      CmdTextFont.dispose();
      DspTextFont.dispose();
      HelpTextFont.dispose();
      LabelFont.dispose();
      BoldFont.dispose();
      BigBoldFont.dispose();
      BoldFixed.dispose();
      BigFixed.dispose();
   }
}
