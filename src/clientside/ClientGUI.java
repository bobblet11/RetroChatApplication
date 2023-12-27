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
	private JPanel cards;
	private int windowWidth = 500, windowHeight = 500;
	private GridBagConstraints c;
	private CardLayout c1;
	
	//sign in
	private JPanel logInPage;
	private JPanel logInPanel;
	private final static String LOGIN_CARD = "LOGIN_CARD";
	private final String LOGIN_TITLE = "Sign in";
	private final String USERNAME = "Username";
	private final String PASSWORD = "Password";
	private final String SUBMIT = "Submit";
	private final String AUTHORISED = "Connection Authorised";
	private final String UNAUTHORISED = "Account not found";
	private final String BLOCKED_LOGIN = "You have been blocked";
	private JLabel logInTitle;
	private JLabel usernameLabel;
	private JTextField usernameField;
	private JLabel passwordLabel;
	private JPasswordField passwordField;
	private JButton submit;
	private JLabel authorisationResult;
	
	//serverListPage
	private JPanel serverListPage;
	private final static String SERVERLIST_CARD = "SERVERLIST_CARD";
	private final String SERVERPAGE_TITLE = "Chatrooms";
	private final String SERVERLIST_TITLE = "Servers";
	private final String JOIN = "Join";
	private final String MEMBERS = "Members";
	private JLabel serverListPageTitle;
	private JLabel serverListTitle;
	private JLabel memberListTitle;
	private JButton join;
	private JPanel leftHalf;
	private JPanel rightHalf;
	private JPanel controllerPanel1;
	DefaultListModel<String>  chatroomListModel;
	JList<String> chatroomList;
	DefaultListModel<String>  participantsListModel;
	JList<String> participantsList;
	JScrollPane participantsScrollPane;
	
	//messagingPage
	private JPanel messagePage;
	private final static String MESSAGING_CARD = "MESSAGING_CARD";
	private final String MESSAGING_PAGE_TITLE = "TITLE";
	private final String SEND = "Send";
	private final String LEAVE = "Leave";
	private JLabel messagingPageTitle;
	private JPanel controllerPanel2;
	private JLabel memberListTitle2;
	private JTextArea leftTextPanel;
	private JScrollPane scrollMessages;
	private JScrollPane scrollParticipants;
	private JPanel rightParticipantsPanel;
	private JButton sendMessageButton;
	private JButton leaveChatroomButton;
	private JTextField messageInput;
	DefaultListModel<String>  participantsListModel2;
	JList<String> participantsList2;
	JScrollPane participantsScrollPane2;
	
	ClientNetworkManager networkManager;
	public ClientGUI(ClientNetworkManager networkManager)
	{
		this.networkManager = networkManager;
		initialiseGUI();
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
		messagePage = new JPanel(new BorderLayout());
		
		//page title
		JPanel top = new JPanel();
		messagingPageTitle = new JLabel(MESSAGING_PAGE_TITLE);
		messagingPageTitle.setFont(messagingPageTitle.getFont().deriveFont(40.0f));
		messagingPageTitle.setHorizontalAlignment(JLabel.CENTER);
		leaveChatroomButton = new JButton(LEAVE);
		leaveChatroomButton.addActionListener(new LeaveButtonListener());
		top.add(messagingPageTitle);
		top.add(leaveChatroomButton);
		
		//messages
		leftTextPanel = new JTextArea(100,10);
		leftTextPanel.setEditable(false);
		scrollMessages = new JScrollPane(leftTextPanel);
		scrollMessages.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollMessages.setPreferredSize(new Dimension(280, 450));
		
		//participants
		rightParticipantsPanel = new JPanel(new BorderLayout());
		//members list title
		memberListTitle2 = new JLabel(MEMBERS);
		memberListTitle2.setFont(memberListTitle2.getFont().deriveFont(12.0f));
		memberListTitle2.setHorizontalAlignment(JLabel.LEFT);
		//members list
		participantsListModel2 = new DefaultListModel<>();
		participantsList2 = new JList(participantsListModel2);
		participantsScrollPane2 = new JScrollPane(participantsList2);
		
		//adding
		rightParticipantsPanel.setPreferredSize(new Dimension(180, 450));
		rightParticipantsPanel.add(memberListTitle2, BorderLayout.NORTH);
		rightParticipantsPanel.add(participantsScrollPane2, BorderLayout.CENTER);
		
		//controller
		controllerPanel2 = new JPanel();
		//send button
		sendMessageButton = new JButton(SEND);
		//type input
		messageInput = new JTextField(20);
		//adding
		controllerPanel2.add(messageInput);
		controllerPanel2.add(sendMessageButton);
		
		//final adding
		messagePage.add(top,BorderLayout.NORTH);
		messagePage.add(scrollMessages, BorderLayout.WEST);
		messagePage.add(rightParticipantsPanel, BorderLayout.EAST);
		messagePage.add(controllerPanel2,BorderLayout.SOUTH);
		
		//insert into card layout
		cards.add(messagePage,MESSAGING_CARD );
		
	}
	
	private void initialiseServerList()
	{
		//cleanup
		resetGridConstraints(c);
		
		//loading widgets
		//page
		serverListPage = new JPanel(new BorderLayout());
		//page title
		serverListPageTitle = new JLabel(SERVERPAGE_TITLE);
		serverListPageTitle.setFont(serverListPageTitle.getFont().deriveFont(40.0f));
		serverListPageTitle.setHorizontalAlignment(JLabel.CENTER);
						
		
		//servers list
		leftHalf = new JPanel(new BorderLayout());
		leftHalf.setPreferredSize(new Dimension(280, 450));
		//server list title
		serverListTitle = new JLabel(SERVERLIST_TITLE);
		serverListTitle.setFont(serverListTitle.getFont().deriveFont(20.0f));
		serverListTitle.setHorizontalAlignment(JLabel.LEFT);
		//chatroomsList
		chatroomListModel = new DefaultListModel<>();
		chatroomList = new JList(chatroomListModel);
		JScrollPane chatroomScrollPane = new JScrollPane(chatroomList);
		chatroomList.addListSelectionListener(new ServerSelectListener());
		leftHalf.add(chatroomScrollPane, BorderLayout.CENTER);
		//controller
		controllerPanel1 = new JPanel();
		join = new JButton(JOIN);
		join.addActionListener(new JoinButtonListener());
		controllerPanel1.add(join);
		//adding
		leftHalf.add(controllerPanel1, BorderLayout.SOUTH);
		leftHalf.add(serverListTitle, BorderLayout.NORTH);
		
		
		//members list
		rightHalf = new JPanel(new BorderLayout());
		rightHalf.setPreferredSize(new Dimension(180, 450));
		//members list title
		memberListTitle = new JLabel(MEMBERS);
		memberListTitle.setFont(memberListTitle.getFont().deriveFont(20.0f));
		memberListTitle.setHorizontalAlignment(JLabel.LEFT);
		//partipcants
		participantsListModel = new DefaultListModel<>();
		participantsList = new JList(participantsListModel);
		participantsScrollPane = new JScrollPane(participantsList);
		rightHalf.add(participantsScrollPane);
		//adding
		rightHalf.add(memberListTitle, BorderLayout.NORTH);
		
		
		//final adding
		serverListPage.add(serverListPageTitle, BorderLayout.NORTH);
		serverListPage.add(leftHalf, BorderLayout.WEST);
		serverListPage.add(rightHalf,BorderLayout.EAST);
		//insert into card layout
		cards.add(serverListPage, SERVERLIST_CARD);
	}
	
	private void initialiseLogIn()
	{
		//cleanup
		resetGridConstraints(c);
		
		//loading widgets
		//page
		logInPage = new JPanel();
		logInPanel = new JPanel();
		logInPage.setLayout(new BorderLayout());
		logInPanel.setLayout(new GridBagLayout());
		//log in widgets
		//title
		logInTitle = new JLabel(LOGIN_TITLE);
		//username
		usernameLabel = new JLabel(USERNAME);
		usernameField = new JTextField(20); 
		//password
		passwordLabel = new JLabel(PASSWORD);
		passwordField = new JPasswordField(20);
		//submit button
		submit = new JButton(SUBMIT);
		submit.addActionListener(new LoginButtonListener());
		//authorisation result
		authorisationResult = new JLabel();
		
		
		//adding to grid
		//title
		c.gridwidth = 2; c.gridx = 1; c.gridy = 0; c.ipady = 50;
		logInTitle.setFont(logInTitle.getFont().deriveFont(20.0f));
		logInTitle.setHorizontalAlignment(JLabel.CENTER);
		logInPanel.add(logInTitle,c);
		resetGridConstraints(c);
		//username
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
		//authorisation result
		c.gridx = 1; c.gridy = 3; c.gridwidth = 2; c.ipady = 5;
		logInPanel.add(authorisationResult, c);
		resetGridConstraints(c);
		//submit button
		c.gridx = 1; c.gridy = 4; c.gridwidth = 2; c.ipadx = 10; c.ipady = 10;
		logInPanel.add(submit, c);
		resetGridConstraints(c);
		
		logInPage.add(logInPanel, BorderLayout.CENTER);
		//insert into card layout
		cards.add(logInPage, LOGIN_CARD);
		
	}
	
	//regularly poll using this method
	private void updateServerList()
	{
		chatroomListModel.clear();
		networkManager.fetchChatroomList();
		//replace with custom cell renderer instead of string
		for (ArrayList<String> item : ClientNetworkManager.chatroomList)
		{
			chatroomListModel.addElement("chatroomID: " + item.get(0) + " ---- members: " + item.get(item.size()-1));
		}
		
	}
	
	private void displayParticipants(int selectedID)
	{
		participantsListModel.clear();
		for (int i = 1; i < ClientNetworkManager.chatroomList.get(selectedID).size()-1; i++)
		{
			String username = ClientNetworkManager.chatroomList.get(selectedID).get(i);
			if (username == null)
				username = "UnknownUser";
			participantsListModel.addElement(username);
			participantsListModel2.addElement(username);
		}
	}
	
	class LoginButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			String username = usernameField.getText();
			String password = new String(passwordField.getPassword());
			
			boolean result = false;
			
			try
			{
				result = networkManager.attemptLogIn(username, password);
				if (result)
				{
					authorisationResult.setText(AUTHORISED);
					submit.setEnabled(false);
					c1.show(cards,SERVERLIST_CARD);
					//request server lists
					updateServerList();
				}
				else
				{
					authorisationResult.setText(UNAUTHORISED);
					passwordField.setText("");
				}
			}
			catch(Exception e)
			{
				authorisationResult.setText(BLOCKED_LOGIN);
				submit.setEnabled(false);
			}
						
		}
	}
	
	class JoinButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			networkManager.setConnectedChatroomID(Integer.parseInt(chatroomList.getSelectedValue().substring(chatroomList.getSelectedValue().indexOf(':')+2,chatroomList.getSelectedValue().indexOf(':')+3)));
			networkManager.sendMessage(new Message(Integer.toString(networkManager.getConnectedChatroomID()), networkManager.getUsername(), Message.JOIN_CHATROOM_REQUEST));
			if (networkManager.readMessage().joinChatroomIsApproved())
			{
				c1.show(cards, MESSAGING_CARD);
			}
		}
	}
	class LeaveButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			networkManager.sendMessage(new Message(Integer.toString(networkManager.getConnectedChatroomID()), networkManager.getUsername(), Message.EXIT_CHATROOM_REQUEST));
			c1.show(cards, SERVERLIST_CARD);
			if (networkManager.readMessage().exitChatroomIsApproved())
			{
				c1.show(cards, SERVERLIST_CARD);
			}
			networkManager.setConnectedChatroomID(-1);
		}
	}
	
	class ServerSelectListener implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent event)
		{
			if (!event.getValueIsAdjusting())
			{
				displayParticipants(Integer.parseInt(chatroomList.getSelectedValue().substring(chatroomList.getSelectedValue().indexOf(':')+2,chatroomList.getSelectedValue().indexOf(':')+3)));
			}
		}
	}
}
