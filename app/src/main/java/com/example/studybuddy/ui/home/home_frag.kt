package com.example.studybuddy.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studybuddy.AppViewModelFactory
import com.example.studybuddy.R
import com.example.studybuddy.data.local.DatabaseProvider
import com.example.studybuddy.data.repository.CourseRepository
import com.example.studybuddy.databinding.FragmentHomeBinding
import com.example.studybuddy.ui.course.CourseAdapter
import com.example.studybuddy.ui.course.CourseFragmentDirections
import com.example.studybuddy.ui.course.CourseViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class Quote(val content: String, val author: String)
private val quotes = listOf(
    Quote("Có công mài sắt, có ngày nên kim", "Tục ngữ Việt Nam"),
    Quote("Học, học nữa, học mãi", "Lenin"),
    Quote("Không có việc gì khó, chỉ sợ lòng không bền", "Hồ Chí Minh"),
    Quote("Kiến tha lâu đầy tổ", "Tục ngữ Việt Nam"),
    Quote("Giáo dục là vũ khí mạnh nhất mà bạn có thể sử dụng để thay đổi thế giới", "Nelson Mandela"),
    Quote("Học tập không phải là sự chuẩn bị cho cuộc sống, học tập chính là cuộc sống", "John Dewey"),
    Quote("Chỉ có những người biết học suốt đời mới là người thành công", "Albert Einstein"),
    Quote("Chìa khóa của sự thành công là sự kiên nhẫn", "Benjamin Franklin"),
    Quote("Thất bại là cơ hội để bắt đầu lại một cách thông minh hơn", "Henry Ford"),
    Quote("Người không đọc sách chẳng khác gì kẻ không biết chữ", "Mark Twain"),
    Quote("Không có thiên tài bẩm sinh, chỉ có sự chăm chỉ không ngừng", "Thomas Edison"),
    Quote("Tương lai thuộc về những ai tin vào vẻ đẹp của giấc mơ mình", "Eleanor Roosevelt"),
    Quote("Học hỏi từ ngày hôm qua, sống cho hôm nay, hy vọng cho ngày mai", "Albert Einstein"),
    Quote("Không bao giờ là quá già để đặt mục tiêu mới hoặc mơ một giấc mơ mới", "C.S. Lewis"),
    Quote("Bạn không bao giờ biết mình có thể làm được gì nếu không cố gắng", "Theodore Roosevelt"),
    Quote("Giáo dục là cánh cửa dẫn đến sự tự do", "George Washington Carver"),
    Quote("Sách là bạn tốt nhất của con người", "Ralph Waldo Emerson"),
    Quote("Mỗi ngày là một cơ hội để học điều gì đó mới", "Dalai Lama"),
    Quote("Thành công là một cuộc hành trình, không phải là điểm đến", "Arthur Ashe"),
    Quote("Người học không bao giờ già", "Henry Ford"),
    Quote("Học là việc làm cả đời", "Aristotle"),
    Quote("Tôi không thất bại. Tôi chỉ tìm ra 10,000 cách không hoạt động", "Thomas Edison"),
    Quote("Hãy làm hôm nay những gì người khác không làm, để ngày mai bạn có thể làm những gì người khác không thể", "Jerry Rice"),
    Quote("Không có con đường tắt đến bất kỳ nơi nào đáng giá", "Beverly Sills"),
    Quote("Học hỏi là một kho báu sẽ đi theo chủ nhân của nó đến mọi nơi", "Ngạn ngữ Trung Quốc"),
    Quote("Người biết nhiều ngôn ngữ là một tâm hồn có nhiều cuộc sống", "Carl Friedrich Gauss"),
    Quote("Hãy hành động như thể những gì bạn làm tạo ra sự khác biệt. Vì nó thực sự như vậy", "William James"),
    Quote("Sự giáo dục bắt đầu từ khi còn trong nôi và chỉ kết thúc khi xuống mồ", "Robert E. Lee"),
    Quote("Hãy đặt mục tiêu cao, và đừng ngừng học hỏi", "Michael Jordan"),
    Quote("Học tập là con đường ngắn nhất để đạt được tự do", "Frederick Douglass"),
    Quote("Học hành là nền móng cho mọi thành công", "Ngạn ngữ Hy Lạp"),
    Quote("Chúng ta không ngừng học hỏi, bởi học hỏi là điều kiện của sự tiến bộ", "George Bernard Shaw"),
    Quote("Hãy biến nỗi sợ thất bại thành động lực để cố gắng", "Chris Grosser"),
    Quote("Người thành công là người học từ thất bại", "Richard Branson"),
    Quote("Sự tò mò là khởi đầu của mọi trí tuệ", "Aristotle"),
    Quote("Thời gian học là thời gian sống", "Henry Adams"),
    Quote("Người đọc sách sống một ngàn cuộc đời trước khi chết", "George R.R. Martin"),
    Quote("Học hỏi mỗi ngày, tiến bộ mỗi giờ", "John Wooden"),
    Quote("Đừng đợi đến ngày mai để làm điều có thể làm hôm nay", "Benjamin Franklin"),
    Quote("Học không bao giờ là thừa", "Ngạn ngữ Anh"),
    Quote("Tri thức là sức mạnh", "Francis Bacon"),
    Quote("Người có trí thức là người giàu nhất", "Plato"),
    Quote("Học tập là sự đầu tư tốt nhất", "Benjamin Franklin"),
    Quote("Người học hỏi là người thay đổi thế giới", "Bill Gates"),
    Quote("Hãy tin rằng bạn có thể làm được, và bạn đã đi được nửa đường", "Theodore Roosevelt"),
    Quote("Sự chăm chỉ luôn vượt trội hơn tài năng thiên bẩm", "Tim Notke"),
    Quote("Điều kỳ diệu không phải là chúng ta hoàn thành được điều gì, mà là chúng ta không bao giờ bỏ cuộc", "Harriet Beecher Stowe"),
    Quote("Hãy luôn sẵn sàng học hỏi, vì tri thức là vô tận", "Mahatma Gandhi")
)

