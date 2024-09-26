# Quiz-Application
A quiz-based KBC (Kaun Banega Crorepati) application using Java, with a fully interactive GUI
for user registration, question answering, and game progression.

System Requirements

    Java Development Kit (JDK): 8 or higher
    MySQL Database: 5.7 or higher
    MySQL Connector/J: 8.0 or higher
Create a new database named 'kbc'
The following tables are used in the application:

    'User' Table

    id (Primary Key): Unique identifier for each user.
    username: Name of the user.
    total_winnings: Total amount won by the user.

    'Questions' Table

    SrNo (Primary Key): Unique identifier for each question.
    question: The quiz question.
    answer: Correct answer for the question.
    optionA, optionB, optionC, optionD: Multiple choice options.
    flag: Indicates difficulty level or question category.

    'Winnings' Table

    id (Primary Key): Unique identifier for each record in the winnings table.
    user_id: Reference to the user's id field in the User table.
    total_amount: Total amount won by the user in a particular game.
Compile and Run the Application
        
    javac -cp .:/path/to/mysql-connector-java.jar kbc_copy.java
    java -cp .:/path/to/mysql-connector-java.jar kbc_copy

Play the Game

    Register as a new user.
    Start the quiz and answer questions.
    Track your score and winnings.
