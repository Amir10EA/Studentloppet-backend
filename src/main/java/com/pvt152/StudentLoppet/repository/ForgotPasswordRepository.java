package com.pvt152.StudentLoppet.repository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pvt152.StudentLoppet.model.ForgotPassword;
import com.pvt152.StudentLoppet.model.User;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {

    @Query("select fp from ForgotPassword fp where fp.otp =?1 and fp.user =?2")
    Optional<ForgotPassword> findByUserAndToken(Integer otp, User user);

    @Query("select fp from ForgotPassword fp where fp.user =?1")
    ForgotPassword findByUser(User user);
}