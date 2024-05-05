import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field.Store.YES
import org.apache.lucene.document.TextField
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.store.FSDirectory
import java.nio.file.Files
import java.nio.file.Path
import java.util.*

/**
 * Main function to start the application.
 */
fun main() {
    val indexPath = "index"
    val scanner = Scanner(System.`in`)
    println("Welcome!")

    ensureIndexExists(indexPath, scanner)

    FSDirectory.open(Path.of(indexPath)).use { indexDir ->
        DirectoryReader.open(indexDir).use { reader ->
            val searcher = IndexSearcher(reader)
            queryLoop(searcher)
        }
    }
}

/**
 * Ensures the index directory is ready for use, creating or updating it if necessary.
 */
fun ensureIndexExists(indexPath: String, scanner: Scanner) {
    val path = Path.of(indexPath)
    if (Files.exists(path) && Files.list(path).findAny().isPresent) {
        handleExistingIndex(indexPath, scanner)
    } else {
        println("No index files found. Please enter the path to data to create a new index:")
        indexData(indexPath, getDataPath(scanner))
    }
}

/**
 * Handles user input for reindexing existing data.
 */
fun handleExistingIndex(indexPath: String, scanner: Scanner) {
    println("There are indexed files. Do you want to reindex them (Y/N)?")
    when (scanner.nextLine().uppercase()) {
        "Y" -> indexData(indexPath, getDataPath(scanner))
        "N" -> Unit
        else -> {
            println("Please enter 'Y' (for 'yes') or 'N' (for 'no').")
            handleExistingIndex(indexPath, scanner)
        }
    }
}

/**
 * Gets the data path from the user.
 */
fun getDataPath(scanner: Scanner): String {
    while (true) {
        println("Please enter the path to data:")
        val dataPath = scanner.nextLine()
        if (Files.isDirectory(Path.of(dataPath))) {
            return dataPath
        } else {
            println("Data directory does not exist or is not a directory. Please try again.")
        }
    }
}

/**
 * Indexes data from the given path into the specified index path.
 */
fun indexData(indexPath: String, dataPath: String) {
    FSDirectory.open(Path.of(indexPath)).use { indexDir ->
        IndexWriter(indexDir, IndexWriterConfig(StandardAnalyzer())).use { writer ->
            val fileCount = indexDirectory(writer, dataPath)
            println("Total files indexed: $fileCount")
        }
    }
}

/**
 * Indexes each text file in the specified directory.
 */
fun indexDirectory(writer: IndexWriter, dataPath: String): Int {
    val dataDir = Path.of(dataPath)
    if (!Files.isDirectory(dataDir)) {
        println("Data directory does not exist or is not a directory")
        return 0
    }

    writer.deleteAll()
    println("Index cleared.")

    return Files.walk(dataDir).filter { it.isRegularFile() && it.toString().endsWith(".txt") }.map { path ->
            Document().apply {
                add(TextField("contents", Files.readString(path), YES))
                add(TextField("path", path.toString(), YES))
            }.also {
                writer.addDocument(it)
                println("Indexed: ${path.fileName}")
            }
        }.count().toInt().also {
            println("Indexing complete. Total files indexed: $it")
        }
}

/**
 * Continuously prompts the user to enter search queries and displays results.
 */
fun queryLoop(searcher: IndexSearcher) {
    val scanner = Scanner(System.`in`)
    while (true) {
        println("Enter a search query (or type '!q' to quit):")
        val line = scanner.nextLine().trim()

        if (line == "!q") {
            println("Exiting program...")
            break
        }

        try {
            if (line.isNotEmpty()) {
                searchIndex(searcher, line)
            }
        } catch (e: org.apache.lucene.queryparser.classic.ParseException) {
            println("Invalid search query: ${e.message}")
        }
    }
}


/**
 * Searches the index with the given query string.
 */
fun searchIndex(searcher: IndexSearcher, queryString: String) {
    try {
        val query = QueryParser("contents", StandardAnalyzer()).parse(queryString)
        val hits = searcher.search(query, 10)
        if (hits.totalHits.value == 0L) {
            println("No results found for '$queryString'. Check spelling or try different keywords.")
        } else {
            println("Found ${hits.totalHits} hits:")
            hits.scoreDocs.forEach { hit ->
                println("Hit: ${searcher.doc(hit.doc).get("path")}, (score: ${hit.score})")
            }
        }
    } catch (e: org.apache.lucene.queryparser.classic.ParseException) {
        println("Error searching for '$queryString': ${e.message}")
    } catch (e: Exception) {
        println("An unexpected error occurred during search: ${e.message}")
    }
}

/**

Extension function to check if a path represents a regular file.
 */
fun Path.isRegularFile() = Files.isRegularFile(this)