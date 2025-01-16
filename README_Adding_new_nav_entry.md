# Adding custom screen to navigation drawer
## 1. Fragment
- Go to `java -> your package name (com.example.studybuddy)`
- Right click "ui" folder, choose `New -> Package`
- Right click the newly created folder, choose `New -> Fragment -> Fragment (with ViewModel)`

## 2. Navigation drawer
- Go to `res -> menu -> navigation_bar_drawer.xml`
- Create another entry like the current existing one
- Create the `android:id` (this one will be used for navigation graph)

## 3. Navigation graph
- Go to `res -> navigation -> mobile_navigation.xml`
- Create another entry like the current existing one
- MATCH the `android:id` to the one in navigation drawer

## 4. MainActivity.kt (optional)
- This is for changing the back button to the menu button
- Go to `java -> your package name (com.example.studybuddy) -> MainActivity.kt`
- Find `appBarConfiguration = AppBarConfiguration`
- Add `R.id.<the_id_of_navigation_drawer_item>` 
(example: `R.id.nav_settings`)
