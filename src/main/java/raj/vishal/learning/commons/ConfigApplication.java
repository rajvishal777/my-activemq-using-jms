
package raj.vishal.learning.commons;

import org.glassfish.jersey.server.*;

import raj.vishal.learning.sender.MessageSenderWebService;


public class ConfigApplication extends ResourceConfig{
  
  
  public ConfigApplication(){
    //Registering class to be looked for path via servlet
    register(MessageSenderWebService.class);
    
  }

}
