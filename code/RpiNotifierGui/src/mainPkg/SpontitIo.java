package mainPkg;
/*   using the Spontit Notification Python package, send push/email/text
 *    messages to IOS and/or browsers
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;

import enumerationsPkg.Paths;

public class SpontitIo
{
   public static void Send(String msg, String options) throws Exception
   {
       String[] cmd = {
         "python",
         Paths.PY_PATH + "spontitApi.py",
         msg,
         options
       };
       Process p = Runtime.getRuntime().exec(cmd);
       BufferedReader stdInput = new BufferedReader(new 
                InputStreamReader(p.getInputStream()));

       BufferedReader stdError = new BufferedReader(new 
                InputStreamReader(p.getErrorStream()));
       String s;
       // read the output from the command
       System.out.println("Here is the standard output of the command:\n");
       while ((s=stdInput.readLine()) != null)
       {
          System.out.println(s);
       }
       String err = "";
       // read any errors from the attempted command
       System.out.println("Here is the standard error of the command (if any):\n");
       while ((s = stdError.readLine()) != null)
       {
          err += s + "\n";
//           System.out.println(s);
       }
       if (err.length() > 0)
       {
          throw new Exception("Python script error:\n"+err);
       }
   }
}
