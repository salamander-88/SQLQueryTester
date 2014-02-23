SQLQueryTester
==============

Contains the source code of initial version of the SQLQueryTester project.

GENERAL DESCRIPTION

The SQLQueryTester project is dedicated to users who is studying SQL query language. The main function of the project is to check correctness of SQL query syntax, running it over the table "patients", and to display any table modifications immediately to the user so that he could see how one or another query changes the data. The SQLQueryTester does not need any DBMSs installations.

The table "patients" contains entities of integer and string formats as well as images. After each launch the table is filled in with the same sample rows. Any user's modifications are not saved after the application is closed. However, the user can save SQL-queries, he typed, in a separate .sql file and then appload them in the next application launch. The last table column contains images. On the GUI images are displayed as small icons. By hovering mouse coursor over an icon, the corresponding scaled-up image appears in a popup window.

TECHNICAL DESCRIPTION

The SQLQueryTester project is entirely written in Java. The GUI is constructed using the Swing GUI library.
The data is stored in the SQLite database.