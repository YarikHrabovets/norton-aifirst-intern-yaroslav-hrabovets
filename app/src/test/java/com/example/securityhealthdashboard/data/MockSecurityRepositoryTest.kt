package com.example.securityhealthdashboard.data

import com.example.securityhealthdashboard.data.repository.MockSecurityRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class MockSecurityRepositoryTest {

    @Test
    fun `fetchSecurityReport returns expected data`() = runTest {
        val repository = MockSecurityRepository()
        val report = repository.fetchSecurityReport()
        
        assertNotNull(report)
        assertEquals(0, report.overallScore)
        assertEquals(6, report.categories.size)
        assertEquals(0, report.recommendations.size)
    }
}
