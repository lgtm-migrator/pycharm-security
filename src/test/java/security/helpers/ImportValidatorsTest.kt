package security.helpers

import com.intellij.openapi.application.ApplicationManager
import com.jetbrains.python.PythonFileType
import com.jetbrains.python.psi.PyFile
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import security.SecurityTestTask

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ImportValidatorsTest: SecurityTestTask() {

    @BeforeAll
    override fun setUp() {
        super.setUp()
    }

    @AfterAll
    override fun tearDown(){
        super.tearDown()
    }

    @Test
    fun `test empty file`(){
        val code = """
            
        """.trimIndent()
        assertFalse(testHasImport(code, "django"))
    }

    @Test
    fun `test file with no imports`(){
        val code = """
            x = 1
        """.trimIndent()
        assertFalse(testHasImport(code, "django"))
    }

    @Test
    fun `test simple import`(){
        val code = """
            import django
        """.trimIndent()
        assertTrue(testHasImport(code, "django"))
    }

    @Test
    fun `test dotted import`(){
        val code = """
            import ..
        """.trimIndent()
        assertFalse(testHasImport(code, "django"))
    }

    @Test
    fun `test from import`(){
        val code = """
            from django.db import DbConnection
        """.trimIndent()
        assertTrue(testHasImport(code, "django"))
    }

    @Test
    fun `test from import multi`(){
        val code = """
            from django.db import DbConnection
        """.trimIndent()
        assertTrue(testHasImport(code, "django.db"))
    }

    @Test
    fun `test multiple import`(){
        val code = """
            import banana
            import apple
        """.trimIndent()
        assertTrue(testHasImport(code, "apple"))
    }

    @Test
    fun `test multiple import dotted`(){
        val code = """
            import banana
            import fruit.apple
        """.trimIndent()
        assertTrue(testHasImport(code, "fruit"))
    }

    @Test
    fun `test multiple import dotted not exist`(){
        val code = """
            import banana
            import fruit.apple
        """.trimIndent()
        assertFalse(testHasImport(code, "pear"))
    }

    private fun testHasImport(code: String, importName: String): Boolean{
        var hasImport: Boolean = false
        ApplicationManager.getApplication().runReadAction {
            val testFile = this.createLightFile("test.py", PythonFileType.INSTANCE.language, code)
            assertNotNull(testFile)
            hasImport = hasImportedNamespace(testFile as PyFile, importName)
        }
        return hasImport
    }
}