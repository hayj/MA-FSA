package com.zunama;

import java.util.*;

public class DawgWordable
{
	private DawgStateWordable root;
	private String previousWord = "";
	private Map<String, DawgStateWordable> register = new HashMap<String, DawgStateWordable>();
	private int totalEdges = 0;
	private String currentPrefix;

	public interface Wordable extends Comparable<Wordable>
	{
		public abstract String getWord();
	}

	public DawgStateWordable getRoot()
	{
		return this.root;
	}

	public DawgWordable(List<Wordable> wordables)
	{
		root = new DawgStateWordable();
		root.setEndWord(false);
		insertWords(wordables);
		register = null;
	}

	public boolean search(String word)
	{
		DawgStateWordable current = root;

		for(Character c : word.toCharArray())
		{
			if(current.getEdges().containsKey(c))
				current = current.getEdges().get(c);
			else
				return false;
		}

		return current.isEndWord();
	}

	public List<String> prefixSearch(String prefix)
	{

		if(prefix == null)
			throw new RuntimeException("Prefix is set to null");

		DawgStateWordable current = root;
		currentPrefix = prefix.toLowerCase();
		List<String> words = new ArrayList<String>();

		for(char letter : prefix.toLowerCase().toCharArray())
		{
			if(current.getEdges().containsKey(letter))
				current = current.getEdges().get(letter);
			else
				return words;
		}

		prefixSearch(current, words, "");
		return words;
	}

	public boolean prefixExist(String prefix)
	{

		if(prefix == null)
			throw new RuntimeException("Prefix is set to null");

		DawgStateWordable current = root;

		for(char c : prefix.toCharArray())
		{
			if(current.getEdges().containsKey(c))
				current = current.getEdges().get(c);
			else
				return false;
		}

		return true;
	}

	public int getTotalEdges()
	{
		return totalEdges;
	}

	private void prefixSearch(DawgStateWordable state, List<String> words, String currentString)
	{
		if(state.isEndWord())
		{
			words.add(currentPrefix + currentString);
		}

		for(Character key : state.getEdges().keySet())
		{
			DawgStateWordable nextStateToVist = state.getEdges().get(key);
			String newString = currentString + key;

			prefixSearch(nextStateToVist, words, newString);
		}
	}

	private void insertWords(List<Wordable> wordables)
	{
		Collections.sort(wordables);
		for(Wordable wordable : wordables)
		{
			insertWord(wordable);
		}
	}

	private void insertWord(Wordable wordable)
	{
		String word = wordable.getWord();
		if(word.compareTo(previousWord) < 0)
			throw new RuntimeException("Trying to insert a word out of order.");

		word = word.toLowerCase();

		String commonPrefix = getCommonPrefix(word, previousWord);
		String currentSuffix = word.substring(commonPrefix.length());

		DawgStateWordable lastState = getLastState(commonPrefix);

		if(lastState.getEdges().size() > 0)
		{
			replaceOrRegister(lastState);
		}

		addSufix(lastState, currentSuffix, wordable);

		previousWord = word;
	}

	private void addSufix(DawgStateWordable lastState, String currentSuffix, Wordable wordable)
	{
		char[] wordCharArray = currentSuffix.toCharArray();

		for(char c : wordCharArray)
		{
			DawgStateWordable nextState = new DawgStateWordable();
			lastState.getEdges().put(c, nextState);
			totalEdges++;
			lastState = nextState;
		}

		lastState.setEndWord(true, wordable);
	}

	private void replaceOrRegister(DawgStateWordable state)
	{
		Character c = getMostRecentAddedLetter(state);
		DawgStateWordable child = state.getEdges().get(c);

		if(child.getEdges().size() > 0)
		{
			replaceOrRegister(child);
		}

		if(register.containsKey(child.toString()))
		{
			state.getEdges().put(c, register.get(child.toString()));
			totalEdges--;
		}
		else
		{
			register.put(child.toString(), child);
		}
	}

	private Character getMostRecentAddedLetter(DawgStateWordable state)
	{
		Character out = null;
		for(Character key : state.getEdges().keySet())
		{
			out = key;
		}
		return out;
	}

	private DawgStateWordable getLastState(String commonPrefix)
	{
		if(commonPrefix == null || commonPrefix.length() == 0)
			return root;

		DawgStateWordable current = root;

		for(char c : commonPrefix.toCharArray())
		{
			current = current.getEdges().get(new Character(c));
		}

		return current;
	}

	private String getCommonPrefix(String word, String previousWord)
	{
		int count = 0;
		int minCheck = Math.min(word.length(), previousWord.length());

		for(int i = 0 ; i < minCheck ; i++)
		{
			if(word.charAt(i) == previousWord.charAt(i))
				count++;
			else
				break;
		}

		return word.substring(0, count);
	}
}