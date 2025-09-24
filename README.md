# Muzz Chat Application Exercise

An Android chat application for Muzz Android Exercise built with Jetpack Compose, following Clean Architecture principles and Android development best practices. This application demonstrates a real-time messaging interface with user switching capabilities, message read status indicators, and proper message grouping.

**Note**: The "master" branch works similarly to the "toggleUser" branch for switching between users, while the "auto-reply-impl" branch simulates automatic responses.

## 📱 Features

- **Real-time Messaging**: Send and receive messages with immediate UI updates
- **User Switching**: Switch between two predefined users via dropdown menu
- **Message Read Status**: Visual indicators showing sent/delivered/read status with checkmarks
- **Smart Message Grouping**: Messages are grouped by sender and time intervals
- **Time Sectioning**: Automatic timestamp display for messages sent more than 1 hour apart
- **Persistent Storage**: Messages are stored locally using Room database
- **Material Design 3**: Modern UI following Google's latest design guidelines
- **Responsive Design**: Optimized for different screen sizes and orientations

## 🏗️ Architecture

This project follows **Clean Architecture** principles with clear separation of concerns:
### Architecture Layers

1. **Domain Layer**: Pure business logic with no Android dependencies
2. **Data Layer**: Handles data persistence and repository implementations
3. **Presentation Layer**: UI components, ViewModels, and user interaction logic

## 🛠️ Tech Stack

### Core Technologies
- **Kotlin**: 100% Kotlin codebase
- **Jetpack Compose**: Modern declarative UI toolkit
- **Material Design 3**: Latest Google design system
- **Coroutines & Flow**: Asynchronous programming and reactive streams

### Architecture & DI
- **Clean Architecture**: Separation of concerns with dependency inversion
- **Hilt**: Dependency injection framework
- **MVVM Pattern**: Model-View-ViewModel architecture pattern

### Data Persistence
- **Room Database**: Local SQLite abstraction
- **StateFlow**: Reactive state management
- **Type Converters**: LocalDateTime serialization

### Testing
- **JUnit 4**: Unit testing framework
- **MockK**: Mocking library for Kotlin
- **Robolectric**: Android unit testing
- **Espresso**: UI testing framework
- **Compose Testing**: Jetpack Compose testing utilities
- **Turbine**: Testing Flow emissions

## 🎯 Key Implementation Decisions

### 1. Clean Architecture Implementation

**Decision**: Implement Clean Architecture with strict layer separation

**Rationale**:
- Ensures testability and maintainability
- Allows for easy mocking and testing of individual components
- Follows Android best practices and SOLID principles

**Implementation**:
- Domain layer contains pure business logic (no Android dependencies)
- Repository pattern abstracts data sources
- Use cases encapsulate business rules
- Dependency inversion through interfaces

### 2. UI State Management

**Decision**: Separate UI state from ViewModel in Composable functions

**Rationale**:
- Improves testability of UI components
- Allows for better composition and reusability
- Enables easier UI testing without ViewModel dependencies

### 3. Message Grouping Logic
**Decision**: Group messages by sender and time intervals

**Rationale**:
- Improves user experience by reducing visual clutter
- Follows modern messaging app patterns (WhatsApp, Telegram)
- Optimizes screen space usage

**Implementation**:

- Messages from the same sender within 20 seconds are grouped together 
- Different senders create separate message groups 
- Timestamps shown for messages more than 1 hour apart

### 4. Read Status Implementation
**Decision**: Use isRead boolean property with visual indicators 

**Rationale**:
- Simple and effective way to show message status 
- Uses familiar checkmark icons (single = sent, double = read)
- Green color for read messages provides clear visual feedback

**Visual Indicators**:
- ✓✓ (Gray): Message sent 
- ✓✓ (Yellow): Message read

### 5. Database Design
**Decision**: Use Room with LocalDateTime for timestamps 

**Rationale**:
- Room provides excellent SQLite abstraction 
- LocalDateTime offers better date/time handling than timestamps 
- Type converters ensure proper serialization

### 6. User Management
**Decision**: Two predefined users with local switching 

**Rationale**:
- Simplifies demo/testing scenarios 
- Avoids complex authentication implementation 
- Focuses on core messaging functionality

**Implementation**:
- Sarah (user1) and Miracle (user2) as predefined users 
- Local switching via dropdown menu 
- Automatic message read marking on user switch

### 7. Testing Strategy
**Decision**: Comprehensive testing with different approaches for different layers 

**Rationale**:
- Unit tests for business logic (domain layer)
- Integration tests for data layer (Room)
- UI tests for presentation layer (Compose)
- Robolectric for JVM-based UI tests with mocking

**Testing Structure**:
- test/: Unit tests with MockK and Robolectric 
- androidTest/: Integration and E2E tests with real components

## 📋 Assumptions Made
1. **User Management**

   **Assumption**: Only two users needed for demonstration

   **Justification**: Simplifies the demo and focuses on messaging functionality

2. **Message Delivery**

   **Assumption**: Messages are instantly "delivered" and marked as read when other user is active

   **Justification**: Simulates real-time messaging without network complexity
   
3. **Data Persistence**

   **Assumption**: Local storage only (no backend/server)

   **Justification**: Focuses on mobile app architecture and UI/UX
   
4. **Message Types**

   **Assumption**: Text messages only

   **Justification**: Keeps scope manageable and focuses on core architecture
 
5. **Time Formatting**

   **Assumption**: Standard time format (Today, Yesterday, Day of week, Full date)

   **Justification**: Follows common messaging app patterns
   
6. **UI Design**

   **Assumption**: Muzz brand colors and modern Material Design

   **Justification**: Professional appearance with good accessibility
 
7. **Performance**

   **Assumption**: Moderate message volume (hundreds, not thousands)

   **Justification**: Suitable for typical personal/small group chats
   
## 🚀 Getting Started
**Prerequisites**:
- Android Studio Hedgehog (2023.1.1) or newer 
- Android SDK 30+ 
- Gradle 8.0+