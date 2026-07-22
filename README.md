# GoRide - Desktop Ride-Hailing System

**GoRide** is a JavaFX desktop ride-hailing system built in Java. It connects passengers with available drivers using real-time dynamic matching, asynchronous multithreaded trip execution, encrypted authentication, persistent database connectivity, and local user session management via Java Object Serialization.

---

## ✨ System Features

* **Multi-Role Authentication Gateway**: Dedicated authentication and command interface views for both Passengers and Drivers.
* **ACID Database Transactions**: SQL integration with SHA-256 password hashing for robust user registration and login workflows.
* **Binary Session Persistence (Java Serialization)**: User sessions dynamically persist locally (`session.dat`) across application restarts, bypassing repetitive logins until an explicit logout is initiated.
* **Randomized Driver Dispatch Engine**: Real-time database query dispatch engine selecting available drivers at random (`ORDER BY RAND()`) to balance dispatch distribution.
* **Interactive 1–5 Star Rating System**: Interactive modal dialog allowing passengers to rate drivers upon trip completion, feeding driver feedback into system logs.
* **Asynchronous Execution Pipeline**: Multithreaded execution handling trip booking, driver dispatch, location tracking, dynamic fare generation, and billing processing without freezing the UI thread.

---