package jav.wacc;

import java.util.List;

import static java.lang.System.exit;

public class ErrorMessage {
  private static List<String> syntacticError;
  private static List<String> semanticError;

  public static void add(String errorMessage, int exitCode) {
    if (exitCode==100) {
      syntacticError.add(errorMessage);
    } else {
      semanticError.add(errorMessage);
    }
  }

  public static void errorWriter() {
    StringBuilder sb = new StringBuilder();
    if (syntacticError.size()>0) {
      sb.append("Errors detected during compilation! Exit code 100 returned.");
      for (String e : syntacticError) {
        sb.append(e+'\n');
      }
    } else {
      sb.append("Errors detected during compilation! Exit code 200 returned.");
      for (String e : semanticError) {
        sb.append(e+'\n');
      }
    }
    System.out.println(sb.toString());
    if (syntacticError.size()>0) {
      exit(100);
    } else {
      exit(200);
    }
  }

}
