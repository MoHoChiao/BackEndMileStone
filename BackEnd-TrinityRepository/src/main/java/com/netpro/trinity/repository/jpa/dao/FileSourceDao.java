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
	
	//file source uid field
	List<FileSource> findByFilesourceuidIn(List<String> uids);
	List<FileSource> findByFilesourceuidIn(List<String> uids, Sort sort);
	List<FileSource> findByFilesourceuidNotIn(List<String> uids, Sort sort);
	Page<FileSource> findByFilesourceuidIn(List<String> uids, Pageable pageable);
	
	
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
	//file source name field with Filesourceuid constraints
	List<FileSource> findByfilesourcenameAndFilesourceuidIn(String name, List<String> uids);
	List<FileSource> findByfilesourcenameAndFilesourceuidIn(String name, Sort sort, List<String> uids);
	Page<FileSource> findByfilesourcenameAndFilesourceuidIn(String name, Pageable pageable, List<String> uids);
	List<FileSource> findByfilesourcenameIgnoreCaseAndFilesourceuidIn(String name, List<String> uids);
	List<FileSource> findByfilesourcenameIgnoreCaseAndFilesourceuidIn(String name, Sort sort, List<String> uids);
	Page<FileSource> findByfilesourcenameIgnoreCaseAndFilesourceuidIn(String name, Pageable pageable, List<String> uids);
	List<FileSource> findByfilesourcenameLikeAndFilesourceuidIn(String name, List<String> uids);
	List<FileSource> findByfilesourcenameLikeAndFilesourceuidIn(String name, Sort sort, List<String> uids);
	Page<FileSource> findByfilesourcenameLikeAndFilesourceuidIn(String name, Pageable pageable, List<String> uids);
	List<FileSource> findByfilesourcenameLikeIgnoreCaseAndFilesourceuidIn(String name, List<String> uids);
	List<FileSource> findByfilesourcenameLikeIgnoreCaseAndFilesourceuidIn(String name, Sort sort, List<String> uids);
	Page<FileSource> findByfilesourcenameLikeIgnoreCaseAndFilesourceuidIn(String name, Pageable pageable, List<String> uids);
	
	
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
	//description field with Filesourceuid constraints
	List<FileSource> findBydescriptionAndFilesourceuidIn(String description, List<String> uids);
	List<FileSource> findBydescriptionAndFilesourceuidIn(String description, Sort sort, List<String> uids);
	Page<FileSource> findBydescriptionAndFilesourceuidIn(String description, Pageable pageable, List<String> uids);
	List<FileSource> findBydescriptionIgnoreCaseAndFilesourceuidIn(String description, List<String> uids);
	List<FileSource> findBydescriptionIgnoreCaseAndFilesourceuidIn(String description, Sort sort, List<String> uids);
	Page<FileSource> findBydescriptionIgnoreCaseAndFilesourceuidIn(String description, Pageable pageable, List<String> uids);
	List<FileSource> findBydescriptionLikeAndFilesourceuidIn(String description, List<String> uids);
	List<FileSource> findBydescriptionLikeAndFilesourceuidIn(String description, Sort sort, List<String> uids);
	Page<FileSource> findBydescriptionLikeAndFilesourceuidIn(String description, Pageable pageable, List<String> uids);
	List<FileSource> findBydescriptionLikeIgnoreCaseAndFilesourceuidIn(String description, List<String> uids);
	List<FileSource> findBydescriptionLikeIgnoreCaseAndFilesourceuidIn(String description, Sort sort, List<String> uids);
	Page<FileSource> findBydescriptionLikeIgnoreCaseAndFilesourceuidIn(String description, Pageable pageable, List<String> uids);
}

