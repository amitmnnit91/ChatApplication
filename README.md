# STOMP based chat application

This is a chat app built using web-socket which is a lightweight layer build over TCP.
In this project, we use STOMP messaging with Spring to create an interactive web application.
Currently we are supporting two modes of chats - 
 - One to One chat
 - Group chat (currently only one group support exists)
 
Along with this other supported features are - 
 - Marking messages as READ whenever other members view it
 - Saving messages till user is offline and then re-publishing when he/she gets online
 
Require 
 - JDK 1.8 or above
 - Maven 3.2+
 
Run config - 
 - Simply run the project using command- 
 	mvn spring-boot:run
 - Open http://localhost:8080/ in browser 
 	It will prompt for user name please provide that
  	Friend's name is required when one to one chat is preferred else it can be ignored
   	Most validations have been ignored for now, so request all testers to be little lenient
