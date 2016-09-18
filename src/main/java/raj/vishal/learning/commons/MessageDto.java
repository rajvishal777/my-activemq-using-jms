
package raj.vishal.learning.commons;

/* Stores the Message */
public class MessageDto {
  
  //Stores the capability
  String capability;
  
  //Stores the message payload
  String payLoad;
  
  
  public String getPayLoad() {
    return payLoad;
  }
  
  public void setPayLoad(String payLoad) {
    this.payLoad = payLoad;
  }
  
  public String getCapability() {
    return capability;
  }
  
  public void setCapability(String capability) {
    this.capability = capability;
  }
  @Override
  public String toString() {
    return "MessageDto [capability=" + capability + ", payLoad=" + payLoad + "]";
  }
  
  
  

}
