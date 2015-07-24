package com.zunama;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Ignore;
import org.junit.Test;

import com.zunama.Dawg.Wordable;

public class DawgTestWordable
{
	@Test
	@Ignore
	public void test1()
	{
		ArrayList<Wordable> frenchVoc = loadFrenchVoc();

		long startTime = System.currentTimeMillis();
		Dawg dawg = new Dawg(frenchVoc);
		long stopTime = System.currentTimeMillis();
		long duration = stopTime - startTime;
		System.out.println("Words count: " + frenchVoc.size());
		System.out.println("Total Edges: " + dawg.getTotalEdges());
		System.out.println("Built DAWG in: " + ((double) duration / 1000.0) + " secondes");
		assertTrue(dawg.getTotalEdges() == 35843 && dawg.getTotalEdges() <= frenchVoc.size());

		startTime = System.currentTimeMillis();
		String prefix = "bon";
		System.out.println("Prefix exist in DAWG: " + dawg.prefixExist(prefix));
		List<String> foundWords = dawg.prefixSearch(prefix);
		stopTime = System.currentTimeMillis();
		duration = stopTime - startTime;
		//for(String s : foundWords)
		//	System.out.println(s);
		System.out.println("Searched prefix found " + foundWords.size() + " words in: " + duration + " ms");
		assertTrue(foundWords.contains("bonifiez"));

		DawgState dawgState = dawg.getRoot();
		String word = "";
		for(int i = 0 ; i < 100 ; i++)
		{
			int u = 0;
			char c = 'z';
			for(Entry<Character, DawgState> entry : dawgState.getEdges().entrySet())
			{
				c = entry.getKey();
				dawgState = entry.getValue();
				if(u == 3)
					break;
				u++;
			}
			word += c;
			if(dawgState.isEndWord())
				break;
		}
		assertTrue(dawgState.isEndWord());
		assertTrue(word.equals("didyme"));
		System.out.println(dawgState.getWordable().getWord());
		System.out.println(word);
		assertTrue(dawgState.getWordable().getWord().equals(word));
	}
	
	@Test
	public void test2()
	{
		ArrayList<Wordable> voc = new ArrayList<Wordable>();
		voc.add(new WordableTest("aa"));
		voc.add(new WordableTest("bb"));
		voc.add(new WordableTest("ab"));
		voc.add(new WordableTest("ba"));
		Dawg dawg = new Dawg(voc);
		
		DawgState dawgState = dawg.getRoot();
		String word = "";
		int change = 0;
		for(int i = 0 ; i < 100 ; i++)
		{
			int u = 0;
			char c = 'z';
			for(Entry<Character, DawgState> entry : dawgState.getEdges().entrySet())
			{
				c = entry.getKey();
				dawgState = entry.getValue();
				if(u == change)
					break;
				u++;
			}
			word += c;
			if(dawgState.isEndWord())
				break;
			change++;
		}
		assertTrue(dawgState.isEndWord());
		assertTrue(word.equals("ab"));
		System.out.println(dawgState.getWordable().getWord());
		System.out.println(word);
		assertTrue(dawgState.getWordable().getWord().equals(word));
	}

	private class WordableTest implements Wordable
	{
		private String word;

		WordableTest(String word)
		{
			this.word = word;
		}

		public int compareTo(Wordable wordable)
		{
			return this.getWord().compareTo(wordable.getWord());
		}

		public String getWord()
		{
			return this.word;
		}

	}

	private ArrayList<Wordable> loadFrenchVoc()
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(
					"/tmp/ikcom-dictionary/liste.de.mots.francais.frgut.txt"));
			String word = "";
			ArrayList<Wordable> frenchVoc = new ArrayList<Wordable>();
			while((word = br.readLine()) != null)
			{
				frenchVoc.add(new WordableTest(word.toLowerCase()));
			}
			return frenchVoc;
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
