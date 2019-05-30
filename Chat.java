/*
CET 350 
Program 7 - Chat Program 
Created: 4/26/17
Last Edited: 5/3/17
Description: 
Program that is used either client side or server side.
Client side allows the user to send messages to server, 
server side listens for connections and attaches them 
once found.  
*/

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.lang.*;
import java.text.*;
import java.net.*; 


public class Chat extends Frame implements ActionListener, WindowListener, Runnable, ItemListener, ComponentListener
{

	private static final long serialVersionUID = 1L;
//initialize server sockets and variables
		  Socket server;
		  Socket client;
    ServerSocket listen_socket;
          String hostIP = "192.168.0.1";
             int portNum = 41004; 
             int service = 0; 
     

  //initialize panels, layouts, and component variables
	      BorderLayout bdr = new BorderLayout(); 
	      GridBagConstraints gc = new GridBagConstraints(); 
	      GridBagLayout gl = new GridBagLayout();
  private Thread theThread; 
  private Panel controlPanel; 
  private Insets i; 
      int h = 500; 
      int w = 500; 
      int xUnits; 
      int yUnits; 
      int sh; 
      int sw;  
  
  //initialize buttons, labels, and text areas
  private Button send; 
  private Button host; 
  private Button serverButton; 
  private Button port; 
  private Button connect; 
  private Button disconnect; 
  private Label hostLabel; 
  private Label portLabel; 
  private Label messageLabel; 
  private TextField messageField; 
  private TextArea statusField; 
  private TextField hostField; 
  private TextField portField; 
  private TextArea sentMessages; 
  
  //initialize Menu Bar, Menus, and Menu components 
  private MenuBar mb; 
  private Menu Control, Color; 
  private MenuItem quit, disconnectitem, connectitem, serveritem; 
  private CheckboxMenuItem red, blue, green, yellow, orange, black;  

  Chat()//constructor for Chat class
  {    
    this.setVisible(true); 
    this.setLayout(bdr); 
    this.setMinimumSize(new Dimension(750,750)); 
    MakeSheet(); 
    
    try
    {
      initcomponents(); 
    }
    catch(Exception e) 
    {
      e.printStackTrace(); 
    }
  }
  
  public static void main(String []args) //main method for chat class 
  {
    Chat c = new Chat(); 
  }
  
  public void initcomponents() //method to initialize gui components
  {
    //initialize menu components
         mb = new MenuBar(); 
    Control = new Menu(); 
      Color = new Menu();
    
    //add menus to menu bar 
         mb.add(Control); 
         mb.add(Color); 
    Control.setLabel(" Control "); 
      Color.setLabel(" Text Color "); 
    
    //initialize menu items 
          quit = Control.add(new MenuItem(" Quit ", new MenuShortcut(KeyEvent.VK_Q))); 
disconnectitem = Control.add(new MenuItem(" Disconnect ", new MenuShortcut(KeyEvent.VK_D))); 
   connectitem = Control.add(new MenuItem(" Connect ", new MenuShortcut(KeyEvent.VK_C))); 
    serveritem = Control.add(new MenuItem(" Start Server ", new MenuShortcut(KeyEvent.VK_S))); 
    				quit.addActionListener(this); 
          disconnectitem.addActionListener(this); 
             connectitem.addActionListener(this); 
              serveritem.addActionListener(this); 
    
    //initialize checkbox menu items 
    Color.add(red = new CheckboxMenuItem(" Red ", false)); 
    Color.add(yellow = new CheckboxMenuItem(" Yellow ", false)); 
    Color.add(blue = new CheckboxMenuItem(" Blue ", false)); 
    Color.add(orange = new CheckboxMenuItem(" Orange ", false)); 
    Color.add(green = new CheckboxMenuItem(" Green ", false)); 
    Color.add(black = new CheckboxMenuItem(" Black ", true)); 
    
    //add item listener to checkbox items
      red.addItemListener(this); 
   yellow.addItemListener(this); 
     blue.addItemListener(this); 
   orange.addItemListener(this); 
    green.addItemListener(this); 
    black.addItemListener(this); 
    
    //initialize frame values 
    this.setTitle(" Chat Program "); 
    this.setVisible(true); 
    this.setResizable(true); 
    this.addWindowListener(this);  
    this.addComponentListener(this); 
    this.setLayout(bdr); 
    this.setMenuBar(mb);
    this.setResizable(true);
    
    //initialize top panel component  
    sentMessages = new TextArea("", 10, 80);
    this.add(sentMessages, BorderLayout.NORTH);  
    
    //initialize bottom panel component 
    statusField = new TextArea("", 3, 80); 
    this.add(statusField, BorderLayout.SOUTH); 
    
    //initialize middle panel component
    controlPanel = new Panel(); 
    this.add(controlPanel, BorderLayout.CENTER);
     
    //initialize components for panel layout
    double colWeight[] = {0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5, 0.5}; 
    double rowWeight[] = {0.5, 0.5, 0.5, 0.5, 0.5}; 
    int colWidth[] = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}; 
    int rowHeight[] = {1, 1, 1, 1, 1}; 
    
