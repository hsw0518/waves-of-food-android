package com.example.myapplication

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginInputValidatorTest {
    @Test fun acceptsEmailAndInternationalPhoneNumbers() {
        listOf("hello@example.com", "  student.name+food@university.ac.kr  ",
            "+82 10-1234-5678", "01012345678", "(202) 555-0123")
            .forEach { assertTrue(it, LoginInputValidator.isValidIdentifier(it)) }
    }

    @Test fun rejectsMissingAndMalformedIdentifiers() {
        listOf("", " ", "hello", "hello@", "@example.com", "a..b@example.com",
            "a@example..com", "a@-example.com", "a b@example.com", "12345", "+821012345678901234", "010-food-1234")
            .forEach { assertFalse(it, LoginInputValidator.isValidIdentifier(it)) }
    }
}
