package org.example.service.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.dto.PublisherDto;
import org.example.entity.Publisher;
import org.example.mapper.PublisherMapper;
import org.example.repository.PublisherRepository;
import org.example.repository.impl.PublisherRepositoryImpl;
import org.example.service.PublisherService;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PublisherServiceImpl implements PublisherService {

    @Getter
    private static final PublisherServiceImpl instance = new PublisherServiceImpl();

    private PublisherRepository repository = PublisherRepositoryImpl.getInstance();
    private final PublisherMapper mapper = PublisherMapper.getInstance();

    @Override
    public List<PublisherDto> findAll() {
        return repository.findAll().stream()
                .map(mapper::mapFrom)
                .toList();
    }

    @Override
    public PublisherDto findById(Integer id) {
        if (id == null || id < 1) throw new IllegalArgumentException();

        Publisher publisher = repository.findById(id);
        if (publisher == null) return null;

        return mapper.mapFrom(publisher);
    }
}
