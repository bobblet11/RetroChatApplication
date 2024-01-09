package client;
import javax.swing.*;
import javax.swing.text.*;

import server.Server;

import javax.swing.event.*;

import shared.*;
import shared.Message.command;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ClientGUI {

	private final String windowIcon = "resources/icon.png";
	
	enum Cards {
		LOGIN_CARD,
		SERVERLIST_CARD,
		MESSAGING_CARD
	}
	
	//window
	static JFrame frame;
	static JPanel cards;
	static CardLayout cardLayout;
	private int windowWidth = 500, windowHeight = 500;
	private GridBagConstraints c;
	
	//sign in
	private final String LOGIN_TITLE = "Sign in";
	private final String USERNAME = "Username";
	private final String PASSWORD = "Password";
	private final String SUBMIT = "Submit";
	
	static final String AUTHORISED = "Connection Authorised";
	static final String UNAUTHORISED = "Account not found";
	static final String BLOCKED_LOGIN = "You have been blocked";
	
	private JTextField usernameField;
	static JPasswordField passwordField;
	static JLabel authorisationResult;
	static JButton submit;
	
	//serverListPage
	private final String SERVERPAGE_TITLE = "Chatrooms";
	private final String SERVERLIST_TITLE = "Servers";
	private final String JOIN = "Join";
	private final String MEMBERS = "Members";
	
	static DefaultListModel<String>  chatroomListModel =  new DefaultListModel<String>();
	static JList<String> chatroomList = new JList<String>(chatroomListModel);
	
	private DefaultListModel<String>  participantsListModel_SL =  new DefaultListModel<String>();
	
	//messagingPage
	private final String MESSAGING_PAGE_TITLE = "TITLE";
	private final String SEND = "Send";
	private final String LEAVE = "Leave";
	private static final String UNKNOWN_ROOM = "UNKNOWN ID";
	static JLabel messagingPageTitle;
	static String currentChatroom = UNKNOWN_ROOM;
	static JTextPane leftTextPanel;
	
	private JTextField messageInput;
	private DefaultListModel<String>  participantsListModel_M =  new DefaultListModel<String>();
	
	
	static int selectedServer = 0;
	
	private ClientNetworkManager networkManager;
	
	public ClientGUI(ClientNetworkManager networkManager) {
		initialiseGUI();
		this.networkManager = networkManager;
	}
	
	private void resetGridConstraints(GridBagConstraints c) {
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
	
	private void initialiseGUI() {
		c = new GridBagConstraints();
		frame = new JFrame("Retro Chatroom");
		frame.setSize(windowWidth, windowHeight);
		Image icon = Toolkit.getDefaultToolkit().getImage(windowIcon);    
		frame.setIconImage(icon);    
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		cards = new JPanel(new CardLayout());
		frame.add(cards);
		
		initialiseLogIn();
		initialiseServerList();
		initialiseMessagingPage();
		
		cardLayout = (CardLayout)(cards.getLayout());
		cardLayout.show(cards,Cards.LOGIN_CARD.toString());
	}
	
	private void initialiseMessagingPage() {
		//clean up
		resetGridConstraints(c);
		
		//loading widgets
		//page
		JPanel messagePage = new JPanel(new GridBagLayout());
		int UIwidth = 400;
		int UIheight = 400;
		
		//TOP
		//page title
		messagingPageTitle = new JLabel(UNKNOWN_ROOM);
		messagingPageTitle.setFont(messagingPageTitle.getFont().deriveFont(40.0f));
		messagingPageTitle.setHorizontalAlignment(JLabel.CENTER);
		messagingPageTitle.setPreferredSize(new Dimension(UIwidth, 50));
		
//		//server leave button
//		JButton leaveChatroomButton = new JButton(LEAVE);
//		leaveChatroomButton.addActionListener(new LeaveButtonListener());
//		leaveChatroomButton.setPreferredSize(new Dimension(50, 20));
//		//adding to top section
//		c.gridx=1; c.gridy = 0; c.gridwidth=2; 
//		top.add(messagingPageTitle, c);
//		c.gridx=3; c.gridy = 0; c.gridwidth=1; c.ipadx = 25; c.ipady = 10;
//		top.add(leaveChatroomButton,c);
//		resetGridConstraints(c);
//		
		//LEFT
		//messaging
		int textingWidth = 250; int textingHeight = 300;
		int buttonWidth = 20; int buttonHeight = 20;
		JPanel leftMessagingPanel = new JPanel(new GridBagLayout());
		leftMessagingPanel.setPreferredSize(new Dimension(textingWidth, textingHeight));
		//leave button
		JButton leaveChatroomButton = new JButton(LEAVE);
		leaveChatroomButton.addActionListener(new LeaveButtonListener());
		leaveChatroomButton.setPreferredSize(new Dimension(textingWidth, buttonHeight));
		//texting area
		leftTextPanel = new JTextPane();
		leftTextPanel.setEditable(false);
		JScrollPane scrollMessages = new JScrollPane(leftTextPanel);
		scrollMessages.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollMessages.setPreferredSize(new Dimension(textingWidth, textingHeight - buttonHeight));
		//adding
		c.gridx = 0; c.gridy = 0;
		leftMessagingPanel.add(leaveChatroomButton, c);
		c.gridx = 0; c.gridy = 1;
		leftMessagingPanel.add(scrollMessages, c);
		resetGridConstraints(c);
		
		//RIGHT
		//chat-room info
		//panel
		int participantsWidth = 150; int participantsHeight = 300;
		JPanel rightParticipantsPanel = new JPanel(new GridBagLayout());
		rightParticipantsPanel.setPreferredSize(new Dimension(participantsWidth, participantsHeight));
		//title
		int titleHeight = 20;
		JLabel memberListTitle2 = new JLabel(MEMBERS);
		memberListTitle2.setFont(memberListTitle2.getFont().deriveFont(15.0f));
		memberListTitle2.setHorizontalAlignment(JLabel.LEFT);
		memberListTitle2.setPreferredSize(new Dimension(participantsWidth, titleHeight));
		//JList
		participantsListModel_M = new DefaultListModel<>();
		JList<String> participantsList2 = new JList(participantsListModel_M);
		JScrollPane participantsScrollPane2 = new JScrollPane(participantsList2);
		participantsScrollPane2.setPreferredSize(new Dimension(participantsWidth, participantsHeight - titleHeight));
		//adding
		c.gridx = 0; c.gridy = 0;
		rightParticipantsPanel.add(memberListTitle2, c);
		c.gridx = 0; c.gridy = 1;
		rightParticipantsPanel.add(participantsScrollPane2, c);
		resetGridConstraints(c);
		
	
		//BOTTOm
		int controllerWidth = 400; int controllerHeight = 50;
		//page-controller
		JPanel messagingPageController = new JPanel();
		messagingPageController.setPreferredSize(new Dimension(controllerWidth, controllerHeight));
		//send button
		JButton sendMessageButton = new JButton(SEND);
		sendMessageButton.addActionListener(new SendButtonListener());
		//type input
		messageInput = new JTextField(20);
		//adding
		messagingPageController.add(messageInput);
		messagingPageController.add(sendMessageButton);
		resetGridConstraints(c);
		
		//final adding
		c.gridx=0;c.gridy=0;c.gridwidth=2;c.gridheight=1;
		messagePage.add(messagingPageTitle,c);
		c.gridx=0;c.gridy=1;c.gridwidth=1;c.gridheight=1;
		c.ipadx = 25;
		messagePage.add(leftMessagingPanel, c);
		c.gridx=1;c.gridy=1;c.gridwidth=1;c.gridheight=1; 
		messagePage.add(rightParticipantsPanel, c);
		c.gridx=0;c.gridy=2;c.gridwidth=2;c.gridheight=1;
		messagePage.add(messagingPageController,c);
		//insert into card layout
		cards.add(messagePage,Cards.MESSAGING_CARD.toString() );	
	}
	
	private void initialiseServerList() {
		//cleanup
		resetGridConstraints(c);
		
		//loading widgets
		//page
		JPanel serverListPage = new JPanel(new GridBagLayout());
		
		//TOP
		//page title
		JLabel serverListPageTitle = new JLabel(SERVERPAGE_TITLE);
		serverListPageTitle.setFont(serverListPageTitle.getFont().deriveFont(40.0f));
		serverListPageTitle.setHorizontalAlignment(JLabel.CENTER);
		//width 2, height 1
		
		
		//LEFT
		//list of chat-rooms
		int leftSideWidth = 200;
		int leftSideHeight = 400;
		JPanel leftPanelForChatrooms = new JPanel(new GridBagLayout());
		//title
		JLabel serverListTitle = new JLabel(SERVERLIST_TITLE);
		serverListTitle.setFont(serverListTitle.getFont().deriveFont(20.0f));
		serverListTitle.setHorizontalAlignment(JLabel.LEFT);
		serverListTitle.setPreferredSize(new Dimension(leftSideWidth,50));
		//list
		JScrollPane chatroomScrollPane = new JScrollPane(chatroomList);
		chatroomList.addListSelectionListener(new ServerSelectListener());
		chatroomScrollPane.setPreferredSize(new Dimension(leftSideWidth,300));
		//controller
		JPanel serverPageController = new JPanel();
		serverPageController.setPreferredSize(new Dimension(leftSideWidth, 50));
		//join button
		JButton join = new JButton(JOIN);
		join.addActionListener(new JoinButtonListener());
		//adding
		serverPageController.add(join);
		//adding
		c.gridx=0; c.gridy=0;
		leftPanelForChatrooms.add(serverListTitle, c);
		c.gridx=0; c.gridy=1;
		leftPanelForChatrooms.add(chatroomScrollPane, c);
		c.gridx=0; c.gridy=2;
		leftPanelForChatrooms.add(serverPageController, c);
		leftPanelForChatrooms.setPreferredSize(new Dimension(leftSideWidth,leftSideHeight));
		//width 1, height 1
		resetGridConstraints(c);
		
		//RIGHT
		//list of participants in chat-room
		int rightSideWidth = 200;
		int rightSideHeight = 400;
		JPanel rightPanelForParticipants = new JPanel(new GridBagLayout());
		//title
		JLabel memberListTitle = new JLabel(MEMBERS);
		memberListTitle.setFont(memberListTitle.getFont().deriveFont(20.0f));
		memberListTitle.setHorizontalAlignment(JLabel.LEFT);
		memberListTitle.setPreferredSize(new Dimension(rightSideWidth,50));
		//Jlist
		JList<String> participantsList = new JList(participantsListModel_SL);
		JScrollPane participantsScrollPane = new JScrollPane(participantsList);
		participantsScrollPane.setPreferredSize(new Dimension(rightSideWidth, 300));
		
		//adding
		c.gridx = 0; c.gridy = 0;
		rightPanelForParticipants.add(memberListTitle, c);
		c.gridx = 0; c.gridy = 1; c.insets = new Insets(0, 0, 50, 0);
		rightPanelForParticipants.add(participantsScrollPane,c);
		rightPanelForParticipants.setPreferredSize(new Dimension(rightSideWidth,rightSideHeight));
		//width 1, height 1
		resetGridConstraints(c);
		
		//final adding
		c.gridx = 0; c.gridy=0; c.gridheight = 1; c.gridwidth = 2;
		serverListPage.add(serverListPageTitle, c);
		c.gridx = 0; c.gridy = 1; c.gridheight = 1; c.gridwidth = 1; c.ipadx = 25;
		serverListPage.add(leftPanelForChatrooms, c);
		c.gridx = 1; c.gridy = 1; c.gridheight = 1; c.gridwidth = 1; c.ipadx = 25;
		serverListPage.add(rightPanelForParticipants, c);
		//insert into card layout
		cards.add(serverListPage, Cards.SERVERLIST_CARD.toString());
	}
	
	private void initialiseLogIn() {
		//cleanup
		resetGridConstraints(c);
		
		//loading widgets
		//page
		JPanel logInPanel = new JPanel();
		logInPanel.setLayout(new GridBagLayout());
		logInPanel.setMaximumSize(new Dimension(400,400));
		logInPanel.setPreferredSize(new Dimension(400,400));
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
		cards.add(logInPanel, Cards.LOGIN_CARD.toString());
		
	}
	
	static void serverUnavailable() {
		JOptionPane.showMessageDialog(frame, "Server is currently down");
	}
	
	static void updateServerList() {
		//SUPER INEFFECTIVE and INEFFICIENT and shit 
		chatroomListModel.clear();
		for (Chatroom room : ClientNetworkManager.chatroomList) {
			String elementValue = "chatroomID: " + room.getChatroomID() + " ---- members: " + room.getParticipants().size();
			chatroomListModel.addElement(elementValue);
		}
		
  	  	chatroomList.setSelectedIndex(selectedServer);	
	}
	
	private void displayParticipants(int selectedID) {
		participantsListModel_SL.clear();
		participantsListModel_M.clear();
		
		for (Client client : ClientNetworkManager.chatroomList.get(selectedID).getParticipants()){
			participantsListModel_SL.addElement(client.getUsername());
			participantsListModel_M.addElement(client.getUsername());
		}
	}
	
	static void successfulLogin() {
		authorisationResult.setText(AUTHORISED);
		submit.setEnabled(false);
		cardLayout.show(cards,Cards.SERVERLIST_CARD.toString());
	}
	
	static void unsuccessfulLogin() {
		authorisationResult.setText(UNAUTHORISED);
		passwordField.setText("");
	}
	
	static void blockedLogin() {
		authorisationResult.setText(BLOCKED_LOGIN);
		submit.setEnabled(false);
	}
	
	static void joinChatroom() {
		cardLayout.show(cards, Cards.MESSAGING_CARD.toString());
		messagingPageTitle.setText(currentChatroom);
	}
	
	static void exitChatroom() {
		cardLayout.show(cards, Cards.SERVERLIST_CARD.toString());
	}
	
	static void updateTextArea(Message incomingMessage, Color colour) {
		String message = incomingMessage.getSender() + " : " + 
				         incomingMessage.getMessageBody();
		
		StyledDocument doc = leftTextPanel.getStyledDocument();

        Style mainStyle = leftTextPanel.addStyle("Main style", null);
        StyleConstants.setForeground(mainStyle, colour);
        
        Style timeStyle = leftTextPanel.addStyle("Timestamp Style", null);
        StyleConstants.setForeground(timeStyle, Color.LIGHT_GRAY);
        
        try {
            doc.insertString(doc.getLength(), message, mainStyle);
            doc.insertString(doc.getLength(), "  " + incomingMessage.getSimpleFormatedTimestamp() + "\n", timeStyle);
        } 
        catch (BadLocationException e) {
            e.printStackTrace();
        }  
	}	
	
	class LoginButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String username = usernameField.getText();
			String password = new String(passwordField.getPassword());
			networkManager.setUsername(username);
			try {
				Message logInRequest = new Message(username+"?"+password, username, command.LOGIN_REQUEST);
				networkManager.sendData(logInRequest);
			}
			catch(Exception e) {
				blockedLogin();
			}		
		}
	}
	
	class JoinButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			
			if (chatroomList.isSelectionEmpty()) {
				currentChatroom = UNKNOWN_ROOM;
				return;
			}
			
			currentChatroom = chatroomList.getSelectedValue().substring(0,13);
			
			networkManager.setConnectedChatroomID(Character.getNumericValue(chatroomList.getSelectedValue().charAt(12)));
			Message joinChatroomRequest = new Message(Integer.toString(networkManager.getConnectedChatroomID()), networkManager.getUsername(), command.JOIN_CHATROOM_REQUEST);
			networkManager.sendData(joinChatroomRequest);
		}
	}
	
	class LeaveButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			Message leaveChatroomRequest = new Message(Integer.toString(networkManager.getConnectedChatroomID()), networkManager.getUsername(), command.EXIT_CHATROOM_REQUEST);
			networkManager.sendData(leaveChatroomRequest);
			leftTextPanel.setText("");
		}
	}
	
	class SendButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String input = messageInput.getText();
			if (!input.isBlank()){
				Message messageData = new Message(input, networkManager.getUsername());
				networkManager.sendData(messageData);
				messageInput.setText("");
			}
		}
	}
	
	class ServerSelectListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent event) {
			if (!event.getValueIsAdjusting()) {
				if (chatroomList.getSelectedIndex()!= -1) {
					displayParticipants(Character.getNumericValue(chatroomList.getSelectedValue().charAt(12)));
					selectedServer = chatroomList.getSelectedIndex();
				}
				else {
					chatroomList.setSelectedIndex(selectedServer);
				}

			}
		}
	}
	
}
