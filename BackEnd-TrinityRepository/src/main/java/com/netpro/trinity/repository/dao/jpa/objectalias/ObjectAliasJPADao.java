package com.netpro.trinity.repository.dao.jpa.objectalias;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.entity.objectalias.jpa.ObjectAlias;
import com.netpro.trinity.repository.entity.objectalias.jpa.ObjectAliasPKs;

@Repository  //宣告這是一個DAO類別
public interface ObjectAliasJPADao extends JpaRepository<ObjectAlias, ObjectAliasPKs> {
	@Query("select count(oa)>0 from objectalias oa where oa.objectuid=:objectuid AND 1=1")
	Boolean existByObjectuid(@Param("objectuid") String objectuid);
}	