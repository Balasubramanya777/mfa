## 🔐 Multi Factor Authentication

## 📖 Overview

This project demonstrates a secure and standard implementation of Multi-Factor Authentication (MFA) using Spring Boot. It integrates Time-based One-Time Passwords (TOTP) with session-based authentication to provide an additional layer of security beyond username and password.

---

## 🧭 Application Flow

The application enforces two-step authentication:

1. Username and Password verification  
2. One-Time Password (OTP) verification using an authenticator app  

Only users who successfully complete both steps are granted access.

---

## 🛠️ Tech Stack

- Backend: Spring Boot  
- Frontend: Thymeleaf  
- Database: PostgreSQL  
- TOTP Library: `dev.samstevens.totp`  
- Authentication: Spring Security (MFA support)  
- Session Management: HTTP Session  

---

## ⚙️ How It Works

### User Registration

- A new user registers by providing the required details  
- After successful registration, a QR code is generated  
- The user scans the QR code using any authenticator app (e.g., Google Authenticator)  
- A new entry is added in the authenticator app for generating OTPs  

---

### Login Flow

#### Step 1: Username and Password

- The user enters username and password  
- If credentials are invalid, access is denied  

#### Step 2: OTP Verification

- If credentials are valid, the user is redirected to an OTP verification page  
- The user must enter the code from the authenticator app  

---

## 🚫 Access Rules

- Invalid username or password → Access denied  
- Valid credentials but missing or incorrect OTP → Access denied  
- Access is granted only when both steps are successfully completed  

---

## 🧩 Multi-Factor Authentication Configuration

This project uses Spring Security’s MFA support instead of custom logic.

```java
@EnableMultiFactorAuthentication(
    authorities = {
        FactorGrantedAuthority.PASSWORD_AUTHORITY,
        FactorGrantedAuthority.OTT_AUTHORITY
    }
)
```

### Explanation

- `PASSWORD_AUTHORITY` ensures successful username/password authentication  
- `OTT_AUTHORITY` ensures OTP (One-Time Token) verification is completed  

Both are required for full authentication.

---

## 🤔 Why This Approach

In a previous implementation, MFA was handled using custom filters and boolean flags, which was not a standard approach and was harder to maintain.

This project improves on that by:

- Using built-in Spring Security MFA features  
- Following a cleaner and more maintainable design  
- Providing better security and extensibility  

---

## ✨ Features

- Two-step authentication (Password + OTP)  
- QR code-based TOTP setup  
- Compatible with standard authenticator apps  
- Session-based authentication  
- Clean and maintainable Spring Security integration  

---

## 🏁 Conclusion

This project showcases a modern and secure way to implement Multi-Factor Authentication using Spring Boot. By leveraging Spring Security and TOTP, it provides a scalable and reliable authentication system.
