package deflatecompression;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Driver {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        String input = "";
        String result = "";
        LZ77 compressor = new LZ77(999, 999);
        NumRemover remover = new NumRemover();
        Scanner fileIn = new Scanner(new File("Input.txt"));
        PrintWriter out = new PrintWriter(new FileWriter("lz77Encoded.txt"));
        while (fileIn.hasNext()) {
            String temp = fileIn.nextLine() + " ";
            input += temp;
            System.out.println(temp);
        }
        result = remover.RemoveNums(input);
        result = compressor.compress(result);
        //out.println(result);
        result = compressor.compressOutput(result);
        //out.println(result);
        //result = compressor.expandOutput(input);
        //out.println(result);
        //result = compressor.decompress(input);
        out.println(result);
        out.close();
    }
}