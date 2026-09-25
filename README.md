# NAMA : Hafidz Haqiqi 

# NIM : 124140016

# Tugas Praktikum: News Feed Simulator


## Fitur yang Diimplementasikan
1. **Flow** untuk mensimulasikan data berita baru setiap 2 detik.
2. **Filter** berita berdasarkan kategori (Tech, Sports, Politics, Entertainment).
3. **Transform data** (`map`) menjadi format tampilan yang lebih rapi (menambahkan tag kategori huruf kapital pada judul).
4. **StateFlow** untuk menyimpan dan menampilkan jumlah berita yang sudah dibaca.
5. **Coroutines** untuk mengambil detail isi berita secara asynchronous saat item berita di-klik.

## Cara Menjalankan Aplikasi

Aplikasi ini bisa dijalankan sebagai Desktop App maupun Android App.

### 1. Menjalankan di Desktop (Paling Cepat)
Pastikan Anda memiliki Java JDK ter-install. Dari terminal, jalankan:
```bash
./gradlew :desktopApp:run
```
*(Catatan: Jika Amenggunakan Android Studio, perlu menentukan path Java, contoh: `JAVA_HOME=/opt/android-studio/jbr ./gradlew :desktopApp:run`)*

### 2. Menjalankan di Android Emulator
Pastikan Anda telah menyalakan Android Emulator, lalu jalankan:
```bash
./gradlew :androidApp:installDebug
adb shell am start -n com.example.pam/.MainActivity
```