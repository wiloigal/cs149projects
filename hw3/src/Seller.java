import java.util.LinkedList;
import java.util.Queue;

/**
 * This class holds the common data that each level of sellers will have
 * @author josh
 *
 */
public abstract class Seller {
	Queue<Customer> customers = new LinkedList<Customer>();
	public static void main(String[] args){
		System.out.print("HI");
	}
}
