package com.example.nyc_school.vm

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.nyc_school.data.impl.RepositoryImpl
import com.example.nyc_school.data.network.response.ApiResponse
import com.example.nyc_school.data.network.response.ErrorCode
import com.example.nyc_school.data.network.response.ResponseStatus
import com.example.nyc_school.utils.TestUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class SchoolViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher = StandardTestDispatcher()

    @Mock
    lateinit var repo: RepositoryImpl

    lateinit var viewModel: SchoolViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)

        viewModel = SchoolViewModel(repo)
    }

    @After
    fun tearDown() {
        Mockito.clearAllCaches()
    }

    @Test
    fun getSchoolInfo_Success() = runTest {
        val value = ApiResponse(
            status = ResponseStatus(code = ErrorCode.SUCCESS),
            content = TestUtils.schools
        )

        Mockito.`when`(repo.getSchoolInfo()).thenReturn(value)

        viewModel.getSchoolInfo()

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(repo.getSchoolInfo(), viewModel.liveData.value)
    }

    @Test
    fun getSchoolInfo_Empty() = runTest {
        val value = ApiResponse(
            status = ResponseStatus(code = ErrorCode.SUCCESS),
            content = emptyList()
        )

        Mockito.`when`(repo.getSchoolInfo()).thenReturn(value)

        viewModel.getSchoolInfo()

        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(0, viewModel.liveData.value?.content?.size)
    }
}