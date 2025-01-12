# Example: navigating from Course_Frag to AddCourse_Frag
## 1. AddCourse_Fragment
- Go to `java -> your package name (com.example.studybuddy) -> ui -> course`
- Right click "course", choose `New -> Fragment -> Fragment (with ViewModel)`

## 2. Navigation graph
- Go to `res -> navigation -> mobile_navigation.xml`
- Create another entry like the current existing one for AddCourse_Frag
- Use the UI to add action from Course_Frag to 
(alternatively, add `<action>` tag in Course_Frag)
```
<action
    android:id="@+id/action_nav_course_to_course_addcourse"
    app:destination="@id/course_addcourse" />
```

## 3. course_frag.kt
- Get navController, get the button
- Bind button onclick to trigger navController.navigate()
```
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val navController = findNavController()

    val testbutton : Button = view.findViewById(R.id.testbutton)
    testbutton.setOnClickListener {
        navController.navigate(R.id.course_addcource_frag)
    }
}
```
