package doc.wacc.utils;

import java.util.List;

import static java.lang.System.exit;

public class ErrorMessage {
  private static List<String> syntacticErrorList;
  private static List<String> semanticErrorList;

  public static void addSyntaxError(String errorMessage) {
    syntacticErrorList.add(errorMessage);
  }

  public static void addSemanticError(String errorMessage) {
     semanticErrorList.add(errorMessage);
  }

  public static void errorWriter() {
    StringBuilder sb = new StringBuilder();
    if (syntacticErrorList.size() == 0) {
      if (semanticErrorList.size() > 0) {
        for (String msg : semanticErrorList) {
          sb.append(msg).append("\n");
        }
      } else {
        sb.append("done\n");
      }
    }
    System.out.println(sb.toString());
    if (semanticErrorList.size() > 0) {
      exit(200);
    }
  }

}
