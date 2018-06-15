/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList;
    private HashSet<String> wordSet;
    private HashMap<Integer,ArrayList<String>> sizeToWord;
    private HashMap<String,ArrayList<String>> lettersToWord;
    private int wordLength;
    public AnagramDictionary(Reader reader) throws IOException {
        wordList = new ArrayList();
        wordSet = new HashSet();
        lettersToWord = new HashMap();
        sizeToWord = new HashMap();
        wordLength = DEFAULT_WORD_LENGTH;
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            Integer intTo = new Integer(word.length());
            if(sizeToWord.containsKey(intTo)) {
                ArrayList<String> tempList = sizeToWord.get(intTo);
                tempList.add(word);
                sizeToWord.put(intTo,tempList);
            } else {
                ArrayList<String> tempList = new ArrayList();
                tempList.add(word);
                sizeToWord.put(intTo,tempList);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        if(wordSet.contains(base)) {
            if(!base.contains(word)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String target = sortLetters(targetWord.toLowerCase());
        for( String item : wordList) {
            if(targetWord.length() == item.length()) {
                String temp2 = item.toLowerCase();
               String temp = sortLetters(temp2);
              if(temp.equals(target)) {
                    if(lettersToWord.containsKey(target)) {
                        result.add(item);
                        ArrayList<String> tempList = lettersToWord.remove(target);
                        tempList.add(item);
                        lettersToWord.put(target, tempList);
                    }
                    else {
                        result.add(item);
                        lettersToWord.put(target,result);
                    }
                }
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String temp = sortLetters(word.toLowerCase());
        List<String> result1 = getAnagrams(word);
        result = (ArrayList<String>) result1;
        ArrayList<String> tempList = new ArrayList();
        for(String item: wordList) {
            if(item.length() == word.length()+1) {
                if(sortLetters(item.toLowerCase()).equals(temp)) {
                    tempList.add(item);
                }
            }
        }
        for(String item : tempList) {
            if(!result.contains(item)) {
                result.add(item);
            }
        }
        lettersToWord.remove(temp);
        lettersToWord.put(temp,result);
        return result;
    }

    public String pickGoodStarterWord() {
        Random rand = new Random();
        Integer index = new Integer(wordLength);
        ArrayList<String> tempList = sizeToWord.get(index);
        String startWord = tempList.get(rand.nextInt(tempList.size()));
        List<String> check = getAnagramsWithOneMoreLetter(startWord);
        //Has a hard time finding initial 3 letter word with MIN_NUM_ANAGRAMS due to randomness
        /*while((check.size() < MIN_NUM_ANAGRAMS)) {
            startWord = tempList.get(rand.nextInt(tempList.size()));
            check = getAnagramsWithOneMoreLetter(startWord);
        } */
        if(wordLength < MAX_WORD_LENGTH) {
            wordLength++;
        }
        return startWord;
    }

    public String sortLetters(String in) {
        String input = in.toLowerCase();
        char[] charArr = input.toCharArray();
        Arrays.sort(charArr);
        String res = "";
        for (int i = 0; i < input.length(); i++) {
            if (res.indexOf(input.charAt(i)) == -1) {
                res += input.charAt(i);
            }
        }
        return res;
    }
}

