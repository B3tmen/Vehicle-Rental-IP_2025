# 💡 VehicleRental - Employee application made using React + Typescript + Vite + PrimeReact UI library

# ⚙️ Specification

This is the central application of the system used by employees. The application has a page for logging in to the system, and appropriate pages will be displayed depending on the role the employee has.

**Administrators** have access to the following pages:
- Page for the management of vehicles, where all vehicles are able to be display by type in table. It should be possible to create a vehicle or delete it. Additionally, an upload option is available on this page of a CSV file containing data about the vehicle(s).
- Details page where all the data about a vehicle is displayed. In addition to the basic information, malfunctions can be added or deleted, and all rentals of that vehicle are displayed.
- Manufacturer management (CRUD) page.
- User management page where all users (clients and employees, separately) are displayed. Client accounts can be blocked or unblocked, and for employee accounts enable CRUD operations.

**Operators** have access to the following pages:
- Rental overview page - information display only.
- Page for viewing rented vehicles on a map
- Customer overview page with options to block or unblock accounts.
- The page for entering the malfunctions of vehicles.

**Managers** have access to the following pages:
- All admin pages.
- All operator pages.
- Page for viewing statistics (graph of total income per day for the selected month, number of malfunctions per vehicle type, chart of total income by type of vehicle).
- Page for defining rental prices.

On all pages for viewing data, it is necessary to enable searching by values ​​from at least one column. Pagination of tables, or virtual scroll for lists is necessary.
The application must have a uniform appearance on all pages. 

This application is made using React + Typescript. Other libraries include PrimeReact UI, FontAwesome, yup.

# 💻 Demo
_Notice_: The quality of the following videos is not the best and does not provide an exact representation of the application.

https://github.com/user-attachments/assets/7ae31e85-e242-4d56-b526-70ea8feae780

https://github.com/user-attachments/assets/aa9195c6-fdc6-4645-80ec-d7f7bfbd4d46



