
# 🕵️‍♂️ Detective Z

**Detective Z** is an intelligent web application that empowers users to investigate suspicious websites, links, or products to identify potential **scams**, **fraud**, **phishing**, or other malicious activities.

It combines **real-time web crawling**, **heuristic security scoring**, and **AI-powered analysis** to deliver a comprehensive risk assessment—helping users stay safe online.

---

## 🚀 Key Features

- ✅ **Link Risk Scoring**  
  Uses structural signals (e.g., presence of contact info, privacy policy, HTTPS usage) to generate a security risk score.

- 🌐 **Live Web Crawling**  
  Automatically visits and extracts content from the provided URL, even when traditional metadata is lacking.

- 🤖 **AI-Powered Content Analysis**  
  Leverages generative AI to evaluate the extracted content, red flags, and tone of the page to assess if it's potentially deceptive.

- 🔍 **Search-Enhanced Contextual Intelligence**  
  Performs parallel real-time searches using platforms like **Google**, **Reddit**, **X (formerly Twitter)**, **Meta**, and **YouTube** to find social signals, user reports, and discussions that might indicate malicious behavior.

- 📊 **Human-Readable Report**  
  Returns a Markdown-formatted summary explaining:
    - Detected risks and red flags
    - Confidence level
    - Advice on what to look out for

---

## 🧠 How It Works

1. **Input Query or Link**  
   The user provides a product link or a keyword (e.g., `"XYZ Crypto Investment"`).

2. **Website Crawling & Feature Extraction**  
   The system fetches the webpage, checks for key indicators like:
    - Contact information
    - Social media links
    - HTTPS usage
    - Presence of privacy policy
    - Use of sensitive forms

3. **Heuristic Scoring**  
   A score is calculated based on presence/absence of trustworthy site elements.

4. **Contextual Search**  
   Searches across multiple sources for user complaints or scam reports.

5. **AI Analysis**  
   Combines the website content, search results, and security score to generate a clear risk assessment using large language models.

---

## 🧱 Built With

- Java (Spring Boot)
- Project Reactor (`Mono`, `Flux`)
- Jsoup for HTML parsing
- OpenAI API for AI analysis
- Google Search API
- Modular architecture with adapters for external services

---

## 🛡 Use Cases

- Verify a **shopping site** before making a purchase
- Investigate a **crypto project or airdrop**
- Analyze **phishing links** received via email or DM
- Research potentially **malicious services or products**

---

## 🔒 Disclaimer

Detective Z provides **advisory insights** based on heuristics, public data, and AI models. While it is a powerful tool to assist in identifying suspicious activity, it should not be used as a sole legal or cybersecurity authority.










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
