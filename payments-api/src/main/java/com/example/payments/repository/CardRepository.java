package com.example.payments.repository;

import com.example.payments.entity.Card;
import com.example.payments.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    <T> List<T> findByUser(User user, Class<T> type);
    <T> Optional<T> findById(Long id, Class<T> type);
    <T> Optional<T> findByCardNumber(String number, Class<T> type);
}
