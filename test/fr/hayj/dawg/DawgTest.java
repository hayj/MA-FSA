package fr.hayj.dawg;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Ignore;
import org.junit.Test;

import fr.hayj.dawg.Dawg.Wordable;

public class DawgTest
{
	@Test
	@Ignore
	public void test1()
	{
		ArrayList<String> frenchVoc = loadFrenchVoc();

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
		// for(String s : foundWords)
		// System.out.println(s);
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
		System.out.println(word);
		assertTrue(word.equals("didyme"));
	}

	@Test
	@Ignore
	public void test2()
	{
		ArrayList<Wordable> wordables = new ArrayList<Wordable>();
		wordables.add(new WordableTest("aa", 1));
		wordables.add(new WordableTest("bb", 2));
		wordables.add(new WordableTest("ba", 3));

		Dawg dawg = Dawg.wordablesToDawg(wordables);
		dawg.setWordables(wordables);

		HashMap<String, List<String>> map = new HashMap<String, List<String>>();
		ArrayList<String> subList = new ArrayList<String>();
		subList.add("cc");
		subList.add("dd");
		subList.add("cd");
		map.put("aa", subList);

		DawgState dawgState = dawg.getRoot();
		dawgState = dawgState.getEdge('b');
		assertTrue(dawgState.isEndWordSequence() == false);
		assertTrue(dawgState.isEndWord() == false);
		assertTrue(dawgState.getEdge('c') == null);
		assertTrue(dawgState.getEdges().size() == 2);
		dawgState = dawgState.getEdge('a');
		assertTrue(dawgState.isEndWordSequence() == false);
		assertTrue(dawgState.isEndWord() == false);
		assertTrue(dawgState.getSubDawg() == null);

		dawgState = dawg.getRoot().getEdge('a').getEdge('a').getSubDawg().getRoot();
		assertTrue(dawgState.getEdges().size() == 2);
		assertTrue(dawgState.isEndWord() == false);
		assertTrue(dawgState.isEndWordSequence() == false);
		dawgState = dawgState.getEdge('c').getEdge('d');
		assertTrue(dawgState.getEdges().size() == 0);
		assertTrue(dawgState.isEndWordSequence() == true);
		assertTrue(dawgState.isEndWord() == true);
	}

	private class WordableTest implements Wordable
	{
		private String word;
		private int value;

		WordableTest(String word, int value)
		{
			this.word = word;
			this.value = value;
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

	private ArrayList<String> loadFrenchVoc()
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader("./data/liste.de.mots.francais.frgut.txt"));
			String word = "";
			ArrayList<String> frenchVoc = new ArrayList<String>();
			while((word = br.readLine()) != null)
			{
				frenchVoc.add(word.toLowerCase());
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
