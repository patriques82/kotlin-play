package K9dsl

// Project

fun createProject(init: Project.() -> Unit): Project =
    Project().apply(init)

class Project {
    var group: String = ""
    var version: String = ""
    val repositories = Repositories(mutableListOf())
    val dependencies = Dependencies(mutableListOf())

    fun repositories(init: Repositories.() -> Unit) {  // Lambda with receiver
        repositories.init()
    }

    fun dependencies(init: Dependencies.() -> Unit) {
        dependencies.init()
    }
}

// Repositories

open class Repository(val name: String)

class Maven: Repository("Maven")

class Repositories(private val repositories: MutableList<Repository>) :
    Iterable<Repository> by repositories {
    fun mavenCentral() {
        repositories += Maven()
    }
}

// Dependencies

open class Dependency(val name: String)

class KotlinDependency(library: String) : Dependency("kotlin:$library")

class FileDependency(file: String) : Dependency("/path-to-project/:$file")

class Dependencies(private val dependencies: MutableList<Dependency>) :
    Iterable<Dependency> by dependencies {

    fun compile(dependency: Dependency) {
        dependencies += dependency
    }

    operator fun invoke(body: Dependencies.() -> Unit) = body() // invoke operator
}

fun kotlin(library: String): Dependency = KotlinDependency(library)

fun files(file: String): Dependency = FileDependency(file)



fun main(args: Array<String>) {

    val project = createProject {
        group = "kotlin-play"
        version = "1.0-SNAPSHOT"
        repositories {
            mavenCentral()
        }
        dependencies {
            compile(kotlin("stdlib-jdk8"))
            compile(kotlin("reflect"))
        }
        dependencies.compile(files("libs/json-lib-2.4-jdk15.jar"))
    }

    project.dependencies.forEach { println(it.name) }
    // kotlin:stdlib-jdk8
    // kotlin:reflect
    // path-to-project/:libs/json-lib-2.4-jdk15.jar
    project.repositories.forEach { println(it.name) }
    // Maven
}