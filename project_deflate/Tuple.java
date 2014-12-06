package deflatecompression;

public class Tuple {
    private int offset;
    private int stringLength;
    private String nextChar;

    Tuple(int a, int b, String c) {
        offset = a;
        stringLength = b;
        nextChar = c;
    }
    
    public int getOffset()
    {
        return offset;
    }
    public int getStringLength()
    {
        return stringLength;
    }
    public String getNextChar()
    {
        return nextChar;
    }

    @Override
    public String toString() {
        return offset + "," + stringLength + "," + nextChar;
    }
}