         send = new Button(); 
         host = new Button(); 
 serverButton = new Button(); 
         port = new Button(); 
      connect = new Button(); 
   disconnect = new Button(); 
       
   	    send.setLabel(" Send Message ");
        host.setLabel(" Change Host "); 
serverButton.setLabel(" Start Server ");
        port.setLabel(" Change Port "); 
     connect.setLabel(" Connect "); 
  disconnect.setLabel(" Disconnect "); 
  
    hostLabel = new Label(" Host: "); 
    portLabel = new Label(" Port: ");
 messageLabel = new Label(" Message: "); 
 
 messageField = new TextField(50);  
    hostField = new TextField("Host", 50); 
    portField = new TextField("44004", 50); 
    
    //add listeners to components 
        host.addActionListener(this); 
        send.addActionListener(this); 
serverButton.addActionListener(this); 
     connect.addActionListener(this); 
  disconnect.addActionListener(this); 
messageField.addActionListener(this); 
    
    //assign values to panel layout 
    gl.rowHeights = rowHeight; 
    gl.columnWidths = colWidth; 
    gl.rowWeights = rowWeight; 
    gl.columnWeights = colWeight; 
    gc.anchor = 10; 
    gc.fill = -1; 
    
    //add message label to grid
    gc.gridx = 1; 
    gc.gridy = 1; 
    gl.setConstraints(messageLabel, gc);  
    
    //add text field to grid 
    gc.gridx = 2; 
    gc.gridy = 1; 
    gl.setConstraints(messageField, gc); 
    
    //add send button to grid
    gc.gridx = 3; 
    gc.gridy = 1; 
    gl.setConstraints(send, gc); 
    
    //add host label to grid
    gc.gridx = 1; 
    gc.gridy = 2; 
    gl.setConstraints(hostLabel, gc); 
    
    //add host field to grid
    gc.gridx = 2; 
    gc.gridy = 2; 
    gl.setConstraints(hostField, gc);
    
    //add host button to grid
    gc.gridx = 3; 
    gc.gridy = 2; 
    gl.setConstraints(host, gc);  
    
    //add server button to grid 
    gc.gridx = 4; 
    gc.gridy = 2; 
    gl.setConstraints(serverButton, gc); 
    
    //add port label to grid
    gc.gridx = 1; 
    gc.gridy = 3; 
    gl.setConstraints(portLabel, gc); 
    
    //add port field to grid 
    gc.gridx = 2; 
    gc.gridy = 3; 
    gl.setConstraints(portField, gc);
    
    //add port button to grid
    gc.gridx = 3; 
    gc.gridy = 3; 
    gl.setConstraints(port, gc); 
    
    //add connect button to grid
    gc.gridx = 4; 
    gc.gridy = 3; 
    gl.setConstraints(connect, gc);  
    
    //add disconnect button to grid
    gc.gridx = 4; 
    gc.gridy = 4; 
    gl.setConstraints(disconnect, gc);  
    
    //add components to panel 
    controlPanel.add(messageLabel); 
    controlPanel.add(messageField);  
    controlPanel.add(send); 
    controlPanel.add(hostLabel); 
    controlPanel.add(hostField); 
    controlPanel.add(host);  
    controlPanel.add(serverButton); 
    controlPanel.add(portLabel); 
    controlPanel.add(portField); 
    controlPanel.add(port); 
    controlPanel.add(connect); 
    controlPanel.add(disconnect); 
    
    //set layout for panel
    controlPanel.setLayout(gl); 
        
