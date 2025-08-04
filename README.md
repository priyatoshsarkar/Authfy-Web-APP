# Authfy-Web-APP
"A web application for user authentication."  "Authfy-Web-APP is a user authentication system built for the web."  
"A simple and secure web app for handling user sign-up, login, and password management."

Authfy is a secure and scalable web application that implements **JWT-based authentication** using **Spring Boot**, designed to manage user login, registration, and profile management.

---

## 🚀 Features

- 🔐 JWT (JSON Web Token) authentication
- 👤 User registration and login
- 🍪 HTTP-only Cookie-based token storage
- 📦 RESTful APIs (Spring MVC)
- 🛡️ Spring Security integration
- 📚 Context-based role access with `@CurrentSecurityContext`
- 💾 MySQL database integration (Spring Data JPA)

---

## 🧰 Tech Stack

| Layer        | Tech Used             |
|--------------|------------------------|
| **Frontend** | HTML, CSS (Optional: JS or Thymeleaf if used) |
| **Backend**  | Java, Spring Boot, Spring Security |
| **Database** | MySQL |
| **Tools**    | Maven, Git, GitHub, Postman |

---
Update your application.properties with DB credentials:

properties
Copy
Edit
spring.datasource.url=jdbc:mysql://localhost:3306/authfy_db
spring.datasource.username=root
spring.datasource.password=your_password
🧪 Testing
Use Postman or frontend to:

Register a user

Log in and store the JWT cookie

Access protected endpoints with the token

📌 Future Enhancements
Refresh token support

Role-based access control (RBAC)

Frontend login UI

👨‍💻 Author
Priyatosh Sarkar
GitHub

📄 License
This project is open-source under the MIT License.
### ✅ How to Add This to GitHub:

1. Save it as a file in your project root: `README.md`

2. Run this in Git Bash or PowerShell:

```bash
git add README.md
git commit -m "Add README for Authfy Web App"
git push
