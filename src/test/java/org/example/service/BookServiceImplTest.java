package org.example.service;

import org.example.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BookServiceImplTest {

    private BookServiceImpl service = BookServiceImpl.getInstance();

    @Test
    void testFindById_ShouldThrowException_WhenArgumentIsNull() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.findById(null));
    }

}
