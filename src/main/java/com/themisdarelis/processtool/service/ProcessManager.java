package com.themisdarelis.processtool.service;

import com.themisdarelis.processtool.model.ProcessInfo;
import java.util.ArrayList;

//https://docs.oracle.com/javase/8/docs/api/java/lang/ProcessBuilder.html
public class ProcessManager {

  //vlepoume poio einai to leitourgiko systima
  //gia mac den xero ti na valo kai den eho mac na to dokimaso
  private static String[] figureOutOSCommand() {
    String os = System.getProperty("os.name").toLowerCase();
    if (os.contains("win")) {
      //auto tora tha doulepsi kai se windows 10 kai 11? poios xerei?...
      return new String[]{ "cmd.exe", "/c"};
    } else {
      return new String[]{"sh", "-c"};
    }
  }

  private static Process executeCommand(String command) throws Exception {
    ProcessBuilder builder = new ProcessBuilder();
    var osEnvironment = figureOutOSCommand();
    builder.command(osEnvironment[0], osEnvironment[1], command.trim());
    return builder.start();
  }

  public static void startProcess(String command) throws Exception {
    executeCommand(command);
  }

  public static void killProcess(long pid) throws Exception {
    executeCommand("kill " + pid);
  }

  public static ArrayList<ProcessInfo> getCurrentProcesses(){
    ArrayList<ProcessInfo> processList = new ArrayList<>();
    //παρε τα προσεσις
    ProcessHandle.allProcesses().forEach(process -> {
      ProcessHandle.Info info = process.info();
      String command = info.command().orElse("unknown");
      String user = info.user().orElse("unknown");
      //καλυτερα οχι τα ρουτ προσεσις, μπορει να γινει ζημια!
      //kai ola ta unknown exo
        if (!user.equals("root")&& !command.equals("unknown")&& !user.equals("unknown")) {
          processList.add(new ProcessInfo(command, process.pid(), user));
          }
    });
    return processList;
  }
}
