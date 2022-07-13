# CHAT-LOG
Log service for chats.

# APIs
### 1.) POST: /chatlogs/{user}
Creates a new chatlog entry for the user.<br/>
Takes: 
 * message - a String representing the message text.
 * isSent - a Boolean representing if this message was sent by the user.
 * createdAt - a String representing the timestamp.
 
Response: The response is a unique messageID.

![Post api](https://user-images.githubusercontent.com/55765572/178684219-3d9f02f6-cff0-4381-973d-df3eca0917a4.png)

### 2.) GET: /chatlogs/{user}
Gives a list 'limit' (no of) of user chat log starting 'from' (messageId) sorted in reverse timeorder (most recent messages first).<br/>
Takes two optional parameters:
 * from: a key of the same type as messageID to determine where to start from.
 * limit: an Integer stating how many messages should return. Default to 10.
 
Response: A paginated response containing a list of user chat logs, messageId from and to, count (total logs asked) and size (total logs recieved/present).

![GET api](https://user-images.githubusercontent.com/55765572/178686520-104bd395-12d9-433c-bcdd-7c5a27777a98.png)

### 3.) DELETE: /chatlogs/{user}
Deletes all the chat logs for a given user.<br/>
Response: A success message if all chats for given user deleted successfully OR a error message if no chat found for given user.

![DELETE all api](https://user-images.githubusercontent.com/55765572/178688765-0ed7ac1d-363f-49ed-a61e-273157a4f5ff.png)

### 4.) DELETE: /chatlogs/{user}/{messageId}
Deletes the given chatlog for a given user.<br/>
Response: A success message if deleted successfully OR a error message if chat log not found for given user with given messageId.

![DELETE api](https://user-images.githubusercontent.com/55765572/178687600-bb301f98-8af7-4e56-ae7d-a8a313cf47a0.png)
