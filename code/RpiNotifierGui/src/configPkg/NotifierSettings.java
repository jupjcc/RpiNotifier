package configPkg;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;

import enumerationsPkg.Paths;
import utilsPkg.StatusLogger;
import utilsPkg.Utils;

public class NotifierSettings
{
   public static final String SPONTIT_ID = "john_cockerham6705";
   public static final String SPONTIT_CODE = "EAUPYSHJGD2MHMT68CKVLE43F1N9AEYXZ20RVKMICM5IRFHZF7EQ935IQLDMGUYCPF9KPI6SPD9CT5I908AWIP67P9B53JSAUQJM";

   String settingsFileName;
   public static String DefaultRuntimeFolder;
   //  the following are populated by jsontoGson
   public float StopWatchSec = 0;
   public float UserVal = 0;
   public boolean StopWatchRunning = false;
//   public boolean PushOn = false;
//   public boolean EmailOn = false;
//   public boolean PhoneOn = false;
   public boolean AddedTextOn = false;
   public String AddedText = "manually entered text";
   //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
   
   private static StatusLogger st;
   private static NotifierSettings instance;
   
   public NotifierSettings()
   {
      String os = System.getProperty("os.name");
      DefaultRuntimeFolder = os.toLowerCase().contains("win")?
              "c:\\RpiNotifier\\bin" : "/home/pi/rpinotifier/runtime/bin";
   }
   
   public NotifierSettings GetInstance(StatusLogger s)
   {
      st = s;
      readJsonObjectFromFile();
      return instance;
   }
   
   public void readJsonObjectFromFile()
   {
      Gson gson = new Gson();
      InputStreamReader isReader;
      instance = this;
      settingsFileName = Paths.CONFIGS_PATH+ "NotifierSettings.json";
      st.LogStatus("Reading program settings from " + settingsFileName + "\n");
      try
      {
         isReader = new InputStreamReader(new FileInputStream(settingsFileName));
         JsonReader jr = new JsonReader(isReader);
         try
         {
            instance = gson.fromJson(isReader, NotifierSettings.class);
         }
         catch (JsonParseException je)
         {
            st.LogStatus(Utils.ExceptionString(je) + 
                     "\nException parsing Settings file:" +
                     "\n" + settingsFileName + "\nUsing defaults\n", StatusLogger.LogLevel.WARN);
            instance = this;
         }
         isReader.close();
         jr.close();
         if (instance == null)
         {
            st.LogStatus("\nError reading Settings file:" +
                     "\n" + settingsFileName + "\nUsing defaults\n", StatusLogger.LogLevel.WARN);
            instance = this;
         }
      }
      catch (Exception je)
      {
         st.LogStatus(Utils.ExceptionString(je) + 
                "\nException reading Settings file:" +
                "\n" + settingsFileName + "\nUsing defaults\n", StatusLogger.LogLevel.WARN);
      }
   }
   
   public void WriteJsonObject()
   {
      Gson gson = new Gson();
      try
      {
         File f = new File(settingsFileName);
         f.createNewFile();
         FileWriter fw = new FileWriter(f);
         gson.toJson(this, fw);
         fw.close();
      }
      catch (Exception je)
      {
         st.LogStatus(Utils.ExceptionString(je) + 
                  "\nException saving to settings file:" +
                  "\n" + settingsFileName + "\n", StatusLogger.LogLevel.WARN);

      }
   }
   
}
