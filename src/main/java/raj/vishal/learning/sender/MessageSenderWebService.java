
package raj.vishal.learning.sender;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;


@Path("/send")
public class MessageSenderWebService {
  
  //Holds connection factory which creates connection
  private ConnectionFactory factory = null;
  
  //Holds Created connection which will instantiate a session
  private Connection connection = null;
  
  //Holds Session Object. Any Message exchange has to be associated with session
  private Session session = null;
  
  //Holds Destination(Queue) where the message will be put
  private Destination destination = null;
  
  //Holds the producer object which actually puts the message in Queue
  private MessageProducer producer = null;
  
  
  
  /**
   * 
   * The method <code>sendMessage</code> 
   * is responsible for putting a message in the queue.
   * It receives the message via a POST request in JSON format.
   * @param jsonString
   * @return
   * @see
   */
  @POST
  @Consumes("application/json")
  public Response sendMessage(final String jsonString){
    
    Response response = null;
    boolean msgStatus = sendMessageToQueue(jsonString);
    
    if(msgStatus)
      response = Response.ok().build();
    else
      response = Response.status(400).build();
    
    return response;
  }
  
  
  private boolean sendMessageToQueue(String json){
    try {
      //Instantiate a connection factory which connects to ActiveMQ at Default URL. 
      //It can be changed also
      factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
      
      
      //Creating and starting connection out of factory
      connection = factory.createConnection();
      connection.start();
      
      //Creating a new session in which message exchange will happen
      session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      
      //Taking the reference of queue object
      destination = session.createQueue("MyQueue");
      
      //Creating producer to put message in queue
      producer = session.createProducer(destination);
      
      TextMessage message = session.createTextMessage();
      message.setText(json);
      
      //Sending message to queue
      producer.send(message);
      
      System.out.println("Message put successfully ");
      
    } catch (JMSException e) {
      System.out.println("Error while putting message to Queue");
      return false;
    } 
    return true;
  }
  
  
  

}
