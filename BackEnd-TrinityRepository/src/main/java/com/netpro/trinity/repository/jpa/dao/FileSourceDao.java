package com.netpro.trinity.repository.jpa.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.netpro.trinity.repository.jpa.entity.FileSource;

@Repository  //宣告這是一個DAO類別
public interface FileSourceDao extends JpaRepository<FileSource, String> {
	@Query("select count(filesource)>0 from Filesource filesource where filesource.filesourcename=:filesourcename")
	Boolean existByName(@Param("filesourcename") String filesourcename);
	
	//file source name field
	List<FileSource> findByfilesourcename(String name);
	List<FileSource> findByfilesourcename(String name, Sort sort);
	Page<FileSource> findByfilesourcename(String name, Pageable pageable);
	List<FileSource> findByfilesourcenameIgnoreCase(String name);
	List<FileSource> findByfilesourcenameIgnoreCase(String name, Sort sort);
	Page<FileSource> findByfilesourcenameIgnoreCase(String name, Pageable pageable);
	List<FileSource> findByfilesourcenameLike(String name);
	List<FileSource> findByfilesourcenameLike(String name, Sort sort);
	Page<FileSource> findByfilesourcenameLike(String name, Pageable pageable);
	List<FileSource> findByfilesourcenameLikeIgnoreCase(String name);
	List<FileSource> findByfilesourcenameLikeIgnoreCase(String name, Sort sort);
	Page<FileSource> findByfilesourcenameLikeIgnoreCase(String name, Pageable pageable);
	
	//description field
	List<FileSource> findBydescription(String description);
	List<FileSource> findBydescription(String description, Sort sort);
	Page<FileSource> findBydescription(String description, Pageable pageable);
	List<FileSource> findBydescriptionIgnoreCase(String description);
	List<FileSource> findBydescriptionIgnoreCase(String description, Sort sort);
	Page<FileSource> findBydescriptionIgnoreCase(String description, Pageable pageable);
	List<FileSource> findBydescriptionLike(String description);
	List<FileSource> findBydescriptionLike(String description, Sort sort);
	Page<FileSource> findBydescriptionLike(String description, Pageable pageable);
	List<FileSource> findBydescriptionLikeIgnoreCase(String description);
	List<FileSource> findBydescriptionLikeIgnoreCase(String description, Sort sort);
	Page<FileSource> findBydescriptionLikeIgnoreCase(String description, Pageable pageable);
}