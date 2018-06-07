package com.netpro.trinity.repository.configuration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.configuration.entity.Monitorconfig;

@Repository  //宣告這是一個DAO類別
public interface MonitorconfigJPADao extends JpaRepository<Monitorconfig, String> {  //自動繼承JapRepository下的所有方法

}