package ai.promoted

interface LogService {
    // TODO
    suspend fun sendLogs(logs: List<Any>)
}