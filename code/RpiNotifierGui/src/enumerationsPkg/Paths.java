package enumerationsPkg;

import utilsPkg.Utils;

public class Paths
{
   public static String EXE_PATH;
   public static String PY_PATH;
   public static String CONFIGS_PATH;
   public static String DATA_LOGS_PATH;
   static String basePath;
   
   public Paths(String runtimePath)
   {
      int binPos = runtimePath.toLowerCase().lastIndexOf("bin");
      basePath = runtimePath.substring(0, binPos);
      EXE_PATH = Utils.TerminatedPath(basePath);
      PY_PATH = Utils.TerminatedPath(basePath + "py");
      CONFIGS_PATH = Utils.TerminatedPath(basePath + "config");
      DATA_LOGS_PATH = Utils.TerminatedPath(basePath + "logs");
   }
}
