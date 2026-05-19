# RetroChatApplication 💬
![Login Showcase](https://raw.githubusercontent.com/bobblet11/RetroChatApplication/master/resources/showcaseImages/LoginPage.png)

[![Made with Java](https://img.shields.io/badge/Made%20with-Java-orange.svg)](https://www.oracle.com/java/)
[![Sockets](https://img.shields.io/badge/Uses-Sockets-blue.svg)]()
[![Multithreading](https://img.shields.io/badge/Supports-Multithreading-purple.svg)]()
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Platform: Cross‑Network](https://img.shields.io/badge/Platform-Cross%20Network-lightgrey.svg)]()

A **retro‑style chat application written in Java** that uses **sockets** and **multithreading** to host asynchronous communication between clients across different networks.  
The GUI is built using **Java Swing** and **AWT**.

---

## 📦 Installation & Quickstart

### Client
- Configure the **IP** and **PORT** in `Client/NetworkManager.java` (default: localhost).
1. Download the repository.  
2. Run either `client.jar` or `Client/Main.java`.  
3. Log in with:  
   - Username: `admin`  
   - Password: `admin`  
4. Browse chatrooms and view participants.  
5. Join a chatroom.  
6. Start chatting! 🎉  

### Server
1. Download the repository.  
2. Run `Server/Main.java` in the terminal.  
3. Choose a port in the terminal.  
4. Clients can now connect using the server’s **IP** and **PORT**.  

---

## 🖼️ Showcase

### 🔑 Logging In
![Login](https://raw.githubusercontent.com/bobblet11/RetroChatApplication/master/resources/showcaseImages/LoginPage1.png)
![Login](https://raw.githubusercontent.com/bobblet11/RetroChatApplication/master/resources/showcaseImages/LoginPage2.png)

### 📂 Browse Chatrooms
![Chatroom Selection](https://raw.githubusercontent.com/bobblet11/RetroChatApplication/master/resources/showcaseImages/chatroomSelection.png)

### 💬 Chat
![Messenger](https://raw.githubusercontent.com/bobblet11/RetroChatApplication/master/resources/showcaseImages/chatroomMessenger.png)
![Messenger](https://raw.githubusercontent.com/bobblet11/RetroChatApplication/master/resources/showcaseImages/chatroomMessenger1.png)

---

## 🛠️ Features
- **Socket‑based networking** for client/server communication.  
- **Multithreaded server** to handle multiple clients asynchronously.  
- **Swing/AWT GUI** for a retro desktop feel.  
- **Chatroom browsing** with participant lists.  
- **Authentication system** (default admin credentials).  

---

## 🚀 Future Improvements
- Persistent user accounts and chat history.  
- Encrypted communication channels.  
- Customizable themes for the GUI.  
- Support for file sharing between clients.  
