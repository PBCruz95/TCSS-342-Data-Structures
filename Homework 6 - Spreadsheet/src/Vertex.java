import java.util.LinkedList;
import java.util.List;

// Represents a vertex in the graph.
class Vertex
{
	public String name; // Vertex name
	public List<Edge> adj; // Adjacent vertices
	public double dist; // Cost
	public Vertex prev; // Previous vertex on shortest path
	public Vertex pos;
	public int scratch;// Extra variable used in algorithm
	public int row, column;

	public Vertex( String nm, int theRow, int theCol )
	{ 
		name = nm; adj = new LinkedList<Edge>( );
		row = theRow;
		column = theCol;
		reset( ); 
	}

	public void reset( )
	{ 
		dist = Graph.INFINITY; 
		prev = null; 
		pos = null; 
		scratch = 0; 
	}
}
