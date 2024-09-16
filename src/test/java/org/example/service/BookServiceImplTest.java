package org.example.service;

import org.example.dto.BookDto;
import org.example.entity.Book;
import org.example.mapper.BookMapper;
import org.example.repository.impl.BookRepositoryImpl;
import org.example.service.impl.BookServiceImpl;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepositoryImpl repository;
    private BookMapper mapper = BookMapper.getInstance();
    @InjectMocks
    private BookServiceImpl service;

    @Test
    @DisplayName("findById throws exception when argument is null")
    void testFindById_ShouldThrowException_WhenArgIsNull() {
        assertThrows(IllegalArgumentException.class, () -> service.findById(null));
    }

    @Test
    @DisplayName("findById throws exception when argument is zero or negative")
    void testFindById_ShouldThrowException_WhenArgIsZeroOrNegative() {
        assertThrows(IllegalArgumentException.class, () -> service.findById(0));
        assertThrows(IllegalArgumentException.class, () -> service.findById(-1));
    }

    @Test
    @DisplayName("findById returns expected object when called")
    void testFindById_ShouldReturnExpectedDTO_WhenCalled() {
        Book testBook = Instancio.create(Book.class);
        Mockito.doReturn(testBook).when(repository).findById(Mockito.anyInt());

        BookDto expectedBookDto = mapper.mapFrom(testBook);
        BookDto actualBookDto = service.findById(1);

        assertEquals(expectedBookDto, actualBookDto);
    }

    @Test
    @DisplayName("findById should address repository")
    void testFindById_ShouldAddressRepository_WhenCalled() {
        Mockito.doReturn(null).when(repository).findById(Mockito.anyInt());
        service.findById(1);

        Mockito.verify(repository, Mockito.times(1)).findById(Mockito.anyInt());
    }

    @Test
    @DisplayName("findById should return null when object is not in the database")
    void testFindById_ShouldReturnNull_WhenObjectIsNotInTheDB() {
        Mockito.doReturn(null).when(repository).findById(255);
        BookDto actualBookDto = service.findById(255);

        assertNull(actualBookDto);
    }

    @Test
    @DisplayName("findAll returns expected objects when called")
    void testFindAll_ShouldReturnExpectedDTOs_WhenCalled() {
        List<Book> authors = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            authors.add(Instancio.create(Book.class));
        }

        Mockito.doReturn(authors).when(repository).findAll();

        List<BookDto> expectedBookDtos = authors.stream().map(mapper::mapFrom).toList();
        List<BookDto> actualBookDtos = service.findAll();

        assertEquals(expectedBookDtos, actualBookDtos);
    }

    @Test
    @DisplayName("findAll should address repository")
    void testFindAll_ShouldAddressRepository_WhenCalled() {
        Mockito.doReturn(List.of()).when(repository).findAll();
        service.findAll();

        Mockito.verify(repository, Mockito.times(1)).findAll();
    }
}
