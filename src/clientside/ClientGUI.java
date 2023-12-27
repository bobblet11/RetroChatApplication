package clientside;
import javax.swing.*;
import javax.swing.event.*;

import misc.Message;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ClientGUI {
	
	public static void main(String args[])
	{
		ClientNetworkManager networkManager = new ClientNetworkManager();
		ClientGUI gui = new ClientGUI(networkManager);
	}
	
	//window
	private JFrame frame;
	private static JPanel cards;
	private int windowWidth = 500, windowHeight = 500;
	private GridBagConstraints c;
	private static CardLayout c1;
	
	//sign in
	private final static String LOGIN_CARD = "LOGIN_CARD";
	private final String LOGIN_TITLE = "Sign in";
	private final String USERNAME = "Username";
	private final String PASSWORD = "Password";
	private final String SUBMIT = "Submit";
	private static final String AUTHORISED = "Connection Authorised";
	private static final String UNAUTHORISED = "Account not found";
	private static final String BLOCKED_LOGIN = "You have been blocked";
	private JTextField usernameField;
	private static JPasswordField passwordField;
	private static JLabel authorisationResult;
	private static JButton submit;
	
	//serverListPage
	private final static String SERVERLIST_CARD = "SERVERLIST_CARD";
	private final String SERVERPAGE_TITLE = "Chatrooms";
	private final String SERVERLIST_TITLE = "Servers";
	private final String JOIN = "Join";
	private final String MEMBERS = "Members";
	public static JList<String> chatroomList;
	static DefaultListModel<String>  chatroomListModel;
	private DefaultListModel<String>  participantsListModel_SL;
	
	//messagingPage
	private final static String MESSAGING_CARD = "MESSAGING_CARD";
	private final String MESSAGING_PAGE_TITLE = "TITLE";
	private final String SEND = "Send";
	private final String LEAVE = "Leave";
	private static JTextArea leftTextPanel;
	private JTextField messageInput;
	private DefaultListModel<String>  participantsListModel_M;
	
	public static int selectedServer = -1;
	private Timer timer;
	
	private ClientNetworkManager networkManager;
	
	public ClientGUI(ClientNetworkManager networkManager)
	{
		initialiseGUI();
		this.networkManager = networkManager;
	}
	
	private void resetGridConstraints(GridBagConstraints c)
	{
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1; // default value
		c.gridheight = 1; // default value
		c.weightx = 0.0; // default value
		c.weighty = 0.0; // default value
		c.anchor = GridBagConstraints.CENTER; // default value
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 0, 0); // default value
		c.ipadx = 0; // default value
		c.ipady = 0; // default value
	}
	
	private void initialiseGUI()
	{
		//window
		c = new GridBagConstraints();
		frame = new JFrame("Retro Chatroom");
		frame.setSize(windowWidth, windowHeight);
		//cards
		cards = new JPanel(new CardLayout());
		frame.add(cards);
		
		
		//card initialization
		initialiseLogIn();
		initialiseServerList();
		initialiseMessagingPage();
		
		
		c1 = (CardLayout)(cards.getLayout());
		c1.show(cards,LOGIN_CARD);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	private void initialiseMessagingPage()
	{
		//clean up
		resetGridConstraints(c);
		
		//loading widgets
		//page
		JPanel messagePage = new JPanel(new BorderLayout());
		
		
		//page top section
		JPanel top = new JPanel(new BorderLayout());
		//page title
		JLabel messagingPageTitle = new JLabel(MESSAGING_PAGE_TITLE);
		messagingPageTitle.setFont(messagingPageTitle.getFont().deriveFont(40.0f));
		messagingPageTitle.setHorizontalAlignment(JLabel.CENTER);
		//server leave button
		JButton leaveChatroomButton = new JButton(LEAVE);
		leaveChatroomButton.addActionListener(new LeaveButtonListener());
		//adding to top section
		top.add(messagingPageTitle,BorderLayout.CENTER);
		top.add(leaveChatroomButton,BorderLayout.EAST);
		
		
		//messaging
		//texting area
		leftTextPanel = new JTextArea(100,10);
		leftTextPanel.setEditable(false);
		JScrollPane scrollMessages = new JScrollPane(leftTextPanel);
		scrollMessages.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollMessages.setPreferredSize(new Dimension(280, 450));
		
		
		//chat-room info
		//panel
		JPanel rightParticipantsPanel = new JPanel(new BorderLayout());
		rightParticipantsPanel.setPreferredSize(new Dimension(180, 450));
		//title
		JLabel memberListTitle2 = new JLabel(MEMBERS);
		memberListTitle2.setFont(memberListTitle2.getFont().deriveFont(20.0f));
		memberListTitle2.setHorizontalAlignment(JLabel.LEFT);
		//JList
		participantsListModel_M = new DefaultListModel<>();
		JList<String> participantsList2 = new JList(participantsListModel_M);
		JScrollPane participantsScrollPane2 = new JScrollPane(participantsList2);
		//adding
		rightParticipantsPanel.add(memberListTitle2, BorderLayout.NORTH);
		rightParticipantsPanel.add(participantsScrollPane2, BorderLayout.CENTER);
		
		
		//bottom page-controller
		JPanel messagingPageController = new JPanel();
		//send button
		JButton sendMessageButton = new JButton(SEND);
		//type input
		messageInput = new JTextField(20);
		//adding
		messagingPageController.add(messageInput);
		messagingPageController.add(sendMessageButton);
		
		
		//final adding
		messagePage.add(top,BorderLayout.NORTH);
		messagePage.add(scrollMessages, BorderLayout.WEST);
		messagePage.add(rightParticipantsPanel, BorderLayout.EAST);
		messagePage.add(messagingPageController,BorderLayout.SOUTH);
		//insert into card layout
		cards.add(messagePage,MESSAGING_CARD );
		
	}
	
	private void initialiseServerList()
	{
		//cleanup
		resetGridConstraints(c);
		
		//loading widgets
		//page
		JPanel serverListPage = new JPanel(new BorderLayout());
		
		
		//page title
		JLabel serverListPageTitle = new JLabel(SERVERPAGE_TITLE);
		serverListPageTitle.setFont(serverListPageTitle.getFont().deriveFont(40.0f));
		serverListPageTitle.setHorizontalAlignment(JLabel.CENTER);
						
		
		//list of chat-rooms
		JPanel leftPanelForChatrooms = new JPanel(new BorderLayout());
		leftPanelForChatrooms.setPreferredSize(new Dimension(280, 450));
		//title
		JLabel serverListTitle = new JLabel(SERVERLIST_TITLE);
		serverListTitle.setFont(serverListTitle.getFont().deriveFont(20.0f));
		serverListTitle.setHorizontalAlignment(JLabel.LEFT);
		//list
		chatroomListModel = new DefaultListModel<>();
		chatroomList = new JList(chatroomListModel);
		JScrollPane chatroomScrollPane = new JScrollPane(chatroomList);
		chatroomList.addListSelectionListener(new ServerSelectListener());
		//controller
		JPanel serverPageController = new JPanel();
		//join button
		JButton join = new JButton(JOIN);
		join.addActionListener(new JoinButtonListener());
		//adding
		serverPageController.add(join);
		//adding
		leftPanelForChatrooms.add(serverListTitle, BorderLayout.NORTH);
		leftPanelForChatrooms.add(chatroomScrollPane, BorderLayout.CENTER);
		leftPanelForChatrooms.add(serverPageController, BorderLayout.SOUTH);
		
		
		//list of participants in chat-room
		JPanel rightPanelForParticipants = new JPanel(new BorderLayout());
		rightPanelForParticipants.setPreferredSize(new Dimension(180, 450));
		//title
		JLabel memberListTitle = new JLabel(MEMBERS);
		memberListTitle.setFont(memberListTitle.getFont().deriveFont(20.0f));
		memberListTitle.setHorizontalAlignment(JLabel.LEFT);
		//Jlist
		participantsListModel_SL = new DefaultListModel<>();
		JList<String> participantsList = new JList(participantsListModel_SL);
		JScrollPane participantsScrollPane = new JScrollPane(participantsList);
		//adding
		rightPanelForParticipants.add(memberListTitle, BorderLayout.NORTH);
		rightPanelForParticipants.add(participantsScrollPane);
		
		
		//final adding
		serverListPage.add(serverListPageTitle, BorderLayout.NORTH);
		serverListPage.add(leftPanelForChatrooms, BorderLayout.WEST);
		serverListPage.add(rightPanelForParticipants,BorderLayout.EAST);
		//insert into card layout
		cards.add(serverListPage, SERVERLIST_CARD);
	}
	
	private void initialiseLogIn()
	{
		//cleanup
		resetGridConstraints(c);
		
		
		//loading widgets
		//page
		JPanel logInPanel = new JPanel();
		logInPanel.setLayout(new GridBagLayout());
		//title
		JLabel logInTitle = new JLabel(LOGIN_TITLE);
		logInTitle.setFont(logInTitle.getFont().deriveFont(20.0f));
		logInTitle.setHorizontalAlignment(JLabel.CENTER);
		//user-name
		JLabel usernameLabel = new JLabel(USERNAME);
		usernameField = new JTextField(20); 
		//password
		JLabel passwordLabel = new JLabel(PASSWORD);
		passwordField = new JPasswordField(20);
		//submit button
		submit = new JButton(SUBMIT);
		submit.addActionListener(new LoginButtonListener());
		//authentication result
		authorisationResult = new JLabel();
		
		
		//adding to grid
		//title
		c.gridwidth = 2; c.gridx = 1; c.gridy = 0; c.ipady = 50;
		logInPanel.add(logInTitle,c);
		resetGridConstraints(c);
		//user-name
		c.gridx = 1; c.gridy = 1; c.ipadx = 5;
		logInPanel.add(usernameLabel,c);
		c.gridx = 2; c.gridy = 1; c.ipadx = 5;
		logInPanel.add(usernameField,c);
		resetGridConstraints(c);
		//password
		c.gridx = 1; c.gridy = 2; c.ipadx = 5; c.insets = new Insets(10,0,10,0);
		logInPanel.add(passwordLabel,c);
		c.gridx = 2; c.gridy = 2; c.ipadx = 5;
		logInPanel.add(passwordField,c);
		resetGridConstraints(c);
		//authentication result
		c.gridx = 1; c.gridy = 3; c.gridwidth = 2; c.ipady = 5;
		logInPanel.add(authorisationResult, c);
		resetGridConstraints(c);
		//submit button
		c.gridx = 1; c.gridy = 4; c.gridwidth = 2; c.ipadx = 10; c.ipady = 10;
		logInPanel.add(submit, c);
		resetGridConstraints(c);
		
		
		//final adding
		//insert into card layout
		cards.add(logInPanel, LOGIN_CARD);
		
	}
	
	//regularly poll using this method
	public static void updateServerList()
	{
		chatroomListModel.clear();
		for (ArrayList<String> item : ClientNetworkManager.chatroomList)
		{
			chatroomListModel.addElement("chatroomID: " + item.get(0) + " ---- members: " + item.get(item.size()-1));
		}
  	  	chatroomList.setSelectedIndex(selectedServer);	
		
	}
	
	private void displayParticipants(int selectedID)
	{
		participantsListModel_SL.clear();
		participantsListModel_M.clear();
		for (int i = 1; i < ClientNetworkManager.chatroomList.get(selectedID).size()-1; i++)
		{
			String username = ClientNetworkManager.chatroomList.get(selectedID).get(i);
			if (username == null)
				username = "UnknownUser";
			participantsListModel_SL.addElement(username);
			participantsListModel_M.addElement(username);
		}
	}
	
	public static void successfulLogin()
	{
		authorisationResult.setText(AUTHORISED);
		submit.setEnabled(false);
		//go to next page
		c1.show(cards,SERVERLIST_CARD);
	}
	
	public static void unsuccessfulLogin()
	{
		authorisationResult.setText(UNAUTHORISED);
		passwordField.setText("");
	}
	
	public static void blockedLogin()
	{
		authorisationResult.setText(BLOCKED_LOGIN);
		submit.setEnabled(false);
	}
	
	public static void joinChatroom()
	{
		c1.show(cards, MESSAGING_CARD);
	}
	
	public static void exitChatroom()
	{
		c1.show(cards, SERVERLIST_CARD);
	}
	
	
	class LoginButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			String username = usernameField.getText();
			String password = new String(passwordField.getPassword());
			
			networkManager.setUsername(username);
			
			try
			{
				Message logInRequest = new Message(username+"?"+password, username, Message.LOGIN_REQUEST);
				networkManager.sendMessage(logInRequest);
			}
			catch(Exception e)
			{
				blockedLogin();
			}
						
		}
	}
	
	public static void updateTextArea(Message incomingMessage)
	{
		String message = incomingMessage.getSender() + ": " + incomingMessage.getMessageBody() + "/n";
		leftTextPanel.append(message);
	}	
	
	class JoinButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			networkManager.setConnectedChatroomID(Character.getNumericValue(chatroomList.getSelectedValue().charAt(12)));
			networkManager.sendMessage(new Message(Integer.toString(networkManager.getConnectedChatroomID()), networkManager.getUsername(), Message.JOIN_CHATROOM_REQUEST));
		}
	}
	class LeaveButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			networkManager.sendMessage(new Message(Integer.toString(networkManager.getConnectedChatroomID()), networkManager.getUsername(), Message.EXIT_CHATROOM_REQUEST));
		}
	}
	
	class ServerSelectListener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent event)
		{
			if (!event.getValueIsAdjusting())
			{
				if (chatroomList.getSelectedIndex()!= -1)
				{
					System.out.println("HERE");
					displayParticipants(Character.getNumericValue(chatroomList.getSelectedValue().charAt(12)));
					selectedServer = chatroomList.getSelectedIndex();
				}
				else
				{
					chatroomList.setSelectedIndex(selectedServer);
				}

			}
		}
	}
}
