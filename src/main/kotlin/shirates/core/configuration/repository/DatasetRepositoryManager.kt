package shirates.core.configuration.repository

import org.json.JSONObject
import shirates.core.utility.misc.JsonUtility
import shirates.core.utility.toPath

/**
 * DatasetRepositoryManager
 */
object DatasetRepositoryManager {

    /**
     * repositories
     */
    val repositories = mutableMapOf<String, DatasetRepository>()

    /**
     * clear
     */
    fun clear() {

        repositories.clear()
    }

    /**
     * loadFromFile
     */
    fun loadFromFile(file: String) {

        val repositoryJso = JsonUtility.getJSONObjectFromFile(file = file)
        val repositoryName = file.toPath().toFile().nameWithoutExtension
        setRepository(repositoryName = repositoryName, jsonObject = repositoryJso)
    }

    /**
     * getRepository
     */
    fun getRepository(repositoryName: String): DatasetRepository {

        if (repositories.containsKey(repositoryName).not()) {
            throw IllegalAccessError("repository not found. (repositoryName=$repositoryName)")
        }

        return repositories[repositoryName]!!
    }

    /**
     * setRepository
     */
    fun setRepository(repositoryName: String, jsonObject: JSONObject) {

        val repo = DatasetRepository(repositoryName = repositoryName, jsonObject = jsonObject)
        setRepository(repository = repo)
    }

    /**
     * setRepository
     */
    fun setRepository(repository: DatasetRepository) {

        repositories[repository.repositoryName] = repository
    }

    /**
     * getValue
     */
    fun getValue(repositoryName: String, longKey: String): String {

        if (repositories.containsKey(repositoryName).not()) {
            throw IllegalAccessError("Repository not found. (repositoryName=$repositoryName)")
        }

        val repository = getRepository(repositoryName)
        return repository.getValue(longKey = longKey)
    }
}