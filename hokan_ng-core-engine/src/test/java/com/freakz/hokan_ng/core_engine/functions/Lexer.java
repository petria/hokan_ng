package com.freakz.hokan_ng.core_engine.functions;

/**
 * Created by pairio on 25.6.2014.
 *
 * @author Petri Airio
 */

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
  public static enum TokenType {
    // Token types cannot have underscores
    COMMAND("@\\p{Upper}\\p{Lower}+\\(.*?\\)|@\\p{Upper}\\p{Lower}+\\(\\)"),

    //    COMMAND("@\\p{Upper}\\p{Lower}+\\([^(]+\\)"),
    NUMBER("-?[0-9]+"), BINARYOP("[*|/|+|-]"), WHITESPACE("[ \t\f\r\n]+");

    public final String pattern;

    private TokenType(String pattern) {
      this.pattern = pattern;
    }
  }

  public static class Token {

    public TokenType type;
    public String data;
    public int start;
    public int end;

    public Token(TokenType type, String data, int start, int end) {
      this.type = type;
      this.data = data;
      this.start = start;
      this.end = end;
    }

    @Override
    public String toString() {
      return String.format("(%s %s)", type.name(), data);
    }
  }

  public static ArrayList<Token> lex(String input) {
    // The tokens to return
    ArrayList<Token> tokens = new ArrayList<Token>();

    // Lexer logic begins here
    StringBuffer tokenPatternsBuffer = new StringBuffer();
    for (TokenType tokenType : TokenType.values())
      tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
    Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));

    // Begin matching tokens
    Matcher matcher = tokenPatterns.matcher(input);
    while (matcher.find()) {
      if (matcher.group(TokenType.COMMAND.name()) != null) {
        Token t = new Token(TokenType.COMMAND, matcher.group(TokenType.COMMAND.name()), matcher.start(), matcher.end());
        tokens.add(t);
        System.out.println("--> " + t);
      }
/*      else
      if (matcher.group(TokenType.NUMBER.name()) != null) {
        tokens.add(new Token(TokenType.NUMBER, matcher.group(TokenType.NUMBER.name())));
        continue;
      } else if (matcher.group(TokenType.BINARYOP.name()) != null) {
        tokens.add(new Token(TokenType.BINARYOP, matcher.group(TokenType.BINARYOP.name())));
        continue;
      } else if (matcher.group(TokenType.WHITESPACE.name()) != null)
        continue;*/
    }

    return tokens;
  }

  public static void main(String[] args) {
//    String input = "11 + 22 - 33 + @Fb() Gfdsh(fsdkl fdslkfsdl @Foo(fds f sd fd @Fofofof(param1) fdsfsd @Crap() ffufuf)) vcvc @Some(stuff) fdfsdf";
    String input = "11 + @Bf() 22 - 33 + F:0 Gfdsh(fsdkl fdslkfsdl @Foo(fds f sd fd @Fofofof(param1) fdsfsd F:1 ffufuf)) vcvc F:2 fdfsdf";
    System.out.println("input :: " + input);

    StringBuilder processed = new StringBuilder(input);
    int c = 0;
    while (true) {
      // Create tokens and print them
      ArrayList<Token> tokens = lex(processed.toString());
      if (tokens.size() == 0) {
        break;
      }
      int offset = 0;
      for (Token token : tokens) {
        processed.replace(token.start - offset, token.end - offset, "{}");
        int tokenLen = token.end - token.start;
        offset += (tokenLen - 2);
      }
      String tmp = processed.toString();
      for (Token token : tokens) {
        tmp = tmp.replaceFirst("\\{\\}", "F:" + c);
        c++;
        int tokenLen = token.end - token.start;
        offset += (tokenLen - 2);
      }
      processed = new StringBuilder(tmp);
    }

    System.out.println("output :: " + processed.toString());


  }
}
