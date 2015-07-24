package com.zunama;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.junit.Test;


public class DawgTestString
{
	@Test
	public void test()
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
		System.out.println(word);
		assertTrue(word.equals("didyme"));
	}

	private ArrayList<String> loadFrenchVoc()
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(
					"./data/liste.de.mots.francais.frgut.txt"));
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
