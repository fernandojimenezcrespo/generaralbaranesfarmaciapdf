 
package generaralbaranesfarmaciapdf.utils;

 
public class Utils {
   
public Integer  isNumeric(String input) {
  try {
    return Integer.parseInt(input);
    
  }
  catch (NumberFormatException e) {
    // s is not numeric
    return 0;
  }
}    
}
