package com.example.studybuddy.ui.theme


import androidx.compose.ui.graphics.Color

// Dark mode colors
val DarkPrimary = Color(0xFFBB86FC)               // Tím nhạt, tạo điểm nhấn nhẹ nhàng
val DarkOnPrimary = Color(0xFF3700B3)             // Tím đậm, dùng cho text trên nền tím
val DarkPrimaryContainer = Color(0xFF6200EE)      // Màu tím đậm hơn, dùng cho container chính
val DarkOnPrimaryContainer = Color(0xFFEDE7F6)    // Màu sáng cho text trên container

val DarkInversePrimary = Color(0xFFBB86FC)        // Màu chính khi đảo nền

val DarkSecondary = Color(0xFF03DAC6)             // Màu xanh biển nhạt, nhẹ nhàng và dễ chịu
val DarkOnSecondary = Color(0xFF018786)           // Xanh đậm, dùng cho text trên nền xanh nhạt
val DarkSecondaryContainer = Color(0xFF004D40)    // Xanh đậm hơn cho vùng container
val DarkOnSecondaryContainer = Color(0xFFB2DFDB)  // Xanh nhạt cho text trên container

val DarkTertiary = Color(0xFFFFC107)              // Màu vàng nhạt cho nhấn mạnh khác
val DarkOnTertiary = Color(0xFF3E2723)            // Nâu đậm cho text trên nền vàng
val DarkTertiaryContainer = Color(0xFF5D4037)     // Nâu nhẹ cho container
val DarkOnTertiaryContainer = Color(0xFFFFE082)   // Vàng nhạt cho text trên nền nâu

val DarkBackground = Color(0xFF121212)            // Nền tối cho toàn app
val DarkOnBackground = Color(0xFFEDE7F6)          // Màu sáng trên nền tối

val DarkSurface = Color(0xFF1E1E1E)               // Bề mặt tối cho các thành phần nổi bật
val DarkOnSurface = Color(0xFFEDE7F6)             // Màu text trên bề mặt
val DarkSurfaceVariant = Color(0xFF2E2E2E)        // Bề mặt phụ cho các container
val DarkOnSurfaceVariant = Color(0xFFCFCFCF)      // Màu sáng vừa cho text trên bề mặt phụ
val DarkSurfaceTint = DarkPrimary

val DarkInverseSurface = Color(0xFFEDE7F6)        // Nền sáng đảo ngược
val DarkInverseOnSurface = Color(0xFF121212)      // Text trên nền sáng đảo ngược

val DarkError = Color(0xFFCF6679)                 // Màu đỏ nhẹ cho lỗi
val DarkOnError = Color(0xFF3700B3)               // Text trên màu lỗi
val DarkErrorContainer = Color(0xFFB00020)        // Màu container cho lỗi
val DarkOnErrorContainer = Color(0xFFF2DEDE)      // Text trên container lỗi

val DarkOutline = Color(0xFF666666)               // Màu outline cho các thành phần
val DarkOutlineVariant = Color(0xFF777777)        // Màu variant nhẹ hơn một chút
val DarkScrim = Color(0x66000000)                 // Màu overlay tối

val DarkSurfaceBright = Color(0xFF2A2A2A)         // Màu sáng nhẹ cho bề mặt
val DarkSurfaceContainer = Color(0xFF3A3A3A)      // Container trung gian
val DarkSurfaceContainerHigh = Color(0xFF4A4A4A)
val DarkSurfaceContainerHighest = Color(0xFF5A5A5A)
val DarkSurfaceContainerLow = Color(0xFF1A1A1A)
val DarkSurfaceContainerLowest = Color(0xFF0A0A0A)
val DarkSurfaceDim = Color(0xFF121212)

// Light mode colors
val LightPrimary = Color(0xFF6200EE)               // Tím đậm, dễ nhận diện trong nền sáng
val LightOnPrimary = Color(0xFFFFFFFF)             // Text trắng trên nền tím
val LightPrimaryContainer = Color(0xFFBB86FC)      // Tím nhạt hơn cho container
val LightOnPrimaryContainer = Color(0xFF3700B3)    // Tím đậm cho text trên nền container nhạt

val LightInversePrimary = Color(0xFF6200EE)        // Màu chính khi đảo nền

val LightSecondary = Color(0xFF018786)             // Xanh biển, nhẹ nhàng cho điểm nhấn
val LightOnSecondary = Color(0xFFFFFFFF)           // Trắng trên nền xanh biển
val LightSecondaryContainer = Color(0xFF03DAC6)    // Xanh nhạt cho container phụ
val LightOnSecondaryContainer = Color(0xFF004D40)  // Xanh đậm cho text trên container nhạt

val LightTertiary = Color(0xFFFFA000)              // Màu vàng sáng hơn cho nhấn mạnh
val LightOnTertiary = Color(0xFF3E2723)            // Nâu đậm cho text trên nền vàng
val LightTertiaryContainer = Color(0xFFFFD54F)     // Vàng nhạt cho container phụ
val LightOnTertiaryContainer = Color(0xFF795548)   // Nâu nhạt trên container vàng

val LightBackground = Color(0xFFFFFFFF)            // Nền sáng cho toàn app
val LightOnBackground = Color(0xFF121212)          // Text tối trên nền sáng

val LightSurface = Color(0xFFF5F5F5)               // Bề mặt sáng cho các thành phần nổi bật
val LightOnSurface = Color(0xFF121212)             // Text tối trên bề mặt sáng
val LightSurfaceVariant = Color(0xFFE0E0E0)        // Bề mặt phụ
val LightOnSurfaceVariant = Color(0xFF757575)      // Màu tối cho text trên bề mặt phụ
val LightSurfaceTint = LightPrimary

val LightInverseSurface = Color(0xFF121212)        // Nền tối đảo ngược
val LightInverseOnSurface = Color(0xFFFFFFFF)      // Text trên nền tối đảo ngược

val LightError = Color(0xFFB00020)                 // Đỏ nhẹ cho lỗi
val LightOnError = Color(0xFFFFFFFF)               // Text trắng trên màu lỗi
val LightErrorContainer = Color(0xFFF2DEDE)        // Đỏ nhạt cho container lỗi
val LightOnErrorContainer = Color(0xFFB00020)      // Text đỏ trên container lỗi

val LightOutline = Color(0xFFBDBDBD)               // Màu outline cho các thành phần
val LightOutlineVariant = Color(0xFF9E9E9E)        // Màu variant sáng hơn một chút
val LightScrim = Color(0x66000000)                 // Overlay tối

val LightSurfaceBright = Color(0xFFF8F8F8)         // Màu sáng cho bề mặt
val LightSurfaceContainer = Color(0xFFECECEC)      // Màu trung gian cho container
val LightSurfaceContainerHigh = Color(0xFFE0E0E0)
val LightSurfaceContainerHighest = Color(0xFFD4D4D4)
val LightSurfaceContainerLow = Color(0xFFF3F3F3)
val LightSurfaceContainerLowest = Color(0xFFF5F5F5)
val LightSurfaceDim = Color(0xFFEDEDED)


