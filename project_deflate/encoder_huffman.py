import operator

#primary class containing and utilizing algorithims for Huffman coding
class HuffmanEncoder(object):

    def __init__(self):
        self.getCompressedText()
        self.identifyChars()
        self.getCharValues()
        self.writeBinString()
        self.writeHuffmanCode()
        self.dprint()
        

    #gets the input text (should have been compressed by LZ77 already)
    def getCompressedText(self):
        #inputFileString = "lz77Encoded.txt"
        inputFileString = raw_input("input file:")
        self.inputFile = open(inputFileString, "r")
        #self.compressedText = raw_input("compressed text:")
        self.compressedText = self.inputFile.read()
        self.inputFile.close()

    #identifies all the characters and their frequencies in the input text
    def identifyChars(self):
        self.charFrequency = {}
        for char in self.compressedText:
            if not char in self.charFrequency:
                self.charFrequency[char] = 1
            else:
                self.charFrequency[char] = self.charFrequency[char] +1
        self.sortedCharFreq = sorted(self.charFrequency.iteritems(), key=operator.itemgetter(1))


    #gets the binary values for 
    def getCharValues(self):
        self.charBinValues = {}
        self.huffTree = []
        #create tree nodes for each value pair
        for char in self.sortedCharFreq:
            node = TreeNode(character=char[0], 
                            frequency=char[1])
            self.huffTree.append(node)
            self.huffTree = sorted(self.huffTree, key=lambda nodeF: nodeF.freq)
        #organize tree
        while len(self.huffTree)>1:
            self.huffTree[0] = TreeNode(self.huffTree[0], self.huffTree[1], 
                                    None, self.huffTree[0].freq + self.huffTree[1].freq)
            del self.huffTree[1]
            self.huffTree = sorted(self.huffTree, key=lambda nodeF: nodeF.freq)

        self.BinTraversal(self.huffTree[0], "")
        self.sortedCharBinValues = sorted(self.charBinValues.iteritems(), key=operator.itemgetter(1))

    #print method for debugging
    def dprint(self):
        print "-- HUFFMAN ENCODER --", "\n", "compressed text:", self.compressedText, "\n", \
              "sorted char frequency:", self.sortedCharFreq, "\n", \
              "number of chars:", len(self.charFrequency.keys()), "\n", \
              "sorted tree:"
        for node in self.huffTree:
            print node
        print "sorted char binary values:", self.sortedCharBinValues, "\n", \
                "-- END HUFFMAN ENCODER --"

    #used to traverse tree and assign binary string values (in order)
    def BinTraversal(self, node, currBinStr):
        if node.left:
            self.BinTraversal(node.left, currBinStr + "0")
        if node.char:
            self.charBinValues[node.char] = currBinStr
        if node.right:
            self.BinTraversal(node.right, currBinStr + "1")

    #writes the encoded binary string to a txt file
    def writeBinString(self):
        self.outputFile = open("fullyEncoded.txt", "w")
        for char in self.compressedText:
            self.outputFile.write(self.charBinValues[char])
        self.outputFile.close()

    #writes the code need to decipher binary string to a txt file
    def writeHuffmanCode(self):
        self.codeFile = open("HuffmanCode.txt", "w")
        for tup in self.sortedCharBinValues:
            self.codeFile.write(tup[1])
            self.codeFile.write("\n")
            self.codeFile.write(tup[0])
            self.codeFile.write("\n")
        self.codeFile.close

#tree used for Huffman algorithim
#my convention: left is 0, right is 1
class TreeNode(object):

    def __init__(self, leftNode=None, rightNode=None, character=None, frequency=None):
        self.left = leftNode
        self.right = rightNode
        self.char = character
        self.freq = frequency

    def __str__(self):
        return "<<" + str(self.left) + ", " + str(self.right) + ", " \
                 + str(self.char) + ", " + str(self.freq) + ">>"



r = HuffmanEncoder()