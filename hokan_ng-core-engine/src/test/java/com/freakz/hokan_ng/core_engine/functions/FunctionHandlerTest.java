package com.freakz.hokan_ng.core_engine.functions;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pairio on 23.6.2014.
 *
 * @author Petri Airio (petri.j.airio@gmail.com)
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {})
public class FunctionHandlerTest {

  /*  @Autowired*/
  private FunctionHandler functionHandler = new FunctionHandlerImpl();

  @Test
  public void testFunctionHandler() {
//    String inputLine = "ddasds @Test(1244 , ffsfddfs @bar(fsdffsds, fsfsdf) ) ffsd fadsfd";
    String _inputLine = "ddasds @Test( ffsd f@Testii(cccXCX  cccxccx)dsfd) @Toka(fjfj @Aaaa() @Bbb(ffff  @Cfff(ccccc) bbbb)  fjf) @Kolkki(ffjksfkd, ffsfd) gffdfdg";

    int recursionLevel = 1;
    Map<Integer, List<HokanFunction>> levelMap = new HashMap<>();
    List<String> toParse = new ArrayList<>();
    toParse.add(_inputLine);
    while (toParse.size() > 0) {
      String line = toParse.remove(0);

      System.out.println("inputLine   : " + line);
      boolean matches = functionHandler.hasFunction(line);
      System.out.println("hasFunction : " + matches);
      List<HokanFunction> result = null;
      if (matches) {
        result = functionHandler.parseFunctions(line);
        levelMap.put(recursionLevel, result);
        if (result != null) {
          for (HokanFunction hokanFunction : result) {
            System.out.format("hokanFunction[%3d] : %s\n", recursionLevel, hokanFunction.functionString);
            toParse.add(hokanFunction.functionArgs);
          }
          recursionLevel++;
        }
      }
    }

    int offf = 0;
  }

}
