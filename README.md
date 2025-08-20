# 🔐 Keylo Password Manager

A secure, user-friendly password manager designed specifically for **elderly users** to help them stay on top of their digital security with **strong password standards**. Built with **Spring Boot** and **(vite) React TypeScript**, featuring encrypted credential storage, JWT authentication, and an intuitive interface that makes password management accessible for all ages.


## 🌟 Features

### **Designed for Elderly Users**
- **Simple Interface**: Clean, large buttons and clear text for easy navigation
- **Password Security Education**: Helps seniors understand and adopt strong password practices


### 🔒 **Security First**
- **Strong Password Standards**: Encourages and supports creation of robust, unique passwords
- **AES Encryption**: All passwords encrypted before database storage
- **JWT Authentication**: Secure token-based authentication with auto-refresh
- **BCrypt Password Hashing**: User passwords securely hashed

### 💼 **Credential Management**
- **CRUD Operations**: Create, read, update, and delete credentials
- **Organized Storage**: Store website credentials with metadata
- **Password Visibility Toggle**: Secure password viewing when needed

### 🎨 **Accessible UI/UX**
- **Senior-Friendly Design**: Large fonts, high contrast, intuitive layout
- **Responsive Design**: Works on desktop, tablet, and mobile devices
- **Clear Navigation**: Simple, logical flow that's easy to understand
- **Real-time Feedback**: Helpful messages and loading states

## 🏗️ Architecture

```
Keylo Password Manager (For Elderly Users)
├── Backend (Spring Boot)
│   ├── REST API endpoints
│   ├── JWT security layer
│   ├── Strong password validation
│   ├── AES encryption service
│   └── MySQL database
└── Frontend (React TypeScript)
    ├── Senior-friendly interface
    ├── Large, clear components
    ├── Password strength guidance
    ├── Simple navigation
    └── Accessible design patterns
```

## 🚀 Quick Start

### Prerequisites

- **Java 17+** and Maven
- **Node.js 16+** and npm
- **MySQL 8.0+**
- **Git**

### 1. Clone the Repository

```bash
git clone git@github.com:BlairCrumbly/Keylo.git
cd keylo
```

### 2. Backend Setup

```bash
# Navigate to backend directory
cd backend


# Configure application properties
cp src/main/resources/application.properties.example src/main/resources/application.properties
# Edit with your database credentials

# Create .env file for encryption keys
cp .env.example .env
# Add your SECRET_KEY and JWT_SECRET (base64 encoded)

# Install dependencies and run
mvn clean install
mvn spring-boot:run
```

**Backend runs on:** `http://localhost:8080`

### 3. Frontend Setup

```bash
# Navigate to frontend directory (new terminal)
cd frontend

# Install dependencies
npm install

# Configure environment
cp .env.example .env
# Set REACT_APP_API_BASE_URL=http://localhost:8080

# Start development server
npm start
```

**Frontend runs on:** `http://localhost:5173`

## Project Structure

### Backend Structure
```
src/main/java/com/keylo/
├── controller/
│   ├── UserController.java          # Authentication endpoints
│   └── CredentialController.java    # Credential CRUD endpoints
├── service/
│   ├── UserService.java            # User business logic
│   └── CredentialService.java      # Credential business logic
├── security/
│   ├── JwtUtils.java               # JWT token management
│   ├── JwtAuthFilter.java          # JWT authentication filter
│   ├── EncryptionUtil.java         # AES encryption service
│   ├── SecurityConfig.java         # Spring Security configuration
│   └── CustomUserDetailsService.java
├── model/
│   ├── User.java                   # User entity
│   └── Credential.java             # Credential entity
├── repository/
│   ├── UserRepository.java         # User data access
│   └── CredentialRepository.java   # Credential data access
├── dto/
│   ├── LoginRequest.java
│   ├── JwtResponse.java
│   ├── CredentialRequest.java
│   └── CredentialResponse.java
└── KeyloApplication.java           # Main application class
```

### Frontend Structure
```
src/
├── components/
│   ├── auth/                       # Authentication components
│   ├── common/                     # Reusable UI components
│   ├── credentials/                # Credential management
│   └── layout/                     # Layout components
├── contexts/
│   └── AuthContext.tsx             # Authentication context
├── hooks/
│   └── useCredentials.ts           # Credential data hook
├── pages/
│   ├── AuthPage.tsx                # Login/Register page
│   └── DashboardPage.tsx           # Main dashboard
├── services/
│   ├── authService.ts              # Authentication API calls
│   └── credentialService.ts        # Credential API calls
├── types/
│   └── index.ts                    # TypeScript interfaces
├── utils/
│   ├── constants.ts                # App constants
│   └── validation.ts               # Form validation
├── config/
│   └── api.ts                      # Axios configuration
└── App.tsx                         # Main app component
```

