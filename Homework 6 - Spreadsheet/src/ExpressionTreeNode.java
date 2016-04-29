
/**
 * This class is a single node in an expression tree. The node will contain a
 * token value and may have a left and right node.
 * 
 * @author Patrick Cruz
 *
 */
public class ExpressionTreeNode {

	// The value of the node
	protected Token token;

	// The node's left child
	protected ExpressionTreeNode left;

	// The node's right child
	protected ExpressionTreeNode right;

	/**
	 * Initializes an ExpressionTreeNode with a token, a left child, and a right
	 * child.
	 * 
	 * @param theToken
	 *            the node's value
	 * @param theLeft
	 *            the node's left child
	 * @param theRight
	 *            the node's right child
	 */
	public ExpressionTreeNode(final Token theToken, final ExpressionTreeNode theLeft,
			final ExpressionTreeNode theRight) {
		this.token = theToken;
		this.left = theLeft;
		this.right = theRight;
	}

	/**
	 * Initializes an ExpressionTreeNode with a token and no children.
	 * 
	 * @param theToken
	 *            the node's value
	 */
	public ExpressionTreeNode(final Token theToken) {
		this(theToken, null, null);
	}
}
