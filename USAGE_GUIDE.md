# Application Screenshots and Usage Guide

## Login Screen

The login screen allows users to authenticate as either Admin or User:
- Admin login requires username: `admin` and password: `admin`
- User login only requires a username

## Admin View

### Main Interface
The admin view features:
1. **Top Bar**: Title and logout button
2. **Left Panel - Stables Table**:
   - Columns: Name, Capacity, Load (current/max and percentage)
   - Buttons: Add Stable, Remove Stable, Sort by Load
3. **Right Panel - Horses Table**:
   - Columns: Name, Breed, Type, Condition, Age, Price
   - Search field for filtering by name
   - ComboBox for filtering by condition
   - Buttons: Add Horse, Remove Horse, Edit Horse, Sort by Name, Sort by Price

### Operations

#### Adding a Stable
1. Click "Add Stable" button
2. Dialog appears with fields:
   - Name (text field)
   - Capacity (number field)
3. Click OK to add or Cancel to abort

#### Adding a Horse
1. Select a stable from the left table
2. Click "Add Horse" button
3. Dialog appears with fields:
   - Name, Breed (text fields)
   - Type (ComboBox: Hot-blooded, Cold-blooded)
   - Condition (ComboBox: Healthy, Sick, Training, Quarantine, Sold)
   - Age, Price, Weight (number fields)
4. Click OK to add

#### Editing a Horse
1. Select a horse from the right table
2. Click "Edit Horse" button
3. Dialog shows editable fields:
   - Condition (can be changed)
   - Weight (can be updated)
4. Click OK to save changes

#### Removing Items
1. Select a stable or horse
2. Click "Remove Stable" or "Remove Horse"
3. Confirmation dialog appears
4. Click OK to confirm removal

#### Sorting and Filtering
- **Sort Stables**: Click "Sort by Load" to order by occupancy percentage
- **Sort Horses**: Click "Sort by Name" or "Sort by Price"
- **Filter Horses**: Type in search field or select condition from dropdown

## User View

### Main Interface
The user view is similar to admin view but read-only:
1. **Top Bar**: "User View (Read-Only)" title and logout button
2. **Left Panel - Stables Table**: View-only, no edit buttons
3. **Right Panel - Horses Table**: 
   - View-only table
   - Search and filter controls (same as admin)
   - Sort buttons (same as admin)

### Operations

#### Viewing Data
- Click on a stable in the left table to see its horses
- All data is visible but cannot be modified

#### Searching and Filtering
- Type in search field to filter horses by name
- Select condition from dropdown to filter by status
- Works the same as admin view

## Error Handling

The application shows error dialogs for:
- Duplicate stable names
- Duplicate horse names in same stable
- Capacity exceeded when adding horses
- Attempting to remove non-existent items
- Invalid data (negative capacity, empty names, etc.)

All errors are shown in user-friendly dialog boxes with clear messages.

## Sample Data

On startup, the application loads sample data:

**Stables**:
- North Farm (capacity 10) - 4 horses
- East Barn (capacity 5) - 2 horses
- South Stable (capacity 8) - 3 horses
- West Ranch (capacity 3) - 1 horse

**Sample Horses**:
- Bella (Arabian, hot-blooded, healthy, 15,000 PLN)
- Argo (Shire, cold-blooded, training, 8,000 PLN)
- Coco (Thoroughbred, hot-blooded, healthy, 22,000 PLN)
- And 7 more...

## Navigation Flow

```
Login Screen
    ├─> Admin Login (with credentials)
    │       └─> Admin View
    │               ├─> Manage Stables
    │               ├─> Manage Horses
    │               └─> Logout → Back to Login
    │
    └─> User Login (no password required)
            └─> User View (Read-only)
                    ├─> View Stables
                    ├─> View Horses
                    ├─> Search/Filter
                    └─> Logout → Back to Login
```

## Running the Application

To see the actual UI, run:
```bash
mvn javafx:run
```

The application will:
1. Load sample data
2. Display login screen
3. Allow navigation based on user role
