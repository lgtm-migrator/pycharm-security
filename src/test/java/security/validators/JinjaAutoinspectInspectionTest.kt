package security.validators

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import security.Checks
import security.SecurityTestTask

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JinjaAutoinspectInspectionTest: SecurityTestTask() {
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
        assertFalse(JinjaAutoinspectInspection().staticDescription.isNullOrEmpty())
    }

    @Test
    fun `test jinja2 no autoescape`(){
        val code = """
            import jinja2
            env = jinja2.Environment()
        """.trimIndent()
        testCodeCallExpression(code, 1, Checks.JinjaAutoinspectCheck, "test.py", JinjaAutoinspectInspection())
    }

    @Test
    fun `test jinja2 autoescape is false`(){
        val code = """
            import jinja2
            env = jinja2.Environment(autoescape=False)
        """.trimIndent()
        testCodeCallExpression(code, 1, Checks.JinjaAutoinspectCheck, "test.py", JinjaAutoinspectInspection())
    }

    @Test
    fun `test jinja2 autoescape is true`(){
        val code = """
            import jinja2
            env = jinja2.Environment(autoescape=True)
        """.trimIndent()
        testCodeCallExpression(code, 0, Checks.JinjaAutoinspectCheck, "test.py", JinjaAutoinspectInspection())
    }

    @Test
    fun `test jinja2 with existin autoescape to selector function`(){
        val code = """
            import jinja2
            env = jinja2.Environment(autoescape=jinja2.select_autoescape(
                enabled_extensions=('html', 'xml'),
                default_for_string=True,
            ))
        """.trimIndent()
        testCodeCallExpression(code, 0, Checks.JinjaAutoinspectCheck, "test.py", JinjaAutoinspectInspection())
    }

    @Test
    fun `test jinja2 no autoescape on template`(){
        val code = """
            import jinja2
            env = jinja2.Template()
        """.trimIndent()
        testCodeCallExpression(code, 1, Checks.JinjaAutoinspectCheck, "test.py", JinjaAutoinspectInspection())
    }

    @Test
    fun `test jinja2 autoescape is false on template`(){
        val code = """
            import jinja2
            env = jinja2.Template(autoescape=False)
        """.trimIndent()
        testCodeCallExpression(code, 1, Checks.JinjaAutoinspectCheck, "test.py", JinjaAutoinspectInspection())
    }

    @Test
    fun `test jinja2 autoescape is true on template`(){
        val code = """
            import jinja2
            env = jinja2.Template(autoescape=True)
        """.trimIndent()
        testCodeCallExpression(code, 0, Checks.JinjaAutoinspectCheck, "test.py", JinjaAutoinspectInspection())
    }

    @Test
    fun `test jinja2 with existin autoescape to selector function on Template`(){
        val code = """
            import jinja2
            env = jinja2.Template(autoescape=jinja2.select_autoescape(
                enabled_extensions=('html', 'xml'),
                default_for_string=True,
            ))
        """.trimIndent()
        testCodeCallExpression(code, 0, Checks.JinjaAutoinspectCheck, "test.py", JinjaAutoinspectInspection())
    }

    @Test
    fun `test jinja2 with not template call`(){
        val code = """
            import jinja2
            env = jinja2.Templateeeee(autoescape=jinja2.select_autoescape(
                enabled_extensions=('html', 'xml'),
                default_for_string=True,
            ))
        """.trimIndent()
        testCodeCallExpression(code, 0, Checks.JinjaAutoinspectCheck, "test.py", JinjaAutoinspectInspection())
    }

    @Test
    fun `test jinja2 with not jinja qn`(){
        val code = """
            import jinja2000
            env = jinja2000.Template(autoescape=jinja2.select_autoescape(
                enabled_extensions=('html', 'xml'),
                default_for_string=True,
            ))
        """.trimIndent()
        testCodeCallExpression(code, 0, Checks.JinjaAutoinspectCheck, "test.py", JinjaAutoinspectInspection())
    }

    @Test
    fun `test jinja2 autoescape non bool`(){
        val code = """
            import jinja2
            env = jinja2.Template(autoescape='banana')
        """.trimIndent()
        testCodeCallExpression(code, 0, Checks.JinjaAutoinspectCheck, "test.py", JinjaAutoinspectInspection())
    }
}