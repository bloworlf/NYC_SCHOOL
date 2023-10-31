package com.example.nyc_school.data.impl

import com.example.nyc_school.data.network.ApiCall
import com.example.nyc_school.data.network.response.ErrorCode
import com.example.nyc_school.data.repo.Repository
import com.example.nyc_school.utils.TestUtils
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class RepositoryImplTest {

    lateinit var repo: Repository

    @Mock
    lateinit var apiCall: ApiCall

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        repo = RepositoryImpl(apiCall)
    }

    @After
    fun tearDown() {
        Mockito.clearAllCaches()
    }

    @Test
    fun getSchoolInfo_Success() = runTest {
        Mockito.`when`(apiCall.getSchoolInfo()).thenReturn(TestUtils.schools)

        val response = repo.getSchoolInfo()
        assertTrue(response.status.code == ErrorCode.SUCCESS)
        assertNotNull(response.content)
        assertEquals(response.content?.size, 3)
    }

    @Test
    fun getSchoolInfo_Empty() = runTest {
        Mockito.`when`(apiCall.getSchoolInfo()).thenReturn(emptyList())

        val response = repo.getSchoolInfo()
        assertTrue(response.status.code == ErrorCode.SUCCESS)
        assertNotNull(response.content)
        assertEquals(response.content?.size, 0)
    }

    @Test
    fun getSchoolInfo_Error() = runTest {
        Mockito.`when`(apiCall.getSchoolInfo()).thenThrow(RuntimeException("Exception is thrown"))

        val response = repo.getSchoolInfo()
        assertTrue(response.status.code != ErrorCode.SUCCESS)
        assertFalse(response.status.message.isNullOrEmpty())
        assertNull(response.content)
    }

//    @Test
//    fun getByDBN() {
//    }
//
//    @Test
//    fun getByName() {
//    }
}