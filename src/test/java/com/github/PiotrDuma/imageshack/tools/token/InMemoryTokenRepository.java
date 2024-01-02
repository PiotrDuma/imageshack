package com.github.PiotrDuma.imageshack.tools.token;

import com.github.PiotrDuma.imageshack.NoImplementationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;

public class InMemoryTokenRepository implements TokenRepositoryJpa{
    private final Map<Long, TokenEntity> database = new HashMap<>();

    @Override
    public TokenEntity save(TokenEntity token) {
        database.put((long)database.size()+1, token);
        return token;
    }

    @Override
    public Optional<TokenEntity> findByPublicId(UUID publicId) {
        return database.values().stream()
                .filter(e -> e.getTokenId().equals(publicId))
                .findAny();
    }

    @Override
    public List<TokenEntity> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public List<TokenEntity> findAll(Sort sort) {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }

    @Override
    public List<TokenEntity> findAllById(Iterable<Long> longs) {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }

    @Override
    public <S extends TokenEntity> List<S> saveAll(Iterable<S> entities) {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }

    @Override
    public void flush()  {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }

    @Override
    public <S extends TokenEntity> S saveAndFlush(S entity) {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }

    @Override
    public <S extends TokenEntity> List<S> saveAllAndFlush(Iterable<S> entities) {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }

    @Override
    public void deleteAllInBatch(Iterable<TokenEntity> entities)  {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs)  {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }

    @Override
    public void deleteAllInBatch()  {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }

    @Override
    public TokenEntity getOne(Long aLong) {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }

    @Override
    public TokenEntity getById(Long aLong) {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }

    @Override
    public <S extends TokenEntity> List<S> findAll(Example<S> example) {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }

    @Override
    public <S extends TokenEntity> List<S> findAll(Example<S> example, Sort sort) {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }

    @Override
    public Page<TokenEntity> findAll(Pageable pageable) {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }

    @Override
    public Optional<TokenEntity> findById(Long aLong)  {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }

    @Override
    public boolean existsById(Long aLong)  {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }

    @Override
    public long count()  {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }

    @Override
    public void deleteById(Long aLong)  {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }

    @Override
    public void delete(TokenEntity entity)  {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs)  {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }

    @Override
    public void deleteAll(Iterable<? extends TokenEntity> entities)  {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }

    @Override
    public void deleteAll()  {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }

    @Override
    public <S extends TokenEntity> Optional<S> findOne(Example<S> example)  {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }

    @Override
    public <S extends TokenEntity> Page<S> findAll(Example<S> example, Pageable pageable) {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }

    @Override
    public <S extends TokenEntity> long count(Example<S> example) {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }

    @Override
    public <S extends TokenEntity> boolean exists(Example<S> example)  {
        throw new NoImplementationException("Missing TokenRepository's method implementation");
    }
}
