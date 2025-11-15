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

  public static void startProcess(String command) throws Exception {
    ProcessBuilder builder = new ProcessBuilder();
    var osEnviroment = figureOutOSCommand();
    builder.command(osEnviroment[0], osEnviroment[1], command.trim());
    builder.start();
  }

  public static void killProcess(long pid) throws Exception {
    var osEnviroment = figureOutOSCommand();
    ProcessBuilder builder = new ProcessBuilder();
    builder.command(osEnviroment[0] ,osEnviroment[1] , "kill " + pid);
    builder.start();
  }

  public static void runNanoEditor(String filePath) throws Exception {
    ProcessBuilder builder = new ProcessBuilder();
    builder.command("sh", "-c", "nano " + filePath);
    builder.inheritIO();
    Process process = builder.start();
    process.waitFor();
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
