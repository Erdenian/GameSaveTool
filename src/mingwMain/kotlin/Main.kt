import kotlinx.cinterop.pointed
import kotlinx.cinterop.toKString
import platform.posix.closedir
import platform.posix.opendir
import platform.posix.readdir
import platform.windows.MB_ICONQUESTION
import platform.windows.MB_OK
import platform.windows.MessageBoxW

@OptIn(ExperimentalUnsignedTypes::class)
fun main() {
    val supportedExtensions = listOf(".gsba", ".rar", ".zip")

    val dublicates = checkNotNull(listFiles("./")).asSequence()
        .filter { name -> supportedExtensions.any { name.endsWith(it) } }
        .map { name ->
            if (name.endsWith(".gsba")) name.takeWhile { it != '_' }
            else name.dropLastWhile { it != '.' }.dropLast(1)
        }.groupingBy { it }
        .eachCount()
        .filter { it.value > 1 }
        .map { (name, count) -> "$name ($count times)" }
        .takeIf { it.isNotEmpty() }
    MessageBoxW(
        null, dublicates?.joinToString("\n") ?: "No dublicates",
        "Dublicates", (MB_OK or MB_ICONQUESTION).toUInt()
    )
}

@OptIn(ExperimentalStdlibApi::class)
private fun listFiles(path: String): List<String>? = opendir(path)?.let { directory ->
    buildList {
        try {
            var info = readdir(directory)
            while (info != null) {
                add(info.pointed.d_name.toKString())
                info = readdir(directory)
            }
        } finally {
            closedir(directory)
        }
    }
}
