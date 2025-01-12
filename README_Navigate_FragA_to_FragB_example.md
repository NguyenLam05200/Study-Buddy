# Example: navigating from CourseFragment to AddCourse_Fragment
## 1. AddCourse_Fragment
- Go to `java -> your package name (com.example.studybuddy) -> ui -> course`
- Right click "course", choose `New -> Fragment -> Fragment (with ViewModel)`

## 2. Navigation graph
- Go to `res -> navigation -> mobile_navigation.xml`
- Create another entry like the current existing one for AddCourse_Fragment
- Use the UI to add action from course to addcourse fragment
(alternatively, add <action> tag in course fragment)
```<action
    android:id="@+id/action_nav_course_to_course_addcourse"
    app:destination="@id/course_addcourse" />```

## 3. CourseFragment.kt