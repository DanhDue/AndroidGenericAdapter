package com.danhdueexoictif.androidgenericadapter.ui.screen.sample

import com.danhdueexoictif.androidgenericadapter.BaseViewModelTest
import com.danhdueexoictif.androidgenericadapter.MainCoroutineRule
import com.danhdueexoictif.androidgenericadapter.R
import com.danhdueexoictif.androidgenericadapter.data.bean.NewBieObject
import com.danhdueexoictif.androidgenericadapter.data.remote.NetworkResponse
import com.danhdueexoictif.androidgenericadapter.data.remote.NoConnectivityException
import com.danhdueexoictif.androidgenericadapter.data.remote.invoke
import com.danhdueexoictif.androidgenericadapter.data.remote.response.HttpResponseCode
import com.danhdueexoictif.androidgenericadapter.data.remote.response.NewBieResObject
import com.danhdueexoictif.androidgenericadapter.data.repository.SampleRepository
import com.danhdueexoictif.androidgenericadapter.runBlocking
import com.danhdueexoictif.androidgenericadapter.utils.Constants
import io.kotlintest.shouldBe
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class SampleViewModelTest : BaseViewModelTest<SampleViewModel>() {

    // Set the main coroutines dispatcher for unit testing
    @get:Rule
    var coroutinesRule = MainCoroutineRule()

    @MockK
    private lateinit var sampleRepo: SampleRepository

    override fun setup() {
        super.setup()
        every { sampleRepo.getNewbies(Constants.DEFAULT_FIRST_PAGE) } returns CompletableDeferred()
        viewModel = spyk(SampleViewModel(sampleRepo))
    }

    /**
     * Implement: test load newbie with coroutine
     * Condition: call load newbie with coroutine
     * Expect result: retrieve an NetworkResponse
     */
    @Test
    fun should_doLoadNewBieWithCoroutine() {
        // Given
        val expectedNewbieList = NewBieResObject(listOf(NewBieObject(1001)))
        every { sampleRepo.getNewbies(Constants.DEFAULT_FIRST_PAGE) } returns CompletableDeferred(
            NetworkResponse.Success(expectedNewbieList)
        )
        // When
        val newbieList = runBlocking { sampleRepo.getNewbies(Constants.DEFAULT_FIRST_PAGE).await() }
        // Then
        newbieList() shouldBe expectedNewbieList
    }

    /**
     * Implement: test load newbies successfully.
     * Condition: Call function to loadData => return NetworkResponse.Success
     * Expect result: isLoadSuccess.value = true
     */
    @Test
    fun loadData_ifSuccess() = coroutinesRule.runBlocking {
        // Given
        every { sampleRepo.getNewbies(Constants.DEFAULT_FIRST_PAGE) } returns CompletableDeferred(
            NetworkResponse.Success(NewBieResObject(listOf(NewBieObject(1001))))
        )
        // When
        viewModel.loadData(Constants.DEFAULT_FIRST_PAGE)
        // Then
        assertEquals(true, viewModel.isLoadSuccess.value)
        assertEquals(
            R.layout.item_first_newbie,
            (viewModel.listItem.value?.get(0) as NewBieObject).layoutId
        )
        assertEquals(1001, (viewModel.listItem.value?.get(0) as NewBieObject).id)
    }

    /**
     * Implement: test load newbies when do not have a connection.
     * Condition: Call function to loadData => return NetworkResponse.NetworkError(NoConnectivityException())
     * Expect result: isLoadSuccess.value = false, viewModel.noInternetConnectionEvent.value = "No Internet Connection."
     */
    @Test
    fun loadData_ifDoNotHaveConnection() = coroutinesRule.runBlocking {
        // Given
        every { sampleRepo.getNewbies(Constants.DEFAULT_FIRST_PAGE) } returns CompletableDeferred(
            NetworkResponse.NetworkError(NoConnectivityException())
        )
        // When
        viewModel.loadData(Constants.DEFAULT_FIRST_PAGE)
        // Then
        assertEquals(false, viewModel.isLoadSuccess.value)
        assertEquals(false, viewModel.isLoadMore)
        assertEquals("No Internet Connection.", viewModel.noInternetConnectionEvent.value)
    }

    /**
     * Implement: test load newbie when have a server error.
     * Condition: call function to load data => return HttpResponseCode.HTTP_NOT_FOUND
     * Expect result: newbieList is NetworkResponse.ServerError and its error code is HttpResponseCode.HTTP_NOT_FOUND
     */
    @Test
    fun loadData_ifHaveServerError() {
        // Given
        every { sampleRepo.getNewbies(Constants.DEFAULT_FIRST_PAGE) } returns CompletableDeferred(
            NetworkResponse.ServerError("", HttpResponseCode.HTTP_NOT_FOUND, null)
        )
        // When
        val newbieList = runBlocking { sampleRepo.getNewbies(Constants.DEFAULT_FIRST_PAGE).await() }
        viewModel.loadData(Constants.DEFAULT_FIRST_PAGE)
        // Then
        assertEquals(true, viewModel.isLoadFail.value)
        assertEquals(false, viewModel.isLoadSuccess.value)
        assertEquals(
            HttpResponseCode.HTTP_NOT_FOUND,
            (newbieList as NetworkResponse.ServerError).code
        )
    }
}