class home_frag : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var quoteAdapter: QuoteAdapter
    private lateinit var viewModel: home_vm


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Khởi tạo repository và factory
        val repository = CourseRepository(DatabaseProvider.getDatabase())
        val factory = AppViewModelFactory(repository)

        // Sử dụng factory để tạo ViewModel
        viewModel = ViewModelProvider(this, factory)[home_vm::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val root: View = binding.root
//        setupRecyclerView(root)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        quoteAdapter = QuoteAdapter(quotes)
        binding.quoteViewPager.adapter = quoteAdapter

        // Hiệu ứng chuyển đổi
        binding.quoteViewPager.setPageTransformer { page, position ->
            page.apply {
                val absPos = kotlin.math.abs(position)
                scaleY = 1 - (0.2f * absPos)
                alpha = 1 - (0.3f * absPos)
            }
        }

        // Auto-slide
        startAutoSlide()

        // Courses Adapter
        val courseAdapter = CourseAdapter(
            onAction = { course, action ->
                when (action) {
                    CourseAdapter.Action.EDIT -> navigateToEditCourse(course.id)
                    CourseAdapter.Action.DELETE -> viewModel.deleteCourse(course)
                }
            },
            onItemClick = { course ->
                navigateToCourseDetails(course.id)
            }
        )
        binding.recyclerViewUpcomingCourses.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewUpcomingCourses.adapter = courseAdapter

        // Observe upcoming courses
        viewModel.upcomingCourses.observe(viewLifecycleOwner) { courses ->
            courseAdapter.submitList(courses)
        }

        // Fetch data
        viewModel.fetchUpcomingCourses()
    }

    private fun startAutoSlide() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                val currentItem = binding.quoteViewPager.currentItem
                val nextItem = (currentItem + 1) % quotes.size
                binding.quoteViewPager.setCurrentItem(nextItem, true)
                handler.postDelayed(this, 5000) // 5 seconds delay
            }
        }, 5000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        handler.removeCallbacksAndMessages(null) // Stop auto-slide when fragment is destroyed
    }


    private fun navigateToAddCourse() {

        val action = home_fragDirections.actionHomeFragToManageCourseFrag(
            courseId = -1, // Giá trị mặc định cho chế độ ADD
            mode = "ADD"
        )
        findNavController().navigate(action)
    }

    private fun navigateToEditCourse(courseId: Int) {
        // Navigate to EditCourseFragment
        val action = home_fragDirections.actionHomeFragToManageCourseFrag(
            courseId = courseId,
            mode = "EDIT"
        )
        findNavController().navigate(action)
    }

    private fun navigateToCourseDetails(courseId: Int) {
        val action = home_fragDirections.actionHomeFragToManageCourseFrag(
            courseId = courseId,
            mode = "DETAILS"
        )
        findNavController().navigate(action)
    }
}