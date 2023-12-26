package clientside;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientGUI {
	
	public static void main(String args[])
	{
		ClientNetworkManager networkManager = new ClientNetworkManager();
		ClientGUI gui = new ClientGUI(networkManager);
	}
	
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
	
	//serverListPgae
	private JPanel serverListPage;
	private final static String SERVERLIST_CARD = "SERVERLIST_CARD";
	private final String SERVERPAGE_TITLE = "Chatrooms";
	private final String SERVERLIST_TITLE = "Servers";
	private final String JOIN = "Join";
	private final String MEMBERS = "Members";
	private JLabel serverListPageTitle;
	private JLabel serverListTitle;
	private JLabel memberListTitle1;
	private JButton join;
	private JPanel leftHalf;
	private JPanel rightHalf;
	private JPanel controllerPanel1;
	
	//messagingPage
	private JPanel messagePage;
	private final static String MESSAGING_CARD = "MESSAGING_CARD";
	private final String MESSAGING_PAGE_TITLE = "TITLE";
	private final String SEND = "Send";
	private JLabel messagingPageTitle;
	private JPanel controllerPanel2;
	private JLabel memberListTitle2;
	private JTextArea leftTextPanel;
	private JScrollPane scrollMessages;
	private JScrollPane scrollParticipants;
	private JPanel rightParticipantsPanel;
	private JPanel rightParticipantsList;
	private JButton sendMessageButton;
	private JTextField messageInput;
	
	ClientNetworkManager networkManager;
	public ClientGUI(ClientNetworkManager networkManager)
	{
		this.networkManager = networkManager;
		networkManager.connect();
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
		c = new GridBagConstraints();
		frame = new JFrame("Retro Chatroom");
		frame.setSize(windowWidth, windowHeight);
		
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
		resetGridConstraints(c);
		messagePage = new JPanel(new BorderLayout());
		
		messagingPageTitle = new JLabel(MESSAGING_PAGE_TITLE);
		messagingPageTitle.setFont(messagingPageTitle.getFont().deriveFont(40.0f));
		messagingPageTitle.setHorizontalAlignment(JLabel.CENTER);
		
		leftTextPanel = new JTextArea(100,10);
		leftTextPanel.setEditable(false);
		scrollMessages = new JScrollPane(leftTextPanel);
		scrollMessages.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollMessages.setPreferredSize(new Dimension(380, 450));
		
		memberListTitle2 = new JLabel(MEMBERS);
		memberListTitle2.setFont(memberListTitle2.getFont().deriveFont(12.0f));
		memberListTitle2.setHorizontalAlignment(JLabel.LEFT);
		rightParticipantsPanel = new JPanel(new BorderLayout());
		rightParticipantsList = new JPanel();
		rightParticipantsList.setPreferredSize(new Dimension(20, 350));
		scrollParticipants = new JScrollPane(rightParticipantsList);
		scrollParticipants.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollParticipants.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		rightParticipantsPanel.add(memberListTitle2, BorderLayout.NORTH);
		rightParticipantsPanel.add(scrollParticipants, BorderLayout.SOUTH);
		rightParticipantsPanel.setPreferredSize(new Dimension(80, 450));
		
		controllerPanel2 = new JPanel();
		sendMessageButton = new JButton(SEND);
		messageInput = new JTextField(20);
		controllerPanel2.add(messageInput);
		controllerPanel2.add(sendMessageButton);
		
		messagePage.add(messagingPageTitle,BorderLayout.NORTH);
		messagePage.add(scrollMessages, BorderLayout.WEST);
		messagePage.add(rightParticipantsPanel, BorderLayout.EAST);
		messagePage.add(controllerPanel2,BorderLayout.SOUTH);
		
		cards.add(messagePage,MESSAGING_CARD );
		
	}
	
	private void initialiseServerList()
	{
		resetGridConstraints(c);
		
		serverListPage = new JPanel(new BorderLayout());
		
		serverListPageTitle = new JLabel(SERVERPAGE_TITLE);
		serverListTitle = new JLabel(SERVERLIST_TITLE);
		memberListTitle1 = new JLabel(MEMBERS);
		
		join = new JButton(JOIN);
		leftHalf = new JPanel(new BorderLayout());
		rightHalf = new JPanel(new BorderLayout());
		controllerPanel1 = new JPanel();
		

		serverListPageTitle.setFont(serverListPageTitle.getFont().deriveFont(40.0f));
		serverListPageTitle.setHorizontalAlignment(JLabel.CENTER);
		serverListPage.add(serverListPageTitle, BorderLayout.NORTH);
		
		serverListPage.add(leftHalf, BorderLayout.WEST);
		leftHalf.setPreferredSize(new Dimension(280, 450));
		
		leftHalf.add(controllerPanel1, BorderLayout.SOUTH);
		serverListTitle.setFont(serverListTitle.getFont().deriveFont(20.0f));
		serverListTitle.setHorizontalAlignment(JLabel.LEFT);
		leftHalf.add(serverListTitle, BorderLayout.NORTH);
		controllerPanel1.add(join);
		

		rightHalf.setPreferredSize(new Dimension(180, 450));
		rightHalf.setBackground(Color.black);
		serverListPage.add(rightHalf,BorderLayout.EAST);
		
		memberListTitle1.setFont(memberListTitle1.getFont().deriveFont(20.0f));
		memberListTitle1.setHorizontalAlignment(JLabel.LEFT);
		rightHalf.add(memberListTitle1, BorderLayout.NORTH);
		
		cards.add(serverListPage, SERVERLIST_CARD);
	}
	
	private void initialiseLogIn()
	{
		resetGridConstraints(c);
		
		logInPage = new JPanel();
		logInPanel = new JPanel();
		logInPage.setLayout(new BorderLayout());
		logInPanel.setLayout(new GridBagLayout());
		
		logInTitle = new JLabel(LOGIN_TITLE);
		usernameLabel = new JLabel(USERNAME);
		usernameField = new JTextField(20); 
		passwordLabel = new JLabel(PASSWORD);
		passwordField = new JPasswordField(20);
		submit = new JButton(SUBMIT);
		submit.addActionListener(new LoginButtonListener());
		authorisationResult = new JLabel();
		
		c.gridwidth = 2;
		c.gridx = 1;
		c.gridy = 0;
		c.ipady = 50;
		logInTitle.setFont(logInTitle.getFont().deriveFont(20.0f));
		logInTitle.setHorizontalAlignment(JLabel.CENTER);
		logInPanel.add(logInTitle,c);
		resetGridConstraints(c);
		
		c.gridx = 1;
		c.gridy = 1;
		c.ipadx = 5;
		logInPanel.add(usernameLabel,c);
		
		c.gridx = 2;
		c.gridy = 1;
		c.ipadx = 5;
		logInPanel.add(usernameField,c);
		
		c.gridx = 1;
		c.gridy = 2;
		c.ipadx = 5;
		c.insets = new Insets(10,0,10,0);
		logInPanel.add(passwordLabel,c);
		
		c.gridx = 2;
		c.gridy = 2;
		c.ipadx = 5;
		logInPanel.add(passwordField,c);
		resetGridConstraints(c);
		
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 2;
		c.ipady = 5;
		logInPanel.add(authorisationResult, c);
		
		c.gridx = 1;
		c.gridy = 4;
		c.gridwidth = 2;
		c.ipadx = 10;
		c.ipady = 10;
		logInPanel.add(submit, c);
		
		logInPage.add(logInPanel, BorderLayout.CENTER);
		cards.add(logInPage, LOGIN_CARD);
		
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
				result = networkManager.LogIn(username, password);
				if (result)
				{
					authorisationResult.setText(AUTHORISED);
					submit.setEnabled(false);
					c1.show(cards,SERVERLIST_CARD);
					//request server lists
					networkManager.requestChatroomList();
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
}