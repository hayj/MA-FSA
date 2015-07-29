package fr.hayj.dawg;

import java.util.LinkedHashMap;
import java.util.Map;

import fr.hayj.dawg.Dawg.Wordable;

@Deprecated
public class DawgState {
    private static int nextId = 0;

    private int id;
    private LinkedHashMap<Character, DawgState> edges = new LinkedHashMap<Character, DawgState>();
    private boolean endWord = false;
    
    private Wordable wordable = null;
	private Dawg subDawg;

    public DawgState() {
        id = nextId++;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(endWord);

        for (Map.Entry<Character, DawgState> entry : edges.entrySet()){
            sb.append(entry.getKey());
            sb.append(entry.getValue().getId());
        }

        return sb.toString();
    }

    public int getId() {
        return id;
    }

    public LinkedHashMap<Character, DawgState> getEdges() {
        return edges;
    }

    public DawgState getEdge(char c) {
        return this.edges.get(c);
    }

    public boolean isEndWord() {
        return endWord;
    }

    public void setEndWord(boolean endWord) {
        this.endWord = endWord;
    }
    
    public void setWordable(Wordable wordable)
	{
		this.wordable = wordable;
	}

	public Wordable getWordable()
	{
		return this.wordable;
	}
	
	public Dawg getSubDawg()
	{
		return subDawg;
	}
	
	public void setSubDawg(Dawg subDawg)
	{
		this.subDawg = subDawg;
	}

	public boolean hasSubDawg()
	{
		return this.subDawg != null;
	}

	public boolean isEndWordSequence()
	{
		return this.isEndWord() && this.subDawg == null;
	}
}