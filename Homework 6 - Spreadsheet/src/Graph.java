import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;

// Graph class: evaluate shortest paths.
//
// CONSTRUCTION: with no parameters.
//
// ******************PUBLIC OPERATIONS**********************
// void addEdge( String v, String w, double cvw )
// --> Add additional edge
// void printPath( String w ) --> Print path after alg is run
// void acyclic( String s ) --> Single-source acyclic
// ******************ERRORS*********************************
// Some error checking is performed to make sure that graph is ok
// and that graph satisfies properties needed by each
// algorithm. Exceptions are thrown if errors are detected.

public class Graph
{
	public static final double INFINITY = Double.MAX_VALUE;
	
	private Map<String,Vertex> vertexMap = new HashMap<String,Vertex>( );
	
	private Queue<Vertex> topOrder = new LinkedList<Vertex>( );

	public void addEdge( String sourceName, int srcRow, int srcCol, String destName, int destRow, int destCol)
	{
		Vertex v = getVertex( sourceName, srcRow, srcCol );
		Vertex w = getVertex( destName, destRow, destCol );
		v.adj.add( new Edge( w) );
	}

	
	//Represents an entry in the priority queue for Dijkstra's algorithm.
	class Path implements Comparable<Path>
	{
		public Vertex dest; // w
		public double cost; // d(w)

		public Path( Vertex d, double c )
		{
			dest = d;
			cost = c;
		}

		public int compareTo( Path rhs )
		{
			double otherCost = rhs.cost;

			return cost < otherCost ? -1 : cost > otherCost ? 1 : 0;
		}
	}
	
	public Queue<Vertex> getOrdering()
	{
		return topOrder;
	}
	
	
	
	public void acyclic( String startName )
	{
		Vertex start = vertexMap.get( startName );
		if( start == null )
			throw new NoSuchElementException( "Start vertex not found" );

		clearAll( );
		Queue<Vertex> q = new LinkedList<Vertex>( );
		start.dist = 0;

		Collection<Vertex> vertexSet = inDegree();
		int size = vertexSet.size();
		// Enqueue vertices of indegree zero
		for( Vertex v : vertexSet )
			if( v.scratch == 0 )
			{
				q.add( v );
				topOrder.add(v);
			}

		int iterations;
		for( iterations = 0; !q.isEmpty( ); iterations++ )
		{
			Vertex v = q.remove( );

			for( Edge e : v.adj )
			{
				Vertex w = e.dest;
				double cvw = e.cost;

				if( --w.scratch == 0 )
				{
					q.add( w );
					topOrder.add(w);
				}

				if( v.dist == INFINITY )
					continue;

				if( w.dist > v.dist + cvw )
				{
					w.dist = v.dist + cvw;
					w.prev = v;
				}
			}
		}

		if( iterations != vertexMap.size( ) )
			throw new GraphException( "Graph has a cycle!" );
	}
	
	private Collection<Vertex> inDegree()
	{
		// Compute the indegrees
		Collection<Vertex> vertexSet = vertexMap.values( );
		for( Vertex v : vertexSet )
			for( Edge e : v.adj )
				e.dest.scratch++;
		return vertexSet;
	}

	private Vertex getVertex( String vertexName, int row, int col )
	{
		Vertex v = vertexMap.get( vertexName );
		if( v == null )
		{
			v = new Vertex( vertexName, row, col );
			vertexMap.put( vertexName, v );
		}
		return v;
	}
	
	public void printPath( String destName )
	{
		Vertex w = vertexMap.get( destName );
		if( w == null )
			throw new NoSuchElementException( );
		else if( w.dist == INFINITY )
			System.out.println( destName + " is unreachable" );
		else
		{
			System.out.print( "(Cost is: " + w.dist + ") " );
			printPath( w );
			System.out.println( );
		}
	}
	
	private void printPath( Vertex dest )
	{
		if( dest.prev != null )
		{
			printPath( dest.prev );
			System.out.print( " to " );
		}
		System.out.print( dest.name );
	}
	
	
	private void clearAll( )
	{
		for( Vertex v : vertexMap.values( ) )
			v.reset( );
	}

}



// Used to signal violations of preconditions for
// various shortest path algorithms.
class GraphException extends RuntimeException
{
	public GraphException( String name )
	{ super( name ); }
}