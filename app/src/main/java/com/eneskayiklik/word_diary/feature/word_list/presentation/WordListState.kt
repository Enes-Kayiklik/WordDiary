package com.eneskayiklik.word_diary.feature.word_list.presentation

import androidx.annotation.StringRes
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.data_store.data.SwipeAction
import com.eneskayiklik.word_diary.core.database.entity.LearnState
import com.eneskayiklik.word_diary.core.database.model.FolderWithWords
import com.eneskayiklik.word_diary.util.extensions.filterIf
import com.google.android.gms.ads.nativead.NativeAd

data class WordListState(
    val folder: FolderWithWords? = null,
    val initialWordIndex: Int = -1,
    val listStatistic: ListStatistic = ListStatistic(),
    val filterType: List<WordListFilterType> = listOf(WordListFilterType.NewWord),
    val sortType: WordListSortType = WordListSortType.DateAdded,
    val sortDirection: WordListSortDirection = WordListSortDirection.Desc,
    val swipeAction: SwipeAction = SwipeAction.SPEECH_LOUD,
    val dialogType: WordListDialogType = WordListDialogType.NONE,
    val nativeAd: NativeAd? = null
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

enum class WordListFilterType(@StringRes val title: Int, val learnState: LearnState?) {
    Learned(R.string.filter_by_learned, LearnState.Learned),
    NewWord(R.string.filter_by_new_word, LearnState.NewWord),
    Learning(R.string.filter_by_learning, LearnState.Learning),
    Favorite(R.string.filter_by_favorite, null)
}

enum class WordListSortType(@StringRes val title: Int) {
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
    var filtered = words
        .filterIf(
            filter = { filterType.contains(WordListFilterType.Favorite) },
            predicate = { it.isFavorite }
        )/*.filterIf(
            filter = { (filterType.size == 1 && filterType[0] == WordListFilterType.Favorite).not() },
            predicate = { it.state in filterType.map { f -> f.learnState } }
        )*/

    filtered = if (sortDirection == WordListSortDirection.Asc) {
        when (sortType) {
            WordListSortType.DateAdded -> filtered.sortedBy { it.addedDate }
            WordListSortType.LastStudied -> filtered.sortedBy { it.lastStudyDate }
            WordListSortType.StudyCount -> filtered.sortedBy { it.studyCount }
        }
    } else {
        when (sortType) {
            WordListSortType.DateAdded -> filtered.sortedByDescending { it.addedDate }
            WordListSortType.LastStudied -> filtered.sortedByDescending { it.lastStudyDate }
            WordListSortType.StudyCount -> filtered.sortedByDescending { it.studyCount }
        }
    }

    return copy(
        words = filtered
    )
}