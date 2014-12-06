package deflatecompression;

import java.util.*;

class LZ77 {

    int searchBufferSize;
    int lookAheadBufferSize;

    public LZ77() {
        searchBufferSize = 63;
        lookAheadBufferSize = 7;
    }

    public LZ77(int searchBufferSize, int lookAheadBufferSize) {
        this.searchBufferSize = searchBufferSize;
        this.lookAheadBufferSize = lookAheadBufferSize;
    }

    public String compress(String input) {
        StringBuilder output = new StringBuilder();
        ArrayList<Tuple> encodedData = new ArrayList<>();
        Tuple currTuple;
        String currPattern;
        int matchLength;
        int matchIndex;
        int charCount = 0;
        int searchWindowStart;
        int lookAheadWindowEnd;

        while (charCount < input.length()) {
            System.out.println("Compressing... " + charCount * 100 / input.length() + "%");
            searchWindowStart = 0;
            if (charCount - searchBufferSize > 0) {
                searchWindowStart = charCount - searchBufferSize;
            }
            lookAheadWindowEnd = input.length();
            if (charCount + lookAheadBufferSize < input.length()) {
                lookAheadWindowEnd = charCount + lookAheadBufferSize;
            }
            if (charCount == 0) {
                currPattern = "";
            } else {
                currPattern = input.substring(searchWindowStart, charCount);
            }
            matchLength = 1;
            String searchTarget = input.substring(charCount, charCount + matchLength);
            if (currPattern.indexOf(searchTarget) != -1) {
                matchLength++;
                while (matchLength <= lookAheadBufferSize && (charCount + matchLength) < input.length()) {
                    searchTarget = input.substring(charCount, charCount + matchLength);
                    matchIndex = currPattern.indexOf(searchTarget);
                    if ((matchIndex != -1) && ((charCount + matchLength) < input.length())) {
                        matchLength++;
                    } else {
                        break;
                    }
                }
                matchLength--;
                matchIndex = currPattern.indexOf(input.substring(charCount, charCount + matchLength));
                charCount += matchLength;
                int offset = searchBufferSize - matchIndex;
                if (charCount < (searchBufferSize + matchLength)) {
                    offset = charCount - matchIndex - matchLength;
                }
                String nextChar = "";
                try {
                    nextChar = input.substring(charCount, charCount + 1);
                } catch (Exception e) {
                    nextChar = " ";
                }
                currTuple = new Tuple(offset, matchLength, nextChar);
                encodedData.add(currTuple);
            } else if (currPattern.indexOf(searchTarget) == -1) {
                String nextChar = input.substring(charCount, charCount + 1);
                currTuple = new Tuple(0, 0, nextChar);
                encodedData.add(currTuple);
            }
            if (encodedData.get(encodedData.size() - 1).getStringLength() <= (encodedData.get(encodedData.size() - 1).toString()).length() + 1 && encodedData.get(encodedData.size() - 1).getStringLength() != 0) {
                Tuple temp = encodedData.remove(encodedData.size() - 1);
                String nextChar = "";
                charCount -= matchLength;
                for (int i = 0; i < temp.getStringLength(); i++) {
                    nextChar = input.substring(charCount - temp.getOffset() + i, charCount - temp.getOffset() + 1 + i);
                    encodedData.add(new Tuple(0, 0, nextChar));
                }
                charCount += matchLength;
                try {
                    encodedData.add(new Tuple(0, 0, input.substring(charCount, charCount + 1)));
                } catch (Exception e) {
                    encodedData.add(new Tuple(0, 0, " "));
                }
            }
            charCount++;
        }
        
        for (int i = encodedData.size() - 1; i > 0; i--) {
            if (encodedData.get(i).getNextChar().equals(" ") && encodedData.get(i).getOffset()==0) {
                encodedData.remove(i);
            } else {
                break;
            }
        }
        
        for (int i = 0; i < encodedData.size(); i++) {
            //System.out.println("encodedData("+i+")");
            System.out.println("Formating... " + i * 100 / encodedData.size() + "%");
            output.append("(").append(encodedData.get(i)).append(")");
        }
        return output.toString();
    }

    public String decompress(String input) {
        String output = "";
        ArrayList<Tuple> encodedData = new ArrayList<>();
        int index = 0;
        int segmentLength = 0;
        int commaCounter = 0;

        for (index = 0; index < input.length(); index++) {
            System.out.println("Decoding... " + index * 100 / input.length() + "%");
            if (input.charAt(index) == '(') {
                for (segmentLength = 0; segmentLength < input.length(); segmentLength++) {
                    if (input.charAt(index + segmentLength) == ',') {
                        commaCounter++;
                    }
                    if (commaCounter == 2) {
                        break;
                    }
                }
                String currTuple = input.substring(index + 1, index + segmentLength + 2);
                //System.out.println(currTuple);
                StringTokenizer st = new StringTokenizer(currTuple, ",");
                String string1 = st.nextToken();
                String string2 = st.nextToken();
                String string3;
                try {
                    string3 = st.nextToken();
                } catch (Exception e) {
                    string3 = ",";
                }
                encodedData.add(new Tuple(new Integer(string1), new Integer(string2), string3));
                index += segmentLength + 2;
                commaCounter = 0;
            }
        }

        for (int i = 0; i < encodedData.size(); i++) {
            System.out.println("Decompressing... " + i * 100 / encodedData.size() + "%");
            Tuple currTuple = encodedData.get(i);
            if (currTuple.getStringLength() == 0) {
                output += (currTuple.getNextChar());
            } else {
                for (int j = 0; j < currTuple.getStringLength(); j++) {
                    char currChar = output.charAt(output.length() - currTuple.getOffset());
                    output += currChar;
                }
                String temp = currTuple.getNextChar();
                if (!(temp.equals("\0"))) {
                    output += currTuple.getNextChar();
                }
            }
        }
        return output;
    }

    public String toBinary(String input) {
        char[] chars = input.toCharArray();
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            System.out.println("Converting to Binary... " + i * 100 / chars.length + "%");
            temp.append(Integer.toBinaryString(chars[i]));
        }
        return temp.toString();
    }

    public String compressOutput(String input) {
        StringBuilder output = new StringBuilder(input.replace("(0,0,", "("));
        for (int i = 0; i < output.length() - 2; i++) {
            System.out.println("Compressing output... " + i * 100 / output.length() + "%");
            if (output.charAt(i) == '(') {
                if (output.charAt(i + 2) == ')') {
                    output.deleteCharAt(i);
                    output.deleteCharAt(i+1);
                    i--;
                }
            }
        }
        return output.toString();
    }

    public String expandOutput(String input) {
        String temp = "(0,0," + input.substring(0, 1) + ")";
        StringBuilder output = new StringBuilder(temp);
        for (int i = 1; i < input.length(); i++) {
            System.out.println("Expanding output... " + i * 100 / input.length() + "%");
            if (Character.isLetter(input.charAt(i)) || input.charAt(i) == ' ') {
                if (!(input.substring(i - 1, i).equals(","))) {
                    output.append("(0,0,").append(input.substring(i, i + 1)).append(")");
                } else {
                    output.append(input.substring(i, i + 1));
                }
            } else {
                output.append(input.substring(i, i + 1));
            }
        }
        return output.toString();
    }
}