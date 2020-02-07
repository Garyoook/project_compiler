package jav.wacc;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class ErrorMessage {

  private static List<String> errorMessages = new ArrayList<>();
  private static boolean syntactic = false;

  public static void add(String error, int exitCode) {
    errorMessages.add(error + ", exit(" + exitCode + ")");
    if (exitCode == 100) {
      syntactic = true;
    }
  }

  public static String errorWriter() {
    StringBuilder sb = new StringBuilder();
    sb.append("Total " + errorMessages.size() + " errors.\n");
    for (String e : errorMessages) {
      sb.append(e);
      sb.append('\n');
    }
    return sb.toString();
  }

  public static void tryExit() {
    if (errorMessages.size()>0) {
      if (syntactic) {
        exit(100);
      } else {
        exit(200);
      }
    }
  }
}
