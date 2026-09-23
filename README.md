# DoubleDoors

Opens adjacent doors and trapdoors of the same type!

## Features

- Opens adjacent doors and trapdoors of the same type on player interaction.
- Powers adjacent doors and trapdoors of the type on redstone power change.
- Fires events for compatibility with protection plugins.

## Compiling

Prerequisites: JDK 11 or higher, Apache Maven.

Then run `mvn package` to build the plugin .jar file.

## API

Releases are published on GitHub:
```xml
<repositories>
    <!-- other repositories... -->
    <repository>
        <id>github-jannyboy11-doubledoors-repo</id>
        <url>https://maven.pkg.github.com/Jannyboy11/DoubleDoors</url>
    </repository>
</repositories>
```

Snapshots are published on Repsy:
```xml
<repositories>
    <!-- other repositories... -->
    <repository>
        <id>jannyboy11-repsy</id>
        <url>https://repo.repsy.io/mvn/jannyboy11/minecraft</url>
    </repository>
</repositories>
```

Then use the following maven dependency:
```xml
<dependency>
    <groupId>com.janboerman.doubledoors</groupId>
    <artifactId>double-doors</artifactId>
    <version>1.3-SNAPSHOT</version> <!-- replace with latest version. -->
</dependency>
```

[Javadocs](https://jannyboy11.github.io/DoubleDoors/javadoc/) are available on GitHub pages.

## Releasing

- Run `mvn release:prepare -DignoreSnapshots` and fill in the correct version numbers.
- Run `mvn release:perform` and `git push`.
