package security.validators

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import security.Checks
import security.SecurityTestTask

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PyyamlLoadInspectionTest: SecurityTestTask() {
    @BeforeAll
    override fun setUp() {
        super.setUp()
    }

    @AfterAll
    override fun tearDown(){
        super.tearDown()
    }

    @Test
    fun `verify description is not empty`(){
        assertFalse(PyyamlLoadInspection().staticDescription.isNullOrEmpty())
    }

    @Test
    fun `test yaml load`(){
        val code = """
            import yaml
            yaml.load()
        """.trimIndent()
        testCodeCallExpression(code, 1, Checks.PyyamlUnsafeLoadCheck, "test.py", PyyamlLoadInspection())
    }

    @Test
    fun `test yaml load with args`(){
        val code = """
            import yaml
            yaml.load(f)
        """.trimIndent()
        testCodeCallExpression(code, 1, Checks.PyyamlUnsafeLoadCheck, "test.py", PyyamlLoadInspection())
    }

    @Test
    fun `test not yaml qn load`(){
        val code = """
            import yaml
            waml.load()
        """.trimIndent()
        testCodeCallExpression(code, 0, Checks.PyyamlUnsafeLoadCheck, "test.py", PyyamlLoadInspection())
    }

    @Test
    fun `test yaml not load`(){
        val code = """
            import yaml
            yaml.loooad()
        """.trimIndent()
        testCodeCallExpression(code, 0, Checks.PyyamlUnsafeLoadCheck, "test.py", PyyamlLoadInspection())
    }

    @Test
    fun `test yaml safe_load`(){
        val code = """
            import yaml
            yaml.safe_load()
        """.trimIndent()
        testCodeCallExpression(code, 0, Checks.PyyamlUnsafeLoadCheck, "test.py", PyyamlLoadInspection())
    }

    @Test
    fun `test yaml load with args and safeloader skips`(){
        val code = """
            import yaml
            yaml.load(f, Loader=yaml.SafeLoader)
        """.trimIndent()
        testCodeCallExpression(code, 0, Checks.PyyamlUnsafeLoadCheck, "test.py", PyyamlLoadInspection())
    }

    @Test
    fun `test yaml load with args and invalid safeloader fires`(){
        val code = """
            import yaml
            yaml.load(f, Loader=None)
        """.trimIndent()
        testCodeCallExpression(code, 1, Checks.PyyamlUnsafeLoadCheck, "test.py", PyyamlLoadInspection())
    }

    @Test
    fun `test yaml load with args and normal loader fires`(){
        val code = """
            import yaml
            yaml.load(f, Loader=yaml.Loader)
        """.trimIndent()
        testCodeCallExpression(code, 1, Checks.PyyamlUnsafeLoadCheck, "test.py", PyyamlLoadInspection())
    }
}