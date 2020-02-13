package doc.wacc.utils;

import java.util.ArrayList;
import java.util.List;

import static java.lang.System.exit;

public class ErrorMessage {
  private static List<String> syntacticErrorList = new ArrayList<>();
  private static List<String> semanticErrorList = new ArrayList<>();

  public static void addSyntaxError(String errorMessage) {
    syntacticErrorList.add("Syntax Error: " + errorMessage);
  }

  public static void addSemanticError(String errorMessage) {
     semanticErrorList.add("Semantic Error: " + errorMessage);
  }

  public static void errorWriter() {
    StringBuilder sb = new StringBuilder();
    if (syntacticErrorList.size() == 0) {
      if (semanticErrorList.size() > 0) {
        for (String msg : semanticErrorList) {
          sb.append(msg).append("\n");
        }
        sb.append("Exit code 200 returned");
      }
    } else {
      for (String msg : syntacticErrorList) {
        sb.append(msg).append("\n");
      }
      sb.append("Exit code 100 returned");
    }
    System.out.println(sb.toString());
    if (syntacticErrorList.size() > 0) {
      exit(100);
    } else if (semanticErrorList.size() > 0) {
      exit(200);
    }
  }

  public static boolean hasError() {
    return (semanticErrorList.size()>0) || (syntacticErrorList.size()>0);
  }

}
