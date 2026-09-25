# News Feed Simulator (Kotlin Multiplatform)

Aplikasi konsol **News Feed Simulator** yang dikembangkan menggunakan Kotlin Multiplatform (KMP). Aplikasi ini dibuat untuk memenuhi kriteria penugasan (Pertemuan 3) yang meliputi implementasi Flow, Coroutines, StateFlow, beserta testing yang dimigrasikan ke struktur lintas *platform*.

## Fitur dan Kriteria yang Terpenuhi

1. **Flow Builder (`getNewsFlow`)**:
   - Mensimulasikan data berita baru menggunakan `flow { ... }` setiap 2 detik (`delay(2000)`).
2. **Penggunaan Operators (`filter`, `map`, `onEach`)**:
   - Berada di module `jvmMain` pada `Main.kt` untuk dieksekusi.
3. **StateFlow Implementation**:
   - `MutableStateFlow` digunakan secara internal di dalam `commonMain`.
4. **Coroutines Usage (`async`/`await` & Dispatchers)**:
   - Fungsi asinkron berjalan menggunakan `async(Dispatchers.Default)` agar kompatibel di `commonMain` untuk multiplatform.
5. **Clean Code & Unit Tests (Bonus +10%)**:
   - Berada di `commonTest` menggunakan `kotlin.test` sehingga pengujian berjalan di *multiplatform*.

## Struktur Proyek KMP

```text
src/
├── commonMain/kotlin/com/newsfeed/
│   ├── model/
│   │   ├── News.kt           // Data class untuk Berita
│   │   └── NewsDetail.kt     // Data class untuk Detail Berita
│   └── NewsFeedManager.kt    // Core Logic (Flow Builder, StateFlow, Coroutines Async)
├── commonTest/kotlin/com/newsfeed/
│   └── NewsFeedManagerTest.kt // Unit test untuk Flow dan Coroutines (menggunakan runTest)
└── jvmMain/kotlin/com/newsfeed/
    └── Main.kt               // Titik masuk (Entry point) khusus target JVM dengan runBlocking
```

## Cara Menjalankan (KMP JVM Target)

Karena menggunakan plugin Kotlin Multiplatform dengan target JVM, perintah Gradle sedikit berbeda:

**1. Menjalankan Aplikasi**
```bash
./gradlew jvmRun
```

**2. Menjalankan Unit Test**
```bash
./gradlew jvmTest
# atau
./gradlew allTests
```
