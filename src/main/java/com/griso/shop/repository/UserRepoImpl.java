package com.griso.shop.repository;

import com.griso.shop.entities.UserDB;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public class UserRepoImpl implements IUserRepo {
    @Override
    public Optional<UserDB> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public <S extends UserDB> S save(S s) {
        return null;
    }

    @Override
    public <S extends UserDB> List<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<UserDB> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<UserDB> findAll() {
        return null;
    }

    @Override
    public Iterable<UserDB> findAllById(Iterable<String> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(UserDB userDB) {

    }

    @Override
    public void deleteAll(Iterable<? extends UserDB> iterable) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<UserDB> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<UserDB> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends UserDB> S insert(S s) {
        return null;
    }

    @Override
    public <S extends UserDB> List<S> insert(Iterable<S> iterable) {
        return null;
    }

    @Override
    public <S extends UserDB> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends UserDB> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends UserDB> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends UserDB> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends UserDB> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends UserDB> boolean exists(Example<S> example) {
        return false;
    }
}