    SizeScreen(); //initialize start of screen 
    start(); //begin thread 
    this.validate(); //validate components  
    
  }
  
  public void start() //method to start thread 
  {
    if(theThread == null)
    {
      theThread = new Thread(this);
      theThread.start();  
    }
  }
  
  public void run()
  { 
 
  }
  
  public void stop() //method to stop thread and close window
  {
    theThread = null; 
    this.dispose(); 
    this.setVisible(false); 
  }
  
  public void MakeSheet() //use insets to create sheet 
  {
    xUnits = w/34; 
    yUnits = h/24; 
    
    i = this.getInsets(); 
    
    sh = h - i.top - i.bottom - (yUnits*6);
    sw = w - i.left - i.right - (xUnits*2);
  }
  
  private void SizeScreen() //set frame size 
  {
     this.setSize(w,h);
	 this.setLocation((xUnits), (yUnits));
  }
  
  public void actionPerformed(ActionEvent ae)
  {
    Object o = ae.getSource(); 
    
    if(o == quit) //if user selects quit or hits hot key, end the program 
    {
      stop(); 
    }
    
    if(o == send)//if user selects send or hits enter 
    {
       
    }
    
    if(o == serverButton)//if user clicks the server button
    {
          connect.setEnabled(false); 
      connectitem.setEnabled(false); 
      startServer();
     
      
    }
    
    if(o == serveritem)
    {
          connect.setEnabled(false); 
      connectitem.setEnabled(false); 
      startServer();
             
    }
    
    if(o == disconnectitem)//if user clicks the disconnect button
    {
          connect.setEnabled(true); 
      connectitem.setEnabled(true); 
     serverButton.setEnabled(true); 
       serveritem.setEnabled(true); 
    }
    
    if(o == disconnect)
    {
          connect.setEnabled(true); 
      connectitem.setEnabled(true); 
     serverButton.setEnabled(true); 
       serveritem.setEnabled(true); 
    }
    
    if(o == connectitem)//if the user clicks the connect button 
    {
      serverButton.setEnabled(false); 
        serveritem.setEnabled(false); 
        startClient();
    }
    
    if(o == port)//if user clicks on the port button 
    {
    
    }
    
    if(o == host)//if user clicks on the host button 
    {
    
    }
    
    if(o == connect)//if user clicks on connect button 
    {
      serverButton.setEnabled(false); 
        serveritem.setEnabled(false);
        startClient();
    }
  }
  
  
  public void startServer(){
	  
	  try {
	  		listen_socket = new ServerSocket(portNum);
	  	//	client = listen_socket.accept();
	  		listen_socket.setSoTimeout(1000000000);
	  		statusField.append("in 1 \n");
	  	} catch (Exception e) {
	  		statusField.append("ERROR in startServer (new ServerSocket() \n");
	  	}
	  statusField.append("in 1 \n");
	  
	//  try {
//		client = listen_socket.accept();
//		statusField.append("in 2 \n");
//	} catch (IOException e) {
//		statusField.append("ERROR in startServer (listen_socket.Accept() \n");
//	}
	  
//	  try {
//		InputStreamReader is = new InputStreamReader(client.getInputStream());
//		BufferedReader br = new BufferedReader(is);
//		PrintWriter pw = new PrintWriter(client.getOutputStream(), true);
//	} catch (IOException e) {
///		statusField.append("ERROR in startServer communications try/catch \n");
//	}
	  
	  
	  
	  
	  
/*	  try {
	  		listen_socket = new ServerSocket(portNum);
	  		
	  	} catch (IOException e) {
	  		e.printStackTrace();
	  	}
	      while(theThread != null && service == 0)
	      {
	   	  try {
	   		client = listen_socket.accept();
	   	} catch (IOException e) {
	   		e.printStackTrace();
	   		statusField.append("Error \n");
	   	}
	   	  statusField.append("Connecting....... \n");
	       } */
  }
  
  public void startClient(){
	  
	  server = new Socket();
	  
	  try {
		server.connect(new InetSocketAddress(hostIP, portNum));
		statusField.append("in 3 \n");
	} catch (IOException e) {
		statusField.append("ERROR in start client \n");
	}
	  statusField.append("in 3 \n");
  }
  
  public void windowDeactivated(WindowEvent we) 
  {
     
  }
  
  public void windowActivated(WindowEvent we)
  {
  
  }
  
  public void windowDeiconified(WindowEvent we)
  {
  
  }
  
  public void windowIconified(WindowEvent we)
  {
  
  }
  
  public void windowClosed(WindowEvent we) 
  {
     
  }
  
  public void windowClosing(WindowEvent we) //close window when exited
  { 
    stop(); 
  }
  
  public void windowOpened(WindowEvent we)
  {
  
  }
  
  public void itemStateChanged(ItemEvent ie)
  {
    Object o = ie.getSource(); 
    
    if(o == red) //if user clicks red, set red to true, other options to false
    {
       red.setState(true); 
      blue.setState(false); 
    orange.setState(false); 
     green.setState(false);  
    yellow.setState(false); 
     black.setState(false); 
    }
    
    if(o == blue) //if user clicks blue, set blue to true, other options to false
    {
     blue.setState(true); 
      red.setState(false); 
   orange.setState(false); 
   yellow.setState(false); 
    black.setState(false); 
    green.setState(false); 
    }
    
    if(o == orange) //if user clicks orange, set orange to true, other options to false
    {
   orange.setState(true); 
      red.setState(false); 
   yellow.setState(false); 
    black.setState(false); 
    green.setState(false); 
     blue.setState(false); 
    }
    
    if(o == yellow) //if user clicks yellow, set yellow to true, other options to false
    {
   yellow.setState(true); 
      red.setState(false); 
   orange.setState(false); 
    black.setState(false); 
    green.setState(false); 
     blue.setState(false); 
    }
    
    if(o == green) //if user clicks green, set green to true, other options to false
    {
    green.setState(true);
      red.setState(false); 
   orange.setState(false); 
    black.setState(false); 
     blue.setState(false); 
   yellow.setState(false); 
    }
    
    if(o == black) //if user clicks black, set black to true, other options to false
    {
    black.setState(true); 
      red.setState(false); 
   orange.setState(false); 
    green.setState(false); 
     blue.setState(false); 
   yellow.setState(false); 
    }
  }
  
  public void componentHidden(ComponentEvent ce)
  {
  
  }
  
  public void componentShown(ComponentEvent ce)
  {
  
  }
  
  public void componentMoved(ComponentEvent ce)
  {
  
  }
  
  public void componentResized(ComponentEvent ce) //resizes components when resized
  {
     h = this.getHeight();
	 w = this.getWidth();
      
	 MakeSheet();
	 SizeScreen();
  }
  
}//end class Chat 