package com.netpro.trinity.repository.notification.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.notification.entity.Notification;

@Repository  //宣告這是一個DAO類別
public interface NotificationJPADao extends JpaRepository<Notification, String> {
	@Query("select count(n)>0 from Notification n where n.notificationname=:notificationname AND n.targetuid=:targetuid AND 1=1")
	Boolean existByNameAndTargetUid(@Param("notificationname") String notificationname, @Param("targetuid") String targetuid);
	
	//notificationname field
	List<Notification> findBynotificationname(String name);
	List<Notification> findBynotificationname(String name, Sort sort);
	Page<Notification> findBynotificationname(String name, Pageable pageable);
	List<Notification> findBynotificationnameIgnoreCase(String name);
	List<Notification> findBynotificationnameIgnoreCase(String name, Sort sort);
	Page<Notification> findBynotificationnameIgnoreCase(String name, Pageable pageable);
	List<Notification> findBynotificationnameLike(String name);
	List<Notification> findBynotificationnameLike(String name, Sort sort);
	Page<Notification> findBynotificationnameLike(String name, Pageable pageable);
	List<Notification> findBynotificationnameLikeIgnoreCase(String name);
	List<Notification> findBynotificationnameLikeIgnoreCase(String name, Sort sort);
	Page<Notification> findBynotificationnameLikeIgnoreCase(String name, Pageable pageable);
	
	//description field
	List<Notification> findBydescription(String description);
	List<Notification> findBydescription(String description, Sort sort);
	Page<Notification> findBydescription(String description, Pageable pageable);
	List<Notification> findBydescriptionIgnoreCase(String description);
	List<Notification> findBydescriptionIgnoreCase(String description, Sort sort);
	Page<Notification> findBydescriptionIgnoreCase(String description, Pageable pageable);
	List<Notification> findBydescriptionLike(String description);
	List<Notification> findBydescriptionLike(String description, Sort sort);
	Page<Notification> findBydescriptionLike(String description, Pageable pageable);
	List<Notification> findBydescriptionLikeIgnoreCase(String description);
	List<Notification> findBydescriptionLikeIgnoreCase(String description, Sort sort);
	Page<Notification> findBydescriptionLikeIgnoreCase(String description, Pageable pageable);
}

