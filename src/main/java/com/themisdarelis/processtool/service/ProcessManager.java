package com.themisdarelis.processtool.service;

import com.themisdarelis.processtool.model.ProcessInfo;
import java.util.ArrayList;


public class ProcessManager {
  public static void startProcess(String command) throws Exception {
    ProcessBuilder builder = new ProcessBuilder();
    builder.command("sh", "-c", command);
    builder.start();
  }

  public static void killProcess(long pid) throws Exception {
    ProcessBuilder builder = new ProcessBuilder();
    builder.command("sh", "-c", "kill " + pid);
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
