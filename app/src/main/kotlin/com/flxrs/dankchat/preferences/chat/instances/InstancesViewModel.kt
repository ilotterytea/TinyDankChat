package com.flxrs.dankchat.preferences.chat.instances

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flxrs.dankchat.preferences.chat.ChatSettingsDataStore
import com.flxrs.dankchat.preferences.chat.CustomCommand
import com.flxrs.dankchat.preferences.chat.TinyInstance
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import kotlin.time.Duration.Companion.seconds

@KoinViewModel
class InstancesViewModel(private val chatSettingsDataStore: ChatSettingsDataStore) : ViewModel() {
    val instances = chatSettingsDataStore.settings
        .map { it.tinyInstances.toImmutableList() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5.seconds),
            initialValue = chatSettingsDataStore.current().tinyInstances.toImmutableList(),
        )

    fun save(instances: List<TinyInstance>) = viewModelScope.launch {
        val filtered = instances.filter { it.url.isNotBlank() }
        chatSettingsDataStore.update { it.copy(tinyInstances = filtered) }
    }
}
