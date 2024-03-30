package com.seoulventure.melonbox.domain

import com.seoulventure.melonbox.data.RemoteConfigDataSource
import javax.inject.Inject

class GetTutorialUrlUseCase @Inject constructor(
    private val remoteConfigDataSource: RemoteConfigDataSource
) {

    operator fun invoke(): String {
        return remoteConfigDataSource.getTutorialUrl()
    }
}
