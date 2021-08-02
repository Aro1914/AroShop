# Note
This project is dependent on the JDK 1.8 platform

## Resolving '"sqlite-jdbc-3.32.3.2.jar" file/folder could not be found' in NetBeans
If you have downloaded this project and wish to run it on your NetBeans IDE, you might get a dialogue box telling you that there are some unresolved issues in the project the moment you open it on the IDE. How can you resolve this?
1. Click on the Resolve Problems button, then you should see a box like this
![Screenshot (429)](https://user-images.githubusercontent.com/68448315/127789108-8a054ac1-2e9d-40d6-b2e7-0728cf71133a.jpeg)

2. Next Click on the Resolve button
3. Locate the Database folder to the get the JDBC for SQLite. Its stored in a subfolder of the src/ directory called Database.
4. Double click on the .jar file for the SQLite JDBC and you should be taken back to the dialogue box above, but this time the yellow triangle icon on the left of the message—"sqlite-jdbc-3.32.3.2.jar" file/folder could not be found—should be a green circle.
5. Click on the close button, and run your application.
