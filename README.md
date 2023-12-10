# GLS Case Study: Hackathon Team Management 

**LA01 OOP – Group 5**
- Albert Yulius Ramahalim - 2602078726
- Christoffer Edbert Karuniawan - 2602082944
- Kenzie Raditya Tirtarahardja - 2602153581

---

## Repository Structure

All relevant files are in `hackathon`.
- The path `hackathon/database` contains the CSV files used to store the database.
- The path `hackathon/src/main/java` contains all Java files.

### `connection` package

Package that manages the `Connection` class (reading and writing to CSV files).

### `main` package

Package that manages the `Main` class.

### `menu` package

Package that manages the `Menu` class (displays the menu to the user).

### `model` package

Package that contains the superclass `Model` as well as its children `Team` and `User`, which are the main objects the database works with.

### `query` package

Package that manages the `Query` class, a façade class that handles all repository calls by the user.

### `repository` package

Package that contains the interface `Repository` as well as its children `TeamRepository` and `UserRepository`, which contain the main functionalities to manage the database. Also contains `RepositoryUtil` which has utility functions used in those repositories.