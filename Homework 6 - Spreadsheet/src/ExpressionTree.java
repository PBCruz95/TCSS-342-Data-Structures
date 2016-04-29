
import java.util.Stack;

/**
 * This class is a tree of tokens found in a cell. Doing a post-order traversal
 * of the tree will calculate the tokens in the cell.
 * 
 * @author Patrick Cruz
 *
 */
public class ExpressionTree {

	// The tree's root. This node will not have parents
	private ExpressionTreeNode root;

	/**
	 * Initializes an empty tree.
	 */
	public ExpressionTree() {
		this.root = null;
	}

	/**
	 * Initializes an ExpressionTree.
	 * 
	 * @param theRoot
	 *            the root of the tree
	 */
	public ExpressionTree(final ExpressionTreeNode theRoot) {
		this.root = theRoot;
	}

	/**
	 * Empties the tree.
	 */
	public void empty() {
		this.root = null;
	}

	/**
	 * Builds an expression tree from a stack of tokens.
	 * 
	 * @param tokenStack
	 *            the stack of tokens
	 */
	public void buildTree(Stack<Token> tokenStack) {
		root = getTree(tokenStack);
		if (!tokenStack.isEmpty()) {
			System.out.println("Error in buildTree.");
		}
	}

	/**
	 * Gets tokens from a stack and organizes them into a proper tree.
	 * 
	 * @param theToken
	 */
	public ExpressionTreeNode getTree(Stack<Token> tokenStack) {
		ExpressionTreeNode returnTree = null;
		Token aToken;

		if (tokenStack.isEmpty())
			return null;

		aToken = tokenStack.pop();
		if (aToken instanceof LiteralToken || aToken instanceof CellToken) {
			// Literals and Cells are leaves in the tree
			returnTree = new ExpressionTreeNode(aToken);
			return returnTree;
		} else if (aToken instanceof OperatorToken) {
			// Continue finding tokens that will form the right and left
			// subtrees
			ExpressionTreeNode rightTree = getTree(tokenStack);
			ExpressionTreeNode leftTree = getTree(tokenStack);
			returnTree = new ExpressionTreeNode(aToken, leftTree, rightTree);
			return returnTree;
		}
		return returnTree;
	}

	/**
	 * Calculates the entire tree from the root.
	 * 
	 * @return the tree's value
	 */
	public int calculate(Cell[][] cellArray) {
		if (this.root != null)
			return calculate(this.root, cellArray);
		else
			return 0;
	}

	/**
	 * Calculates a subtree from its root through a post-order traversal.
	 * 
	 * @param theNode
	 *            the root of a subtree
	 * @return the expected value of a tree
	 */
	public int calculate(final ExpressionTreeNode theNode, Cell[][] cellArray) {
		int total=0;
		int leftVal=0;
		int rightVal=0;

		if(theNode != null && theNode.token instanceof OperatorToken) {
			
			//Go through left subtree
			//Object Cell;
			if(theNode.left != null && theNode.left.token instanceof LiteralToken)
			{
				leftVal = ((LiteralToken) theNode.left.token).getValue();
			}
				//TODO leftVal = theNode.left.token.value
		else if(theNode.left != null && theNode.left.token instanceof OperatorToken)
		{
			leftVal = calculate(theNode.left, cellArray);
		}				
		else if(theNode.left != null && theNode.left.token instanceof CellToken)
		{
			Cell aCell = cellArray[((CellToken)theNode.left.token).getRow()][((CellToken)theNode.left.token).getCol()];
			leftVal = aCell.getValue();
		}
			//Go through right subtree
			if(theNode.right.token instanceof LiteralToken)
			{
				rightVal = ((LiteralToken) theNode.right.token).getValue();
			}

				//TODO rightVal = theNode.right.token.value
	   	else if(theNode.right.token instanceof OperatorToken)
				rightVal = calculate(theNode.right, cellArray);
		else if(theNode.right.token instanceof CellToken)
		{
			Cell aCell = cellArray[((CellToken)theNode.right.token).getRow()][((CellToken)theNode.right.token).getCol()];
			rightVal = aCell.getValue();
		}
			
			switch (((OperatorToken) theNode.token).getOperatorToken()) {
			//TODO leftVal = theNode.right.token.value
			
			//Calculate both subtrees
			case OperatorToken.PLUS:
				total = leftVal + rightVal;
				break;
			case OperatorToken.MINUS:
				total = leftVal - rightVal;
				break;
			case OperatorToken.MULT:
				total = leftVal * rightVal;
				break;
			case OperatorToken.DIV:
				total = leftVal / rightVal;
				break;
			default:
				System.out.println("ERROR");
			}
		}
		else if (theNode != null && theNode.token instanceof LiteralToken)
			total = ((LiteralToken) theNode.token).getValue();
		else if (theNode != null && theNode.token instanceof CellToken)
		{
			Cell aCell = cellArray[((CellToken)theNode.token).getRow()][((CellToken)theNode.token).getCol()];
			total = aCell.getValue();
		}
		
		return total;
	}
	
	public void print()
	{
		print(root);
	}

	/**
	 * Prints the entire tree in a post-order traversal.
	 * 
	 * @param theNode
	 *            a root of a subtree
	 */
	private void print(final ExpressionTreeNode theNode) {
		if (root == null)
			System.out.println("The tree is empty.");
		else if (theNode != null) {
			print(theNode.left);
			print(theNode.right);
			System.out.println(theNode.token.toString());
		}
	}
}
