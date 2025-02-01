package timingtest;

/** An SLList is a list of integers, which hides the terrible truth
 * of the nakedness within. */
public class SLList<Item> {
	private class IntNode {
		public Item item;
		public IntNode next;

		public IntNode(Item i, IntNode n) {
			item = i;
			next = n;
		}
	}

	/* The first item (if it exists) is at sentinel.next. */
	private IntNode sentinel;
	private int size;
	private IntNode last; // add a last pointer, always points the last node

	/** Creates an empty timingtest.SLList. */
	public SLList() {
		sentinel = new IntNode(null, null);
		last = sentinel;
		size = 0;
	}

	public SLList(Item x) {
		sentinel = new IntNode(null, null);
		last = new IntNode(x, null);
		sentinel.next = last;
		size = 1;
	}

	/** Adds x to the front of the list. */
	public void addFirst(Item x) {
		sentinel.next = new IntNode(x, sentinel.next);
		if (size == 0){
			last = sentinel.next;
		}
		size = size + 1;
	}

	/** Returns the first item in the list. */
	public Item getFirst() {
		return sentinel.next.item;
	}

	/** Adds x to the end of the list. */
	public void addLast(Item x) {
		IntNode newNode = new IntNode(x, null); // create a tempNode
		last.next = newNode;
		last = newNode;
		size = size + 1;
	}

	/** returns last item in the list */
	public Item getLast(int i) {
		return last.item; // now, last is the end of List
	}


	/** Returns the size of the list. */
	public int size() {
		return size;
	}

	public static void main(String[] args) {
		/* Creates a list of one integer, namely 10 */
		SLList L = new SLList();
		L.addLast(20);
		System.out.println(L.size());
	}
}
