package ai.promoted.metrics.usecases

import ai.promoted.internal.Clock
import ai.promoted.metrics.AbstractContent
import ai.promoted.metrics.ImpressionData
import ai.promoted.metrics.MetricsLogger

/**
 * This class should be retained as a singleton to ensure the collection view keys are properly
 * tracked across their lifecycle.
 */
internal class TrackImpressionsUseCase(
    private val clock: Clock,
    private val logger: MetricsLogger,
    private val sessionUseCase: TrackSessionUseCase,
    private val viewUseCase: TrackViewUseCase,
    private val impressionIdProvider: ImpressionIdGenerator
) {
    private val collectionsAndImpressions = mutableMapOf<String, List<ImpressionData>>()

    fun onCollectionVisible(collectionViewKey: String, content: List<AbstractContent>) {
        val identifiableContent

        content
            .asSequence()
            .map {
                it to impressionIdProvider.generateImpressionId(it.insertionId, it.contentId)
            }
            .mapNotNull {
                it.second?.let { ImpressionData()}
            }
            .filter {

            }


        /*
            New impressions are determined by:
            - Finding AbstractContent that did not have an impression recorded on the last call to
            onCollectionVisible()
            - For each of those new pieces of content, ensuring an impression ID can be generated
            (it has either a content ID or insertion ID)
         */
        val newImpressions = getNewImpressions(collectionViewKey, content)

        newImpressions.forEach { impression ->
            logger.enqueueMessage(
                createImpressionMessage(impression)
            )
        }

        // Store the new list of impressions
        collectionsAndImpressions[collectionViewKey] = newImpressions
    }

    fun onCollectionHidden(collectionViewKey: String) {
        collectionsAndImpressions.remove(collectionViewKey)
    }

    private fun getNewImpressions(
        collectionViewKey: String,
        content: List<AbstractContent>
    ): List<ImpressionData> {
        // Filter out any content that was already logged as an impression on the last call
        val notYetSeenContent = filterAlreadySeenContent(collectionViewKey, content)
        if (notYetSeenContent.isEmpty()) return emptyList()

        // Filter out any content for which we cannot generate an impression ID
        val now = clock.currentTimeMillis
        val sessionId = sessionUseCase.sessionId
        val viewId = viewUseCase.viewId
        return mapContentToIdentifiableImpressions(now, sessionId, viewId, content)
    }

    private fun filterAlreadySeenContent(
        collectionViewKey: String,
        latestCollectionContent: List<AbstractContent>
    ) =
        latestCollectionContent.filter { visibleContent ->
            val alreadyImpressed =
                collectionsAndImpressions[collectionViewKey]?.any { previousImpression ->
                    previousImpression.originalContent == visibleContent
                } ?: false

            return@filter !alreadyImpressed
        }

    private fun mapContentToIdentifiableImpressions(
        impressionTime: Long,
        sessionId: String,
        viewId: String,
        content: List<AbstractContent>
    ) = content.mapNotNull { newContent ->
        impressionIdProvider
            .generateImpressionId(
                newContent.insertionId,
                newContent.contentId
            )?.let { impressionId ->
                ImpressionData(
                    originalContent = newContent,
                    time = impressionTime,
                    sessionId = sessionId,
                    viewId = viewId,
                    impressionId = impressionId,
                    // TODO
                    requestId = null
                )
            }
    }
}

/*
  private func broadcastStartAndAddImpressions(contentArray: [Content], now: TimeInterval) {
    guard !contentArray.isEmpty else { return }
    var impressions = [Impression]()
    for content in contentArray {
      let impression = Impression(content: content, startTime: now, endTime: nil)
      impressions.append(impression)
      impressionStarts[content] = now
    }
    monitor.execute {
      for impression in impressions {
        metricsLogger.logImpression(content: impression.content)
      }
    }
    delegate?.impressionLogger(self, didStartImpressions: impressions)
  }

    /// Call this method when the collection view changes content, but
  /// does not provide per-item updates for the change. For example,
  /// when a collection reloads.
  @objc(collectionViewDidChangeVisibleContent:)
  public func collectionViewDidChangeVisibleContent(_ contentArray: [Content]) {
    monitor.execute {
      let now = clock.now

      var newlyShownContent = [Content]()
      for content in contentArray {
        if impressionStarts[content] == nil {
          newlyShownContent.append(content)
        }
      }

      var newlyHiddenContent = [Content]()
      for content in impressionStarts.keys {
        if !contentArray.contains(content) {
          newlyHiddenContent.append(content)
        }
      }

      broadcastStartAndAddImpressions(contentArray: newlyShownContent, now: now)
      broadcastEndAndRemoveImpressions(contentArray: newlyHiddenContent, now: now)
    }
  }

  private func broadcastEndAndRemoveImpressions(contentArray: [Content], now: TimeInterval) {
    guard !contentArray.isEmpty else { return }
    var impressions = [Impression]()
    for content in contentArray {
      guard let start = impressionStarts.removeValue(forKey: content) else { continue }
      let impression = Impression(content: content, startTime: start, endTime: now)
      impressions.append(impression)
    }
    // Not calling `metricsLogger`. No logging end impressions for now.
    delegate?.impressionLogger(self, didEndImpressions: impressions)
  }
}
 */