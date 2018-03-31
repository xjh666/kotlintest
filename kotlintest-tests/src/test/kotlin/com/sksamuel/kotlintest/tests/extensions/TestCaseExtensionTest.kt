package com.sksamuel.kotlintest.tests.extensions

import io.kotlintest.Project
import io.kotlintest.TestCase
import io.kotlintest.extensions.TestCaseExtension
import io.kotlintest.specs.WordSpec
import io.kotlintest.shouldBe
import java.util.concurrent.atomic.AtomicInteger

object Numbers {

  val a = AtomicInteger(1)
  val b = AtomicInteger(1)

  val add1 = object : TestCaseExtension {
    override fun intercept(testCase: TestCase, test: () -> Unit) {
      if (testCase.name().contains("ZZQQ")) {
        a.addAndGet(1)
        test()
        b.addAndGet(1)
      }
    }
  }

  val add2 = object : TestCaseExtension {
    override fun intercept(testCase: TestCase, test: () -> Unit) {
      if (testCase.name().contains("ZZQQ")) {
        a.addAndGet(2)
        test()
        b.addAndGet(2)
      }
    }
  }

  init {
    Project.registerExtension(add1)
    Project.registerExtension(add2)
  }
}

class TestCaseExtensionTest : WordSpec() {

  init {

    "TestCaseExtensions" should {
      "be activated by registration with ProjectExtensions ZZQQ" {
        // the sum and mult before calling test() should have fired
        Numbers.a.get() shouldBe 4
        Numbers.b.get() shouldBe 1
      }
      "use around advice ZZQQ" {
        // in this second test, both the after from the previous test, and the before of this test should have fired
        Numbers.a.get() shouldBe 7
        Numbers.b.get() shouldBe 4
      }
      "use extensions registered on config ZZQQ" {
        Numbers.a.get() shouldBe 11
      }.config(extensions = listOf(Numbers.add1))
    }
  }
}