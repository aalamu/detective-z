### Basic Configuration

1. Request for environment and secret details to be able to run the application
2. Get the script files script.sh for Linux/MacOs or script.bat for Windows
3. Copy the script file to the root of your project as shown below
<img width="485" alt="Screenshot 2025-01-06 at 12 54 09" src="https://github.com/user-attachments/assets/d98040d4-25b2-4f0c-b34c-d21dcc22d089" />




### How to Run
1. Navigate to the ```detective-z``` project folder using the ```cd``` command in your terminal or command prompt 
2. Run ```./mvnw clean``` to clean the package
3. Run ```./mvnw install``` to install the dependencies
4. Run the script file in the terminal or command prompt to set the environment keys and values for the session

**Example**:

For Windows
```script.bat```

For MacOS
```source ./script.sh```

5. Run the app using ```mvn spring-boot:run```
6. Open your browser and open localhost:8080
