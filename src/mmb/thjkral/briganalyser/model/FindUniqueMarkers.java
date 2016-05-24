/*
 * This code is created by:
 * Tom Kral | Bioinformatics intern
 * t.h.j.kral@umcg.nl | +31642935544
 * 
 * Commissioned by:
 * University Medical Center Groningen | Medical Microbiology
 */
package mmb.thjkral.briganalyser.model;

import java.util.ArrayList;

/**
 * Finds unique regions in the genome of the outbreak strain.
 * 
 * Requires an ArrayList with Ring objects.
 * @author KralTHJ
 */
public class FindUniqueMarkers {
    
    /*
    GLOBAL VALUES. PLEASE DELETE WHEN MORE FUNCTIONALITY IS PRESENT!
    */
    int totalLength = 5357923;
    int numRings = 6;
    
    /**
     *
     * @param ringList
     * @return 
     */
    public ArrayList<UniqueMarker> start(ArrayList<Ring> ringList) {
        
        ArrayList<ArrayList<Gap>> overlapList = compare(ringList);
        ArrayList<UniqueMarker> umList = isolateUniqueMarkers(overlapList);
        
        return(umList);
        
    }//start()

    private ArrayList<ArrayList<Gap>> compare(ArrayList<Ring> ringList) {
        
        ArrayList<ArrayList<Gap>> overlappingGaps = new ArrayList<>();
        
        //compare gaps of first ring with the other gaps
        for (int i = 0; i < ringList.get(0).getGapsArray().size(); i++) {//for every Gap in Ring 1
            
            Gap gapOne = (Gap) ringList.get(0).getGapsArray().get(i);
            int start = gapOne.getStartCor();
            int stop = gapOne.getStopCor();
            
            ArrayList<Gap> matches = new ArrayList<>();
            matches.add(gapOne);
            
            for (int j = 1; j < ringList.size(); j++) {//for every other Ring
                
                for (int k = 0; k < ringList.get(j).getGapsArray().size(); k++) {//for every Gap of that Ring
                    
                    Gap gapTwo = (Gap) ringList.get(j).getGapsArray().get(k);
                    int startTwo = gapTwo.getStartCor();
                    int stopTwo = gapTwo.getStopCor();
                    
                    if (start >= startTwo && stop <= stopTwo) {
                        matches.add(gapTwo);
                    }
                    
                }
            }
            if (matches.size() == numRings) {//if all rings share overlap
                overlappingGaps.add(matches);
            }
            
        }
//        System.out.println("There are " + overlappingGaps.size() + " overlapping gaps");
        return overlappingGaps;
        
    }//compare()
    
    private ArrayList<UniqueMarker> isolateUniqueMarkers (ArrayList<ArrayList<Gap>> overlappingGaps) {
       
        ArrayList<UniqueMarker> umList = new ArrayList<>();
        
//        ReadFile parser = new ReadFile();
//        String refSequence = parser.getReferenceSequence("null", 20, 50);
        
        /*
        Determine the start and stop position of the UniqueMarker on the reference
        genome. From the overlapping gaps, this is the highest start position
        and the lowest stop position. This is done by comparing one gap to the 
        rest. Every time a better position is discovered, the best position is
        saved.
        */
        for (int i = 0; i < overlappingGaps.size(); i++) {//for every array
            
            //set highest startposition to first Gap in ArrayList
            int startHighest = overlappingGaps.get(i).get(0).getStartCor();
            //set lowest stopposition to first Gap in ArrayList
            int stopLowest = overlappingGaps.get(i).get(0).getStopCor();
            
            for (int j = 0; j < overlappingGaps.get(i).size(); j++) {//for every gap in array
                
                int startCurr = overlappingGaps.get(i).get(j).getStartCor();
                int stopCurr = overlappingGaps.get(i).get(j).getStopCor();
                
                if (startCurr >= startHighest) {
                    startHighest = startCurr;
                }
                if (stopCurr <= stopLowest) {
                    stopLowest = stopCurr;
                }
            }
            
            /*
            First, calculate the length (= distance between to positions) of the
            possible UniqueMarker
            */
            int difference = stopLowest - startHighest;
            
            /*
            If the length is sufficient: isolate the sequence from the reference-
            genome and make a UniqueMarker object.
            */
            if (difference >= 0 && difference <= 1) {
//                String markerSequence = refSequence.substring(startHighest, stopLowest);
//                markerSequence = markerSequence.toUpperCase();
                String tempSequence = "ATACAGATATAGACAAGCGCGCGCCCGCTAGAGAGCACGTCGCGCGAGCGTGTTTGCGCGCGAAAAGCGCGCTGAGATTCGCGCATACAGATATAGACAAGCGCGCGCCCGCTAGAGAGCACGTCGCGCGAGCGTGTTTGCGCGCGAAAAGCGCGCTGAGATTCGCGC";
                UniqueMarker um = new UniqueMarker(i, 
                        startHighest, 
                        stopLowest, 
                        difference, 
                        tempSequence,
                        makeComplementary(tempSequence));
                umList.add(um);
            }
        }
        
        return umList;
        
    }//isolateUniqueMarkers()
    
    
    
    /**
     * Returns the complement of a given DNA sequence.
     * The sequence is not reversed however. When working with Strings in Java,
     * it is easier if I can start at the beginning (AKA the '3)
     * @param sequence
     * @return complementary sequence
     */
    private String makeComplementary (String sequence) {
        
        /*
        Reverse the sequence
        */
        String seqComp = new StringBuilder(sequence).reverse().toString();
        
        /*
        With StringBuilder(), a new String is made.
        */
        StringBuilder complement = new StringBuilder();
        
        /*
        Traverse through the sequence. Everytime a nucleotide is encountered,
        add to the new String
        */
        for (int i = 0; i < seqComp.length(); i++) {
            char c = seqComp.charAt(i);
            
            if (c == 'A') {
                complement.append('T');
            }
            if (c == 'T') {
                complement.append('A');
            }
            if (c == 'G') {
                complement.append('C');
            }
            if (c == 'C') {
                complement.append('G');
            }            
        }
        
        return complement.toString();
        
    }//makeComplementary()
    
}//class()
