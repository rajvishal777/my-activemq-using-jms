
package raj.vishal.learning.receiver;

/* This abstract class is to be extended by a Listener who 
 * wants the message to be received from the queue*/

public abstract class AbstractMessageListener {
  
  /**
   * This abstract method is supposed to 
   * return capability for any listener that
   * extends this class. Based on the capability,
   * Message is transferred to the listener 
   * */
  public abstract String getCapability();
  
  
  /**
   * 
   * The method <code>onMessageReceive</code> 
   * defines the action to be taken when message is received
   * @param message
   * @see
   */
  public abstract void onMessageReceive(String message);

}
