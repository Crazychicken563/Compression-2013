/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package deflatecompression;

/**
 *
 * @author Seva
 */
public class NumRemover {
    public String RemoveNums(String input)
    {
        StringBuilder output = new StringBuilder();
        for(int i=0;i<input.length()-1;i++)
        {
            System.out.println("Converting to Letters Only... "+i*100/input.length()+"%");
            if(Character.isLetter(input.charAt(i)) || input.charAt(i) == ' ' || input.charAt(i) == '.')
            {
                output.append(input.charAt(i));
            }
        }
        return output.toString();
    }
}
