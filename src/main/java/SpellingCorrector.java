import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;


public class SpellingCorrector {
	private Map<String,Integer> languageModel = new HashMap<String,Integer>();
	private String alphabet = "abcdefghijklmnopqrstuvwxyz";
	
	public SpellingCorrector(){
		train();
	}
	
	public Set<String> deletions(String word) {
		Set<String> deletes = new HashSet<String>();
		for (int i = 0; i < word.length(); i++){
			StringBuffer stringBufferWord = new StringBuffer(word);
			deletes.add(stringBufferWord.deleteCharAt(i).toString());
		}
		return deletes;
	}
	
	public Set<String> transpositions(String word){
		Set<String> transpositions = new HashSet<String>();
		for (int i = 0; i < word.length() - 1; i++){
			transpositions.add(transpose(word, i));
		}
		return transpositions;
	}
	
	private String transpose(String word, int index){
		StringBuffer stringBufferWord = new StringBuffer(word);
		stringBufferWord.replace(index, index+1, word.substring(index+1,index+2));
		stringBufferWord.replace(index+1, index+2, word.substring(index,index+1));
		return stringBufferWord.toString();
	}
	
	public Set<String> alterations(String word){
		Set<String> alterations = new HashSet<String>();
		for (int i = 0; i < word.length(); i++){
			for(int j = 0; j < alphabet.length(); j++){
				StringBuffer stringBuffer = new StringBuffer(word);
				stringBuffer.replace(i, i + 1, alphabet.substring(j, j + 1));
				alterations.add(stringBuffer.toString());
			}
		}
		return alterations;
	}
	
	public Set<String> inserts(String word){
		Set<String> inserts = new HashSet<String>();
		for (int i = 0; i <= word.length(); i++){
			for(int j = 0; j < alphabet.length(); j++){
				StringBuffer stringBuffer = new StringBuffer(word);
				stringBuffer.insert(i, alphabet.substring(j, j + 1));
				inserts.add(stringBuffer.toString());
			}
		}
		return inserts;
	}
	
	public Set<String> edits(String word){
		Set<String> edits1 = new HashSet<String>();
		edits1.addAll(deletions(word));
		edits1.addAll(transpositions(word));
		edits1.addAll(alterations(word));
		edits1.addAll(inserts(word));
		return edits1;
	}
	
	
	private Set<String> knownEdits2(String word){
		Set<String> edits1 = edits(word);
		Set<String> edits2 = new HashSet<String>();
		for (String edit1Word : edits1){
			edits2.addAll(edits(edit1Word));
		}
		return knownWords(edits2);
	}
	
	private Set<String> knownWords(Set<String> words){
		Set<String> knownWords = new HashSet<String>();
		for (String word : words){
			if (isWordKnown(word)){
				knownWords.add(word);
			}
		}
		return knownWords;
	}
	
	private boolean isWordKnown(String word){
		if (languageModel.get(word) != null){
			return true;
		}
		else {
			return false;
		}
	}
	
	
	public Set<String> candidates(String word){
		Set<String> wordSet = new HashSet<String>();
		Set<String> candidates;
		wordSet.add(word);
		if (isWordKnown(word)){
			return wordSet;
		}
		candidates = knownWords(edits(word));
		if (!candidates.isEmpty()){
			return candidates;
		}
		
		candidates = knownEdits2(word);
		if (!candidates.isEmpty()){
			return candidates;
		}
		return wordSet;
	}
	
	public String correct(String word){
		Set<String> candidatesSet = candidates(word);
		if (candidatesSet.size() == 1){
			return candidatesSet.iterator().next();
		}
		int maxWordCount = 0;
		String selectedWord ="";
		for (String wordCandidate : candidatesSet){
			int wordCount = languageModel.get(wordCandidate);
			if (wordCount > maxWordCount){
				maxWordCount = wordCount;
				selectedWord = wordCandidate;
			}
		}
		return selectedWord;
			
	}


	public void train() {
		try {
			BufferedReader input = new BufferedReader(new FileReader(
					"big.txt"));
			try {
				String line = null;
				while ((line = input.readLine()) != null) {
					StringTokenizer tok = new StringTokenizer(line);
					while (tok.hasMoreElements()) {
						String word = (String) tok.nextElement();
						if (languageModel.get(word) == null) {
							 languageModel.put(word, 1);
						}
						else{
							int increment = languageModel.get(word) + 1;
							languageModel.put(word, increment);
						}
					}
				}
			} finally {
				input.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	
}
