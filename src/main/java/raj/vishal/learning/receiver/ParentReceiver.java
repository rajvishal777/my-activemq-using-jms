
package raj.vishal.learning.receiver;

import java.io.IOException;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import raj.vishal.learning.commons.MessageDto;


@Component
public class ParentReceiver implements MessageListener{

  //Holds connection factory which creates connection
  private ConnectionFactory factory;
  
  //Holds Created connection which will instantiate a session
  private Connection connection;
  
  //Holds Session Object. Any Message exchange has to be associated with session
  private Session session;
  
  //Holds Destination(Queue) where the message will be put
  private Destination destination;
  
  //Holds the consumer object which actually reads the message in Queue
  private MessageConsumer consumer;

   
  
  @Autowired
  private ApplicationContext applicationContext;
  
  
  @Override
  public void onMessage(Message message) {
    
    try{
      if(message instanceof TextMessage){
        TextMessage txtMessage = (TextMessage)message;
        System.out.println(txtMessage.getText() + " " + MessageDto.class);
        
        //Getting messageDto from string jsonMessage using jackson faster xml deserialize 
        MessageDto msg = deserializeJson(txtMessage.getText(),MessageDto.class);
        
        //Sends the message to appropriate queue based on capability
        sendMessageUsingCapability(msg.getCapability(),msg.getPayLoad());
      }
      else{
        System.out.println("message read from queue is not an instance of text message");
      }
    }
    catch(JMSException | IOException e){
      System.out.print("Exception occured with following trace : " );
      e.printStackTrace();
    }
    
  }
  
  
  /**
   * 
   * The method <code>sendMessageUsingCapability</code> 
   * searches for all the classes extending AbstractMessageListener
   * and sends the message to listener matching the capability of the message
   * @param capability
   * @param payload
   * @see
   */
  private void sendMessageUsingCapability(String capability, String payload){
    
    AbstractMessageListener fetchedListener = null;
    Map<String, AbstractMessageListener> listenersMap =
        applicationContext.getBeansOfType(AbstractMessageListener.class);

    
    for(AbstractMessageListener listener : listenersMap.values()){
      if(listener.getCapability().equalsIgnoreCase(capability)){
        fetchedListener = listener;
        break;
      }
    }
    
    if(fetchedListener == null){
      System.out.println("No listeneres for sent capability");
    }
    else{
      fetchedListener.onMessageReceive(payload);
    }
    
  }
  
  
  /**
   * 
   * The method <code>run</code>
   * establishes initial connection with the queue
   * at PostConstruction of this Bean
   * @see
   */
  @PostConstruct
  public void run(){
    //Instantiate a connection factory which connects to ActiveMQ at Default URL. 
    //It can be changed also
    factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_BROKER_URL);
    try{
      //Creating and starting connection out of factory
      connection = factory.createConnection();
      connection.start();
      
      
      //Creating a new session in which message exchange will happen
      session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
      
      
      //Taking the reference of queue object
      destination = session.createQueue("MyQueue");
      
      
      //Creating consumer to read message from queue
      consumer = session.createConsumer(destination);
      
      
      //Getting the consumer listener associated with this class
      //After this, if message arrives at the connected Queue, 
      //onMessage of this class will get called automatically 
      consumer.setMessageListener(this);
    }
    catch(JMSException e){
      System.out.println("Error creating connection for receive");
    }
  }
  
  
  
  private <T> T deserializeJson(String jsonString, Class<T> myClass) throws JsonParseException, JsonMappingException, IOException{
    
    Class<T> dtoClass = myClass;
    ObjectMapper mapper = new ObjectMapper();
    T obj = mapper.readValue(jsonString, dtoClass);
    return obj;
    
  }

}
