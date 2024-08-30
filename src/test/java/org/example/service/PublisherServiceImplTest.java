package org.example.service;

import org.example.dto.AuthorDto;
import org.example.dto.PublisherDto;
import org.example.entity.Author;
import org.example.entity.Publisher;
import org.example.mapper.PublisherMapper;
import org.example.repository.impl.PublisherRepositoryImpl;
import org.example.service.impl.PublisherServiceImpl;
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
class PublisherServiceImplTest {

    @Mock
    private PublisherRepositoryImpl repository;
    private PublisherMapper mapper = PublisherMapper.getInstance();
    @InjectMocks
    private PublisherServiceImpl service;

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
        Publisher testPublisher = Instancio.create(Publisher.class);
        Mockito.doReturn(testPublisher).when(repository).findById(Mockito.anyInt());

        PublisherDto expectedPublisherDto = mapper.mapFrom(testPublisher);
        PublisherDto actualPublisherDto = service.findById(1);

        assertEquals(expectedPublisherDto, actualPublisherDto);
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
        PublisherDto actualAuthorDto = service.findById(255);

        assertNull(actualAuthorDto);
    }

    @Test
    @DisplayName("findAll returns expected objects when called")
    void testFindAll_ShouldReturnExpectedDTOs_WhenCalled() {
        List<Publisher> authors = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            authors.add(Instancio.create(Publisher.class));
        }

        Mockito.doReturn(authors).when(repository).findAll();

        List<PublisherDto> expectedPublisherDtos = authors.stream().map(mapper::mapFrom).toList();
        List<PublisherDto> actualPublisherDtos = service.findAll();

        assertEquals(expectedPublisherDtos, actualPublisherDtos);
    }

    @Test
    @DisplayName("findAll should address repository")
    void testFindAll_ShouldAddressRepository_WhenCalled() {
        Mockito.doReturn(List.of()).when(repository).findAll();
        service.findAll();

        Mockito.verify(repository, Mockito.times(1)).findAll();
    }

}
