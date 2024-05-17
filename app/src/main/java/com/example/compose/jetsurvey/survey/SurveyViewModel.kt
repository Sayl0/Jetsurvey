/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.compose.jetsurvey.survey

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.compose.jetsurvey.survey.question.Superhero

const val simpleDateFormatPattern = "EEE, MMM d"

class SurveyViewModel(
    private val photoUriManager: PhotoUriManager
) : ViewModel() {

    private val questionOrder: List<SurveyQuestion> = listOf(
        SurveyQuestion.FEELING_ABOUT_SELFIES,
        SurveyQuestion.FREE_TIME,
        SurveyQuestion.FREE_NEXT_TIME,
        SurveyQuestion.FREE_NEXT2_TIME,
        SurveyQuestion.FREE_NEXT3_TIME,
        SurveyQuestion.FREE_NEXT4_TIME,
//        SurveyQuestion.SUPERHERO,
//        SurveyQuestion.LAST_TAKEAWAY,
//        SurveyQuestion.TAKE_SELFIE,
    )

    private var questionIndex = 0

    // ----- Responses exposed as State -----

    private val _freeTimeResponse = mutableStateListOf<Int>()
    val freeTimeResponse: List<Int>
        get() = _freeTimeResponse

    private val _nextFreeTimeResponse = mutableStateListOf<Int>()
    val nextFreeTimeResponse: List<Int>
        get() = _nextFreeTimeResponse

    private val _next2FreeTimeResponse = mutableStateListOf<Int>()
    val next2FreeTimeResponse: List<Int>
        get() = _next2FreeTimeResponse

    private val _next3FreeTimeResponse = mutableStateListOf<Int>()
    val next3FreeTimeResponse: List<Int>
        get() = _next3FreeTimeResponse

    private val _next4FreeTimeResponse = mutableStateListOf<Int>()
    val next4FreeTimeResponse: List<Int>
        get() = _next4FreeTimeResponse


//    private val _superheroResponse = mutableStateOf<Superhero?>(null)
//    val superheroResponse: Superhero?
//        get() = _superheroResponse.value
//
//    private val _takeawayResponse = mutableStateOf<Long?>(null)
//    val takeawayResponse: Long?
//        get() = _takeawayResponse.value

    private val _feelingAboutSelfiesResponse = mutableStateOf<Float?>(null)
    val feelingAboutSelfiesResponse: Float?
        get() = _feelingAboutSelfiesResponse.value

//    private val _selfieUri = mutableStateOf<Uri?>(null)
//    val selfieUri
//        get() = _selfieUri.value

    // ----- Survey status exposed as State -----

    private val _surveyScreenData = mutableStateOf(createSurveyScreenData())
    val surveyScreenData: SurveyScreenData?
        get() = _surveyScreenData.value

    private val _isNextEnabled = mutableStateOf(false)
    val isNextEnabled: Boolean
        get() = _isNextEnabled.value

    /**
     * Returns true if the ViewModel handled the back press (i.e., it went back one question)
     */
    fun onBackPressed(): Boolean {
        if (questionIndex == 0) {
            return false
        }
        changeQuestion(questionIndex - 1)
        return true
    }

    fun onPreviousPressed() {
        if (questionIndex == 0) {
            throw IllegalStateException("onPreviousPressed when on question 0")
        }
        changeQuestion(questionIndex - 1)
    }

    fun onNextPressed() {
        changeQuestion(questionIndex + 1)
    }

    private fun changeQuestion(newQuestionIndex: Int) {
        questionIndex = newQuestionIndex
        _isNextEnabled.value = getIsNextEnabled()
        _surveyScreenData.value = createSurveyScreenData()
    }

    fun onDonePressed(onSurveyComplete: () -> Unit) {
        // Here is where you could validate that the requirements of the survey are complete
        onSurveyComplete()
    }

    fun onFreeTimeResponse(selected: Boolean, answer: Int) {
        if (selected) {
            _freeTimeResponse.add(answer)
        } else {
            _freeTimeResponse.remove(answer)
        }
        _isNextEnabled.value = getIsNextEnabled()
    }

    fun onNextFreeTimeResponse(selected: Boolean, answer: Int) {
        if (selected) {
            _nextFreeTimeResponse.add(answer)
        } else {
            _nextFreeTimeResponse.remove(answer)
        }
        _isNextEnabled.value = getIsNextEnabled()
    }

    fun onNext2FreeTimeResponse(selected: Boolean, answer: Int) {
        if (selected) {
            _next2FreeTimeResponse.add(answer)
        } else {
            _next2FreeTimeResponse.remove(answer)
        }
        _isNextEnabled.value = getIsNextEnabled()
    }

    fun onNext3FreeTimeResponse(selected: Boolean, answer: Int) {
        if (selected) {
            _next3FreeTimeResponse.add(answer)
        } else {
            _next3FreeTimeResponse.remove(answer)
        }
        _isNextEnabled.value = getIsNextEnabled()
    }

    fun onNext4FreeTimeResponse(selected: Boolean, answer: Int) {
        if (selected) {
            _next4FreeTimeResponse.add(answer)
        } else {
            _next4FreeTimeResponse.remove(answer)
        }
        _isNextEnabled.value = getIsNextEnabled()
    }

//    fun onSuperheroResponse(superhero: Superhero) {
//        _superheroResponse.value = superhero
//        _isNextEnabled.value = getIsNextEnabled()
//    }
//
//    fun onTakeawayResponse(timestamp: Long) {
//        _takeawayResponse.value = timestamp
//        _isNextEnabled.value = getIsNextEnabled()
//    }

    fun onFeelingAboutSelfiesResponse(feeling: Float) {
        _feelingAboutSelfiesResponse.value = feeling
        _isNextEnabled.value = getIsNextEnabled()
    }

//    fun onSelfieResponse(uri: Uri) {
//        _selfieUri.value = uri
//        _isNextEnabled.value = getIsNextEnabled()
//    }

    fun getNewSelfieUri() = photoUriManager.buildNewUri()

    private fun getIsNextEnabled(): Boolean {
        return when (questionOrder[questionIndex]) {
            SurveyQuestion.FEELING_ABOUT_SELFIES -> _feelingAboutSelfiesResponse.value != null
            SurveyQuestion.FREE_TIME -> _freeTimeResponse.isNotEmpty()
            SurveyQuestion.FREE_NEXT_TIME -> _freeTimeResponse.isNotEmpty()
            SurveyQuestion.FREE_NEXT2_TIME -> _freeTimeResponse.isNotEmpty()
            SurveyQuestion.FREE_NEXT3_TIME -> _freeTimeResponse.isNotEmpty()
            SurveyQuestion.FREE_NEXT4_TIME -> _freeTimeResponse.isNotEmpty()
//            SurveyQuestion.SUPERHERO -> _superheroResponse.value != null
//            SurveyQuestion.LAST_TAKEAWAY -> _takeawayResponse.value != null

//            SurveyQuestion.TAKE_SELFIE -> _selfieUri.value != null
        }
    }

    private fun createSurveyScreenData(): SurveyScreenData {
        return SurveyScreenData(
            questionIndex = questionIndex,
            questionCount = questionOrder.size,
            shouldShowPreviousButton = questionIndex > 0,
            shouldShowDoneButton = questionIndex == questionOrder.size - 1,
            surveyQuestion = questionOrder[questionIndex],
        )
    }
}

class SurveyViewModelFactory(
    private val photoUriManager: PhotoUriManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SurveyViewModel::class.java)) {
            return SurveyViewModel(photoUriManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

enum class SurveyQuestion {
    FREE_TIME,
    FREE_NEXT_TIME,
    FREE_NEXT2_TIME,
    FREE_NEXT3_TIME,
    FREE_NEXT4_TIME,
//    SUPERHERO,
//    LAST_TAKEAWAY,
    FEELING_ABOUT_SELFIES,
//    TAKE_SELFIE,
}

data class SurveyScreenData(
    val questionIndex: Int,
    val questionCount: Int,
    val shouldShowPreviousButton: Boolean,
    val shouldShowDoneButton: Boolean,
    val surveyQuestion: SurveyQuestion,
)