## 🔧 Configuration

### Backend Configuration

**application.properties**
```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/keylo_db
spring.datasource.username=your_username
spring.datasource.password=your_password

# JPA
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Server
server.port=8080
```

**.env (Backend)**
```env
# AES Encryption Key (base64 encoded, 32 bytes)
SECRET_KEY=your_base64_encoded_32_byte_key

# JWT Secret (base64 encoded, 32+ bytes)
JWT_SECRET=your_base64_encoded_jwt_secret
```

### Frontend Configuration

**.env (Frontend)**
```env
REACT_APP_API_BASE_URL=http://localhost:8080
```

## 🔐 Security Implementation

### Password Standards for Elderly Users
- **Strong Password Guidance**: Built-in tips and requirements for creating secure passwords
- **Password Complexity**: Encourages use of uppercase, lowercase, numbers, and special characters

### Encryption & Protection
- User passwords: **BCrypt** hashing with salt
- Stored credentials: **AES-256** encryption
- Encryption keys: Environment variables (never hardcoded)

### JWT Authentication
- **Access tokens**: 24-hour expiration
- **Auto-refresh**: Seamless token renewal
- **Secure storage**: LocalStorage with cleanup on logout

### API Security
- **Protected endpoints**: JWT required for credential access
- **User isolation**: Users can only access their own data
- **Input validation**: Server-side validation for all inputs

## 📊 Database Schema

### Users Table
| Column   | Type         | Constraints      |
|----------|--------------|------------------|
| id       | BIGINT       | PRIMARY KEY, AUTO_INCREMENT |
| email    | VARCHAR(255) | UNIQUE, NOT NULL |
| password | VARCHAR(255) | NOT NULL (BCrypt hashed) |

### Credentials Table
| Column         | Type         | Constraints      |
|----------------|--------------|------------------|
| credId         | BIGINT       | PRIMARY KEY, AUTO_INCREMENT |
| userId         | BIGINT       | FOREIGN KEY, NOT NULL |
| siteName       | VARCHAR(255) | NOT NULL         |
| siteUrl        | VARCHAR(255) | NULLABLE         |
| loginEmail     | VARCHAR(255) | NOT NULL         |
| loginUsername  | VARCHAR(255) | NULLABLE         |
| loginPassword  | TEXT         | NOT NULL (AES encrypted) |
| description    | TEXT         | NULLABLE         |
| createdAt      | DATETIME     | DEFAULT CURRENT_TIMESTAMP |

## 🚦 API Endpoints

### Authentication
```http
POST   /api/auth/register     # User registration
POST   /api/auth/login        # User login
POST   /api/auth/refresh      # Refresh JWT token
POST   /api/auth/logout       # User logout
GET    /api/auth/{id}         # Get user details

```

### Credentials
```http
GET    /api/credentials           # Get user's credentials
POST   /api/credentials           # Create new credential
PUT    /api/credentials/{id}      # Update credential
DELETE /api/credentials/{id}      # Delete credential
```


## 🔒 Security Best Practices

### Implemented
-  Environment-based secrets management
-  AES encryption for sensitive data
-  Strong password requirements and guidance
-  JWT with secure expiration
- Input validation and sanitization
- CORS configuration
- SQL injection prevention (JPA)

### Recommended Additions
- 🔄 Rate limiting
- 🔄 Account lockout after failed attempts
- 🔄 Two-factor authentication (simplified for seniors)
- 🔄 HTTPS enforcement

## 🤝 Contributing

1. **Fork the repository**
2. **Create feature branch**: `git checkout -b feature/amazing-feature`
3. **Commit changes**: `git commit -m 'Add amazing feature'`
4. **Push to branch**: `git push origin feature/amazing-feature`
5. **Open Pull Request**

### Development Guidelines
- Follow existing code style
- Add tests for new features
- Update documentation
- Use meaningful commit messages

## 📝 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

### Common Issues

- Verify .env file exists with keys
- Ensure Java 17+ is installed

**Frontend API errors:**
- Verify backend is running on port 8080
- Check CORS configuration
- Validate .env file has correct API URL

## 🗺️ Roadmap

### v2.0 (Planned)
- [ ] Password generator with senior-friendly explanations
- [ ] Large print mode for better accessibility
- [ ] Import functionality from browsers
- [ ] Simplified backup and recovery

### v2.1 (Future)
- [ ] Data leak notifications and guidance
- [ ] Password generator with senior-friendly explanations
- [ ] Regular security check reminders
- [ ] Large print mode for better accessibility
- [ ] Simplified two-factor authentication

---

**Made with ❤️ for the elderly community**

*Keylo - Helping seniors stay secure in the digital age*