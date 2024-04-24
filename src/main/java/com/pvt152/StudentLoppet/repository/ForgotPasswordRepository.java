package com.pvt152.StudentLoppet.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pvt152.StudentLoppet.model.ForgotPassword;
import com.pvt152.StudentLoppet.model.User;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {

    @Query("SELECT f FROM ForgotPassword f WHERE f.otp = :otp AND f.user = :user")
    Optional<ForgotPassword> findByUserAndToken(Integer otp, User user);
}