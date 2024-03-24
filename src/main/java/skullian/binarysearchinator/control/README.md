# README!

This is the directory where all the FXML Controllers are.

The `main.fxml` page / `help.fxml` / `help.fxml` screen is all controlled by `MainController`

Clicking Start on the main page either leads to `JarTypeAnalysisController` (**searching.fxml**)
if no previous SQLite data is found. 

If there is data found, it will instead lead to `PDCController` (**resume_confirmation.fxml**), where it asks the user if they want to
continue with their previous data or restart.


If they accept, they continue to `DependencyAnalysisController` (**decom.fxml**), skipping jar analysis, then to `TBC`.

If they restart, all SQLite data for that folder is wiped and the database connection is closed.
They are returned to the main screen (**main.fxml**) controlled by `MainController`.

`MainController` -> No Data Found? -> `JarTypeAnalysisController` -> `ConfigConfirmationHandler` -> `DependencyAnalysisController` -> `TBC`

`MainController` -> Data Found? -> `PDCController` -> `DependencyAnalysisController` -> `TBC`



(PDCController = PreviousDataConfirmationController)