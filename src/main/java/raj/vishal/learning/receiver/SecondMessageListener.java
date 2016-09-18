
package raj.vishal.learning.receiver;

import org.springframework.stereotype.Component;


@Component
public class SecondMessageListener extends AbstractMessageListener{

  /* (non-Javadoc)
   * @see raj.vishal.learning.receiver.IMessageListener#onMessageReceive(java.lang.String)
   */
  @Override
  public void onMessageReceive(String message) {
    
    System.out.println("Message received by second message listener : " + message);
    
  }

  /* (non-Javadoc)
   * @see raj.vishal.learning.receiver.IMessageListener#getCapability()
   */
  @Override
  public String getCapability() {
    return "second";
  }

}
