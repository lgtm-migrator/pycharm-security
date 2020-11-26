package security.validators

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import security.Checks
import security.SecurityTestTask

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SubprocessShellModeInspectionTest: SecurityTestTask() {
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
        assertFalse(SubprocessShellModeInspection().staticDescription.isNullOrEmpty())
    }

    @Test
    fun `test subprocess call with shell mode`(){
        val code = """
            import subprocess
            subprocess.call(shell=True)
        """.trimIndent()
        testCodeCallExpression(code, 1, Checks.SubprocessShellCheck, "test.py", SubprocessShellModeInspection())
    }

    @Test
    fun `test subprocess call with shell mode ref arg`(){
        val code = """
            import subprocess
            subprocess.call(x, shell=True)
        """.trimIndent()
        testCodeCallExpression(code, 1, Checks.SubprocessShellCheck, "test.py", SubprocessShellModeInspection())
    }

    @Test
    fun `test subprocess call with shell mode list ref arg`(){
        val code = """
            import subprocess
            subprocess.call([x], shell=True)
        """.trimIndent()
        testCodeCallExpression(code, 1, Checks.SubprocessShellCheck, "test.py", SubprocessShellModeInspection())
    }

    @Test
    fun `test subprocess call with shell mode string literal arg`(){
        val code = """
            import subprocess
            subprocess.call('test', shell=True)
        """.trimIndent()
        testCodeCallExpression(code, 0, Checks.SubprocessShellCheck, "test.py", SubprocessShellModeInspection())
    }

    @Test
    fun `test subprocess call with shell mode list literal arg`(){
        val code = """
            import subprocess
            subprocess.call(['test', 'x'], shell=True)
        """.trimIndent()
        testCodeCallExpression(code, 0, Checks.SubprocessShellCheck, "test.py", SubprocessShellModeInspection())
    }

    @Test
    fun `test subprocess call with shell mixed list arg`(){
        val code = """
            import subprocess
            subprocess.call(['test', x], shell=True)
        """.trimIndent()
        testCodeCallExpression(code, 1, Checks.SubprocessShellCheck, "test.py", SubprocessShellModeInspection())
    }

    @Test
    fun `test subprocess call with escaped arg`(){
        val code = """
            import subprocess
            import shlex
            subprocess.call(shlex.quote(x), shell=True)
        """.trimIndent()
        testCodeCallExpression(code, 0, Checks.SubprocessShellCheck, "test.py", SubprocessShellModeInspection())
    }

    @Test
    fun `test subprocess call with escaped list arg`(){
        val code = """
            import subprocess
            import shlex
            subprocess.call([shlex.quote(x)], shell=True)
        """.trimIndent()
        testCodeCallExpression(code, 0, Checks.SubprocessShellCheck, "test.py", SubprocessShellModeInspection())
    }

    @Test
    fun `test subprocess call with other func call`(){
        val code = """
            import subprocess
            import shlex
            subprocess.call([something(x)], shell=True)
        """.trimIndent()
        testCodeCallExpression(code, 1, Checks.SubprocessShellCheck, "test.py", SubprocessShellModeInspection())
    }

    @Test
    fun `test subprocess Popen with shell mode`(){
        val code = """
            import subprocess
            subprocess.Popen(shell=True)
        """.trimIndent()
        testCodeCallExpression(code, 1, Checks.SubprocessShellCheck, "test.py", SubprocessShellModeInspection())
    }

    @Test
    fun `test subprocess run with shell mode`(){
        val code = """
            import subprocess
            subprocess.run(shell=True)
        """.trimIndent()
        testCodeCallExpression(code, 1, Checks.SubprocessShellCheck, "test.py", SubprocessShellModeInspection())
    }

    @Test
    fun `test subprocess run with shell mode off`(){
        val code = """
            import subprocess
            subprocess.run(shell=False)
        """.trimIndent()
        testCodeCallExpression(code, 0, Checks.SubprocessShellCheck, "test.py", SubprocessShellModeInspection())
    }

    @Test
    fun `test subprocess run with shell mode non-bool`(){
        val code = """
            import subprocess
            subprocess.run(shell=is_shell())
        """.trimIndent()
        testCodeCallExpression(code, 0, Checks.SubprocessShellCheck, "test.py", SubprocessShellModeInspection())
    }

    @Test
    fun `test subprocess run with no shell arg`(){
        val code = """
            import subprocess
            subprocess.run('zxx')
        """.trimIndent()
        testCodeCallExpression(code, 0, Checks.SubprocessShellCheck, "test.py", SubprocessShellModeInspection())
    }

    @Test
    fun `test subprocess other function`(){
        val code = """
            import subprocess
            subprocess.fun('zxx')
        """.trimIndent()
        testCodeCallExpression(code, 0, Checks.SubprocessShellCheck, "test.py", SubprocessShellModeInspection())
    }

    @Test
    fun `test normal subprocess call`(){
        val code = """
            import subprocess
            subprocess.call()
        """.trimIndent()
        testCodeCallExpression(code, 0, Checks.SubprocessShellCheck, "test.py", SubprocessShellModeInspection())
    }
}