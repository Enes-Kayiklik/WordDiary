package com.eneskayiklik.word_diary.feature.word_list.presentation

import androidx.annotation.StringRes
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.ad_manager.WordbookAd
import com.eneskayiklik.word_diary.core.data_store.data.SwipeAction
import com.eneskayiklik.word_diary.core.database.model.FolderWithWords
import com.eneskayiklik.word_diary.util.extensions.filterIf

data class WordListState(
    val folder: FolderWithWords? = null,
    val initialWordIndex: Int = -1,
    val listStatistic: ListStatistic = ListStatistic(),
    val filterType: List<WordListFilterType> = listOf(
        WordListFilterType.Beginner,
        WordListFilterType.Intermediate,
        WordListFilterType.Advanced,
        WordListFilterType.Expert,
        WordListFilterType.Master
    ),
    val sortType: WordListSortType = WordListSortType.DateAdded,
    val sortDirection: WordListSortDirection = WordListSortDirection.Desc,
    val swipeAction: SwipeAction = SwipeAction.SPEECH_LOUD,
    val dialogType: WordListDialogType = WordListDialogType.NONE,
    val nativeAd: WordbookAd? = null
) {
    val showEmptyLayout = folder?.words.isNullOrEmpty()
    val isLoading = folder == null

    val title = folder?.folder?.title ?: ""
    val words = folder?.words ?: emptyList()
    val isDialogActive = dialogType != WordListDialogType.NONE
    val isWordQueueVisible = initialWordIndex >= 0

    val userLangIso = folder?.folder?.userLangCode
    val folderLangIso = folder?.folder?.folderLangCode
}

data class ListStatistic(
    val progress: Float = 0F,
    val percentage: Int = 0,
    val totalWords: Int = 0,
    val masteredWords: Int = 0
)

enum class WordListFilterType(@StringRes val title: Int, val proficiency: IntRange?) {
    Beginner(R.string.beginner, 0..24),
    Intermediate(R.string.intermediate, 25..49),
    Advanced(R.string.advanced, 50..74),
    Expert(R.string.expert, 75..99),
    Master(R.string.master, 100..100),
    Favorite(R.string.favorite, null)
}

enum class WordListSortType(@StringRes val title: Int) {
    Proficiency(R.string.sort_by_proficiency),
    DateAdded(R.string.sort_by_date_added),
    LastStudied(R.string.sort_by_last_studied),
    StudyCount(R.string.sort_by_study_count)
}

enum class WordListSortDirection(@StringRes val title: Int) {
    Asc(R.string.sort_direction_ascending),
    Desc(R.string.sort_direction_descending),
}

enum class WordListDialogType {
    NONE,
    DELETE,
    DELETE_LIST
}

fun FolderWithWords.filterWords(
    filterType: List<WordListFilterType>,
    sortType: WordListSortType,
    sortDirection: WordListSortDirection
): FolderWithWords {
    val ranges = filterType.mapNotNull { it.proficiency }

    var filtered = words
        .filterIf(
            filter = { filterType.contains(WordListFilterType.Favorite) },
            predicate = { it.isFavorite }
        ).filterIf(
            filter = { ranges.isNotEmpty() },
            predicate = { word -> ranges.any { word.proficiency.toInt() in it } }
        )

    filtered = if (sortDirection == WordListSortDirection.Asc) {
        when (sortType) {
            WordListSortType.DateAdded -> filtered.sortedBy { it.addedDate }
            WordListSortType.LastStudied -> filtered.sortedBy { it.lastStudyDate }
            WordListSortType.StudyCount -> filtered.sortedBy { it.studyCount }
            WordListSortType.Proficiency -> filtered.sortedBy { it.proficiency }
        }
    } else {
        when (sortType) {
            WordListSortType.DateAdded -> filtered.sortedByDescending { it.addedDate }
            WordListSortType.LastStudied -> filtered.sortedByDescending { it.lastStudyDate }
            WordListSortType.StudyCount -> filtered.sortedByDescending { it.studyCount }
            WordListSortType.Proficiency -> filtered.sortedByDescending { it.proficiency }
        }
    }

    return copy(
        words = filtered
    )
}