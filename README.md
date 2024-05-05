## Searcher

This project is a test project for internship, a simple command-line application built with Kotlin. It uses Apache Lucene for indexing and searching text files.

### Features
- You can index a directory of text files, making their contents searchable.
-  Once indexed, you can search for specific terms or phrases within the indexed files.

### Getting Started

1. Clone the repository to your machine.

2. Compile the Kotlin code to build the application.

3. Execute the compiled application.

### Usage
When you run the application, follow the instructions:

#### Indexing
- If there are indexed files already, you'll be asked if you want to reindex them. Respond with `Y` or `N`.
- If there are no indexed files or if you choose to reindex, you'll be prompted to enter the path to the data directory. It should contain some .txt files.

#### Searching
- After indexing is complete, you can enter search queries to search for specific terms or phrases within the indexed files.
- Type your search query and press Enter. The application will display the search results, including the file paths and scores.

#### Exiting
- To exit the application, type `!q` and press Enter.

### Example
Here's an example of how the application works:

Welcome!

There are indexed files. Do you want to reindex them (Y/N)?

> Y

Please enter the path to data:

> data

Index cleared.

Indexed: 5.txt

Indexed: 4.txt

Indexed: 3.txt

Indexed: 2.txt

Indexed: 1.txt

Indexing complete. Total files indexed: 5

Enter a search query (or type '!q' to quit):

> love

Found 3 hits hits:

- Hit: data/2.txt, (score: 0.27480933)
- Hit: data/1.txt, (score: 0.26752004)
- Hit: data/3.txt, (score: 0.19892561)

Enter a search query (or type '!q' to quit):

> truth

Found 1 hits hits:

- Hit: data/1.txt, (score: 0.9196625)

Enter a search query (or type '!q' to quit):

> dare

Found 4 hits hits:

- Hit: data/3.txt, (score: 0.155104)
- Hit: data/2.txt, (score: 0.14667575)
- Hit: data/1.txt, (score: 0.14278519)
- Hit: data/5.txt, (score: 0.13909568)

Enter a search query (or type '!q' to quit):

> lalala

No results found for 'lalala'. Check spelling or try different keywords.

Enter a search query (or type '!q' to quit):

> !q

Exiting program...


### Notes
- Ensure that the data directory contains text files (`*.txt`) for indexing.
- For advanced search queries or information on scoring, you can refer to Lucene's documentation.
