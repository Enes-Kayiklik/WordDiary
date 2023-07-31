package com.eneskayiklik.word_diary.feature.word_list.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eneskayiklik.word_diary.R
import com.eneskayiklik.word_diary.core.util.components.FilterChip
import com.eneskayiklik.word_diary.feature.word_list.presentation.WordListFilterType
import com.eneskayiklik.word_diary.feature.word_list.presentation.WordListSortDirection
import com.eneskayiklik.word_diary.feature.word_list.presentation.WordListSortType

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterMenu(
    modifier: Modifier = Modifier,
    selectedFilterType: List<WordListFilterType>,
    selectedSortType: WordListSortType,
    selectedSortDirection: WordListSortDirection,
    onFilterSelected: (WordListFilterType) -> Unit,
    onSortSelected: (WordListSortType) -> Unit,
    onSortDirectionSelected: (WordListSortDirection) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(id = R.string.filter_by),
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WordListFilterType.values().forEach { item ->
                FilterChip(
                    isSelected = item in selectedFilterType,
                    content = {
                        Text(
                            text = stringResource(id = item.title),
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    onClick = { onFilterSelected(item) },
                    modifier = Modifier.clip(MaterialTheme.shapes.small)
                )
            }
        }

        Text(
            text = stringResource(id = R.string.sort_by),
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WordListSortType.values().forEach { item ->
                FilterChip(
                    isSelected = item == selectedSortType,
                    content = {
                        Text(
                            text = stringResource(id = item.title),
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    onClick = { onSortSelected(item) },
                    modifier = Modifier.clip(MaterialTheme.shapes.small)
                )
            }
        }

        Text(
            text = stringResource(id = R.string.sort_direction),
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WordListSortDirection.values().forEach { item ->
                FilterChip(
                    isSelected = item == selectedSortDirection,
                    content = {
                        Text(
                            text = stringResource(id = item.title),
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    onClick = { onSortDirectionSelected(item) },
                    modifier = Modifier.clip(MaterialTheme.shapes.small)
                )
            }
        }
    }
}