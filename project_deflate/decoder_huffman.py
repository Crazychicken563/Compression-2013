import operator

#decodes binary string from Huffman encoding; should now be in LZ77 compressed text
class HuffmanDecoder(object):

    def __init__(self):
        self.getHuffmanCode()
        self.getEncodedText()
        self.decodeText()
        self.writeDecodedText()
        self.dprint()

    #gets the code to decode the encoded text
    def getHuffmanCode(self):
        self.codeFile = open("HuffmanCode.txt", "r")
        self.charBinValues = {}
        #whether the next line is a code; true is binary code, false is char
        valueCode = True
        lastCode = ""
        #constuct dictionary
        for line in self.codeFile:
            trueLine = line[:-1]
            if valueCode:
                self.charBinValues[trueLine] = ""
                lastCode = trueLine
                valueCode = False
            else:
                self.charBinValues[lastCode] = trueLine
                valueCode = True

        self.sortedCharBinValues = sorted(self.charBinValues.iteritems(), key=operator.itemgetter(0))
        self.codeFile.close()

    #gets previously Huffman encoded text
    def getEncodedText(self):
        self.inputFile = open("fullyEncoded.txt", "r")
        self.encodedText = self.inputFile.read()
        self.inputFile.close()

    #decodes Huffman encoded text
    def decodeText(self):
        self.decodedText = ""
        self.textToDecode = self.encodedText[:]
        while len(self.textToDecode)>1:
            codeSoFar = ""
            charsSoFar = 0
            while not codeSoFar in self.charBinValues.keys():
                charsSoFar = charsSoFar + 1
                codeSoFar = self.textToDecode[:charsSoFar] 
            self.decodedText += self.charBinValues[codeSoFar]
            #print self.charBinValues[codeSoFar]
            self.textToDecode = self.textToDecode[charsSoFar:]

    #prints function for debugging
    def dprint(self):
        print "-- HUFFMAN DECODER --", "\n", \
                "sorted char binary values", self.sortedCharBinValues, "\n", \
                "encoded text:", self.encodedText, "\n", \
                "decoded text:", self.decodedText, "\n", \
                "bits when fully encoded:", len(self.encodedText)*2, "\n", \
                "bits when decoded:", len(self.decodedText)*8
        print "-- END HUFFMAN DECODER --"

    #writes decoded text to txt file
    def writeDecodedText(self):
        self.outputFile = open("HuffmanDecoded.txt", "w")
        self.outputFile.write(self.decodedText)
        self.outputFile.close()

r = HuffmanDecoder()