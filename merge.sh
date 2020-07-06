#!/bin/bash
./out/file-merger.sh ./src/main/kotlin/lifegame kt

java -jar ./out/importFixer.jar out/Out.kt lifegame.container,lifegame.util