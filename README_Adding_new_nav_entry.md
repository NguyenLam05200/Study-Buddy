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

# Các bước tạo và sử dụng table mới trong Realm:
## 1. Tạo Model:
   Tạo file CourseModel.kt và kế thừa từ RealmObject.
   Định nghĩa các field cần thiết với các thuộc tính Realm hỗ trợ.
   ```kotlin
   class CourseModel : RealmObject {
   @PrimaryKey
   var id: Long = 0
   var name: String = ""
   var startTime: RealmInstant = RealmInstant.now()
   // Thêm các field khác...
   }
   ```
## 2. Cấu hình Realm:
   Thêm CourseModel vào schema trong DatabaseProvider.
   ```kotlin
   val config = RealmConfiguration.Builder(schema = setOf(CourseModel::class))
   .deleteRealmIfMigrationNeeded()
   .build()
   realm = Realm.open(config)
   ```
## 3. Tạo Repository:
   Tạo CourseRepository để quản lý dữ liệu.
   Viết các hàm CRUD (Create, Read, Update, Delete).
   ```kotlin
   class CourseRepository(private val realm: Realm) {
   fun getAllCourses(): Flow<List<CourseModel>> = realm.query<CourseModel>().asFlow().map { it.list }
   suspend fun addCourse(course: CourseModel) { realm.write { copyToRealm(course) } }
   suspend fun deleteCourse(course: CourseModel) { realm.write { findLatest(course)?.let { delete(it) } } }
   }
   ```
## 4. Tạo ViewModel:
   Tạo CourseViewModel và gọi CourseRepository để thực hiện các thao tác.
   ```kotlin
   class CourseViewModel(private val repository: CourseRepository) : ViewModel() {
   val courses: Flow<List<CourseModel>> = repository.getAllCourses()
   fun addCourse(course: CourseModel) { viewModelScope.launch { repository.addCourse(course) } }
   }
   ```
## 5. Kết nối với UI:
   Sử dụng ViewModel trong Fragment để hiển thị danh sách hoặc thực hiện thao tác CRUD.
   ```kotlin
   val viewModel: CourseViewModel by viewModels {
   val realm = DatabaseProvider.getDatabase(requireContext())
   CourseViewModelFactory(CourseRepository(realm))
   }
   viewModel.courses.collectLatest { courses -> adapter.submitList(courses) }
   ```
## 6. Hiển thị RecyclerView:
   Dùng CourseAdapter để hiển thị danh sách CourseModel trong RecyclerView.

