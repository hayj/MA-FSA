package com.zunama;

import java.util.LinkedHashMap;
import java.util.Map;

import com.zunama.DawgWordable.Wordable;

public class DawgStateWordable
{
	private static int nextId = 0;

	private int id;
	private LinkedHashMap<Character, DawgStateWordable> edges = new LinkedHashMap<Character, DawgStateWordable>();
	private boolean endWord = false;

	private Wordable wordable = null;

	public DawgStateWordable()
	{
		id = nextId++;
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		sb.append(endWord);

		for(Map.Entry<Character, DawgStateWordable> entry : edges.entrySet())
		{
			sb.append(entry.getKey());
			sb.append(entry.getValue().getId());
		}

		return sb.toString();
	}

	public int getId()
	{
		return id;
	}

	public LinkedHashMap<Character, DawgStateWordable> getEdges()
	{
		return edges;
	}

	public DawgStateWordable getEdge(char c)
	{
		return this.edges.get(c);
	}

	public boolean isEndWord()
	{
		return endWord;
	}

	public void setEndWord(boolean endWord)
	{
		this.endWord = endWord;
	}

	public void setEndWord(boolean endWord, Wordable wordable)
	{
		this.endWord = endWord;
		this.wordable = wordable;
	}

	public Wordable getWordable()
	{
		return this.wordable;
	}
}